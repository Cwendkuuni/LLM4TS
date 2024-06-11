import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void setLongFileMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void finish() throws Exception {
        tarArchiveOutputStream.finish();
        // TODO: Add assertions for finish() method
    }

    @Test
    public void close() throws Exception {
        tarArchiveOutputStream.close();
        // TODO: Add assertions for close() method
    }

    @Test
    public void getRecordSize() throws Exception {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        // TODO: Add assertions for getRecordSize() method
    }

    @Test
    public void putArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        long expectedSize = 10;
        entry.setSize(expectedSize);

        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals(entry.getName(), tarArchiveOutputStream.getCurrentEntry().getName());
        assertEquals(expectedSize, tarArchiveOutputStream.getCurrentEntry().getSize());
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void closeArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        long expectedSize = 10;
        entry.setSize(expectedSize);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        // TODO: Add assertions for closeArchiveEntry() method
    }

    @Test
    public void write() throws Exception {
        byte[] data = "Hello, world!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
        // TODO: Add assertions for write() method
    }

    @Test(expected = IOException.class)
    public void write_exceedMaxSize() throws Exception {
        byte[] data = "Hello, world!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length + 1);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    private static final long CRC32_VALUE = 2670073976L;

    @Test
    public void write_withCRC32() throws Exception {
        byte[] data = "Hello, world!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);
        entry.setCrc(CRC32_VALUE);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
        // TODO: Add assertions for write() method with CRC32 value
    }

    @Test(expected = IOException.class)
    public void write_withWrongCRC32() throws Exception {
        byte[] data = "Hello, world!".getBytes();

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);
        entry.setCrc(CRC32_VALUE + 1);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
    }
}