package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SpecifiedException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.tukaani.xz.LZMAInputStreamTest;

import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.CoderId;

@RunWith(Parameterized.class)
public class CodersTest {

    private SevenZMethod method;
    private Coder coder;

    public CodersTest(SevenZMethod method, Coder coder) {
        this.method = method;
        this.coder = coder;
    }

    @Parameterized.Parameters(name = "{0}Decoder and {1}Encoder tests")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {SevenZMethod.COPY, Coders.CopyDecoder.class},
            {SevenZMethod.LZMA, Coders.LZMADecoder.class},
            {SevenZMethod.LZMA2, Coders.LZMA2Decoder.class},
            {SevenZMethod.DEFLATE, Coders.DeflateDecoder.class},
            {SevenZMethod.BZIP2, Coders.BZIP2Decoder.class},
            {SevenZMethod.AES256SHA256, Coders.AES256SHA256Decoder.class}
        });
    }

    @Test
    public void testDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder decoder = coder.getInstance();
        assertEquals("Testing decoder for " + method, decoder.getClass(), coder.getClass().getComponentType());
        InputStream decodedStream = Coders.addDecoder(inputStream, decoder, new byte[0]);
        assertNull("Decoder should not return a non null stream when decoding empty input", decodedStream);

        byte[] password = "password".getBytes();
        byte[] input = "input".getBytes();
        byte[] expectedOutput = new byte[10];
        byte[] actualOutput = new byte[10];

        if (decoder instanceof LZMADecoder) {
            LZMAInputStreamTest lzmaTest = new LZMAInputStreamTest();
            lzmaTest.setNextInput(new ByteArrayInputStream(input));
            lzmaTest.nextInputStream();
            lzmaTest.nextInput(new ByteArrayInputStream(password), true);
            lzmaTest.nextByteArray();
            System.arraycopy(lzmaTest.getNextByteArray(), 0, expectedOutput, 0, 10);
        } else {
            InputStream inputStreamWithPassword = new ByteArrayInputStream(password);
            InputStream inputStreamWithInput = new ByteArrayInputStream(input);
            OutputStream outputStream = new ByteArrayOutputStream();
            Coder encoder = Coders.getCoderForMethod(method);
            OutputStream encodedStream = Coders.addEncoder(outputStream, method, password);
            encoder.encode(encodedStream, password);
            encodedStream.close();
            inputStreamWithInput = new CipherInputStream(inputStreamWithInput, Cipher.getInstance("AES/CBC/PKCS5Padding")
                    .doFinal(password));
            decodedStream = Coders.addDecoder(inputStreamWithInput, decoder, password);
            int bytesRead = 0;
            while (bytesRead < 10) {
                int currentByteRead = decodedStream.read();
                if (currentByteRead != -1) {
                    actualOutput[bytesRead++] = (byte) currentByteRead;
                }
            }
            decodedStream.close();
        }

        assertEquals("Decoded output for " + method, expectedOutput, actualOutput);

        inputStream.close();
        decodedStream.close();
    }

    @Test(expected = IOException.class)
    public void testDecoderWithUnsupportedMethod() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder decoder = Coders.AES256SHA256Decoder.class.newInstance();
        assertEquals("Testing decoder for " + method, decoder.getClass(), Coders.AES256SHA256Decoder.class);
        InputStream decodedStream = Coders.addDecoder(inputStream, decoder, new byte[0]);
        assertEquals("Decoder should throw an IOException with unsupported method", method, SevenZMethod.AES256SHA256);
    }

    @Test
    public void testEncoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "password".getBytes();
        byte[] input = "input".getBytes();
        byte[] expectedOutput = new byte[15];
        byte[] actualOutput = new byte[15];

        OutputStream outputStream = new ByteArrayOutputStream();
        Coder encoder = coder.getInstance();
        assertEquals("Testing encoder for " + method, encoder.getClass(), coder.getClass());
        OutputStream encodedStream = Coders.addEncoder(outputStream, method, password);
        encodedStream.write(input, 0, input.length);
        encodedStream.close();

        if (encoder instanceof LZMAEncoder) {
            LZMAInputStream lzmaTest = new LZMAInputStream(new ByteArrayInputStream(expectedOutput), -1);
            byte[] decodedInput = new byte[input.length];
            int bytesDecoded = 0;
            while (bytesDecoded < input.length) {
                int currentByteDecoded = lzmaTest.read();
                if (currentByteDecoded != -1) {
                    decodedInput[bytesDecoded++] = (byte) currentByteDecoded;
                }
            }
            assertArrayEquals("Encoded and decoded input for " + method, input, decodedInput);
        } else {
            InputStream inputStream = new ByteArrayInputStream(input);
            InputStream inputStreamWithPassword = new ByteArrayInputStream(password);
            InputStream decodedStream = Coders.addDecoder(inputStream, decoder, password);
            int bytesRead = 0;
            while (bytesRead < input.length) {
                int currentByteRead = decodedStream.read();
                if (currentByteRead != -1) {
                    actualOutput[bytesRead++] = (byte) currentByteRead;
                }
            }
            decodedStream.close();

            OutputStream outputStreamWithInput = new ByteArrayOutputStream();
            Coder decoderForMethod = Coders.getCoderForMethod(method);
            OutputStream decoderStream = Coders.addEncoder(outputStreamWithInput, decoderForMethod, password);
            decoderStream.write(actualOutput, 0, actualOutput.length);
            decoderStream.close();

            assertEquals("Encoded and decoded output for " + method, expectedOutput, outputStreamWithInput.toByteArray());

            inputStream.close();
            decodedStream.close();
            outputStreamWithInput.close();
        }

        outputStream.close();
        encodedStream.close();
    }

    @Test(expected = IOException.class)
    public void testEncoderWithUnsupportedMethod() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "password".getBytes();
        byte[] input = "input".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();
        Coder encoder = Coders.BZIP2Decoder.class.newInstance();
        assertEquals("Testing encoder for " + method, encoder.getClass(), Coders.BZIP2Decoder.class);
        OutputStream encodedStream = Coders.addEncoder(outputStream, method, password);
        encodedStream.write(input, 0, input.length);
        encodedStream.close();

        assertEquals("Encoder should throw an IOException with unsupported method", method, SevenZMethod.BZIP2);
    }
}