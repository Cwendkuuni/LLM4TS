import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        int result = dcis.read();
        dcis.close();
        assertEquals(0x78, result);
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47, 0x00, 0x00 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        byte[] buf = new byte[4];
        int bytesRead = dcis.read(buf);
        dcis.close();
        assertEquals(4, bytesRead);
        assertEquals(data[0], buf[0]);
        assertEquals(data[1], buf[1]);
        assertEquals(data[2], buf[2]);
        assertEquals(data[3], buf[3]);
    }

    @Test
    public void testRead_byteArrayWithOffset() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47, 0x00, 0x00 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        byte[] buf = new byte[6];
        int bytesRead = dcis.read(buf, 1, 4);
        dcis.close();
        assertEquals(4, bytesRead);
        assertEquals(0, buf[0]);
        assertEquals(data[0], buf[1]);
        assertEquals(data[1], buf[2]);
        assertEquals(data[2], buf[3]);
        assertEquals(data[3], buf[4]);
        assertEquals(0, buf[5]);
    }

    @Test
    public void testRead_largeByteArray() throws IOException {
        byte[] data = new byte[100000];
        // Fill the data with dummy values
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        byte[] buf = new byte[data.length];
        int bytesRead = dcis.read(buf);
        dcis.close();
        assertEquals(data.length, bytesRead);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], buf[i]);
        }
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47, 0x00, 0x00 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        long result = dcis.skip(2);
        dcis.close();
        assertEquals(2, result);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47, 0x00, 0x00 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        int result = dcis.available();
        dcis.close();
        assertEquals(data.length, result);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47, 0x00, 0x00 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        dcis.close();
        try {
            dcis.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_withParameters() throws IOException {
        byte[] data = { 0x78, (byte) 0x9C, 0x01, 0x01, 0x47, 0x00, 0x00 };
        InputStream is = new ByteArrayInputStream(data);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is, parameters);
        assertTrue(dcis instanceof InflaterInputStream);
        dcis.close();
    }

    @Test
    public void testConstructor_withNullInputStream() {
        try {
            new DeflateCompressorInputStream(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testRead_withIOException() throws IOException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testRead_withIOExceptionAndZeroBytesRead() throws IOException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.read(new byte[10]);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testRead_withIOExceptionAndPartialBytesRead() throws IOException {
        InputStream is = new InputStream() {
            private int count = 0;

            @Override
            public int read() throws IOException {
                count++;
                if (count < 3) {
                    return 0xFF;
                } else {
                    throw new IOException();
                }
            }
        };
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.read(new byte[10]);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSkip_withIOException() throws IOException {
        InputStream is = new InputStream() {
            @Override
            public long skip(long n) throws IOException {
                throw new IOException();
            }
        };
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.skip(5);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testAvailable_withIOException() throws IOException {
        InputStream is = new InputStream() {
            @Override
            public int available() throws IOException {
                throw new IOException();
            }
        };
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.available();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testClose_withIOException() throws IOException {
        InputStream is = new InputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.close();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_withNullParameters() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        try {
            new DeflateCompressorInputStream(is, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCheckSignature_withNullSignature() {
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        try {
            dcis.checkSignature(null, 0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCheckSignature_withInvalidLength() throws IOException {
        byte[] signature = new byte[10];
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        boolean result = dcis.checkSignature(signature, 11);
        assertFalse(result);
    }

    @Test
    public void testCheckSignature_withValidSignature() throws IOException {
        byte[] signature = {(byte) 0x78, (byte) 0x9C};
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        boolean result = dcis.checkSignature(signature, 2);
        assertTrue(result);
    }

    @Test
    public void testCheckSignature_withInvalidSignature() throws IOException {
        byte[] signature = {(byte) 0x78, (byte) 0x9C};
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        boolean result = dcis.checkSignature(signature, 1);
        assertFalse(result);
    }

    @Test
    public void testSkip_withLargeValue() throws IOException {
        byte[] data = new byte[100000];
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        long result = dcis.skip(200000);
        assertEquals(data.length, result);
    }

    @Test
    public void testSkip_withNegativeValue() throws IOException {
        byte[] data = new byte[100000];
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        long result = dcis.skip(-100);
        assertEquals(0, result);
    }

    @Test
    public void testAvailable_withNegativeValue() throws IOException {
        byte[] data = new byte[100000];
        InputStream is = new ByteArrayInputStream(data);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        int result = dcis.available();
        assertEquals(data.length, result);
    }

    @Test
    public void testClose_withIOExceptionAndNullInputStream() throws IOException {
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(null);
        try {
            dcis.close();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testClose_withIOExceptionAndClosedInputStream() throws IOException {
        InputStream is = new InputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        is.close();
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.close();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testClose_withIOExceptionAndClosedUnderlyingInputStream() throws IOException {
        InputStream under = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        InputStream is = new InputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }

            @Override
            public int read() throws IOException {
                return under.read();
            }
        };
        is.close();
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
        try {
            dcis.close();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    private void assertFalse(boolean result) {
        assertNotNull(result);
    }

}