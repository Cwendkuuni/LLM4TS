import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead_singleByte() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
        int result = inputStream.read();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRead_byteArray() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, false);
        byte[] buffer = new byte[1024];
        int result = inputStream.read(buffer, 0, 1024);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullByteArray() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
        inputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, false);
        byte[] buffer = new byte[1024];
        inputStream.read(buffer, 1025, 1);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, false);
        boolean result = inputStream.markSupported();
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_negativeLength() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
        inputStream.skip(-1);
    }

    @Test
    public void testAvailable() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, false);
        int result = inputStream.available();
        assertTrue(result == 0 || result == 1);
    }

}