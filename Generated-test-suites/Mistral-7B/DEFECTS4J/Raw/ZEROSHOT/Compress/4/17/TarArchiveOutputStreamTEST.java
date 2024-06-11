import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() throws Exception {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() throws Exception {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();

        byte[] expectedEofRecord = new byte[TarConstants.STANDARD_RECORD_SIZE * 2];
        for (int i = 0; i < expectedEofRecord.length; i++) {
            expectedEofRecord[i] = 0;
        }

        byte[] output = outputStream.toByteArray();
        byte[] actualEofRecord = new byte[output.length - TarConstants.STANDARD_RECORD_SIZE];
        System.arraycopy(output, TarConstants.STANDARD_RECORD_SIZE, actualEofRecord, 0, actualEofRecord.length);

        assertArrayEquals(expectedEofRecord, actualEofRecord);
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutPuttingEntry() throws Exception {
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        TarArchiveEntry unclosedEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(unclosedEntry);
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        assertFalse(tarArchiveOutputStream.isOpen());
        assertNull(tarArchiveOutputStream.currName);
        assertNull(tarArchiveOutputStream.currSize);
        assertNull(tarArchiveOutputStream.currBytes);
    }

    @Test
    public void testGetRecordSize() throws Exception {
        assertEquals(TarConstants.STANDARD_RECORD_SIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals("test.txt", tarArchiveOutputStream.currName);
        assertEquals(0, tarArchiveOutputStream.currSize);
        assertEquals(0, tarArchiveOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws Exception {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws Exception {
        String longFileName = "longFileNameThatIsTooLongForTarEntryWithoutGNUExtension";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals(longFileName, tarArchiveOutputStream.currName);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutGNUExtension() throws Exception {
        String longFileName = "longFileNameThatIsTooLongForTarEntryWithoutGNUExtension";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertNull(tarArchiveOutputStream.currName);
        assertEquals(0, tarArchiveOutputStream.currSize);
        assertEquals(0, tarArchiveOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePuttingEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        int numToWrite = data.length;

        tarArchiveOutputStream.write(data, 0, numToWrite);

        byte[] output = outputStream.toByteArray();
        byte[] expectedRecord = new byte[TarConstants.STANDARD_RECORD_SIZE];

        System.arraycopy(entry.getHeader(expectedRecord), 0, expectedRecord, 0, TarConstants.STANDARD_RECORD_SIZE - 1);
        byte[] dataBytes = new byte[numToWrite];
        System.arraycopy(data, 0, dataBytes, 0, numToWrite);

        for (int i = 0; i < expectedRecord.length; ++i) {
            if (i < TarConstants.STANDARD_RECORD_SIZE - 1) {
                assertEquals(expectedRecord[i], output[i]);
            } else {
                byte[] record = new byte[TarConstants.STANDARD_RECORD_SIZE];
                System.arraycopy(output, i, record, 0, TarConstants.STANDARD_RECORD_SIZE);
                assertTrue(Arrays.equals(dataBytes, Arrays.copyOfRange(record, 512, 512 + numToWrite)));
            }
        }
    }

    @Test
    public void testWriteWithAssemble() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data1 = "test1".getBytes(StandardCharsets.UTF_8);
        byte[] data2 = "test2".getBytes(StandardCharsets.UTF_8);
        int numToWrite1 = data1.length;
        int numToWrite2 = data2.length;

        tarArchiveOutputStream.write(data1, 0, numToWrite1);
        tarArchiveOutputStream.write(data2, 0, numToWrite2);

        byte[] output = outputStream.toByteArray();
        byte[] expectedRecord = new byte[TarConstants.STANDARD_RECORD_SIZE];

        System.arraycopy(entry.getHeader(expectedRecord), 0, expectedRecord, 0, TarConstants.STANDARD_RECORD_SIZE - 1);
        byte[] dataBytes1 = new byte[numToWrite1];
        byte[] dataBytes2 = new byte[numToWrite2];

        System.arraycopy(data1, 0, dataBytes1, 0, numToWrite1);
        System.arraycopy(data2, 0, dataBytes2, 0, numToWrite2);

        for (int i = 0; i < expectedRecord.length; ++i) {
            if (i < TarConstants.STANDARD_RECORD_SIZE - 1) {
                assertEquals(expectedRecord[i], output[i]);
            } else {
                byte[] record = new byte[TarConstants.STANDARD_RECORD_SIZE];
                System.arraycopy(output, i, record, 0, TarConstants.STANDARD_RECORD_SIZE);
                assertTrue(Arrays.equals(dataBytes1, Arrays.copyOfRange(record, 512, 512 + numToWrite1)));
                assertTrue(Arrays.equals(dataBytes2, Arrays.copyOfRange(record, 512 + numToWrite1, 512 + numToWrite1 + numToWrite2)));
            }
        }
    }
}