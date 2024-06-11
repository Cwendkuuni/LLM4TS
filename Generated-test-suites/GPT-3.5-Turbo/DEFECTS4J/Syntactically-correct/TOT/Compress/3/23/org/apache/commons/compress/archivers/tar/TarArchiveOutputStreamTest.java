import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);

        assertEquals("test.txt", tarOutput.getCurrentEntry().getName());
        assertFalse(tarOutput.getCurrentEntry().isDirectory());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutput.write(data);

        tarOutput.closeArchiveEntry();
        tarOutput.close();

        // Check if the output stream contains the correct data
        byte[] result = outputStream.toByteArray();
        assertEquals(data.length, result.length);
        assertArrayEquals(data, result);
    }

    @Test(expected = IOException.class)
    public void testWrite_exceedsEntrySize() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        entry.setSize(5);
        tarOutput.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutput.write(data);

        tarOutput.closeArchiveEntry();
        tarOutput.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutput.write(data);

        tarOutput.closeArchiveEntry();

        assertNull(tarOutput.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_incompleteWrite() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutput.write(data);

        tarOutput.close();
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutput.write(data);

        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();

        // Check if the output stream ends with two EOF records
        byte[] result = outputStream.toByteArray();
        assertEquals(1024, result.length);
        assertEquals(0, result[1022]);
        assertEquals(0, result[1023]);
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutput.write(data);

        tarOutput.closeArchiveEntry();
        tarOutput.close();

        // Check if the output stream ends with two EOF records
        byte[] result = outputStream.toByteArray();
        assertEquals(1024, result.length);
        assertEquals(0, result[1022]);
        assertEquals(0, result[1023]);
    }
}