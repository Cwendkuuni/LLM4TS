import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;
    private BeanProperty beanProperty;
    private AnnotatedMethod setter;
    private JavaType javaType;
    private KeyDeserializer keyDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        beanProperty = mock(BeanProperty.class);
        setter = mock(AnnotatedMethod.class);
        javaType = mock(JavaType.class);
        keyDeserializer = mock(KeyDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);

        settableAnyProperty = new SettableAnyProperty(beanProperty, setter, javaType, keyDeserializer,
                valueDeserializer, typeDeserializer);
    }

    @Test
    public void testGetProperty() {
        assertEquals(beanProperty, settableAnyProperty.getProperty());
    }

    @Test
    public void testHasValueDeserializer() {
        assertEquals(true, settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        assertEquals(javaType, settableAnyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "property";

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p, times(1)).getCurrentToken();
        verify(p, times(1)).getNullValue();
        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        settableAnyProperty.withValueDeserializer(deserializer);

        settableAnyProperty.deserialize(p, ctxt);

        verify(p, times(1)).getCurrentToken();
        verify(deserializer, times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object propName = "property";
        Object value = "value";

        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object, Object> val = new HashMap<>();
        val.put(propName, value);
        when(setter instanceof AnnotatedField).thenReturn(true);
        when((AnnotatedField) setter).thenReturn(field);
        when(field.getValue(instance)).thenReturn(val);

        settableAnyProperty.set(instance, propName, value);

        verify(field, times(1)).getValue(instance);
    }

    @Test(expected = IOException.class)
    public void testSet_IOException() throws IOException {
        Object instance = new Object();
        Object propName = "property";
        Object value = "value";
        Exception exception = new Exception();

        AnnotatedField field = mock(AnnotatedField.class);
        when(setter instanceof AnnotatedField).thenReturn(true);
        when((AnnotatedField) setter).thenReturn(field);
        when(field.getValue(instance)).thenThrow(exception);

        settableAnyProperty.set(instance, propName, value);
    }

    @Test
    public void testThrowAsIOE_Exception() throws IOException {
        Object propName = "property";
        Object value = "value";
        Exception exception = new IllegalArgumentException("Invalid argument.");

        IOException result = null;
        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (IOException e) {
            result = e;
        }

        assertEquals("Problem deserializing \"any\" property 'property' of class javaType (expected type: javaType; actual type: java.lang.String) (no error message provided)",
                result.getMessage());
    }

    @Test
    public void testThrowAsIOE_IOException() throws IOException {
        Object propName = "property";
        Object value = "value";
        Exception exception = new IOException("IO error.");

        IOException result = null;
        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (IOException e) {
            result = e;
        }

        assertEquals(exception, result);
    }

    @Test
    public void testRethrowIfRTE() {
        RuntimeException exception = new RuntimeException("Runtime error.");

        RuntimeException result = null;
        try {
            ClassUtil.throwIfRTE(exception);
        } catch (RuntimeException e) {
            result = e;
        }

        assertEquals(exception, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testRethrowIfRTE_JsonProcessingException() {
        Exception exception = new JsonProcessingException("JsonProcessing error.") {};

        try {
            ClassUtil.throwIfRTE(exception);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testToString() {
        String result = settableAnyProperty.toString();

        assertEquals("[any property on class null]", result);
    }
}