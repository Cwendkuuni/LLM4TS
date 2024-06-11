import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = Files.createTempFile("testFile", ".tar").toFile();
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(testFile));
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        entry.setSize(10);
        entry.setName("testEntry.txt");

        tarOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        File tempFile = Files.newTempFile("testData", ".txt").toFile();
        try (FileInputStream inputStream = new FileInputStream(testFile)) {
            byte[] readData = new byte[10];
            inputStream.read(readData, 0, 10);
            assertEquals(new String(testData), new String(readData));

            Files.copy(testFile, tempFile);
            assertEquals(new String(Files.readAllBytes(tempFile.toPath())), "testEntry.txt\000" +
                    "testData for TarArchiveOutputStreamTest\000" +
                    "012\000\000\000\000\000\000\000\000\000" +
                    "\000\000");

            Files.delete(tempFile.toPath());
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "testEntry_with_long_name.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        entry.setName(longFileName);

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        // This test should pass, but it's important to verify that the archive
        // can be read correctly with a compatible archive reader
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        String longFileName = "testEntry_with_long_name.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        entry.setName(longFileName);

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_ERROR);
        tarOutputStream.putArchiveEntry(entry);

        try {
            tarOutputStream.write(new byte[1], 0, 1);
        } finally {
            tarOutputStream.closeArchiveEntry();
        }

        throw new AssertionError("Expected IOException when long file name is required.");
    }
}