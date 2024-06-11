import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash testHash;

    @Before
    public void setUp() {
        testHash = new TFloatCharHash();
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0f, testHash.getNoEntryKey(), 0);
        assertEquals('\0', testHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(testHash.contains(1.0f));

        testHash.insertKey(1.0f);
        assertTrue(testHash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        assertFalse(testHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return false;
            }
        }));

        testHash.insertKey(1.0f);
        assertTrue(testHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        testHash.insertKey(1.0f);
        testHash.removeAt(0);

        assertFalse(testHash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, testHash.index(1.0f));

        testHash.insertKey(1.0f);
        assertEquals(0, testHash.index(1.0f));
        assertEquals(-1, testHash.index(2.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, testHash.insertKey(1.0f));
        assertEquals(-1, testHash.insertKey(1.0f));
    }

    @Test
    public void testReadExternal() {
        // Test reading external values when values are initially not set
        TFloatCharHash externalHash = new TFloatCharHash();
        TFloatCharHash newHash = new TFloatCharHash();
        newHash.no_entry_key = 2.0f;
        newHash.no_entry_value = 'a';

        try {
            externalHash.readExternal(new ObjectInputStub(new byte[]{0, 2, 0, 0, 0, 0, 0, 0, 0, 0}));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(newHash.getNoEntryKey(), externalHash.getNoEntryKey(), 0);
        assertEquals(newHash.getNoEntryValue(), externalHash.getNoEntryValue());

        // Test reading external values when values are already set
        newHash.no_entry_key = 3.0f;
        newHash.no_entry_value = 'b';

        try {
            externalHash.readExternal(new ObjectInputStub(new byte[]{0, 3, 0, 0, 0, 0, 0, 0, 0, 0}));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(newHash.getNoEntryKey(), externalHash.getNoEntryKey(), 0);
        assertEquals(newHash.getNoEntryValue(), externalHash.getNoEntryValue());
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash newHash = new TFloatCharHash();
        newHash.no_entry_key = 2.0f;
        newHash.no_entry_value = 'a';

        try {
            newHash.writeExternal(new ObjectOutputStub());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // Custom ObjectInput stub for testing readExternal
    private class ObjectInputStub implements ObjectInput {

        private byte[] data;

        ObjectInputStub(byte[] data) {
            this.data = data;
        }

        @Override
        public int read() throws IOException {
            return 0;
        }

        @Override
        public int read(byte[] b) throws IOException {
            return 0;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return 0;
        }

        @Override
        public long skip(long n) throws IOException {
            return 0;
        }

        @Override
        public int available() throws IOException {
            return 0;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void readFully(byte[] b) throws IOException {

        }

        @Override
        public void readFully(byte[] b, int off, int len) throws IOException {

        }

        @Override
        public int skipBytes(int n) throws IOException {
            return 0;
        }

        @Override
        public boolean readBoolean() throws IOException {
            return false;
        }

        @Override
        public byte readByte() throws IOException {
            return 0;
        }

        @Override
        public int readUnsignedByte() throws IOException {
            return 0;
        }

        @Override
        public short readShort() throws IOException {
            return 0;
        }

        @Override
        public int readUnsignedShort() throws IOException {
            return 0;
        }

        @Override
        public char readChar() throws IOException {
            return (char) data[0];
        }

        @Override
        public int readInt() throws IOException {
            return 0;
        }

        @Override
        public long readLong() throws IOException {
            return 0;
        }

        @Override
        public float readFloat() throws IOException {
            return ByteBuffer.wrap(Arrays.copyOfRange(data, 1, 5)).getFloat();
        }

        @Override
        public double readDouble() throws IOException {
            return 0;
        }

        @Override
        public String readLine() throws IOException {
            return null;
        }

        @Override
        public String readUTF() throws IOException {
            return null;
        }
    }

    // Custom ObjectOutput stub for testing writeExternal
    private class ObjectOutputStub implements ObjectOutput {

        @Override
        public void write(int b) throws IOException {

        }

        @Override
        public void write(byte[] b) throws IOException {

        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {

        }

        @Override
        public void flush() throws IOException {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void writeBoolean(boolean v) throws IOException {

        }

        @Override
        public void writeByte(int v) throws IOException {

        }

        @Override
        public void writeShort(int v) throws IOException {

        }

        @Override
        public void writeChar(int v) throws IOException {

        }

        @Override
        public void writeInt(int v) throws IOException {

        }

        @Override
        public void writeLong(long v) throws IOException {

        }

        @Override
        public void writeFloat(float v) throws IOException {

        }

        @Override
        public void writeDouble(double v) throws IOException {

        }

        @Override
        public void writeBytes(String s) throws IOException {

        }

        @Override
        public void writeChars(String s) throws IOException {

        }

        @Override
        public void writeUTF(String str) throws IOException {

        }
    }

}