import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(512, outputStream.toByteArray().length);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileName_ErrorMode() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("long_file_name.txt"), "long_file_name.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutArchiveEntry_LongFileName_TruncateMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("long_file_name.txt"), "long_file_name.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(512, outputStream.toByteArray().length);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_GnuMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("long_file_name.txt"), "long_file_name.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(1024, outputStream.toByteArray().length);
    }

    @Test(expected = IOException.class)
    public void testWrite_BeyondEntrySize() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[600];
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWrite_SingleRecord() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[512];
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(1024, outputStream.toByteArray().length);
    }

    @Test
    public void testWrite_MultipleRecords() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[1000];
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(1024, outputStream.toByteArray().length);
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512], 0, 512);

        tarArchiveOutputStream.close();

        assertEquals(1024, outputStream.toByteArray().length);
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512], 0, 512);

        tarArchiveOutputStream.finish();

        assertEquals(1536, outputStream.toByteArray().length);
    }
}