import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        integerSerializer.serialize(123, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIntegerSerializer_NegativeNumber() throws JsonProcessingException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        integerSerializer.serialize(-123, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "-123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIntegerSerializer_LargeNumber() throws JsonProcessingException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        integerSerializer.serialize(Integer.MAX_VALUE, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = Integer.MAX_VALUE;
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntegerSerializer_FloatNumber() throws JsonProcessingException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        integerSerializer.serialize(3.14, jsonGenerator, new SerializerProvider().builder().build());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        intLikeSerializer.serialize(123, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIntLikeSerializer_FloatNumber() throws JsonProcessingException {
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        intLikeSerializer.serialize(3.14, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "3.14";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        longSerializer.serialize(123L, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLongSerializer_NegativeNumber() throws JsonProcessingException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        longSerializer.serialize(-123L, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "-123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLongSerializer_LargeNumber() throws JsonProcessingException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        longSerializer.serialize(Long.MAX_VALUE, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = Long.MAX_VALUE;
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = JsonProcessingException.class)
    public void testLongSerializer_ShortNumber() throws JsonProcessingException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        longSerializer.serialize(32767, jsonGenerator, new SerializerProvider().builder().build());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        FloatSerializer floatSerializer = FloatSerializer.instance;
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        floatSerializer.serialize(3.14f, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "3.14";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFloatSerializer_NegativeNumber() throws JsonProcessingException {
        FloatSerializer floatSerializer = FloatSerializer.instance;
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        floatSerializer.serialize(-3.14f, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = "-3.14";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFloatSerializer_LargeNumber() throws JsonProcessingException {
        FloatSerializer floatSerializer = FloatSerializer.instance;
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        floatSerializer.serialize(Float.MAX_VALUE, jsonGenerator, new SerializerProvider().builder().build());

        String actualOutput = jsonGenerator.getOutputTestString();
        String expectedOutput = Float.MAX_VALUE;
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = JsonProcessingException.class)
    public void testFloatSerializer_IntegerNumber() throws JsonProcessingException {
        FloatSerializer floatSerializer = FloatSerializer.instance;
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValue(null);

        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(System.out);
        floatSerializer.serialize(123, jsonGenerator, new SerializerProvider().builder().build());
    }
}