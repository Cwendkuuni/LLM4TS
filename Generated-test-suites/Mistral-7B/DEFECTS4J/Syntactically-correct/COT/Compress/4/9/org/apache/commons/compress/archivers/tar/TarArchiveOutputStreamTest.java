import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() throws IOException {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = tarArchiveInputStream.getNextEntry();
        assertEquals("test.txt", readEntry.getName());
        assertEquals(13, readEntry.getSize());
        String entryContents = new String(new byte[13][].map(b -> new String(b, 0, b.length))[0]);
        assertEquals("Hello World!", entryContents);

        tarArchiveInputStream.close();
        archiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new ArchiveEntry() {});
    }

    @Test
    public void testPutArchiveEntryWithLargeFileName() throws IOException {
        String largeFileName = "test_entry_with_long_name_for_testing_tar_archive_output_stream_" + new String(new char[TarConstants.NAMELEN + 1])[0];
        ArchiveEntry entry = new TarArchiveEntry(largeFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(largeFileName.getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = tarArchiveInputStream.getNextEntry();
        assertEquals(largeFileName, readEntry.getName());

        tarArchiveInputStream.close();
        archiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithSizeExceedingEntrySize() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(), 0, 20);
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedContent = "Hello World!".getBytes();
        byte[] readContent = new byte[expectedContent.length];
        tarArchiveOutputStream.read(readContent);
        assertArrayEquals(expectedContent, readContent);
    }

    @Test
    public void testWriteWithNullByteArray() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write((byte[]) null);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write((byte[]) null, -1, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeLength() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write((byte[]) null, 0, -1);
    }
}