import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);

        assertEquals(entry, tarOutputStream.getEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertEquals(0, tarOutputStream.getCurrentBytes());
    }

    @Test
    public void testWriteBytes() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data);

        assertEquals(data.length, tarOutputStream.getCurrentBytes());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        assertEquals(1024, outputStream.size());
    }
}