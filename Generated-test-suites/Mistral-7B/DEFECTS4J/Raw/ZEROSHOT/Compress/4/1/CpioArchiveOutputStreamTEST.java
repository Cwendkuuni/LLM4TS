import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = byteArrayOutputStream;
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithValidArguments() throws IOException {
        String entryName = "testEntry.txt";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry(testFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = ArchiveUtils.toByteArray(MAGIC_NEW);
        byte[] expectedEntryHeader = ArchiveUtils.toByteArray(expectedEntryName(entryName).getBytes(StandardCharsets.UTF_8));
        byte[] expectedEntryData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] actualOutput = byteArrayOutputStream.toByteArray();

        assertEquals(expectedHeader.length + expectedEntryHeader.length + expectedEntryData.length, actualOutput.length);
        assertTrue(byteArrayOutputStream.toByteArray().startsWith(expectedHeader));
        assertTrue(byteArrayOutputStream.toByteArray().contains(expectedEntryHeader));
        assertTrue(byteArrayOutputStream.toByteArray().contains(expectedEntryData));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonCpioArchiveEntry() throws IOException {
        String entryName = "testEntry.txt";
        File testFile = new File("testFile.txt");
        ArchiveEntry invalidEntry = new FileArchiveEntry(testFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        String entryName = "testEntry.txt";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry(testFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testFinish() throws IOException {
        String entryName = "testEntry.txt";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry(testFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.finish();

        byte[] expectedTrailer = ArchiveUtils.toByteArray(CPIO_TRAILER.getBytes(StandardCharsets.UTF_8));

        byte[] actualOutput = byteArrayOutputStream.toByteArray();

        assertTrue(byteArrayOutputStream.toByteArray().contains(expectedTrailer));

        byte[] expectedFinalOutput = new byte[expectedHeader.length + expectedEntryHeader.length + expectedEntryData.length + expectedTrailer.length];
        System.arraycopy(expectedHeader, 0, expectedFinalOutput, 0, expectedHeader.length);
        System.arraycopy(expectedEntryHeader, 0, expectedFinalOutput, expectedHeader.length, expectedEntryHeader.length);
        System.arraycopy(expectedEntryData, 0, expectedFinalOutput, expectedHeader.length + expectedEntryHeader.length, expectedEntryData.length);
        System.arraycopy(expectedTrailer, 0, expectedFinalOutput, expectedHeader.length + expectedEntryHeader.length + expectedEntryData.length, expectedTrailer.length);

        assertEquals(expectedFinalOutput.length, actualOutput.length);
        assertArrayEquals(expectedFinalOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        String entryName = "testEntry.txt";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry(testFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testClose() throws IOException {
        String entryName = "testEntry.txt";
        File testFile = new File("testFile.txt");
        CpioArchiveEntry archiveEntry = new CpioArchiveEntry(testFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.close();

        assertNull(cpioArchiveOutputStream.getNextEntry());
        assertFalse(cpioArchiveOutputStream.isOpen());
    }

    private String expectedEntryName(String entryName) {
        String osName = System.getProperty("os.name").toLowerCase();
        return (osName.contains("win") ? "." + entryName : entryName);
    }

}