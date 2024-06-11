import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() {
        try {
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testFinish() {
        assertDoesNotThrow(() -> tarArchiveOutputStream.finish());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> tarArchiveOutputStream.close());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = mock(TarArchiveEntry.class);
        when(entry.getName()).thenReturn("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[]{65, 66, 67});
        assertDoesNotThrow(() -> tarArchiveOutputStream.closeArchiveEntry());
    }

    @Test
    public void testWrite() throws IOException {
        byte[] bytes = new byte[]{65, 66, 67};
        assertDoesNotThrow(() -> tarArchiveOutputStream.write(bytes, 0, bytes.length));
    }

    @Test
    public void testCloseArchiveEntry() {
        assertDoesNotThrow(() -> tarArchiveOutputStream.closeArchiveEntry());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(null, "test.txt");
        assertEquals("test.txt", archiveEntry.getName());
    }

}