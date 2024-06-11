import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        assertNotNull(tarOutputStream.currName);
        assertEquals("test.txt", tarOutputStream.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = ArchiveUtils.toAsciiBytes("Hello, World!");
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write(data);

        assertEquals(13, tarOutputStream.currBytes);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertTrue(tarOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testDoubleFinish() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.closed);
    }

    @Test(expected = IOException.class)
    public void testDoubleClose() throws IOException {
        tarOutputStream.close();
        tarOutputStream.close();
    }
}