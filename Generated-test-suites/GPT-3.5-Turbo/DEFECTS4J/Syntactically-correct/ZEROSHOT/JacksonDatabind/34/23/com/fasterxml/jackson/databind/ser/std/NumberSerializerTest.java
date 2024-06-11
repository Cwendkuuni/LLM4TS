import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;

public class NumberSerializerTest {

    @Test
    public void testSerialize_Integer() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(10, generator, provider);
        assertEquals(generator.getNumberValue(), 10);
    }

    @Test
    public void testSerialize_Long() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(100L, generator, provider);
        assertEquals(generator.getNumberValue(), 100L);
    }

    @Test
    public void testSerialize_Double() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(3.14, generator, provider);
        assertEquals(generator.getNumberValue(), 3.14, 0.001);
    }

    @Test
    public void testSerialize_Float() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(2.718f, generator, provider);
        assertEquals(generator.getNumberValue(), 2.718f, 0.001f);
    }

    @Test
    public void testSerialize_Byte() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize((byte) 5, generator, provider);
        assertEquals(generator.getNumberValue(), 5);
    }

    @Test
    public void testSerialize_Short() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize((short) 3, generator, provider);
        assertEquals(generator.getNumberValue(), 3);
    }

    @Test
    public void testSerialize_BigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(new BigDecimal("3.141592653589793238462643383"), generator, provider);
        assertEquals(generator.getNumberValue(), new BigDecimal("3.141592653589793238462643383"));
    }

    @Test
    public void testSerialize_BigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(new BigInteger("1234567890"), generator, provider);
        assertEquals(generator.getNumberValue(), new BigInteger("1234567890"));
    }

    @Test
    public void testSerialize_OtherNumber() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(7.77, generator, provider);
        assertEquals(generator.getNumberValue(), 7.77, 0.001);
    }

    @Test
    public void testGetSchema_Integer() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        SerializerProvider provider = new SerializerProvider();

        JsonNode schema = serializer.getSchema(provider, Integer.TYPE);
        assertEquals(schema.get("type").textValue(), "integer");
    }

    @Test
    public void testGetSchema_BigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = new SerializerProvider();

        JsonNode schema = serializer.getSchema(provider, BigInteger.class);
        assertEquals(schema.get("type").textValue(), "integer");
    }

    @Test
    public void testGetSchema_BigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = new SerializerProvider();

        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);
        assertEquals(schema.get("type").textValue(), "number");
    }

    @Test
    public void testAcceptJsonFormatVisitor_Integer() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonFormatVisitorWrapperMock visitor = new JsonFormatVisitorWrapperMock();
        JavaType typeHint = new JavaTypeMock(Integer.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(visitor.getNumberType(), JsonParser.NumberType.INT);
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapperMock visitor = new JsonFormatVisitorWrapperMock();
        JavaType typeHint = new JavaTypeMock(BigDecimal.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(visitor.getNumberType(), JsonParser.NumberType.BIG_DECIMAL);
    }

    @Test
    public void testAcceptJsonFormatVisitor_OtherType() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapperMock visitor = new JsonFormatVisitorWrapperMock();
        JavaType typeHint = new JavaTypeMock(Number.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(visitor.getNumberType(), JsonParser.NumberType.FLOAT);
    }

}