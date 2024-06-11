
import static org.junit.Assert.*;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        // Test input stream with supported compression method
        try {
            InputStream inputStream = new FileInputStream("compressedFile.7z");
            Coder coder = new Coder();
            byte[] password = "password".getBytes();
            InputStream result = addDecoder(inputStream, coder, password);

            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        // Test input stream with unsupported compression method
        try {
            InputStream inputStream = new FileInputStream("compressedFile.7z");
            Coder coder = new Coder();
            byte[] password = "password".getBytes();
            InputStream result = addDecoder(inputStream, coder, password);

            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [methodId]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() {
        // Test output stream with supported compression method
        try {
            OutputStream outputStream = new FileOutputStream("compressedFile.7z");
            SevenZMethod method = SevenZMethod.LZMA;
            byte[] password = "password".getBytes();
            OutputStream result = addEncoder(outputStream, method, password);

            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        // Test output stream with unsupported compression method
        try {
            OutputStream outputStream = new FileOutputStream("compressedFile.7z");
            SevenZMethod method = SevenZMethod.BZIP2;
            byte[] password = "password".getBytes();
            OutputStream result = addEncoder(outputStream, method, password);

            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method BZIP2", e.getMessage());
        }
    }

}
