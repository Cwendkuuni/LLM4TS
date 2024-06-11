import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry_Directory() throws IOException {
        File directory = new File("path/to/directory");
        TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(directory, directory.getName());

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // Verify archive entry
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        TarArchiveEntry extractedEntry = tarInputStream.getNextTarEntry();

        assertEquals(directory.getName(), extractedEntry.getName());
        assertTrue(extractedEntry.isDirectory());
    }

    @Test
    public void testPutArchiveEntry_File() throws IOException {
        File file = new File("path/to/file.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, file.getName());

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Sample content".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // Verify archive entry
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        TarArchiveEntry extractedEntry = tarInputStream.getNextTarEntry();

        assertEquals(file.getName(), extractedEntry.getName());
        assertFalse(extractedEntry.isDirectory());

        // Verify file content
        byte[] content = new byte[extractedEntry.getSize()];
        tarInputStream.read(content);

        assertEquals("Sample content", new String(content));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_FileNameTooLong() throws IOException {
        String longFileName = "ThisIsALongFileNameThatExceedsTheMaximumAllowedLengthInTarArchives.txt";
        File file = new File("path/to/" + longFileName);
        TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, longFileName);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();

        tarOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);

        assertNull(tarInputStream.getNextTarEntry());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();

        // Verify if the underlying output stream is closed
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("path/to/file.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, file.getName());

        assertEquals(file.getName(), entry.getName());
        assertEquals(file.length(), entry.getSize());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();

        // Verify if the underlying output stream is flushed
        assertTrue(outputStream.isFlushed());
    }

}