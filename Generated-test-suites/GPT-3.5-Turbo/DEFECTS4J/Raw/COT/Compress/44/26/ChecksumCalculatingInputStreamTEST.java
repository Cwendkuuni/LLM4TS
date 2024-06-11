
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        Adler32 checksum = new Adler32();
        inputStream = new ChecksumCalculatingInputStream(checksum, bais);
    }

    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        assertEquals(1, result);
        assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer);
        assertEquals(3, result);
        assertArrayEquals(new byte[] { 1, 2, 3 }, buffer);
        assertEquals(168423484, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer, 1, 3);
        assertEquals(3, result);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 0 }, buffer);
        assertEquals(168423484, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(3);
        assertEquals(3, result);
        assertEquals(168423484, inputStream.getValue());
    }

    @Test
    public void testGetValue() {
        assertEquals(0, inputStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadException() throws IOException {
        InputStream mockInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(new Adler32(), mockInputStream);
        testStream.read();
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayException() throws IOException {
        InputStream mockInputStream = new InputStream() {
            @Override
            public int read(byte[] b) throws IOException {
                throw new IOException();
            }
        };
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(new Adler32(), mockInputStream);
        testStream.read(new byte[3]);
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayWithOffsetAndLengthException() throws IOException {
        InputStream mockInputStream = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(new Adler32(), mockInputStream);
        testStream.read(new byte[5], 1, 3);
    }

    @Test(expected = IOException.class)
    public void testSkipException() throws IOException {
        InputStream mockInputStream = new InputStream() {
            @Override
            public long skip(long n) throws IOException {
                throw new IOException();
            }
        };
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(new Adler32(), mockInputStream);
        testStream.skip(3);
    }
}
