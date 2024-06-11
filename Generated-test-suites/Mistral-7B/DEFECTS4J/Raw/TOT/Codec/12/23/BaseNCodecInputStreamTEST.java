package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testReadByte() throws IOException {
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(new byte[]{'A'})) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };
        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte readByte = (byte) this.inputStream.read();
        Assert.assertEquals((byte) 'A', readByte);
    }

    @Test
    public void testReadByte_EOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int read = this.inputStream.read();
        Assert.assertEquals(BaseNCodecInputStream.EOF, read);
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(input)) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };

        byte[] output = new byte[3];
        int read = this.inputStream.read(output, 0, 3);

        Assert.assertEquals(read, input.length);
        Assert.assertArrayEquals(input, output);
    }

    @Test
    public void testReadArray_EOF() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        byte[] output = new byte[3];
        int read = this.inputStream.read(output, 0, 3);

        Assert.assertEquals(BaseNCodecInputStream.EOF, read);
    }

    @Test
    public void testReadArray_Partial() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(input)) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };

        byte[] output = new byte[2];
        int read = this.inputStream.read(output, 0, 2);

        Assert.assertEquals(read, 2);
        Assert.assertArrayEquals(new byte[]{'A', 'B'}, output);
    }

    @Test(expected = NullPointerException.class)
    public void testReadArray_Null() throws IOException {
        byte[] output = null;
        int read = this.inputStream.read(output, 0, 3);

        // this should throw a NullPointerException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArray_IndexOutOfBounds() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        byte[] output = new byte[3];

        int read = this.inputStream.read(output, 2, 3);

        // this should throw an IndexOutOfBoundsException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArray_NegativeOffset() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        byte[] output = new byte[3];

        int read = this.inputStream.read(output, -1, 3);

        // this should throw an IndexOutOfBoundsException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArray_NegativeLength() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        byte[] output = new byte[3];

        int read = this.inputStream.read(output, 0, -1);

        // this should throw an IndexOutOfBoundsException
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(new byte[0])) {
            @Override
            public boolean markSupported() {
                return true;
            }
        };

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        Assert.assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(input)) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };

        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long skip = 3;
        this.inputStream.skip(skip);

        byte[] expected = new byte[3];
        this.inputStream.read(expected);

        Assert.assertArrayEquals(input, expected);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(input)) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };

        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int available = this.inputStream.available();
        Assert.assertEquals(input.length, available);

        byte[] output = new byte[input.length];
        int read = this.inputStream.read(output, 0, input.length);

        Assert.assertEquals(input.length, read);
        Assert.assertArrayEquals(input, output);
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = {'A', 'B', 'C'};
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(input)) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };

        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        this.inputStream.close();

        byte[] output = new byte[3];
        int read = this.inputStream.read(output, 0, 3);

        Assert.assertEquals(BaseNCodecInputStream.EOF, read);
    }

    @Test
    public void testDecode() throws IOException {
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF".getBytes())) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = this.inputStream.read(buffer, 0, buffer.length)) != BaseNCodecInputStream.EOF) {
            outputStream.write(buffer, 0, read);
        }

        byte[] decoded = new BaseNCodec().decode(outputStream.toByteArray());
        Assert.assertArrayEquals(new byte[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C'}, decoded);
    }

    @Test
    public void testEncode() throws IOException {
        InputStream inputStream = new FilterInputStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C'})) {
            @Override
            public boolean markSupported() {
                return false;
            }
        };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.inputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = this.inputStream.read(buffer, 0, buffer.length)) != BaseNCodecInputStream.EOF) {
            outputStream.write(buffer, 0, read);
        }

        String encoded = new String(baseNCodec.encode(outputStream.toByteArray()));
        Assert.assertEquals("ABCABC", encoded);
    }
}