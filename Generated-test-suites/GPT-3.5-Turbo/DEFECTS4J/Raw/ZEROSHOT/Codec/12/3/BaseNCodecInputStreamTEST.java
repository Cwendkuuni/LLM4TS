import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Test case 1
        InputStream in1 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec1 = new BaseNCodec();
        BaseNCodecInputStream inputStream1 = new BaseNCodecInputStream(in1, baseNCodec1, true);
        int result1 = inputStream1.read();
        assertEquals(65, result1);

        // Test case 2
        InputStream in2 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec2 = new BaseNCodec();
        BaseNCodecInputStream inputStream2 = new BaseNCodecInputStream(in2, baseNCodec2, true);
        inputStream2.read();
        int result2 = inputStream2.read();
        assertEquals(66, result2);

        // Test case 3
        InputStream in3 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec3 = new BaseNCodec();
        BaseNCodecInputStream inputStream3 = new BaseNCodecInputStream(in3, baseNCodec3, true);
        inputStream3.read();
        inputStream3.read();
        int result3 = inputStream3.read();
        assertEquals(67, result3);

        // Test case 4
        InputStream in4 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec4 = new BaseNCodec();
        BaseNCodecInputStream inputStream4 = new BaseNCodecInputStream(in4, baseNCodec4, true);
        inputStream4.read();
        inputStream4.read();
        inputStream4.read();
        int result4 = inputStream4.read();
        assertEquals(-1, result4);
    }

    @Test
    public void testReadByteArray() throws IOException {
        // Test case 1
        InputStream in1 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec1 = new BaseNCodec();
        BaseNCodecInputStream inputStream1 = new BaseNCodecInputStream(in1, baseNCodec1, true);
        byte[] buffer1 = new byte[3];
        int result1 = inputStream1.read(buffer1, 0, 3);
        assertArrayEquals(new byte[] {65, 66, 67}, buffer1);
        assertEquals(3, result1);

        // Test case 2
        InputStream in2 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec2 = new BaseNCodec();
        BaseNCodecInputStream inputStream2 = new BaseNCodecInputStream(in2, baseNCodec2, true);
        inputStream2.read();
        byte[] buffer2 = new byte[3];
        int result2 = inputStream2.read(buffer2, 0, 2);
        assertArrayEquals(new byte[] {66, 67, 0}, buffer2);
        assertEquals(2, result2);

        // Test case 3
        InputStream in3 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec3 = new BaseNCodec();
        BaseNCodecInputStream inputStream3 = new BaseNCodecInputStream(in3, baseNCodec3, true);
        inputStream3.read();
        inputStream3.read();
        byte[] buffer3 = new byte[3];
        int result3 = inputStream3.read(buffer3, 0, 1);
        assertArrayEquals(new byte[] {67, 0, 0}, buffer3);
        assertEquals(1, result3);

        // Test case 4
        InputStream in4 = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec4 = new BaseNCodec();
        BaseNCodecInputStream inputStream4 = new BaseNCodecInputStream(in4, baseNCodec4, true);
        byte[] buffer4 = new byte[3];
        int result4 = inputStream4.read(buffer4, 0, 0);
        assertArrayEquals(new byte[] {0, 0, 0}, buffer4);
        assertEquals(0, result4);
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        // Test case 1
        long skip1 = 1;
        long result1 = inputStream.skip(skip1);
        assertEquals(1, result1);

        // Test case 2
        long skip2 = 0;
        long result2 = inputStream.skip(skip2);
        assertEquals(0, result2);
        
        // Test case 3
        long skip3 = 3;
        long result3 = inputStream.skip(skip3);
        assertEquals(2, result3);

        // Test case 4
        long skip4 = -1;
        try {
            inputStream.skip(skip4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The provided skip length is negative", e.getMessage());
        }
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        int result = inputStream.available();
        assertEquals(1, result);
    }

}