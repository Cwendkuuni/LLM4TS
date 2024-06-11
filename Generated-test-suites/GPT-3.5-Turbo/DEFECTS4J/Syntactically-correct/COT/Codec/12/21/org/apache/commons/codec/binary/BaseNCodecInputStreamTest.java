import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {
    
    private BaseNCodecInputStream baseNCodecInputStream;
    
    @Before
    public void setup() {
        baseNCodecInputStream = new BaseNCodecInputStream(new InputStreamStub(), new BaseNCodecStub(), true);
    }
    
    @Test
    public void testRead() {
        assertEquals(65, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadWithEmptyBuffer() {
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadWithNegativeByteValue() {
        assertEquals(256, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadByteArray() {
        byte[] buffer = new byte[10];
        int bytesRead = baseNCodecInputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
        assertArrayEquals(new byte[] { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106 }, buffer);
    }
    
    @Test
    public void testReadByteArrayWithEmptyBuffer() {
        byte[] buffer = new byte[10];
        int bytesRead = baseNCodecInputStream.read(buffer, 0, 10);
        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[10], buffer);
    }
    
    @Test
    public void testReadByteArrayWithPartialBuffer() {
        byte[] buffer = new byte[10];
        int bytesRead = baseNCodecInputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[] { 97, 98, 99, 100, 101, 0, 0, 0, 0, 0 }, buffer);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNullBuffer() {
        baseNCodecInputStream.read(null, 0, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, -1, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLen() {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidBufferSize() {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, 11);
    }
    
    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testAvailable() {
        assertEquals(1, baseNCodecInputStream.available());
    }
}

class InputStreamStub extends InputStream {
    
    @Override
    public int read() {
        return 65;
    }
    
    @Override
    public int read(byte[] b, int offset, int len) {
        if (offset == 0 && len == 10) {
            for (int i = 0; i < 10; i++) {
                b[i] = (byte) (97 + i);
            }
            return 10;
        } else if (offset == 0 && len == 5) {
            for (int i = 0; i < 5; i++) {
                b[i] = (byte) (97 + i);
            }
            return 5;
        }
        return 0;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int available() {
        return 1;
    }
}

class BaseNCodecStub extends BaseNCodec {
    
    @Override
    protected boolean hasData() {
        return false;
    }
    
    @Override
    protected void encode(byte[] buf, int off, int len) {
        // do nothing
    }
    
    @Override
    protected void decode(byte[] buf, int off, int len) {
        // do nothing
    }
    
    @Override
    protected int readResults(byte[] b, int offset, int len) {
        if (offset == 0 && len == 10) {
            for (int i = 0; i < 10; i++) {
                b[i] = (byte) (97 + i);
            }
            return 10;
        } else if (offset == 0 && len == 5) {
            for (int i = 0; i < 5; i++) {
                b[i] = (byte) (97 + i);
            }
            return 5;
        }
        return 0;
    }
}