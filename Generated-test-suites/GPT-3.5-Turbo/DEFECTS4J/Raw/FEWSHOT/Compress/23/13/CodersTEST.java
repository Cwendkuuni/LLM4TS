
package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.*;
import org.junit.Test;

public class CodersTest {
    
    //decode method tests
    
    @Test
    public void testAddDecoder_withLZMAMethod_shouldReturnLZMAInputStream() {
        //Arrange
        InputStream is = new ByteArrayInputStream(new byte[10]);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        
        //Act
        InputStream result = Coders.addDecoder(is, coder, null);
        
        //Assert
        assertTrue(result instanceof LZMAInputStream);
    }
    
    @Test
    public void testAddDecoder_withDeflateMethod_shouldReturnInflaterInputStream() {
        //Arrange
        InputStream is = new ByteArrayInputStream(new byte[10]);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
        
        //Act
        InputStream result = Coders.addDecoder(is, coder, null);
        
        //Assert
        assertTrue(result instanceof InflaterInputStream);
    }
    
    //encode method tests
    
    @Test
    public void testAddEncoder_withDeflateMethod_shouldReturnDeflaterOutputStream() {
        //Arrange
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;
        
        //Act
        OutputStream result = Coders.addEncoder(out, method, null);
        
        //Assert
        assertTrue(result instanceof DeflaterOutputStream);
    }
    
    //encode and decode method tests
    
    @Test
    public void testAddEncoderAndDecoder_withCopyMethod_shouldReturnSameInputStream() {
        //Arrange
        InputStream is = new ByteArrayInputStream(new byte[10]);
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        
        //Act
        OutputStream encodedOutputStream = Coders.addEncoder(out, method, null);
        InputStream result = Coders.addDecoder(is, coder, null);
        
        //Assert
        assertSame(is, result);
        assertSame(out, encodedOutputStream);
    }
    
    @Test
    public void testAddEncoderAndDecoder_withAES256SHA256Method_shouldReturnCipherInputStream() {
        //Arrange
        InputStream is = new ByteArrayInputStream(new byte[10]);
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.AES256SHA256;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        
        //Act
        OutputStream encodedOutputStream = Coders.addEncoder(out, method, new byte[32]);
        InputStream result = Coders.addDecoder(is, coder, new byte[32]);
        
        //Assert
        assertTrue(result instanceof CipherInputStream);
        assertSame(out, encodedOutputStream);
    }
}

