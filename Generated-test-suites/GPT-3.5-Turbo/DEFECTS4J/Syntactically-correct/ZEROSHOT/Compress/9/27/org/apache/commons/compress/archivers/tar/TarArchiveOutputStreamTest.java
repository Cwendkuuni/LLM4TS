import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

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
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        assertEquals(512, outputStream.size());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello World".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        assertEquals(512, outputStream.size());
        assertEquals("Hello World", new String(outputStream.toByteArray()).trim());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();

        assertEquals(1024, outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();

        assertTrue(tarOutputStream.isClosed());
    }
}