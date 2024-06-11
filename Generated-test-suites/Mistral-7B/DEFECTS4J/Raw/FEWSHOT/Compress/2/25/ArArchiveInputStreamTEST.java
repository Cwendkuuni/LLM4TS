package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u001fAR\u0007"
                + "Name: test.txt\n"
                + "Size: 11\n"
                + "Mod Time: 01-Jan-2012 12:00:00\n"
                + "UID: 1001\n"
                + "GID: 1001\n"
                + "Mode: 0100644\n"
                + "Magic: 0\n"
                + "Data:\n"
                + "Test content!\u001f"});
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = test.getNextArEntry();
        assertNotNull(entry);
        assertEquals("Name: test.txt", entry.getName());
        assertEquals(11, entry.getSize());

        entry = test.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u001fAR\u0007"
                + "Name: test.txt\n"
                + "Size: 11\n"
                + "Mod Time: 01-Jan-2012 12:00:00\n"
                + "UID: 1001\n"
                + "GID: 1001\n"
                + "Mode: 0100644\n"
                + "Length: 11\n" // incorrect field name
                + "Magic: 0\n"
                + "Data:\n"
                + "Test content!\u001f"});
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);
        test.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryMalformedHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u001fAR\u0007"
                + "Name: test.txt\n"
                + "Size: 11\n"
                + "Mod Time: 01-Jan-2012 12:00:00\n"
                + "UID: 1001\n"
                + "GID: 1001\n"
                + "Mode: 0100644\n"
                + "Magic: 0\nData:\n" // missing length field
                + "Test content!\u001f"});
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);
        test.getNextArEntry();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] signature = new byte[8];
        assertFalse(ArArchiveInputStream.matches(signature, 8));
    }
}