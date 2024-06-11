import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(2);
        assertEquals(2, tarOutput.getLongFileMode());

        tarOutput.setLongFileMode(1);
        assertEquals(1, tarOutput.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        assertArrayEquals(new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE * 2], tarOutput.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        tarOutput.close();
        assertTrue(tarOutput.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        assertEquals("testFile", tarOutput.getCurrentEntry().getName());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        tarOutput.closeArchiveEntry();
        assertEquals(0, tarOutput.getCurrentEntry().getSize());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = "testData".getBytes();
        tarOutput.write(data);

        assertEquals(data.length, tarOutput.getCurrentEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testWriteWithExceedingSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE + 1];
        tarOutput.write(data);
    }

    @Test
    public void testWriteWithAssembleBuffer() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE - 5];
        tarOutput.write(data);

        byte[] remainingData = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE];
        tarOutput.write(remainingData);

        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarOutput.getCurrentEntry().getSize());
    }

    @Test
    public void testWriteWithMultipleRecords() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE * 3];
        tarOutput.write(data);

        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE * 3, tarOutput.getCurrentEntry().getSize());
    }

    @Test
    public void testWriteWithEmptyBuffer() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = new byte[0];
        tarOutput.write(data);

        assertEquals(0, tarOutput.getCurrentEntry().getSize());
    }

    @Test
    public void testWriteWithEmptyBufferAndNonEmptyData() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = "testData".getBytes();
        tarOutput.write(data, 0, 0);

        assertEquals(0, tarOutput.getCurrentEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE + 1];
        tarOutput.write(data, 0, data.length);
    }

    @Test
    public void testWriteWithValidOffsetAndSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(archiveEntry);

        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE];
        tarOutput.write(data, 5, 2);

        assertEquals(2, tarOutput.getCurrentEntry().getSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile");
        ArchiveEntry archiveEntry = tarOutput.createArchiveEntry(inputFile, "entryName");

        assertEquals("entryName", archiveEntry.getName());
    }

}