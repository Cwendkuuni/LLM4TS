
package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.junit.Test;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        // test default constructor
        InputStream is = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zais = new ZipArchiveInputStream(is);
        assertFalse(zais.isClosed());
        assertNull(zais.getNextZipEntry());
        assertNull(zais.getNextEntry());

        // test constructor with encoding and useUnicodeExtraFields
        is = new ByteArrayInputStream(new byte[0]);
        zais = new ZipArchiveInputStream(is, "UTF-8", true);
        assertFalse(zais.isClosed());
        assertNull(zais.getNextZipEntry());
        assertNull(zais.getNextEntry());
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] { 0x50, 0x4B, 0x03, 0x04 };
        assertTrue(ZipArchiveInputStream.matches(signature, 4));

        signature = new byte[] { 0x50, 0x4B, 0x03, 0x03 };
        assertFalse(ZipArchiveInputStream.matches(signature, 4));

        signature = new byte[] { 0x50, 0x4B, 0x03, 0x04, 0x50 };
        assertTrue(ZipArchiveInputStream.matches(signature, 5));

        signature = new byte[] { 0x50, 0x4B, 0x03, 0x04, 0x50, 0x4B };
        assertTrue(ZipArchiveInputStream.matches(signature, 6));
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zais = new ZipArchiveInputStream(is);
        assertFalse(zais.isClosed());
        zais.close();
        assertTrue(zais.isClosed());
        zais.getNextZipEntry(); // should throw IOException
    }

    @Test(expected = EOFException.class)
    public void testReadFully() throws IOException {
        byte[] b = new byte[10];
        InputStream is = new ByteArrayInputStream(b);
        ZipArchiveInputStream zais = new ZipArchiveInputStream(is);
        zais.readFully(b);
        zais.readFully(b); // should throw EOFException
    }

    @Test
    public void testSkip() throws IOException {
        byte[] b = new byte[10];
        InputStream is = new ByteArrayInputStream(b);
        ZipArchiveInputStream zais = new ZipArchiveInputStream(is);
        long skipped = zais.skip(5);
        assertEquals(5, skipped);
    }

    // Add additional test cases for other methods

}
