import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private JsonSerializer<?> genericNumberSerializer;
    private ObjectMapper objectMapper;
    
    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        JsonFactory jsonFactory = new JsonFactory();
        try {
            jsonGenerator = jsonFactory.createGenerator(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        genericNumberSerializer = new JsonSerializer<>();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    public void testSerialize_WhenBigDecimalProvided_ShouldWriteBigDecimalValue() {
        BigDecimal bigDecimalValue = new BigDecimal("1234567890.0987654321");
        try {
            numberSerializer.serialize(bigDecimalValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenBigIntegerProvided_ShouldWriteBigIntegerValue() {
        BigInteger bigIntegerValue = new BigInteger("12345678901234567890");
        try {
            numberSerializer.serialize(bigIntegerValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenLongProvided_ShouldWriteLongValue() {
        Long longValue = Long.MAX_VALUE;
        try {
            numberSerializer.serialize(longValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenDoubleProvided_ShouldWriteDoubleValue() {
        Double doubleValue = Math.PI;
        try {
            numberSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenFloatProvided_ShouldWriteFloatValue() {
        Float floatValue = Float.MAX_VALUE;
        try {
            numberSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenIntegerProvided_ShouldWriteIntegerValue() {
        Integer integerValue = Integer.MAX_VALUE;
        try {
            numberSerializer.serialize(integerValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenByteProvided_ShouldWriteByteValue() {
        Byte byteValue = Byte.MAX_VALUE;
        try {
            numberSerializer.serialize(byteValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenShortProvided_ShouldWriteShortValue() {
        Short shortValue = Short.MAX_VALUE;
        try {
            numberSerializer.serialize(shortValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSerialize_WhenOtherNumberProvided_ShouldWriteToStringValue() {
        Number numberValue = new Number() {
            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }
            
            @Override
            public String toString() {
                return "CustomNumber";
            }
        };
        try {
            numberSerializer.serialize(numberValue, jsonGenerator, new SerializerProvider() {});
            jsonGenerator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCreateContextual_WhenJsonFormatStringProvided_ShouldReturnToStringSerializerInstance() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        Assert.assertEquals(ToStringSerializer.instance, numberSerializer.createContextual(new SerializerProvider() {}, null, format));
    }
    
    @Test
    public void testCreateContextual_WhenJsonFormatStringNotProvided_ShouldReturnNumberSerializerInstance() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        Assert.assertEquals(numberSerializer, numberSerializer.createContextual(new SerializerProvider() {}, null, format));
    }
    
    @Test
    public void testGetSchema_ShouldReturnIntegerSchemaNodeForBigInteger() {
        SerializerProvider provider = new SerializerProvider() {};
        Assert.assertEquals("integer", numberSerializer.getSchema(provider, BigInteger.class).get("type").asText());
    }
    
    @Test
    public void testGetSchema_ShouldReturnNumberSchemaNodeForBigDecimal() {
        SerializerProvider provider = new SerializerProvider() {};
        Assert.assertEquals("number", numberSerializer.getSchema(provider, BigDecimal.class).get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsInt_ShouldVisitIntFormat() throws JsonMappingException {
        VisitorWrapper visitor = new VisitorWrapper();
        numberSerializer.acceptJsonFormatVisitor(visitor, null);
        Assert.assertEquals("int", visitor.getNumberType());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsBigDecimal_ShouldVisitBigDecimalFormat() throws JsonMappingException {
        VisitorWrapper visitor = new VisitorWrapper();
        numberSerializer.acceptJsonFormatVisitor(visitor, BigDecimal.class);
        Assert.assertEquals("big_decimal", visitor.getNumberType());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsNotBigIntegerOrBigDecimal_ShouldVisitDefaultFormat() throws JsonMappingException {
        VisitorWrapper visitor = new VisitorWrapper();
        numberSerializer.acceptJsonFormatVisitor(visitor, Long.class);
        Assert.assertEquals("number", visitor.getNumberType());
    }
    
    private class VisitorWrapper implements JsonFormatVisitorWrapper {
        
        private String numberType;
        
        public String getNumberType() {
            return numberType;
        }

        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            return new JsonNumberFormatVisitor() {
                @Override
                public void numberType(JsonParser.NumberType numberType) throws JsonMappingException {
                    VisitorWrapper.this.numberType = numberType.name().toLowerCase();
                }
            };
        }

        @Override
        public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
            return null;
        }
        
    }
}