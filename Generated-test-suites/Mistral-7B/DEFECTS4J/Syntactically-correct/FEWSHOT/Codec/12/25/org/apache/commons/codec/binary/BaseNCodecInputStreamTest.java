package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        assertFalse(inputStream.markSupported());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() throws IOException {
        new BaseNCodecInputStream(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSkip() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        bs.skip(-1);
    }

    @Test
    public void testSkip() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        bs.skip(1);
        byte[] actual = new byte[1];
        int read = bs.read(actual, 0, 1);
        assertEquals("Expected reading a byte after skipping", 0, read);
        assertNull("Expected EOF after reading a byte", bs.read(actual, 0, 1));
    }

    @Test
    public void testRead() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes(), "B".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);

        byte[] actual = new byte[1];
        int read = bs.read();
        assertEquals("Expected reading the first byte", 'A', read);
        read = bs.read();
        assertEquals("Expected reading the second byte", 'B', read);
        read = bs.read();
        assertNull("Expected EOF after reading the second byte", bs.read(actual, 0, 1));
    }

    @Test
    public void testReadBytes() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes(), "B".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);

        byte[] expected = new byte[]{'A', 'B'};
        byte[] actual = new byte[2];
        int read = bs.read(actual, 0, 2);
        assertNotNull("Expected reading 2 bytes", actual);
        assertTrue("Expected expected bytes to match the actual bytes", Arrays.equals(expected, actual));
        assertNull("Expected EOF after reading 2 bytes", bs.read(actual, 0, 1));
    }

    @Test
    public void testDoEncode() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes(), "B".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1];
        int bytesRead = 0;

        while ((bytesRead = bs.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        assertEquals("Expected decoded bytes to match the original bytes", "AB", new String(outputStream.toByteArray()));
    }

    @Test
    public void testDoDecode() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"AA==".getBytes(), "AB".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);

        byte[] actual = new byte[2];
        int read = bs.read(actual, 0, 2);
        assertNotNull("Expected reading 2 bytes", actual);
        assertEquals("Expected decoded bytes to match the expected bytes", "AB", new String(actual, 0, 2));
    }

    @Test
    public void testHasData() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        assertFalse("Expected no data when passing an empty input stream", bs.hasData());

        inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()});
        bs = new BaseNCodecInputStream(inputStream, codec, false);
        assertTrue("Expected data when passing a non-empty input stream", bs.hasData());
    }

    @Test
    public void testReadResults() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] buffer = new byte[10];

        int bytesRead = bs.read(buffer, 0, buffer.length);
        assertEquals("Expected 0 bytes to be read when there is no data", 0, bytesRead);
        assertNull("Expected EOF after reading 0 bytes", bs.read(buffer, 0, buffer.length));

        inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()});
        bs = new BaseNCodecInputStream(inputStream, codec, false);
        bytesRead = bs.read(buffer, 0, buffer.length);
        assertEquals("Expected 1 byte to be read when there is one byte of data", 1, bytesRead);
        assertEquals("Expected the first byte to be 'A'", 'A', buffer[0]);
        assertNull("Expected EOF after reading 1 byte", bs.read(buffer, 0, buffer.length));

        inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes(), "B".getBytes()});
        bs = new BaseNCodecInputStream(inputStream, codec, false);
        bytesRead = bs.read(buffer, 0, buffer.length);
        assertEquals("Expected 2 bytes to be read when there are 2 bytes of data", 2, bytesRead);
        assertEquals("Expected the first byte to be 'A'", 'A', buffer[0]);
        assertEquals("Expected the second byte to be 'B'", 'B', buffer[1]);
        assertNull("Expected EOF after reading 2 bytes", bs.read(buffer, 0, buffer.length));
    }

    @Test
    public void testReadResultsWithPartialData() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes(), "B".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] buffer = new byte[3];

        int bytesRead = bs.read(buffer, 0, buffer.length);
        assertEquals("Expected 2 bytes to be read when there are 2 bytes of data", 2, bytesRead);
        assertEquals("Expected the first byte to be 'A'", 'A', buffer[0]);
        assertEquals("Expected the second byte to be 'B'", 'B', buffer[1]);
        assertNull("Expected EOF after reading 2 bytes", bs.read(buffer, 0, buffer.length));
    }

    @Test
    public void testReadResultsWithEmptyData() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] buffer = new byte[1];

        int bytesRead = bs.read(buffer, 0, buffer.length);
        assertNull("Expected EOF when there is no data", bytesRead);
    }

    @Test
    public void testReadResultsWithSingleByteData() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] buffer = new byte[1];

        int bytesRead = bs.read(buffer, 0, buffer.length);
        assertEquals("Expected 1 byte to be read when there is one byte of data", 1, bytesRead);
        assertEquals("Expected the byte to be 'A'", 'A', buffer[0]);
        assertNull("Expected EOF after reading 1 byte", bs.read(buffer, 0, buffer.length));
    }

    @Test
    public void testReadResultsWithPartialByteData() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()});
        BaseNCodecInputStream bs = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] buffer = new byte[2];

        int bytesRead = bs.read(buffer, 0, buffer.length);
        assertEquals("Expected 1 byte to be read when there is one byte of data", 1, bytesRead);
        assertEquals("Expected the byte to be 'A'", 'A', buffer[0]);
        assertNull("Expected EOF after reading 1 byte", bs.read(buffer, 0, buffer.length));
    }
}