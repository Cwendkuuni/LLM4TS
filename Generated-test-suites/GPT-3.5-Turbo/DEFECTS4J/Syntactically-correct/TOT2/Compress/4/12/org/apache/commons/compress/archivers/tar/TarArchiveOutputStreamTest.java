import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

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
        tarOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);

        tarOutputStream.finish();
        assertEquals(1024, outputStream.size()); // EOF record added
    }

    @Test(expected = IOException.class)
    public void testCloseThrowsIOException() throws IOException {
        tarOutputStream.close();
        tarOutputStream.write("Should throw IOException".getBytes());
    }
}