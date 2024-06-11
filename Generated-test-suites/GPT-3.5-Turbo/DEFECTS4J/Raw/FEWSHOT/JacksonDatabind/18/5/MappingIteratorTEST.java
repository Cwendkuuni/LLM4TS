
package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.JacksonDeserialize.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.JacksonDeserialize.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.TypedDeserializationContext;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.RawValue;

public class MappingIteratorTest {
    private static class CustomType {
        private String value;

        public CustomType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private JsonParser createParser(String json) throws IOException {
        return new JsonFactory().createParser(json);
    }

    private DeserializationContext createDeserializationContext(JsonParser parser) {
        return new TypedDeserializationContext(parser, (ValueDeserializer<Object>)null, (TypeFactory)null);
    }

    private MappingIterator<CustomType> createMappingIterator(JsonParser parser, DeserializationContext context) {
        JacksonDeserialize.ValueDeserializer deserializer = new JacksonDeserialize.ValueDeserializer(CustomType.class, null);
        return new MappingIterator<>(TypeFactory.defaultInstance().constructType(CustomType.class), parser, context, deserializer, true, null);
    }

    /**
     * Returns a {@link MappingIterator} for testing with the given input values
     *
     * @param values The input values
     * @return The {@link MappingIterator} for testing
     */
    @SafeVarargs
    private final MappingIterator<CustomType> createMappingIterator(CustomType... values) {
        List<JsonToken> tokens = new ArrayList<>();
        for (CustomType value : values) {
            tokens.add(JsonToken.START_OBJECT);
            tokens.add(JsonToken.FIELD_NAME);
            tokens.add(JsonToken.VALUE_STRING);
            tokens.add(JsonToken.END_OBJECT);
        }
        JsonParser parser = new FakeJsonParser(tokens);
        return createMappingIterator(parser, createDeserializationContext(parser));
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<CustomType> iterator = createMappingIterator();

        boolean result = iterator.hasNextValue();

        assertFalse(result);
    }

    @Test
    public void testHasNextValueWithValues() throws IOException {
        MappingIterator<CustomType> iterator = createMappingIterator(new CustomType("foo"), new CustomType("bar"));

        boolean result1 = iterator.hasNextValue();
        boolean result2 = iterator.hasNextValue();

        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    public void testHasNextValueWithOneValue() throws IOException {
        MappingIterator<CustomType> iterator = createMappingIterator(new CustomType("foo"));

        boolean result1 = iterator.hasNextValue();
        boolean result2 = iterator.hasNextValue();

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<CustomType> iterator = createMappingIterator(new CustomType("foo"), new CustomType("bar"));
        
        CustomType result1 = iterator.nextValue();
        CustomType result2 = iterator.nextValue();

        assertEquals("foo", result1.getValue());
        assertEquals("bar", result2.getValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWhenNoMoreValues() throws IOException {
        MappingIterator<CustomType> iterator = createMappingIterator();

        iterator.nextValue();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWithOneValue() throws IOException {
        MappingIterator<CustomType> iterator = createMappingIterator(new CustomType("foo"));

        CustomType result1 = iterator.nextValue();
        CustomType result2 = iterator.nextValue();

        assertEquals("foo", result1.getValue());
        assertEquals("bar", result2.getValue());
    }
}

