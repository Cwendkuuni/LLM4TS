import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;
    private String testFileName;

    @Before
    public void setUp() throws IOException {
        OutputStream os = new FileOutputStream("test.tar");
        tarOutput = new TarArchiveOutputStream(os);
        testFileName = "testfile.txt";
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
        File tarFile = new File("test.tar");
        tarFile.delete();
    }

    @Test
    public void testPutArchiveEntry_FileEntry() throws IOException {
        File file = new File(testFileName);
        ArchiveEntry entry = new TarArchiveEntry(file, testFileName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        assertEquals(0, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry_DirectoryEntry() throws IOException {
        File directory = new File("directory");
        directory.mkdir();
        ArchiveEntry entry = new TarArchiveEntry(directory, "directory/");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        assertTrue(directory.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_LongFileNameError() throws IOException {
        String longFileName = "verylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilename";
        File file = new File(longFileName);
        ArchiveEntry entry = new TarArchiveEntry(file, longFileName);
        tarOutput.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_LongFileNameTruncate() throws IOException {
        String longFileName = "verylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilename";
        String expectedName = longFileName.substring(0, 100);
        File file = new File(longFileName);
        ArchiveEntry entry = new TarArchiveEntry(file, longFileName);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        ArchiveEntry writtenEntry = tarOutput.createArchiveEntry(file, longFileName);
        assertEquals(expectedName, writtenEntry.getName());
    }

    @Test
    public void testPutArchiveEntry_LongFileNameGnu() throws IOException {
        String longFileName = "verylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilename";
        File file = new File(longFileName);
        ArchiveEntry entry = new TarArchiveEntry(file, longFileName);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();

        File tarFile = new File("test.tar");
        FileInputStream fis = new FileInputStream(tarFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] fileData = new byte[(int) tarFile.length()];
        bis.read(fileData);
        bis.close();

        assertTrue(new String(fileData).contains("GNUtar"));
    }

    @Test
    public void testCloseArchiveEntry_ValidEntry() throws IOException {
        File file = new File(testFileName);
        TarArchiveEntry entry = new TarArchiveEntry(file, testFileName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[]{1, 2, 3});
        tarOutput.closeArchiveEntry();
        assertEquals(3, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_IncompleteData() throws IOException {
        File file = new File(testFileName);
        TarArchiveEntry entry = new TarArchiveEntry(file, testFileName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[]{1, 2});
        tarOutput.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_ClosedEntry() throws IOException {
        File file = new File(testFileName);
        TarArchiveEntry entry = new TarArchiveEntry(file, testFileName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite_ValidData() throws IOException {
        File file = new File(testFileName);
        TarArchiveEntry entry = new TarArchiveEntry(file, testFileName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[]{1, 2, 3});
        tarOutput.closeArchiveEntry();
        assertEquals(3, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedsEntrySize() throws IOException {
        File file = new File(testFileName);
        TarArchiveEntry entry = new TarArchiveEntry(file, testFileName);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[TarArchiveOutputStream.DEFAULT_RECORD_SIZE + 1]);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        tarOutput.writeEOFRecord();
        File tarFile = new File("test.tar");
        FileInputStream fis = new FileInputStream(tarFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] fileData = new byte[(int) tarFile.length()];
        bis.read(fileData);
        bis.close();

        for (byte b : fileData) {
            assertEquals(0, b);
        }
    }

}