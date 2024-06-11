import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata metadata;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        property.withNullProvider(nva);
        verifyNoMoreInteractions(nva);
    }

    @Test
    public void testWithNullDeserializer() {
        property.withValueDeserializer(null);
        assertNull(property.getValueDeserializer());
    }

    @Test
    public void testWithSameDeserializer() {
        property.withValueDeserializer(deserializer);
        assertSame(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithDifferentDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        property.withValueDeserializer(newDeserializer);
        assertNotSame(deserializer, property.getValueDeserializer());
        assertSame(newDeserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty newProperty = property.withName(newName);
        assertNotSame(property, newProperty);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        assertNull(property.deserializeAndSet(p, ctxt, instance));
        verify(p, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(p, never()).nextToken();
        verifyNoMoreInteractions(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullId() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        verify(p, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(p, never()).nextToken();
        verifyNoMoreInteractions(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNonNullId() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = mock(Object.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(id);
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);

        assertSame(instance, property.deserializeSetAndReturn(p, ctxt, instance));
        verify(p, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(objectIdReader, times(1)).getDeserializer();
        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
        verify(ctxt, times(1)).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        verify(roid, times(1)).bindItem(instance);
        verifyNoMoreInteractions(p, ctxt, instance, objectIdReader, valueDeserializer, roid);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws Exception {
        Object instance = mock(Object.class);
        SettableBeanProperty idProperty = null;
        when(objectIdReader.idProperty).thenReturn(idProperty);

        assertNull(property.setAndReturn(instance, mock(Object.class)));
        verify(objectIdReader, times(1)).idProperty;
        verifyNoMoreInteractions(instance, objectIdReader);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws Exception {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, value)).thenReturn(instance);

        assertSame(instance, property.setAndReturn(instance, value));
        verify(objectIdReader, times(1)).idProperty;
        verify(idProperty, times(1)).setAndReturn(instance, value);
        verifyNoMoreInteractions(instance, objectIdReader, idProperty);
    }
}