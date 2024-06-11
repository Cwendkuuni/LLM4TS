import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CodersTest {

    private Coders.CoderId[] coderTable;
    private byte[] password;

    @Before
    public void setUp() {
        coderTable = new Coders.CoderId[] {
                new Coders.CoderId(SevenZMethod.COPY, new Coders.CopyDecoder()),
                new Coders.CoderId(SevenZMethod.LZMA, new Coders.LZMADecoder()),
                new Coders.CoderId(SevenZMethod.LZMA2, new Coders.LZMA2Decoder()),
                new Coders.CoderId(SevenZMethod.DEFLATE, new Coders.DeflateDecoder()),
                new Coders.CoderId(SevenZMethod.BZIP2, new Coders.BZIP2Decoder()),
                new Coders.CoderId(SevenZMethod.AES256SHA256, new Coders.AES256SHA256Decoder())
        };

        password = "testPassword".getBytes();
    }

    @After
    public void tearDown() {
        coderTable = null;
        password = null;
    }

    @Test
    public void testAddDecoder_copyMethod_decodesInputStream() throws IOException {
        Coders.Coder coder = new Coders.Coder(SevenZMethod.COPY.getId());
        byte[] data = "Test Data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        InputStream decodedInputStream = Coders.addDecoder(inputStream, coder, password);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int value;
        while ((value = decodedInputStream.read()) != -1) {
            outputStream.write(value);
        }
        byte[] decodedData = outputStream.toByteArray();

        assertArrayEquals(data, decodedData);
    }

    // Add test cases for other addDecoder methods

    @Test
    public void testAddEncoder_copyMethod_encodesOutputStream() throws IOException {
        Coders.Coder coder = new Coders.Coder(SevenZMethod.COPY.getId());
        byte[] data = "Test Data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        OutputStream encodedOutputStream = Coders.addEncoder(outputStream, coder.method, password);

        int value;
        while ((value = inputStream.read()) != -1) {
            encodedOutputStream.write(value);
        }
        encodedOutputStream.close();
        byte[] encodedData = outputStream.toByteArray();

        assertArrayEquals(data, encodedData);
    }

    // Add test cases for other addEncoder methods

    @Test(expected = IOException.class)
    public void testAddDecoder_invalidMethod_throwsIOException() throws IOException {
        Coders.Coder coder = new Coders.Coder("InvalidMethod".getBytes());
        byte[] data = "Test Data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        Coders.addDecoder(inputStream, coder, password);
    }

    // Add test cases for other error scenarios

}