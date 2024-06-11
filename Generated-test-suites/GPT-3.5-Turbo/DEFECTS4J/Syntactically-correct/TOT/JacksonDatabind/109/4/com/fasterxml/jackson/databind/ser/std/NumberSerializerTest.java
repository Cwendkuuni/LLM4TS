import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void serialize_shouldSerializeBigDecimal() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(new BigDecimal("123.45"), generator, provider);

        generator.flush();
        Assert.assertEquals("123.45", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeBigInteger() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(new BigInteger("12345"), generator, provider);

        generator.flush();
        Assert.assertEquals("12345", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeLong() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(123L, generator, provider);

        generator.flush();
        Assert.assertEquals("123", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeDouble() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(123.45, generator, provider);

        generator.flush();
        Assert.assertEquals("123.45", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeFloat() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(123.45f, generator, provider);

        generator.flush();
        Assert.assertEquals("123.45", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeInteger() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(123, generator, provider);

        generator.flush();
        Assert.assertEquals("123", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeByte() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize((byte) 123, generator, provider);

        generator.flush();
        Assert.assertEquals("123", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeShort() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize((short) 123, generator, provider);

        generator.flush();
        Assert.assertEquals("123", writer.toString());
    }

    @Test
    public void serialize_shouldSerializeFallback() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        numberSerializer.serialize(1234567890.0987654321, generator, provider);

        generator.flush();
        Assert.assertEquals("1234567890.0987654321", writer.toString());
    }

    @Test
    public void getSchema_shouldReturnNumberSchemaForBigInteger() {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        JsonNode schemaNode = numberSerializer.getSchema(provider, BigInteger.class);

        Assert.assertEquals("integer", schemaNode.get("type").asText());
    }

    @Test
    public void getSchema_shouldReturnNumberSchemaForBigDecimal() {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        JsonNode schemaNode = numberSerializer.getSchema(provider, BigDecimal.class);

        Assert.assertEquals("number", schemaNode.get("type").asText());
    }

    @Test
    public void acceptJsonFormatVisitor_shouldVisitIntFormatForBigInteger() throws JsonMappingException {
        JsonParser.NumberType numberType = JsonParser.NumberType.BIG_INTEGER;
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getDefaultTyper(null);

        numberSerializer.acceptJsonFormatVisitor(visitor, BigInteger.class);

        Assert.assertEquals(numberType, visitor.getNumberType());
    }

    @Test
    public void acceptJsonFormatVisitor_shouldVisitFloatFormatForBigDecimal() throws JsonMappingException {
        JsonParser.NumberType numberType = JsonParser.NumberType.BIG_DECIMAL;
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getDefaultTyper(null);

        numberSerializer.acceptJsonFormatVisitor(visitor, BigDecimal.class);

        Assert.assertEquals(numberType, visitor.getNumberType());
    }

}