import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        out = new FileOutputStream(new File("testCpioFile.cpio"));
        cpioOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        cpioOutputStream.close();
        out.close();
        new File("testCpioFile.cpio").delete();
    }

    @Test
    public void testWriteSingleEntryWithStringContents() throws IOException {
        String testString = "This is a test string";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testStringEntry.txt");
        entry.setFileSize(testString.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testString.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = cpioOutputStream.getEntryMap();
        CpioArchiveEntry entryFromMap = entries.get("testStringEntry.txt");
        assertEquals(1, entries.size());
        assertFalse(entryFromMap.isClosed());
        assertEquals(testString.length(), entryFromMap.getSize());
    }

    @Test
    public void testWriteSingleEntryWithFileContents() throws IOException {
        String testFileContents = "This is a test file";
        File testFile = new File("testFile.txt");
        testFile.createNewFile();
        testFile.writeBytes(testFileContents.getBytes(StandardCharsets.UTF_8));
        CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFileEntry.txt");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.writeTo(testFile);
        cpioOutputStream.closeArchiveEntry();
        testFile.delete();

        Map<String, CpioArchiveEntry> entries = cpioOutputStream.getEntryMap();
        CpioArchiveEntry entryFromMap = entries.get("testFileEntry.txt");
        assertEquals(1, entries.size());
        assertFalse(entryFromMap.isClosed());
        assertEquals(testFileContents.length(), entryFromMap.getSize());
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String testString1 = "This is the first test string";
        String testString2 = "This is the second test string";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testString1Entry.txt");
        entry1.setFileSize(testString1.length());
        entry1.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry1);
        cpioOutputStream.write(testString1.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testString2Entry.txt");
        entry2.setFileSize(testString2.length());
        entry2.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry2);
        cpioOutputStream.write(testString2.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = cpioOutputStream.getEntryMap();
        CpioArchiveEntry entry1FromMap = entries.get("testString1Entry.txt");
        CpioArchiveEntry entry2FromMap = entries.get("testString2Entry.txt");
        assertEquals(2, entries.size());
        assertFalse(entry1FromMap.isClosed());
        assertFalse(entry2FromMap.isClosed());
        assertEquals(testString1.length(), entry1FromMap.getSize());
        assertEquals(testString2.length(), entry2FromMap.getSize());
    }

    @Test
    public void testWriteInvalidEntryFormat() throws IOException {
        cpioOutputStream = new CpioArchiveOutputStream(out, (short) 123);
        assertThrows(IllegalArgumentException.class, () -> cpioOutputStream.putArchiveEntry(new CpioArchiveEntry()));
    }

    @Test
    public void testWriteDuplicateEntryName() throws IOException {
        String testString = "This is a test string";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testStringEntry.txt");
        entry.setFileSize(testString.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testString.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testWriteUnmatchedEntryFormat() throws IOException {
        String testString = "This is a test string";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testStringEntry.txt");
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        entry.setFileSize(testString.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(IOException.class, () -> cpioOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testWriteLargeEntry() throws IOException {
        ByteArrayOutputStream testLargeEntry = new ByteArrayOutputStream();
        byte[] largeEntryBytes = new byte[1024 * 1024]; // 1MB
        testLargeEntry.write(largeEntryBytes);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("largeEntry.dat");
        entry.setFileSize(largeEntryBytes.length);
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(largeEntryBytes.toByteArray());
        cpioOutputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = cpioOutputStream.getEntryMap();
        CpioArchiveEntry entryFromMap = entries.get("largeEntry.dat");
        assertEquals(1, entries.size());
        assertFalse(entryFromMap.isClosed());
        assertEquals(largeEntryBytes.length, entryFromMap.getSize());
    }

    @Test
    public void testWriteTrailerEntry() throws IOException {
        cpioOutputStream.finish();

        Map<String, CpioArchiveEntry> entries = cpioOutputStream.getEntryMap();
        CpioArchiveEntry trailerEntry = entries.get(CpioConstants.CPIO_TRAILER);
        assertEquals(1, entries.size());
        assertEquals(0, trailerEntry.getSize());
        assertNull(trailerEntry.getName());
    }
}