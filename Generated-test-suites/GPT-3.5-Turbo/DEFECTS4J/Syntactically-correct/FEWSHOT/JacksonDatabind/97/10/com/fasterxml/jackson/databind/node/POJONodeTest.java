
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = { 0x01, 0x02, 0x03 };
        POJONode node = new POJONode(bytes);
        assertArrayEquals(bytes, node.binaryValue());
        assertNull(new POJONode("test").binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(Boolean.TRUE);
        assertTrue(node.asBoolean(false));
        assertFalse(new POJONode(null).asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
        assertEquals(0, new POJONode(null).asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        assertEquals(100L, node.asLong(0L));
        assertEquals(0L, new POJONode(null).asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(10.5);
        assertEquals(10.5, node.asDouble(0), 0.001);
        assertEquals(0, new POJONode(null).asDouble(0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode("test");
        JsonGenerator generator = new JsonGenerator() {
            @Override
            public Version version() {
                return null;
            }

            @Override
            public ObjectCodec getCodec() {
                return null;
            }

            @Override
            public JsonGenerator setCodec(ObjectCodec objectCodec) {
                return null;
            }

            @Override
            public void writeFieldName(String s) {

            }

            @Override
            public void writeString(String s) {

            }

            @Override
            public void writeRawUTF8String(byte[] bytes, int i, int i1) {

            }

            @Override
            public void writeUTF8String(byte[] bytes, int i, int i1) {

            }

            @Override
            public void writeRaw(String s) {

            }

            @Override
            public void writeRaw(String s, int i, int i1) {

            }

            @Override
            public void writeRaw(char[] chars, int i, int i1) {

            }

            @Override
            public void writeRaw(char c) {

            }

            @Override
            public void writeRawValue(String s) {

            }

            @Override
            public void writeRawValue(String s, int i, int i1) {

            }

            @Override
            public void writeRawValue(char[] chars, int i, int i1) {

            }

            @Override
            public void writeBinary(Base64Variant base64Variant, byte[] bytes, int i, int i1) {

            }

            @Override
            public int writeBinary(Base64Variant base64Variant, InputStream inputStream, int i) throws IOException {
                return 0;
            }

            @Override
            public void writeNumber(int i) {

            }

            @Override
            public void writeNumber(long l) {

            }

            @Override
            public void writeNumber(BigInteger bigInteger) {

            }

            @Override
            public void writeNumber(double v) {

            }

            @Override
            public void writeNumber(float v) {

            }

            @Override
            public void writeNumber(BigDecimal bigDecimal) {

            }

            @Override
            public void writeNumber(String s) {

            }

            @Override
            public void writeBoolean(boolean b) {

            }

            @Override
            public void writeNull() {

            }

            @Override
            public void writeObject(Object o) {

            }

            @Override
            public void writeTree(TreeNode treeNode) {

            }

            @Override
            public void copyCurrentEvent(JsonParser jsonParser) throws IOException {

            }

            @Override
            public void copyCurrentStructure(JsonParser jsonParser) throws IOException {

            }

            @Override
            public JsonStreamContext getOutputContext() {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public int getFeatureMask() {
                return 0;
            }

            @Override
            public JsonGenerator overrideStdFeatures(int i, int i1) {
                return null;
            }

            @Override
            public JsonGenerator overrideFormatFeatures(int i, int i1) {
                return null;
            }
        };
        node.serialize(generator, null);
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertSame(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("other");
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(null));
        assertFalse(node1.equals("test"));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode("test");
        assertEquals("test", node1.toString());
        POJONode node2 = new POJONode(new byte[] { 0x01, 0x02, 0x03 });
        assertEquals("(binary value of 3 bytes)", node2.toString());
        POJONode node3 = new POJONode(new RawValue("raw value"));
        assertEquals("(raw value 'raw value')", node3.toString());
    }
}
