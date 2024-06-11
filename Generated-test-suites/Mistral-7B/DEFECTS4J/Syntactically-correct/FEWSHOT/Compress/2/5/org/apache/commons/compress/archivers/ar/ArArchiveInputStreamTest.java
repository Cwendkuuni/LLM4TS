package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        new Random().nextBytes(signature);
        assertFalse("Unexpected match for invalid signature.", ArArchiveInputStream.matches(signature, signature.length));

        signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue("Unexpected failure for valid signature.", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        String inputData = "AR\n" +
                "  1 1.0 Sun Jul 17 15:12:22 2011 foo.txt\n" +
                "07030522120100000001020200000000000400\n" +
                "AR\n" +
                "  1 1.1 Sun Jul 17 15:12:22 2011 bar.txt\n" +
                "07030522120100000002020200000000000500";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry1 = test.getNextEntry();
        assertEquals("foo.txt", entry1.getName());
        assertEquals(1L, entry1.getSize());

        ArchiveEntry entry2 = test.getNextEntry();
        assertEquals("bar.txt", entry2.getName());
        assertEquals(5L, entry2.getSize());

        assertNull(test.getNextEntry());

        test.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        String inputData = "AR\n" +
                "  1 1.0 Sun Jul 17 15:12:22 2011 foo.txt\n" +
                "07030522120100000001020200000000000401"; // trailing 1 instead of 0

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        test.getNextEntry();

        throw new AssertionError("Expected IOException not thrown.");
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidMagicNumber() throws IOException {
        String inputData = "BOGUS\n" +
                "  1 1.0 Sun Jul 17 15:12:22 2011 foo.txt\n" +
                "07030522120100000001020200000000000400";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        test.getNextEntry();

        throw new AssertionError("Expected IOException not thrown.");
    }
}