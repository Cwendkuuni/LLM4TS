import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream),
                                                            TarBuffer.DEFAULT_BLKSIZE,
                                                            TarBuffer.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(-1);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        assertFalse(tarArchiveOutputStream.isFinished());

        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isFinished());

        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test
    public void testGetRecordSize() throws IOException {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String testFileName = "testFileName";
        File testFile = new File("testFile.txt");
        String testEntryName = "testEntryName";

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFile, testEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        String expectedHeader = "00000000000000000000" +
                Long.toUnsignedLong(testArchiveEntry.getMagic()) +
                Long.toUnsignedLong(testArchiveEntry.getVersion()) +
                Long.toUnsignedLong(testArchiveEntry.getUserID()) +
                Long.toUnsignedLong(testArchiveEntry.getGroupID()) +
                Long.toUnsignedLong(testArchiveEntry.getSize()) +
                Long.toUnsignedLong(testArchiveEntry.getModTime()) +
                Long.toUnsignedLong(testArchiveEntry.getChecksum()) +
                Long.toUnsignedLong(testArchiveEntry.getName().length()) +
                testArchiveEntry.getName().getBytes(StandardCharsets.UTF_8);

        String actualHeader = new String(tarArchiveOutputStream.buffer.getBuffer(), 0, TarBuffer.DEFAULT_RCDSIZE);
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String testFileName = "testFileName";
        File testFile = new File("testFile.txt");
        String testEntryName = "testEntryName";

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFile, testEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(testFileName.getBytes(StandardCharsets.UTF_8), 0, testFileName.getBytes(StandardCharsets.UTF_8).length);
        tarArchiveOutputStream.write(0);

        tarArchiveOutputStream.closeArchiveEntry();

        String expectedEOFRecord = "0000000000000000000000000000" +
                "000000000000";

        String actualEOFRecord = new String(tarArchiveOutputStream.buffer.getBuffer(), 0, TarBuffer.DEFAULT_RCDSIZE);
        assertEquals(expectedEOFRecord, actualEOFRecord);
    }

    @Test
    public void testWrite() throws IOException {
        String testFileName = "testFileName";
        String testString = "testString";
        File testFile = new File("testFile.txt");
        String testEntryName = "testEntryName";

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFile, testEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(testString.getBytes(StandardCharsets.UTF_8), 0, testString.getBytes(StandardCharsets.UTF_8).length);
        tarArchiveOutputStream.closeArchiveEntry();

        String expectedTestString = testString;
        String actualTestString = new String(outputStream.toByteArray(), 0, outputStream.size(), StandardCharsets.UTF_8);
        assertEquals(expectedTestString, actualTestString);
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        String testFileName = "testFileName";
        String testString = "testString";
        File testFile = new File("testFile.txt");
        String testEntryName = "testEntryName";

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFile, testEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(testString.getBytes(StandardCharsets.UTF_8), 0, testString.getBytes(StandardCharsets.UTF_8).length);

        assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
        assertFalse(tarArchiveOutputStream.isFinished());

        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        assertEquals(expectedEOFRecord, tarArchiveOutputStream.buffer.getBuffer());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        String testFileName = "testFileName";
        File testFile = new File("testFile.txt");
        String testEntryName = "testEntryName";

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(testFile, testEntryName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(testFileName.getBytes(StandardCharsets.UTF_8), 0, testFileName.getBytes(StandardCharsets.UTF_8).length);
        tarArchiveOutputStream.write(0);

        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        assertEquals(expectedEOFRecord, tarArchiveOutputStream.buffer.getBuffer());
    }
}