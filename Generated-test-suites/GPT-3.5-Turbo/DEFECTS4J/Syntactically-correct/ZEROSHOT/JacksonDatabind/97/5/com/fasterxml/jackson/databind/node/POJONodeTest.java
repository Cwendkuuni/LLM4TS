package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = new byte[]{1, 2, 3};
        POJONode node = new POJONode(bytes);
        
        assertArrayEquals(bytes, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        String defaultValue = "default";
        
        POJONode node = new POJONode(null);
        assertEquals(defaultValue, node.asText(defaultValue));
        
        node = new POJONode("test");
        assertEquals("test", node.asText(defaultValue));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertEquals(true, node.asBoolean(false));
        
        node = new POJONode("test");
        assertEquals(false, node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(123);
        assertEquals(123, node.asInt(0));
        
        node = new POJONode("test");
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(123L);
        assertEquals(123L, node.asLong(0L));
        
        node = new POJONode("test");
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(123.45);
        assertEquals(123.45, node.asDouble(0.0), 0.01);
        
        node = new POJONode("test");
        assertEquals(0.0, node.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode("test");
        
        MockJsonGenerator generator = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        
        node.serialize(generator, provider);
        
        assertEquals("test", generator.getWriteObject());
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.toString());
    }

    private static class MockJsonGenerator extends JsonGenerator {

        private String writeObject;

        public String getWriteObject() {
            return writeObject;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public int getFeatureMask() {
            return 0;
        }

        @Override
        public JsonGenerator setFeatureMask(int values) {
            return null;
        }

        @Override
        public JsonGenerator useDefaultPrettyPrinter() {
            return null;
        }

        @Override
        public JsonGenerator setCodec(ObjectCodec oc) {
            return null;
        }

        @Override
        public ObjectCodec getCodec() {
            return null;
        }

        @Override
        public Version version() {
            return null;
        }

        @Override
        public JsonGenerator enable(Feature f) {
            return null;
        }

        @Override
        public JsonGenerator disable(Feature f) {
            return null;
        }

        @Override
        public boolean isEnabled(Feature f) {
            return false;
        }

        @Override
        public int getFeatureMask(Feature... features) {
            return 0;
        }

        @Override
        public JsonGenerator configure(Feature f, boolean state) {
            return null;
        }

        @Override
        public JsonGenerator setPrettyPrinter(PrettyPrinter pp) {
            return null;
        }

        @Override
        public PrettyPrinter getPrettyPrinter() {
            return null;
        }

        @Override
        public JsonGenerator useRootName(PropertyName rootName) {
            return null;
        }

        @Override
        public void writeStartArray() throws IOException {
        }

        @Override
        public void writeEndArray() throws IOException {
        }

        @Override
        public void writeStartObject(Object forValue) throws IOException {
        }

        @Override
        public void writeEndObject() throws IOException {
        }

        @Override
        public void writeFieldName(String name) throws IOException {
        }

        @Override
        public void writeFieldName(SerializableString name) throws IOException {
        }

        @Override
        public void writeString(String text) throws IOException {
        }

        @Override
        public void writeString(char[] text, int offset, int len) throws IOException {
        }

        @Override
        public void writeString(SerializableString text) throws IOException {
        }

        @Override
        public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
        }

        @Override
        public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
        }

        @Override
        public void writeRaw(String text) throws IOException {
        }

        @Override
        public void writeRaw(String text, int offset, int len) throws IOException {
        }

        @Override
        public void writeRaw(char[] text, int offset, int len) throws IOException {
        }

        @Override
        public void writeRaw(char c) throws IOException {
        }

        @Override
        public void writeRawValue(String text) throws IOException {
        }

        @Override
        public void writeRawValue(String text, int offset, int len) throws IOException {
        }

        @Override
        public void writeRawValue(char[] text, int offset, int len) throws IOException {
        }

        @Override
        public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {
        }

        @Override
        public int writeBinary(Base64Variant bv, InputStream data, int dataLength) throws IOException {
            return 0;
        }

        @Override
        public void writeNumber(int v) throws IOException {
        }

        @Override
        public void writeNumber(long v) throws IOException {
        }

        @Override
        public void writeNumber(BigInteger v) throws IOException {
        }

        @Override
        public void writeNumber(double v) throws IOException {
        }

        @Override
        public void writeNumber(float v) throws IOException {
        }

        @Override
        public void writeNumber(BigDecimal v) throws IOException {
        }

        @Override
        public void writeNumber(String encodedValue) throws IOException {
            this.writeObject = encodedValue;
        }

        @Override
        public void writeBoolean(boolean state) throws IOException {
        }

        @Override
        public void writeNull() throws IOException {
        }

        @Override
        public void writeObject(Object pojo) throws IOException {
            this.writeObject = pojo.toString();
        }

        @Override
        public void writeTree(TreeNode tree) throws IOException {
        }
    }

    private static class MockSerializerProvider extends SerializerProvider {

        @Override
        public void defaultSerializeValue(Object value, JsonGenerator gen) throws IOException {
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(Class<?> valueType) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonGenerator getGenerator() {
            return null;
        }
    }
}