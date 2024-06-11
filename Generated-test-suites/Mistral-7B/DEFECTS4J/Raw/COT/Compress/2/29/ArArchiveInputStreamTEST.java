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
import java.nio.charset.StandardCharsets;

/**
 * Test class for ArArchiveInputStream
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        String header = "!<arch>\n" +
                "  Type: UNIX\n" +
                "  Version: 2.0\n" +
                "  Size: 33\n" +
                "  Name: test.txt\n" +
                "  Size: 10\n" +
                "  Flags: \n" +
                "  Uid: 0\n" +
                "  Gid: 0\n" +
                "  Mode: 644\n" +
                "  Mtime: 12345\n" +
                "  Mag: 2\n" +
                "!<eoh>\n" +
                "test data";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.UTF_8));
        inputStream = new ByteArrayInputStream(header.getBytes(StandardCharsets.UTF_8));
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(10, arArchiveEntry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", archiveEntry.getName());
        Assert.assertEquals(10, archiveEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterClosed() throws IOException {
        arArchiveInputStream.close();
        arArchiveInputStream.getNextEntry();
    }

    @Test
    public void testClose() throws IOException {
        arArchiveInputStream.close();
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(arArchiveEntry);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x3C, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0A, 0x3E};
        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Invalid header".getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithEmptyInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

}