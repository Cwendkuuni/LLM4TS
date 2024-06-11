package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] testData = {0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0a, 0x12, 0x34, 0x56, 0x2d, 0x75, 0x6e, 0x63, 0x68, 0x6f, 0x72, 0x65, 0x2d, 0x61, 0x73, 0x74, 0x58, 0x31, 0x2d, 0x31, 0x2d, 0x39, 0x3e, 0x00, 0x00, 0x00, 0x07, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x2d, 0x62, 0x61, 0x73, 0x65, 0x2d, 0x73, 0x74, 0x72, 0x65, 0x2d, 0x75, 0x73, 0x68, 0x6f, 0x72, 0x65, 0x2d, 0x63, 0x6f, 0x6d, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x0a, 0x3e, 0x00, 0x00, 0x00, 0x07};

        InputStream input = new ByteArrayInputStream(header);
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        ArchiveEntry entry = test.getNextEntry();
        assertEquals("ar: entry name", "name.ext", entry.getName());
        assertEquals("ar: entry size", 1234L, entry.getSize());

        test.close();
        input = new ByteArrayInputStream(new byte[1]);
        test = new ArArchiveInputStream(input);
        entry = test.getNextEntry();
        assertNull("ar: null entry after EOF", entry);

        test.close();
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = {0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        assertTrue("ar: signature matches", ArArchiveInputStream.matches(signature, signature.length));
        assertFalse("ar: signature does not match", ArArchiveInputStream.matches(new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x01}, signature.length));
    }
}