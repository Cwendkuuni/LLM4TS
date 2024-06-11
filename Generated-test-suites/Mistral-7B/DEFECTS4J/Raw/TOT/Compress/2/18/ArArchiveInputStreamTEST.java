import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextEntry() throws IOException {
        // Test with valid input
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveInputStream.HEADER.getBytes());
        output.write("test.txt\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0AR\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0".getBytes());
        output.write(ArArchiveInputStream.TRAILER.getBytes());
        final InputStream input = new ByteArrayInputStream(output.toByteArray());
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertNull(archiveInputStream.getNextEntry());

        // Test with empty input
        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final ArArchiveInputStream emptyArchiveInputStream = new ArArchiveInputStream(emptyInput);
        Assert.assertNull(emptyArchiveInputStream.getNextEntry());

        // Test with invalid input
        final InputStream invalidInput = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ArArchiveInputStream invalidArchiveInputStream = new ArArchiveInputStream(invalidInput);
        Assert.assertNull(invalidArchiveInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        // Test with valid input
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveInputStream.HEADER.getBytes());
        output.write("test.txt\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0AR\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0".getBytes());
        output.write(ArArchiveInputStream.TRAILER.getBytes());
        final InputStream input = new ByteArrayInputStream(output.toByteArray());
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
        Assert.assertNull(archiveInputStream.getNextEntry());

        // Test with empty input
        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final ArArchiveInputStream emptyArchiveInputStream = new ArArchiveInputStream(emptyInput);
        emptyArchiveInputStream.close();
        Assert.assertNull(emptyArchiveInputStream.getNextEntry());

        // Test with invalid input
        final InputStream invalidInput = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ArArchiveInputStream invalidArchiveInputStream = new ArArchiveInputStream(invalidInput);
        Assert.assertNull(invalidArchiveInputStream.getNextEntry());
        invalidArchiveInputStream.close();
        Assert.assertNull(invalidArchiveInputStream.getNextEntry());
    }

    @Test
    public void testMatches() {
        final byte[] signature = new byte[16];
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 15));
        System.arraycopy(ArArchiveInputStream.HEADER.getBytes(), 0, signature, 0, ArArchiveInputStream.HEADER.length);
        Assert.assertTrue(ArArchiveInputStream.matches(signature, ArArchiveInputStream.HEADER.length + 1));
    }
}