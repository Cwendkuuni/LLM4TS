package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test method for {@link StringArrayDeserializer#StringArrayDeserializer()} constructor.
     */
    @Test
    public void testStringArrayDeserializerConstructor() {
        StringArrayDeserializer serializer = StringArrayDeserializer.instance;
        assertNotNull(serializer);
        assertEquals(String[].class, serializer.getClass());
    }

    /**
     * Test method for {@link StringArrayDeserializer#StringArrayDeserializer(JsonDeserializer)} constructor.
     */
    @Test
    public void testStringArrayDeserializerConstructorWithJsonDeserializer() {
        JsonDeserializer<String> elementDeserializer = mapper.getFactory().createValueDeserializer(String.class, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        StringArrayDeserializer serializer = new StringArrayDeserializer(elementDeserializer);
        assertNotNull(serializer);
        assertEquals(String[].class, serializer.getClass());
        assertEquals(elementDeserializer, serializer._elementDeserializer);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} method.
     */
    @Test
    public void testDeserialize() throws IOException {
        String[] expectedArray = {"test1", "test2", null};
        JsonParser jsonParser = mapper.getFactory().createParser("[\"" + "test1" + "\",\"" + "test2" + "\",null]");
        StringArrayDeserializer serializer = StringArrayDeserializer.instance;
        String[] actualArray = serializer.deserialize(jsonParser, new DeserializationContext(new BaseTest().locale, new StandardSerializers.BaseSerializerFinder(), null));
        assertArrayEquals(expectedArray, actualArray);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)} method.
     */
    @Test
    public void testDeserializeCustom() throws IOException {
        JsonDeserializer<String> elementDeserializer = mapper.getFactory().createValueDeserializer(String.class, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        StringArrayDeserializer serializer = new StringArrayDeserializer(elementDeserializer);
        String[] expectedArray = {"test1", "test2", null};
        JsonParser jsonParser = mapper.getFactory().createParser("[\"" + "test1" + "\",\"" + "test2" + "\",null]");
        String[] actualArray = serializer.deserializeCustom(jsonParser, new DeserializationContext(new BaseTest().locale, new StandardSerializers.BaseSerializerFinder(), null));
        assertArrayEquals(expectedArray, actualArray);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)} method.
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = new StdTypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonToken t = p.nextToken();
                if (t == JsonToken.VALUE_NULL) {
                    return null;
                }
                return new String[] { p.getText() };
            }
        };
        String[] expectedArray = {"test1"};
        JsonParser jsonParser = mapper.getFactory().createParser("[\"" + "test1" + "\"]");
        String[] actualArray = new StringArrayDeserializer(null).deserializeWithType(jsonParser, new DeserializationContext(new BaseTest().locale, new StandardSerializers.BaseSerializerFinder(), typeDeserializer), typeDeserializer);
        assertArrayEquals(expectedArray, actualArray);
    }

    /**
     * Test method for {@link StringArrayDeserializer#handleNonArray(JsonParser, DeserializationContext)} method.
     */
    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("\"test1\"");
        String[] actualArray = new StringArrayDeserializer(null).handleNonArray(jsonParser, new DeserializationContext(new BaseTest().locale, new StandardSerializers.BaseSerializerFinder(), null));
        assertArrayEquals(new String[]{"test1"}, actualArray);
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)} method.
     */
    @Test
    public void testCreateContextual() throws IOException {
        JsonDeserializer<Object> elementDeserializer = mapper.getFactory().createValueDeserializer(Object.class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        BeanProperty property = new BeanProperty("testProperty", String[].class, null);
        StringArrayDeserializer serializer = new StringArrayDeserializer(null);
        JsonDeserializer<?> deser = serializer.createContextual(new DeserializationContext(new BaseTest().locale, new StandardSerializers.BaseSerializerFinder(), null), property);
        assertTrue(deser instanceof StringArrayDeserializer);
    }
}