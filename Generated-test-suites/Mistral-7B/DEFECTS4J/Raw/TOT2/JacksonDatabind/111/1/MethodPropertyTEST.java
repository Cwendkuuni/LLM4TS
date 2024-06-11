package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import org.mockito.*;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstruction() throws Exception {
        AnnotatedMethod methodAnnotation = mock(AnnotatedMethod.class);
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType javaType = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations annotations = mock(Annotations.class);

        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, annotations, methodAnnotation);

        Assert.assertNotNull(methodProperty);
    }

    @Test
    public void testMethodPropertyDeserialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = JsonNodeFactory.instance.objectNode();

        MethodProperty methodProperty = new MethodProperty(mock(BeanPropertyDefinition.class), mock(JavaType.class), mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));
        JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        when(objectMapper.findValueDeserializer(eq(methodProperty._annotated.getDeclaringClass())))
                .thenReturn(jsonDeserializer);
        when(jsonDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenAnswer(invocationOnMock -> mock(Object.class));

        Object instance = mock(Object.class);

        methodProperty.deserializeAndSet(objectMapper.readTree(jsonNode), mock(DeserializationContext.class), instance);

        Assert.assertNotNull(instance);
    }

    @Test
    public void testMethodPropertyEdgeCases() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = JsonNodeFactory.instance.objectNode();

        MethodProperty methodProperty = new MethodProperty(mock(BeanPropertyDefinition.class), mock(JavaType.class), mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));

        // Test skipNulls flag
        jsonNode.put("property", null);
        Object instance = mock(Object.class);
        when(methodProperty._skipNulls).thenReturn(true);

        methodProperty.deserializeAndSet(objectMapper.readTree(jsonNode), mock(DeserializationContext.class), instance);
        Assert.assertNull(instance);

        // Test null valueDeserializer
        JsonDeserializer<?> jsonDeserializer = null;
        when(objectMapper.findValueDeserializer(eq(methodProperty._annotated.getDeclaringClass())))
                .thenReturn(jsonDeserializer);

        jsonNode.put("property", "non-null_value");
        instance = mock(Object.class);

        try {
            methodProperty.deserializeAndSet(objectMapper.readTree(jsonNode), mock(DeserializationContext.class), instance);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }

        // Test null TypeDeserializer
        TypeDeserializer typeDeserializer = null;
        when(objectMapper.findValueDeserializer(eq(methodProperty._annotated.getDeclaringClass())))
                .thenReturn(jsonDeserializer);
        when(jsonDeserializer.getTypeDeserializer())
                .thenReturn(typeDeserializer);

        jsonNode.put("property", "non-null_value");
        instance = mock(Object.class);

        try {
            methodProperty.deserializeAndSet(objectMapper.readTree(jsonNode), mock(DeserializationContext.class), instance);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }
}