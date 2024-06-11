package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_NullInput() {
        InputStream input = null;
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_EmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        String invalidHeader = "abcdef";
        byte[] invalidHeaderBytes = invalidHeader.getBytes();
        InputStream input = new ByteArrayInputStream(invalidHeaderBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_ValidHeader() throws IOException {
        String validHeader = "   !<arch>\n" +
                "   name; size; date; uid; gid; mode;\n" +
                "0001  foo.txt;729;1536229345;1001;1001;644;\n" +
                "   !<trail>\n";
        byte[] validHeaderBytes = validHeader.getBytes();
        InputStream input = new ByteArrayInputStream(validHeaderBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("foo.txt", entry.getName());
        assertEquals(729L, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_EndOfStream() throws IOException {
        String validHeader = "   !<arch>\n" +
                "   name; size; date; uid; gid; mode;\n" +
                "0001  foo.txt;729;1536229345;1001;1001;644;\n" +
                "   !<trail>\n";
        byte[] validHeaderBytes = validHeader.getBytes();
        InputStream input = new ByteArrayInputStream(validHeaderBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNotNull(entry);
        ArArchiveEntry nextEntry = arArchiveInputStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_InvalidLength() {
        byte[] signature = new byte[5];
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_InvalidSignature() {
        byte[] signature = new byte[8];
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x67; // wrong character
        signature[6] = 0x68;
        signature[7] = 0x3e;
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }
}