import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.sevenz.Coders;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        // Typical Use Case
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        byte[] password = "password".getBytes();
        try {
            Coders.addDecoder(inputStream, SevenZMethod.LZMA, password);
        } catch (IOException e) {
            fail("Exception thrown for typical use case");
        }

        // Edge Case
        inputStream = new ByteArrayInputStream(new byte[]{1});
        try {
            Coders.addDecoder(inputStream, SevenZMethod.COPY, password);
        } catch (IOException e) {
            assertEquals("Unsupported compression method [0x00]", e.getMessage());
        }

        // Error Scenario
        inputStream = null;
        try {
            Coders.addDecoder(inputStream, SevenZMethod.COPY, password);
            fail("No exception thrown for null input stream");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() {
        // Typical Use Case
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] password = "password".getBytes();
        try {
            Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);
        } catch (IOException e) {
            fail("Exception thrown for typical use case");
        }

        // Edge Case
        outputStream = new ByteArrayOutputStream();
        try {
            Coders.addEncoder(outputStream, SevenZMethod.COPY, password);
        } catch (IOException e) {
            assertEquals("Unsupported compression method COPY", e.getMessage());
        }

        // Error Scenario
        outputStream = null;
        try {
            Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);
            fail("No exception thrown for null output stream");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }
}