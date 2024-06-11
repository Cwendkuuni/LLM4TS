package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMADecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.BZIP2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        byte[] password = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        assertNotNull(Coders.addDecoder(is, coder, password));
    }

    @Test
    public void testAddDecoderUnsupported() throws IOException {
        byte[] password = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = new byte[] { 0x01 };
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        try {
            Coders.addDecoder(is, coder, password);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unsupported compression method"));
        }
    }

    @Test
    public void testAddEncoder() throws IOException {
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(Coders.addEncoder(out, method, password));
    }

    @Test
    public void testAddEncoderUnsupported() throws IOException {
        SevenZMethod method = new SevenZMethod();
        byte[] password = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Coders.addEncoder(out, method, password);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unsupported compression method"));
        }
    }

    @Test
    public void testCopyDecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Coder coder = new Coder();
        coder.properties = new byte[1];
        CoderBase decoder = new CopyDecoder();
        assertNotNull(decoder.decode(in, coder, null));
    }

    @Test
    public void testCopyDecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = null;
        CoderBase decoder = new CopyDecoder();
        try {
            decoder.encode(out, password);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("method doesn't support writing"));
        }
    }

    @Test
    public void testLZMADecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[1]);
        CoderCoder = new Coder();
        coder.properties = new byte[5];
        CoderBase decoder = new LZMADecoder();
        assertNotNull(decoder.decode(in, coder, null));
    }

    @Test
    public void testDeflateDecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[1]);
        CoderCoder = new Coder();
        coder.properties = new byte[1];
        CoderBase decoder = new DeflateDecoder();
        assertNotNull(decoder.decode(in, coder, null));
    }

    @Test
    public void testDeflateDecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = null;
        CoderBase decoder = new DeflateDecoder();
        assertNotNull(decoder.encode(out, password));
    }

    @Test
    public void testBZIP2DecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[1]);
        CoderCoder = new Coder();
        coder.properties = new byte[1];
        CoderBase decoder = new BZIP2Decoder();
        assertNotNull(decoder.decode(in, coder, null));
    }

    @Test
    public void testBZIP2DecoderEncode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = null;
        CoderBase decoder = new BZIP2Decoder();
        assertNotNull(decoder.encode(out, password));
    }

    @Test
    public void testAES256SHA256DecoderDecode() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[1]);
        CoderCoder = new Coder();
        coder.properties = new byte[32];
        CoderBase decoder = new AES256SHA256Decoder();
        assertNotNull(decoder.decode(in, coder, null));
    }
}