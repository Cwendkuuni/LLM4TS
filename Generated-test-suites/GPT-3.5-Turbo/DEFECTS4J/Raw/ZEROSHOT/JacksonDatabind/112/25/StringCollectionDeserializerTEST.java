import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private JavaType collectionType;
    private JsonDeserializer<String> valueDeser;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeser;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() {
        collectionType = mock(JavaType.class);
        valueDeser = mock(JsonDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeser = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        deserializer = new StringCollectionDeserializer(collectionType, valueDeser, valueInstantiator);
        deserializer = new StringCollectionDeserializer(collectionType, valueInstantiator, delegateDeser,
                valueDeser, nullValueProvider, Boolean.TRUE);
    }

    @Test
    public void testIsCachable() {
        assertTrue(deserializer.isCachable());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> deserializer2 = mock(JsonDeserializer.class);

        when(valueInstantiator.getDelegateCreator()).thenReturn(mock(AnnotatedWithParams.class));
        when(valueInstantiator.getDelegateType(any())).thenReturn(mock(JavaType.class));
        when(ctxt.getConfig()).thenReturn(mock(DeserializationConfig.class));
        when(ctxt.handleSecondaryContextualization(eq(valueDeser), eq(property),
                any())).thenReturn(deserializer2);
        when(ctxt.findContextualValueDeserializer(any(), eq(property))).thenReturn(deserializer2);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertSame(deserializer, result);
        verify(valueInstantiator).getDelegateCreator();
        verify(valueInstantiator).getDelegateType(any());
        verify(ctxt, times(2)).getConfig();
        verify(ctxt).handleSecondaryContextualization(eq(valueDeser), eq(property),
                any());
        verify(ctxt).findContextualValueDeserializer(any(), eq(property));
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<Object> result = deserializer.getContentDeserializer();
        assertSame(valueDeser, result);
    }

    @Test
    public void testGetValueInstantiator() {
        ValueInstantiator result = deserializer.getValueInstantiator();
        assertSame(valueInstantiator, result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();

        when(delegateDeser.deserialize(eq(p), eq(ctxt))).thenReturn(new ArrayList<String>());
        when(valueInstantiator.createUsingDelegate(eq(ctxt),
                anyListOf(Object.class))).thenReturn(new ArrayList<String>());
        when(valueInstantiator.createUsingDefault(eq(ctxt))).thenReturn(new ArrayList<String>());
        when(p.isExpectedStartArrayToken()).thenReturn(false);

        Collection<String> deserialized = deserializer.deserialize(p, ctxt);
        assertNotNull(deserialized);
        assertEquals(result, deserialized);

        verify(delegateDeser).deserialize(eq(p), eq(ctxt));
        verify(valueInstantiator).createUsingDelegate(eq(ctxt),
                anyListOf(Object.class));
        verify(valueInstantiator).createUsingDefault(eq(ctxt));
        verify(p).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        List<String> result = new ArrayList<>();

        when(delegateDeser.deserialize(eq(p), eq(ctxt))).thenReturn(new ArrayList<String>());
        when(valueInstantiator.createUsingDelegate(eq(ctxt),
                anyListOf(Object.class))).thenReturn(new ArrayList<String>());
        when(p.isExpectedStartArrayToken()).thenReturn(true);
        when(p.nextTextValue()).thenReturn("test", null);

        Collection<String> deserialized = deserializer.deserialize(p, ctxt, result);
        assertNotNull(deserialized);
        assertEquals(result, deserialized);

        verify(delegateDeser).deserialize(eq(p), eq(ctxt));
        verify(valueInstantiator).createUsingDelegate(eq(ctxt),
                anyListOf(Object.class));
        verify(p, times(2)).nextTextValue();
        verify(p, times(2)).getCurrentToken();
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);

        when(p.nextTextValue()).thenReturn("test", null);
        when(deser.deserialize(eq(p), eq(ctxt))).thenReturn("test");

        Collection<String> deserialized = deserializer.deserializeUsingCustom(p, ctxt, result, deser);
        assertNotNull(deserialized);
        assertEquals(result, deserialized);

        verify(p, times(2)).nextTextValue();
        verify(deser, times(2)).deserialize(eq(p), eq(ctxt));
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();

        when(p.isExpectedStartArrayToken()).thenReturn(false);
        when(valueDeser.deserialize(eq(p), eq(ctxt))).thenReturn("test");

        Collection<String> deserialized = deserializer.handleNonArray(p, ctxt, result);
        assertNotNull(deserialized);
        assertEquals(result, deserialized);

        verify(p).isExpectedStartArrayToken();
        verify(valueDeser).deserialize(eq(p), eq(ctxt));
    }
}