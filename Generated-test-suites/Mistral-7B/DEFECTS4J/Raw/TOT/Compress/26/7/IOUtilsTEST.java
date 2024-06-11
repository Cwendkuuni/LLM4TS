package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test cases for IOUtils class.
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid input and output streams.
     */
    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 'st'});

        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(3, copiedBytes);

        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, output.toByteArray());
    }

    /**
     * Test copy method with empty input stream.
     */
    @Test
    public void testCopyWithEmptyInput() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[0]);

        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(0, copiedBytes);

        Assert.assertArrayEquals(new byte[0], output.toByteArray());
    }

    /**
     * Test copy method with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullInputStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = null;

        IOUtils.copy(input, output);
    }

    /**
     * Test copy method with null output stream.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullOutputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        OutputStream output = null;

        IOUtils.copy(input, output);
    }

    /**
     * Test skip method with valid input stream and known number of bytes to skip.
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'e', 's', 't', 'e', 's'});

        long skipped = IOUtils.skip(input, 3L);
        Assert.assertEquals(3, skipped);

        Assert.assertArrayEquals(new byte[]{'s', 't', 'e', 's', 't', 'e', 's', 't', 'e', 's'}, input.readAllBytes());
    }

    /**
     * Test skip method with valid input stream and zero bytes to skip.
     */
    @Test
    public void testSkipWithZeroBytes() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});

        long skipped = IOUtils.skip(input, 0L);
        Assert.assertEquals(0, skipped);

        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, input.readAllBytes());
    }

    /**
     * Test skip method with valid input stream and known number of bytes greater than available bytes.
     */
    @Test
    public void testSkipWithMoreBytesThanAvailable() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T'});

        long skipped = IOUtils.skip(input, 3L);
        Assert.assertEquals(0, skipped);

        Assert.assertArrayEquals(new byte[]{'T'}, input.readAllBytes());
    }

    /**
     * Test skip method with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testSkipWithNullInputStream() throws IOException {
        InputStream input = null;

        IOUtils.skip(input, 0L);
    }

    /**
     * Test readFully method with valid input stream and byte array.
     */
    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] output = new byte[3];

        int read = IOUtils.readFully(input, output);
        Assert.assertEquals(3, read);

        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, output);
    }

    /**
     * Test readFully method with empty input stream.
     */
    @Test
    public void testReadFullyWithEmptyInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] output = new byte[0];

        int read = IOUtils.readFully(input, output);
        Assert.assertEquals(0, read);

        Assert.assertArrayEquals(new byte[0], output);
    }

    /**
     * Test readFully method with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullInputStream() throws IOException {
        InputStream input = null;
        byte[] output = new byte[3];

        IOUtils.readFully(input, output);
    }

    /**
     * Test readFully method with null output array.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullOutputArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[][] output = null;

        IOUtils.readFully(input, output);
    }

    /**
     * Test toByteArray method with valid input stream.
     */
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});

        byte[] expected = new byte[]{'T', 'e', 's'};
        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test toByteArray method with empty input stream.
     */
    @Test
    public void testToByteArrayWithEmptyInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);

        byte[] expected = new byte[0];
        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test toByteArray method with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayWithNullInputStream() throws IOException {
        InputStream input = null;

        IOUtils.toByteArray(input);
    }
}