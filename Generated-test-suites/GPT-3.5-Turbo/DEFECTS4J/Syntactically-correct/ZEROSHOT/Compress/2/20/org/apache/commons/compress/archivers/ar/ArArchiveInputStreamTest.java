import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        arArchiveInputStream.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(arArchiveEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();
        Assert.assertNull(archiveEntry);
    }

    @Test
    public void testClose() throws IOException {
        arArchiveInputStream.close();
        Assert.assertTrue(arArchiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        int read = arArchiveInputStream.read();
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] bytes = new byte[10];
        int read = arArchiveInputStream.read(bytes);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] bytes = new byte[10];
        int read = arArchiveInputStream.read(bytes, 0, 10);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = 8;
        boolean result = ArArchiveInputStream.matches(signature, length);
        Assert.assertTrue(result);
    }

}