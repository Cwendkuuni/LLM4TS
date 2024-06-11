package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        out = new FilterOutputStream(byteArrayOutputStream);
        cpioOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNotNull(cpioOutputStream);
    }

    /**
     * Test putNextEntry method with valid arguments
     */
    @Test
    public void testPutNextEntryValidArgs() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        assertTrue(cpioOutputStream.isInWriteMode());
    }

    /**
     * Test putNextEntry method with null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryNullArgument() {
        cpioOutputStream.putNextEntry(null);
    }

    /**
     * Test putNextEntry method with duplicate entry
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry2);
        cpioOutputStream.putNextEntry(entry1);
    }

    /**
     * Test write method with valid arguments
     */
    @Test
    public void testWriteValidArgs() throws IOException {
        String testData = "test data";
        cpioOutputStream.write(testData.getBytes());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String expectedData = "test data" + "\0";
        byte[] expectedDataBytes = expectedData.getBytes();
        assertArrayEquals(expectedDataBytes, bytes);
    }

    /**
     * Test write method with invalid offset or length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteInvalidArgs() throws IOException {
        String testData = "test data";
        cpioOutputStream.write(testData.getBytes(), -1, 1);
    }

    /**
     * Test write method with empty data
     */
    @Test
    public void testWriteEmptyData() throws IOException {
        cpioOutputStream.write(new byte[0]);
    }

    /**
     * Test finish method
     */
    @Test
    public void testFinish() throws IOException {
        String testData = "test data";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.finish();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        // Verify that the header and data are written correctly
        // Addassertionsbasedontheheaderformatandexpectedoutput
    }

    /**
     * Test close method
     */
    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();
        assertFalse(cpioOutputStream.isInWriteMode());
    }
}