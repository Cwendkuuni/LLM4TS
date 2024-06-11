import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * This test class is designed to test the functionalities of CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("test", null);
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (outputStream != null) {
            outputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testWriteFileWithValidArguments() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test Content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry);
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.finish();

        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveUtils.copy(new File(outputFile), testOutputStream);
        String actualContent = new String(testOutputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(fileContent, actualContent.substring(0, fileContent.length()));
    }

    @Test
    public void testWriteFileWithInvalidArguments() throws IOException {
        // Testing putArchiveEntry with null input
        assertThrows(NullPointerException.class, () -> outputStream.putArchiveEntry(null));

        // Testing putArchiveEntry with invalid format
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setFormat(-1);
        assertThrows(IllegalArgumentException.class, () -> outputStream.putArchiveEntry(entry));

        // Testing write with null input
        assertThrows(NullPointerException.class, () -> outputStream.write(null, 0, 0));

        // Testing write with negative offset and length
        assertThrows(IndexOutOfBoundsException.class, () -> outputStream.write(new byte[5], -1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> outputStream.write(new byte[5], 5, 5));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test Content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry);
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));

        // Verify that the entry size matches the written bytes
        assertEquals(fileContent.length(), outputStream.getNextEntry().getSize());

        outputStream.closeArchiveEntry();
        assertNull(outputStream.getNextEntry());
    }

    @Test
    public void testFinish() throws IOException {
        String fileName1 = "testFile1";
        String fileName2 = "testFile2";
        String fileContent1 = "Test Content 1";
        String fileContent2 = "Test Content 2";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();

        entry1.setName(fileName1);
        entry1.setSize(fileContent1.length());
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        entry2.setName(fileName2);
        entry2.setSize(fileContent2.length());
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry1);
        outputStream.write(fileContent1.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(entry2);
        outputStream.write(fileContent2.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        outputStream.finish();

        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveUtils.copy(new File(outputFile), testOutputStream);
        String actualContent = new String(testOutputStream.toByteArray(), StandardCharsets.UTF_8);

        // Verify that the test file contains both files' contents
        assertEquals(fileContent1 + fileContent2, actualContent.substring(0, actualContent.length()));
    }
}