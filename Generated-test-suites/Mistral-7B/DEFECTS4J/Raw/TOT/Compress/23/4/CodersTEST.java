import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.crypto.*")
public class CodersTest {

    private Coders coders;
    private SevenZMethod method;
    private byte[] password = "password".getBytes(StandardCharsets.UTF_8);
    private byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        coders = new Coders();
        method = SevenZMethod.DEFLATE;
    }

    @Test
    public void testAddDecoder_ValidDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testData);
        InputStream decoderOutput = coders.addDecoder(inputStream, method, password);
        byte[] expectedData = testData;
        byte[] actualData = new byte[inputStream.available()];
        decoderOutput.read(actualData);
        decoderOutput.close();
        inputStream.close();
        assertEquals(expectedData.length, actualData.length);
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testAddDecoder_InvalidDecoder() {
        InputStream inputStream = new ByteArrayInputStream(testData);
        assertThrows(IOException.class, () -> coders.addDecoder(inputStream, SevenZMethod.COPY, password));
    }

    @Test
    public void testAddEncoder_ValidEncoder() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        OutputStream encoderOutput = coders.addEncoder(outputStream, method, password);
        encoderOutput.write(testData);
        encoderOutput.close();
        byte[] expectedData = testData;
        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(expectedData.length, actualData.length);
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testAddEncoder_InvalidEncoder() {
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(IOException.class, () -> coders.addEncoder(outputStream, SevenZMethod.COPY, password));
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException {
        byte[] encryptedData = new byte[1024 * 1024];
        PowerMockito.mockStatic(MessageDigest.class);
        MessageDigest md = PowerMockito.mock(MessageDigest.class);
        PowerMockito.whenNewInstance(MessageDigest.class, "SHA-256").thenReturn(md);
        PowerMockito.when(md.digest(PowerMockito.any())).thenReturn(testData);
        InputStream inputStream = new ByteArrayInputStream(encryptedData);
        InputStream decoderOutput = coders.addDecoder(inputStream, method, password);
        byte[] expectedData = testData;
        byte[] actualData = new byte[inputStream.available()];
        decoderOutput.read(actualData);
        decoderOutput.close();
        inputStream.close();
        assertEquals(expectedData.length, actualData.length);
        assertEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256Decoder_InvalidPassword() throws IOException, GeneralSecurityException {
        byte[] encryptedData = new byte[1024 * 1024];
        InputStream inputStream = new ByteArrayInputStream(encryptedData);
        coders.addDecoder(inputStream, SevenZMethod.AES256SHA256, null);
    }
}