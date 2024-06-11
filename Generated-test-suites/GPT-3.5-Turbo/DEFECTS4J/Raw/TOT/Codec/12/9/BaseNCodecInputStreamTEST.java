
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private ByteArrayInputStream dummyInputStream;

    private static final int RANDOM_TEST_ITERATIONS = 100;
    private static final int RANDOM_TEST_MAX_BYTES = 1024;

    @Before
    public void setUp() {
        dummyInputStream = new ByteArrayInputStream(new byte[]{});
    }

    @Test
    public void testRead() throws IOException, EncoderException, DecoderException {
        String inputString = "hello world!";
        byte[] inputBytes = inputString.getBytes();
        BaseNCodec codec = new Base32();
        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        codec.encode(inputBytes, encodedOutputStream);

        dummyInputStream = new ByteArrayInputStream(encodedOutputStream.toByteArray());
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, false);

        int result;
        for (byte inputByte : inputBytes) {
            result = baseNCodecInputStream.read();
            assertEquals(((int) inputByte) & 0xFF, result);
        }

        result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException, EncoderException, DecoderException {
        BaseNCodec codec = new Base32();
        dummyInputStream = new ByteArrayInputStream(new byte[]{});
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithNonBaseNData() throws IOException, EncoderException, DecoderException {
        String inputString = "random text";
        byte[] inputBytes = inputString.getBytes();
        BaseNCodec codec = new Base32();
        dummyInputStream = new ByteArrayInputStream(inputBytes);
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, true);

        int result = baseNCodecInputStream.read();
        assertTrue(result == -1 || result == ((int) inputBytes[0]) & 0xFF);
    }

    @Test
    public void testReadWithZeroLength() throws IOException, EncoderException, DecoderException {
        String inputString = "hello world!";
        byte[] inputBytes = inputString.getBytes();
        BaseNCodec codec = new Base32();
        dummyInputStream = new ByteArrayInputStream(inputBytes);
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, true);

        int result = baseNCodecInputStream.read(new byte[0], 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testReadWithNullByteArray() throws IOException, EncoderException, DecoderException {
        String inputString = "hello world!";
        byte[] inputBytes = inputString.getBytes();
        BaseNCodec codec = new Base32();
        dummyInputStream = new ByteArrayInputStream(inputBytes);
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, true);

        try {
            baseNCodecInputStream.read(null, 0, inputBytes.length);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithInvalidLengthsAndOffsets() throws IOException, EncoderException, DecoderException {
        String inputString = "hello world!";
        byte[] inputBytes = inputString.getBytes();
        BaseNCodec codec = new Base32();
        dummyInputStream = new ByteArrayInputStream(inputBytes);
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, true);

        try {
            baseNCodecInputStream.read(inputBytes, -1, inputBytes.length);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }

        try {
            baseNCodecInputStream.read(inputBytes, 0, -1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }

        try {
            baseNCodecInputStream.read(inputBytes, 0, inputBytes.length + 1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }

        try {
            baseNCodecInputStream.read(inputBytes, inputBytes.length, 1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadResults() throws IOException, EncoderException, DecoderException {
        Random random = new Random();
        byte[] inputData = new byte[RANDOM_TEST_MAX_BYTES];
        random.nextBytes(inputData);
  
        for (int i = 0; i < RANDOM_TEST_ITERATIONS; i++) {
            BaseNCodec codec = new Base32();
            ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
            codec.encode(inputData, encodedOutputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedOutputStream.toByteArray());
            baseNCodecInputStream = new BaseNCodecInputStream(inputStream, codec, false);
            byte[] outputData = new byte[RANDOM_TEST_MAX_BYTES];
            int readLen = baseNCodecInputStream.read(outputData, 0, outputData.length);

            byte[] expectedOutputData = Arrays.copyOfRange(inputData, 0, readLen);
            byte[] actualOutputData = Arrays.copyOfRange(outputData, 0, readLen);
            assertEquals(expectedOutputData, actualOutputData);
        }
    }

    @Test
    public void testMarkSupported() {
        BaseNCodec codec = new Base32();
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() throws IOException {
        BaseNCodec codec = new Base32();
        dummyInputStream = new ByteArrayInputStream(new byte[]{});
        baseNCodecInputStream = new BaseNCodecInputStream(dummyInputStream, codec, false);

        assertEquals(0, baseNCodecInputStream.available());
    }
}

