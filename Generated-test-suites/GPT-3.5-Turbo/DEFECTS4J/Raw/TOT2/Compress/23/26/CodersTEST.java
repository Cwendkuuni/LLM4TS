import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;

public class CodersTest {
    
    @Test
    public void testAddDecoder() throws IOException {
        byte[] data = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F};
        InputStream is = new ByteArrayInputStream(data);
        Coders.Coder coder = new Coders.Coder();
        byte[] password = null;
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof InflaterInputStream);
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = null;
        
        OutputStream result = Coders.addEncoder(outputStream, method, password);
        
        assertNotNull(result);
        assertTrue(result instanceof DeflaterOutputStream);
    }

    // Add more test methods for other public methods in the Coders class
    
}