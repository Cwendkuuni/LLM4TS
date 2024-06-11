import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream byteOutput;

    @Before
    public void setUp() throws Exception {
        byteOutput = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(byteOutput);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        int longFileMode = tarOutput.getLongFileMode();

        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, longFileMode);
    }

    @Test
    public void testGetRecordSize() throws Exception {
        int expectedRecordSize = TarBuffer.DEFAULT_RCDSIZE;

        int recordSize = tarOutput.getRecordSize();

        assertEquals(expectedRecordSize, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        File file = createTempFile();
        String entryName = file.getName();

        TarArchiveEntry entry = new TarArchiveEntry(file, entryName);

        tarOutput.putArchiveEntry(entry);

        assertEquals(entry, tarOutput.getCurrentEntry());
    }

    @Test
    public void testPutArchiveEntryWithLongName() throws Exception {
        String longName = generateLongName();

        String expectedTruncatedName = longName.substring(0, TarConstants.NAMELEN);
        String expectedNonTruncatedName = longName;

        TarArchiveEntry entry = new TarArchiveEntry(new File(longName), longName);

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();

        TarArchiveEntry truncatedEntry = tarOutput.getNextEntry();

        assertEquals(expectedTruncatedName, truncatedEntry.getName());

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();

        TarArchiveEntry nonTruncatedEntry = tarOutput.getNextEntry();

        assertEquals(expectedNonTruncatedName, nonTruncatedEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNameTooLongAndNonTruncateMode() throws Exception {
        String longName = generateLongName();

        TarArchiveEntry entry = new TarArchiveEntry(new File(longName), longName);

        tarOutput.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        File file = createTempFile();
        String entryName = file.getName();

        TarArchiveEntry entry = new TarArchiveEntry(file, entryName);

        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[1024]);
        tarOutput.closeArchiveEntry();

        assertNull(tarOutput.getCurrentEntry());
    }

    @Test
    public void testFinish() throws Exception {
        File file = createTempFile();
        String entryName = file.getName();

        TarArchiveEntry entry = new TarArchiveEntry(file, entryName);

        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[1024]);
        tarOutput.closeArchiveEntry();

        ByteArrayOutputStream expectedBytes = new ByteArrayOutputStream();
        TarArchiveOutputStream expectedTarOutput = new TarArchiveOutputStream(expectedBytes);
        expectedTarOutput.putArchiveEntry(entry);
        expectedTarOutput.write(new byte[1024]);
        expectedTarOutput.closeArchiveEntry();
        expectedTarOutput.finish();

        tarOutput.finish();

        assertArrayEquals(expectedBytes.toByteArray(), byteOutput.toByteArray());
    }

    @Test
    public void testClose() throws Exception {
        tarOutput.close();

        assertTrue(tarOutput.isClosed());
    }

    private File createTempFile() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();
        return tempFile;
    }

    private String generateLongName() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append('a');
        }
        return sb.toString();
    }
}