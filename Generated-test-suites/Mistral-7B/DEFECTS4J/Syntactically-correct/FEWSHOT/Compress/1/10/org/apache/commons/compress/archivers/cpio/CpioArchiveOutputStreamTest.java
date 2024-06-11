package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.util.CpioUtil;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);

        // Test constructor with default format
        CpioArchiveOutputStream defaultFormatCpioArchiveOutputStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, defaultFormatCpioArchiveOutputStream.getFormat());

        // Test write a new entry
        CpioArchiveEntry newEntry = new CpioArchiveEntry();
        newEntry.setName("test1.txt");
        newEntry.setSize(10);
        newEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(newEntry);
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.finish();

        byte[] expectedNewEntryData = new byte[] {
                (byte) 0x07, 'C', 'P', 'I', 'O', 0, 0, // magic number
                1, 0, // version made by
                1, 0, // version needed to extract
                (byte) 0x20, // file type
                (byte) 0x01, // file permissions
                (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, // group id
                (byte) 0x01, // number of links
                1114344969121L, // modification time
                10, // size
                0, 0, // device major
                0, 0, // device minor
                0, 0, // remote device major
                0, 0, // remote device minor
                10, // name length
                1, 0, 0, 10, // name
                0, // checksum
                0 // padding
        };
        byte[] actualNewEntryData = CpioUtil.hexStringToByteArray(""
                + "7c696265636f6e61757465726e6f6d79" // magic number
                + "0101" // version made by
                + "0101" // version needed to extract
                + "20" // file type
                + "01" // file permissions
                + "0000000000" // user id
                + "0000000000" // group id
                + "01" // number of links
                + "737561746572766572656d3434393639313231" // modification time
                + "0a" // size
                + "000000000000" // device major
                + "000000000000" // device minor
                + "000000000000" // remote device major
                + "000000000000" // remote device minor
                + "0a01000a" // name
                + "00" // checksum
                + "00"); // padding

        assertArrayEquals(expectedNewEntryData, cpioArchiveOutputStream.getOutputStream()
                .toByteArray());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                cpioArchiveOutputStream.getOutputStream().toByteArray());
        CpioArchiveEntry actualEntry = new CpioArchiveEntry(inputStream);
        assertEquals("test1.txt", actualEntry.getName());
        assertEquals(10, actualEntry.getSize());
        assertEquals(CpioConstants.FORMAT_NEW, actualEntry.getFormat());

        // Test write a new entry with no name
        CpioArchiveEntry noNameEntry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(noNameEntry);
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.finish();

        byte[] expectedNoNameEntryData = new byte[] {
                (byte) 0x07, 'C', 'P', 'I', 'O', 0, 0, // magic number
                1, 0, // version made by
                1, 0, // version needed to extract
                (byte) 0x20, // file type
                (byte) 0x01, // file permissions
                0, 0, // user id
                0, 0, // group id
                0, // number of links
                1114344969121L, // modification time
                10, // size
                0, 0, // device major
                0, 0, // device minor
                0, 0, // remote device major
                0, 0, // remote device minor
                0, // name length
                0, // padding
                0 // padding
        };

        byte[] actualNoNameEntryData = CpioUtil.hexStringToByteArray(""
                + "7c696265636f6e61757465726e6f6d79" // magic number
                + "0101" // version made by
                + "0101" // version needed to extract
                + "20" // file type
                + "01" // file permissions
                + "0000000000" // user id
                + "0000000000" // group id
                + "00" // number of links
                + "737561746572766572656d3434393639313231" // modification time
                + "0a" // size
                + "000000000000" // device major
                + "000000000000" // device minor
                + "000000000000" // remote device major
                + "000000000000" // remote device minor
                + "00" // name length
                + "00" // name
                + "00" // checksum
                + "00"); // padding

        assertArrayEquals(expectedNoNameEntryData, cpioArchiveOutputStream.getOutputStream()
                .toByteArray());

        // Test write a new entry with a name longer than 65535 characters
        String longName = "a".repeat(65536);
        CpioArchiveEntry longNameEntry = new CpioArchiveEntry();
        longNameEntry.setName(longName);
        longNameEntry.setSize(10);
        longNameEntry.setFormat(CpioConstants.FORMAT_NEW);

        try {
            cpioArchiveOutputStream.putNextEntry(longNameEntry);
            fail("Expected IOException for name longer than 65535 characters");
        } catch (IOException e) {
            // Expected
        }

        // Test write a new entry with a size larger than Int.MAX_VALUE
        byte[] largeData = new byte[Integer.MAX_VALUE + 1];

        try {
            cpioArchiveOutputStream.putNextEntry(newEntry);
            cpioArchiveOutputStream.write(largeData);
            fail("Expected IOException for size larger than Int.MAX_VALUE");
        } catch (IOException e) {
            // Expected
        }

        // Test write multiple entries
        CpioArchiveEntry newEntry2 = new CpioArchiveEntry();
        newEntry2.setName("test2.txt");
        newEntry2.setSize(20);

        cpioArchiveOutputStream.putNextEntry(newEntry2);
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.finish();

        byte[] expectedMultipleEntriesData = new byte[] {
                (byte) 0x07, 'C', 'P', 'I', 'O', 0, 0, // magic number
                1, 0, // version made by
                1, 0, // version needed to extract
                (byte) 0x20, // file type
                (byte) 0x01, // file permissions
                (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, // group id
                (byte) 0x01, // number of links
                1114344969121L, // modification time
                10, // size
                0, 0, // device major
                0, 0, // device minor
                0, 0, // remote device major
                0, 0, // remote device minor
                10, // name length
                1, 0, 0, 10, // name
                0, // checksum
                0 // padding
                // repeat for newEntry2
        };

        byte[] actualMultipleEntriesData = CpioUtil.hexStringToByteArray(""
                + "7c696265636f6e61757465726e6f6d79" // magic number
                + "0101" // version made by
                + "0101" // version needed to extract
                + "20" // file type
                + "01" // file permissions
                + "0000000000" // user id
                + "0000000000" // group id
                + "01" // number of links
                + "737561746572766572656d3434393639313231" // modification time
                + "0a" // size
                + "000000000000" // device major
                + "000000000000" // device minor
                + "000000000000" // remote device major
                + "000000000000" // remote device minor
                + "0a01000a" // name
                + "00" // checksum
                + "00" // padding

                + "7c696265636f6e61757465726e6f6d79" // magic number
                + "0101" // version made by
                + "0101" // version needed to extract
                + "20" // file type
                + "01" // file permissions
                + "0000000000" // user id
                + "0000000000" // group id
                + "01" // number of links
                + "737561746572766572656d3434393639313233" // modification time
                + "14" // size
                + "000000000000" // device major
                + "000000000000" // device minor
                + "000000000000" // remote device major
                + "000000000000" // remote device minor
                + "0a02000a" // name
                + "00" // checksum
                + "00"); // padding

        assertArrayEquals(expectedMultipleEntriesData, cpioArchiveOutputStream.getOutputStream()
                .toByteArray());

        // Test write a new entry with the same name as a previous one
        CpioArchiveEntry sameNameEntry = new CpioArchiveEntry();
        sameNameEntry.setName("test1.txt");
        sameNameEntry.setSize(10);

        try {
            cpioArchiveOutputStream.putNextEntry(sameNameEntry);
            fail("Expected IOException for same name as previous entry");
        } catch (IOException e) {
            // Expected
        }

        // Test closeArchiveEntry
        cpioArchiveOutputStream.closeArchiveEntry();
        assertEquals(null, cpioArchiveOutputStream.getCpioEntry());

        // Test finish
        cpioArchiveOutputStream.finish();
        assertEquals("TRAILER!!!", cpioArchiveOutputStream.getCpioEntry().getName());
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getCpioEntry().getFormat());

        // Test write to an already finished stream
        try {
            cpioArchiveOutputStream.write("Hello World!".getBytes());
            fail("Expected IOException for writing to a finished stream");
        } catch (IOException e) {
            // Expected
        }

        // Test write with no entry open
        try {
            cpioArchiveOutputStream.write("Hello World!".getBytes());
            fail("Expected IOException for writing with no entry open");
        } catch (IOException e) {
            // Expected
        }

        // Test write with no data
        cpioArchiveOutputStream.putNextEntry(newEntry);
        cpioArchiveOutputStream.finish();

        // Test write with a smaller amount of data than the entry size
        byte[] smallerData = new byte[5];
        cpioArchiveOutputStream.putNextEntry(newEntry);
        cpioArchiveOutputStream.write(smallerData);
        cpioArchiveOutputStream.finish();

        // Test writing the same data multiple times
        byte[] sameData = "Hello World!".getBytes();
        cpioArchiveOutputStream.putNextEntry(newEntry);
        cpioArchiveOutputStream.write(sameData);
        cpioArchiveOutputStream.write(sameData);
        cpioArchiveOutputStream.finish();

        // Test write with a larger amount of data than the entry size
        byte[] largerData = new byte[11];
        cpioArchiveOutputStream.putNextEntry(newEntry);
        cpioArchiveOutputStream.write(largerData);
        cpioArchiveOutputStream.finish();

        // Test write with a larger amount of data than the remaining space in the entry
        byte[] largerDataThanEntrySpace = new byte[12];
        try {
            cpioArchiveOutputStream.putNextEntry(newEntry);
            cpioArchiveOutputStream.write(largerDataThanEntrySpace);
            fail("Expected IOException for writing more data than the entry size");
        } catch (IOException e) {
            // Expected
        }

        // Test write with an empty entry
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.finish();

        // Test write with a large empty entry
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.setSize(Integer.MAX_VALUE);
        cpioArchiveOutputStream.finish();

        // Test write with a negative entry size
        try {
            cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
            cpioArchiveOutputStream.setSize(-1);
            fail("Expected IOException for negative entry size");
        } catch (IOException e) {
            // Expected
        }

        // Test close
        cpioArchiveOutputStream.close();
        assertFalse(cpioArchiveOutputStream.isOpen());
    }
}