import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.zip.InflaterInputStream;
import java.util.zip.Inflater;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x01, 0x23, 0x45, 0x67, 0x89, 0xAB, 0xCD, 0xEF});
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(0x78, deflateCompressorInputStream.read());
        assertEquals(0x01, deflateCompressorInputStream.read());
        assertEquals(0x23, deflateCompressorInputStream.read());
        assertEquals(0x45, deflateCompressorInputStream.read());
        assertEquals(0x67, deflateCompressorInputStream.read());
        assertEquals(0x89, deflateCompressorInputStream.read());
        assertEquals(0xAB, deflateCompressorInputStream.read());
        assertEquals(0xCD, deflateCompressorInputStream.read());
        assertEquals(0xEF, deflateCompressorInputStream.read());
        assertEquals(-1, deflateCompressorInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(5, deflateCompressorInputStream.read(buffer));
        assertArrayEquals(new byte[]{0x78, 0x01, 0x23, 0x45, 0x67}, buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(5, deflateCompressorInputStream.read(buffer, 2, 5));
        assertArrayEquals(new byte[]{0x00, 0x00, 0x78, 0x01, 0x23, 0x45, 0x67, 0x00, 0x00, 0x00}, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(5, deflateCompressorInputStream.skip(5));
        assertEquals(0x67, deflateCompressorInputStream.read());
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(9, deflateCompressorInputStream.available());
        assertEquals(4, deflateCompressorInputStream.skip(4));
        assertEquals(5, deflateCompressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        try {
            deflateCompressorInputStream.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testZlibHeaderParameter() throws NoSuchFieldException, IllegalAccessException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x01, 0x23, 0x45, 0x67, 0x89, 0xAB, 0xCD, 0xEF});
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateParameters.setWithZlibHeader(true);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Field inflaterField = deflateCompressorInputStream.getClass().getDeclaredField("in");
        inflaterField.setAccessible(true);
        Object inflaterObject = inflaterField.get(deflateCompressorInputStream);
        assertTrue(inflaterObject instanceof InflaterInputStream);
        InflaterInflaterInputStream inflaterInputStream = (InflaterInputStream) inflaterObject;
        Field inflater = inflaterInputStream.getClass().getDeclaredField("inf");
        inflater.setAccessible(true);
        Object inf = inflater.get(inflaterInputStream);
        assertTrue(inf instanceof Inflater);
        Inflater inflaterInstance = (Inflater) inf;
        assertTrue(inflaterInstance.usesDefaultHeader());
    }

}