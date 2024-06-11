import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CodersTest {

    @Test
    public void testAddDecoder_COPY() throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = mock(InputStream.class);
        Coder copier = mock(Coder.class);
        CoderId copierId = new CoderId(SevenZMethod.COPY, copier);

        when(copier.decode(inputStream, eq(null), anyByteArray())).thenReturn(inputStream);

        InputStream outputStream = Coders.addDecoder(inputStream, copierId, null);
        assertEquals(inputStream, outputStream);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedCompressionMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder copier = new CopyDecoder();
        CoderId copierId = new CoderId(SevenZMethod.COPY, copier);

        Coders.addDecoder(inputStream, new CoderId(SevenZMethod.BZIP2, new BZIP2Decoder()), null);
        Coders.addDecoder(inputStream, copierId, null);
    }

    @Test
    public void testAddDecoder_LZMA() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x37, 0x32, 0x45, 0x4C, 0x44, 0x21, 0x43, 0x32, 0x03, 0x00, 0x14, 0x00, 0x00, 0x08, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
        Coder lzmaDecoder = new LZMADecoder();
        CoderId lzmaDecoderId = new CoderId(SevenZMethod.LZMA, lzmaDecoder);

        InputStream expectedOutput = new ByteArrayInputStream(new byte[]{0x00, 0x00, 0x00, 0x08, (byte) 0x37, 0x32, 0x45, 0x4C, 0x44, 0x21, 0x43, 0x32, 0x03, 0x00, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        InputStream outputStream = Coders.addDecoder(inputStream, lzmaDecoderId, null);
        byte[] outputBytes = new byte[outputStream.available()];
        outputStream.read(outputBytes);
        outputStream.close();

        assertArrayEquals(expectedOutput.readAllBytes(), outputBytes);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_LZMA_InvalidDictionarySize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x37, 0x32, 0x45, 0x4C, 0x44, 0x21, 0x43, 0x32, 0x03, 0x00, 0x14, 0x00, 0x00, 0x08, 0xFF, 0xFF});
        Coder lzmaDecoder = new LZMADecoder();
        CoderId lzmaDecoderId = new CoderId(SevenZMethod.LZMA, lzmaDecoder);

        Coders.addDecoder(inputStream, lzmaDecoderId, null);
    }

    @Test
    public void testAddDecoder_AES256SHA256() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] password = new byte[16];
        Arrays.fill(password, (byte) 0x88);

        Coder aes256sha256Decoder = new AES256SHA256Decoder();
        CoderId aes256sha256DecoderId = new CoderId(SevenZMethod.AES256SHA256, aes256sha256Decoder);

        byte[] expectedOutput = new byte[1024];
        Arrays.fill(expectedOutput, (byte) 0x99);

        InputStream outputStream = Coders.addDecoder(inputStream, aes256sha256DecoderId, password);
        byte[] outputBytes = new byte[outputStream.available()];
        outputStream.read(outputBytes);
        outputStream.close();

        assertEquals(expectedOutput.length, outputBytes.length);
        assertArrayEquals(expectedOutput, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_AES256SHA256_InvalidPasswordSize() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] password = new byte[15];
        Arrays.fill(password, (byte) 0x88);

        Coder aes256sha256Decoder = new AES256SHA256Decoder();
        CoderId aes256sha256DecoderId = new CoderId(SevenZMethod.AES256SHA256, aes256sha256Decoder);

        Coders.addDecoder(inputStream, aes256sha256DecoderId, password);
    }

    @Test
    public void testAddEncoder_COPY() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Coder copier = mock(Coder.class);
        CoderId copierId = new CoderId(SevenZMethod.COPY, copier);

        when(copier.encode(outputStream, anyByteArray())).thenReturn(outputStream);

        Coders.addEncoder(outputStream, SevenZMethod.COPY, null);
        outputStream.flush();
        outputStream.close();

        byte[] expectedOutput = new byte[0];
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(expectedOutput.length, outputBytes.length);
        assertArrayEquals(expectedOutput, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedCompressionMethod() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod unsupportedMethod = new SevenZMethod("UNSUPPORTED");

        Coders.addEncoder(outputStream, unsupportedMethod, null);
    }

    @Test
    public void testAddEncoder_LZMA() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        byte[] properties = new byte[22];
        Arrays.fill(properties, (byte) 0xAA);

        Coder lzmaEncoder = new LZMADecoder();
        CoderId lzmaEncoderId = new CoderId(SevenZMethod.LZMA, lzmaEncoder);

        when(lzmaEncoder.encode(outputStream, anyByteArray())).thenReturn(outputStream);

        Coders.addEncoder(outputStream, SevenZMethod.LZMA, properties);
        outputStream.flush();
        outputStream.close();

        byte[] expectedOutput = new byte[1024];
        Arrays.fill(expectedOutput, (byte) 0xBB);

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(expectedOutput.length, outputBytes.length);
        assertArrayEquals(expectedOutput, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_LZMA_InvalidPropertiesSize() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        byte[] properties = new byte[21];
        Arrays.fill(properties, (byte) 0xAA);

        Coder lzmaEncoder = new LZMADecoder();
        CoderId lzmaEncoderId = new CoderId(SevenZMethod.LZMA, lzmaEncoder);

        Coders.addEncoder(outputStream, SevenZMethod.LZMA, properties);
    }
}