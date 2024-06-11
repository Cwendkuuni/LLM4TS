import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = File.createTempFile("junit-test-", ".cpio");
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // write a test entry
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        testEntry.setSize(10);
        outputStream.putArchiveEntry(testEntry);

        // write test data
        byte[] testData = "Test data".getBytes();
        outputStream.write(testData, 0, testData.length);

        // close the entry
        outputStream.closeArchiveEntry();

        // check if the entry was written correctly
        ArchiveEntry archiveEntry = outputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals(testEntryName, archiveEntry.getName());
        assertEquals(CpioConstants.C_ISREG, archiveEntry.getFileType());
        assertEquals(10, archiveEntry.getSize());

        // check if the data was written correctly
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        testEntry.setSize(10);
        outputStream.putArchiveEntry(testEntry);
        outputStream.write(testData, 0, testData.length);
        outputStream.finish();
        outputStream.close();
        byte[] readData = baos.toByteArray();
        assertEquals(testData.length, readData.length);
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], readData[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAlreadyExists() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        outputStream.putArchiveEntry(testEntry);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testPutArchiveEntryInvalidEntry() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        testEntry.setSize(-1);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testPutArchiveEntryInvalidFormat() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        testEntry.setFormat(CpioConstants.FORMAT_OLD_ASCII + 1);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutName() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        outputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullName() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(null);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutSize() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNegativeSize() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        testEntry.setSize(-1);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testFinish() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Test data".getBytes());
        outputStream.finish();
        outputStream.close();

        ArchiveEntry archiveEntry = outputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals(testEntryName, archiveEntry.getName());
        assertEquals(CpioConstants.C_ISREG, archiveEntry.getFileType());
        assertEquals(10, archiveEntry.getSize());

        // check if the trailer was written correctly
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        testEntry = new CpioArchiveEntry();
        testEntry.setName(CPIO_TRAILER);
        testEntry.setNumberOfLinks(1);
        outputStream.putArchiveEntry(testEntry);
        outputStream.finish();
        outputStream.close();
        byte[] trailerData = baos.toByteArray();
        assertEquals(ArchiveUtils.sizeOf(trailerData), trailerData.length);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        outputStream.putArchiveEntry(testEntry);
        outputStream.finish();
        outputStream.write("Test data".getBytes());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        String testEntryName = "testFile";
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testEntryName);
        outputStream.putArchiveEntry(testEntry);
        outputStream.finish();
        outputStream.finish();
    }
}