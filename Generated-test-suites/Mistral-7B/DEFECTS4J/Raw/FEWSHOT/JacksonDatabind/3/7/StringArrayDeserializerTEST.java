package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for StringArrayDeserializer.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyArray() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[ ]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testArrayWithSingleValue() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[\"Hello\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals("Hello", result[0]);
    }

    @Test
    public void testArrayWithMultipleValues() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[\"Hello\", \"World\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertArrayEquals(new String[] {"Hello", "World"}, result);
    }

    @Test
    public void testArrayWithNullValue() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[null]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertArrayEquals(new String[] {null}, result);
    }

    @Test
    public void testArrayWithCustomValueDeserializer() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[{\"value\":\"Hello\"}]");
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals("Hello", result[0]);
    }

    @Test(expected = IOException.class)
    public void testMissingStartArrayToken() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{\"value\":\"Hello\"}");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(parser, new TestDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidToken() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[1]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(parser, new TestDeserializationContext());
    }

    @Test
    public void testContextualDeserializer() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[{\"value\":\"Hello\"}]");
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), null);
        assertEquals(CustomStringDeserializer.class, contextualDeserializer.getClass());
        contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), new StdName("foo"));
        assertEquals(CustomStringDeserializer.class, contextualDeserializer.getClass());
        contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), new StdName("bar"));
        assertEquals(null, contextualDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithNullValueDeserializer() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[null]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), null);
        assertNull(contextualDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithDefaultStringDeserializer() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[\"Hello\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), null);
        assertNull(contextualDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithCustomStringDeserializer() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[{\"value\":\"Hello\"}]");
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), null);
        assertEquals(CustomStringDeserializer.class, contextualDeserializer.getClass());
    }

    @Test
    public void testCreateContextualDeserializerWithCustomContentConverter() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[{\"value\":1}]");
        CustomStringDeserializerWithConverter customDeserializer = new CustomStringDeserializerWithConverter();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestDeserializationContext(), null);
        assertEquals(CustomStringDeserializerWithConverter.class, contextualDeserializer.getClass());
    }

    private static class CustomStringDeserializer
            extends StdDeserializer<String>
            implements ContextualDeserializer
    {
        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            return "Custom";
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return this;
        }
    }

    private static class CustomStringDeserializerWithConverter
            extends StdDeserializer<String>
            implements ContextualDeserializer
    {
        @JsonDeserialize(using = NumberToStringConverter.class)
        public String deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            return "Custom";
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return this;
        }
    }

    private static class TestDeserializationContext
            extends DeserializationContext {
        @Override
        public Object instanceToBeDeserialized() {
            return null;
        }

        @Override
        public BeanDescription beanDescription() {
            return null;
        }

        @Override
        public ObjectReader rootValueReader() {
            return null;
        }

        @Override
        public JsonFactory jsonFactory() {
            return null;
        }

        @Override
        public ObjectWriter objectWriter() {
            return null;
        }

        @Override
        public AnnotationIntrospector annotationIntrospector() {
            return null;
        }

        @Override
        public JavaType typeDeserializer() {
            return null;
        }

        @Override
        public JavaType typeFinder() {
            return null;
        }

        @Override
        public DeserializationConfig config() {
            return null;
        }

        @Override
        public TypeResolverBuilder typeResolverBuilder() {
            return null;
        }
    }
}