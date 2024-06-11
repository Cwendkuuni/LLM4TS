import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);

        Number number = 1;
        serializer.serialize(number, generator, null);
        generator.flush();

        Number deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = BigInteger.valueOf(1);
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = new BigDecimal("1.0");
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = 1.0;
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = -1;
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = Long.MIN_VALUE;
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = Double.NaN;
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = Float.NaN;
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = new Byte((byte) 1);
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = (short) 1;
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = new BigInteger("1000000000000000001");
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);

        number = new BigDecimal("123456789012345678901234567890");
        serializer.serialize(number, generator, null);
        generator.flush();

        deserializedNumber = MAPPER.readValue(System.in, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test(expected = IOException.class)
    public void testSerializeUnsupportedType() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);

        String unsupportedType = "String";
        serializer.serialize(unsupportedType, generator, null);
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = NumberSerializer.instance;
        Schema schema = serializer.getSchema(null, Number.class);

        assertEquals("number", schema.getType());
        assertTrue(schema.isContainerNode());
        assertTrue(schema.isMapContainerNode());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null; //Stub the visitor

        serializer.acceptJsonFormatVisitor(visitor, Number.class.getType());
    }
}