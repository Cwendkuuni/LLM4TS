package org.apache.commons.compress.archivers.sevenz;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {
    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        
        // Test unsupported compression method
        try {
            is = Coders.addDecoder(new TestInputStream(), coder, password);
            assertEquals("", is);
        } catch(IOException e) {
            assertEquals("Unsupported compression method null", e.getMessage());
        }
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = null;
        SevenZMethod method = null;
        byte[] password = null;
        
        // Test unsupported compression method
        try {
            out = Coders.addEncoder(new TestOutputStream(), method, password);
            assertEquals("", out);
        } catch(IOException e) {
            assertEquals("Unsupported compression method null", e.getMessage());
        }
    }
    
    static class TestInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            return -1;
        }
    }
    
    static class TestOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
        
        }
    }
}