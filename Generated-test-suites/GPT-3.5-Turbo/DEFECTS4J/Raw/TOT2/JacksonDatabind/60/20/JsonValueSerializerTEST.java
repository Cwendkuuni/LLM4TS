package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class JsonValueSerializerTest {

    @Mock
    ContextualSerializer contextualSerializer;

    @Mock
    BeanProperty beanProperty;

    @Mock
    JsonGenerator jsonGenerator;

    @Mock
    SerializerProvider serializerProvider;

    @Test
    public void testSerialize() throws Exception {
        Method method = JsonValueSerializer.class.getDeclaredMethod("serialize", Object.class, JsonGenerator.class, SerializerProvider.class);
        method.setAccessible(true);
        
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null);
        
        String result = "testResult";
        Object bean = new Object();
        Mockito.when(jsonGenerator.writeObject(result)).thenReturn(null);

        method.invoke(jsonValueSerializer, bean, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeObject(result);
    }

    @Test
    public void testCreateContextual() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null);
        JsonSerializer jsonSerializer = jsonValueSerializer.createContextual(serializerProvider, beanProperty);
        
        assertEquals(null, jsonSerializer);
    }

    @Test
    public void testGetSchema() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null);
        JsonNode jsonNode = jsonValueSerializer.getSchema(serializerProvider, null);
        
        assertEquals(com.fasterxml.jackson.databind.jsonschema.JsonSchema.getDefaultSchemaNode(), jsonNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        Method method = JsonValueSerializer.class.getDeclaredMethod("_acceptJsonFormatVisitorForEnum", JsonFormatVisitorWrapper.class, JavaType.class, Class.class);
        method.setAccessible(true);
        
        boolean result = (boolean) method.invoke(jsonValueSerializer, visitor, typeHint, JsonValueSerializer.class);
        
        assertEquals(true, result);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null);
        boolean result = jsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, null);
        
        assertEquals(true, result);
    }
}