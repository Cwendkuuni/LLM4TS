import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    private static final NumberSerializer NUMBER_SERIALIZER = NumberSerializer.instance;

    /**
     * Test method for {@link NumberSerializer#NumberSerializer(Class)} constructor.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void testNumberSerializerConstructor() throws InstantiationException, IllegalAccessException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Assert.assertSame(NUMBER_SERIALIZER, numberSerializer);
    }

    /**
     * Test method for {@link NumberSerializer#createContextual(SerializerProvider, BeanProperty)} method.
     * @throws JsonMappingException
     */
    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<Number> serializer = new NumberSerializer(Number.class).createContextual(null, null);
        Assert.assertSame(NUMBER_SERIALIZER, serializer);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)} method.
     * @throws IOException
     */
    @Test
    public void testSerialize_BigDecimal() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal bigDecimal = new BigDecimal("3.14");
        numberSerializer.serialize(bigDecimal, generator, new SerializerProvider());
        String serializedNumber = generator.getOutputContent();
        Assert.assertEquals("3.14", serializedNumber);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)} method.
     * @throws IOException
     */
    @Test
    public void testSerialize_BigInteger() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        numberSerializer.serialize(bigInteger, generator, new SerializerProvider());
        String serializedNumber = generator.getOutputContent();
        Assert.assertEquals("12345678901234567890", serializedNumber);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)} method.
     * @throws IOException
     */
    @Test
    public void testSerialize_Long() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        long longNumber = 1234567890L;
        numberSerializer.serialize(longNumber, generator, new SerializerProvider());
        String serializedNumber = generator.getOutputContent();
        Assert.assertEquals("1234567890", serializedNumber);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)} method.
     * @throws IOException
     */
    @Test
    public void testSerialize_Double() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        double doubleNumber = 3.14;
        numberSerializer.serialize(doubleNumber, generator, new SerializerProvider());
        String serializedNumber = generator.getOutputContent();
        Assert.assertEquals("3.14", serializedNumber);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)} method.
     * @throws IOException
     */
    @Test
    public void testSerialize_Float() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        float floatNumber = 3.14f;
        numberSerializer.serialize(floatNumber, generator, new SerializerProvider());
        String serializedNumber = generator.getOutputContent();
        Assert.assertEquals("3.14", serializedNumber);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)} method.
     * @throws IOException
     */
    @Test(expected = JsonProcessingException.class)
    public void testSerialize_InvalidNumberType() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Object obj = new Object();
        numberSerializer.serialize(obj, generator, new SerializerProvider());
    }

    /**
     * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)} method.
     * @throws JsonMappingException
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SchemaNode schemaNode = new NumberSerializer(Number.class).getSchema(mapper.getSerializerProvider(), Number.class);
        Assert.assertEquals("number", schemaNode.getSchemaName());
        Assert.assertTrue(schemaNode.getSchemaDefinition().getDescription().startsWith("As a fallback,"));
    }

    /**
     * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} method.
     * @throws JsonMappingException
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(null, null);
        new NumberSerializer(Number.class).acceptJsonFormatVisitor(visitor, Number.class);
    }
}