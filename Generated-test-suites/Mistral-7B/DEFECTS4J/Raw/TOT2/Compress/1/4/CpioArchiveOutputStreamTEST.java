import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.format.CpioFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioFormat.NEW.getId());
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.finish();
            cpioOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        assertTrue(cpioOutputStream.getNextEntryHeaderSize() > 0);
        assertNull(cpioOutputStream.getNextEntry());

        entry = new CpioArchiveEntry();
        entry.setName("testfile2");
        cpioOutputStream.putNextEntry(entry);
        assertNotNull(cpioOutputStream.getNextEntry());
        assertEquals(entry, cpioOutputStream.getNextEntry());
        assertFalse(cpioOutputStream.getNextEntryHeaderSize() > 0);
    }

    @Test
    public void testWriteWithNoEntry() throws IOException {
        assertThrows(IllegalStateException.class, () -> cpioOutputStream.write("Test data".getBytes()));
        assertFalse(cpioOutputStream.hasNextEntry());
    }

    @Test
    public void testWriteWithEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Test data".getBytes());
        assertFalse(cpioOutputStream.hasNextEntry());
        assertEquals("Test data".getBytes(), cpioOutputStream.getBuffer());
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        cpioOutputStream.putNextEntry(entry1);
        cpioOutputStream.write("Test data 1".getBytes());

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        cpioOutputStream.putNextEntry(entry2);
        cpioOutputStream.write("Test data 2".getBytes());

        CpioArchiveEntry entry3 = new CpioArchiveEntry();
        entry3.setName("testfile3");
        cpioOutputStream.putNextEntry(entry3);

        assertFalse(cpioOutputStream.hasNextEntry());

        byte[] expectedData = new byte[]{"Test data 1".getBytes(), "Test data 2".getBytes(), "Test data 3".getBytes()};
        assertEquals(expectedData, cpioOutputStream.getBuffer());
    }

    @Test
    public void testCloseArchiveEntryAfterWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Test data".getBytes());
        cpioOutputStream.closeArchiveEntry();

        assertFalse(cpioOutputStream.hasNextEntry());
        assertNull(cpioOutputStream.getNextEntry());
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        assertFalse(cpioOutputStream.hasNextEntry());
        assertNull(cpioOutputStream.getNextEntry());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveOutputStream newCpioOutputStream = new CpioArchiveOutputStream(inputStream, CpioFormat.NEW.getId());

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        newCpioOutputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        newCpioOutputStream.write(testData);
        newCpioOutputStream.closeArchiveEntry();
        newCpioOutputStream.finish();

        byte[] expectedData = new byte[]{"Test data".getBytes(), (byte) 0};
        assertEquals(expectedData, newCpioOutputStream.getBuffer());
    }

    @Test
    public void testFinishWithCRC() throws IOException {
        cpioOutputStream.setFormat(CpioFormat.NEW_CRC.getId());

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Test data".getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] testData = "Test data".getBytes();
        for (int i = 0; i < testData.length; i++) {
            cpioOutputStream.write(testData[i]);
            cpioOutputStream.crc += testData[i] & 0xFF;
        }

        cpioOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveOutputStream newCpioOutputStream = new CpioArchiveOutputStream(inputStream, CpioFormat.NEW_CRC.getId());

        CpioArchiveEntry newEntry = newCpioOutputStream.getNextEntry();
        assertEquals("testfile", newEntry.getName());
        assertEquals(CpioFormat.NEW_CRC, newEntry.getFormat());
        assertEquals(CpioFormat.NEW_CRC, newCpioOutputStream.getFormat());
        assertEquals(testData.length, newEntry.getSize());
        assertEquals(testData, newCpioOutputStream.getBuffer());
        assertEquals(cpioOutputStream.crc, newEntry.getChksum());
    }
}