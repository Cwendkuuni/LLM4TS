import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry_successfully() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(100);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testPutArchiveEntry_withLongFileName_errorMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("veryLongFileNameLongerThanAllowedByTarConstants");
        
        try {
            tarOutputStream.putArchiveEntry(entry);
        } catch (RuntimeException e) {
            assertEquals("file name 'veryLongFileNameLongerThanAllowedByTarConstants' is too long ( > 100 bytes)", e.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntry_withLongFileName_truncateMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("veryLongFileNameLongerThanAllowedByTarConstants");

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testPutArchiveEntry_withLongFileName_gnuMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("veryLongFileNameLongerThanAllowedByTarConstants");
        entry.setSize(100);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testPutArchiveEntry_withDirectory() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDirectory", true);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testCloseArchiveEntry_wrongBytesWritten() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(100);
        tarOutputStream.putArchiveEntry(entry);

        try {
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            assertEquals("entry 'testFile.txt' closed at '0' before the '100' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void testWrite_lessBytesThanEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(100);
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[50];
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testWrite_moreBytesThanEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(100);
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[150];
        try {
            tarOutputStream.write(data, 0, data.length);
        } catch (IOException e) {
            assertEquals("request to write '150' bytes exceeds size in header of '100' bytes for entry 'testFile.txt'", e.getMessage());
        }
    }

    @Test
    public void testWrite_withAssemBuf() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));

        byte[] data = new byte[tarOutputStream.getRecordSize() * 3];
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testFinish_successfully() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile1.txt"));
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile2.txt"));
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testFinish_withTwoEOFRecords() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();

        assertEquals(Arrays.toString(outputStream.toByteArray()), "expected output");
    }

    @Test
    public void testSetLongFileMode_successfully() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testCreateArchiveEntry_successfully() throws IOException {
        File inputFile = new File("path/to/inputFile.txt");
        TarArchiveEntry entry = (TarArchiveEntry)tarOutputStream.createArchiveEntry(inputFile, "entryName");
        
        assertEquals(inputFile, entry.getFile());
        assertEquals("entryName", entry.getName());
    }
}