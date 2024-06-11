package org.apache.commons.compress.archivers.sevenz; // import necessary packages here

import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

    

    @Test
    public void testAddDecoderWithSupportedMethod() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03});
        Coder coder = new DeflateDecoder();
        byte[] password = new byte[0];
        InputStream expectedOutput = new ByteArrayInputStream(new byte[]{(byte) 0x78, 0x9c, 0x6e, 0x01, 0x02, 0x03});

        InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);

        byte[] actualBytes = new byte[expectedOutput.available()];
        actualOutput.read(actualBytes);

        assertArrayEquals(expectedOutput.readAllBytes(), actualBytes);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderWithUnsupportedMethod() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03});
        Coder coder = new LZMA2Decoder();
        byte[] password = new byte[0];

        Coders.addDecoder(inputStream, coder, password);
    }

    @Test
    public void testAddEncoderWithSupportedMethod() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = new byte[16];

        Coders.addEncoder(outputStream, method, password);

        byte[] expectedOutput = new byte[10]; // expected output size
        byte[] input = new byte[10]; // input data
        for (int i = 0; i < 10; i++) {
            input[i] = (byte) (i + 1);
        }

        outputStream.write(input);
        outputStream.flush();

        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderWithUnsupportedMethod() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = new byte[0];

        Coders.addEncoder(outputStream, method, password);
    }

    @Test
    public void testLZMADecoderWithValidProperties() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, (byte) 0x01, (byte) 0x04,
                (byte) 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d});
        Coder coder = new LZMADecoder();
        byte[] password = new byte[0];

        InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);

        byte[] actualBytes = new byte[17]; // expected output size
        actualOutput.read(actualBytes);

        byte[] expectedOutput = new byte[]{(byte) 0x31, 0x12, 0x23, 0x34, 0x56, (byte) 0x78, 0x9c, 0x6e, 0x01, 0x02,
                (byte) 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

        assertArrayEquals(expectedOutput, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testLZMADecoderWithInvalidDictSize() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, (byte) 0x01, (byte) 0x04,
                (byte) 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, (byte) 0xff, 0x0d});
        Coder coder = new LZMADecoder();
        byte[] password = new byte[0];

        Coders.addDecoder(inputStream, coder, password);
    }

    @Test
    public void testAES256SHA256DecoderWithValidPassword() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f});
        Coder coder = new AES256SHA256Decoder();
        byte[] password = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};

        InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);

        byte[] actualBytes = new byte[16];
        actualOutput.read(actualBytes);

        byte[] expectedOutput = new byte[]{0x67, 0x8F, 0xAC, 0xDB, 0xDB, 0x2B, 0x55, 0x66, 0xD3, 0x27, 0x4E, 0xD8,
                0x8E, 0x5D, 0x0B, 0x48};

        assertArrayEquals(expectedOutput, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256DecoderWithInvalidPasswordLength() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f});
        Coder coder = new AES256SHA256Decoder();
        byte[] password = new byte[]{0x01, 0x02};

        Coders.addDecoder(inputStream, coder, password);
    }

    
}