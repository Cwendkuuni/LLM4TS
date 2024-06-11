package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        signature[0] = (byte) 0x21;
        signature[1] = (byte) 0x3c;
        signature[2] = (byte) 0x61;
        signature[3] = (byte) 0x72;
        signature[4] = (byte) 0x63;
        signature[5] = (byte) 0x68;
        signature[6] = (byte) 0x3e;
        signature[7] = (byte) 0x0a;

        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
        assertFalse(ArArchiveInputStream.matches(new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b }, 8));
        assertFalse(ArArchiveInputStream.matches(new byte[8], 8));
        assertFalse(ArArchiveInputStream.matches(new byte[] { 0x21 }, 1));
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        String testData = "   " +
                "07070101010101010101010101010101" +
                "0000011201030405060708090a0b0c" +
                "000000010102030405060708090a0b" +
                "63616d650a223e617263683e" +
                "00000000000000020000000000000001" +
                "00000000070701010101010101010101" +
                "0000021201030405060708090a0b0c" +
                "000000010102030405060708090a0b" +
                "63616d650a223e617263683e" +
                "00000000000000020000000000000002";

        InputStream input = new ByteArrayInputStream(testData.getBytes(StandardCharsets.US_ASCII));
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        ArchiveEntry entry1 = arInputStream.getNextEntry();
        assertEquals("entry1", entry1);

        ArchiveEntry entry2 = arInputStream.getNextEntry();
        assertEquals("entry2", entry2);

        ArchiveEntry entry3 = arInputStream.getNextEntry();
        assertNull(entry3);
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[10];
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        int read = arInputStream.read(data, 0, data.length);
        assertEquals(-1, read);

        output.write(new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a });
        arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));
        read = arInputStream.read(data, 0, data.length);
        assertEquals(8, read);
        assertArrayEquals(new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a }, data);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = output.toByteArrayInputStream();
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        arInputStream.close();
    }

    private void assertEquals(String name, ArchiveEntry expected) {
        assertEquals(name, expected.getName());
    }
}