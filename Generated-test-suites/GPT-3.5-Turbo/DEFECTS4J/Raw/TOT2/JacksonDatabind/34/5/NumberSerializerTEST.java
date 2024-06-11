import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator g = null; // Mock JsonGenerator for testing
        SerializerProvider provider = null; // Mock SerializerProvider for testing
        BigDecimal value = BigDecimal.TEN;
        serializer.serialize(value, g, provider);
        // Add assertion here to check serialization
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator g = null; // Mock JsonGenerator for testing
        SerializerProvider provider = null; // Mock SerializerProvider for testing
        BigInteger value = BigInteger.TEN;
        serializer.serialize(value, g, provider);
        // Add assertion here to check serialization
    }

    // Add more test cases for other public methods

    @Test
    public void testGetSchema_Integer() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        SerializerProvider provider = null; // Mock SerializerProvider for testing
        Type typeHint = null; // Mock Type for testing
        JsonNode schema = serializer.getSchema(provider, typeHint);
        // Add assertion here to check schema generation
    }

    @Test
    public void testAcceptJsonFormatVisitor_Int() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = null; // Mock JsonFormatVisitorWrapper for testing
        JavaType typeHint = null; // Mock JavaType for testing
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add assertion here to check Json format visitor
    }

    // Add more test cases for edge cases and error scenarios

}