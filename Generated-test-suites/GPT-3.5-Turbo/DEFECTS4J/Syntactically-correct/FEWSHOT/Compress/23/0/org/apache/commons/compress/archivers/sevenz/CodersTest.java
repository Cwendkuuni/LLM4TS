package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.*;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() throws Exception {
        // Test case 1: Valid decoder, should return the input stream
        InputStream is1 = new ByteArrayInputStream(new byte[10]);
        Coder coder = new Coder();
        coder.decompressionMethodId = new byte[]{1, 2, 3};
        byte[] password1 = new byte[16];
        InputStream result1 = Coders.addDecoder(is1, coder, password1);
        assertSame(is1, result1);

        // Test case 2: Unsupported compression method
        boolean exceptionOccured = false;
        try {
            InputStream is2 = new ByteArrayInputStream(new byte[10]);
            Coder coder2 = new Coder();
            coder2.decompressionMethodId = new byte[]{4, 5, 6};
            byte[] password2 = new byte[16];
            Coders.addDecoder(is2, coder2, password2);
        } catch (IOException e) {
            exceptionOccured = true;
            assertEquals("Unsupported compression method [4, 5, 6]", e.getMessage());
        }
        assertTrue(exceptionOccured);
    }

    @Test
    public void testAddEncoder() throws Exception {
        // Test case 1: Encoder found, should return the output stream
        OutputStream out1 = new ByteArrayOutputStream();
        SevenZMethod method1 = SevenZMethod.LZMA;
        byte[] password1 = new byte[16];
        OutputStream result1 = Coders.addEncoder(out1, method1, password1);
        assertSame(out1, result1);

        // Test case 2: Unsupported compression method
        boolean exceptionOccured = false;
        try {
            OutputStream out2 = new ByteArrayOutputStream();
            SevenZMethod method2 = SevenZMethod.PPMD;
            byte[] password2 = new byte[16];
            Coders.addEncoder(out2, method2, password2);
        } catch (IOException e) {
            exceptionOccured = true;
            assertEquals("Unsupported compression method PPMD", e.getMessage());
        }
        assertTrue(exceptionOccured);
    }

}