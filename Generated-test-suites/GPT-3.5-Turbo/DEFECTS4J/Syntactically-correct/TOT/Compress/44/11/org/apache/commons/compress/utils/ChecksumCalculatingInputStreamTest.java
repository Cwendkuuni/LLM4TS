import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] data = new byte[10];
        data[0] = 1;
        data[1] = -1;

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        int value = in.read();

        assertEquals(1, value);
        assertEquals(1, checksum.getValue());

        input.close();
        in.close();
    }

    @Test(expected = IOException.class)
    public void testRead_singleByte_streamExhausted() throws IOException {
        byte[] data = new byte[10];
        data[0] = 1;

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        int value = in.read();
        assertEquals(1, value);
        assertEquals(1, checksum.getValue());

        in.read(); // read beyond available data, IOException expected

        input.close();
        in.close();
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i + 1);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[5];
        int bytesRead = in.read(buffer);

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
        assertEquals(5, bytesRead);
        assertEquals(0x05cadbeefL, checksum.getValue());

        input.close();
        in.close();
    }

    @Test(expected = IOException.class)
    public void testRead_byteArray_streamExhausted() throws IOException {
        byte[] data = new byte[10];
        data[0] = 1;
        data[1] = 2;
        data[2] = 3;
        data[3] = 4;
        data[4] = 5;

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[10];
        int bytesRead = in.read(buffer);
        assertEquals(5, bytesRead);
        assertEquals(0x05cadbeefL, checksum.getValue());

        in.read(buffer); // read beyond available data, IOException expected

        input.close();
        in.close();
    }

    @Test
    public void testRead_byteArray_withOffset() throws IOException {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i + 1);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[5];
        int bytesRead = in.read(buffer, 1, 3);

        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, buffer);
        assertEquals(3, bytesRead);
        assertEquals(0xcbfed556L, checksum.getValue());

        input.close();
        in.close();
    }

    @Test(expected = IOException.class)
    public void testRead_byteArray_withOffset_streamExhausted() throws IOException {
        byte[] data = new byte[10];
        data[0] = 1;
        data[1] = 2;
        data[2] = 3;
        data[3] = 4;
        data[4] = 5;

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[10];
        int bytesRead = in.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals(0x05cadbeefL, checksum.getValue());

        in.read(buffer, 0, 10); // read beyond available data, IOException expected

        input.close();
        in.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i + 1);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        long bytesSkipped = in.skip(3);

        assertEquals(3, bytesSkipped);
        assertEquals(0xd6f3eca5L, checksum.getValue());

        input.close();
        in.close();
    }

    @Test
    public void testSkip_noMoreBytesToSkip() throws IOException {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i + 1);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        long bytesSkipped = in.skip(11); // attempting to skip more bytes than available

        assertEquals(10, bytesSkipped); // skip should not exceed the available bytes
        assertEquals(0x0504030201L, checksum.getValue());

        input.close();
        in.close();
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i + 1);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, input);

        in.skip(4);
        in.read();
        long checksumValue = in.getValue();

        assertEquals(0x82412A1A8L, checksumValue);

        input.close();
        in.close();
    }
}