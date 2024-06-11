import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {

    private static final int BLOCK_SIZE = 512;
    private static final int RECORD_SIZE = 1024;

    @Test
    public void testConstructor1() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        assertNotNull(taos);
        assertEquals(os, taos.out);
        assertEquals(BLOCK_SIZE, taos.getRecordSize());
    }

    @Test
    public void testConstructor2() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 1024;
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os, blockSize);

        assertNotNull(taos);
        assertEquals(os, taos.out);
        assertEquals(blockSize, taos.getRecordSize());
    }

    @Test
    public void testConstructor3() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 1024;
        int recordSize = 2048;
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os, blockSize, recordSize);

        assertNotNull(taos);
        assertEquals(os, taos.out);
        assertEquals(recordSize, taos.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, taos.longFileMode);

        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, taos.longFileMode);

        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, taos.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);
        taos.finish();

        // TODO: Assert something
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);
        taos.close();

        // TODO: Assert something
    }

    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);
        assertEquals(BLOCK_SIZE, taos.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        ArchiveEntry entry = new TarArchiveEntry("test.txt");

        taos.putArchiveEntry(entry);

        // TODO: Assert something
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        taos.closeArchiveEntry();

        // TODO: Assert something
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        byte[] wBuf = new byte[100];
        int wOffset = 0;
        int numToWrite = 100;

        taos.write(wBuf, wOffset, numToWrite);

        // TODO: Assert something
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        taos.writeEOFRecord();

        // TODO: Assert something
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        taos.flush();

        // TODO: Assert something
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(os);

        File inputFile = new File("test.txt");
        String entryName = "test.txt";

        ArchiveEntry entry = taos.createArchiveEntry(inputFile, entryName);

        assertNotNull(entry);
        assertEquals(inputFile, ((TarArchiveEntry) entry).getFile());
        assertEquals(entryName, entry.getName());
    }
}