package com.example; // replace with your package name

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StdKeyDeserializerTest {

    

    @Test
    public void testForType() throws Exception {
        Class<?> stringClass = String.class;
        Object keyDeserializer = StdKeyDeserializer.forType(stringClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());

        Class<?> booleanClass = Boolean.class;
        Object booleanKeyDeserializer = StdKeyDeserializer.forType(booleanClass);
        assertEquals(StdKeyDeserializer.ClassType.TYPE_BOOLEAN, booleanKeyDeserializer._kind);

        Class<?> intClass = Integer.class;
        Object intKeyDeserializer = StdKeyDeserializer.forType(intClass);
        assertEquals(StdKeyDeserializer.ClassType.TYPE_INT, intKeyDeserializer._kind);

        Class<?> longClass = Long.class;
        Object longKeyDeserializer = StdKeyDeserializer.forType(longClass);
        assertEquals(StdKeyDeserializer.ClassType.TYPE_LONG, longKeyDeserializer._kind);

        Class<?> dateClass = Date.class;
        Object dateKeyDeserializer = StdKeyDeserializer.forType(dateClass);
        assertEquals(StdKeyDeserializer.ClassType.TYPE_DATE, dateKeyDeserializer._kind);

        Class<?> enumClass = MyEnum.class;
        Object enumKeyDeserializer = StdKeyDeserializer.forType(enumClass);
        assertEquals(StdKeyDeserializer.ClassType.TYPE_ENUM, enumKeyDeserializer._kind);

        // Add more test cases for other supported types
    }

    @Test
    public void testDeserializeKey() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getDeserializationContext();

        Class<?> stringClass = String.class;
        Object stringKeyDeserializer = StdKeyDeserializer.forType(stringClass);
        String key = "Hello World";
        Object deserializedKey = stringKeyDeserializer.deserializeKey(key, context);
        assertEquals(key, deserializedKey);

        Class<?> booleanClass = Boolean.class;
        Object booleanKeyDeserializer = StdKeyDeserializer.forType(booleanClass);
        String trueKey = "true";
        Object deserializedTrueKey = booleanKeyDeserializer.deserializeKey(trueKey, context);
        assertTrue((Boolean) deserializedTrueKey);

        String falseKey = "false";
        Object deserializedFalseKey = booleanKeyDeserializer.deserializeKey(falseKey, context);
        assertInstanceOf(Boolean.class, deserializedFalseKey);
        assertTrue(!((Boolean) deserializedFalseKey).booleanValue());

        // Add more test cases for other supported types
    }

    @Test
    public void testNegativeNumberParsing() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getDeserializationContext();

        Class<?> intClass = Integer.class;
        Object intKeyDeserializer = StdKeyDeserializer.forType(intClass);
        String negativeNumber = "-123";

        try {
            Object deserializedKey = intKeyDeserializer.deserializeKey(negativeNumber, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid representation: invalid int value: \"-123\"", e.getMessage());
        }
    }

    @Test
    public void testUnknownEnumValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.SerializedForm serializedForm = new ObjectMapper.SerializedForm();
        serializedForm.setSerializationFeatures(Arrays.asList(SerializationFeature.FAIL_ON_EMPTY_BEANS, SerializationFeature.FAIL_ON_EMPTY_BEANS_WITH_EMPTY_NAME));
        JsonFactory jsonFactory = new JsonFactory(new StdKeyDeserializer.StdSerializers.StringSerializers.StringSerializer(serializedForm));

        ObjectMapper objectMapperWithCustomFactory = new ObjectMapper();
        objectMapperWithCustomFactory.setFactory(jsonFactory);

        DeserializationContext context = objectMapperWithCustomFactory.getDeserializationContext();

        Class<?> enumClass = MyEnum.class;
        Object enumKeyDeserializer = StdKeyDeserializer.forType(enumClass);
        String unknownEnumValue = "UnknownValue";

        Object deserializedKey = enumKeyDeserializer.deserializeKey(unknownEnumValue, context);
        assertNull(deserializedKey);
    }

    @Test
    public void testDeserializeCustomClass() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.SerializedForm serializedForm = new ObjectMapper.SerializedForm();
        serializedForm.setSerializationFeatures(Arrays.asList(SerializationFeature.FAIL_ON_EMPTY_BEANS, SerializationFeature.FAIL_ON_EMPTY_BEANS_WITH_EMPTY_NAME));
        JsonFactory jsonFactory = new JsonFactory(new StdKeyDeserializer.StdSerializers.StringSerializers.StringSerializer(serializedForm));

        ObjectMapper objectMapperWithCustomFactory = new ObjectMapper();
        objectMapperWithCustomFactory.setFactory(jsonFactory);

        DeserializationContext context = objectMapperWithCustomFactory.getDeserializationContext();

        Class<?> customClass = CustomClass.class;
        Constructor<?> customClassConstructor = customClass.getDeclaredConstructor(String.class);
        customClassConstructor.setAccessible(true);

        Object customClassKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(customClassConstructor);
        String customKey = "CustomKey";

        Object deserializedKey = customClassKeyDeserializer.deserializeKey(customKey, context);
        assertEquals(customKey, deserializedKey);
    }

    
}

enum MyEnum {
    VALUE_1,
    VALUE_2;
}

class CustomClass {
    private final String name;

    public CustomClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}