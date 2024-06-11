import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarOutput);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        assertEquals(1, tarOutput.getCreatedEntries());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        assertEquals(0, tarOutput.getCreatedEntries());
    }

    @Test
    public void testWrite() throws IOException {
        byte[] content = "This is a test file.".getBytes();
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(content);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();

        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testFinish() throws IOException {
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();

        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testClose() throws IOException {
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIOException() throws IOException {
        tarOutput.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testFinishIOException() throws IOException {
        TarArchiveOutputStream tarOutput2 = new TarArchiveOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
        tarOutput2.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseIOException() throws IOException {
        TarArchiveOutputStream tarOutput2 = new TarArchiveOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
        tarOutput2.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tarOutput.createArchiveEntry(file, file.getName());
        assertEquals(file, entry.getFile());
        assertEquals(file.getName(), entry.getName());
    }

}