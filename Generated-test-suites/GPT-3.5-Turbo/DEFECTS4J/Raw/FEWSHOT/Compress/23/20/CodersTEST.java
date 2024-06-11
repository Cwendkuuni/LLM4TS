package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;

public class CodersTest {
    
    @Test
    public void testAddDecoder() throws IOException {
        // test case 1: test decode with COPY method
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        InputStream decodedStream = Coders.addDecoder(inputStream, coder, null);
        int result = decodedStream.read();
        assertEquals(1, result);
        
        // test case 2: test decode with LZMA method
        data = new byte[]{1, 2, 3, 4, 5};
        inputStream = new ByteArrayInputStream(data);
        coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        decodedStream = Coders.addDecoder(inputStream, coder, null);
        result = decodedStream.read();
        assertEquals(1, result);
        
        // test case 3: unsupported compression method
        data = new byte[]{1, 2, 3, 4, 5};
        inputStream = new ByteArrayInputStream(data);
        coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.ZIP.getId();
        try {
            decodedStream = Coders.addDecoder(inputStream, coder, null);
            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [80, 75, 3, 4]", e.getMessage());
        }
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        // test case 1: test encode with COPY method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.COPY;
        OutputStream encodedStream = Coders.addEncoder(outputStream, method, null);
        encodedStream.write(1);
        byte[] result = outputStream.toByteArray();
        assertArrayEquals(new byte[] {1}, result);
        
        // test case 2: test encode with LZMA method
        outputStream = new ByteArrayOutputStream();
        method = SevenZMethod.LZMA;
        encodedStream = Coders.addEncoder(outputStream, method, null);
        encodedStream.write(1);
        result = outputStream.toByteArray();
        assertArrayEquals(new byte[] {1}, result);
        
        // test case 3: unsupported compression method
        outputStream = new ByteArrayOutputStream();
        method = SevenZMethod.ZIP;
        try {
            encodedStream = Coders.addEncoder(outputStream, method, null);
            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method ZIP", e.getMessage());
        }
    }
}