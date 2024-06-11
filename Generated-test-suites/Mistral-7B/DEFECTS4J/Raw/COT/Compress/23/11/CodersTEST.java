import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.compress.archivers.sevenz.Coders.*;

public class CodersTest {

    @Test
    public void testAddDecoder_Copy() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder copyCoder = new CopyDecoder();
        byte[] password = new byte[0];
        InputStream expectedOutput = inputStream;

        InputStream actualOutput = addDecoder(inputStream, copyCoder, password);
        Assert.assertSame(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedCompressionMethod() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder unsupportedCoder = new Object() {}.new UnsupportedCoder(); // Create an unsupported Coder
        byte[] password = new byte[0];

        addDecoder(inputStream, unsupportedCoder, password);
    }

    @Test
    public void testAddEncoder_LZMA() {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        byte[] password = new byte[0];

        OutputStream expectedOutput = outputStream;

        OutputStream actualOutput = addEncoder(outputStream, lzmaMethod, password);
        Assert.assertSame(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedCompressionMethod() {
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod unsupportedMethod = new Object() {}.new UnsupportedSevenZMethod(); // Create an unsupported SevenZMethod

        addEncoder(outputStream, unsupportedMethod, new byte[0][]);
    }

    @Test
    public void testCoderTable_LZMADecoder() {
        for (CoderId coderId : coderTable) {
            if (coderId.method.equals(SevenZMethod.LZMA)) {
                LZMADecoder lzmaDecoder = (LZMADecoder) coderId.coder;
                byte[] propsByte = new byte[1];
                long dictSize = 1024L * 1024L * 1024L; // 1GB
                byte[] password = new byte[0];

                InputStream actualOutput = lzmaDecoder.decode(createInputStreamWithData(new byte[0]), this, password);
                LZMAInputStream expectedOutput = new LZMAInputStream(new ByteArrayInputStream(new byte[0]), -1, 0, (int) dictSize);

                byte[] outputByteArray = new byte[1024]; // 1KB buffer
                int bytesRead;

                do {
                    bytesRead = actualOutput.read(outputByteArray, 0, outputByteArray.length);
                    assertEquals(bytesRead, expectedOutput.read(outputByteArray, 0, outputByteArray.length));
                } while (bytesRead > 0);

                actualOutput.close();
                expectedOutput.close();
            }
        }
    }

    @Test
    public void testCoderTable_DeflateDecoder() {
        for (CoderId coderId : coderTable) {
            if (coderId.method.equals(SevenZMethod.DEFLATE)) {
                DeflateDecoder deflateDecoder = (DeflateDecoder) coderId.coder;
                byte[] password = new byte[0];

                InputStream actualOutput = deflateDecoder.decode(createInputStreamWithData(new byte[0]), this, password);
                InflaterInputStream expectedOutput = new InflaterInputStream(new DummyByteAddingInputStream(new ByteArrayInputStream(new byte[0])), new Inflater(true));

                byte[] outputByteArray = new byte[1024]; // 1KB buffer
                int bytesRead;

                do {
                    bytesRead = actualOutput.read(outputByteArray, 0, outputByteArray.length);
                    assertEquals(bytesRead, expectedOutput.read(outputByteArray, 0, outputByteArray.length));
                } while (bytesRead > 0);

                actualOutput.close();
                expectedOutput.close();
            }
        }
    }

    @Test
    public void testCoderTable_BZIP2Decoder() {
        for (CoderId coderId : coderTable) {
            if (coderId.method.equals(SevenZMethod.BZIP2)) {
                BZIP2Decoder bzip2Decoder = (BZIP2Decoder) coderId.coder;
                byte[] password = new byte[0];

                InputStream actualOutput = bzip2Decoder.decode(createInputStreamWithData(new byte[0]), this, password);
                BZip2CompressorInputStream expectedOutput = new BZip2CompressorInputStream(new ByteArrayInputStream(new byte[0]));

                byte[] outputByteArray = new byte[1024]; // 1KB buffer
                int bytesRead;

                do {
                    bytesRead = actualOutput.read(outputByteArray, 0, outputByteArray.length);
                    assertEquals(bytesRead, expectedOutput.read(outputByteArray, 0, outputByteArray.length));
                } while (bytesRead > 0);

                actualOutput.close();
                expectedOutput.close();
            }
        }
    }

    @Test
    public void testCoderTable_AES256SHA256Decoder() {
        for (CoderId coderId : coderTable) {
            if (coderId.method.equals(SevenZMethod.AES256SHA256)) {
                SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
                byte[] passwordBytes = "password".getBytes();
                byte[] salt = new byte[16];
                byte[] iv = new byte[16];

                byte[] expectedEncryptedData = new byte[1024]; // 1KB data for encryption
                byte[] actualEncryptedData;

                OutputStream expectedOutputStream = new ByteArrayOutputStream();
                OutputStream actualOutputStream = new ByteArrayOutputStream();

                OutputStream expectedCipherOutputStream = new CipherOutputStream(expectedOutputStream, new CipherInputStream(new ByteArrayInputStream(expectedEncryptedData), new SecretKeySpec("password".getBytes(), "AES")).getCipher());
                OutputStream actualCipherOutputStream = new CipherOutputStream(actualOutputStream, new CipherInputStream(new ByteArrayInputStream(expectedEncryptedData), new SecretKeySpec("password".getBytes(), "AES")));

                OutputStream actualOutputStreamWithPassword = addEncoder(actualOutputStream, aes256SHA256Method, passwordBytes);
                InputStream expectedInputStreamWithPassword = addDecoder(new ByteArrayInputStream(actualEncryptedData), new CoderId(aes256SHA256Method, new AES256SHA256Decoder()), passwordBytes);

                actualCipherOutputStream.write(expectedEncryptedData);
                actualCipherOutputStream.close();

                InputStream inputStream = expectedInputStreamWithPassword;
                OutputStream outputStream = actualOutputStreamWithPassword;

                byte[] decryptedData = new byte[1024]; // 1KB buffer

                int bytesRead;

                do {
                    bytesRead = inputStream.read(decryptedData, 0, decryptedData.length);
                    assertEquals(bytesRead, outputStream.read(decryptedData, 0, decryptedData.length));
                } while (bytesRead > 0);

                inputStream.close();
                outputStream.close();
            }
        }
    }

    private InputStream createInputStreamWithData(byte[] data) {
        return new ByteArrayInputStream(data);
    }

}