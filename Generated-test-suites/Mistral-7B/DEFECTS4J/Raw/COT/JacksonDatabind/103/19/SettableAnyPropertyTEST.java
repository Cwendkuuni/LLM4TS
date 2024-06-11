package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Test class for SettableAnyProperty.
 */
public class SettableAnyPropertyTest {

    @SuppressWarnings("unchecked")
    private static final SettableAnyProperty testProperty = new SettableAnyProperty(
            new BeanProperty(new AnnotatedField(TestClass.class.getDeclaredField("mapField"))),
            new AnnotatedField(TestClass.class.getDeclaredField("mapField")),
            JsonTypeFactory.getDefaultInstance().constructType(Map.class),
            null,
            null,
            null
    );

    /**
     * Test method for {@link SettableAnyProperty#SettableAnyProperty(BeanProperty, AnnotatedMember, JavaType, KeyDeserializer, JsonDeserializer, TypeDeserializer)}.
     */
    @Test
    public void testConstructor() {
        assertNotNull(testProperty);
    }

    /**
     * Test method for {@link SettableAnyProperty#getProperty()}.
     */
    @Test
    public void testGetProperty() {
        assertEquals(testProperty.getProperty(), new BeanProperty(new AnnotatedField(TestClass.class.getDeclaredField("mapField"))));
    }

    /**
     * Test method for {@link SettableAnyProperty#hasValueDeserializer()}.
     */
    @Test
    public void testHasValueDeserializer() {
        assertFalse(testProperty.hasValueDeserializer());
    }

    /**
     * Test method for {@link SettableAnyProperty#getType()}.
     */
    @Test
    public void testType() {
        JsonType type = testProperty.getType();
        assertEquals(Map.class, type.getRawClass());
        assertEquals(Map.class, type.getRawClass());
    }

    /**
     * Test method for {@link SettableAnyProperty#deserializeAndSet(JsonParser, DeserializationContext, Object, String)}.
     * @throws IOException
     */
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        Object instance = new TestClass();

        testProperty.deserializeAndSet(parser, new TestContext(), instance, "key");

        Map<Object, Object> mapField = (Map<Object, Object>) instance.mapField;
        assertEquals("value", mapField.get("key"));
    }

    /**
     * Test method for {@link SettableAnyProperty#deserialize(JsonParser, DeserializationContext)}.
     * @throws IOException
     */
    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"value\":1}");

        Object result = testProperty.deserialize(parser, new TestContext());

        assertEquals(1, result);
    }

    /**
     * Test method for {@link SettableAnyProperty#set(Object, Object, Object)}.
     * @throws IOException
     */
    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new TestClass();

        JsonParser parser = mapper.getFactory().createParser("{\"key\":1}");
        testProperty.deserializeAndSet(parser, new TestContext(), instance, "key");

        Map<Object, Object> mapField = (Map<Object, Object>) instance.mapField;
        assertEquals(1, mapField.get("key"));
    }

    /**
     * Test method for {@link SettableAnyProperty#_throwAsIOE(Exception, Object, Object)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testThrowAsIOE() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new TestClass();

        JsonParser parser = mapper.getFactory().createParser("{\"key\":[1]}");
        testProperty.deserializeAndSet(parser, new TestContext(), instance, "key");
    }

}

class TestClass {
    Map<Object, Object> mapField;
}

class TestContext extends DeserializationContext {
    @Override public Configuration getConfig() { return new Configuration(); }

    @Override public ObjectReader getRootValueFactory() { return new SimpleObjectReader(); }

    @Override public JsonDeserializer<Object> findValueDeserializer(JavaType type) { return null; }

    @Override public ObjectDeserializer<?> findBeanDeserializer(JavaType type) { return null; }

    @Override public JavaType findValueTypeDeserializer(JavaType type, DeserializationContext ctxt) { return null; }

    @Override public KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType keyType) { return null; }
}