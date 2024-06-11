import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    private static final byte[] TEST_DATA = "Test data".getBytes();
    
    @Test
    public void testRead() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertEquals('T', baseNCodecInputStream.read());
        assertEquals('e', baseNCodecInputStream.read());
        assertEquals('s', baseNCodecInputStream.read());
        assertEquals('t', baseNCodecInputStream.read());
        assertEquals(' ', baseNCodecInputStream.read());
        assertEquals('d', baseNCodecInputStream.read());
        assertEquals('a', baseNCodecInputStream.read());
        assertEquals('t', baseNCodecInputStream.read());
        assertEquals('a', baseNCodecInputStream.read());
        
        assertEquals(-1, baseNCodecInputStream.read());
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testReadWithZeroLengthArray() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        byte[] buffer = new byte[0];
        assertEquals(0, baseNCodecInputStream.read(buffer, 0, 0));
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testReadWithNullArray() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertThrows(NullPointerException.class, () -> baseNCodecInputStream.read(null, 0, 1));
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testReadWithNegativeOffset() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        byte[] buffer = new byte[1];
        assertThrows(IndexOutOfBoundsException.class, () -> baseNCodecInputStream.read(buffer, -1, 1));
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testReadWithNegativeLength() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        byte[] buffer = new byte[1];
        assertThrows(IndexOutOfBoundsException.class, () -> baseNCodecInputStream.read(buffer, 0, -1));
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testReadWithInvalidOffset() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        byte[] buffer = new byte[1];
        assertThrows(IndexOutOfBoundsException.class, () -> baseNCodecInputStream.read(buffer, 2, 1));
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testMarkSupported() {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertFalse(baseNCodecInputStream.markSupported());
        
        baseNCodecInputStream.close();
    }
    
    @Test
    public void testAvailable() throws IOException {
        BaseNCodec codec = new BaseNCodec() {
            @Override
            protected void decode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected void encode(byte[] pArray, int inputOffset, int length) throws IOException {
                // Do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // Dummy implementation
                return true;
            }
        };
        
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertEquals(1, baseNCodecInputStream.available());
        
        baseNCodecInputStream.close();
    }
}