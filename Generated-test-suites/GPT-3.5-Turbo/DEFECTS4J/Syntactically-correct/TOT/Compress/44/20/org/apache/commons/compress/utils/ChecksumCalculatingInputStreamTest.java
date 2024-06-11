import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private Checksum mockChecksum;
    private byte[] testData = new byte[]{1, 2, 3, 4, 5};
    private byte[] smallTestData = new byte[]{1};
    private byte[] emptyByteArray = new byte[0];
    
    @Before
    public void setUp() {
        mockChecksum = new MockChecksum();
        inputStream = new ChecksumCalculatingInputStream(mockChecksum, new MockInputStream());
    }
    
    @After
    public void tearDown() {
        inputStream = null;
    }
    
    @Test
    public void testRead_SingleByte_Valid() throws Exception {
        int data = inputStream.read();
        assertEquals(1, data);
        assertEquals(1, mockChecksum.value);
    }
    
    @Test
    public void testRead_ByteArray_Valid() throws Exception {
        byte[] data = new byte[5];
        int bytesRead = inputStream.read(data);
        assertArrayEquals(testData, data);
        assertEquals(5, bytesRead);
        assertEquals(15, mockChecksum.value);
    }
    
    @Test
    public void testRead_ByteArrayOffLen_Valid() throws Exception {
        byte[] data = new byte[3];
        int bytesRead = inputStream.read(data, 1, 2);
        assertArrayEquals(new byte[]{0, 1, 2}, data);
        assertEquals(2, bytesRead);
        assertEquals(3, mockChecksum.value);
    }
    
    @Test
    public void testRead_EmptyByteArray_Valid() throws Exception {
        byte[] data = new byte[0];
        int bytesRead = inputStream.read(data);
        assertArrayEquals(emptyByteArray, data);
        assertEquals(0, bytesRead);
        assertEquals(0, mockChecksum.value);
    }
    
    @Test
    public void testSkip_Valid() throws Exception {
        long bytesSkipped = inputStream.skip(3);
        assertEquals(1, bytesSkipped);
        assertEquals(3, mockChecksum.value);
    }
    
    @Test
    public void testGetValue_Valid() throws Exception {
        long value = inputStream.getValue();
        assertEquals(0, value);
    }
    
    @Test(expected = IOException.class)
    public void testRead_InvalidData_ThrowsIOException() throws Exception {
        Checksum invalidChecksum = new InvalidChecksum();
        ChecksumCalculatingInputStream invalidStream = new ChecksumCalculatingInputStream(invalidChecksum, new MockInputStream());
        invalidStream.read();
    }
    
    private static class MockChecksum implements Checksum {
        private long value = 0;
        
        @Override
        public void update(int b) {
            value += b;
        }
        
        @Override
        public void update(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) {
                value += b[i];
            }
        }
        
        @Override
        public long getValue() {
            return value;
        }
        
        @Override
        public void reset() {
            value = 0;
        }
    }
    
    private static class InvalidChecksum implements Checksum {
        @Override
        public void update(int b) {}
        
        @Override
        public void update(byte[] b, int off, int len) {}
        
        @Override
        public long getValue() {
            return 0;
        }
        
        @Override
        public void reset() {}
    }
    
    private static class MockInputStream extends InputStream {
        private int index = 0;
        
        @Override
        public int read() {
            if (index < 5) {
                return index++;
            }
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            if (index < 5) {
                b[off] = (byte) index++;
                return 1;
            }
            return -1;
        }
    }
}