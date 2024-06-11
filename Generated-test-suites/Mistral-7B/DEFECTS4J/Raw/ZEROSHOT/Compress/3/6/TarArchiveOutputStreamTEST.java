import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertThat(tarOutputStream, is(instanceOf(TarArchiveOutputStream.class)));
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeAndRecordSize() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 1024, 512);
        assertThat(tarOutputStream, is(instanceOf(TarArchiveOutputStream.class)));
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertThat(tarOutputStream.getLongFileMode(), is(TarArchiveOutputStream.LONGFILE_TRUNCATE));
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        byte[] expectedEOFRecord = new byte[2 * TarConstants.HEADER_BLOCK_SIZE];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEOFRecord[i] = 0;
        }
        byte[] actualData = outputStream.toByteArray();
        byte[] expectedData = new byte[TarConstants.HEADER_BLOCK_SIZE + TarConstants.NAME_LENGTH + 44];
        System.arraycopy(entry.getHeaderRecord(), 0, expectedData, 0, TarConstants.HEADER_BLOCK_SIZE);
        System.arraycopy(entry.getName().getBytes(StandardCharsets.UTF_8), 0, expectedData, TarConstants.HEADER_BLOCK_SIZE, TarConstants.NAME_LENGTH);
        System.arraycopy(Long.TO_BYTE_ARRAY(entry.getSize()), 0, expectedData, TarConstants.HEADER_BLOCK_SIZE + TarConstants.NAME_LENGTH, 8);
        System.arraycopy(expectedEOFRecord, 0, expectedData, TarConstants.HEADER_BLOCK_SIZE + TarConstants.NAME_LENGTH + 40, TarConstants.HEADER_BLOCK_SIZE);
        assertThat(actualData, is(expectedData));
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws Exception {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws Exception {
        tarOutputStream.putArchiveEntry(new File("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullBuffer() throws Exception {
        byte[] wBuf = null;
        tarOutputStream.write(wBuf, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeNumToWrite() throws Exception {
        byte[] wBuf = new byte[10];
        tarOutputStream.write(wBuf, -1, 5);
    }

    @Test
    public void testWriteWithValidBuffer() throws Exception {
        byte[] wBuf = new byte[10];
        tarOutputStream.write(wBuf, 0, 5);
    }

    @Test
    public void testWriteWithLeftoverBytes() throws Exception {
        byte[] wBuf = new byte[10];
        tarOutputStream.write(wBuf, 0, 3);
        tarOutputStream.write(wBuf, 3, 2);
    }

    @Test(expected = IOException.class)
    public void testWriteWithMoreBytesThanRemaining() throws Exception {
        byte[] wBuf = new byte[10];
        tarOutputStream.write(wBuf, 0, 15);
    }
}