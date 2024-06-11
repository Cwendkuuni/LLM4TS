import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class NumberSerializerTest {

    // Check basic functionality of serialize method for various Number types
    @Test
    public void testSerializeBasicNumberTypes() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        Number testCases[] = {new BigDecimal("123.45"), new BigDecimal("0"), new BigInteger("12345678901234567890"),
                new Long(123), new Double(123.5), new Float(123.5f), new Integer(123), new Byte((byte) 123), new Short((short) 123)};

        for (Number number : testCases) {
            try {
                SerializerProvider provider = mapper.getSerializerProvider();
                JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
                serializer.serialize(number, generator, provider);
                generator.close();
            } catch (JsonProcessingException e) {
                fail("Unexpected JsonProcessingException for number: " + number);
            } catch (IOException e) {
                fail("Unexpected IOException for number: " + number);
            }
        }
    }

    // Test edge cases and exceptions

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullNumber() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        try {
            SerializerProvider provider = mapper.getSerializerProvider();
            JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
            serializer.serialize(null, generator, provider);
            generator.close();
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for null number");
        } catch (IOException e) {
            fail("Unexpected IOException for null number");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNonNumberTypes() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Object testCase = new Object();
        try {
            SerializerProvider provider = mapper.getSerializerProvider();
            JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
            serializer.serialize(testCase, generator, provider);
            generator.close();
        } catch (JsonProcessingException e) {
            // Expected JsonMappingException
        } catch (IOException e) {
            fail("Unexpected IOException for non-number type: " + testCase.getClass());
        }
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeBigDecimalWithInvalidStringFormat() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        try {
            SerializerProvider provider = mapper.getSerializerProvider();
            JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
            serializer.serialize(new BigDecimal("invalid"), generator, provider);
            generator.close();
            fail("Expected JsonProcessingException for invalid BigDecimal string format");
        } catch (JsonProcessingException e) {
            // Expected JsonProcessingException
        } catch (IOException e) {
            fail("Unexpected IOException for invalid BigDecimal string format");
        }
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeBigIntegerWithLongValueOutOfRange() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectMapper bigIntegerMapper = JsonMapperFactory.builder()
                .withSerializerModifier(new StandardSerializerModifier.Builder().build())
                .build();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number bigIntegerTestCases[] = {new BigInteger("9223372036854775808"), new BigInteger("9223372036854775809")}; // Long.MAX_VALUE and Long.MAX_VALUE + 1
        BigInteger serializedBigInteger;
        try {
            for (Number bigInteger : bigIntegerTestCases) {
                SerializerProvider provider = bigIntegerMapper.getSerializerProvider();
                JsonGenerator generator = bigIntegerMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
                serializer.serialize(bigInteger, generator, provider);
                generator.close();
                ObjectNode jsonNode = mapper.readValue(new ByteArrayInputStream(generator.getOutputStream().toByteArray()), ObjectNode.class);
                TextNode textNode = jsonNode.get("@numberType");
                serializedBigInteger = new BigInteger(textNode.asText());
                if (serializedBigInteger.longValue() != bigInteger.longValue()) {
                    fail("Expected the serialized BigInteger value to be equal to the original BigInteger value: original=" + bigInteger + ", serialized=" + serializedBigInteger);
                }
            }
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for BigInteger with long value out of range: " + bigIntegerTestCases[0]);
        } catch (IOException e) {
            fail("Unexpected IOException for BigInteger with long value out of range: " + bigIntegerTestCases[0]);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeBigIntegerWithStringValueOutOfRange() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectMapper bigIntegerMapper = JsonMapperFactory.builder()
                .withSerializerModifier(new StandardSerializerModifier.Builder().build())
                .build();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        String bigIntegerTestCases[] = {"9223372036854775809", "9223372036854775809abc"}; // Long.MAX_VALUE and Long.MAX_VALUE + 1 followed by an extra character
        BigInteger serializedBigInteger;
        try {
            for (String bigInteger : bigIntegerTestCases) {
                SerializerProvider provider = bigIntegerMapper.getSerializerProvider();
                JsonGenerator generator = bigIntegerMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
                serializer.serialize(new BigInteger(bigInteger), generator, provider);
                generator.close();
                ObjectNode jsonNode = mapper.readValue(new ByteArrayInputStream(generator.getOutputStream().toByteArray()), ObjectNode.class);
                TextNode textNode = jsonNode.get("@numberType");
                serializedBigInteger = new BigInteger(textNode.asText());
                if (serializedBigInteger.compareTo(new BigInteger(bigInteger)) != 0) {
                    fail("Expected the serialized BigInteger value to be equal to the original BigInteger value: original=" + bigInteger + ", serialized=" + serializedBigInteger);
                }
            }
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for BigInteger with string value out of range: " + bigIntegerTestCases[0]);
        } catch (IOException e) {
            fail("Unexpected IOException for BigInteger with string value out of range: " + bigIntegerTestCases[0]);
        }
    }

    @Test
    public void testSerializeBigDecimalWithPrecision() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectMapper bigDecimalMapper = JsonMapperFactory.builder()
                .withSerializerModifier(new StandardSerializerModifier.Builder().build())
                .build();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number bigDecimalTestCases[] = {new BigDecimal("123.45678901234567890123456789"), new BigDecimal("123.45"), new BigDecimal("123")};
        try {
            for (Number bigDecimal : bigDecimalTestCases) {
                SerializerProvider provider = bigDecimalMapper.getSerializerProvider();
                JsonGenerator generator = bigDecimalMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
                serializer.serialize(bigDecimal, generator, provider);
                generator.close();
                ObjectNode jsonNode = mapper.readValue(new ByteArrayInputStream(generator.getOutputStream().toByteArray()), ObjectNode.class);
                TextNode textNode = jsonNode.get("@numberType");
                String numberType = textNode.asText();
                assertEquals("Expected 'number' number type for BigDecimal with precision", "number", numberType);
            }
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for BigDecimal: " + bigDecimalTestCases[0]);
        } catch (IOException e) {
            fail("Unexpected IOException for BigDecimal: " + bigDecimalTestCases[0]);
        }
    }

    @Test
    public void testSerializeBigDecimalWithPrecisionAndScale() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectMapper bigDecimalMapper = JsonMapperFactory.builder()
                .withSerializerModifier(new StandardSerializerModifier.Builder().build())
                .build();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number bigDecimalTestCases[] = {new BigDecimal("123.456789012345678901234567890"), new BigDecimal("123.456"), new BigDecimal("123.45")};
        try {
            for (Number bigDecimal : bigDecimalTestCases) {
                SerializerProvider provider = bigDecimalMapper.getSerializerProvider();
                JsonGenerator generator = bigDecimalMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
                serializer.serialize(bigDecimal, generator, provider);
                generator.close();
                ObjectNode jsonNode = mapper.readValue(new ByteArrayInputStream(generator.getOutputStream().toByteArray()), ObjectNode.class);
                TextNode textNode = jsonNode.get("@numberType");
                String numberType = textNode.asText();
                TextNode precisionNode = jsonNode.get("@numberType/precision");
                int precision = precisionNode.asInt();
                TextNode scaleNode = jsonNode.get("@numberType/scale");
                int scale = scaleNode != null ? scaleNode.asInt() : 0;
                assertEquals("Expected correct number type, precision, and scale for BigDecimal", "number", numberType);
                assertEquals("Expected correct precision for BigDecimal", bigDecimal.toString().indexOf('.') > 0 ? bigDecimal.toString().split("\\.")[1].length() : 0, precision);
                assertEquals("Expected correct scale for BigDecimal", bigDecimalTestCases[0].toString().split("\\.")[1].length() - (bigDecimal.toString().indexOf('.') > 0 ? bigDecimal.toString().split("\\.")[1].length() : 0), scale);
            }
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for BigDecimal: " + bigDecimalTestCases[0]);
        } catch (IOException e) {
            fail("Unexpected IOException for BigDecimal: " + bigDecimalTestCases[0]);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNonFiniteNumber() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        try {
            SerializerProvider provider = mapper.getSerializerProvider();
            JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
            serializer.serialize(Double.POSITIVE_INFINITY, generator, provider);
            generator.close();
            fail("Expected JsonMappingException for non-finite number: Double.POSITIVE_INFINITY");
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for non-finite number: Double.POSITIVE_INFINITY");
        } catch (IOException e) {
            fail("Unexpected IOException for non-finite number: Double.POSITIVE_INFINITY");
        }

        try {
            SerializerProvider provider = mapper.getSerializerProvider();
            JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
            serializer.serialize(Double.NEGATIVE_INFINITY, generator, provider);
            generator.close();
            fail("Expected JsonMappingException for non-finite number: Double.NEGATIVE_INFINITY");
        } catch (JsonProcessingException e) {
            fail("Unexpected JsonProcessingException for non-finite number: Double.NEGATIVE_INFINITY");
        } catch (IOException e) {
            fail("Unexpected IOException for non-finite number: Double.NEGATIVE_INFINITY");
        }

        try {
            SerializerProvider provider = mapper.getSerializerProvider();
            JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getOutputFactory());
            serializer.serialize(Double.NaN, generator, provider);
            generator.close();
            fail("Expected JsonMappingException for non-finite number: Double.NaN");
        } catch (JsonProcessingException e) {
            // Expected JsonMappingException
        } catch (IOException e) {
            fail("Unexpected IOException for non-finite number: Double.NaN");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectMapper bigDecimalMapper = JsonMapperFactory.builder()
                .withSerializerModifier(new StandardSerializerModifier.Builder().build())
                .build();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        try {
            JsonFormatVisitorWrapper visitor = mapper.getFactory().getPolymorphicSerializerProvider().findValueSerializerProvider().findSerializer("number", Number.class);
            visitor.acceptJsonFormatVisitor(new ObjectMapper.DefaultSerializableJsonProperty().getName(), bigDecimalMapper.getTypeFactory().constructType(Number.class));
            assertNotNull("Expected non-null visitor for 'number' type", visitor);
            serializer.acceptJsonFormatVisitor(visitor, bigDecimalMapper.getTypeFactory().constructType(Number.class));
            assertSame("Expected the serializer to be the same for 'number' type", serializer, visitor.getSerializer());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException during acceptJsonFormatVisitor testing: " + e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException during acceptJsonFormatVisitor testing: " + e.getMessage());
        }
    }

    // Helper method to generate random BigDecimal values for testing
    private BigDecimal generateRandomBigDecimal() {
        BigDecimal coefficient = BigDecimal.ONE;
        BigDecimal numerator = BigDecimal.ONE;
        Random random = new Random();
        int precision = 3;
        int randomDigitsCount = 6;
        int exponent = random.nextInt(5);
        while (exponent > 0) {
            coefficient = coefficient.multiply(BigDecimal.TEN);
            exponent--;
        }
        while (precision > 0) {
            int digit = random.nextInt(10);
            numerator = numerator.multiply(BigDecimal.valueOf(digit));
            precision--;
        }
        return coefficient.add(numerator);
    }
}