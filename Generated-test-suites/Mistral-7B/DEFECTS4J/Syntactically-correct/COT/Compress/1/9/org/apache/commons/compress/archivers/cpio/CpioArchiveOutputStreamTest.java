package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.test.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUp() throws Exception {
        testOut = new ByteArrayOutputStream();
        out = new FilterOutputStream(testOut);
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        cpioStream.close();
        out.close();
    }

    @Test
    public void testConstructor() {
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        assertTrue(cpioStream.isInStream());
        assertNull(cpioStream.getCpioEntry());
    }

    @Test
    public void testPutNextEntryWithNullEntry() {
        assertThrows(NullPointerException.class, () -> cpioStream.putNextEntry(null));
    }

    @Test
    public void testPutNextEntryWithDupEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        cpioStream.setFormat((short) (CpioConstants.FORMAT_NEW | 0x01));
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putNextEntry(entry));
    }

    @Test
    public void testWriteHeader() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        byte[] expectedMagic = CpioConstants.MAGIC_NEW;
        byte[] expectedHeader = TestUtils.concatBytes(expectedMagic, entry.getName().getBytes());
        byte[] actualHeader = testOut.toByteArray();
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testWriteData() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        byte[] testData = "test data".getBytes();
        cpioStream.putNextEntry(entry);
        cpioStream.write(testData, 0, testData.length);
        assertEquals(testData.length, cpioStream.getWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        cpioStream.write("test data".getBytes());
        cpioStream.closeArchiveEntry();
        assertEquals(0, cpioStream.getWritten());
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        cpioStream.write("test data".getBytes());
        cpioStream.finish();
        byte[] actualData = testOut.toByteArray();
        byte[] expectedData = TestUtils.concatBytes(
                CpioConstants.MAGIC_NEW,
                entry.getName().getBytes(),
                (byte) 0,
                CpioUtil.long2byteArray(entry.getSize())[0],
                CpioUtil.long2byteArray(entry.getSize())[1],
                CpioConstants.MAGIC_NEW,
                "TRAILER!!!"
                .getBytes(),
                (byte) 0);
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putNextEntry(entry);
        cpioStream.write("test data".getBytes());
        cpioStream.close();
        assertTrue(cpioStream.isClosed());
    }
}