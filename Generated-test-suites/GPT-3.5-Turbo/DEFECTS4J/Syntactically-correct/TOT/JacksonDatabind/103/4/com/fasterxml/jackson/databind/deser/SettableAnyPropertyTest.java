package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.key.KeyDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Mock
    private BeanProperty property;

    @Mock
    private JavaType type;

    @Mock
    private KeyDeserializer keyDeserializer;

    @Mock
    private JsonDeserializer<Object> valueDeserializer;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        settableAnyProperty = new SettableAnyProperty(property, type, keyDeserializer, valueDeserializer, typeDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "property";
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        
        when(keyDeserializer.deserializeKey(propName, deserializationContext)).thenReturn(propName);
        doNothing().when(settableAnyProperty).set(instance, propName, null);
        
        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, instance, propName);
        
        verify(settableAnyProperty, times(1)).set(eq(instance), eq(propName), isA(Object.class));
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        JsonToken jsonToken = JsonToken.VALUE_NULL;
        
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(valueDeserializer.getNullValue(deserializationContext)).thenReturn(null);
        
        Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);
        
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object result = new Object();
        
        when(valueDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenReturn(result);
        
        Object response = settableAnyProperty.deserialize(jsonParser, deserializationContext);
        
        assertEquals(result, response);
    }

    @Test
    public void testSet_NullValue() throws IOException {
        Object instance = new Object();
        String propName = "property";
        AnnotatedField field = mock(AnnotatedField.class);
        
        when(property.getMember()).thenReturn(field);
        when(field.getValue(instance)).thenReturn(null);
        
        settableAnyProperty.set(instance, propName, null);
        
        verify(field, times(0)).setValue(instance, new HashMap<>());
    }

    @Test
    public void testSet_FieldValueNotNull() throws IOException {
        Object instance = new Object();
        String propName = "property";
        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object,Object> map = new HashMap<>();
        
        when(property.getMember()).thenReturn(field);
        when(field.getValue(instance)).thenReturn(map);
        
        settableAnyProperty.set(instance, propName, null);
        
        verify(field, times(1)).setValue(instance, map);
        assertEquals(null, map.get(propName));
    }

    @Test
    public void testSet_FieldNullException() throws IOException {
        Object instance = new Object();
        String propName = "property";
        AnnotatedField field = mock(AnnotatedField.class);
        
        when(property.getMember()).thenReturn(field);
        when(field.getValue(instance)).thenReturn(null);
        
        settableAnyProperty.set(instance, propName, new Object());
        
        verify(field, times(1)).setValue(instance, Mockito.anyMap());
    }

    @Test
    public void testSet_MethodNotNull() throws IOException {
        Object instance = new Object();
        String propName = "property";
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        
        when(property.getMember()).thenReturn(method);
        
        settableAnyProperty.set(instance, propName, null);
        
        verify(method, times(1)).callOnWith(instance, propName, null);
    }

    @Test(expected = IOException.class)
    public void testSet_MethodException() throws IOException {
        Object instance = new Object();
        String propName = "property";
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        
        when(property.getMember()).thenReturn(method);
        doThrow(new Exception()).when(method).callOnWith(instance, propName, null);
        
        settableAnyProperty.set(instance, propName, null);
    }

    @Test
    public void testThrowAsIOE_Exception() {
        Object propName = "property";
        Object value = new Object();
        
        Exception exception = new IllegalArgumentException();
        
        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (IOException e) {
            assertEquals(JsonMappingException.class, e.getClass());
            assertEquals(exception, e.getCause());
        }
    }

    @Test
    public void testThrowAsIOE_IOException() {
        Object propName = "property";
        Object value = new Object();
        
        IOException exception = new IOException();
        
        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (IOException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testThrowAsIOE_RuntimeException() {
        Object propName = "property";
        Object value = new Object();
        
        RuntimeException exception = new RuntimeException();
        
        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (RuntimeException e) {
            assertEquals(exception, e);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowAsIOE_Default() {
        Object propName = "property";
        Object value = new Object();
        
        Exception exception = new Exception();
        
        settableAnyProperty._throwAsIOE(exception, propName, value);
    }
}