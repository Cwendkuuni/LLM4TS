package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {
    private static final SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
            new BeanProperty(new AnnotatedField(SettableAnyProperty.class.getDeclaredField("_property"))),
            new AnnotatedField(SettableAnyProperty.class.getDeclaredField("_setter")),
            JsonTypeFactory.valueType(Map.class), null, null, null);

    @Test
    public void testSettableAnyProperty() {
        assertEquals("SettableAnyProperty", settableAnyProperty.getClass().getName());
        assertEquals(true, settableAnyProperty._property != null);
        assertEquals(true, settableAnyProperty._setter != null);
        assertEquals(true, settableAnyProperty._setterIsField);
        assertEquals(Map.class, settableAnyProperty._type);
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object testInstance = new Object();

        // Test serialization and deserialization with null value
        String jsonString = mapper.writeValueAsString(null);
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext(), testInstance, "_testProperty");
        assertEquals(null, testInstance);

        // Test serialization and deserialization with string value
        jsonString = mapper.writeValueAsString("testString");
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext(), testInstance, "_testProperty");
        Map<Object, Object> testMap = (Map<Object, Object>) testInstance;
        assertEquals("testString", testMap.get("_testProperty"));

        // Test serialization and deserialization with number value
        jsonString = mapper.writeValueAsString(123);
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext(), testInstance, "_testProperty");
        assertEquals(123, testMap.get("_testProperty"));

        // Test serialization and deserialization with boolean value
        jsonString = mapper.writeValueAsString(true);
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext(), testInstance, "_testProperty");
        assertEquals(true, testMap.get("_testProperty"));

        // Test serialization and deserialization with JsonNode value
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("_testProperty", mapper.createObjectNode());
        jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext(), testInstance, "_testProperty");
        testMap = (Map<Object, Object>) testInstance;
        assertNotNull(testMap.get("_testProperty"));

        // Test serialization and deserialization with JsonValue value
        Object jsonValue = mapper.readValue(JsonFactory.instance.createValueInstance(JsonFactory.valueFromObject("testValue")), Object.class);
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(mapper.writeValueAsString(jsonValue)), mapper.getDeserializationContext(), testInstance, "_testProperty");
        testMap = (Map<Object, Object>) testInstance;
        assertEquals("testValue", testMap.get("_testProperty"));

        // Test serialization and deserialization with custom deserializer
        JsonDeserializer<Object> customDeserializer = new StubJsonDeserializer<>();
        SettableAnyProperty settableAnyPropertyWithCustomDeserializer = settableAnyProperty.withValueDeserializer(customDeserializer);
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(mapper.writeValueAsString("testValue")), mapper.getDeserializationContext(), testInstance, "_testProperty");
        testMap = (Map<Object, Object>) testInstance;
        assertEquals("testValue", testMap.get("_testProperty"));
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(mapper.writeValueAsString("testValue"));
        assertEquals("testValue", settableAnyProperty.deserialize(jsonParser, mapper.getDeserializationContext()));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object testInstance = new Object();
        String jsonString = mapper.writeValueAsString("[1, \"test\"]");
        SettableAnyProperty.deserializeAndSet(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext(), testInstance, "_testProperty");
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object testInstance = new Object();
        Map<Object, Object> testMap = new HashMap<>();
        testMap.put("_testProperty", "testString");
        testInstance = testMap;

        // Test setting a value using a setter method
        String setterMethodName = "set_testProperty";
        Class<?> testClass = testInstance.getClass();
        AnnotatedMethod setterMethod = findSetterMethod(testClass, setterMethodName);
        Object testValue = "testValue";
        settableAnyProperty.set(testInstance, "_testProperty", testValue);
        assertEquals(testValue, setterMethod.callOnWith(testInstance, "_testProperty"));

        // Test setting a value using a field
        settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(testClass.getDeclaredField("_testMap"))),
                new AnnotatedField(testClass.getDeclaredField("_testMap")),
                JsonTypeFactory.valueType(Map.class), null, null, null);
        testMap = (Map<Object, Object>) testInstance;
        testMap.put("_testProperty", testValue);
        testInstance = testMap;
        settableAnyProperty.set(testInstance, "_testProperty", "testValue2");
        assertEquals("testValue2", testMap.get("_testProperty"));
    }

    private AnnotatedMethod findSetterMethod(Class<?> testClass, String methodName) {
        AnnotatedMethod[] declaredMethods = testClass.getDeclaredMethods();
        for (AnnotatedMethod declaredMethod : declaredMethods) {
            if (methodName.equals(declaredMethod.getName()) &&
                    declaredMethod.getAnnotations().length > 0 &&
                    declaredMethod.getParameterTypes().length == 2 &&
                    JsonProperty.class.isAssignableFrom(declaredMethod.getParameterTypes()[0].getAnnotation(JsonProperty.class) != null)) {
                return declaredMethod;
            }
        }
        throw new RuntimeException("Could not find setter method: " + methodName);
    }
}

class StubJsonDeserializer<T> extends JsonDeserializer<T> {
    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return deserializationContext.readValue(JsonFactory.valueFromObject("testValue"), JsonDeserializer.None.class);
    }
}