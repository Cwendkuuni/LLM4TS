package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.sevenz.Coders.*;

public class CodersTest {

    @Test
    public void testAddDecoder() throws Exception {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = "".getBytes();
        try {
            Coders.addDecoder(is, coder, password);
        } catch (IOException expected) {
            assertEquals("Unsupported compression method []", expected.getMessage());
        }
    }

    @Test
    public void testAddEncoder() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = null;
        byte[] password = "".getBytes();
        try {
            Coders.addEncoder(out, method, password);
        } catch (IOException expected) {
            assertEquals("Unsupported compression method null", expected.getMessage());
        }
    }

    @Test
    public void testCoderId() {
        CoderId coderId = new CoderId(null, null);
        assertNull(coderId.method);
        assertNull(coderId.coder);
    }

    @Test
    public void testCoderBaseDecode() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = "".getBytes();
        CoderBaseMock coderBaseMock = new CoderBaseMock();
        InputStream result = coderBaseMock.decode(in, coder, password);
        assertNotNull(result);
    }

    @Test
    public void testCoderBaseEncode() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = "".getBytes();
        CoderBaseMock coderBaseMock = new CoderBaseMock();
        try {
            coderBaseMock.encode(out, password);
        } catch (UnsupportedOperationException expected) {
            assertEquals("method doesn't support writing", expected.getMessage());
        }
    }

    @Test
    public void testCopyDecoder() {
        CoderBase coderBase = new Coders.CopyDecoder();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = "".getBytes();
        try {
            InputStream result = coderBase.decode(in, null, password);
            assertSame(in, result);
            OutputStream result1 = coderBase.encode(out, password);
            assertSame(out, result1);
        } catch (IOException unexpected) {
            fail("should not throw an exception");
        }
    }

    @Test
    public void testLZMADecoder() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = "".getBytes();
        Coders.LZMADecoder lzmADecoder = new Coders.LZMADecoder();
        try {
            InputStream result = lzmADecoder.decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException unexpected) {
            fail("should not throw an exception");
        }
    }

    @Test
    public void testDeflateDecoder() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = "".getBytes();
        Coders.DeflateDecoder deflateDecoder = new Coders.DeflateDecoder();
        try {
            InputStream result = deflateDecoder.decode(in, coder, password);
            assertNotNull(result);
            OutputStream result1 = deflateDecoder.encode(null, password);
            assertNotNull(result1);
        } catch (IOException unexpected) {
            fail("should not throw an exception");
        }
    }

    @Test
    public void testBZIP2Decoder() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] password = "".getBytes();
        Coders.BZIP2Decoder bZIP2Decoder = new Coders.BZIP2Decoder();
        try {
            InputStream result = bZIP2Decoder.decode(in, coder, password);
            assertNotNull(result);
            OutputStream result1 = bZIP2Decoder.encode(null, password);
            assertNotNull(result1);
        } catch (IOException unexpected) {
            fail("should not throw an exception");
        }
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256Decoder() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coder coder = new Coder();
        byte[] passwordBytes = "".getBytes();
        Coders.AES256SHA256Decoder aES256SHA256Decoder = new Coders.AES256SHA256Decoder();
        InputStream result = aES256SHA256Decoder.decode(in, coder, passwordBytes);
    }

    @Test
    public void testDummyByteAddingInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Coders.DummyByteAddingInputStream dummyByteAddingInputStream = new Coders.DummyByteAddingInputStream(in);
        int result = dummyByteAddingInputStream.read();
        assertNotEquals(-1, result);
        byte[] b = new byte[4];
        int result1 = dummyByteAddingInputStream.read(b, 0, 4);
        assertNotEquals(-1, result1);
        dummyByteAddingInputStream.close();
    }
    
    // inner class
    private class Coder {
        public byte[] decompressionMethodId;
        public byte[] properties;
    }
    
    // inner class
    private class OutputStream extends OutputStream {

        public OutputStream() {
        }
    }
    
    // inner class
    private class InputStream extends InputStream {

        public InputStream() {
        }
    }
    
    // inner class
    private class CoderBaseMock extends Coders.CoderBase {

        @Override
        InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
            return in;
        }        
    }
}