import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collection;

public class StringCollectionDeserializerTest {
    @Mock
    private JsonDeserializer<String> valueDeserializer;

    @Mock
    private ValueInstantiator valueInstantiator;

    @Mock
    private JsonDeserializer<Object> delegateDeserializer;

    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deserializer = new StringCollectionDeserializer(
                mock(JavaType.class), valueDeserializer, valueInstantiator);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> result = mock(Collection.class);

        // Mocking the behavior of isExpectedStartArrayToken to return true
        when(parser.isExpectedStartArrayToken()).thenReturn(true);

        deserializer.deserialize(parser, context, result);

        // Verify that the expected methods were called with the correct arguments
        verify(parser).isExpectedStartArrayToken();
        verify(parser).nextTextValue();
        verify(result).add(any(String.class));
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> result = mock(Collection.class);

        // Mocking the behavior of isExpectedStartArrayToken to return true
        when(parser.isExpectedStartArrayToken()).thenReturn(true);

        // Mocking the behavior of the valueDeserializer
        when(parser.nextTextValue()).thenReturn(null).thenReturn("value");

        deserializer = new StringCollectionDeserializer(
                mock(JavaType.class), valueInstantiator, delegateDeserializer,
                valueDeserializer, mock(NullValueProvider.class), false);
        deserializer.deserialize(parser, context, result);

        // Verify that the expected methods were called with the correct arguments
        verify(parser).isExpectedStartArrayToken();
        verify(parser, mock(2)).nextTextValue();
        verify(valueDeserializer).deserialize(eq(parser), eq(context));
        verify(result).add(eq("value"));
    }

    @Test
    public void testDeserializeWithNonArrayToken() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> result = mock(Collection.class);

        // Mocking the behavior of isExpectedStartArrayToken to return false
        when(parser.isExpectedStartArrayToken()).thenReturn(false);

        deserializer.deserialize(parser, context, result);

        // Verify that the expected methods were called with the correct arguments
        verify(parser).isExpectedStartArrayToken();
        verify(context).handleUnexpectedToken(eq(Collection.class), eq(parser));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        deserializer.deserializeWithType(parser, context, typeDeserializer);

        // Verify that the expected methods were called with the correct arguments
        verify(typeDeserializer).deserializeTypedFromArray(eq(parser), eq(context));
    }

    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<String> valueDeser = mock(JsonDeserializer.class);

        // Mocking the behavior of the findContentNullProvider method
        NullValueProvider nuller = mock(NullValueProvider.class);
        doReturn(nuller).when(deserializer).findContentNullProvider(eq(context), eq(property), eq(valueDeser));

        // Mocking the behavior of the handleSecondaryContextualization method
        JsonDeserializer<?> des = mock(JsonDeserializer.class);
        when(context.handleSecondaryContextualization(eq(valueDeser), eq(property), any(JavaType.class))).thenReturn(des);

        // Call the createContextual method
        deserializer.createContextual(context, property);

        // Verify that the expected methods were called with the correct arguments
        verify(deserializer).findContentNullProvider(eq(context), eq(property), eq(valueDeserializer));
        verify(context).handleSecondaryContextualization(eq(valueDeserializer), eq(property), any(JavaType.class));
        verify(deserializer).withResolved(eq(delegateDeserializer), eq(valueDeserializer), eq(nuller), eq(false));
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<String> deser = deserializer.getContentDeserializer();
        assertEquals(valueDeserializer, deser);
    }

    @Test
    public void testGetValueInstantiator() {
        ValueInstantiator instantiator = deserializer.getValueInstantiator();
        assertEquals(valueInstantiator, instantiator);
    }
}