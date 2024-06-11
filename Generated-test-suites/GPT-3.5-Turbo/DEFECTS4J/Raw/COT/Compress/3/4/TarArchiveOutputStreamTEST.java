
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream outputStream;
    private TarBuffer buffer;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        buffer = new TarBuffer(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarOutput = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        tarOutput.close();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[TarBuffer.DEFAULT_BLKSIZE * 2];
        assertEquals(expectedBytes.length, actualBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testClose() throws IOException {
        tarOutput.close();

        assertTrue(tarOutput.isClosed());
        assertTrue(buffer.isClosed());
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        int expectedSize = TarBuffer.DEFAULT_RCDSIZE;
        int actualSize = tarOutput.getRecordSize();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testSetLongFileMode() {
        int expectedMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;

        tarOutput.setLongFileMode(expectedMode);
        int actualMode = tarOutput.getLongFileMode();

        assertEquals(expectedMode, actualMode);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        tarOutput.putArchiveEntry(entry);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedHeader = new byte[TarConstants.DEFAULT_RCDSIZE];
        byte[] actualHeader = new byte[TarConstants.DEFAULT_RCDSIZE];

        buffer.readRecord(expectedHeader);
        System.arraycopy(actualBytes, 0, actualHeader, 0, TarConstants.DEFAULT_RCDSIZE);

        assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[TarConstants.DEFAULT_RCDSIZE];

        buffer.readRecord(expectedBytes);

        assertEquals(expectedBytes.length + TarConstants.DEFAULT_RCDSIZE, actualBytes.length);
        assertEquals(0, tarOutput.getCurrentBytes());
        assertEquals(0, tarOutput.getCurrentSize());
        assertNull(tarOutput.getCurrentName());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        tarOutput.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        tarOutput.write(data);
        tarOutput.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[TarConstants.DEFAULT_RCDSIZE + data.length];
        buffer.readRecord(expectedBytes, TarConstants.DEFAULT_RCDSIZE);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testWrite_withOffsetAndLength() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        tarOutput.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        int offset = 0;
        int length = data.length;
        tarOutput.write(data, offset, length);
        tarOutput.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[TarConstants.DEFAULT_RCDSIZE + data.length];
        buffer.readRecord(expectedBytes, TarConstants.DEFAULT_RCDSIZE);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWrite_throwIOException() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        tarOutput.putArchiveEntry(entry);
        byte[] data = new byte[TarBuffer.DEFAULT_BLKSIZE];
        tarOutput.write(data);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite_withAssembleLengthNotZero() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        tarOutput.putArchiveEntry(entry);

        byte[] data1 = "test d".getBytes();
        tarOutput.write(data1);

        byte[] data2 = "ata".getBytes();
        int offset = 0;
        int length = data2.length;
        tarOutput.write(data2, offset, length);

        byte[] data3 = "rest data".getBytes();
        tarOutput.write(data3);

        tarOutput.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes1 = new byte[TarConstants.DEFAULT_RCDSIZE];
        buffer.readRecord(expectedBytes1);

        byte[] expectedBytes2 = new byte[TarBuffer.DEFAULT_BLKSIZE];
        System.arraycopy(data1, 0, expectedBytes2, 0, data1.length);
        System.arraycopy(data2, offset, expectedBytes2, data1.length, length);

        byte[] expectedBytes3 = new byte[TarConstants.DEFAULT_RCDSIZE];
        System.arraycopy(data3, 0, expectedBytes3, 0, data3.length);

        byte[] expectedBytes = new byte[expectedBytes1.length + expectedBytes2.length + expectedBytes3.length];
        System.arraycopy(expectedBytes1, 0, expectedBytes, 0, expectedBytes1.length);
        System.arraycopy(expectedBytes2, 0, expectedBytes, expectedBytes1.length, expectedBytes2.length);
        System.arraycopy(expectedBytes3, 0, expectedBytes, expectedBytes1.length + expectedBytes2.length, expectedBytes3.length);

        assertArrayEquals(expectedBytes, actualBytes);
    }
}

