package org.apache.commons.compress.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Test;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);
        assert null != cis;
        assertEquals(md, cis.getChecksum());
        assertEquals(input, cis.getInputStream());
    }

    /**
     * Test read method with single byte.
     * @throws IOException
     */
    @Test
    public void testReadSingleByte() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final int ret = cis.read();
        assertEquals(0x01, ret);
        assertEquals(0x01, md.digest()[0] & 0xFF);
    }

    /**
     * Test read method with multiple bytes.
     * @throws IOException
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final byte[] b = new byte[2];
        final int ret = cis.read(b, 0, b.length);
        assertEquals(2, ret);
        assertEquals(0x01, b[0]);
        assertEquals(0x02, b[1]);
        assertEquals(0x0102, ByteBuffer.wrap(md.digest()).getLong());
    }

    /**
     * Test read method with end of stream.
     * @throws IOException
     */
    @Test
    public void testReadEndOfStream() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final int ret = cis.read();
        assertEquals(0x01, ret);
        final int ret2 = cis.read();
        assertEquals(-1, ret2);
        assertEquals(0x01, md.digest()[0] & 0xFF);
    }

    /**
     * Test read method with exception.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testReadException() throws IOException, NoSuchAlgorithmException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        assertThrows(IOException.class, () -> new ChecksumCalculatingInputStream(null, input));
        assertThrows(IOException.class, () -> new ChecksumCalculatingInputStream(new MessageDigest("nonexistentalgorithm"), input));
    }

    /**
     * Test skip method.
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final long ret = cis.skip(5);
        assertEquals(1, ret);
        assertEquals(0x01, md.digest()[0] & 0xFF);
    }

    /**
     * Test getValue method.
     * @throws IOException
     */
    @Test
    public void testGetValue() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final byte[] b = new byte[4];
        cis.read(b, 0, b.length);
        final long checksum = cis.getValue();
        assertEquals(0x01020102L, checksum);
        assertEquals(0x01020102, ByteBuffer.wrap(md.digest()).getLong());
    }

    /**
     * Test getValue method with empty stream.
     * @throws IOException
     */
    @Test
    public void testGetValueEmptyStream() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[0][0]);
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final long checksum = cis.getValue();
        assertEquals(0, checksum);
    }

    /**
     * Test getValue method with exception.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testGetValueException() throws IOException, NoSuchAlgorithmException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        assertThrows(IOException.class, () -> new ChecksumCalculatingInputStream(null, input).getValue());
        assertThrows(IOException.class, () -> new ChecksumCalculatingInputStream(new MessageDigest("nonexistentalgorithm"), input).getValue());
    }

    /**
     * Test read from channel.
     * @throws IOException
     */
    @Test
    public void testReadFromChannel() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(new byte[] { 0x01, 0x02 }));
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, channel);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int bytesRead;
        do {
            bytesRead = cis.read(buffer);
            if (bytesRead > 0) {
                output.write(buffer, 0, bytesRead);
            }
        } while (bytesRead > 0);
        final byte[] data = output.toByteArray();
        final long checksum = cis.getValue();
        assertEquals(0x0102, ChecksumUtils.getChecksum(data, "MD5"));
        assertEquals(0x0102, ByteBuffer.wrap(md.digest()).getLong());
    }
}