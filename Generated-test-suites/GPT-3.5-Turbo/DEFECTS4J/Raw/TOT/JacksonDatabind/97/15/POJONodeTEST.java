import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class POJONodeTest {

    private POJONode pojoNode;

    @Before
    public void setUp() {
        pojoNode = new POJONode("test");
    }

    @After
    public void tearDown() {
        pojoNode = null;
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValueWhenValueIsByteArray() throws IOException {
        byte[] byteArray = {1, 2, 3};
        POJONode byteArrayNode = new POJONode(byteArray);
        assertArrayEquals(byteArray, byteArrayNode.binaryValue());
    }

    @Test
    public void testBinaryValueWhenValueIsNotByteArray() throws IOException {
        assertNull(pojoNode.binaryValue());
    }

    @Test
    public void testAsTextWhenValueIsNull() {
        POJONode nullNode = new POJONode(null);
        assertEquals("null", nullNode.asText());
    }

    @Test
    public void testAsTextWhenValueIsNotNull() {
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        assertEquals("default", pojoNode.asText("default"));
    }

    @Test
    public void testAsBooleanWhenValueIsBoolean() {
        POJONode booleanNode = new POJONode(true);
        assertTrue(booleanNode.asBoolean(false));
    }

    @Test
    public void testAsBooleanWhenValueIsNotBoolean() {
        assertFalse(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsIntWhenValueIsInt() {
        POJONode intNode = new POJONode(10);
        assertEquals(10, intNode.asInt(0));
    }

    @Test
    public void testAsIntWhenValueIsNotInt() {
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLongWhenValueIsLong() {
        POJONode longNode = new POJONode(100L);
        assertEquals(100L, longNode.asLong(0L));
    }

    @Test
    public void testAsLongWhenValueIsNotLong() {
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDoubleWhenValueIsDouble() {
        POJONode doubleNode = new POJONode(3.14);
        assertEquals(3.14, doubleNode.asDouble(0.0), 0.0001);
    }

    @Test
    public void testAsDoubleWhenValueIsNotDouble() {
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerializeWhenValueIsNull() throws IOException {
        POJONode nullNode = new POJONode(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        SerializerProvider dummyProvider = null;
        nullNode.serialize(dummyGenerator, dummyProvider);
        assertTrue(dummyGenerator.defaultSerializeNullMethodCalled);
    }

    @Test
    public void testSerializeWhenValueIsJsonSerializable() throws IOException {
        JsonSerializable jsonSerializable = new DummyJsonSerializable();
        POJONode jsonSerializableNode = new POJONode(jsonSerializable);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        SerializerProvider dummyProvider = null;
        jsonSerializableNode.serialize(dummyGenerator, dummyProvider);
        assertTrue(dummyGenerator.serializeMethodCalled);
    }

    @Test
    public void testSerializeWhenValueIsNotJsonSerializable() throws IOException {
        DummyObject dummyObject = new DummyObject();
        POJONode nonSerializableNode = new POJONode(dummyObject);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        SerializerProvider dummyProvider = null;
        nonSerializableNode.serialize(dummyGenerator, dummyProvider);
        assertTrue(dummyGenerator.writeObjectMethodCalled);
    }

    @Test
    public void testGetPojo() {
        assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEqualsWhenObjectIsNull() {
        assertFalse(pojoNode.equals(null));
    }

    @Test
    public void testEqualsWhenObjectIsSameInstance() {
        assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    public void testEqualsWhenObjectIsNotPOJONode() {
        assertFalse(pojoNode.equals(new DummyObject()));
    }

    @Test
    public void testEqualsWhenValuesAreEqual() {
        POJONode equalNode = new POJONode("test");
        assertTrue(pojoNode.equals(equalNode));
    }

    @Test
    public void testEqualsWhenValuesAreNotEqual() {
        POJONode notEqualNode = new POJONode("different");
        assertFalse(pojoNode.equals(notEqualNode));
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToStringWhenValueIsByteArray() {
        byte[] byteArray = {1, 2, 3};
        POJONode byteArrayNode = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", byteArrayNode.toString());
    }

    @Test
    public void testToStringWhenValueIsRawValue() {
        RawValue rawValue = new RawValue("raw");
        POJONode rawValueNode = new POJONode(rawValue);
        assertEquals("(raw value 'raw')", rawValueNode.toString());
    }

    @Test
    public void testToStringWhenValueIsNotByteArrayOrRawValue() {
        assertEquals("test", pojoNode.toString());
    }

    private static class DummyJsonGenerator extends JsonGenerator {
        boolean defaultSerializeNullMethodCalled;
        boolean serializeMethodCalled;
        boolean writeObjectMethodCalled;

        @Override
        public void writeStartObject() throws IOException {
        }

        @Override
        public void writeEndObject() throws IOException {
        }

        @Override
        public void writeFieldName(String name) throws IOException {
        }

        @Override
        public void writeString(String text) throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void writeBinary(byte[] data, int offset, int len) throws IOException {
        }

        @Override
        public void writeNumber(int v) throws IOException {
        }

        @Override
        public void writeNumber(long v) throws IOException {
        }

        @Override
        public void writeNumber(double d) throws IOException {
        }

        @Override
        public void writeBoolean(boolean state) throws IOException {
        }

        @Override
        public void writeNull() throws IOException {
            defaultSerializeNullMethodCalled = true;
        }

        @Override
        public void writeObject(Object pojo) throws IOException {
            writeObjectMethodCalled = true;
        }

        @Override
        public void writeRawValue(String value) throws IOException {
        }

        @Override
        public void writeRawValue(String value, int offset, int length) throws IOException {
        }

        @Override
        public void writeRawValue(char[] value, int offset, int length) throws IOException {
        }

        @Override
        protected void _releaseBuffers() {
        }
    }

    private static class DummyJsonSerializable implements JsonSerializable {

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("jsonSerializable");
        }

        @Override
        public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        }
    }

    private static class DummyObject {
    }
}