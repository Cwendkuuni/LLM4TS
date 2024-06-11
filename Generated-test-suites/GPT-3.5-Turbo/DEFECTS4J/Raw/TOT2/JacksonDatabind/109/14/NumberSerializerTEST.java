import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);

        // Test for BigDecimal
        BigDecimal testBigDecimal = new BigDecimal("123.456");
        serializer.serialize(testBigDecimal, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testBigDecimal);

        // Test for BigInteger
        BigInteger testBigInteger = new BigInteger("123456789");
        serializer.serialize(testBigInteger, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testBigInteger);

        // Test for Long
        Long testLong = 123L;
        serializer.serialize(testLong, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testLong);

        // Test for Double
        Double testDouble = 123.456;
        serializer.serialize(testDouble, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testDouble);

        // Test for Float
        Float testFloat = 123.456f;
        serializer.serialize(testFloat, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testFloat);

        // Test for Integer
        Integer testInt = 123;
        serializer.serialize(testInt, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testInt.intValue());

        // Test for Byte
        Byte testByte = 1;
        serializer.serialize(testByte, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testByte.intValue());

        // Test for Short
        Short testShort = 10;
        serializer.serialize(testShort, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(testShort.intValue());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);

        Type typeHint = new TypeToken<Number>(){}.getType();
        JsonNode schema = serializer.getSchema(mockProvider, typeHint);

        assertTrue(schema.isObject());
        assertTrue(schema.has("type"));
        if(serializer._isInt) {
            assertEquals("integer", schema.get("type").asText());
        } else {
            assertEquals("number", schema.get("type").asText());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper mockVisitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Number.class);

        serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);

        if(serializer._isInt) {
            Mockito.verify(mockVisitor).expectIntegerFormat(typeHint);
        } else {
            Mockito.verify(mockVisitor).expectNumberFormat(typeHint);
        }
    }
}