import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = File.createTempFile("junit-test-", ".tar");
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.close();
        }
        if (outputFile != null) {
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        // Create an input file for the test
        String fileName = "testFile.txt";
        String fileContent = "This is a test file.";
        File inputFile = File.createTempFile(fileName, ".txt");
        Files.write(Paths.get(inputFile.getAbsolutePath()), fileContent.getBytes());

        // Put the archive entry with a valid fileName
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] fileContentBytes = Files.readAllBytes(Paths.get(inputFile.getAbsolutePath()));
        tarOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        tarOutputStream.closeArchiveEntry();

        // Verify that the file was written correctly
        TarArchiveEntry entry = tarOutputStream.getNextEntry();
        assertEquals(fileName, entry.getName());
        assertEquals(fileContentBytes.length, entry.getSize());
        assertNull(entry.isDirectory());

        // Read the file from the output stream and verify its content
        InputStream inputStream = new FileInputStream(outputFile);
        byte[] outputFileContent = new byte[inputFile.length()];
        int read = inputStream.read(outputFileContent, 0, outputFileContent.length);
        assertEquals(fileContentBytes.length, read);
        assertEquals(fileContent, new String(outputFileContent, 0, read));
        inputStream.close();

        // Verify that the next entry is null, indicating the end of the archive
        entry = tarOutputStream.getNextEntry();
        assertNull(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "a".repeat(TarConstants.NAMELEN + 1) + "testFile.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("."), fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        String dirName = "testDir";
        File dirFile = new File(dirName);
        tarOutputStream.putArchiveEntry(new TarArchiveEntry(dirFile, "."));
        tarOutputStream.closeArchiveEntry();

        // Add a test file to the directory
        String fileName = "testFile.txt";
        File inputFile = new File(dirFile, fileName);
        Files.write(Paths.get(inputFile.getAbsolutePath()), "This is a test file.".getBytes());

        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(Arrays.copyOfRange(Files.readAllBytes(Paths.get(inputFile.getAbsolutePath())), 0, 4));
        tarOutputStream.closeArchiveEntry();

        // Verify that the directory and file were written correctly
        TarArchiveEntry directoryEntry = tarOutputStream.getNextEntry();
        assertEquals(dirName, directoryEntry.getName());
        assertNull(directoryEntry.isDirectory());

        TarArchiveEntry fileEntry = tarOutputStream.getNextEntry();
        assertEquals(fileName, fileEntry.getName());
        assertEquals(Files.size(inputFile.toPath()), fileEntry.getSize());
        assertFalse(fileEntry.isDirectory());

        // Read the directory and file from the output stream
        InputStream inputStream = new FileInputStream(outputFile);
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Read the directory record
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        TarEntry tarEntry = new TarEntry(new ByteArrayInputStream(Arrays.copyOfRange(buffer, 0, bytesRead)));
        assertEquals(dirName, tarEntry.getName());
        assertFalse(tarEntry.isDirectory());

        // Read the file record
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        tarEntry = new TarEntry(new ByteArrayInputStream(Arrays.copyOfRange(buffer, 0, bytesRead)));
        assertEquals(fileName, tarEntry.getName());
        assertFalse(tarEntry.isDirectory());

        // Read the file content
        inputStream.skip( TarConstants.HEADER_LENGTH + TarEntry.MIN_RECORD_SIZE + Long.SIZE);
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(Files.readAllBytes(Paths.get(inputFile.getAbsolutePath())).length, bytesRead);
        assertEquals(Files.readAllBytes(Paths.get(inputFile.getAbsolutePath())), Arrays.copyOfRange(buffer, 0, bytesRead));

        inputStream.close();
    }
}