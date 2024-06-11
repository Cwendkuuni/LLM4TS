package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test for ArArchiveInputStream class
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = new byte[]{(byte) 0x3c, (byte) 0x21, (byte) 0x72, (byte) 0x61, (byte) 0x68, (byte) 0x63, (byte) 0x0a, (byte) 0x3e,
                (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) ' ', (byte) 'A', (byte) 'R', (byte) ' ', (byte) 'E', (byte) 'n', (byte) 't', (byte) 'r', (byte) 'y',
                (byte) ' ', (byte) '1', (byte) '.', (byte) '2', (byte) '.', (byte) '3', (byte) ' ', (byte) '7', (byte) '8', (byte) '9', (byte) '0', (byte) '1', (byte) '2', (byte) '3',
                (byte) ' ', (byte) '0', (byte) 'x', (byte) '0', (byte) '1', (byte) ' ', (byte) '7', (byte) '8', (byte) '5', (byte) ' ', (byte) '5', (byte) ' ', (byte) '4', (byte) ' ',
                (byte) '1', (byte) '2', (byte) ' ', (byte) '0', (byte) '7', (byte) '5', (byte) ' ', (byte) '1', (byte) '0', (byte) ' ', (byte) '6', (byte) '4', (byte) ' ', (byte) '4', (byte) ' ',
                (byte) '3', (byte) ' ', (byte) '7', (byte) '8', (byte) '6', (byte) ' ', (byte) '1', (byte) '4', (byte) ' ', (byte) '1', (byte) '2', (byte) ' ', (byte) '2', (byte) '0', (byte) ' ',
                (byte) '7', (byte) '8', (byte) '9', (byte) '1', (byte) ' ', (byte) '0', (byte) '1', (byte) ' ', (byte) '1', (byte) '0', (byte) ' ', (byte) '0', (byte) 'x', (byte) '0', (byte) '1',
                (byte) ' ', (byte) '7', (byte) '8', (byte) '5', (byte) ' ', (byte) '5', (byte) ' ', (byte) '4', (byte) ' ', (byte) '3', (byte) ' ', (byte) '7', (byte) '8', (byte) '6', (byte) ' ', (byte) '1',
                (byte) '4', (byte) ' ', (byte) '1', (byte) '2', (byte) ' ', (byte) '3', (byte) '.', (byte) '2', (byte) '.', (byte) '0', (byte) ' ', (byte) '7', (byte) '8', (byte) '9', (byte) '2', (byte) ' ', (byte) '0',
                (byte) '1', (byte) ' ', (byte) '1', (byte) '0', (byte) ' ', (byte) '0', (byte) 'x', (byte) '0', (byte) '1', (byte) ' '};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        inputStream = testInputStream;
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextArEntry();
        Assert.assertNotNull("getNextArEntry should return an entry", entry);
        Assert.assertEquals("Incorrect entry name", "Entry1.2.0", entry.getName());
        entry = archiveInputStream.getNextArEntry();
        Assert.assertNull("getNextArEntry should return null after last entry", entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryFail() throws IOException {
        archiveInputStream.getNextArEntry();
        archiveInputStream.getNextArEntry();
        archiveInputStream.read(); // read past the last entry
        archiveInputStream.getNextArEntry(); // should throw an exception
    }
}