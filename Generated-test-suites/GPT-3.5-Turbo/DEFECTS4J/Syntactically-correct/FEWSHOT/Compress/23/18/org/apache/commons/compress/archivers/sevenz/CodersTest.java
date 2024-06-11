
package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CodersTest {
    
    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[10]);
        Coder coder = new Coder();
        byte[] password = new byte[10];
        assertEquals(is, Coders.addDecoder(is, coder, password));
    }
    
    @Test(expected = IOException.class)
    public void testAddDecoderThrowsIOException() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[10]);
        Coder coder = new Coder();
        byte[] password = new byte[10];
        Coders.addDecoder(is, coder, password);
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = new byte[10];
        assertEquals(out, Coders.addEncoder(out, method, password));
    }
    
    @Test(expected = IOException.class)
    public void testAddEncoderThrowsIOException() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = new byte[10];
        Coders.addEncoder(out, method, password);
    }
    
}
