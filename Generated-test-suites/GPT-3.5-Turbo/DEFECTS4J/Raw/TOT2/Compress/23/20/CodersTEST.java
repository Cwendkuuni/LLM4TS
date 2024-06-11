import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {

    @Test
    public void testAddDecoderWithSupportedMethod() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        byte[] password = "password".getBytes();
        InputStream is = new ByteArrayInputStream(new byte[0]);
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderWithUnsupportedMethod() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        byte[] password = "password".getBytes();
        InputStream is = new ByteArrayInputStream(new byte[0]);
        coder.decompressionMethodId = new byte[3]; // Unsupported method
        Coders.addDecoder(is, coder, password);
    }

    @Test
    public void testAddEncoderWithSupportedMethod() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = "password".getBytes();
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderWithUnsupportedMethod() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.ED;
        byte[] password = "password".getBytes();
        Coders.addEncoder(out, method, password);
    }

    // Add more test cases for edge cases and exceptions as needed

}