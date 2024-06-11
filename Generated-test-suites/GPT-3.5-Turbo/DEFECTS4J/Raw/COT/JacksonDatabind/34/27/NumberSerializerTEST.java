
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Test BigDecimal
        JsonGeneratorMock generatorMock = new JsonGeneratorMock();
        numberSerializer.serialize(new BigDecimal(10), generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("10", generatorMock.writeValue);

        // Test BigInteger
        generatorMock.reset();
        numberSerializer.serialize(new BigInteger("100"), generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("100", generatorMock.writeValue);

        // Test Integer
        generatorMock.reset();
        numberSerializer.serialize(5, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("5", generatorMock.writeValue);

        // Test Long
        generatorMock.reset();
        numberSerializer.serialize(10L, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("10", generatorMock.writeValue);

        // Test Double
        generatorMock.reset();
        numberSerializer.serialize(1.5, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("1.5", generatorMock.writeValue);

        // Test Float
        generatorMock.reset();
        numberSerializer.serialize(0.5f, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("0.5", generatorMock.writeValue);

        // Test Byte
        generatorMock.reset();
        numberSerializer.serialize((byte) 2, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("2", generatorMock.writeValue);

        // Test Short
        generatorMock.reset();
        numberSerializer.serialize((short) 3, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("3", generatorMock.writeValue);

        // Test other types
        generatorMock.reset();
        numberSerializer.serialize(2.5, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("2.5", generatorMock.writeValue);

        generatorMock.reset();
        numberSerializer.serialize((byte) -2, generatorMock, null);
        assertTrue(generatorMock.writeValueCalled);
        assertEquals("-2", generatorMock.writeValue);
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Test for BigInteger
        JsonNode schemaNode = numberSerializer.getSchema(null, BigInteger.class);
        assertTrue(schemaNode.isTextual());
        assertEquals("integer", schemaNode.textValue());

        // Test for other types
        schemaNode = numberSerializer.getSchema(null, Double.class);
        assertTrue(schemaNode.isTextual());
        assertEquals("number", schemaNode.textValue());

        schemaNode = numberSerializer.getSchema(null, BigDecimal.class);
        assertTrue(schemaNode.isTextual());
        assertEquals("number", schemaNode.textValue());
    }
}
