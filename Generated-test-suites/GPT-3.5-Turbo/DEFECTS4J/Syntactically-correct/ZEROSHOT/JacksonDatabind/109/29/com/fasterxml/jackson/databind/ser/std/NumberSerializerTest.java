import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializer() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        assertNotNull(serializer);
        assertTrue(serializer instanceof ContextualSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider prov = null;
        BeanProperty property = null;

        assertNull(serializer.createContextual(prov, property));
    }

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        // Test with BigDecimal
        BigDecimal value = new BigDecimal("10.5");
        serializer.serialize(value, generator, provider);
        // Verify

        // Test with BigInteger
        BigInteger value2 = new BigInteger("10");
        serializer.serialize(value2, generator, provider);
        // Verify

        // Test with Long
        Long value3 = new Long("10");
        serializer.serialize(value3, generator, provider);
        // Verify

        // Test with Double
        Double value4 = new Double("10.5");
        serializer.serialize(value4, generator, provider);
        // Verify

        // Test with Float
        Float value5 = new Float("10.5");
        serializer.serialize(value5, generator, provider);
        // Verify

        // Test with Integer
        Integer value6 = new Integer("10");
        serializer.serialize(value6, generator, provider);
        // Verify

        // Test with Byte
        Byte value7 = new Byte("1");
        serializer.serialize(value7, generator, provider);
        // Verify

        // Test with Short
        Short value8 = new Short("1");
        serializer.serialize(value8, generator, provider);
        // Verify
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Verify
    }
}