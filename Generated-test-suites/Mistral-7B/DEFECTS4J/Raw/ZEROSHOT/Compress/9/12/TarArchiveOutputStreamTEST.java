import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarArchiveOutputStream.DEFAULT_BLOCK_SIZE,
                TarArchiveOutputStream.DEFAULT_RECORD_SIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test( expected = IllegalArgumentException.class )
    public void testInvalidLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test( expected = IllegalStateException.class )
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryAndWriteBytes() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        String testString = "Hello World!";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testBytes, 0, testBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] readBytes = new byte[testBytes.length];
        inputStream.read(readBytes);
        assertEquals(new String(readBytes, StandardCharsets.UTF_8), testString);
    }

    @Test( expected = IllegalStateException.class )
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String testString = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry entry = new TarArchiveEntry(testString);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test( expected = IOException.class )
    public void testPutArchiveEntryWithLongFileNameAndLongFileMode() throws IOException {
        String testString = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry entry = new TarArchiveEntry(testString);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] nameBytes = ArchiveUtils.toAsciiBytes(entry.getName());
        tarArchiveOutputStream.write(nameBytes);
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        String readName = readEntry.getName();
        assertEquals(readName, testString);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertEquals(TarConstants.MAGIC_NUMBER, inputStream.read());
        assertEquals(TarConstants.MAGIC_NUMBER, inputStream.read());
    }

    @Test( expected = IllegalStateException.class )
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.finish());
    }

    @Test( expected = IllegalStateException.class )
    public void testCloseWithUnfinishedArchive() throws IOException {
        tarArchiveOutputStream.close();
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.close());
    }
}