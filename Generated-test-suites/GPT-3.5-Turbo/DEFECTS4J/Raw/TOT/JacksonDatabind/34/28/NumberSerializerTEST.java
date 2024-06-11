
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumberSerializerTest {

    private ObjectMapper mapper;
    private StringWriter writer;
    private JsonGenerator generator;
    private SerializerProvider provider;

    @Before
    public void setUp() throws IOException {
        mapper = new ObjectMapper();
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        provider = mapper.getSerializerProvider();
    }

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal value = new BigDecimal("123.45");
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123.45", writer.toString());
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger value = new BigInteger("123456789");
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123456789", writer.toString());
    }

    @Test
    public void testSerialize_Integer() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Integer value = 123;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123", writer.toString());
    }

    @Test
    public void testSerialize_Long() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Long value = 123456789L;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123456789", writer.toString());
    }

    @Test
    public void testSerialize_Double() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Double value = 123.45;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123.45", writer.toString());
    }

    @Test
    public void testSerialize_Float() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Float value = 123.45f;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123.45", writer.toString());
    }

    @Test
    public void testSerialize_Byte() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Byte value = 123;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123", writer.toString());
    }

    @Test
    public void testSerialize_Short() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Short value = 123;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123", writer.toString());
    }

    @Test
    public void testSerialize_OtherNumberType() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number value = 123.45;
        
        serializer.serialize(value, generator, provider);
        generator.close();
        
        assertEquals("123.45", writer.toString());
    }

    @Test
    public void testGetSchema_Integer() {
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal value = new BigDecimal("123.45");

        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);
        
        assertTrue(schema.isNumber());
        assertTrue(schema.isIntegralNumber());
        assertTrue(schema.isInt());
        assertEquals("integer", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchema_BigInteger() {
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger value = new BigInteger("123456789");

        JsonNode schema = serializer.getSchema(provider, BigInteger.class);
        
        assertTrue(schema.isNumber());
        assertTrue(schema.isIntegralNumber());
        assertTrue(schema.isInt());
        assertEquals("integer", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchema_OtherNumberType() {
        NumberSerializer serializer = NumberSerializer.instance;
        Number value = 123.45;

        JsonNode schema = serializer.getSchema(provider, Number.class);
        
        assertTrue(schema.isNumber());
        assertTrue(schema.isInt() && schema.isFloat());
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor_Integer() {
        NumberSerializer serializer = NumberSerializer.instance;
        
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).expectNumberFormat(typeHint);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigDecimal() {
        NumberSerializer serializer = NumberSerializer.instance;
        
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).expectFloatFormat(typeHint, JsonParser.NumberType.BIG_DECIMAL);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitor_OtherNumberType() {
        NumberSerializer serializer = NumberSerializer.instance;
        
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).expectNumberFormat(typeHint);
        verifyNoMoreInteractions(visitor);
    }
}

