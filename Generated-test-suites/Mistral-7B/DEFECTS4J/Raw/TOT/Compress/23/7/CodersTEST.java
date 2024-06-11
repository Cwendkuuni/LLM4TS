import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class CodersTest {

    private static final byte[] TEST_DATA = "Test data".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ENCRYPTED_DATA = new byte[1024];
    private static final byte[] PASSWORD = "password".getBytes(StandardCharsets.UTF_8);
    private static final byte[] SALT = "salt".getBytes(StandardCharsets.UTF_8);
    private static final byte[] IV = new byte[16];
    private static final SevenZMethod METHOD = SevenZMethod.AES256SHA256;

    @Before
    public void setUp() throws Exception {
        new SecureRandom().nextBytes(IV);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("AES");
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("AES");
        SecretKey secretKey = keyFactory.generateSecretKey(new PKCS8EncodedKeySpec(keyPair.getPrivateKey().getEncoded()));
        System.arraycopy(secretKey.getEncoded(), 0, ENCRYPTED_DATA, 0, ENCRYPTED_DATA.length);
    }

    @After
    public void cleanUp() throws Exception {
        TestUtils.cleanTestFiles();
    }

    @Test
    public void testAddDecoder_copyDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Coder coder = new CopyDecoder();
        InputStream result = Coders.addDecoder(inputStream, coder, null);
        byte[] decodedData = new byte[TEST_DATA.length];
        int bytesRead = result.read(decodedData, 0, decodedData.length);
        assertEquals(bytesRead, TEST_DATA.length);
        assertArrayEquals(TEST_DATA, decodedData);
    }

    @Test
    public void testAddDecoder_lzmaDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TestUtils.getLzmaCompressedData());
        Coder coder = new LZMADecoder();
        InputStream result = Coders.addDecoder(inputStream, coder, null);
        byte[] decodedData = new byte[TestUtils.getLzmaCompressedData().length];
        int bytesRead = result.read(decodedData, 0, decodedData.length);
        assertEquals(bytesRead, TestUtils.getLzmaCompressedData().length);
        assertArrayEquals(TestUtils.getLzmaCompressedData(), decodedData);
    }

    @Test
    public void testAddDecoder_unsupportedDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Coder coder = new Object() {}.getClass().newInstance();
        assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, null));
    }

    @Test
    public void testAddDecoder_aes256Sha256Decoder() throws GeneralSecurityException, IOException {
        InputStream inputStream = new ByteArrayInputStream(ENCRYPTED_DATA);
        Coder coder = new AES256SHA256Decoder();
        InputStream result = Coders.addDecoder(inputStream, coder, PASSWORD);
        byte[] decodedData = new byte[TEST_DATA.length];
        int bytesRead = result.read(decodedData, 0, decodedData.length);
        assertEquals(bytesRead, TEST_DATA.length);
        assertArrayEquals(TEST_DATA, decodedData);
    }

    @Test
    public void testAddEncoder_copyEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder coder = new CopyDecoder();
        OutputStream result = Coders.addEncoder(outputStream, SevenZMethod.COPY, null);
        byte[] inputData = TEST_DATA;
        result.write(inputData, 0, inputData.length);
        result.flush();
        byte[] encodedData = outputStream.toByteArray();
        assertArrayEquals(inputData, TEST_DATA);
        assertArrayEquals(encodedData, TEST_DATA);
    }

    @Test
    public void testAddEncoder_lzmaEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder coder = new LZMADecoder();
        OutputStream result = Coders.addEncoder(outputStream, SevenZMethod.LZMA, null);
        byte[] inputData = TEST_DATA;
        result.write(inputData, 0, inputData.length);
        result.flush();
        byte[] encodedData = outputStream.toByteArray();
        assertArrayEquals(inputData, TEST_DATA);
        assertArrayEquals(encodedData, TestUtils.getLzmaCompressedData());
    }

    @Test
    public void testAddEncoder_unsupportedEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder coder = new Object() {}.getClass().newInstance();
        assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, SevenZMethod.LZMA, null));
    }

    @Test
    public void testAddEncoder_aes256Sha256Encoder() throws GeneralSecurityException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder coder = new AES256SHA256Decoder();
        OutputStream result = Coders.addEncoder(outputStream, METHOD, null);
        byte[] inputData = TEST_DATA;
        result.write(inputData, 0, inputData.length);
        result.flush();
        byte[] encodedData = outputStream.toByteArray();
        assertArrayEquals(inputData, TEST_DATA);
        assertArrayEquals(encodedData, ENCRYPTED_DATA);
    }

    @Test
    public void testAddEncoder_aes256Sha256Encoder_password() throws GeneralSecurityException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder coder = new AES256SHA256Decoder();
        OutputStream result = Coders.addEncoder(outputStream, METHOD, PASSWORD);
        byte[] inputData = TEST_DATA;
        result.write(inputData, 0, inputData.length);
        result.flush();
        byte[] encodedData = outputStream.toByteArray();
        byte[] decodedData = new byte[encodedData.length];
        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(encodedData), new AES256SHA256Decoder(), PASSWORD);
        int bytesRead = decoder.read(decodedData, 0, decodedData.length);
        assertEquals(bytesRead, TEST_DATA.length);
        assertArrayEquals(TEST_DATA, decodedData);
    }
}