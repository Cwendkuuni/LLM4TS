import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "password".getBytes();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x74, (byte) 0x69, (byte) 0x73, (byte) 0x71, (byte) 0x75, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, (byte) 0x66, (byte) 0x6F});

        Coder copierCoder = new Coders.CopyDecoder();
        InputStream decoderStream = Coders.addDecoder(inputStream, copierCoder, password);
        byte[] expected = "Hello World!".getBytes();
        byte[] actual = new byte[expected.length];
        int readBytes = decoderStream.read(actual, 0, expected.length);
        Assert.assertEquals(readBytes, expected.length);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testAddDecoderUnsupportedMethod() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Coder unsupportedCoder = new Object() {};
        try {
            Coders.addDecoder(inputStream, unsupportedCoder, new byte[0]);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method [B@0: unsupported method", e.getMessage());
        }
    }

    @Test
    public void testAddEncoderCopyDecoder() throws IOException {
        byte[] password = "password".getBytes();
        byte[] source = "Hello World!".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        Coder copierCoder = new Coders.CopyDecoder();
        Coders.addEncoder(outputStream, SevenZMethod.COPY, password);
        outputStream.write(source);
        outputStream.flush();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] decoded = new byte[source.length];
        int readBytes = Coders.addDecoder(inputStream, copierCoder, password).read(decoded, 0, decoded.length);
        Assert.assertEquals(readBytes, source.length);
        Assert.assertArrayEquals(source, decoded);
    }

    @Test
    public void testAddEncoderDeflateDecoder() throws IOException {
        byte[] password = "password".getBytes();
        byte[] source = "Hello World!".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        Coder deflateCoder = new Coders.DeflateDecoder();
        Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, password);
        outputStream.write(source);
        outputStream.flush();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] decoded = new byte[source.length + 1]; // ZLIB requires an extra dummy byte
        int readBytes = Coders.addDecoder(inputStream, deflateCoder, password).read(decoded, 0, decoded.length);
        Assert.assertEquals(readBytes, source.length + 1);
        Assert.assertArrayEquals(source, Arrays.copyOfRange(decoded, 0, source.length));
    }

    @Test
    public void testAddEncoderLZMADecoder() throws IOException, NoSuchAlgorithmException {
        byte[] password = "password".getBytes();
        byte[] source = "Hello World!".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        Coder lzmaCoder = new LZMADecoder();
        Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);
        outputStream.write(source.getBytes());
        outputStream.flush();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] decoded = new byte[source.length];
        int readBytes = Coders.addDecoder(inputStream, lzmaCoder, password).read(decoded, 0, decoded.length);
        Assert.assertEquals(readBytes, source.length);
        Assert.assertArrayEquals(source, decoded);
    }

    @Test
    public void testAddEncoderBZIP2Decoder() throws IOException {
        byte[] password = "password".getBytes();
        byte[] source = "Hello World!".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        Coder bzip2Coder = new Coders.BZIP2Decoder();
        Coders.addEncoder(outputStream, SevenZMethod.BZIP2, password);
        outputStream.write(source);
        outputStream.flush();

        Path expectedFilePath = Files.createTempFile("expectedBZIP2", ".bz2");
        Files.write(expectedFilePath, outputStream.toByteArray());
        try (InputStream inputStream = new FileInputStream(expectedFilePath.toFile())) {
            byte[] decoded = new byte[source.length];
            int readBytes = Coders.addDecoder(inputStream, bzip2Coder, password).read(decoded, 0, decoded.length);
            Assert.assertEquals(readBytes, source.length);
            Assert.assertArrayEquals(source, decoded);
        } finally {
            Files.deleteIfExists(expectedFilePath);
        }
    }

    @Test
    public void testAddEncoderAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "password".getBytes();
        byte[] source = "Hello World!".getBytes();
        byte[] iv = new byte[16];
        byte[] salt = new byte[8];
        byte[] aesKeyBytes = new byte[32];

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password);
        messageDigest.update(salt);
        messageDigest.update(iv);
        aesKeyBytes = messageDigest.digest();

        SecretKey secretKey = new SecretKeySpec(aesKeyBytes, "AES");
        byte[] encrypted = new byte[source.length + 1 + iv.length];

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        cipher.doFinal(source);
        System.arraycopy(iv, 0, encrypted, source.length, iv.length);
        System.arraycopy(source, 0, encrypted, source.length + 1, source.length);

        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
        outputStream.write(encrypted);
        outputStream.flush();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] decoded = new byte[source.length];
        int readBytes = Coders.addDecoder(inputStream, new Coders.AES256SHA256Decoder(), password).read(decoded, 0, decoded.length);
        Assert.assertEquals(readBytes, source.length);
        Assert.assertArrayEquals(source, decoded);
    }
}