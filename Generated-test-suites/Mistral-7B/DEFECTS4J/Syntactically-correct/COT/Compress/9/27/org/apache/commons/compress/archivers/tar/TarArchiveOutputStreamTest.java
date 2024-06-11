import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = Mockito.mock(OutputStream.class);
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getRecordBuf());
        assertNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertEquals(0, tarArchiveOutputStream.getLongFileMode());
        assertFalse(tarArchiveOutputStream.isFinished());
        assertFalse(tarArchiveOutputStream.isClosed());
        assertFalse(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(10);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishAlreadyFinished() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseAlreadyClosed() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        tarArchiveOutputStream.writeEOFRecord();
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_SIZE * 2];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        byte[] actualEOFRecord = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        assertNotNull(tarArchiveOutputStream.getCurrName());
        assertEquals(fileName, tarArchiveOutputStream.getCurrName());
        assertFalse(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = null;
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileWithLongName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        if (tarArchiveOutputStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_GNU) {
            byte[] expectedLongFileNameRecord = ArchiveUtils.toBytes("testFileWithLongName.txt\0");
            byte[] actualLongFileNameRecord = byteArrayOutputStream.toByteArray();
            assertArrayEquals(expectedLongFileNameRecord, actualLongFileNameRecord);
        } else {
            fail("Long file name truncation is not supported in this test.");
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameInvalidMode() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileWithLongName.txt");
        tarArchiveOutputStream.setLongFileMode(10);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        assertNull(tarArchiveOutputStream.getCurrName());
        assertFalse(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryNoEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        String testString = "Hello, World!";
        byte[] testBytes = testString.getBytes();
        OutputStream outputStreamSpy = Mockito.spy(outputStream);
        byte[] expectedBytes = new byte[testBytes.length];
        System.arraycopy(testBytes, 0, expectedBytes, 0, testBytes.length);
        Mockito.doAnswer(invocation -> {
            byte[] bytes = (byte[]) invocation.getArguments()[0];
            System.arraycopy(bytes, 0, expectedBytes, 0, bytes.length);
            return null;
        }).when(outputStreamSpy).write(Mockito.anyBytes());

        tarArchiveOutputStream = new TarArchiveOutputStream(outputStreamSpy);
        tarArchiveOutputStream.write(testBytes);
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        String testString = "Hello, World!";
        byte[] testBytes = testString.getBytes();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        tarArchiveOutputStream.write(testBytes);
        tarArchiveOutputStream.write(testBytes);
    }
}