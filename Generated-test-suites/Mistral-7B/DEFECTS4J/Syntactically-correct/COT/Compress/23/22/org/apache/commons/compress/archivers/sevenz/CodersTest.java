import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SpecifiedPermission;

public class CodersTest {
    @Test
    public void testAddDecoder_ValidMethod() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream encoderOutput = Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, new byte[0]);
        encoderOutput.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Coder decoder = mock(DeflateDecoder.class);
        CoderId expectedCoderId = new CoderId(SevenZMethod.DEFLATE, decoder);

        InputStream actualInputStream = Coders.addDecoder(inputStream, new Coder(decoder.getClass().getName(), decoder), new byte[0]);
        assertArrayEquals(expectedCoderId.coder.getClass().getName(), decoder.getClass().getName());
        assertArrayEquals(expectedCoderId.method, SevenZMethod.DEFLATE);
        assertSame(actualInputStream, inputStream);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedMethod() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coders.addDecoder(inputStream, new Coder("CustomMethod", mock(CoderBase.class)), new byte[0]);
    }

    @Test
    public void testAddEncoder_ValidMethod() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Coder decoder = mock(DeflateDecoder.class);
        CoderId coderId = new CoderId(SevenZMethod.DEFLATE, decoder);

        OutputStream expectedOutputStream = Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, new byte[0]);
        assertSame(expectedOutputStream, outputStream);

        assertThrows(UnsupportedOperationException.class, () -> Coders.addEncoder(inputStream, SevenZMethod.DEFLATE, new byte[0]).encode(inputStream, new byte[0]));

        InputStream decoderInputStream = Coders.addDecoder(inputStream, coderId, new byte[0]);
        ByteArrayOutputStream decoderOutputStream = new ByteArrayOutputStream();
        decoder.decode(decoderInputStream, decoder, new byte[0]);

        OutputStream actualOutputStream = Coders.addEncoder(decoderOutputStream, SevenZMethod.DEFLATE, new byte[0]);
        byte[] expectedByteArray = new byte[1];
        actualOutputStream.write(expectedByteArray);
        actualOutputStream.close();

        decoderOutputStream.seek(0);
        byte[] actualByteArray = new byte[1];
        decoderOutputStream.read(actualByteArray);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedMethod() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.COPY, new byte[0]);
    }

    @Test
    public void testCoderTable() {
        for (CoderId coderId : Coders.coderTable) {
            assertNotNull(coderId);
            assertNotNull(coderId.coder);
        }
    }

    @Test(expected = IOException.class)
    public void testDecodeWithInvalidPassword() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream encoderOutput = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, new byte[0]);
        encoderOutput.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AES256SHA256Decoder decoder = new AES256SHA256Decoder();
        assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, new Coder("AES256SHA256Decoder", decoder), new byte[1]));
    }

    @Test
    public void testDecodeWithValidPassword() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream encoderOutput = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, new byte[0]);
        encoderOutput.write(new byte[]{"password".getBytes()});
        encoderOutput.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AES256SHA256Decoder decoder = new AES256SHA256Decoder();
        CoderId expectedCoderId = new CoderId(SevenZMethod.AES256SHA256, decoder);

        InputStream actualInputStream = Coders.addDecoder(inputStream, new Coder("AES256SHA256Decoder", decoder), "password".getBytes());
        assertArrayEquals(expectedCoderId.coder.getClass().getName(), decoder.getClass().getName());
        assertArrayEquals(expectedCoderId.method, SevenZMethod.AES256SHA256);
        assertSame(actualInputStream, inputStream);
    }
}

class Coder {
    String name;
    CoderBase coder;

    public Coder(String name, CoderBase coder) {
        this.name = name;
        this.coder = coder;
    }
}