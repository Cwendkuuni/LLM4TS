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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    /**
     * Test putArchiveEntry method with valid arguments.
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] recordBuf = new byte[TarConstants.RECORD_LENGTH];
        tarOutputStream.read(recordBuf, 0, TarConstants.RECORD_LENGTH);

        assertNotNull(recordBuf);
        assertEquals(TarConstants.HEADER_MAGIC_NUMBER, recordBuf[0]);
        assertEquals(TarConstants.STANDARD_FILE_TYPE, recordBuf[1]);
        assertEquals(TarConstants.LONG_FILE_NAME_FLAGS, recordBuf[12]);
        assertEquals("test.txt", new String(recordBuf, 16, TarConstants.NAME_LENGTH));
    }

    /**
     * Test putArchiveEntry method with long file name.
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + ".txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test closeArchiveEntry method.
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] recordBuf = new byte[TarConstants.RECORD_LENGTH];

        tarOutputStream.write(new byte[]{'A', 'B', 'C'}, 0, 3);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.write(new byte[]{'D', 'E', 'F'}, 0, 3);
        byte[] data = tarOutputStream.toByteArray();

        int index1 = findHeaderIndex(data, "test.txt");
        int index2 = findHeaderIndex(data, "test.txt") + TarConstants.RECORD_LENGTH;

        assertNotNull(index1);
        assertNotNull(index2);

        byte[] entryData = new byte[3];
        System.arraycopy(data, index1 + TarConstants.HEADER_LENGTH + TarConstants.FILE_HEADER_LENGTH, entryData, 0, 3);
        assertEquals("ABC", new String(entryData));

        byte[] nextEntryData = new byte[3];
        System.arraycopy(data, index2 + TarConstants.RECORD_LENGTH, nextEntryData, 0, 3);
        assertEquals("DEF", new String(nextEntryData));
    }

    /**
     * Test finish method.
     */
    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.finish();

        byte[] eofRecord1 = new byte[TarConstants.RECORD_LENGTH];
        tarOutputStream.read(eofRecord1, 0, TarConstants.RECORD_LENGTH);
        byte[] eofRecord2 = new byte[TarConstants.RECORD_LENGTH];
        tarOutputStream.read(eofRecord2, 0, TarConstants.RECORD_LENGTH);

        assertNotNull(eofRecord1);
        assertNotNull(eofRecord2);

        for (byte b : eofRecord1) {
            assertEquals(0, b);
        }

        for (byte b : eofRecord2) {
            assertEquals(0, b);
        }
    }

    private int findHeaderIndex(byte[] data, String fileName) {
        int index = -1;
        for (int i = 0; i < data.length - TarConstants.RECORD_LENGTH; ++i) {
            byte[] header = new byte[TarConstants.NAME_LENGTH + TarConstants.HEADER_LENGTH];
            System.arraycopy(data, i, header, 0, header.length);

            if (new String(header, 16, TarConstants.NAME_LENGTH).equals(fileName)) {
                index = i;
                break;
            }
        }
        return index;
    }
}