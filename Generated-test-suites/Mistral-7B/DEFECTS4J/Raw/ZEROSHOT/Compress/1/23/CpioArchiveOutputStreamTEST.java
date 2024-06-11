package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private CpioArchiveOutputStream cpioStream;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioStream.close();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNotNull(cpioStream);
    }

    /**
     * Test writing a new entry
     */
    @Test
    public void testWriteEntry() throws IOException {
        String fileName = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(5);

        cpioStream.putNextEntry(entry);
        cpioStream.write(new byte[] { 'T', 'e', 's', 't', ' ', 'F', 'i', 'l',
                'e' });
        cpioStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[] { 0x07, 0x01, 'T', 'e', 's', 't',
                ' ', 'F', 'i', 'l', 'e', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7,
                0, 0, 0, 5, 'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '\0' };

        byte[] header = out.toByteArray();
        assertEquals("Incorrect header written", expectedHeader, header, 0);

        HashMap<String, CpioArchiveEntry> names = cpioStream.getNames();
        CpioArchiveEntry entryFromMap = names.get(fileName);
        assertEquals("Wrong entry", entry, entryFromMap);
    }

    /**
     * Test writing multiple entries
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        String fileName1 = "testfile1";
        String fileName2 = "testfile2";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(fileName1);
        entry1.setSize(5);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(fileName2);
        entry2.setSize(10);

        cpioStream.putNextEntry(entry1);
        cpioStream.write("Test file 1".getBytes());
        cpioStream.closeArchiveEntry();

        cpioStream.putNextEntry(entry2);
        cpioStream.write("Test file 2".getBytes());
        cpioStream.closeArchiveEntry();

        cpioStream.finish();

        byte[] expectedFinalHeader = new byte[] { 0x07, 0x01, 0x01, 0, 0, 0, 5,
                'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '1', 0, 0, 0, 0, 0, 0,
                0, 7, 0, 0, 0, 10, 'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '2',
                '\0' };

        byte[] header = out.toByteArray();
        assertEquals("Incorrect final header written", expectedFinalHeader,
                header, 0);

        HashMap<String, CpioArchiveEntry> names = cpioStream.getNames();
        CpioArchiveEntry entry1FromMap = names.get(fileName1);
        CpioArchiveEntry entry2FromMap = names.get(fileName2);
        assertEquals("Wrong entry 1", entry1, entry1FromMap);
        assertEquals("Wrong entry 2", entry2, entry2FromMap);
    }

    /**
     * Test writing the same entry multiple times
     */
    @Test(expected = IOException.class)
    public void testWriteSameEntryMultipleTimes() throws IOException {
        String fileName = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(5);

        cpioStream.putNextEntry(entry);
        cpioStream.write("Test file".getBytes());
        cpioStream.closeArchiveEntry();

        cpioStream.putNextEntry(entry);
        cpioStream.write("Test file".getBytes());
        cpioStream.closeArchiveEntry();

        cpioStream.close();
    }

    /**
     * Test writing an entry with a negative size
     */
    @Test(expected = IOException.class)
    public void testWriteNegativeSizeEntry() throws IOException {
        String fileName = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(-5);

        cpioStream.putNextEntry(entry);
        cpioStream.write("Test file".getBytes());
        cpioStream.closeArchiveEntry();
    }

    /**
     * Test writing an entry with a size larger than the remaining space
     */
    @Test(expected = IOException.class)
    public void testWriteLargeSizeEntry() throws IOException {
        String fileName = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(11);

        cpioStream.putNextEntry(entry);
        cpioStream.write("Test file".getBytes());
        cpioStream.closeArchiveEntry();

        cpioStream.putNextEntry(new CpioArchiveEntry());
        CpioUtil.writeLong(out, 0, 4, false);// add trailer header
        cpioStream.finish();
    }

}