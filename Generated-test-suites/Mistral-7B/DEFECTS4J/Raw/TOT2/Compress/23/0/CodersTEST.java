import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class CodersTest {

    @Test
    public void testAddDecoder_Copy() throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});
        Coder copyCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.COPY.getId();
        }};
        byte[] password = new byte[0];
        InputStream testStream = Coders.addDecoder(inputStream, copyCoder, password);
        byte[] result = ByteStreams.toByteArray(testStream);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F}, result);
    }

    @Test
    public void testAddDecoder_LZMA() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});
        Coder lzmaCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.LZMA.getId();
        }};
        byte[] password = new byte[16];
        byte[] expected = new byte[5];
        Arrays.fill(expected, (byte) 0x01);
        InputStream testStream = Coders.addDecoder(inputStream, lzmaCoder, password);
        byte[] result = ByteStreams.toByteArray(testStream);
        Assert.assertArrayEquals(expected, Arrays.copyOfRange(result, 0, 5));
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_Unsupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});
        Coder unsupportedCoder = new Coder() {{
            decompressionMethodId = new byte[]{(byte) 0xfe, 0x01};
        }};
        byte[] password = new byte[0];
        Coders.addDecoder(inputStream, unsupportedCoder, password);
    }

    @Test
    public void testAddEncoder_Copy() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod copyMethod = SevenZMethod.COPY;
        Coder copyCoder = new Coder() {{
            method = copyMethod;
        }};
        byte[] password = new byte[0];
        OutputStream testStream = Coders.addEncoder(outputStream, copyMethod, password);
        testStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});
        testStream.close();
        byte[] result = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F}, result);
    }

    @Test
    public void testAddEncoder_LZMA() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        Coder lzmaCoder = new Coder() {{
            method = lzmaMethod;
        }};
        byte[] password = new byte[16];
        OutputStream testStream = Coders.addEncoder(outputStream, lzmaMethod, password);
        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        testStream.write(input, 0, input.length);
        testStream.close();
        byte[] result = outputStream.toByteArray();
        LZMAInputStream testInputStream = new LZMAInputStream(new ByteArrayInputStream(result));
        byte[] readResult = new byte[input.length];
        testInputStream.read(readResult, 0, readResult.length);
        Assert.assertArrayEquals(input, readResult);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_Unsupported() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod unsupportedMethod = new SevenZMethod((byte) 0xff);
        Coder unsupportedCoder = new Coder() {{
            method = unsupportedMethod;
        }};
        byte[] password = new byte[0];
        Coders.addEncoder(outputStream, unsupportedMethod, password);
    }
}