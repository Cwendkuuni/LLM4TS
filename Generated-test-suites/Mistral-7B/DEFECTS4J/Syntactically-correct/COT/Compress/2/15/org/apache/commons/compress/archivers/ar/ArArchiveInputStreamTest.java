import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() {
        String header = "!<arch>\n" +
                "  Type: unix\n" +
                "  Version: 2.0\n" +
                "  Size: 338\n" +
                "  Data:\n" +
                "\n";
        String entryData = "name test.txt\n" +
                "size 15\n" +
                "mode 644\n" +
                "uid 1000\n" +
                "gid 1000\n" +
                "mtime 1234567890\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(entryData.getBytes(StandardCharsets.UTF_8));

        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(arArchiveInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(arArchiveEntry);
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(15, arArchiveEntry.getSize());

        arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(arArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{"1", "2"});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayInputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithEmptyData() throws IOException {
        String header = "!<arch>\n" +
                "  Type: unix\n" +
                "  Version: 2.0\n" +
                "  Size: 0\n" +
                "  Data:\n" +
                "\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithTruncatedData() throws IOException {
        String header = "!<arch>\n" +
                "  Type: unix\n" +
                "  Version: 2.0\n" +
                "  Size: 338\n" +
                "  Data:\n" +
                "name test.txt\n" +
                "size 15\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();

        byte[] entryData = "mode 644\n".getBytes(StandardCharsets.UTF_8);
        arArchiveInputStream.read(entryData, 0, entryData.length);

        arArchiveInputStream.getNextArEntry();

        // Truncate the input stream to avoid reading the trailer
        inputStream.skip(16);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidTrailer() throws IOException {
        String header = "!<arch>\n" +
                "  Type: unix\n" +
                "  Version: 2.0\n" +
                "  Size: 338\n" +
                "  Data:\n" +
                "name test.txt\n" +
                "size 15\n";

        String trailer = "trailer\n" +
                "  Magic: ar\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(trailer.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();
        Assert.assertSame(arArchiveInputStream.getNextArEntry(), archiveEntry);

        archiveEntry = arArchiveInputStream.getNextEntry();
        Assert.assertNull(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithNullInput() {
        try {
            new ArArchiveInputStream(null);
        } catch (NullPointerException e) {
            throw new IOException("Unexpected exception", e);
        }
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        arArchiveInputStream.close();
        arArchiveInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);

        read = arArchiveInputStream.read();
        Assert.assertNotEquals(-1, read);

        arArchiveInputStream.mark(1);
        read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
        Assert.assertEquals(buffer[0], (byte) 0x72);
        arArchiveInputStream.reset();

        read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);

        read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertNotEquals(-1, read);

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(arArchiveEntry);

        read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);

        arArchiveInputStream.close();
    }

    @Test
    public void testReadWithLength() throws IOException {
        byte[] buffer = new byte[16];
        int read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);

        read = arArchiveInputStream.read(buffer, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertEquals(buffer[0], (byte) 0x72);

        read = arArchiveInputStream.read(buffer, 0, buffer.length - 1);
        Assert.assertEquals(15, read);

        arArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        Arrays.fill(signature, 0x3c);
        Assert.assertTrue(ArArchiveInputStream.matches(signature, 8));

        signature[0] = 0x3d;
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 8));

        signature[1] = 0x61;
        Assert.assertTrue(ArArchiveInputStream.matches(signature, 8));

        signature[2] = 0x73;
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 8));
    }
}