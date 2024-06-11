import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coders.Coder;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        byte[] password = "password".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        SevenZMethod method = SevenZMethod.COPY;
        
        InputStream decodedStream = Coders.addDecoder(inputStream, coder, password);
        
        assertNotNull(decodedStream);
    }

    @Test
    public void testAddEncoder() throws IOException {
        byte[] password = "password".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        
        OutputStream encodedStream = Coders.addEncoder(outputStream, method, password);
        
        assertNotNull(encodedStream);
    }

    @Test
    public void testLZMADecoder() throws IOException {
        byte[] password = "password".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        
        InputStream decodedStream = Coders.LZMADecoder().decode(inputStream, coder, password);
        
        assertNotNull(decodedStream);
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        byte[] password = "password".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        
        InputStream decodedStream = Coders.DeflateDecoder().decode(inputStream, coder, password);
        
        assertNotNull(decodedStream);
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        byte[] password = "password".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        
        InputStream decodedStream = Coders.BZIP2Decoder().decode(inputStream, coder, password);
        
        assertNotNull(decodedStream);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException {
        byte[] password = "password".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        
        InputStream decodedStream = Coders.AES256SHA256Decoder().decode(inputStream, coder, password);
        
        assertNotNull(decodedStream);
    }
}