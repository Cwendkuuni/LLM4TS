import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CodersTest {

    private static final byte[] PASSWORD = "password".getBytes(StandardCharsets.UTF_8);
    private static final byte[] TEST_DATA = new byte[]{(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF};

    private SevenZMethod method;
    private CoderBase decoder;
    private CoderBase encoder;
    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() throws Exception {
        method = SevenZMethod.DEFLATE;
        decoder = new DeflateDecoder();
        encoder = new DeflateEncoder();
    }

    @After
    public void tearDown() throws Exception {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }

    @Test
    public void testCopyDecoder() throws IOException, GeneralSecurityException {
        input = new ByteArrayInputStream(TEST_DATA);
        OutputStream output = new ByteArrayOutputStream();
        InputStream decoderOutput = Coders.addDecoder(input, decoder, null);
        output = Coders.addEncoder(output, method, null);
        byte[] decoded = new byte[input.available()];
        int len = decoderOutput.read(decoded);
        assertEquals(input.available(), len);
        assertArrayEquals(TEST_DATA, decoded);

        input.reset();
        byte[] encoded = new byte[output.size()];
        System.arraycopy(output.toByteArray(), 0, encoded, 0, encoded.length);
        byte[] decodedFromEncoded = new byte[encoded.length];
        input = new ByteArrayInputStream(encoded);
        decoderOutput = Coders.addDecoder(input, decoder, null);
        len = decoderOutput.read(decodedFromEncoded);
        assertEquals(TEST_DATA.length, len);
        assertArrayEquals(TEST_DATA, decodedFromEncoded);
    }

    @Test
    public void testDeflateDecoder() throws IOException, GeneralSecurityException {
        input = new ByteArrayInputStream(TEST_DATA);
        OutputStream output = new ByteArrayOutputStream();
        InputStream decoderOutput = Coders.addDecoder(input, decoder, null);
        output = Coders.addEncoder(output, method, null);
        byte[] decoded = new byte[input.available()];
        int len = decoderOutput.read(decoded);
        assertEquals(input.available(), len);
        assertArrayEquals(TEST_DATA, decoded);

        input.reset();
        byte[] encoded = new byte[output.size()];
        System.arraycopy(output.toByteArray(), 0, encoded, 0, encoded.length);
        byte[] decodedFromEncoded = new byte[encoded.length];
        input = new ByteArrayInputStream(encoded);
        decoderOutput = Coders.addDecoder(input, decoder, null);
        len = decoderOutput.read(decodedFromEncoded);
        assertEquals(TEST_DATA.length, len);
        assertArrayEquals(TEST_DATA, decodedFromEncoded);

        // Edge case: empty input
        input = new ByteArrayInputStream(new byte[0]);
        decoderOutput = Coders.addDecoder(input, decoder, null);
        len = decoderOutput.read(decodedFromEncoded);
        assertEquals(0, len);
        assertArrayEquals(new byte[0], decodedFromEncoded);

        // Error scenario: wrong decoder
        decoder = new CopyDecoder();
        input = new ByteArrayInputStream(TEST_DATA);
        input = Coders.addDecoder(input, decoder, null);
        decoderOutput = new ByteArrayInputStream(new byte[0]);
        try {
            len = decoderOutput.read(decodedFromEncoded);
        } catch (IOException e) {
            assertEquals("Unsupported compression method DEFLATE", e.getMessage());
        }
    }

    @Test
    public void testDeflateEncoder() throws IOException {
        input = new ByteArrayInputStream(TEST_DATA);
        OutputStream output = new ByteArrayOutputStream();
        InputStream decoderOutput = Coders.addDecoder(input, decoder, null);
        byte[] decoded = new byte[input.available()];
        int len = decoderOutput.read(decoded);
        assertEquals(TEST_DATA.length, len);
        assertArrayEquals(TEST_DATA, decoded);

        output = Coders.addEncoder(output, method, null);
        byte[] encoded = new byte[output.size()];
        System.arraycopy(output.toByteArray(), 0, encoded, 0, encoded.length);
        byte[] decodedFromEncoded = new byte[encoded.length];
        InputStream encoderInput = new ByteArrayInputStream(encoded);
        InputStream decoderInput = new ByteArrayInputStream(TEST_DATA);
        decoderInput = Coders.addDecoder(decoderInput, decoder, null);
        byte[] decodedFromInput = new byte[decoderInput.available()];
        len = decoderInput.read(decodedFromInput);
        assertEquals(TEST_DATA.length, len);
        assertArrayEquals(TEST_DATA, decodedFromInput);

        assertArrayEquals(encoded, decodedFromEncoded);
    }

    @Test
    public void testEncryptionWithPassword() throws Exception {
        byte[] inputData = TEST_DATA;
        byte[] salt = new byte[16];
        byte[] iv = new byte[16];

        // Encrypt data using a password
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, "password".toCharArray());
        PrivateKey key = (PrivateKey) keyStore.getKey("RSA", "password".toCharArray());
        Certificate certificate = keyStore.getCertificate("test");

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(PASSWORD);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] encryptedData = cipher.doFinal(inputData);

        byte[] encryptedDataWithSalt = new byte[digest.length + iv.length + encryptedData.length];
        System.arraycopy(digest, 0, encryptedDataWithSalt, 0, digest.length);
        System.arraycopy(iv, 0, encryptedDataWithSalt, digest.length, iv.length);
        System.arraycopy(encryptedData, 0, encryptedDataWithSalt, digest.length + iv.length, encryptedData.length);

        // Test encryption with the same password
        InputStream input = new ByteArrayInputStream(encryptedDataWithSalt);
        OutputStream output = new ByteArrayOutputStream();
        input = Coders.addDecoder(input, null, null); // No decoding needed, just checking encryption
        output = Coders.addEncoder(output, method, PASSWORD);
        byte[] decryptedData = new byte[input.available()];
        int len = input.read(decryptedData);
        assertEquals(inputData.length, len);
        assertArrayEquals(inputData, decryptedData);
        assertArrayEquals(encryptedData, decryptedData);
    }
}