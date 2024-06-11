
package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class WritableObjectIdTest {

    private static class DummyObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public void releaseGenerator() {
        }

        @Override
        public Class<?> getScope() {
            return null;
        }

        @Override
        public ObjectIdGenerator<Object> forScope(Class<?> scope) {
            return null;
        }

        @Override
        public ObjectIdGenerator<Object> newForSerialization(Object context) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return false;
        }

        @Override
        public Object generateId(Object forPojo) {
            return null;
        }

        @Override
        public Object generateId(Object forPojo, Class<?> type) {
            return null;
        }

        @Override
        public boolean maySerializeAsObject() {
            return false;
        }
    }

    private static class DummySerializer extends StdSerializer<Object> {
        private static final long serialVersionUID = 1L;

        protected DummySerializer() {
            super(Object.class);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        }
    }

    @Test
    public void testConstructor() {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        assertEquals(generator, objectId.generator);
        assertNull(objectId.id);
        assertFalse(objectId.idWritten);
    }

    @Test
    public void testWriteAsId_NoId() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, false);
        assertFalse(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_IdWritten() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        objectId.id = 123;
        objectId.idWritten = true;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, false);
        assertTrue(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_GenCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        objectId.id = 123;
        JsonGenerator gen = new DummyJsonGenerator();
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, false);
        assertTrue(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsId_GenCannotWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        objectId.id = 123;
        JsonGenerator gen = new DummyJsonGenerator(false);
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, false);
        assertTrue(objectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        DummyObjectIdGenerator generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        assertNull(objectId.id);
        assertEquals(generator.generateId(forPojo), objectId.generateId(forPojo));
        assertEquals(objectId.id, generator.generateId(forPojo));
    }

    @Test
    public void testWriteAsField_IdWritten() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        objectId.idWritten = true;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, false);
        objectId.writeAsField(gen, provider, w);
    }

    @Test
    public void testWriteAsField_GenCanWriteObjectId() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        JsonGenerator gen = new DummyJsonGenerator();
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, null, null, false);
        objectId.writeAsField(gen, provider, w);
    }

    @Test
    public void testWriteAsField_NameNotNull() throws IOException {
        ObjectIdGenerator<?> generator = new DummyObjectIdGenerator();
        WritableObjectId objectId = new WritableObjectId(generator);
        JsonGenerator gen = new DummyJsonGenerator();
        SerializerProvider provider = null;
        ObjectIdWriter w = new ObjectIdWriter(null, new DummySerializableString(), null, false);
        objectId.writeAsField(gen, provider, w);
    }

    private static class DummyJsonGenerator extends JsonGenerator {
        private boolean canWriteObjectId;

        protected DummyJsonGenerator() {
            canWriteObjectId = true;
        }

        protected DummyJsonGenerator(boolean canWriteObjectId) {
            this.canWriteObjectId = canWriteObjectId;
        }

        @Override
        public void writeStartArray() throws IOException {
        }

        @Override
        public void writeStartArray(Object  forValue) throws IOException {
        }

        @Override
        public void writeEndArray() throws IOException {
        }

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
        public void writeFieldName(SerializableString name) throws IOException {
        }

        @Override
        public void writeString(String text) throws IOException {
        }

        @Override
        public void writeString(char[] text, int offset, int len) throws IOException {
        }

        @Override
        public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
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
        public void writeNumber(double d) throws IOException {
        }

        @Override
        public void writeNumber(float f) throws IOException {
        }

        @Override
        public void writeNumber(BigDecimal dec) throws IOException {
        }

        @Override
        public void writeBoolean(boolean state) throws IOException {
        }

        @Override
        public void writeNull() throws IOException {
        }

        @Override
        public void writeObject(Object value) throws IOException {
        }

        @Override
        public void writeTree(TreeNode node) throws IOException {
        }

        @Override
        public void writeStringField(String fieldName, String value) throws IOException {
        }

        @Override
        public void writeBooleanField(String fieldName, boolean value) throws IOException {
        }

        @Override
        public void writeNullField(String fieldName) throws IOException {
        }

        @Override
        public void writeOmittedField(String fieldName) throws IOException {
        }

        @Override
        public void writeObjectId(Object id) throws IOException {
        }

        @Override
        public void writeObjectRef(Object id) throws IOException {
        }

        @Override
        public void writeTypeId(Object typeId) throws IOException {
        }

        @Override
        public void writeEmbeddedObject(Object object) throws IOException {
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
        public void writeBinary(byte[] data, int offset, int len) throws IOException {
        }

        @Override
        public int writeBinary(InputStream data, int dataLength) throws IOException {
            return 0;
        }

        @Override
        public void copyCurrentEvent(JsonParser jp) throws IOException {
        }

        @Override
        public void copyCurrentStructure(JsonParser jp) throws IOException {
        }

        @Override
        public boolean canWriteBinaryNatively() {
            return false;
        }

        @Override
        public boolean canWriteObjectId() {
            return canWriteObjectId;
        }

        @Override
        public boolean canWriteTypeId() {
            return false;
        }

        @Override
        public boolean canWriteFormattedNumbers() {
            return false;
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void close() throws IOException {
        }
    }

    private static class DummySerializableString implements SerializableString {
        private static final long serialVersionUID = 1L;

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public int charLength() {
            return 0;
        }

        @Override
        public char[] asQuotedChars() {
            return null;
        }

        @Override
        public byte[] asUnquotedUTF8() {
            return null;
        }

        @Override
        public byte[] asQuotedUTF8() {
            return null;
        }
    }
}
