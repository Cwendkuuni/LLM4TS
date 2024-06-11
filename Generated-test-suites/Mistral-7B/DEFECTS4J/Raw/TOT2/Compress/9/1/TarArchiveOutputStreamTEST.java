import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * TarArchiveOutputStreamTest class
 */
public class TarArchiveOutputStreamTest {

    private File temporaryFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    /**
     * Set up method for the TarArchiveOutputStreamTest class
     *
     * @throws Exception if an error occurs during setup
     */
    @Before
    public void setUp() throws Exception {
        temporaryFile = File.createTempFile("TarArchiveOutputStreamTest", null);
        outputStream = new FileOutputStream(temporaryFile);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    /**
     * Tear down method for the TarArchiveOutputStreamTest class
     *
     * @throws IOException if an error occurs during tear down
     */
    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
        IOUtils.closeQuietly(temporaryFile.getInputStream());
        temporaryFile.delete();
    }

    /**
     * Test putArchiveEntry method with valid input
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");

        // Test
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Verify
        assertEquals("test.txt", tarArchiveOutputStream.getName());
        assertEquals(10, tarArchiveOutputStream.getSize());

        // Test writing data to the archive
        ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
        byte[] data = "Hello, World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify data has been written to the archive
        ByteArrayInputStream inputStream = new ByteArrayInputStream(dataOutputStream.toByteArray());
        TarArchiveEntry readEntry = tarArchiveOutputStream.getNextEntry();
        byte[] readData = new byte[data.length];
        int readLength = inputStream.read(readData, 0, data.length);
        assertEquals(data, readData);
        assertNull(tarArchiveOutputStream.getNextEntry());

        // Clean up
        tarArchiveOutputStream.finish();
    }

    /**
     * Test putArchiveEntry method with invalid input - null entry
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testPutArchiveEntryNullEntry() throws IOException {
        // Test
        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(null));
    }

    /**
     * Test putArchiveEntry method with invalid input - empty name
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testPutArchiveEntryEmptyName() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry(" ");

        // Test
        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.putArchiveEntry(entry));
    }

    /**
     * Test putArchiveEntry method with invalid input - negative size
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testPutArchiveEntryNegativeSize() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(-10);

        // Test
        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.putArchiveEntry(entry));
    }

    /**
     * Test closeArchiveEntry method with valid input
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Test
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
        assertNull(tarArchiveOutputStream.getName());
        assertEquals(0, tarArchiveOutputStream.getSize());
    }

    /**
     * Test closeArchiveEntry method with invalid input - unclosed entry already
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testCloseArchiveEntryUnclosedEntry() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Test and verify
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
    }

    /**
     * Test finish method with valid input
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testFinish() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Test
        tarArchiveOutputStream.finish();

        // Verify
        assertTrue(tarArchiveOutputStream.isFinished());
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test finish method with invalid input - already finished
     *
     * @throws IOException if an error occurs during testing
     */
    @Test
    public void testFinishAlreadyFinished() throws IOException {
        // Set up
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        // Test and verify
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.finish());
    }
}