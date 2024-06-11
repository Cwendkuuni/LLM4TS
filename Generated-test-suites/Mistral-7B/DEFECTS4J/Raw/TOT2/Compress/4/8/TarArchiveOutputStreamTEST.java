import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private ByteArrayOutputStream byteArrayOutputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        out = byteArrayOutputStream;
        tarArchiveOutputStream = new TarArchiveOutputStream(out, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntryWithValidFileName() throws IOException {
        File file = new File("testFile");
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        archiveEntry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarRecord = new byte[TarConstants.RECORD_LENGTH];
        // The first 512 bytes are the header record
        System.arraycopy(archiveEntry.getHeaderRecord(), 0, expectedTarRecord, 0, archiveEntry.getHeaderRecord().length);
        // The next 512 bytes are the data record
        System.arraycopy(archiveEntry.getHeaderRecord(), 0, expectedTarRecord, 512, archiveEntry.getHeaderRecord().length);
        System.arraycopy("Test file content".getBytes(), 0, expectedTarRecord, 1024, "Test file content".length());

        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedTarRecord, actualTarRecord);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFileName() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileName".repeat(100);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // The long file name will be truncated in the archive
        String expectedFileName = longFileName.substring(0, TarConstants.NAMELEN);
        TarArchiveEntry expectedArchiveEntry = new TarArchiveEntry(expectedFileName);
        byte[] expectedTarRecord = expectedArchiveEntry.getHeaderRecord();

        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedTarRecord, actualTarRecord.subArray(0, expectedTarRecord.length));
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        File dir = new File("testDir");
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testDir/", TarEntryType.DIRECTORY);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedTarRecord = new byte[TarConstants.RECORD_LENGTH];
        // The first 512 bytes are the header record
        System.arraycopy(archiveEntry.getHeaderRecord(), 0, expectedTarRecord, 0, archiveEntry.getHeaderRecord().length);
        // The next 512 bytes are the null byte record for an empty directory
        for (int i = 0; i < TarConstants.RECORD_LENGTH; i++) {
            expectedTarRecord[512 + i] = (byte) 0;
        }

        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedTarRecord, actualTarRecord);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        File file = new File("testFile");
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content".getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        File file1 = new File("testFile1.txt");
        TarArchiveEntry archiveEntry1 = new TarArchiveEntry("testFile1.txt");
        TarArchiveEntry archiveEntry2 = new TarArchiveEntry("testFile2.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry1);
        tarArchiveOutputStream.write("Test file 1 content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);
        tarArchiveOutputStream.write("Test file 2 content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarRecord2 = new byte[TarConstants.RECORD_LENGTH];
        // The first 512 bytes are the header record for file2
        System.arraycopy(archiveEntry2.getHeaderRecord(), 0, expectedTarRecord2, 0, archiveEntry2.getHeaderRecord().length);
        // The next 512 bytes are the null byte record for an empty entry
        for (int i = 0; i < TarConstants.RECORD_LENGTH; i++) {
            expectedTarRecord2[512 + i] = (byte) 0;
        }

        byte[] actualTarRecord2 = byteArrayOutputStream.toByteArray().subArray(archiveEntry1.getHeaderRecord().length, archiveEntry1.getHeaderRecord().length + archiveEntry1.getHeaderRecord().length);
        assertArrayEquals(expectedTarRecord2, actualTarRecord2);
    }
}