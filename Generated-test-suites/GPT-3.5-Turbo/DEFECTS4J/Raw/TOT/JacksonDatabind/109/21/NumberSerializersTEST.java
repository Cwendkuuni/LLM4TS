import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = ShortSerializer.instance;
        DummyJsonGenerator generator = new DummyJsonGenerator();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize((short) 42, generator, provider);

        assertEquals("42", generator.output());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        DummyJsonGenerator generator = new DummyJsonGenerator();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(42, generator, provider);

        assertEquals("42", generator.output());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        DummyJsonGenerator generator = new DummyJsonGenerator();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(42, generator, provider);

        assertEquals("42", generator.output());
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        DummyJsonGenerator generator = new DummyJsonGenerator();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(42L, generator, provider);

        assertEquals("42", generator.output());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = FloatSerializer.instance;
        DummyJsonGenerator generator = new DummyJsonGenerator();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(42.0f, generator, provider);

        assertEquals("42.0", generator.output());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        DummyJsonGenerator generator = new DummyJsonGenerator();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(42.0, generator, provider);

        assertEquals("42.0", generator.output());
    }

    private static class DummyJsonGenerator extends com.fasterxml.jackson.core.JsonGenerator {
        private StringBuilder output = new StringBuilder();

        public String output() {
            return output.toString();
        }

        @Override
        public void close() throws IOException {}

        @Override
        public void flush() throws IOException {}

        @Override
        public Version version() {
            return null;
        }

        @Override
        public ObjectCodec getCodec() {
            return null;
        }

        @Override
        public JsonGenerator setCodec(ObjectCodec oc) {
            return null;
        }

        @Override
        public void writeObject(Object value) throws IOException {}

        @Override
        public void writeTree(TreeNode rootNode) throws IOException {}

        @Override
        public void writeString(String value) throws IOException {
            output.append(value);
        }

        @Override
        public void writeString(char[] text, int offset, int len) throws IOException {
            output.append(text, offset, len);
        }

        @Override
        public void writeString(Reader reader, int len) throws IOException {}

        @Override
        public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {}

        @Override
        public void writeUTF8String(byte[] text, int offset, int length) throws IOException {}

        @Override
        public void writeRaw(String text) throws IOException {}

        @Override
        public void writeRaw(String text, int offset, int len) throws IOException {}

        @Override
        public void writeRaw(char[] text, int offset, int len) throws IOException {}

        @Override
        public void writeRaw(char c) throws IOException {}

        @Override
        public void writeRawValue(String text) throws IOException {}

        @Override
        public void writeRawValue(String text, int offset, int len) throws IOException {}

        @Override
        public void writeRawValue(char[] text, int offset, int len) throws IOException {}

        @Override
        public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {}

        @Override
        public void writeNumber(int value) throws IOException {}

        @Override
        public void writeNumber(long value) throws IOException {}

        @Override
        public void writeNumber(BigInteger value) throws IOException {}

        @Override
        public void writeNumber(double value) throws IOException {}

        @Override
        public void writeNumber(float value) throws IOException {}

        @Override
        public void writeNumber(BigDecimal value) throws IOException {}

        @Override
        public void writeNumber(String value) throws IOException {}

        @Override
        public void writeBoolean(boolean state) throws IOException {}

        @Override
        public void writeNull() throws IOException {}

        @Override
        public void writeFieldName(String name) throws IOException {}

        @Override
        public void writeFieldName(SerializableString name) throws IOException {}

        @Override
        public void writeStartArray() throws IOException {}

        @Override
        public void writeEndArray() throws IOException {}

        @Override
        public void writeStartObject() throws IOException {}

        @Override
        public void writeEndObject() throws IOException {}

        @Override
        public void writeObject(Object pojo) throws IOException {}

        @Override
        public void writeTree(JsonNode rootNode) throws IOException {}

        @Override
        public void copyCurrentEvent(JsonParser jp) throws IOException {}

        @Override
        public void copyCurrentStructure(JsonParser jp) throws IOException {}

        @Override
        public JsonStreamContext getOutputContext() {
            return null;
        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public boolean isExpectedStartArrayToken() {
            return false;
        }

        @Override
        public boolean isExpectedStartObjectToken() {
            return false;
        }

        @Override
        public void setCurrentValue(Object v) {}

        @Override
        public void overrideCurrentName(String name) {}

        @Override
        public String getCurrentName() throws IOException {
            return null;
        }

        @Override
        public JsonToken getCurrentToken() {
            return null;
        }

        @Override
        public int getCurrentTokenId() {
            return 0;
        }

        @Override
        public boolean hasCurrentToken() {
            return false;
        }

        @Override
        public boolean hasTokenId(int id) {
            return false;
        }

        @Override
        public boolean hasToken(JsonToken t) {
            return false;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            return null;
        }

        @Override
        public JsonToken nextValue() throws IOException {
            return null;
        }

        @Override
        public JsonParser.NumberType getNumberType() throws IOException {
            return null;
        }

        @Override
        public void setCodec(ObjectCodec c) {}

        @Override
        public void close(ServletResponse response) throws IOException {}

        @Override
        public boolean isRunningOnJDK14() {
            return false;
        }

        @Override
        public void writeRaw(char[] text, int offset, int len, boolean addLF) throws IOException {}

        @Deprecated
        @Override
        public void writeUTF8String(byte[] text, int offset, int len) throws IOException {}

        @Override
        public void writeRawValue(String text, boolean addLF) throws IOException {}

        @Override
        public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {}

        @Override
        public boolean canWriteBinaryNatively() {
            return false;
        }

        @Override
        public void writeString(SerializableString text) throws IOException {}

        @Override
        public void writeString(char c) throws IOException {}

        @Override
        public void writeRaw(String text, boolean addLF) throws IOException {}

        @Override
        public void writeBoolean(Boolean b) throws IOException {}

        @Override
        public void writeNumber(Integer number) throws IOException {}

        @Override
        public void enable(Feature f) {}

        @Override
        public void disable(Feature f) {}

        @SuppressWarnings("unchecked")
        @Override
        public void setSchema(FormatSchema schema) {}

        @SuppressWarnings("unchecked")
        @Override
        public void setPrettyPrinter(PrettyPrinter pp) {}

        @Override
        public void writeNumber(String encodedValue) throws IOException {}

        @Override
        public void writeNumber(int i) throws IOException {}

        @Override
        public void writeNumber(long l) throws IOException {}
    }
}