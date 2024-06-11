import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private static final String TEST_ENTRY_NAME = "test_entry.txt";
    private static final String TEST_CONTENTS = "Hello, World!";

    private File testFile;
    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
        testFile.delete();
    }

    @Test
    public void testCreateAndWriteEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry(CPIO_TRAILER);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.finish();
        cpioOutputStream.close();

        byte[] expectedTrailerBytes = ArchiveUtils.toByteArray(TEST_FILE_NAME);
        byte[] actualTrailerBytes = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream.reset();
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);

        entry = new CpioArchiveEntry(CPIO_TRAILER);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(expectedTrailerBytes, 0, expectedTrailerBytes.length);
        cpioOutputStream.finish();
        cpioOutputStream.close();

        byte[] actualTrailerBytes2 = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedTrailerBytes, actualTrailerBytes2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        cpioOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonCpioArchiveEntry() throws IOException {
        ArchiveEntry nonCpioArchiveEntry = new ArchiveEntry("test.txt");
        cpioOutputStream.putArchiveEntry(nonCpioArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullStream() throws IOException {
        new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidFormat() throws IOException {
        new CpioArchiveOutputStream(new FileOutputStream(testFile), (short) 0xFF);
    }

    @Test
    public void testWriteEntryAndContents() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry(TEST_ENTRY_NAME);
        entry.setFileSize(TEST_CONTENTS.length());
        entry.setMode(CpioConstants.C_ISREG);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(TEST_CONTENTS.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.finish();
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> entries = cpioToMap(TEST_FILE_NAME);
        CpioArchiveEntry actualEntry = entries.get(TEST_ENTRY_NAME);

        assertNotNull(actualEntry);
        assertEquals(TEST_ENTRY_NAME, actualEntry.getName());
        assertEquals(TEST_CONTENTS.length(), actualEntry.getSize());

        byte[] actualEntryBytes = byteArrayToBytes(actualEntry.getData());
        assertEquals(TEST_CONTENTS, new String(actualEntryBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("entry1.txt");
        entry1.setFileSize(10);
        entry1.setMode(CpioConstants.C_ISREG);

        CpioArchiveEntry entry2 = new CpioArchiveEntry("entry2.txt");
        entry2.setFileSize(20);
        entry2.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry1);
        cpioOutputStream.write("Hello".getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.putArchiveEntry(entry2);
        cpioOutputStream.write("World".getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.finish();
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> entries = cpioToMap(TEST_FILE_NAME);
        CpioArchiveEntry entry1Actual = entries.get("entry1.txt");
        CpioArchiveEntry entry2Actual = entries.get("entry2.txt");

        assertNotNull(entry1Actual);
        assertNotNull(entry2Actual);
        assertEquals(10, entry1Actual.getSize());
        assertEquals(20, entry2Actual.getSize());

        byte[] entry1ActualBytes = byteArrayToBytes(entry1Actual.getData());
        byte[] entry2ActualBytes = byteArrayToBytes(entry2Actual.getData());

        assertEquals("Hello", new String(entry1ActualBytes, StandardCharsets.UTF_8));
        assertEquals("World", new String(entry2ActualBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteEntriesWithSameNameButDifferentFormat() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("entry1.txt");
        entry1.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry1.setFileSize(10);
        entry1.setMode(CpioConstants.C_ISREG);

        CpioArchiveEntry entry2 = new CpioArchiveEntry("entry1.txt");
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        entry2.setFileSize(20);
        entry2.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry1);
        cpioOutputStream.write("Hello".getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.putArchiveEntry(entry2);
        cpioOutputStream.write("World".getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.finish();
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> entries = cpioToMap(TEST_FILE_NAME);
        CpioArchiveEntry entry1Actual = entries.get("entry1.txt");
        CpioArchiveEntry entry2Actual = entries.get("entry1.txt");

        assertNotNull(entry1Actual);
        assertNotNull(entry2Actual);
        assertFalse(entry1Actual.equals(entry2Actual)); // Different objects
        assertEquals(10, entry1Actual.getSize());
        assertEquals(20, entry2Actual.getSize());

        byte[] entry1ActualBytes = byteArrayToBytes(entry1Actual.getData());
        byte[] entry2ActualBytes = byteArrayToBytes(entry2Actual.getData());

        assertEquals("Hello", new String(entry1ActualBytes, StandardCharsets.UTF_8));
        assertEquals("World", new String(entry2ActualBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteEntriesWithSameNameButSameFormat() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("entry1.txt");
        entry1.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry1.setFileSize(10);
        entry1.setMode(CpioConstants.C_ISREG);

        CpioArchiveEntry entry2 = new CpioArchiveEntry("entry1.txt");
        entry2.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry2.setFileSize(20);
        entry2.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry1);
        cpioOutputStream.write("Hello".getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.putArchiveEntry(entry2);
        cpioOutputStream.write("World".getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.finish();
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> entries = cpioToMap(TEST_FILE_NAME);
        CpioArchiveEntry entry1Actual = entries.get("entry1.txt");
        CpioArchiveEntry entry2Actual = entries.get("entry1.txt");

        assertNotNull(entry1Actual);
        assertNotNull(entry2Actual);
        assertFalse(entry1Actual.equals(entry2Actual)); // Different objects
        assertEquals(10, entry1Actual.getSize());
        assertEquals(20, entry2Actual.getSize());

        byte[] entry1ActualBytes = byteArrayToBytes(entry1Actual.getData());
        byte[] entry2ActualBytes = byteArrayToBytes(entry2Actual.getData());

        assertEquals("Hello", new String(entry1ActualBytes, StandardCharsets.UTF_8));
        assertEquals("World", new String(entry2ActualBytes, StandardCharsets.UTF_8));

        CpioArchiveEntry entry1Updated = entries.get("entry1.txt");
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);

        cpioOutputStream.putNextEntry(entry1Updated);
        cpioOutputStream.write("Updated".getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.finish();
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> updatedEntries = cpioToMap(TEST_FILE_NAME);
        CpioArchiveEntry updatedEntryActual = updatedEntries.get("entry1.txt");

        assertEquals("Updated", new String(byteArrayToBytes(updatedEntryActual.getData()), StandardCharsets.UTF_8));
    }

    private byte[] byteArrayToBytes(byte[] byteArray) {
        return byteArray;
    }

    private Map<String, CpioArchiveEntry> cpioToMap(String fileName) throws IOException {
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(new FileInputStream(fileName));
        return ArchiveStreams.toMap(archiveInputStream);
    }
}