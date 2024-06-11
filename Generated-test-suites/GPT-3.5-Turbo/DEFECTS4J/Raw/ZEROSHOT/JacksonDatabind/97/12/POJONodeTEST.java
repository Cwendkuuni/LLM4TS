import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() {
        POJONode pojoNode = new POJONode(null);
        byte[] binaryValue = new byte[]{0x01, 0x02};
        pojoNode._value = binaryValue;
        try {
            assertArrayEquals(binaryValue, pojoNode.binaryValue());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
        pojoNode._value = "test";
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
        pojoNode._value = "test";
        assertEquals("test", pojoNode.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertTrue(pojoNode.asBoolean(true));
        assertFalse(pojoNode.asBoolean(false));
        pojoNode._value = true;
        assertTrue(pojoNode.asBoolean(false));
        assertFalse(pojoNode.asBoolean(true));
        pojoNode._value = false;
        assertFalse(pojoNode.asBoolean(true));
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(10, pojoNode.asInt(10));
        pojoNode._value = 20;
        assertEquals(20, pojoNode.asInt(10));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(100L, pojoNode.asLong(100L));
        pojoNode._value = 200L;
        assertEquals(200L, pojoNode.asLong(100L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(10.0, pojoNode.asDouble(10.0), 0.0);
        pojoNode._value = 20.0;
        assertEquals(20.0, pojoNode.asDouble(10.0), 0.0);
    }

    @Test
    public void testSerialize() {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        try {
            pojoNode.serialize(mockGenerator, mockProvider);
            Mockito.verify(mockProvider).defaultSerializeNull(mockGenerator);
            assertEquals(null, pojoNode._value);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        pojoNode._value = new JsonSerializable() {
            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        try {
            pojoNode.serialize(mockGenerator, mockProvider);
            Mockito.verify((JsonSerializable) pojoNode._value).serialize(mockGenerator, mockProvider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        pojoNode._value = "test";
        try {
            pojoNode.serialize(mockGenerator, mockProvider);
            Mockito.verify(mockGenerator).writeObject(pojoNode._value);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(null, pojoNode.getPojo());
        Object testPojo = new Object();
        pojoNode._value = testPojo;
        assertSame(testPojo, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);
        assertTrue(pojoNode1.equals(pojoNode1));
        assertFalse(pojoNode1.equals(null));
        assertFalse(pojoNode1.equals("test"));
        assertTrue(pojoNode1.equals(pojoNode2));
        pojoNode1._value = "test";
        assertFalse(pojoNode1.equals(pojoNode2));
        pojoNode2._value = "test";
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0, pojoNode.hashCode());
        pojoNode._value = "test";
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.toString());
        byte[] binaryValue = new byte[]{0x01, 0x02};
        pojoNode._value = binaryValue;
        assertEquals("(binary value of 2 bytes)", pojoNode.toString());
        pojoNode._value = new RawValue("test");
        assertEquals("(raw value 'test')", pojoNode.toString());
        pojoNode._value = "test";
        assertEquals("test", pojoNode.toString());
    }
}