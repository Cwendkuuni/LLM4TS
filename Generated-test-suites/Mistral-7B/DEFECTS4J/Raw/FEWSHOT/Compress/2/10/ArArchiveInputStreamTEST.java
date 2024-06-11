package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        final byte[] signature = new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a};
        assertEquals(true, ArArchiveInputStream.matches(signature, signature.length));
        assertEquals(false, ArArchiveInputStream.matches(new byte[]{1, 2, 3, 4}, signature.length));
        assertEquals(false, ArArchiveInputStream.matches(signature, 5));
    }

    @Test
    public void testGetNextArEntryWithValidArHeaders() throws IOException {
        final byte[] arHeader = new byte[]{0x21, 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, 0x3e, 0x0a,
                0x63, 0x61, 0x6d, 0x65, 0x64, 0x75, 0x72, 0x65, 0x6e, 0x2d, 0x74, 0x68, 0x65, 0x72, 0x73, 0x65, 0x74, 0x3e,
                0x0a, 0x64, 0x65, 0x66, 0x61, 0x75, 0x61, 0x72, 0x65, 0x73, 0x74, 0x2d, 0x69, 0x73, 0x2d, 0x72, 0x65, 0x6d, 0x65, 0x74, 0x68, 0x65, 0x72, 0x65, 0x2d, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x5f, 0x63, 0x68, 0x61, 0x6d, 0x65, 0x73, 0x73, 0x41, 0x75, 0x74, 0x5f, 0x63, 0x6f, 0x6e, 0x66, 0x66, 0x66, 0x3e, 0x0a, 0x63, 0x61, 0x72, 0x65, 0x6e, 0x74, 0x61, 0x74, 0x61, 0x2d, 0x67, 0x65, 0x6e, 0x65, 0x74, 0x3e, 0x0a,
                0x3c, 0x61, 0x72, 0x65, 0x6e, 0x3e, 0x0a, 0x74, 0x65, 0x73, 0x74, 0x31, 0x31, 0x32, 0x2d, 0x31, 0x2e, 0x63, 0x6f, 0x6d, 0x0a,
                0x61, 0x75, 0x74, 0x65, 0x2d, 0x63, 0x61, 0x6d, 0x65, 0x64, 0x73, 0x3e, 0x0a};

        InputStream input = new ByteArrayInputStream(arHeader);
        ArArchiveInputStream testee = new ArArchiveInputStream(input);

        ArArchiveEntry actual = testee.getNextArEntry();
        assertNotNull(actual);
        assertEquals("compressed/test-file-1.txt", actual.getName());
        assertEquals(1112, actual.getSize());

        actual = testee.getNextArEntry();
        assertEquals(null, actual);
    }

    @Test
    public void testGetNextArEntryWithInvalidArHeader() throws IOException {
        final byte[] arHeader = new byte[]{0x21, 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, 0x3e, 0x0a};

        InputStream input = new ByteArrayInputStream(arHeader);
        ArArchiveInputStream testee = new ArArchiveInputStream(input);

        ArArchiveEntry actual = testee.getNextArEntry();
        assertNotNull(actual);
        assertEquals("", actual.getName());
        assertEquals(-1, actual.getSize());

        actual = testee.getNextArEntry();
        assertNull(actual);
    }

    @Test
    public void testGetNextArEntryWithCorruptedArHeader() throws IOException {
        final byte[] arHeader = new byte[]{0x21, 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, 0x68, 0x3e, 0x0a};

        InputStream input = new ByteArrayInputStream(arHeader);
        ArArchiveInputStream testee = new ArArchiveInputStream(input);

        ArArchiveEntry actual = testee.getNextArEntry();
        assertNull(actual);
    }
}