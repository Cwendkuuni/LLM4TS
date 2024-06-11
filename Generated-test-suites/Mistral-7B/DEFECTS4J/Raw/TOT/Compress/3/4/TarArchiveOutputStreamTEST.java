import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private File testFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("junit", "tar");
        outputStream = new FileOutputStream(testFile);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.finish();
        tarOutputStream.close();
        outputStream.close();
        testFile.delete();
    }

    @Test
    public void testSetLongFileMode() {
        // Set long file mode to "LONGFILE\_TRUNCATE"
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Create an entry with a long file name
        TarArchiveEntry entry = new TarArchiveEntry("src/test/resources/verylongfilename.txt");

        // Put entry on the output stream
        tarOutputStream.putArchiveEntry(entry);

        // Write some data
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, data.length);

        // Close entry
        tarOutputStream.closeArchiveEntry();

        // Ensure that the file name was truncated in the archive
        TarArchiveEntry archiveEntry = tarOutputStream.getNextEntry();
        assertEquals("verylongfilename", archiveEntry.getName().substring(0, TarConstants.NAMELEN));
    }

    @Test
    public void testLongFileModeError() {
        // Set long file mode to "LONGFILE\_ERROR"
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        // Create an entry with a long file name
        TarArchiveEntry entry = new TarArchiveEntry("src/test/resources/verylongfilename.txt");

        // Put entry on the output stream
        assertThrows(RuntimeException.class, () -> tarOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testLongFileModeGnu() {
        // Set long file mode to "LONGFILE\_GNU"
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Create an entry with a long file name
        TarArchiveEntry entry = new TarArchiveEntry("src/test/resources/verylongfilename.txt");

        // Put long name entry on the output stream
        tarOutputStream.putArchiveEntry(entry);

        // Write long nameentry data
        byte[] longNameData = "verylongfilename.txt\0".getBytes();
        tarOutputStream.write(longNameData, 0, longNameData.length);

        // Put data entry on the output stream
        TarArchiveEntry dataEntry = new TarArchiveEntry("data.txt");
        tarOutputStream.putArchiveEntry(dataEntry);

        // Write data
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, data.length);

        // Close data entry
        tarOutputStream.closeArchiveEntry();

        // Finish the archive
        tarOutputStream.finish();

        // Ensure that the archive contains the long file name
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(testFile));
        TarArchiveEntry archiveEntry;
        while ((archiveEntry = tarInputStream.getNextEntry()) != null) {
            if (archiveEntry.getName().equals("verylongfilename.txt")) {
                byte[] nameBytes = archiveEntry.getName().getBytes();
                assertEquals("verylongfilename.txt", new String(nameBytes, 0, nameBytes.length - 1));
                break;
            }
        }

        // Close input stream
        tarInputStream.close();
    }

    @Test
    public void testCloseWithoutClosingArchiveEntry() throws Exception {
        // Create an entry
        TarArchiveEntry entry = new TarArchiveEntry("data.txt");

        // Put entry on the output stream
        tarOutputStream.putArchiveEntry(entry);

        // Write some data
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, data.length);

        // Close the output stream without closing the archive entry
        outputStream.close();

        // Ensure that an IOException is thrown when trying to write more data
        assertThrows(IOException.class, () -> tarOutputStream.write(data, 0, data.length));

        // Ensure that an EOFException is thrown when trying to read data from the archive
        assertThrows(EOFException.class, () -> new TarArchiveInputStream(new FileInputStream(testFile)).read());
    }

    @Test
    public void testWriteAndReadData() throws Exception {
        // Create an entry
        TarArchiveEntry entry = new TarArchiveEntry("data.txt");

        // Put entry on the output stream
        tarOutputStream.putArchiveEntry(entry);

        // Write some data
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, data.length);

        // Close entry
        tarOutputStream.closeArchiveEntry();

        // Finish the archive
        tarOutputStream.finish();

        // Ensure that the data can be read back from the archive
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(testFile));
        byte[] readData = new byte[data.length];
        int length;
        while ((length = tarInputStream.read(readData, 0, data.length)) > 0) {
            buffer.write(readData, 0, length);
        }

        // Close input stream
        tarInputStream.close();

        // Ensure that the data is the same as the original data
        byte[] expectedData = "This is a test".getBytes();
        assertEquals(expectedData.length, buffer.size());
        byte[] actualData = buffer.toByteArray();
        assertEquals(new String(expectedData), new String(actualData));
    }
}