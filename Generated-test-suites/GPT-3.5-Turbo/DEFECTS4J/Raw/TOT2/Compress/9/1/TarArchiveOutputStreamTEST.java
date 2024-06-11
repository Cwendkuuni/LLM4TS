import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarStream;
    private ByteArrayOutputStream byteStream;

    @Before
    public void setUp() {
        byteStream = new ByteArrayOutputStream();
        tarStream = new TarArchiveOutputStream(byteStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        tarStream.putArchiveEntry(new ArchiveEntry("testFile.txt"));
        tarStream.closeArchiveEntry();
        assertNotNull(tarStream);
    }

    @Test
    public void testWrite() throws IOException {
        tarStream.putArchiveEntry(new ArchiveEntry("testFile.txt"));
        tarStream.write("Test Data".getBytes());
        tarStream.closeArchiveEntry();
        assertNotNull(tarStream);
    }

    @Test
    public void testFinish() throws IOException {
        tarStream.finish();
        assertTrue(byteStream.size() > 0);
    }

    @Test(expected = IOException.class)
    public void testFinishWhenAlreadyFinished() throws IOException {
        tarStream.finish();
        tarStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarStream.close();
        assertTrue(byteStream.size() > 0);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ArchiveEntry entry = tarStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        assertNotNull(entry);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWhenStreamFinished() throws IOException {
        tarStream.finish();
        tarStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
    }

    @Test
    public void testSetLongFileMode() {
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.longFileMode);
    }

    @Test
    public void testFinishWhenUnclosedEntry() throws IOException {
        tarStream.putArchiveEntry(new ArchiveEntry("testFile.txt"));
        try {
            tarStream.finish();
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test(expected = IOException.class)
    public void testFinishWhenHaveUnclosedEntry() throws IOException {
        tarStream.putArchiveEntry(new ArchiveEntry("testFile.txt"));
        tarStream.putArchiveEntry(new ArchiveEntry("testFile2.txt"));
        tarStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutCurrentEntry() throws IOException {
        tarStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingEntrySize() throws IOException {
        tarStream.putArchiveEntry(new ArchiveEntry("testFile.txt"));
        tarStream.write(new byte[1000]);
        tarStream.closeArchiveEntry();
    }

}