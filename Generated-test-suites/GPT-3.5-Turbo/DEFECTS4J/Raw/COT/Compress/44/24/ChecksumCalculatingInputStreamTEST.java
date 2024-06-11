import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        Checksum checksum = new MyChecksum();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = calcInputStream.read();
        assertEquals(1, readByte);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        Checksum checksum = new MyChecksum();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];
        int bytesRead = calcInputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertEquals(1 + 2 + 3, checksum.getValue());
    }

    @Test
    public void testReadWithByteArrayAndOffsets() throws IOException {
        Checksum checksum = new MyChecksum();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[5];
        int bytesRead = calcInputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertEquals(0 + 1 + 2, checksum.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new MyChecksum();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(1, calcInputStream.skip(1));
        assertEquals(1 + 2 + 3 + 4 + 5, checksum.getValue());
    }

    @Test
    public void testGetValue() {
        Checksum checksum = new MyChecksum();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(0, calcInputStream.getValue());
    }

    private class MyChecksum implements Checksum {
        private long value;

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
}