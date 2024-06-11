import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
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
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() {
        tarOutputStream.setLongFileMode(123);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(inputStream);
        newTarOutputStream.finish();

        assertEquals(2, inputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(inputStream);
        newTarOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarConstants.RECORD_SIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = new byte[5];

        tarOutputStream.write(expectedData);
        tarOutputStream.closeArchiveEntry();

        byte[] actualBytes = outputStream.toByteArray();

        byte[] expectedBytes = new byte[expectedHeader.length + expectedData.length + 512];
        System.arraycopy(expectedHeader, 0, expectedBytes, 0, expectedHeader.length);
        System.arraycopy(expectedData, 0, expectedBytes, expectedHeader.length, expectedData.length);
        System.arraycopy(entry.getFooterBytes(), 0, expectedBytes, expectedHeader.length + expectedData.length, 512 - expectedData.length);

        assertEquals(expectedBytes.length, actualBytes.length);
        assertEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[10]);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = new byte[15 + 512];
        System.arraycopy(entry.getHeaderBytes(), 0, expectedBytes, 0, 15);
        System.arraycopy(entry.getFooterBytes(), 0, expectedBytes, 15, 512);

        byte[] actualBytes = outputStream.toByteArray();

        assertEquals(expectedBytes.length, actualBytes.length);
        assertEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[10];
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = new byte[15 + 10 + 512];
        System.arraycopy(entry.getHeaderBytes(), 0, expectedBytes, 0, 15);
        System.arraycopy(data, 0, expectedBytes, 15, 10);
        System.arraycopy(entry.getFooterBytes(), 0, expectedBytes, 25, 512);

        byte[] actualBytes = outputStream.toByteArray();

        assertEquals(expectedBytes.length, actualBytes.length);
        assertEquals(expectedBytes, actualBytes);
    }
}