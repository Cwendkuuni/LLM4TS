import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void serializeShouldSerializeBigDecimal() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        BigDecimal value = new BigDecimal("10.5");

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10.5", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeBigInteger() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        BigInteger value = new BigInteger("10");

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeInteger() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Integer value = 10;

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeLong() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Long value = 100000000000L;

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("100000000000", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeDouble() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Double value = 10.5;

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10.5", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeFloat() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Float value = 10.5f;

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10.5", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeShort() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Short value = 10;

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeByte() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Byte value = 10;

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("10", generatorStub.getLastNumberValue());
    }

    @Test
    public void serializeShouldSerializeOtherNumberTypes() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        Number value = new ComplexNumber(2, 3);

        // Act
        numberSerializer.serialize(value, generatorStub, null);

        // Assert
        assertEquals("2+3i", generatorStub.getLastNumberValue());
    }

    @Test
    public void getSchemaShouldReturnIntegerSchemaForBigInteger() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);

        // Act
        JsonNode schema = numberSerializer.getSchema(null, null);

        // Assert
        assertEquals("integer", schema.asText());
    }

    @Test
    public void getSchemaShouldReturnNumberSchemaForOtherTypes() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Act
        JsonNode schema = numberSerializer.getSchema(null, null);

        // Assert
        assertEquals("number", schema.asText());
    }

    @Test
    public void acceptJsonFormatVisitorShouldVisitIntFormatForBigInteger() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapperStub visitorStub = new JsonFormatVisitorWrapperStub();
        JavaType typeHint = null;

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorStub, typeHint);

        // Assert
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitorStub.getLastVisitedIntFormat());
    }

    @Test
    public void acceptJsonFormatVisitorShouldVisitFloatFormatForBigDecimal() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapperStub visitorStub = new JsonFormatVisitorWrapperStub();
        JavaType typeHint = null;

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorStub, typeHint);

        // Assert
        assertEquals(JsonParser.NumberType.DOUBLE, visitorStub.getLastVisitedFloatFormat());
    }

    @Test
    public void acceptJsonFormatVisitorShouldVisitNumberFormatForOtherTypes() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapperStub visitorStub = new JsonFormatVisitorWrapperStub();
        JavaType typeHint = null;

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorStub, typeHint);

        // Assert
        assertEquals(JsonParser.NumberType.BIG_DECIMAL, visitorStub.getLastVisitedNumberFormat());
    }
    
    private static class JsonGeneratorStub extends JsonGenerator {
        private String lastNumberValue;

        public String getLastNumberValue() {
            return lastNumberValue;
        }

        @Override
        public void writeNumber(int v) throws IOException {
            lastNumberValue = String.valueOf(v);
        }

        @Override
        public void writeNumber(long v) throws IOException {
            lastNumberValue = String.valueOf(v);
        }

        @Override
        public void writeNumber(BigInteger v) throws IOException {
            lastNumberValue = v.toString();
        }

        @Override
        public void writeNumber(double v) throws IOException {
            lastNumberValue = String.valueOf(v);
        }

        @Override
        public void writeNumber(float v) throws IOException {
            lastNumberValue = String.valueOf(v);
        }

        @Override
        public void writeNumber(BigDecimal v) throws IOException {
            lastNumberValue = v.toString();
        }

        @Override
        public void writeNumber(String encodedValue) throws IOException {
            lastNumberValue = encodedValue;
        }

        // Other overridden methods omitted for brevity
    }

    private static class JsonFormatVisitorWrapperStub extends JsonFormatVisitorWrapper {
        private JsonParser.NumberType lastVisitedIntFormat;
        private JsonParser.NumberType lastVisitedFloatFormat;
        private JsonParser.NumberType lastVisitedNumberFormat;

        public JsonParser.NumberType getLastVisitedIntFormat() {
            return lastVisitedIntFormat;
        }

        public JsonParser.NumberType getLastVisitedFloatFormat() {
            return lastVisitedFloatFormat;
        }

        public JsonParser.NumberType getLastVisitedNumberFormat() {
            return lastVisitedNumberFormat;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType typeHint) throws JsonMappingException {
            return new JsonNumberFormatVisitor() {
                @Override
                public void numberType(JsonParser.NumberType type) {
                    lastVisitedNumberFormat = type;
                }
            };
        }

        @Override
        public void expectIntFormat(JavaType typeHint) throws JsonMappingException {
            lastVisitedIntFormat = JsonParser.NumberType.INT;
        }

        @Override
        public void expectFloatFormat(JavaType typeHint) throws JsonMappingException {
            lastVisitedFloatFormat = JsonParser.NumberType.DOUBLE;
        }

        // Other overridden methods omitted for brevity
    }
    
    private static class ComplexNumber extends Number {
        private double real;
        private double imaginary;

        public ComplexNumber(double real, double imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }

        public double getReal() {
            return real;
        }

        public double getImaginary() {
            return imaginary;
        }

        @Override
        public int intValue() {
            return (int) real;
        }

        @Override
        public long longValue() {
            return (long) real;
        }

        @Override
        public float floatValue() {
            return (float) real;
        }

        @Override
        public double doubleValue() {
            return real;
        }
    }
}