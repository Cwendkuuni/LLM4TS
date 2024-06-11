import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_DIRECTORY_NAME = "testDirectory";
    private static final String LONG_FILE_NAME = "a" + TarConstants.NAMELEN + "longFile.txt";
    private static final int TEST_DATA_SIZE = 10;

    private File testFile;
    private File testDirectory;
    private TarArchiveOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        testDirectory = new File(TEST_DIRECTORY_NAME);
        testDirectory.mkdir();
        outputStream = new TarArchiveOutputStream(new File("test.tar").getOutputStream());
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile);
        outputStream.putArchiveEntry(entry);
        outputStream.write(new byte[TEST_DATA_SIZE]);
        outputStream.closeArchiveEntry();
        outputStream.finish();

        TarArchiveEntry entryFromArchive = extractEntry("test.tar", TEST_FILE_NAME);
        byte[] bytesFromArchive = Files.readAllBytes(new File("test.tar", TEST_FILE_NAME).toPath());
        assertEquals(TEST_DATA_SIZE, bytesFromArchive.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(LONG_FILE_NAME), LONG_FILE_NAME);
        outputStream.putArchiveEntry(longFileNameEntry);
        outputStream.write("Long file name test".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.finish();

        TarArchiveEntry longFileNameEntryFromArchive = extractEntry("test.tar", LONG_FILE_NAME);
        assertNotNull(longFileNameEntryFromArchive);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        TarArchiveEntry directoryEntry = new TarArchiveEntry(testDirectory, TEST_DIRECTORY_NAME);
        outputStream.putArchiveEntry(directoryEntry);
        outputStream.finish();

        File testDirectoryFromArchive = extractDirectory("test.tar", TEST_DIRECTORY_NAME);
        assertNotNull(testDirectoryFromArchive);
        assertEquals(TEST_DIRECTORY_NAME, testDirectoryFromArchive.getName());
    }

    @Test
    public void testPutArchiveEntryWithErrorWhenFileAlreadyExists() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile);
        outputStream.putArchiveEntry(entry);
        outputStream.finish();
        outputStream.putArchiveEntry(entry);

        assertThrows(IOException.class, () -> outputStream.putArchiveEntry(entry));
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("longFile.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.write("Long file name test".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.finish();

        byte[] archiveContent = outputStream.toByteArray();
        int longFileMode = (archiveContent[0] & 0xFF) << 6;
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, longFileMode);
    }

    @After
    public void tearDown() throws IOException {
        if (testDirectory.exists()) {
            testDirectory.deleteRecursively();
        }
        outputStream.close();
    }

    private File extractEntry(String archiveName, String entryName) throws IOException {
        File outputFile = new File(entryName);
        TarArchiveInputStream inputStream = new TarArchiveInputStream(new File(archiveName).getInputStream());
        TarArchiveEntry entry = inputStream.getNextEntry();
        if (entry == null || !entry.getName().equals(entryName)) {
            throw new IOException("Unable to find entry with name '" + entryName + "' in archive '" + archiveName + "'.");
        }
        Files.copy(inputStream, outputFile.toPath());
        inputStream.closeEntry();
        inputStream.close();
        return outputFile;
    }

    private File extractDirectory(String archiveName, String directoryName) throws IOException {
        File outputDirectory = new File(directoryName);
        TarArchiveInputStream inputStream = new TarArchiveInputStream(new File(archiveName).getInputStream());
        TarArchiveEntry entry = inputStream.getNextEntry();
        if (entry == null || !entry.isDirectory() || !entry.getName().equals(directoryName)) {
            throw new IOException("Unable to find directory with name '" + directoryName + "' in archive '" + archiveName + "'.");
        }
        outputDirectory.mkdirs();
        while (inputStream.hasNextEntry()) {
            TarArchiveEntry fileEntry = inputStream.getNextEntry();
            if (fileEntry == null) {
                break;
            }
            String relativePath = fileEntry.getName().substring(directoryName.length() + 1);
            File outputFile = new File(outputDirectory.getPath() + File.separator + relativePath);
            Files.copy(inputStream, outputFile.toPath());
            inputStream.closeEntry();
        }
        inputStream.close();
        return outputDirectory;
    }
}