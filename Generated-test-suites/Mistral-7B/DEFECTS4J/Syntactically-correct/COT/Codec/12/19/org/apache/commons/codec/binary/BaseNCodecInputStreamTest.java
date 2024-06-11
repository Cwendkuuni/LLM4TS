import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream inputStream;
    private ByteArrayInputStream inputStreamSource;
    private ByteArrayOutputStream outputStream;
    private BaseNCodecOutputStream outputStreamDestination;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        inputStreamSource = new ByteArrayInputStream(new byte[]{0x5, 0x6, 0x7, 0x8});
        outputStream = new ByteArrayOutputStream();
        outputStreamDestination = new BaseNCodecOutputStream(outputStream, baseNCodec, true);
        inputStream = new BaseNCodecInputStream(inputStreamSource, baseNCodec, true);
    }

    @Test
    public void testReadMethod() throws IOException {
        int readByte;
        for (int i = 0; i < 4; i++) {
            readByte = inputStream.read();
            assertEquals("Unexpected byte read", (byte) (i + 5), readByte);
        }
        assertEquals("End of file not reached", BaseNCodecInputStream.EOF, inputStream.read());
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = inputStream.read(buffer);
        assertArrayEquals("Unexpected number of bytes read", new byte[]{5, 6, 7, 8}, buffer);
        bytesRead = inputStream.read(buffer);
        assertEquals("End of file not reached", 0, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayMethodWithNullBuffer() throws IOException {
        inputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayMethodWithNegativeOffset() throws IOException {
        inputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayMethodWithNegativeLength() throws IOException {
        inputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayMethodWithBufferSizeTooSmall() throws IOException {
        inputStream.read(new byte[1], 0, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayMethodWithOffsetTooBig() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer, 3, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayMethodWithOffsetAndLengthTooBig() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer, 3, 3);
    }

    @Test
    public void testMarkSupportedMethod() throws IOException {
        assertFalse("Marking not supported", inputStream.markSupported());
    }

    @Test
    public void testCloseMethod() throws IOException {
        inputStream.close();
        assertFalse("InputStream was not closed", inputStream.isOpen());
    }

    @Test
    public void testSkipMethod() throws IOException {
        long bytesToSkip = 512;
        inputStream.skip(bytesToSkip);
        assertFalse("Unexpected EOF flag", inputStream.markSupported());
        byte[] buffer = new byte[1];
        inputStream.read(buffer);
        assertEquals("Unexpected byte read", (byte) 6, buffer[0]);
    }

    @Test
    public void testAvailableMethod() throws IOException {
        assertEquals("Unexpected number of bytes available", 4, inputStream.available());
    }

    @Test
    public void testResetMethod() throws IOException {
        inputStream.mark(100);
        inputStream.read();
        inputStream.reset();
        assertEquals("Unexpected number of bytes available", 4, inputStream.available());
    }

    @Test
    public void testReadFullyMethod() throws IOException {
        byte[] buffer = new byte[4];
        inputStream.readFully(buffer);
        assertArrayEquals("Unexpected byte array", new byte[]{5, 6, 7, 8}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyMethodWithNullBuffer() throws IOException {
        inputStream.readFully(null);
    }

    @Test
    public void testTransferToMethod() throws IOException {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        int bytesTransferred = inputStream.transferTo(target);
        assertEquals("Unexpected number of bytes transferred", 4, bytesTransferred);
        byte[] result = target.toByteArray();
        assertArrayEquals("Unexpected byte array", new byte[]{5, 6, 7, 8}, result);
    }

    @Test(expected = NullPointerException.class)
    public void testTransferToMethodWithNullOutputStream() throws IOException {
        inputStream.transferTo(null);
    }
}