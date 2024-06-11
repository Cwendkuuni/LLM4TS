import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() throws Exception {
        String testDumpArchive = "TEST DUMP ARCHIVE";
        InputStream inputStream = new ByteArrayInputStream(testDumpArchive.getBytes());
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
    }

    @Test
    public void getNextDumpEntry() throws IOException {
        Assert.assertNotNull(dumpArchiveInputStream.getNextDumpEntry());
    }

    @Test(expected = InvalidFormatException.class)
    public void getNextDumpEntry_InvalidFormatException() throws IOException {
        dumpArchiveInputStream.getNextDumpEntry();
    }

    @Test
    public void getNextEntry() throws IOException {
        Assert.assertNotNull(dumpArchiveInputStream.getNextEntry());
    }

    @Test(expected = InvalidFormatException.class)
    public void getNextEntry_InvalidFormatException() throws IOException {
        dumpArchiveInputStream.getNextEntry();
    }

    @Test
    public void read() throws IOException {
        byte[] buffer = new byte[10];
        Assert.assertEquals(10, dumpArchiveInputStream.read(buffer));
    }

    @Test
    public void read_TooManyBytes() throws IOException {
        byte[] buffer = new byte[20];
        Assert.assertEquals(10, dumpArchiveInputStream.read(buffer));
    }

    @Test
    public void read_EOF() throws IOException {
        byte[] buffer = new byte[20];
        dumpArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, dumpArchiveInputStream.read(buffer));
    }

    @Test(expected = IllegalStateException.class)
    public void read_IllegalStateException() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream("TEST".getBytes()));
        inputStream.read();
    }

    @Test
    public void close() throws IOException {
        dumpArchiveInputStream.close();
        Assert.assertTrue(dumpArchiveInputStream.isClosed);
    }
}