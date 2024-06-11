import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream inputStream;

    @Before
    public void setup() {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new DeflateCompressorInputStream(byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        assertEquals(1, result);

        result = inputStream.read();
        assertEquals(2, result);

        result = inputStream.read();
        assertEquals(3, result);

        result = inputStream.read();
        assertEquals(4, result);

        result = inputStream.read();
        assertEquals(5, result);

        result = inputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(3, result);

        buffer = new byte[3];
        result = inputStream.read(buffer);
        assertArrayEquals(new byte[]{4, 5, 0}, buffer);
        assertEquals(2, result);

        buffer = new byte[3];
        result = inputStream.read(buffer);
        assertArrayEquals(new byte[]{0, 0, 0}, buffer);
        assertEquals(-1, result);
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(2);
        assertEquals(2, result);

        int readResult = inputStream.read();
        assertEquals(3, readResult);
    }

    @Test
    public void testAvailable() throws IOException {
        int result = inputStream.available();
        assertEquals(5, result);

        inputStream.read(new byte[4]);
        result = inputStream.available();
        assertEquals(1, result);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();

        int result = inputStream.read();
        assertEquals(-1, result);
    }
}