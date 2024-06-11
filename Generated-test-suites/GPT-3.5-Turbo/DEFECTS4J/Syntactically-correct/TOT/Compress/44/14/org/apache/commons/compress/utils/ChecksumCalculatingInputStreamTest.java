import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    // test read() method
    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        int result = inputStream.read();
        
        assertEquals(1, result);
        assertEquals(1, inputStream.getValue());
        inputStream.close();
    }

    // test read(byte[] b) method
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);

        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(3, bytesRead);
        assertEquals(770737173, inputStream.getValue());

        inputStream.close();
    }

    // test read(byte[] b, int off, int len) method
    @Test
    public void testReadByteArrayWithOffAndLen() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);

        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, buffer);
        assertEquals(3, bytesRead);
        assertEquals(1370776605, inputStream.getValue());

        inputStream.close();
    }

    // test skip(long n) method
    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        long result = inputStream.skip(2);

        assertEquals(2, result);
        assertEquals(966280102, inputStream.getValue());

        inputStream.close();
    }

    // test read() method with empty stream
    @Test
    public void testReadWithEmptyStream() throws IOException {
        byte[] data = {};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        int result = inputStream.read();

        assertEquals(-1, result);
        assertEquals(0, inputStream.getValue());

        inputStream.close();
    }
  
    // test read(byte[] b) method with empty stream
    @Test
    public void testReadByteArrayWithEmptyStream() throws IOException {
        byte[] data = {};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);

        assertEquals(-1, bytesRead);
        assertEquals(0, inputStream.getValue());

        inputStream.close();
    }

    // test read(byte[] b, int off, int len) method with empty stream
    @Test
    public void testReadByteArrayWithOffAndLenWithEmptyStream() throws IOException {
        byte[] data = {};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);

        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
        assertEquals(-1, bytesRead);
        assertEquals(0, inputStream.getValue());

        inputStream.close();
    }

    // test skip(long n) method with empty stream
    @Test
    public void testSkipWithEmptyStream() throws IOException {
        byte[] data = {};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        long result = inputStream.skip(2);

        assertEquals(0, result);
        assertEquals(0, inputStream.getValue());

        inputStream.close();
    }
  
    // test read() method with exception
    @Test(expected = IOException.class)
    public void testReadWithException() throws IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Custom IO Exception");
            }
        };
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(crc32, inputStream);

        calculatingInputStream.read();
    }

    // test read(byte[] b) method with exception
    @Test(expected = IOException.class)
    public void testReadByteArrayWithException() throws IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read(byte[] b) throws IOException {
                throw new IOException("Custom IO Exception");
            }
        };
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(crc32, inputStream);

        byte[] buffer = new byte[3];
        calculatingInputStream.read(buffer);
    }

    // test read(byte[] b, int off, int len) method with exception
    @Test(expected = IOException.class)
    public void testReadByteArrayWithOffAndLenWithException() throws IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw new IOException("Custom IO Exception");
            }
        };
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(crc32, inputStream);

        byte[] buffer = new byte[5];
        calculatingInputStream.read(buffer, 1, 3);
    }

    // test skip(long n) method with exception
    @Test(expected = IOException.class)
    public void testSkipWithException() throws IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public long skip(long n) throws IOException {
                throw new IOException("Custom IO Exception");
            }
        };
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(crc32, inputStream);

        calculatingInputStream.skip(2);
    }

    // test getValue() method
    @Test
    public void testGetValue() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum crc32 = new CRC32();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);

        inputStream.read(new byte[3]);

        long result = inputStream.getValue();

        assertEquals(770737173, result);

        inputStream.close();
    }
}