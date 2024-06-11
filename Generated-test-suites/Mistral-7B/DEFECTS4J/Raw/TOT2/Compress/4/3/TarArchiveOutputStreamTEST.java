import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * TarArchiveOutputStreamTest class for testing TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    /**
     * Test putArchiveEntry method with a valid entry.
     */
    @Test
    public void testPutArchiveEntryValidEntry() throws IOException {
        final String entryName = "testEntry.txt";
        final String entryContent = "Test entry content.";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName);

        // Write entry content to the output stream
        tarArchiveOutputStream.write(entryContent.getBytes(StandardCharsets.UTF_8), 0, entryContent.length());

        // Put archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        assertNotNull(archiveEntry.getSize());
        assertEquals(entryContent.length(), archiveEntry.getSize());

        // Close archive entry
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test putArchiveEntry method with long file names.
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        final String entryName = "a/long/file/name.txt";
        final String entryContent = "Test long file name entry content.";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName);

        if (tarArchiveOutputStream.longFileMode != TarArchiveOutputStream.LONGFILE_GNU) {
            throw new RuntimeException("Long file names are not supported with the current longFileMode.");
        }

        // Write entry content to the output stream
        tarArchiveOutputStream.write(entryContent.getBytes(StandardCharsets.UTF_8), 0, entryContent.length());

        // Put archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        assertNotNull(archiveEntry.getSize());
        assertEquals(entryContent.length(), archiveEntry.getSize());

        // Close archive entry
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test putArchiveEntry method with invalid input.
     */
    @Test
    public void testPutArchiveEntryInvalidInput() throws IOException {
        TarArchiveEntry archiveEntry = null;

        // Test when putArchiveEntry is called without a valid entry
        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));

        // Test when putArchiveEntry is called with a null entry name
        archiveEntry = new TarArchiveEntry(null);
        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    /**
     * Test finish method.
     */
    @Test
    public void testFinish() throws IOException {
        assertFalse(tarArchiveOutputStream.isFinished());

        tarArchiveOutputStream.finish();

        assertTrue(tarArchiveOutputStream.isFinished());
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        assertFalse(tarArchiveOutputStream.isClosed());

        tarArchiveOutputStream.close();

        assertTrue(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test write method.
     */
    @Test
    public void testWrite() throws IOException {
        final String testString = "Test write method.";
        final byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(testBytes, 0, testBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] readBytes = new byte[testBytes.length];
        inputStream.read(readBytes);

        assertNotNull(readBytes);
        assertEquals(testString, new String(readBytes, StandardCharsets.UTF_8));
    }

    /**
     * Test closeArchiveEntry method.
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        final String entryName = "testCloseArchiveEntry.txt";
        final String entryContent = "Test close archive entry content.";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName);

        // Write entry content to the output stream
        tarArchiveOutputStream.write(entryContent.getBytes(StandardCharsets.UTF_8), 0, entryContent.length());

        // Put archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Read entry content
        byte[] readBytes = new byte[entryContent.length()];
        tarArchiveOutputStream.read(readBytes, 0, entryContent.length());

        assertNotNull(readBytes);
        assertEquals(entryContent, new String(readBytes, StandardCharsets.UTF_8));

        // Close archive entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Check if the next write operation throws an exception
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write(new byte[1], 0, 1));
    }

    /**
     * Test closeArchiveEntry method with a null entry name.
     */
    @Test
    public void testCloseArchiveEntryNullName() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(null));
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry archiveEntry = new TarArchiveEntry("testCloseArchiveEntry.txt");
        assertThrows(IOException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    /**
     * Test setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        assertNotEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.longFileMode);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.longFileMode);
    }

    /**
     * Test getRecordSize method.
     */
    @Test
    public void testGetRecordSize() throws IOException {
        assertNotNull(tarArchiveOutputStream.getRecordSize());
        assertNotEquals(0, tarArchiveOutputStream.getRecordSize());
    }
}