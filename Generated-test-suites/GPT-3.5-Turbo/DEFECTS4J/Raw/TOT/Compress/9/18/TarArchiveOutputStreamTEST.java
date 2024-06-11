import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import org.apache.commons.compress.archivers.tar.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    
    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File file = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        tarOutputStream.putArchiveEntry(entry);
        assertTrue(tarOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedStream() throws IOException {
        tarOutputStream.finish();
        File file = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameTruncateMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        File file = new File("verylongfilenameexceedingthelimit.txt");
        String entryName = "verylongfilenameexceedingthelimit.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        File file = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        tarOutputStream.putArchiveEntry(entry);
        String content = "This is a test.";
        tarOutputStream.write(content.getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.close();

        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ArchiveEntry readEntry = tarInputStream.getNextEntry();
        String readContent = readContent(tarInputStream);
        tarInputStream.close();

        assertEquals(entryName, readEntry.getName());
        assertEquals(content, readContent);
    }

    @Test(expected = IOException.class)
    public void testWriteWithFinishedStream() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.write(new byte[10]);
    }

    @Test
    public void testFinish() throws IOException {
        File file = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        assertTrue(tarOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinishedStream() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        File file = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }

    private String readContent(TarArchiveInputStream tarInputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = tarInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toString();
    }

}