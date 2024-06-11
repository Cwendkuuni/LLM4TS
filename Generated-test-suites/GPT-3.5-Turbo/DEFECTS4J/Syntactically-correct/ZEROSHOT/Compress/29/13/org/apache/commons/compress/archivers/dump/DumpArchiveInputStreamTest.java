import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() throws Exception {
        String headerMagicValue = "962083";
        byte[] buffer = new byte[32];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = Byte.parseByte(headerMagicValue.substring(i, i + 1));
        }
        InputStream is = new ByteArrayInputStream(buffer);
        dumpArchiveInputStream = new DumpArchiveInputStream(is);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertNotNull(summary);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = dumpArchiveInputStream.read(buffer);
        Assert.assertEquals(10, bytesRead);
    }

    @Test(expected = EOFException.class)
    public void testReadEOF() throws IOException {
        byte[] buffer = new byte[1024];
        while (dumpArchiveInputStream.read(buffer) != -1) {
            // Read until EOF
        }
        dumpArchiveInputStream.read(buffer);
    }

    @Test
    public void testClose() throws IOException {
        dumpArchiveInputStream.close();
        Assert.assertTrue(dumpArchiveInputStream.isClosed());
    }

}