import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer number = 123;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
        Integer number = -123;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
        Integer number = Integer.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Integer.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
        Integer number = Integer.MAX_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Integer.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerWithNull() throws JsonProcessingException {
        Integer number = null;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("null", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertNull(parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Number number = 123;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializerWithNegativeNumber() throws JsonProcessingException {
        Number number = -123;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializerWithMinValue() throws JsonProcessingException {
        Number number = Number.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Number.MIN_VALUE.toString(), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializerWithMaxValue() throws JsonProcessingException {
        Number number = Number.MAX_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Number.MAX_VALUE.toString(), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializerWithNull() throws JsonProcessingException {
        Number number = null;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("null", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertNull(parser.readValueAs(Integer.class));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short number = (short) 123;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.shortValue(), parser.readValueAs(Short.class));
    }

    @Test
    public void testShortSerializerWithNegativeNumber() throws JsonProcessingException {
        Short number = (short) -123;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.shortValue(), parser.readValueAs(Short.class));
    }

    @Test
    public void testShortSerializerWithMinValue() throws JsonProcessingException {
        Short number = Short.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Short.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.shortValue(), parser.readValueAs(Short.class));
    }

    @Test
    public void testShortSerializerWithMaxValue() throws JsonProcessingException {
        Short number = Short.MAX_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Short.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.shortValue(), parser.readValueAs(Short.class));
    }

    @Test
    public void testShortSerializerWithNull() throws JsonProcessingException {
        Short number = null;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("null", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertNull(parser.readValueAs(Short.class));
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long number = 123L;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.longValue(), parser.readValueAs(Long.class));
    }

    @Test
    public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
        Long number = -123L;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-123", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.longValue(), parser.readValueAs(Long.class));
    }

    @Test
    public void testLongSerializerWithMinValue() throws JsonProcessingException {
        Long number = Long.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Long.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.longValue(), parser.readValueAs(Long.class));
    }

    @Test
    public void testLongSerializerWithMaxValue() throws JsonProcessingException {
        Long number = Long.MAX_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Long.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.longValue(), parser.readValueAs(Long.class));
    }

    @Test
    public void testLongSerializerWithNull() throws JsonProcessingException {
        Long number = null;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("null", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertNull(parser.readValueAs(Long.class));
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float number = 123.45F;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("123.45", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.floatValue(), parser.readValueAs(Float.class));
    }

    @Test
    public void testFloatSerializerWithNegativeNumber() throws JsonProcessingException {
        Float number = -123.45F;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-123.45", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.floatValue(), parser.readValueAs(Float.class));
    }

    @Test
    public void testFloatSerializerWithMinValue() throws JsonProcessingException {
        Float number = Float.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Float.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.floatValue(), parser.readValueAs(Float.class));
    }

    @Test
    public void testFloatSerializerWithMaxValue() throws JsonProcessingException {
        Float number = Float.MAX_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Float.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.floatValue(), parser.readValueAs(Float.class));
    }

    @Test
    public void testFloatSerializerWithNull() throws JsonProcessingException {
        Float number = null;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("null", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertNull(parser.readValueAs(Float.class));
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double number = 123.45;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("123.45", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.doubleValue(), parser.readValueAs(Double.class));
    }

    @Test
    public void testDoubleSerializerWithNegativeNumber() throws JsonProcessingException {
        Double number = -123.45;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-123.45", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.doubleValue(), parser.readValueAs(Double.class));
    }

    @Test
    public void testDoubleSerializerWithMinValue() throws JsonProcessingException {
        Double number = Double.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Double.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.doubleValue(), parser.readValueAs(Double.class));
    }

    @Test
    public void testDoubleSerializerWithMaxValue() throws JsonProcessingException {
        Double number = Double.MAX_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals(Double.toString(number), json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertEquals(number.doubleValue(), parser.readValueAs(Double.class));
    }

    @Test
    public void testDoubleSerializerWithNull() throws JsonProcessingException {
        Double number = null;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("null", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        assertNull(parser.readValueAs(Double.class));
    }
}