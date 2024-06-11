import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    /**
     * Test method for {@link IOUtils#copy(InputStream, OutputStream)}.
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1});
        OutputStream output = new ByteArrayOutputStream();

        long result = IOUtils.copy(input, output);
        Assert.assertEquals(1, result);

        byte[] expectedOutput = new byte[]{(byte) 0x1};
        byte[] actualOutput = output.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link IOUtils#copy(InputStream, OutputStream, int)}.
     * @throws IOException
     */
    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});
        OutputStream output = new ByteArrayOutputStream();

        long result = IOUtils.copy(input, output, IOUtils.COPY_BUF_SIZE);
        Assert.assertEquals(3, result);

        byte[] expectedOutput = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        byte[] actualOutput = output.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link IOUtils#skip(InputStream, long)}.
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});

        long skipped = IOUtils.skip(input, 2);
        Assert.assertEquals(2, skipped);

        int bytesLeft = 3 - 2;
        byte[] actualOutput = new byte[bytesLeft];
        IOUtils.readFully(input, actualOutput);

        byte[] expectedOutput = new byte[]{(byte) 0x3};
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link IOUtils#readFully(InputStream, byte[])}.
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});

        byte[] actualOutput = new byte[3];
        int bytesRead = IOUtils.readFully(input, actualOutput);

        Assert.assertEquals(3, bytesRead);

        byte[] expectedOutput = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link IOUtils#readFully(InputStream, byte[], int, int)}.
     * @throws IOException
     */
    @Test
    public void testReadFullyPartial() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});

        byte[] actualOutput = new byte[2];
        int bytesRead = IOUtils.readFully(input, actualOutput, 0, 2);

        Assert.assertEquals(2, bytesRead);

        byte[] expectedOutput = new byte[]{(byte) 0x1, (byte) 0x2};
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link IOUtils#toByteArray(InputStream)}.
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});

        byte[] actualOutput = IOUtils.toByteArray(input);

        byte[] expectedOutput = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link IOUtils#closeQuietly(Closeable)}.
     */
    @Test
    public void testCloseQuietly() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});

        IOUtils.closeQuietly(input);

        try {
            input.close();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}