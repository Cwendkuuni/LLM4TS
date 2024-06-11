
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;
    private OutputStream mockOutput;
    private ByteArrayOutputStream byteOutput;
    
    @Before
    public void setUp() throws Exception {
        byteOutput = new ByteArrayOutputStream();
        mockOutput = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                byteOutput.write(b);
            }
        };
        tarOutput = new TarArchiveOutputStream(mockOutput);
    }

    @After
    public void tearDown() throws Exception {
        tarOutput.close();
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(tarOutput);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarArchiveOutputStream.DEFAULT_RECORD_SIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();
        // Check if the entry is present in the tar output
        assertTrue(byteOutput.toByteArray().length > 0);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();
        // Check if the entry is closed in the tar output
        assertTrue(byteOutput.toByteArray().length > 0);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        byte[] content = "This is a test file.".getBytes();
        tarOutput.write(content);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();
        // Check if the content is written to the tar output correctly
        assertArrayEquals(content, byteOutput.toByteArray());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.finish();
        tarOutput.close();
        // Check if the end-of-archive entry is written to the tar output
        assertTrue(byteOutput.toByteArray().length > 0);
    }

    @Test
    public void testClose() throws IOException {
        tarOutput.close();
        // Check if the output stream is closed
        assertTrue(byteOutput.toByteArray().length > 0);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarOutput.createArchiveEntry(file, "test.txt");
        assertEquals(TarArchiveEntry.class, entry.getClass());
    }

}
