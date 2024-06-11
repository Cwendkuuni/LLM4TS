import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() {
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        Assert.assertEquals(68, byteArrayOutputStream.size());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.finish();

        Assert.assertThrows(IOException.class, () -> tarOutputStream.putNextEntry(new TarArchiveEntry("test2.txt")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithNullEntry() {
        Assert.assertThrows(IllegalArgumentException.class, () -> tarOutputStream.putNextEntry(null));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithClassCastException() throws IOException {
        ArchiveEntry archiveEntry = new Object();
        Assert.assertThrows(ClassCastException.class, () -> tarOutputStream.putNextEntry(archiveEntry));
        Assert.assertThrows(ClassCastException.class, () -> tarOutputStream.putNextEntry((ArchiveEntry) archiveEntry));
    }

    @Test
    public void testPutNextEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testPutNextEntryWithLongFileName() throws IOException {
        String longFileName = "test_long_filename_with_more_than_100_characters.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.finish();

        byte[] expectedBytes = longFileName.getBytes();
        for (int i = 0; i < expectedBytes.length; i++) {
            if (expectedBytes[i] == 0) {
                expectedBytes[i] = TarConstants.LF_GNUTYPE_LONGNAME;
            }
        }

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedHeader = new byte[TarConstants.HEADER_LENGTH];
        System.arraycopy(expectedBytes, 0, expectedHeader, 0, expectedBytes.length);
        int expectedSize = expectedBytes.length + TarConstants.MAGIC_LENGTH;

        Assert.assertArrayEquals(expectedSize, byteArrayOutputStream.size());
        Assert.assertArrayEquals(expectedHeader, Arrays.copyOfRange(actualBytes, 0, expectedSize));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithTooLongFileName() throws IOException {
        String tooLongFileName = "test_long_filename_with_more_than_101_characters.txt";
        TarArchiveEntry entry = new TarArchiveEntry(tooLongFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putNextEntry(entry);
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        Assert.assertThrows(IOException.class, () -> tarOutputStream.write("Hello World!".getBytes()));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedBytes = "Hello World!".getBytes();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        int index = TarConstants.HEADER_LENGTH + entry.getFileSize();
        Assert.assertArrayEquals(expectedBytes, Arrays.copyOfRange(actualBytes, index, index + expectedBytes.length));
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        Assert.assertEquals(68, byteArrayOutputStream.size());
    }
}