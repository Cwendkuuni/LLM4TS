import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link TarArchiveOutputStream} class.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream,
                                                   TarBuffer.DEFAULT_BLKSIZE,
                                                   TarBuffer.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        // Default constructor
        TarArchiveOutputStream tarOutputStream1 = new TarArchiveOutputStream(
                new java.io.FileOutputStream("testFile.tar"));

        // Constructor with block size and record size
        TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(
                new java.io.FileOutputStream("testFile.tar"), 512, 512);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        // Finish method should write EOF record twice
        tarOutputStream.finish();
        assertEquals(2 * TarConstants.RECORD_LENGTH, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testFinishWithPutArchiveEntryMissingCloseArchiveEntry()
            throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.finish();
    }

    @Test
    public void testWriteByte() throws IOException {
        // Write byte test
        byte testByte = 75;
        tarOutputStream.write(testByte);
        byte[] expectedBytes = {testByte};
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testWriteBytes() throws IOException {
        // Write bytes test
        String testString = "testString";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testBytes);
        byte[] expectedBytes = testBytes;
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // PutArchiveEntry test
        ArchiveEntry testEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        ArchiveEntry actualEntry = tarOutputStream.getNextEntry();
        assertSame(testEntry, actualEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // CloseArchiveEntry test
        ArchiveEntry testEntry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();
        assertNull(tarOutputStream.getNextEntry());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        // CreateArchiveEntry test
        File testFile = new File("testFile.txt");
        ArchiveEntry testEntry = tarOutputStream.createArchiveEntry(testFile,
                                                                    "testEntry.txt");
        assertSame(new TarArchiveEntry(testFile, "testEntry.txt"), testEntry);
    }
}