import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
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

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarOut;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
    }

    @Test
    public void testConstructorWithOutputStream() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new File("test.tar").getOutputFileStream());
        assertNull(tarOut.getArchiveEntry());
        tarOut.close();
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out, 512, 512);
        assertNull(tarOut.getArchiveEntry());
        tarOut.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out, 512, 512);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out, 512, 512);
        tarOut.setLongFileMode(4);
        tarOut.close();
    }

    @Test
    public void testFinish() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        tarOut.finish();
        String result = out.toString(StandardCharsets.UTF_8);
        String expected = getExpectedHeaderForTestFile("test.txt") + "00000000000000000000\n"
                + getExpectedHeaderForTestFile("test.txt") + "00000000000000000000";
        assertEquals(expected, result);
        tarOut.close();
    }

    @Test
    public void testClose() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        tarOut.finish();
        tarOut.close();
        String result = out.toString(StandardCharsets.UTF_8);
        String expected = getExpectedHeaderForTestFile("test.txt") + "00000000000000000000\n"
                + getExpectedHeaderForTestFile("test.txt") + "00000000000000000000";
        assertEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        tarOut.finish();
        tarOut.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        tarOut.finish();
        tarOut.close();
        tarOut.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out, 512, 512);
        assertEquals(512, tarOut.getRecordSize());
        tarOut.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        TarArchiveEntry archiveEntry = tarOut.getArchiveEntry();
        assertEquals("test.txt", archiveEntry.getName());
        assertTrue(archiveEntry.isFile());
        assertEquals(12, archiveEntry.getSize());
        assertEquals(12, archiveEntry.getCompressedSize());
        assertEquals(12, archiveEntry.getCrc());
        tarOut.close();
    }

    @Test
    public void testPutArchiveEntryWithDirectoryFlag() throws IOException {
        putEntryInArchive("testdir/", null, true);
        TarArchiveEntry archiveEntry = tarOut.getArchiveEntry();
        assertEquals("testdir/", archiveEntry.getName());
        assertTrue(archiveEntry.isDirectory());
        tarOut.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        tarOut.closeArchiveEntry();
        TarArchiveEntry archiveEntry = tarOut.getArchiveEntry();
        assertNull(archiveEntry);
        tarOut.close();
    }

    @Test
    public void testWrite() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        byte[] testBytes = "Hello World".getBytes();
        tarOut.write(testBytes, 0, testBytes.length);
        String result = out.toString(StandardCharsets.UTF_8);
        String expected = getExpectedHeaderForTestFile("test.txt") + new String(testBytes, StandardCharsets.UTF_8)
                + "\n";
        assertEquals(expected, result);
        tarOut.close();
    }

    @Test
    public void testWriteWithPadding() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        byte[] testBytes = "Hello World".getBytes();
        byte[] paddingBytes = new byte[512 - testBytes.length];
        tarOut.write(testBytes, 0, testBytes.length);
        tarOut.write(paddingBytes, 0, paddingBytes.length);
        String result = out.toString(StandardCharsets.UTF_8);
        String expected = getExpectedHeaderForTestFile("test.txt") + new String(testBytes, StandardCharsets.UTF_8)
                + new String(paddingBytes, StandardCharsets.UTF_8) + "\n";
        assertEquals(expected, result);
        tarOut.close();
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondEntrySize() throws IOException {
        putEntryInArchive("test.txt", "Hello World");
        byte[] testBytes = "Hello World".getBytes();
        byte[] beyondEntrySizeBytes = new byte[128];
        tarOut.write(testBytes, 0, testBytes.length);
        tarOut.write(beyondEntrySizeBytes, 0, beyondEntrySizeBytes.length);
    }

    private void putEntryInArchive(String fileName, String content, boolean isDirectory) throws IOException {
        if (content != null) {
            byte[] testBytes = content.getBytes();
            tarOut.putArchiveEntry(new TarArchiveEntry(fileName, content.getBytes(StandardCharsets.UTF_8)));
            tarOut.write(testBytes, 0, testBytes.length);
            tarOut.closeArchiveEntry();
        } else {
            tarOut.putArchiveEntry(new TarArchiveEntry(fileName, isDirectory));
            tarOut.closeArchiveEntry();
        }
    }

    private String getExpectedHeaderForTestFile(String fileName) {
        return "00" + Long.toUnsignedLong(TarConstants.MAGIC) + "\n"
                + "00" + Long.toUnsignedLong(TarConstants.VERSION_1000) + "\n"
                + "00" + Long.toUnsignedLong(TarConstants.FLAGS_PAX_FORMAT) + "\n"
                + "60" + Long.toUnsignedLong(fileName.length() + 1) + "\n"
                + fileName + "\n";
    }
}