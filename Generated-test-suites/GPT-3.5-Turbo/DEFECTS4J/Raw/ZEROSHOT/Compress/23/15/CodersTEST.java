import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CodersTest {

    private Coders coders;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setup() {
        coders = new Coders();
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[10]);
    }

    @Test
    public void testAddDecoder() {
        try {
            InputStream decodedInput = coders.addDecoder(inputStream, new Coder(), new byte[0]);
            assertNotNull(decodedInput);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddDecoderUnsupportedMethod() {
        try {
            coders.addDecoder(inputStream, new Coder(), new byte[0]);
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [0]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() {
        try {
            OutputStream encodedOutput = coders.addEncoder(outputStream, SevenZMethod.COPY, new byte[0]);
            assertNotNull(encodedOutput);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddEncoderUnsupportedMethod() {
        try {
            coders.addEncoder(outputStream, new SevenZMethod(), new byte[0]);
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method SevenZMethod[null=null]", e.getMessage());
        }
    }

    private class Coder extends org.apache.commons.compress.archivers.sevenz.Coder {
        byte[] decompressionMethodId = new byte[]{0};

        @Override
        public byte[] getDecompressionMethodId() {
            return decompressionMethodId;
        }
    }
}