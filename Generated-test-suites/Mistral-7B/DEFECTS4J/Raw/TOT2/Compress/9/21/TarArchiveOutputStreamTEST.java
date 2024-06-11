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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        String testFileName = "testFileName.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(testFile, testFileName);

        tarOutputStream.putArchiveEntry(testEntry);

        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.write(testFileContent, 0, testFileContent.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        byte[] tarOutput = outputStream.toByteArray();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(tarOutput);
        TarArchiveOutputStream testTarOutputStream = new TarArchiveOutputStream(testInputStream);

        ArchiveEntry archiveEntry = testTarOutputStream.getNextEntry();

        assertNotNull(archiveEntry);
        assertEquals(testFileName, archiveEntry.getName());

        byte[] readContent = new byte[testFileContent.length];
        testTarOutputStream.read(readContent, 0, testFileContent.length);

        assertEquals(new String(readContent, StandardCharsets.UTF_8),
                new String(testFileContent, StandardCharsets.UTF_8));

        testTarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(null, longFileName);

        tarOutputStream.putArchiveEntry(longFileNameEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        TarArchiveEntry nullFileNameEntry = new TarArchiveEntry(null, null);

        tarOutputStream.putArchiveEntry(nullFileNameEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFile() throws IOException {
        TarArchiveEntry nullFileEntry = new TarArchiveEntry(null, "testFileName.txt");

        tarOutputStream.putArchiveEntry(nullFileEntry);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("testFile.txt"), "testFileName.txt");
        tarOutputStream.putArchiveEntry(testEntry);

        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.write(testFileContent, 0, testFileContent.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        byte[] tarOutput = outputStream.toByteArray();

        int expectedEOFRecordSize = 2 * TarConstants.RECORD_SIZE;
        int actualEOFRecordSize = Arrays.stream(tarOutput).filter(b -> b == 0).sum();

        assertEquals(expectedEOFRecordSize, actualEOFRecordSize);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("testFile.txt"), "testFileName.txt");
        tarOutputStream.putArchiveEntry(testEntry);

        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.write(testFileContent, 0, testFileContent.length);

        tarOutputStream.finish();
        tarOutputStream.closeArchiveEntry();
    }
}