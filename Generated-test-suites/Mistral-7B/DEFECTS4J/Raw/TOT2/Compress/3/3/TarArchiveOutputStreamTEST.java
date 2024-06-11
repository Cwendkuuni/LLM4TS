import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.tar");
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile));
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "Test file content".getBytes(StandardCharsets.UTF_8));

        tarStream.putArchiveEntry(testEntry);
        tarStream.write("Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();
        tarStream.finish();

        TarArchiveOutputStream testStream = new TarArchiveOutputStream(new FileInputStream(testFile));
        ArchiveEntry entry = testStream.getNextEntry();
        assertEquals(new TarArchiveEntry("test.txt"), entry);
        testStream.close();

        File testFileContent = new File(testFile, "test.txt");
        String testFileContentText = IOUtils.toString(new FileInputStream(testFileContent), StandardCharsets.UTF_8);
        assertEquals("Test file content", testFileContentText);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry testEntry = new TarArchiveEntry("test" + TarConstants.NAMELEN + "longFileName.txt", "Test file content".getBytes(StandardCharsets.UTF_8));

        tarStream.putArchiveEntry(testEntry);
        tarStream.write("Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();
        tarStream.finish();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName_longFileModeGNU() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile), TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry testEntry = new TarArchiveEntry("test" + TarConstants.NAMELEN + "longFileName.txt", "Test file content".getBytes(StandardCharsets.UTF_8));

        tarStream.putArchiveEntry(testEntry);
        tarStream.write("Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.write("longFileName".getBytes(StandardCharsets.UTF_8));
        tarStream.write(0);
        tarStream.closeArchiveEntry();
        tarStream.finish();

        TarArchiveOutputStream testStream = new TarArchiveOutputStream(new FileInputStream(testFile));
        ArchiveEntry entry = testStream.getNextEntry();
        assertEquals(new TarArchiveEntry("test" + TarConstants.NAMELEN + "longFileName.txt"), entry);
        testStream.close();

        File testFileContent = new File(testFile, "test" + TarConstants.NAMELEN + "longFileName.txt");
        String testFileContentText = IOUtils.toString(new FileInputStream(testFileContent), StandardCharsets.UTF_8);
        assertEquals("Test file content", testFileContentText);

        File testLongFileName = new File(testFile, "longFileName");
        assertFalse(testLongFileName.exists());
    }

    @Test
    public void testPutArchiveEntries() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile));

        TarArchiveEntry testEntry1 = new TarArchiveEntry("test1.txt", "Test file content 1".getBytes(StandardCharsets.UTF_8));
        TarArchiveEntry testEntry2 = new TarArchiveEntry("test2.txt", "Test file content 2".getBytes(StandardCharsets.UTF_8));

        tarStream.putArchiveEntry(testEntry1);
        tarStream.write("Test file content 1".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();

        tarStream.putArchiveEntry(testEntry2);
        tarStream.write("Test file content 2".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();

        tarStream.finish();

        TarArchiveOutputStream testStream = new TarArchiveOutputStream(new FileInputStream(testFile));

        ArchiveEntry entry1 = testStream.getNextEntry();
        assertEquals(new TarArchiveEntry("test1.txt"), entry1);
        testStream.closeArchiveEntry();

        ArchiveEntry entry2 = testStream.getNextEntry();
        assertEquals(new TarArchiveEntry("test2.txt"), entry2);
        testStream.closeArchiveEntry();

        testStream.close();

        File testFileContent1 = new File(testFile, "test1.txt");
        String testFileContentText1 = IOUtils.toString(new FileInputStream(testFileContent1), StandardCharsets.UTF_8);
        assertEquals("Test file content 1", testFileContentText1);

        File testFileContent2 = new File(testFile, "test2.txt");
        String testFileContentText2 = IOUtils.toString(new FileInputStream(testFileContent2), StandardCharsets.UTF_8);
        assertEquals("Test file content 2", testFileContentText2);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(byteStream);

        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.putArchiveEntry(testEntry);

        tarStream.write("Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();
        tarStream.finish();

        byte[] testFileContent = tarStream.getBuffer().getBuffer();
        String testFileContentText = new String(testFileContent, StandardCharsets.UTF_8);
        assertEquals("Test file content", testFileContentText);
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(byteStream, TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry testEntry = new TarArchiveEntry("test" + TarConstants.NAMELEN + "longFileName.txt", "Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.putArchiveEntry(testEntry);

        tarStream.write("Test file content".getBytes(StandardCharsets.UTF_8));
        tarStream.write("longFileName".getBytes(StandardCharsets.UTF_8));
        tarStream.write(0);
        tarStream.closeArchiveEntry();
        tarStream.finish();

        byte[] testFileContent = tarStream.getBuffer().getBuffer();
        String testFileContentText = new String(testFileContent, StandardCharsets.UTF_8);
        assertEquals("Test file content", testFileContentText.substring(0, testFileContentText.length() - 1));

        assertEquals(testFileContent[testFileContentText.length()], (byte) 0);
    }

    @Test
    public void testWriteWithLargeData() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(byteStream, 4096);

        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", new byte[1024 * 1024]);
        tarStream.putArchiveEntry(testEntry);

        tarStream.write(new byte[1024 * 1024]);
        tarStream.closeArchiveEntry();
        tarStream.finish();

        byte[] testFileContent = tarStream.getBuffer().getBuffer();
        assertTrue(testFileContent.length > 1024 * 1024);
    }
}