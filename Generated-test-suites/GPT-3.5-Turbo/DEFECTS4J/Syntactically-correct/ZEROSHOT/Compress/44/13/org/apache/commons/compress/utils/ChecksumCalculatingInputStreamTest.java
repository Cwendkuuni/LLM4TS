import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private static class DummyChecksum implements Checksum {

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

    @Test
    public void testRead() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DummyChecksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int result = calculatingInputStream.read();

        assertEquals(0, result);
        assertEquals(0, checksum.getValue());

        result = calculatingInputStream.read();

        assertEquals(1, result);
        assertEquals(1, checksum.getValue());

        result = calculatingInputStream.read();

        assertEquals(2, result);
        assertEquals(3, checksum.getValue());

        result = calculatingInputStream.read();

        assertEquals(3, result);
        assertEquals(6, checksum.getValue());

        result = calculatingInputStream.read();

        assertEquals(4, result);
        assertEquals(10, checksum.getValue());

        result = calculatingInputStream.read();

        assertEquals(5, result);
        assertEquals(15, checksum.getValue());

        result = calculatingInputStream.read();

        assertEquals(-1, result);
        assertEquals(15, checksum.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DummyChecksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];
        int result = calculatingInputStream.read(buffer);

        assertArrayEquals(new byte[]{0, 1, 2}, buffer);
        assertEquals(3, result);
        assertEquals(3, checksum.getValue());

        result = calculatingInputStream.read(buffer);

        assertArrayEquals(new byte[]{3, 4, 5}, buffer);
        assertEquals(3, result);
        assertEquals(12, checksum.getValue());

        result = calculatingInputStream.read(buffer);

        assertArrayEquals(new byte[]{0, 0, 0}, buffer);
        assertEquals(-1, result);
        assertEquals(12, checksum.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DummyChecksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[6];
        int result = calculatingInputStream.read(buffer, 0, 6);

        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5}, buffer);
        assertEquals(6, result);
        assertEquals(15, checksum.getValue());

        result = calculatingInputStream.read(buffer, 0, 6);

        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0}, buffer);
        assertEquals(-1, result);
        assertEquals(15, checksum.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DummyChecksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long result = calculatingInputStream.skip(3);

        assertEquals(3, result);
        assertEquals(6, checksum.getValue());

        result = calculatingInputStream.skip(3);

        assertEquals(0, result);
        assertEquals(6, checksum.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DummyChecksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long result = calculatingInputStream.getValue();

        assertEquals(0, result);

        calculatingInputStream.read();

        result = calculatingInputStream.getValue();

        assertEquals(0, result);

        calculatingInputStream.read();

        result = calculatingInputStream.getValue();

        assertEquals(1, result);

        calculatingInputStream.skip(2);

        result = calculatingInputStream.getValue();

        assertEquals(3, result);

        calculatingInputStream.read(new byte[3]);

        result = calculatingInputStream.getValue();

        assertEquals(6, result);

        calculatingInputStream.close();
    }

}