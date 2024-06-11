import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("testFile.txt");
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarOutputStream.putArchiveEntry(entry);

        assertEquals(entry.getName(), tarOutputStream.currName);
        assertEquals(entry.getSize(), tarOutputStream.currSize);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileName_Error() throws IOException {
        String longFileName = generateLongFileName(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);

        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_Truncate() throws IOException {
        String longFileName = generateLongFileName(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        tarOutputStream.putArchiveEntry(entry);

        assertEquals(longFileName.substring(0, TarConstants.NAMELEN), tarOutputStream.currName);
        assertEquals(entry.getSize(), tarOutputStream.currSize);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_Gnu() throws IOException {
        String longFileName = generateLongFileName(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        tarOutputStream.putArchiveEntry(entry);

        assertEquals(TarConstants.GNU_LONGLINK, tarOutputStream.currName);
        assertEquals(longFileName.length() + 1, tarOutputStream.currSize);
    }

    @Test
    public void testCloseArchiveEntry_NoError() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertEquals(0, tarOutputStream.assemLen);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_UnderflowError() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello".getBytes());

        assertEquals(5, tarOutputStream.currBytes);

        tarOutputStream.write(" World".getBytes());

        assertEquals(11, tarOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void testWrite_TooManyBytesError() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(5);

        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.write("Hello World".getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        assertArrayEquals(expectedEOFRecord, outputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();

        assertTrue(tarOutputStream.closed);
        assertTrue(outputStream.closed);
        assertEquals(0, outputStream.size());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("input.txt");
        String entryName = "file.txt";
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, entryName);

        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
        assertEquals(entryName, archiveEntry.getName());
    }

    private String generateLongFileName(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }
}