package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test case for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    @Test
    public void testArArchiveInputStream() {
        final byte[] inputData = new byte[] {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 
                'A', 'R', 0x01, ' ', 'a', 'r', ' ', 'i', 'n', ' ', '1', '2', '3', ' ',
                '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ', '2', '0', '1', ' ',
                '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ', '3', '0', '1', ' ',
                '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ', '4', '0', '1', ' ',
                (byte) 0xdf, 0x0a, (byte) 0xdf, 0x0a, (byte) 0xdf, 0x0a, 
                'A', 'R', 0x01, ' ', 'b', 'i', 'r', ' ', 't', 'e', 's', 't',
                ' ', '1', '4', ' ', '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ',
                '2', '0', '2', ' ',
                (byte) 0xdf, 0x0a, (byte) 0xdf, 0x0a, (byte) 0xdf, 0x0a, 
                'A', 'R', 0x01, ' ', 'c', 'o', 'n', 't', 'e', 'n', 't',
                ' ', '1', '5', ' ', '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ',
                '3', '0', '2', ' '};

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        try (ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream)) {
            ArchiveEntry entry;
            while ((entry = archiveInputStream.getNextEntry()) != null) {
                assertTrue(entry instanceof ArArchiveEntry);
                ArArchiveEntry arArchiveEntry = (ArArchiveEntry) entry;
                System.out.println("Entry Name: " + arArchiveEntry.getName());
                System.out.println("Entry Size: " + arArchiveEntry.getSize());
            }
            // Test getNextEntry returns null after EOF
            assertNull(archiveInputStream.getNextEntry());
        }
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() {
        final byte[] inputData = new byte[] {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                'A', 'R', 0x01, ' ', 'a', 'r', ' ', 'i', 'n', ' ', '1', '2', '3', ' ',
                '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ', '4', '0', '1', ' ',
                (byte) 0xdf, 0x0a, (byte) 0xdf, 0x0a, 
                'A', 'R', 0x01, ' ', 'b', 'i', 'r', ' ', 't', 'e', 's', 't',
                ' ', '1', '4', ' ', '7', 'z', ' ', 'a', 'r', ' ', 'i', 'n', ' ',
                '2', '0', '2', ' ',
                0x78, 0x0a, 0x78, 0x0a, 0x78, 0x0a};

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        try (ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream)) {
            archiveInputStream.getNextArEntry();
            fail("Expected IOException");
        }
    }
}