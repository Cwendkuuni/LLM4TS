package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CodersTest {
    
    @Test
    public void testAddDecoder() throws IOException {
        // Test with valid coder
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[] {0x01, 0x00};
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[100]);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            assertNotNull(Coders.addDecoder(is, coder, null));
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
        
        // Test with unsupported coder
        Coders.Coder unsupportedCoder = new Coders.Coder();
        unsupportedCoder.decompressionMethodId = new byte[] {0x00, 0x00};
        try {
            Coders.addDecoder(is, unsupportedCoder, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        // Test with valid method
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            assertNotNull(Coders.addEncoder(os, SevenZMethod.COPY, null));
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
        
        // Test with unsupported method
        try {
            Coders.addEncoder(os, SevenZMethod.BZIP2, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Pass
        }
    }
}