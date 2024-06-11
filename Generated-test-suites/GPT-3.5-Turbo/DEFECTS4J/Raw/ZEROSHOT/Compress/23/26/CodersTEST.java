import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class CodersTest {
    
    @Test
    public void testAddDecoder() {
        try {
            InputStream is = new FileInputStream("test.txt");
            Coder coder = new Coder();
            byte[] password = "password".getBytes();
            Coders.addDecoder(is, coder, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testAddEncoder() {
        try {
            OutputStream out = new FileOutputStream("test.txt");
            SevenZMethod method = new SevenZMethod();
            byte[] password = "password".getBytes();
            Coders.addEncoder(out, method, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testCopyDecoder() {
        Coders.CoderBase copyDecoder = new Coders.CopyDecoder();
        InputStream in = new FileInputStream("test.txt");
        Coder coder = new Coder();
        byte[] password = "password".getBytes();
        try {
            copyDecoder.decode(in, coder, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLZMADecoder() {
        Coders.CoderBase lzmDecoder = new Coders.LZMADecoder();
        InputStream in = new FileInputStream("test.txt");
        Coder coder = new Coder();
        byte[] password = "password".getBytes();
        try {
            lzmDecoder.decode(in, coder, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testDeflateDecoder() {
        Coders.CoderBase deflateDecoder = new Coders.DeflateDecoder();
        InputStream in = new FileInputStream("test.txt");
        Coder coder = new Coder();
        byte[] password = "password".getBytes();
        try {
            deflateDecoder.decode(in, coder, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testBZIP2Decoder() {
        Coders.CoderBase bzip2Decoder = new Coders.BZIP2Decoder();
        InputStream in = new FileInputStream("test.txt");
        Coder coder = new Coder();
        byte[] password = "password".getBytes();
        try {
            bzip2Decoder.decode(in, coder, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testAES256SHA256Decoder() {
        Coders.CoderBase aesDecoder = new Coders.AES256SHA256Decoder();
        InputStream in = new FileInputStream("test.txt");
        Coder coder = new Coder();
        byte[] password = "password".getBytes();
        try {
            aesDecoder.decode(in, coder, password);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testDummyByteAddingInputStream() {
        DummyByteAddingInputStream stream = new DummyByteAddingInputStream(new FileInputStream("test.txt"));
        try {
            stream.read();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}