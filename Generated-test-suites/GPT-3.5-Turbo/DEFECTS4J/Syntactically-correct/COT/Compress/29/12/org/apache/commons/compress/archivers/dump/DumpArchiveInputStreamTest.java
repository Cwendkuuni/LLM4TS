
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenReturn(-1);
        archiveInputStream = new DumpArchiveInputStream(inputStream);
    }

    @Test
    public void testGetCount() {
        int count = archiveInputStream.getCount();
        assert count == -1;
    }

    @Test
    public void testGetBytesRead() {
        long bytesRead = archiveInputStream.getBytesRead();
        assert bytesRead == 0;
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = archiveInputStream.getSummary();
        assert summary != null;
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        DumpArchiveEntry entry = archiveInputStream.getNextDumpEntry();
        assert entry == null;
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = archiveInputStream.getNextEntry();
        assert entry == null;
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = archiveInputStream.read(buffer);
        assert bytesRead == -1;
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        boolean isClosed = archiveInputStream.isClosed();
        assert isClosed;
    }

    @Test
    public void testMatches() {
        byte[] buffer = new byte[32];
        boolean matches = DumpArchiveInputStream.matches(buffer, buffer.length);
        assert matches;
    }
}
