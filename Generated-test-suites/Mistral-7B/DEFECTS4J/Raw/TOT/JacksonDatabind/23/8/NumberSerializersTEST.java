import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = new IntegerSerializer();

        // Positive integer test
        Integer input = 123;
        String expectedJson = "123";
        JsonNode actualJson = mapper.readTree(mapper.writerWithRootValue(integerSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Negative integer test
        input = -123;
        expectedJson = "-123";
        actualJson = mapper.readTree(mapper.writerWithRootValue(integerSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Integer with leading zeros test
        input = 1230;
        expectedJson = "1230";
        actualJson = mapper.readTree(mapper.writerWithRootValue(integerSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Integer out of range test
        long maxLongValue = Long.MAX_VALUE;
        input = (int) maxLongValue;
        expectedJson = Long.toString(maxLongValue);
        actualJson = mapper.readTree(mapper.writerWithRootValue(integerSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Integer NaN test
        input = Integer.NaN;
        expectedJson = "NaN";
        actualJson = mapper.readTree(mapper.writerWithRootValue(integerSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Integer Infinity test
        input = Integer.MAX_VALUE;
        expectedJson = "Infinity";
        actualJson = mapper.readTree(mapper.writerWithRootValue(integerSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        // Positive integer test
        Number input = 123;
        String expectedJson = "123";
        JsonNode actualJson = mapper.readTree(mapper.writerWithRootValue(intLikeSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Negative integer test
        input = -123;
        expectedJson = "-123";
        actualJson = mapper.readTree(mapper.writerWithRootValue(intLikeSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Integer with leading zeros test
        input = 1230;
        expectedJson = "1230";
        actualJson = mapper.readTree(mapper.writerWithRootValue(intLikeSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Integer out of range test
        long maxLongValue = Long.MAX_VALUE;
        input = (Number) maxLongValue;
        expectedJson = Long.toString(maxLongValue);
        actualJson = mapper.readTree(mapper.writerWithRootValue(intLikeSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Number NaN test
        input = Double.NaN;
        expectedJson = "NaN";
        actualJson = mapper.readTree(mapper.writerWithRootValue(intLikeSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Number Infinity test
        input = Double.POSITIVE_INFINITY;
        expectedJson = "Infinity";
        actualJson = mapper.readTree(mapper.writerWithRootValue(intLikeSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = new ShortSerializer();

        // Positive short test
        Short input = Short.parseShort("123");
        String expectedJson = "123";
        JsonNode actualJson = mapper.readTree(mapper.writerWithRootValue(shortSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Negative short test
        input = Short.parseShort("-123");
        expectedJson = "-123";
        actualJson = mapper.readTree(mapper.writerWithRootValue(shortSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Short out of range test
        short maxShortValue = Short.MAX_VALUE;
        input = (short) maxShortValue;
        expectedJson = Short.toString(maxShortValue);
        actualJson = mapper.readTree(mapper.writerWithRootValue(shortSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Short NaN test
        input = Short.NaN;
        expectedJson = "NaN";
        actualJson = mapper.readTree(mapper.writerWithRootValue(shortSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Short Infinity test
        input = Short.MAX_VALUE;
        expectedJson = "Infinity";
        actualJson = mapper.readTree(mapper.writerWithRootValue(shortSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = new LongSerializer();

        // Positive long test
        Long input = Long.parseLong("12345678901234567890");
        String expectedJson = "12345678901234567890";
        JsonNode actualJson = mapper.readTree(mapper.writerWithRootValue(longSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Negative long test
        input = -Long.parseLong("12345678901234567890");
        expectedJson = "-12345678901234567890";
        actualJson = mapper.readTree(mapper.writerWithRootValue(longSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Long out of range test
        long maxLongValue = Long.MAX_VALUE;
        input = maxLongValue;
        expectedJson = Long.toString(maxLongValue);
        actualJson = mapper.readTree(mapper.writerWithRootValue(longSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Long NaN test
        input = Long.NaN;
        expectedJson = "NaN";
        actualJson = mapper.readTree(mapper.writerWithRootValue(longSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Long Infinity test
        input = Long.MAX_VALUE;
        expectedJson = "Infinity";
        actualJson = mapper.readTree(mapper.writerWithRootValue(longSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = new FloatSerializer();

        // Positive float test
        Float input = 123.456f;
        String expectedJson = "123.456";
        JsonNode actualJson = mapper.readTree(mapper.writerWithRootValue(floatSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Negative float test
        input = -123.456f;
        expectedJson = "-123.456";
        actualJson = mapper.readTree(mapper.writerWithRootValue(floatSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Float NaN test
        input = Float.NaN;
        expectedJson = "NaN";
        actualJson = mapper.readTree(mapper.writerWithRootValue(floatSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Float Infinity test
        input = Float.POSITIVE_INFINITY;
        expectedJson = "Infinity";
        actualJson = mapper.readTree(mapper.writerWithRootValue(floatSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = new DoubleSerializer();

        // Positive double test
        Double input = 123.456;
        String expectedJson = "123.456";
        JsonNode actualJson = mapper.readTree(mapper.writerWithRootValue(doubleSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Negative double test
        input = -123.456;
        expectedJson = "-123.456";
        actualJson = mapper.readTree(mapper.writerWithRootValue(doubleSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Double NaN test
        input = Double.NaN;
        expectedJson = "NaN";
        actualJson = mapper.readTree(mapper.writerWithRootValue(doubleSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());

        // Double Infinity test
        input = Double.POSITIVE_INFINITY;
        expectedJson = "Infinity";
        actualJson = mapper.readTree(mapper.writerWithRootValue(doubleSerializer).writeValueAsString(input));
        assertEquals(expectedJson, actualJson.asText());
    }
}