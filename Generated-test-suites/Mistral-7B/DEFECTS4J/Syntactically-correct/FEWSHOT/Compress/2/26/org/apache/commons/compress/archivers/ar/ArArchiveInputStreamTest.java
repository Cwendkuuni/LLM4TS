package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        final byte[] header = "  AR* Magic: 2.0.12  Type: unknown  Size: 8924  Name: test.txt\r\n".getBytes();
        final byte[] trailer = "\n".getBytes();
        final String entryName = "test.txt";
        final long entryLength = 8924L;
        final InputStream testInputStream = new ByteArrayInputStream(new byte[] {});
        final InputStream input = new ByteArrayInputStream(new byte[] {});
        input.readAllBytes(); // read the first header to simulate EOF

        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        assertEquals(null, entry);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(trailer);

        input.reset();
        input.write(outputStream.toByteArray());

        arArchiveInputStream = new ArArchiveInputStream(input);
        entry = arArchiveInputStream.getNextArEntry();

        assertEquals(entryName, entry.getName());
        assertEquals(entryLength, entry.getSize());

        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testMatches() {
        final byte[] signature = new byte[8];
        signature[0] = 0x3c;
        signature[1] = 0x21;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        final boolean matches = ArArchiveInputStream.matches(signature, signature.length);

        assertEquals(true, matches);
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        final byte[] header = "  AR* Magic: 2.0.12  Type: unknown  Size: 8924  Name: test.txt\r\nInvalid".getBytes();
        final byte[] trailer = "\n".getBytes();

        InputStream input = new ByteArrayInputStream(new byte[] {});
        input.readAllBytes(); // read the first header to simulate EOF

        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.getNextArEntry();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(trailer);

        input.reset();
        input.write(outputStream.toByteArray());

        arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.getNextArEntry();
    }
}