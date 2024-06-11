import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private ByteArrayOutputStream byteArrayOutputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-", null);
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        tempFile.delete();
    }

    /**
     * Test putting an archive entry with a valid name.
     */
    @Test
    public void testPutArchiveEntryValidName() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry readEntry = new TarArchiveEntry(new File(tempFile, "testEntry.txt"));
        assertEquals(29L, readEntry.getSize());
        assertNotNull(readEntry.getName());
        assertEquals("testEntry.txt", readEntry.getName());

        byte[] expectedBytes = new byte[]{
                (byte) 0x0, (byte) 'T', (byte) 'a', (byte) 'r', (byte) 0, // Header
                (byte) 'h', (byte) 'e', (byte) 'a', (byte) 'd', (byte) 'e', (byte) 'r',
                (byte) ':', (byte) 't', (byte) 'a', (byte) 'r', (byte) '/',
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 'E', (byte) 'n',
                (byte) 't', (byte) 'r', (byte) 'y', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, // Name
                (byte) 10, (byte) 0, // Mode
                (byte) 6, (byte) 0, // Number of Links
                (byte) 0, (byte) 0, // Uid
                (byte) 0, (byte) 0, // Gid
                (byte) 0, (byte) 0, // Size
                (byte) 0, (byte) 0, // Mtime
                (byte) 0, (byte) 0, // Checksum
                (byte) 0, (byte) 0, // Name Length
                (byte) 0, (byte) 0, // File Name Extension Length
                (byte) 0 // Flags
        };
        byte[] actualBytes = tarOutputStream.getBuffer().getData();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test putting an archive entry with an invalid name.
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryInvalidName() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt".getBytes());
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    /**
     * Test writing data to an archive entry.
     */
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedBytes = "Hello, World!".getBytes();
        byte[] actualBytes = new ByteArrayInputStream(byteArrayOutputStream.toByteArray()).readAllBytes();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test setting long file mode.
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar"));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    /**
     * Test writing long file names to a tar archive.
     */
    @Test
    public void testWriteLongFileName() throws IOException {
        String longFileName = "testEntry.txt/long/filename/with/long/path/components";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry readEntry = new TarArchiveEntry(new File(tempFile, longFileName));
        assertEquals(29L, readEntry.getSize());
        assertNotNull(readEntry.getName());
        assertEquals(longFileName, readEntry.getName());
    }

    /**
     * Test writing a directory entry.
     */
    @Test
    public void testWriteDirectoryEntry() throws IOException {
        TarArchiveEntry directoryEntry = new TarArchiveEntry("testDirectory");
        directoryEntry.setDirectory(true);
        tarOutputStream.putArchiveEntry(directoryEntry);
        tarOutputStream.finish();
    }

    /**
     * Test closing an archive without a closeArchiveEntry() call.
     */
    @Test(expected = IOException.class)
    public void testCloseWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.finish();
    }

    /**
     * Test closing an archive with a closeArchiveEntry() call.
     */
    @Test
    public void testCloseWithCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }
}