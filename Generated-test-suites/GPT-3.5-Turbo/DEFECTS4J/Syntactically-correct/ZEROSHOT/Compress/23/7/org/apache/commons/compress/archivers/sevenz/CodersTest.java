import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.sevenz.Coders;

public class CodersTest {
    
    @Before
    public void setUp() {
        // Set up code here
    }
    
    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = null;
        Coders.Coder coder = new Coders.Coder();
        byte[] password = new byte[] { 1, 2, 3, 4 };
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
    }
    
    @Test(expected=IOException.class)
    public void testAddDecoderUnsupportedMethod() throws IOException {
        InputStream is = null;
        Coders.Coder coder = new Coders.Coder();
        byte[] password = new byte[] { 1, 2, 3, 4 };
        Coders.addDecoder(is, coder, password);
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = null;
        Coders.SevenZMethod method = Coders.SevenZMethod.DEFLATE;
        byte[] password = new byte[] { 1, 2, 3, 4 };
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNotNull(result);
    }
    
    @Test(expected=IOException.class)
    public void testAddEncoderUnsupportedMethod() throws IOException {
        OutputStream out = null;
        Coders.SevenZMethod method = Coders.SevenZMethod.XZ;
        byte[] password = new byte[] { 1, 2, 3, 4 };
        Coders.addEncoder(out, method, password);
    }
}