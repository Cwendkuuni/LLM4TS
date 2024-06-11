import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setUp() {
        mockOutputStream = mock(OutputStream.class);
        tarOutputStream = new TarArchiveOutputStream(mockOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        verify(mockOutputStream, times(2)).write(any(byte[].class));
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        verify(mockOutputStream).close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarOutputStream.putArchiveEntry(entry);
        verify(mockOutputStream).write(any(byte[].class));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
        verify(mockOutputStream, times(2)).write(any(byte[].class));
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        tarOutputStream.write(data, 0, data.length);
        verify(mockOutputStream).write(eq(data));
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile");
        String entryName = "testEntry";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        assertEquals(TarArchiveEntry.class, entry.getClass());
        assertEquals(file, ((TarArchiveEntry) entry).getFile());
        assertEquals(entryName, ((TarArchiveEntry) entry).getName());
    }

}