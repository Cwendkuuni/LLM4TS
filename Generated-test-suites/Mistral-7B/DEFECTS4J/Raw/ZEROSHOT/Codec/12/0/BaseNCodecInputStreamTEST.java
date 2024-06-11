package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} .
     */
    @Test
    public void testBaseNCodecInputStreamConstruction() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertNotNull(inputStreamFilter);
        assertEquals(inputStream, inputStreamFilter.in);
        assertEquals(false, inputStreamFilter.doEncode);
        assertEquals(baseNCodec, inputStreamFilter.baseNCodec);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} .
     */
    @Test
    public void testBaseNCodecInputStreamConstructionEncode() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertNotNull(inputStreamFilter);
        assertEquals(inputStream, inputStreamFilter.in);
        assertEquals(true, inputStreamFilter.doEncode);
        assertEquals(baseNCodec, inputStreamFilter.baseNCodec);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()} .
     */
    @Test
    public void testReadOneByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readByte = inputStreamFilter.read();
        assertEquals('A', readByte);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()} .
     */
    @Test
    public void testReadEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readByte = inputStreamFilter.read();
        assertEquals(EOF, readByte);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)} .
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] buffer = new byte[3];
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B', 'C' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readBytes = inputStreamFilter.read(buffer, 0, buffer.length);
        assertEquals(buffer.length, readBytes);
        assertEquals('A', buffer[0]);
        assertEquals('B', buffer[1]);
        assertEquals('C', buffer[2]);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)} .
     */
    @Test
    public void testReadEndOfStreamMultipleBytes() throws IOException {
        byte[] buffer = new byte[3];
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readBytes = inputStreamFilter.read(buffer, 0, buffer.length);
        assertEquals(EOF, readBytes);
        assertNull(buffer);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()} .
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteWithNegativeOffset() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int readByte = inputStreamFilter.read(-1);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()} .
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[3];
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int readBytes = inputStreamFilter.read(buffer, -1, buffer.length);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)} .
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeLength() throws IOException {
        byte[] buffer = new byte[3];
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int readBytes = inputStreamFilter.read(buffer, 0, -1);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#markSupported()} .
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse(inputStreamFilter.markSupported());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)} .
     */
    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long skipBytes = 1024;
        long skippedBytes = inputStreamFilter.skip(skipBytes);
        assertEquals(skipBytes, skippedBytes);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)} .
     */
    @Test
    public void testSkipMoreThanAvailableBytes() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long skipBytes = 1025;
        long skippedBytes = inputStreamFilter.skip(skipBytes);
        assertEquals(-1, skippedBytes);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read} with mocked {@link BaseNCodec}.
     */
    @Test
    public void testReadWithMockedBaseNCodec() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { 'X', 'Y' });
        when(baseNCodec.readResults(anyByteArray(), anyInt(), anyInt())).thenReturn(2);

        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];

        int readBytes = inputStreamFilter.read(buffer, 0, buffer.length);
        assertEquals(2, readBytes);
        assertEquals('X', buffer[0]);
        assertEquals('Y', buffer[1]);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read} with mocked {@link BaseNCodec} and EOF.
     */
    @Test
    public void testReadWithMockedBaseNCodecEndOfStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.hasData()).thenReturn(false);

        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[1];

        int readBytes = inputStreamFilter.read(buffer, 0, buffer.length);
        assertEquals(EOF, readBytes);
        assertNull(buffer);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read} with mocked {@link BaseNCodec} and no data.
     */
    @Test
    public void testReadWithMockedBaseNCodecNoData() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.hasData()).thenReturn(false);

        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[1];

        int readBytes = inputStreamFilter.read(buffer, 0, buffer.length);
        assertEquals(EOF, readBytes);
        assertNull(buffer);
    }

    /**
     * Test method for {@link BaseNCodecOutputStream#write} with mocked {@link BaseNCodec}.
     */
    @Test
    public void testWriteWithMockedBaseNCodec() throws IOException {
        byte[] sourceData = new byte[] { 0x30, 0x1E, 0x23, 0x24 };
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.encode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { '1', '2' });
        when(baseNCodec.writeTo(anyByteArray(), anyInt(), anyInt(), any(OutputStream.class)))
                .thenAnswer(invocation -> invocation.getArgument(3).write(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2)));

        BaseNCodecOutputStream outputStreamFilter = new BaseNCodecOutputStream(outputStream, baseNCodec, true);
        outputStreamFilter.write(sourceData, 0, sourceData.length);

        byte[] expectedOutputData = new byte[] { '1', '2' };
        byte[] actualOutputData = outputStream.toByteArray();
        assertArrayEquals(expectedOutputData, actualOutputData);
    }
}