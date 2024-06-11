import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        ChecksumChecksumCalculatingInputStreamChecksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int value = calculatingInputStream.read();

        assert value == 0x01;
        assert calculatingInputStream.getValue() == 0xcbf43926;
    }

    @Test
    public void testReadArray() throws IOException {
        ChecksumChecksumCalculatingInputStreamChecksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        byte[] buffer = new byte[2];
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int value = calculatingInputStream.read(buffer);

        assert value == 2;
        assert calculatingInputStream.getValue() == 0xcbf43926;
    }

    @Test
    public void testReadArrayWithOffset() throws IOException {
        ChecksumChecksumCalculatingInputStreamChecksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        byte[] buffer = new byte[3];
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int value = calculatingInputStream.read(buffer, 1, 2);

        assert value == 2;
        assert buffer[0] == 0;
        assert buffer[1] == 0x01;
        assert buffer[2] == 0x02;
        assert calculatingInputStream.getValue() == 0xcbf43926;
    }

    @Test
    public void testSkip() throws IOException {
        ChecksumChecksumCalculatingInputStreamChecksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = calculatingInputStream.skip(2);

        assert skipped == 1;
        assert calculatingInputStream.getValue() == 0xcbf43926;
    }

    @Test
    public void testGetValue() {
        ChecksumChecksumCalculatingInputStreamChecksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long value = calculatingInputStream.getValue();

        assert value == 0;
    }
}