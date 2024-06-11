import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File testFile = new File("testFile.txt");
        String testFileName = "testFile.txt";
        File testDirectory = new File("testDirectory");
        String testDirectoryName = "testDirectory";
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        TarArchiveEntry testFileEntry = new TarArchiveEntry(testFile, testFileName);
        TarArchiveEntry testDirectoryEntry = new TarArchiveEntry(testDirectory, testDirectoryName.isEmpty() ? "." : testDirectoryName);

        tarOS.putNextEntry(testFileEntry);
        String testString = "This is a test file.";
        tarOS.write(testString.getBytes(StandardCharsets.UTF_8));
        tarOS.closeArchiveEntry();

        tarOS.putNextEntry(testDirectoryEntry);
        tarOS.closeArchiveEntry();

        tarOS.finish();
        byte[] tarData = baos.toByteArray();

        ByteArrayOutputStream testBaos = new ByteArrayOutputStream();
        TarArchiveOutputStream testTarOS = new TarArchiveOutputStream(testBaos);
        testTarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        testTarOS.write(tarData, 0, tarData.length);
        testTarOS.finish();
        byte[] expectedTarData = testBaos.toByteArray();

        Assert.assertArrayEquals(expectedTarData, tarData);

        byte[] testFileBytes = new byte[testFile.length()];
        Files.readAllBytes(testFile.toPath(), testFileBytes);

        TarArchiveOutputStream newTarOS = new TarArchiveOutputStream(baos);
        newTarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        newTarOS.putNextEntry(new TarArchiveEntry(testFile, testFileName));
        newTarOS.write(testFileBytes, 0, testFileBytes.length);
        newTarOS.closeArchiveEntry();
        newTarOS.finish();

        Assert.assertArrayEquals(tarData, baos.toByteArray());

        tarOS.close();
        baos.close();
        testBaos.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryTooLargeFileName() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        String tooLongFileName = new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry testFileEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");

        tarOS.putNextEntry(testFileEntry);
        tarOS.finish();

        testFileEntry = new TarArchiveEntry(new File(""), tooLongFileName);
        tarOS.putNextEntry(testFileEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullFileName() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);

        TarArchiveEntry testFileEntry = null;

        tarOS.putNextEntry(testFileEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);

        TarArchiveEntry testFileEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");

        tarOS.closeArchiveEntry();
        tarOS.putNextEntry(testFileEntry);
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);

        TarArchiveEntry testFileEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
        tarOS.putNextEntry(testFileEntry);
        tarOS.write("This is a test file.".getBytes(StandardCharsets.UTF_8));
        tarOS.write("This should not be written.".getBytes(StandardCharsets.UTF_8));
        tarOS.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        tarOS.finish();

        TarArchiveEntry testFileEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
        tarOS.putNextEntry(testFileEntry);
    }
}