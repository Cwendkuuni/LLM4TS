
import org.apache.commons.compress.utils.*;
import org.junit.Test;
import java.io.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);

        cis.read();

        assert checksum.getValue() == 1;
    }

    @Test
    public void testRead_byteArray() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[2];
        cis.read(buffer);

        assert checksum.getValue() == 16777216;
    }

    @Test
    public void testRead_byteArrayWithOffsetAndLength() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];
        cis.read(buffer, 1, 2);

        assert checksum.getValue() == 16777216;
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);

        cis.skip(2);

        assert cis.read() == 3;
        assert checksum.getValue() == 65535;
    }

    @Test
    public void testGetValue() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);

        cis.read();

        assert cis.getValue() == checksum.getValue();
    }

}
