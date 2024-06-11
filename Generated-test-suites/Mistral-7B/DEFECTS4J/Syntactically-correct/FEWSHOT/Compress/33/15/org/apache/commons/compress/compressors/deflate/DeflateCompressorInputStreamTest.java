package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() {
        DeflateCompressorInputStream inputStream = null;

        // null input stream
        try {
            inputStream = new DeflateCompressorInputStream(null, new DeflateParameters());
            assertNull(inputStream);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // empty input stream
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        inputStream = new DeflateCompressorInputStream(in, new DeflateParameters());
        assertEquals(in, inputStream.getInputStream());
        assertFalse(inputStream.isReadAhead());
        assertEquals(0, inputStream.available());
        assertFalse(inputStream.markSupported());

        // test reading
        final byte[] buf = new byte[1024];
        final InputStream testInputStream = new ByteArrayInputStream(new byte[]{'A'});
        inputStream = new DeflateCompressorInputStream(testInputStream, new DeflateParameters());
        assertEquals(1, inputStream.read());
        assertEquals('A', inputStream.read());
        assertNull(inputStream.read());
        assertEquals(0, inputStream.available());
        assertEquals(-1, inputStream.read());

        // test reading byte array
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        inputStream = new DeflateCompressorInputStream(testInputStream, new DeflateParameters());
        int read = -1;
        while ((read = inputStream.read()) != -1) {
            out.write(read);
        }
        final byte[] result = out.toByteArray();
        assertTrue(result.length > 0);
        IOUtils.dumpBytes(System.out, result, 0, result.length);

        // test reading byte array with offset and length
        final int offset = 0;
        final int length = 10;
        inputStream.reset();
        read = -1;
        while ((read = inputStream.read(buf, offset, length)) != -1) {
            assertEquals(read, length);
            offset += length;
        }
        assertEquals(result.length, offset);

        // test skip
        inputStream.reset();
        long skipped = 0;
        while (skipped < result.length) {
            skipped += inputStream.skip(1);
        }
        assertEquals(result.length, skipped);

        // test close
        inputStream.close();

        // test signature
        final byte[] header = new byte[]{'G', 'P', 'G', 'N', 0x18, 0x02, 0};
        final InputStream signatureInputStream = new ByteArrayInputStream(header);
        inputStream = new DeflateCompressorInputStream(signatureInputStream, new DeflateParameters());
        assertTrue(inputStream.isReadAhead());
        assertEquals(header.length, inputStream.available());
        assertTrue(inputStream.markSupported());
        final byte[] signature = new byte[header.length];
        inputStream.read(signature);
        Adler32 adler = new Adler32();
        adler.update(header);
        adler.update(signature);
        long adler32 = adler.getValue();
        final long expectedAdler32 = 0xde7987e5L;
        assertEquals(expectedAdler32, adler32);

        inputStream.close();
    }
}