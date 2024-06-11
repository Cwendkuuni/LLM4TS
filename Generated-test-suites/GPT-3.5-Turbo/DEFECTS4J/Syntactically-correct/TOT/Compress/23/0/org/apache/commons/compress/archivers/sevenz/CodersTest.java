import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class CodersTest {

    @Test
    public void testAddDecoderCopy() throws IOException {
        InputStream is = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        byte[] password = {};
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertNotNull(result);
        assertEquals(is, result);
    }
    
    @Test
    public void testAddDecoderLZMA() throws IOException {
        // Test with properties size < 4
        InputStream is = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        coder.properties = new byte[] {1, 2, 3};
        byte[] password = {};
        
        try {
            InputStream result = Coders.addDecoder(is, coder, password);

            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("Dictionary larger than 4GiB maximum size", e.getMessage());
        }
        
        // Test with properties size == 4
        is = new ByteArrayInputStream("Test".getBytes());
        coder.properties = new byte[] {1, 2, 3, 4};
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof LZMAInputStream);
    }
    
    @Test
    public void testAddDecoderDeflate() throws IOException {
        InputStream is = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        byte[] password = {};
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof InflaterInputStream);
    }
    
    @Test
    public void testAddDecoderBzip2() throws IOException {
        InputStream is = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        byte[] password = {};
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof BZip2CompressorInputStream);
    }
    
    @Test
    public void testAddDecoderAES256SHA256() throws IOException {
        InputStream is = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        coder.properties = new byte[2];
        byte[] password = {};
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof CipherInputStream);
    }
    
    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedMethod() throws IOException {
        InputStream is = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        coder.decompressionMethodId = new byte[] {1, 2, 3};
        byte[] password = {};
        
        Coders.addDecoder(is, coder, password);
    }
    
    @Test
    public void testAddEncoderCopy() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = {};
        
        OutputStream result = Coders.addEncoder(out, method, password);
        
        assertNotNull(result);
        assertEquals(out, result);
    }
    
    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedMethod() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = new SevenZMethod();
        byte[] password = {};
        
        Coders.addEncoder(out, method, password);
    }

    @Test
    public void testCoderIdConstructor() {
        SevenZMethod method = SevenZMethod.COPY;
        CoderBase coder = new Coders.CoderBase();
        
        Coders.CoderId coderId = new Coders.CoderId(method, coder);
        
        assertNotNull(coderId);
        assertEquals(method, coderId.method);
        assertEquals(coder, coderId.coder);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCoderBaseEncode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] password = {};
        
        Coders.CoderBase coder = new Coders.CoderBase();
        
        coder.encode(out, password);
    }
    
    @Test
    public void testCopyDecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        byte[] password = {};
        
        InputStream result = new Coders.CopyDecoder().decode(in, coder, password);
        
        assertNotNull(result);
        assertEquals(in, result);
    }
    
    @Test
    public void testCopyDecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = {};
        
        OutputStream result = new Coders.CopyDecoder().encode(out, password);
        
        assertNotNull(result);
        assertEquals(out, result);
    }
    
    @Test
    public void testLZMADecoderDecode() throws IOException {
        // Test with dictionary size > 4GiB maximum size
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        coder.properties = new byte[] {1, 2, 3, 4, 5};
        byte[] password = {};
        
        try {
            InputStream result = new Coders.LZMADecoder().decode(in, coder, password);
            
            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("Dictionary larger than 4GiB maximum size", e.getMessage());
        }
        
        // Test with dictionary size < 4GiB maximum size
        in = new ByteArrayInputStream("Test".getBytes());
        coder.properties = new byte[] {1, 2, 3, 4};
        
        InputStream result = new Coders.LZMADecoder().decode(in, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof LZMAInputStream);
    }
    
    @Test
    public void testDeflateDecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        byte[] password = {};
        
        InputStream result = new Coders.DeflateDecoder().decode(in, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof InflaterInputStream);
    }
    
    @Test(expected = IOException.class)
    public void testDeflateDecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = {};
        
        new Coders.DeflateDecoder().encode(out, password);
    }
    
    @Test
    public void testBZIP2DecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        byte[] password = {};
        
        InputStream result = new Coders.BZIP2Decoder().decode(in, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof BZip2CompressorInputStream);
    }
    
    @Test
    public void testBZIP2DecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = {};
        
        OutputStream result = new Coders.BZIP2Decoder().encode(out, password);
        
        assertNotNull(result);
        assertTrue(result instanceof BZip2CompressorOutputStream);
    }
    
    @Test
    public void testAES256SHA256DecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Coder coder = new Coder();
        coder.properties = new byte[2];
        byte[] password = {};

        InputStream result = new Coders.AES256SHA256Decoder().decode(in, coder, password);
        
        assertNotNull(result);
        assertTrue(result instanceof CipherInputStream);
    }

}