package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectDeserializer<String> STANDARD_STRING_DESERIALIZER = new StandardSerializer<>(String.class);

    @Test
    public void testDeserializeStandard() throws Exception {
        String[] result = StringArrayDeserializer.instance.deserialize(createTestParser(), new TestContext());
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonDeserializer<String> customDeserializer = new StandardSerializer<>(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(createTestParser(), new TestContext());
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
        String[] result = StringArrayDeserializer.instance.deserializeWithType(createTestParser(), new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testHandleNonArraySingleValue() throws Exception {
        JsonParser parser = createTestParser();
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // read VALUE_STRING: "foo"
        parser.nextToken(); // read END_ARRAY

        String[] result = StringArrayDeserializer.instance.handleNonArray(parser, new TestContext());
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testHandleNonArrayEmptyArray() throws Exception {
        JsonParser parser = createTestParser();
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // read END_ARRAY

        String[] result = StringArrayDeserializer.instance.handleNonArray(parser, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayInvalidType() throws Exception {
        JsonParser parser = createTestParser();
        parser.nextToken(); // skip START_ARRAY
        parser.nextToken(); // read VALUE_NUMBER: 123
        parser.nextToken(); // read END_ARRAY

        String[] result = StringArrayDeserializer.instance.handleNonArray(parser, new TestContext());
        fail("Expected IOException");
    }

    @Test
    public void testCreateContextualDefault() throws Exception {
        JsonDeserializer<String> customDeserializer = new StandardSerializer<>(String.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance.createContextual(new TestContext(), null);
        assertSame(StringArrayDeserializer.instance, deserializer);
    }

    @Test
    public void testCreateContextualCustom() throws Exception {
        JsonDeserializer<String> customDeserializer = new StandardSerializer<>(String.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance.createContextual(new TestContext(), null);
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestContext(), null);
        assertInstanceof(StringArrayDeserializer.class, contextualDeserializer);
        assertNotSame(deserializer, contextualDeserializer);
        assertSame(customDeserializer, contextualDeserializer.getValueDeserializer());
    }

    @Test
    public void testCreateContextualCustomWithTypeDeserializer() throws Exception {
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
        JsonDeserializer<String> customDeserializer = new StandardSerializer<>(String.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance.createContextual(new TestContext(), null);
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestContext(), null);
        assertInstanceof(StringArrayDeserializer.class, contextualDeserializer);
        assertNotSame(deserializer, contextualDeserializer);
        JsonDeserializer<?> valueDeserializer = contextualDeserializer.getValueDeserializer();
        assertSame(customDeserializer, valueDeserializer);
        assertSame(typeDeserializer, contextualDeserializer.getTypeDeserializer());
    }

    @Test
    public void testCreateContextualCustomWithContentConverter() throws Exception {
        JsonDeserializer<String> customDeserializer = new StringToIntegerDeserializer();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance.createContextual(new TestContext(), null);
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestContext(), null);
        assertInstanceof(StringArrayDeserializer.class, contextualDeserializer);
        assertNotSame(deserializer, contextualDeserializer);
        JsonDeserializer<?> valueDeserializer = contextualDeserializer.getValueDeserializer();
        assertSame(customDeserializer, valueDeserializer);
    }

    @Test
    public void testCreateContextualNull() throws Exception {
        JsonDeserializer<String> customDeserializer = new StandardSerializer<>(String.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance.createContextual(new TestContext(), null);
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestContext(), null);
        assertNull(contextualDeserializer);
    }

    private JsonParser createTestParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser("[\n" +
                "  \"foo\",\n" +
                "  \"bar\"\n" +
                "]");
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer();
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(Class<?> valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(Class<?> valueType, PropertyName propertyName) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> getDeserializer(Class<?> valueType) throws JsonMappingException {
            return valueType == String.class ? STANDARD_STRING_DESERIALIZER : null;
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public ObjectReader getValueFactory() {
            return null;
        }

        @Override
        public TypeDeserializer getTypeDeserializer() {
            return null;
        }

        @Override
        public String getBaseName() {
            return null;
        }

        @Override
        public String getCurrentName() {
            return null;
        }

        @Override
        public Object getCurrentValue() {
            return null;
        }

        @Override
        public JsonParser getCurrentLocation() {
            return null;
        }

        @Override
        public JsonParser getParser() {
            return null;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }

        @Override
        public int getCurrentLine() {
            return 0;
        }

        @Override
        public int getCurrentColumn() {
            return 0;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void setBeanProperty(BeanProperty property) {
        }

        @Override
        public void setContextualHandler(DeserializationContext.ContextualHandler handler) {
        }

        @Override
        public void setPropertyId(String propertyName) {
        }

        @Override
        public void setPropertyName(PropertyName propertyName) {
        }

        @Override
        public void setPropertyRegistration(BeanProperty propertyRegistration) {
        }

        @Override
        public void setCurrentValue(Object value) {
        }

        @Override
        public void setCurrentLocation(JsonParser location) {
        }

        @Override
        public void setDefaultUseWrapperName(boolean useWrapperName) {
        }

        @Override
        public void setUnmarplingContext(UnmarplingContext context) {
        }

        @Override
        public void setActiveContext(DeserializationContext activeContext) {
        }

        @Override
        public <T extends JsonDeserializer<?>> T findValueDeserializer(Class<T> valueType) throws JsonMappingException {
            return null;
        }

        @Override
        public void registerModifier(PropertyModifier propertyModifier) {
        }

        @Override
        public void registerPropertyIdentifier(PropertyIdentifierIdentifierProperty identifier) {
        }

        @Override
        public void setCurrentContextualHandler(DeserializationContext.ContextualHandler contextualHandler) {
        }
    }
}