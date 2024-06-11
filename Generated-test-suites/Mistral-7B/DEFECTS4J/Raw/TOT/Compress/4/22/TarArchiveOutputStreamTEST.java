import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TarArchiveOutputStreamTest {

    private File outputFile;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("output.tar");
    }

    @After
    public void tearDown() throws Exception {
        if (outputFile.exists()) {
            Files.delete(Paths.get(outputFile.getAbsolutePath()));
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarOutputStream tar = new TarOutputStream(new FileOutputStream(outputFile));
        tar.setLongFileMode(TarOutputStream.LONGFILE_GNU);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFileName);

        tar.putNextEntry(entry);

        String testData = "Test data for TarArchiveOutputStreamTest.";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);

        tar.write(testDataBytes, 0, testDataBytes.length);
        tar.closeArchiveEntry();
        tar.finish();
        tar.close();

        File outputFileContent = new File(outputFile.getAbsolutePath() + "/testFile.txt");
        byte[] outputFileContentBytes = Files.readAllBytes(Paths.get(outputFileContent.getAbsolutePath()));

        assertThat(outputFileContentBytes, is(testDataBytes));
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameError() throws IOException {
        TarOutputStream tar = new TarOutputStream(new FileOutputStream(outputFile));

        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/TestFileName.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), longFileName);

        tar.putNextEntry(entry);

        tar.finish();
        tar.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarOutputStream tar = new TarOutputStream(new FileOutputStream(outputFile));

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFileName);

        tar.putNextEntry(entry);

        String testData = "Test data for TarArchiveOutputStreamTest.";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);

        tar.write(testDataBytes, 0, testDataBytes.length);

        // Do not close the entry

        tar.finish();
        tar.close();
    }
}