import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class CodersTest {

    @Test
    public void testAddDecoderCopy() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertEquals(is, result);
    }
    
    @Test
    public void testAddDecoderLZMA() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertTrue(result instanceof LZMAInputStream);
    }
    
    @Test
    public void testAddDecoderDeflate() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertTrue(result instanceof InflaterInputStream);
    }
    
    @Test
    public void testAddDecoderBZIP2() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        coder.decompressionMethodId = SevenZMethod.BZIP2.getId();
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertTrue(result instanceof BZip2CompressorInputStream);
    }
    
    @Test
    public void testAddDecoderAES256SHA256() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        coder.decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        
        InputStream result = Coders.addDecoder(is, coder, password);
        
        assertTrue(result instanceof InputStream);
    }
    
    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedMethod() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        coder.decompressionMethodId = new byte[] {10, 20, 30};
        
        Coders.addDecoder(is, coder, password);
    }
    
    @Test
    public void testAddEncoderCopy() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = null;
        
        OutputStream result = Coders.addEncoder(out, method, password);
        
        assertEquals(out, result);
    }
    
    @Test
    public void testAddEncoderLZMA() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = null;
        
        OutputStream result = Coders.addEncoder(out, method, password);
        
        assertTrue(result instanceof LZMAOutputStream);
    }
    
    @Test
    public void testAddEncoderDeflate() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = null;
        
        OutputStream result = Coders.addEncoder(out, method, password);
        
        assertTrue(result instanceof DeflaterOutputStream);
    }
    
    @Test
    public void testAddEncoderBZIP2() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.BZIP2;
        byte[] password = null;
        
        OutputStream result = Coders.addEncoder(out, method, password);
        
        assertTrue(result instanceof BZip2CompressorOutputStream);
    }
    
    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedMethod() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = new SevenZMethod();
        byte[] password = null;
        
        Coders.addEncoder(out, method, password);
    }
    
    @Test
    public void testCoderIdConstructor() {
        SevenZMethod method = new SevenZMethod();
        CoderBase coder = new Coders.CoderBase();
        
        Coders.CoderId coderId = new Coders.CoderId(method, coder);
        
        assertEquals(method, coderId.method);
        assertEquals(coder, coderId.coder);
    }
    
    @Test
    public void testCopyDecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        Coders.CopyDecoder decoder = new Coders.CopyDecoder();
        
        InputStream result = decoder.decode(in, coder, password);
        
        assertEquals(in, result);
    }
    
    @Test
    public void testCopyDecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = null;
        Coders.CopyDecoder decoder = new Coders.CopyDecoder();
        
        OutputStream result = decoder.encode(out, password);
        
        assertEquals(out, result);
    }
    
    @Test
    public void testLZMADecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        Coders.LZMADecoder decoder = new Coders.LZMADecoder();
        coder.properties = new byte[] {1, 2, 3, 4, 5};
        
        InputStream result = decoder.decode(in, coder, password);
        
        assertTrue(result instanceof LZMAInputStream);
    }
    
    @Test
    public void testDeflateDecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        Coders.DeflateDecoder decoder = new Coders.DeflateDecoder();
        
        InputStream result = decoder.decode(in, coder, password);
        
        assertTrue(result instanceof InflaterInputStream);
    }
    
    @Test
    public void testDeflateDecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = null;
        Coders.DeflateDecoder decoder = new Coders.DeflateDecoder();
        
        OutputStream result = decoder.encode(out, password);
        
        assertTrue(result instanceof DeflaterOutputStream);
    }
    
    @Test
    public void testBZIP2DecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        Coders.BZIP2Decoder decoder = new Coders.BZIP2Decoder();
        
        InputStream result = decoder.decode(in, coder, password);
        
        assertTrue(result instanceof BZip2CompressorInputStream);
    }
    
    @Test
    public void testBZIP2DecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = null;
        Coders.BZIP2Decoder decoder = new Coders.BZIP2Decoder();
        
        OutputStream result = decoder.encode(out, password);
        
        assertTrue(result instanceof BZip2CompressorOutputStream);
    }
    
    @Test
    public void testAES256SHA256DecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = null;
        Coders.AES256SHA256Decoder decoder = new Coders.AES256SHA256Decoder();
        
        InputStream result = decoder.decode(in, coder, password);
        
        assertTrue(result instanceof InputStream);
    }

    @Test
    public void testDummyByteAddingInputStreamRead() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coders.DummyByteAddingInputStream inputStream = new Coders.DummyByteAddingInputStream(in);
        
        int result = inputStream.read();
        
        assertEquals(116, result);
    }
    
    @Test
    public void testDummyByteAddingInputStreamReadArray() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coders.DummyByteAddingInputStream inputStream = new Coders.DummyByteAddingInputStream(in);
        byte[] b = new byte[4];
        
        int result = inputStream.read(b, 0, 4);
        
        assertEquals(4, result);
    }
    
    @Test
    public void testCodersClass() {
        assertNotNull(new Coders());
    }

}