import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    private BaseNCodecInputStream baseNCodecInputStream;
    private TestBaseNCodec baseNCodec;
    
    @Before
    public void setup() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        baseNCodec = new TestBaseNCodec();
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }
    
    @Test
    public void testRead() throws IOException {
        int byte1 = baseNCodecInputStream.read();
        assertEquals((int) 't', byte1);
        
        int byte2 = baseNCodecInputStream.read();
        assertEquals((int) 'e', byte2);
        
        int byte3 = baseNCodecInputStream.read();
        assertEquals((int) 's', byte3);
        
        int byte4 = baseNCodecInputStream.read();
        assertEquals((int) 't', byte4);
        
        int byte5 = baseNCodecInputStream.read();
        assertEquals((int) ' ', byte5);
        
        int byte6 = baseNCodecInputStream.read();
        assertEquals((int) 'd', byte6);
        
        int byte7 = baseNCodecInputStream.read();
        assertEquals((int) 'a', byte7);
        
        int byte8 = baseNCodecInputStream.read();
        assertEquals((int) 't', byte8);
        
        int eof = baseNCodecInputStream.read();
        assertEquals(-1, eof);
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = baseNCodecInputStream.read(buffer, 0, 4);
        assertEquals(4, bytesRead);
        assertEquals((byte) 't', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 's', buffer[2]);
        assertEquals((byte) 't', buffer[3]);
        
        bytesRead = baseNCodecInputStream.read(buffer, 0, 4);
        assertEquals(4, bytesRead);
        assertEquals((byte) ' ', buffer[0]);
        assertEquals((byte) 'd', buffer[1]);
        assertEquals((byte) 'a', buffer[2]);
        assertEquals((byte) 't', buffer[3]);
        
        bytesRead = baseNCodecInputStream.read(buffer, 0, 4);
        assertEquals(1, bytesRead);
        assertEquals((byte) -1, buffer[0]);
    }
    
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int bytesRead = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, bytesRead);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, -1, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBuffer() throws IOException {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, 0, 5);
    }
    
    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeLength() throws IOException {
        baseNCodecInputStream.skip(-1);
    }
    
    @Test
    public void testAvailable() throws IOException {
        assertEquals(7, baseNCodecInputStream.available());
        baseNCodecInputStream.read();
        assertEquals(6, baseNCodecInputStream.available());
        baseNCodecInputStream.skip(4);
        assertEquals(1, baseNCodecInputStream.available());
        baseNCodecInputStream.read();
        assertEquals(0, baseNCodecInputStream.available());
    }
    
    @Test
    public void testBaseNCodecInputStream() {
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
        assertTrue(baseNCodecInputStream.doEncode);
    }
    
    private class TestBaseNCodec extends BaseNCodec {
        @Override
        public boolean hasData() {
            return false;
        }

        @Override
        public int readResults(byte[] b, int offset, int len) {
            return 0;
        }

        @Override
        public void encode(byte[] b, int off, int len) {

        }

        @Override
        public void decode(byte[] b, int off, int len) {

        }
    }
}