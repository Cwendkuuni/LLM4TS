import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {
    
    @Test
    public void testAddDecoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[] {0x01, 0x02};
        
        byte[] inputBytes = {0x00, 0x01, 0x02, 0x03, 0x04};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        byte[] passwordBytes = {0x05, 0x06, 0x07, 0x08};
        
        try {
            InputStream output = Coders.addDecoder(inputStream, coder, passwordBytes);
            
            int inputValue = output.read();
            int expectedValue = 0x00;
            
            assertEquals(expectedValue, inputValue);
            
            output.close();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        Coders.SevenZMethod method = Coders.SevenZMethod.BZIP2;
        
        byte[] passwordBytes = {0x01, 0x02, 0x03, 0x04};
        
        byte[] inputBytes = {0x05, 0x06, 0x07, 0x08, 0x09};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            OutputStream output = Coders.addEncoder(outputStream, method, passwordBytes);
            
            output.write(inputStream.read());
            
            output.close();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}