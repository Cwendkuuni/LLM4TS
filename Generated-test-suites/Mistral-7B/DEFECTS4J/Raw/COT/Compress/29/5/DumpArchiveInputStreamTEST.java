import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.dump.DumpArchiveUtil;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TapeInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;
    private static final int TEST_BUFFER_SIZE = 10;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedMagicBytes = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(expectedMagicBytes, (byte) 0x00);
        DumpArchiveConstants.writeMagic(expectedMagicBytes);
        outputStream.write(expectedMagicBytes, 0, DumpArchiveConstants.TP_SIZE);

        ByteArrayOutputStream summaryOutputStream = new ByteArrayOutputStream();
        DumpArchiveSummary expectedSummary = new DumpArchiveSummary(expectedMagicBytes, new ZipEncoding("UTF-8"));
        expectedSummary.write(summaryOutputStream);

        ByteArrayInputStream summaryInputStream = new ByteArrayInputStream(summaryOutputStream.toByteArray());

        ByteArrayOutputStream blockBufferOutputStream = new ByteArrayOutputStream();
        byte[] blockBuffer = new byte[4 * DumpArchiveConstants.TP_SIZE];
        blockBuffer[0] = (byte) 0x00;
        blockBuffer[1] = (byte) 0x01;
        blockBuffer[2] = (byte) 0x02;
        blockBuffer[3] = (byte) 0x03;
        blockBufferOutputStream.write(blockBuffer, 0, blockBuffer.length);

        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8");

        inputStream.readCLRI();
        inputStream.readBITS();
    }

    @After
    public void tearDown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testMatches() throws Exception {
        byte[] testBytes1 = new byte[32];
        byte[] testBytes2 = new byte[1024];
        assertTrue(DumpArchiveInputStream.matches(testBytes1, 32));
        assertTrue(DumpArchiveInputStream.matches(testBytes2, 1024));
        assertFalse(DumpArchiveInputStream.matches(testBytes1, 1));
        assertFalse(DumpArchiveInputStream.matches(testBytes1, 33));
        assertFalse(DumpArchiveInputStream.matches(testBytes2, 31));
    }

    @Test
    public void testGetSummary() throws Exception {
        DumpArchiveSummary actualSummary = inputStream.getSummary();
        assertEquals(expectedSummary.getMagic(), actualSummary.getMagic());
        assertEquals(expectedSummary.getChecksum(), actualSummary.getChecksum());
        assertEquals(expectedSummary.getVolumeLabel(), actualSummary.getVolumeLabel());
        assertEquals(expectedSummary.getVolumeName(), actualSummary.getVolumeName());
    }

    @Test
    public void testGetNextEntry_DirectoryEntry() throws Exception {
        DumpArchiveEntry actualEntry = inputStream.getNextEntry();
        assertTrue(actualEntry.isDirectory());
        assertNull(actualEntry.getName());
        assertEquals(0, actualEntry.getSize());
        assertEquals(0, actualEntry.getOffset());

        String expectedPath = "/";
        DumpArchiveEntry expectedEntry = new DumpArchiveEntry(0, 0, 0, expectedPath, null);
        assertEquals(expectedEntry, actualEntry);
    }

    @Test
    public void testGetNextEntry_FileEntry() throws Exception {
        DumpArchiveEntry actualEntry = inputStream.getNextEntry();
        assertFalse(actualEntry.isDirectory());
        assertNull(actualEntry.getName());
        assertEquals(0, actualEntry.getSize());
        assertEquals(0, actualEntry.getOffset());

        byte[] readBuf = new byte[TEST_BUFFER_SIZE];
        int read = inputStream.read(readBuf, 0, TEST_BUFFER_SIZE);
        assertEquals(read, TEST_BUFFER_SIZE);

        actualEntry = inputStream.getNextEntry();
        assertTrue(actualEntry.isFile());
        assertEquals("testFile", actualEntry.getName());
        assertEquals(TEST_BUFFER_SIZE, actualEntry.getSize());
        assertEquals(0, actualEntry.getOffset());
    }

    @Test(expected = EOFException.class)
    public void testGetNextEntry_EndOfStream() throws Exception {
        for (int i = 0; i < 10; i++) {
            inputStream.getNextEntry();
        }
        inputStream.getNextEntry();
    }

    @Test
    public void testRead() throws Exception {
        byte[] testBytes = new byte[TEST_BUFFER_SIZE];
        int read = 0;

        for (int i = 0; i < 10; i++) {
            read = inputStream.read(testBytes, 0, TEST_BUFFER_SIZE);
            assertEquals(TEST_BUFFER_SIZE, read);
        }

        byte[] expectedBytes = new byte[TEST_BUFFER_SIZE];
        Arrays.fill(expectedBytes, (byte) 0x01);
        byte[] actualBytes = new byte[TEST_BUFFER_SIZE];
        inputStream.read(actualBytes, 0, TEST_BUFFER_SIZE);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = EOFException.class)
    public void testRead_EndOfStream() throws Exception {
        byte[] testBytes = new byte[TEST_BUFFER_SIZE];
        int read = inputStream.read(testBytes, 0, TEST_BUFFER_SIZE);
        assertEquals(TEST_BUFFER_SIZE, read);

        inputStream.getNextEntry();
        inputStream.getNextEntry();

        read = inputStream.read(testBytes, 0, TEST_BUFFER_SIZE);
        assertEquals(-1, read);
    }

    @Test
    public void testClose() throws Exception {
        inputStream.close();
        assertFalse(inputStream.isOpen());
    }
}