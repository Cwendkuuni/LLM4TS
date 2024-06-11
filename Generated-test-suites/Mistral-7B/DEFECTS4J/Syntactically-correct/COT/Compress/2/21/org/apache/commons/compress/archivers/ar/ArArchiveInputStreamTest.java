import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setup() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveInputStream.HEADER);
        outputStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x0a});
        outputStream.write(new byte[]{0x61, 0x72, 0x63, 0x68, 0x74, 0x73, 0x73, 0x31, 0x30, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0a, 0x00, 0x00, 0x0a, 0x3e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0a});
        outputStream.write(new byte[]{0x61, 0x72, 0x63, 0x68, 0x65, 0x78, 0x65, 0x00, 0x00, 0x00, 0x09, 0x01, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x0a, 0x3e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0a});
        outputStream.write(ArArchiveInputStream.TRAILER);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("name1.txt", entry.getName());
        Assert.assertEquals(9L, entry.getSize());

        entry = archiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("name2.bin", entry.getName());
        Assert.assertEquals(10L, entry.getSize());

        entry = archiveInputStream.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x0a, 0x3e, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x0a});
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        archiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithNoEntry() throws IOException {
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertSame(entry, archiveInputStream.getNextArEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int read = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
        Assert.assertEquals((byte) 0x21, buffer[0]);

        read = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
        Assert.assertEquals((byte) 0x3c, buffer[0]);
    }

    @Test
    public void testReadWithLength() throws IOException {
        byte[] buffer = new byte[3];
        int read = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{0x61, 0x72, 0x63}, buffer);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] buffer = new byte[1];
        int read = archiveInputStream.read(buffer, 12, buffer.length);
        Assert.assertEquals(0, read);
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[1];
        int read = archiveInputStream.read(buffer, -1, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[1];
        int read = archiveInputStream.read(buffer, 0, -1);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[16];
        Assert.assertTrue(ArArchiveInputStream.matches(signature, 16));
    }

    @Test
    public void testMatchesWithInvalidLength() {
        byte[] signature = new byte[8];
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 8));
    }
}