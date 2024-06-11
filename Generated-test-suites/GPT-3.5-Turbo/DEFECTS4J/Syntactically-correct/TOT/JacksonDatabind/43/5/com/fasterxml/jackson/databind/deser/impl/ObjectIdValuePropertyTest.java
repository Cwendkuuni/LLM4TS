import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {
    @Mock
    private ObjectIdReader objectIdReader;
    @Mock
    private PropertyMetadata metadata;
    @Mock
    private JsonDeserializer<?> deserializer;
    @Mock
    private PropertyName newName;
    @Mock
    private JsonParser p;
    @Mock
    private DeserializationContext ctxt;
    @Mock
    private Object instance;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
        assertNotNull(result);
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(deserializer);
        assertNotNull(result);
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);

        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);

        verify(deserializer).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);

        objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(deserializer).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn_NullId() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(p, ctxt)).thenReturn(null);

        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn_IdPropertyNotNull() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(p, ctxt)).thenReturn("id");

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, "id")).thenReturn("result");

        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(idProperty).setAndReturn(instance, "id");
        assertEquals("result", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_IdPropertyNull() throws IOException {
        objectIdValueProperty.setAndReturn(instance, "value");
    }
}