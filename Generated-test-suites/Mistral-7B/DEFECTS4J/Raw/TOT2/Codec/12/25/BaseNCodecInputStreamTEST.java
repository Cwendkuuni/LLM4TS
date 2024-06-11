package org.junit.tests;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadMethod() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readByte = baseNCodecInputStream.read();

        Assert.assertNotEquals(-1, readByte);
        Assert.assertEquals(readByte, (byte) 'T');
    }

    @Test
    public void testReadAtEndOfInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.markSupported(); // mark the stream for reading later
        baseNCodecInputStream.skip(inputStream.available()); // skip all available bytes

        int readByte = baseNCodecInputStream.read();

        Assert.assertEquals(-1, readByte);
    }

    @Test
    public void testReadEmptyInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readByte = baseNCodecInputStream.read();

        Assert.assertEquals(-1, readByte);
    }

    @Test
    public void testReadPastEndOfInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.markSupported(); // mark the stream for reading later
        baseNCodecInputStream.skip(inputStream.available()); // skip all available bytes

        int readByte = baseNCodecInputStream.read();
        int readByte2 = baseNCodecInputStream.read();

        Assert.assertEquals(-1, readByte);
        Assert.assertEquals(-1, readByte2);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullInputStream() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(null, baseNCodec, false);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBaseNCodecObject() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeByteArrayOffset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int[] byteArray = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't' };
        int readByte = baseNCodecInputStream.read(byteArray, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeByteArrayLength() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readByte = baseNCodecInputStream.read(new byte[0], 0, -1);
    }
}