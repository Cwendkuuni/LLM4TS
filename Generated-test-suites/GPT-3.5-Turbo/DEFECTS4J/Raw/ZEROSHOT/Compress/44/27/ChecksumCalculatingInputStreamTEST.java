import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(data));
        int result = inputStream.read();

        assert result == 1;
        assert inputStream.getValue() == 2386489562L;
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(data));
        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer);

        assert result == 3;
        assert inputStream.getValue() == 2386489562L;
    }

    @Test
    public void testReadArrayOffsetLength() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(data));
        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer, 0, 3);

        assert result == 3;
        assert inputStream.getValue() == 2386489562L;
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(data));
        long result = inputStream.skip(2);

        assert result == 1;
        assert inputStream.getValue() == 2386489562L;
    }
}