import org.junit.Test;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {
    
    @Test
    public void testAddDecoder_CopyDecoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        InputStream is = new ByteArrayInputStream(input);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        byte[] password = null;
        
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decoded = new byte[input.length];
        decodedStream.read(decoded);
        
        assertArrayEquals(input, decoded);
    }
    
    @Test
    public void testAddDecoder_LZMADecoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        InputStream is = new ByteArrayInputStream(input);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        coder.properties = new byte[] { 1, 2, 3, 4, 5 };
        byte[] password = null;
        
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decoded = new byte[input.length];
        decodedStream.read(decoded);
        
        assertArrayEquals(input, decoded);
    }
    
    @Test
    public void testAddDecoder_DeflateDecoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        InputStream is = new ByteArrayInputStream(input);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
        byte[] password = null;
        
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decoded = new byte[input.length];
        decodedStream.read(decoded);
        
        assertArrayEquals(input, decoded);
    }
    
    @Test
    public void testAddDecoder_BZIP2Decoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        InputStream is = new ByteArrayInputStream(input);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.BZIP2.getId();
        byte[] password = null;
        
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decoded = new byte[input.length];
        decodedStream.read(decoded);
        
        assertArrayEquals(input, decoded);
    }
    
    @Test
    public void testAddDecoder_AES256SHA256Decoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        InputStream is = new ByteArrayInputStream(input);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        coder.properties = new byte[] { 1, 2, 3, 4, 5 };
        byte[] password = new byte[] { 1, 2, 3, 4 };
        
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decoded = new byte[input.length];
        decodedStream.read(decoded);
        
        assertArrayEquals(input, decoded);
    }
    
    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedMethod() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        InputStream is = new ByteArrayInputStream(input);
        Coder coder = new Coder();
        coder.decompressionMethodId = new byte[] { 1, 2, 3 };
        byte[] password = null;
        
        Coders.addDecoder(is, coder, password);
    }
    
    @Test
    public void testAddEncoder_CopyDecoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = null;
        
        OutputStream encodedStream = Coders.addEncoder(os, method, password);
        encodedStream.write(input);
        encodedStream.close();
        
        byte[] encoded = ((ByteArrayOutputStream) os).toByteArray();
        
        assertArrayEquals(input, encoded);
    }
    
    @Test
    public void testAddEncoder_LZMADecoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = null;
        
        OutputStream encodedStream = Coders.addEncoder(os, method, password);
        encodedStream.write(input);
        encodedStream.close();
        
        byte[] encoded = ((ByteArrayOutputStream) os).toByteArray();
        
        assertArrayEquals(input, encoded);
    }
    
    @Test
    public void testAddEncoder_DeflateDecoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = null;
        
        OutputStream encodedStream = Coders.addEncoder(os, method, password);
        encodedStream.write(input);
        encodedStream.close();
        
        byte[] encoded = ((ByteArrayOutputStream) os).toByteArray();
        
        assertArrayEquals(input, encoded);
    }
    
    @Test
    public void testAddEncoder_BZIP2Decoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.BZIP2;
        byte[] password = null;
        
        OutputStream encodedStream = Coders.addEncoder(os, method, password);
        encodedStream.write(input);
        encodedStream.close();
        
        byte[] encoded = ((ByteArrayOutputStream) os).toByteArray();
        
        assertArrayEquals(input, encoded);
    }
    
    @Test
    public void testAddEncoder_AES256SHA256Decoder() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.AES256SHA256;
        byte[] password = new byte[] { 1, 2, 3, 4 };
        
        OutputStream encodedStream = Coders.addEncoder(os, method, password);
        encodedStream.write(input);
        encodedStream.close();
        
        byte[] encoded = ((ByteArrayOutputStream) os).toByteArray();
        
        assertArrayEquals(input, encoded);
    }
    
    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedMethod() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA2;
        byte[] password = null;
        
        Coders.addEncoder(os, method, password);
    }
    
    @Test
    public void testCoderBase_Encode() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        OutputStream os = new ByteArrayOutputStream();
        
        Coders.CoderBase coderBase = Coders.coderTable[0].coder;
        OutputStream encodedStream = coderBase.encode(os, null);
        encodedStream.write(input);
        encodedStream.close();
        
        byte[] encoded = ((ByteArrayOutputStream) os).toByteArray();
        
        assertArrayEquals(input, encoded);
    }
}