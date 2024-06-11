import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.tar.TarConstants.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".tar").toFile();
        outputStream = new FileOutputStream(tempFile);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarOutputStream != null) {
            tarOutputStream.finish();
            tarOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (tempFile != null && tempFile.exists()) {
            Files.delete(Paths.get(tempFile.getAbsolutePath()));
        }
    }

    /**
     * Test putArchiveEntry method with a regular file entry.
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a sample file to be archived
        File sampleFile = new File("sample.txt");
        Files.write(Paths.get(sampleFile.getAbsolutePath()), "Hello, World!".getBytes());

        // Put the file entry into the archive stream
        ArchiveEntry archiveEntry = new TarArchiveEntry("sample.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Write the file contents to the archive
        Files.copy(Paths.get(sampleFile.getAbsolutePath()), tarOutputStream, new FileCopyOptions());
        tarOutputStream.closeArchiveEntry();

        // Read the archive and check the contents
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(tempFile));
        ArchiveEntry entry = tarInputStream.getNextEntry();
        assertEquals("sample.txt", entry.getName());
        byte[] buffer = new byte[1024];
        int len;
        while ((len = tarInputStream.read(buffer)) > 0) {
            String content = new String(buffer, 0, len);
            assertEquals("Hello, World!", content);
        }
        tarInputStream.close();

        // Clean up
        Files.delete(Paths.get(sampleFile.getAbsolutePath()));
    }

    /**
     * Test putArchiveEntry method with a directory entry.
     */
    @Test
    public void testPutArchiveEntryDirectory() throws IOException {
        // Create a test directory
        File testDirectory = new File("test_directory");
        testDirectory.mkdir();

        // Put the directory entry into the archive stream
        ArchiveEntry archiveEntry = new TarArchiveEntry("test_directory/");
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Write the directory structure to the archive
        tarOutputStream.finish();
        tarOutputStream.close();

        // Read the archive and check the directory structure
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(tempFile));
        ArchiveEntry entry = tarInputStream.getNextEntry();
        assertEquals("test_directory/", entry.getName());
        assertEquals(TarEntry.DIRECTORY, entry.getMagic());

        // Create a test file inside the directory
        File testFile = new File(testDirectory.getAbsolutePath() + "/test.txt");
        Files.write(Paths.get(testFile.getAbsolutePath()), "Test file".getBytes());

        // Put the file entry into the archive stream
        archiveEntry = new TarArchiveEntry("test_directory/test.txt");
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        tarOutputStream.putArchiveEntry(archiveEntry);
        Files.copy(Paths.get(testFile.getAbsolutePath()), tarOutputStream, new FileCopyOptions());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        // Read the archive and check the file contents
        tarInputStream = new TarArchiveInputStream(new FileInputStream(tempFile));
        entry = tarInputStream.getNextEntry();
        assertEquals("test_directory/", entry.getName());
        assertEquals(TarEntry.DIRECTORY, entry.getMagic());

        entry = tarInputStream.getNextEntry();
        assertEquals("test_directory/test.txt", entry.getName());
        byte[] buffer = new byte[1024];
        int len;
        while ((len = tarInputStream.read(buffer, 0, buffer.length)) > 0) {
            String content = new String(buffer, 0, len);
            assertEquals("Test file", content);
        }
        tarInputStream.close();

        // Clean up
        tarInputStream.close();
        Files.delete(Paths.get(testDirectory.getAbsolutePath()));
    }

    /**
     * Test setLongFileMode method with long file names.
     */
    @Test
    public void testSetLongFileModeLongFileName() throws IOException {
        // Create a test file with a long file name
        File longFileNameFile = new File("long_file_name_test.txt");
        Files.write(Paths.get(longFileNameFile.getAbsolutePath()), "Long file name test".getBytes());

        // Set the long file mode to LONGFILE_GNU
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Put the long file entry into the archive stream
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileNameFile.getAbsolutePath());
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Write the long file name to the archive
        byte[] nameBytes = longFileNameFile.getName().getBytes();
        write(tarOutputStream, nameBytes);
        write(tarOutputStream, (byte) 0); // NUL terminator
        tarOutputStream.closeArchiveEntry();

        // Finish the archive and close the stream
        tarOutputStream.finish();
        tarOutputStream.close();

        // Read the archive and check the long file name
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(tempFile));
        ArchiveEntry entry = tarInputStream.getNextEntry();
        assertEquals("long_file_name_test.txt", entry.getName());
        tarInputStream.close();

        // Clean up
        Files.delete(Paths.get(longFileNameFile.getAbsolutePath()));
    }

    /**
     * Test setLongFileMode method with long file names and truncation.
     */
    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeTruncate() throws IOException {
        // Create a test file with a long file name
        File longFileNameFile = new File("long_file_name_test.txt");
        Files.write(Paths.get(longFileNameFile.getAbsolutePath()), "Long file name test".getBytes());

        // Set the long file mode to LONGFILE_TRUNCATE
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Try to put the long file entry into the archive stream
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileNameFile.getAbsolutePath());
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Finish the archive and close the stream
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    private void write(OutputStream outputStream, byte[] data) throws IOException {
        outputStream.write(data, 0, data.length);
    }

    private void write(OutputStream outputStream, byte data) throws IOException {
        outputStream.write(data);
    }
}