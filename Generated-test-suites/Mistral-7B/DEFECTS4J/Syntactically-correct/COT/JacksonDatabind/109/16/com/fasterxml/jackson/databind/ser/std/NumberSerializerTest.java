import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer.StaticSerializerMappings;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws JsonGenerationException, IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectNumbers numbers = new ObjectNumbers();

        serializer.serialize(numbers.integer, gen, new ObjectMapper().getDefaultSerializerProvider());
        serializer.serialize(numbers.longNumber, gen, new ObjectMapper().getDefaultSerializerProvider());
        serializer.serialize(numbers.floatNumber, gen, new ObjectMapper().getDefaultSerializerProvider());
        serializer.serialize(numbers.doubleNumber, gen, new ObjectMapper().getDefaultSerializerProvider());
        serializer.serialize(numbers.bigInteger, gen, new ObjectMapper().getDefaultSerializerProvider());
        serializer.serialize(numbers.bigDecimal, gen, new ObjectMapper().getDefaultSerializerProvider());

        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertNotNull(parser.getValueAsInt());
        assertNotNull(parser.getValueAsLong());
        assertNotNull(parser.getValueAsFloat());
        assertNotNull(parser.getValueAsDouble());
        assertNotNull(parser.getValueAsBigInteger());
        assertNotNull(parser.getValueAsBigDecimal());

        gen.close();
        parser.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidNumber() throws JsonGenerationException, IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Object object = new Object();
        serializer.serialize(object, gen, new ObjectMapper().getDefaultSerializerProvider());
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeString() throws JsonGenerationException, IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        String string = "test";
        serializer.serialize(string, gen, new ObjectMapper().getDefaultSerializerProvider());
    }

    @Test
    public void testCreateContextualSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper.DefaultTypingContext context = mapper.getDefaultTypingContext();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        assertNotNull(serializer.createContextual(mapper.getSerializerProvider(), context.forProperty("test")));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualSerializerInvalidType() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper.DefaultTypingContext context = mapper.getDefaultTypingContext();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        serializer.createContextual(mapper.getSerializerProvider(), context.forProperty("test", String.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = factory.constructJsonFormatVisitor(StaticSerializerMappings.FIND_FORMAT_VISITOR, null);

        serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Number.class));

        assertEquals("number", visitor.getFormatName());
        assertTrue(visitor.isExpectedFormatId(SerializableFormat.FullDefaultSerializableFormat.class));
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = factory.constructNumberFormatVisitor(SerializableFormat.FullDefaultSerializableFormat.class, null);

        serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(BigDecimal.class));

        assertEquals("number", visitor.getFormatName());
        assertTrue(visitor.isExpectedFormatId(SerializableFormat.FullDefaultSerializableFormat.class));
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigInteger() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = factory.constructNumberFormatVisitor(SerializableFormat.FullDefaultSerializableFormat.class, null);

        serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(BigInteger.class));

        assertEquals("number", visitor.getFormatName());
        assertTrue(visitor.isExpectedFormatId(SerializableFormat.FullDefaultSerializableFormat.class));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonNode schema = serializer.getSchema(mapper.getDefaultSerializerProvider(), mapper.getTypeFactory().constructType(Number.class));

        assertEquals("number", schema.asText());
    }
}

class ObjectNumbers {
    int integer = 1;
    long longNumber = 1L;
    float floatNumber = 1.0F;
    double doubleNumber = 1.0;
    BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
    BigDecimal bigDecimal = new BigDecimal("123456789012345678901234567890.123456");
}