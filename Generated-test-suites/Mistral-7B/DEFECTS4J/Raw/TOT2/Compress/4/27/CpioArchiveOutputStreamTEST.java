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
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private static final String TEST_ARCHIVE_NAME = "test.arch";
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteOutputStream;

    @Before
    public void setUp() throws IOException {
        byteOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(TEST_FILE_NAME), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (new File(TEST_FILE_NAME).exists()) {
            new File(TEST_FILE_NAME).delete();
        }
        if (new File(TEST_ARCHIVE_NAME).exists()) {
            new File(TEST_ARCHIVE_NAME).delete();
        }
    }

    @Test
    public void testCreateCpioArchiveOutputStream() throws IOException {
        assertNotNull(outputStream);
    }

    @Test
    public void testPutArchiveEntryAndWriteFileData() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test content";
        ArchiveEntry entry = new CpioArchiveEntry(fileName);
        outputStream.putArchiveEntry(entry);
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = outputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals(fileName, archiveEntry.getName());

        byte[] fileData = byteOutputStream.toByteArray();
        byte[] expectedFileData = ArchiveUtils.concat(
                ArchiveUtils.toBytes(ArchiveUtils.toAsciiBytes(MAGIC_NEW)),
                ArchiveUtils.toBytes(ArchiveUtils.toAsciiBytes(fileName)),
                ArchiveUtils.toBytes(Long.toByteArray(fileContent.length())),
                fileContent.getBytes(StandardCharsets.UTF_8),
                ArchiveUtils.toBytes(Long.toByteArray(fileContent.length()))
        );

        assertEquals(expectedFileData.length, fileData.length);
        assertTrue(ArchiveUtils.areEqual(expectedFileData, fileData));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        outputStream.putArchiveEntry(new CpioArchiveEntry("TestEntry"));
        outputStream.putArchiveEntry(new CpioArchiveEntry("TestEntry", 123));
        outputStream.putArchiveEntry(new CpioArchiveEntry("TestEntry", 123, CpioConstants.FORMAT_INVALID));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        String fileName = "testFile";
        ArchiveEntry entry = new CpioArchiveEntry(fileName);
        outputStream.putArchiveEntry(entry);
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullName() throws IOException {
        outputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryWithNullFile() throws IOException {
        String fileName = "testFile";
        ArchiveEntry entry = new CpioArchiveEntry(fileName);
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutTwoArchiveEntriesAndFinish() throws IOException {
        String fileName1 = "testFile1";
        String fileName2 = "testFile2";
        String fileContent1 = "Test content 1";
        String fileContent2 = "Test content 2";

        ArchiveEntry entry1 = new CpioArchiveEntry(fileName1);
        ArchiveEntry entry2 = new CpioArchiveEntry(fileName2);

        outputStream.putArchiveEntry(entry1);
        outputStream.write(fileContent1.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(entry2);
        outputStream.write(fileContent2.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.finish();

        ArchiveEntry archiveEntry1 = outputStream.getNextEntry();
        ArchiveEntry archiveEntry2 = outputStream.getNextEntry();

        assertEquals(fileName1, archiveEntry1.getName());
        assertEquals(fileName2, archiveEntry2.getName());

        archiveEntry1.close();
        archiveEntry2.close();

        byte[] fileData1 = byteOutputStream.toByteArray();
        byte[] fileData2 = byteOutputStream.toByteArray();

        byte[] expectedFileData1 = ArchiveUtils.concat(
                ArchiveUtils.toBytes(ArchiveUtils.toAsciiBytes(MAGIC_NEW)),
                ArchiveUtils.toBytes(ArchiveUtils.toAsciiBytes(fileName1)),
                ArchiveUtils.toBytes(Long.toByteArray(fileContent1.length())),
                fileContent1.getBytes(StandardCharsets.UTF_8),
                ArchiveUtils.toBytes(Long.toByteArray(fileContent1.length()))
        );

        byte[] expectedFileData2 = ArchiveUtils.concat(
                ArchiveUtils.toBytes(ArchiveUtils.toAsciiBytes(MAGIC_NEW)),
                ArchiveUtils.toBytes(ArchiveUtils.toAsciiBytes(fileName2)),
                ArchiveUtils.toBytes(Long.toByteArray(fileContent2.length())),
                fileContent2.getBytes(StandardCharsets.UTF_8),
                ArchiveUtils.toBytes(Long.toByteArray(fileContent2.length()))
        );

        assertEquals(expectedFileData1.length, fileData1.length);
        assertTrue(ArchiveUtils.areEqual(expectedFileData1, fileData1));

        assertEquals(expectedFileData2.length, fileData2.length);
        assertTrue(ArchiveUtils.areEqual(expectedFileData2, fileData2));
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry("testFile");
        outputStream.putArchiveEntry(entry);
        outputStream.finish();

        ArchiveEntry archiveEntry = outputStream.getNextEntry();

        assertEquals(CpioConstants.C_TRAILER, archiveEntry.getFormat());
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test content";
        ArchiveEntry entry = new CpioArchiveEntry(fileName);
        outputStream.putArchiveEntry(entry);
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8).subarray(0, 1));
        outputStream.closeArchiveEntry();
    }
}