import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    // Test Data
    private static final byte[] TEST_DATA = "Hello World".getBytes();

    // Constructor test
    @Test
    public void testConstructor() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        Assert.assertNotNull(inputStream);
        Assert.assertTrue(inputStream.available() >= 0);
        Assert.assertEquals(baseNCodec, inputStream.getBaseNCodec());
        Assert.assertEquals(true, inputStream.getDoEncode());
    }

    // Test read() method
    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        int result = inputStream.read();

        Assert.assertTrue(result >= 0 && result <= 255);

        int eofResult = inputStream.read();
        Assert.assertEquals(-1, eofResult);
    }

    // Test read(byte b[], int offset, int len) method
    @Test
    public void testReadWithBuffer() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[TEST_DATA.length];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals(TEST_DATA.length, bytesRead);
        Assert.assertArrayEquals(TEST_DATA, buffer);
    }

    // Test read(byte b[], int offset, int len) method with invalid inputs
    @Test(expected = NullPointerException.class)
    public void testReadWithBufferInvalidInputs1() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = null;
        inputStream.read(buffer, 0, TEST_DATA.length);

        Assert.fail("NullPointerException should have been thrown");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithBufferInvalidInputs2() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[TEST_DATA.length];
        inputStream.read(buffer, -1, TEST_DATA.length);

        Assert.fail("IndexOutOfBoundsException should have been thrown");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithBufferInvalidInputs3() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[TEST_DATA.length];
        inputStream.read(buffer, 0, TEST_DATA.length + 1);

        Assert.fail("IndexOutOfBoundsException should have been thrown");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithBufferInvalidInputs4() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[TEST_DATA.length + 1];
        inputStream.read(buffer, 1, TEST_DATA.length);

        Assert.fail("IndexOutOfBoundsException should have been thrown");
    }

    // Test markSupported() method
    @Test
    public void testMarkSupported() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        Assert.assertFalse(inputStream.markSupported());
    }

    // Test available() method
    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        Assert.assertEquals(TEST_DATA.length, inputStream.available());
    }

    // Test read(byte b[], int offset, int len) method with data in multiple chunks
    @Test
    public void testReadWithMultipleChunks() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[TEST_DATA.length];

        // Read 5 bytes in the first chunk
        int bytesRead1 = inputStream.read(buffer, 0, 5);

        Assert.assertEquals(5, bytesRead1);
        Assert.assertArrayEquals("Hello".getBytes(), buffer);

        // Read 6 bytes in the second chunk
        int bytesRead2 = inputStream.read(buffer, 0, 6);

        Assert.assertEquals(6, bytesRead2);
        Assert.assertArrayEquals("World".getBytes(), buffer);
    }

    // Test the readResults method
    @Test
    public void testReadResults() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return true;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return length;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.readResults(buffer, 0, 5);

        Assert.assertEquals(5, bytesRead);
    }

    // Test readResults method with no data available
    @Test
    public void testReadResultsWithNoDataAvailable() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            public void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.readResults(buffer, 0, 5);

        Assert.assertEquals(0, bytesRead);
    }

}