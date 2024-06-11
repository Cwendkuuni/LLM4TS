import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty property = (ObjectIdValueProperty) objectIdValueProperty.withName(newName);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = (ObjectIdValueProperty) objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty property = (ObjectIdValueProperty) objectIdValueProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, property.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);
        assertNull(objectIdValueProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        verify(objectIdValueProperty).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object result = mock(Object.class);
        when(objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance)).thenReturn(result);
        assertEquals(result, objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        objectIdValueProperty.set(instance, value);
        verify(objectIdValueProperty).setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        Object result = mock(Object.class);
        when(objectIdValueProperty.setAndReturn(instance, value)).thenReturn(result);
        assertEquals(result, objectIdValueProperty.setAndReturn(instance, value));
    }
}