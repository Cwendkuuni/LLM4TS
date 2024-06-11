
import org.junit.*;
import static org.junit.Assert.*;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        // Test basic functionality
        InputStream is = new InputStream();
        Coder coder = new Coder();
        byte[] password = new byte[16];
        try {
            InputStream result = Coders.addDecoder(is, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
        
        // Test unsupported compression method
        Coder unsupportedCoder = new Coder();
        unsupportedCoder.decompressionMethodId = new byte[3];
        try {
            Coders.addDecoder(is, unsupportedCoder, password);
            fail("Should throw an IOException");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [0, 0, 0]", e.getMessage());
        }
    }
    
    @Test
    public void testAddEncoder() {
        // Test basic functionality
        OutputStream out = new OutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = new byte[16];
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
        
        // Test unsupported compression method
        SevenZMethod unsupportedMethod = SevenZMethod.PPMd;
        try {
            Coders.addEncoder(out, unsupportedMethod, password);
            fail("Should throw an IOException");
        } catch (IOException e) {
            assertEquals("Unsupported compression method PPMd", e.getMessage());
        }
    }
}
