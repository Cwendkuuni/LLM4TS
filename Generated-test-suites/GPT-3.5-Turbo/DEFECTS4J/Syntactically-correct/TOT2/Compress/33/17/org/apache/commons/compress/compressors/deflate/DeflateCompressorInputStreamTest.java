import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = {72, 101, 108, 108, 111};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        assertEquals('H', deflateInputStream.read());
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] testData = {72, 101, 108, 108, 111};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buf = new byte[3];
        assertEquals(3, deflateInputStream.read(buf, 0, 3));
    }

    @Test
    public void testSkip() throws IOException {
        byte[] testData = {72, 101, 108, 108, 111};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(2, deflateInputStream.skip(2));
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] testData = {72, 101, 108, 108, 111};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(5, deflateInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = {72, 101, 108, 108, 111};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        deflateInputStream.close();
        // No assertion needed, just checking if it throws any exceptions
    }
}