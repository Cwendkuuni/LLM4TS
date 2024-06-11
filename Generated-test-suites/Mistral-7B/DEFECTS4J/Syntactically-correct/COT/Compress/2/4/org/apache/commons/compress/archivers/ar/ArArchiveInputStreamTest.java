import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0061\u0072\u0063AR\u0068\u0061\u003e"
                + "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                + "\u0000\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"
                + "\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"}.toString().getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(arArchiveEntry);
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(12, arArchiveEntry.getSize());

        arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(arArchiveEntry);

        arArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidSignature() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0061\u0072\u0063AR\u0068\u0061\u003e"
                + "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                + "\u0000\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"
                + "\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0001"}.toString().getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0061\u0072\u0063AR\u0068\u0061\u003e"
                + "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                + "\u0000\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"
                + "\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"}.toString().getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        Assert.assertEquals(ArArchiveEntry.class, entry.getClass());
        ArArchiveEntry arArchiveEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(12, arArchiveEntry.getSize());

        entry = arArchiveInputStream.getNextEntry();
        Assert.assertNull(entry);

        arArchiveInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0061\u0072\u0063AR\u0068\u0061\u003e"
                + "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                + "\u0000\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"
                + "\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"}.toString().getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        Assert.assertEquals(ArArchiveEntry.class, entry.getClass());
        ArArchiveEntry arArchiveEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(12, arArchiveEntry.getSize());

        arArchiveInputStream.close();

        try {
            entry = arArchiveInputStream.getNextEntry();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testRead() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{"\u003c\u0021AR\u0061\u0072\u0063AR\u0068\u0061\u003e"
                + "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                + "\u0000\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"
                + "\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"}.toString().getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read();
        Assert.assertEquals(-1, read);

        byte[] buffer = new byte[5];
        read = arArchiveInputStream.read(buffer);
        Assert.assertEquals(0, read);

        buffer = new byte[]{'A', 'B', 'C', 'D', 'E'};
        read = arArchiveInputStream.read(buffer);
        Assert.assertEquals(5, read);
        Assert.assertArrayEquals(buffer, new byte[]{'A', 'B', 'C', 'D', 'E'});

        arArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(ArArchiveInputStream.matches(new byte[]{"\u003c\u0021AR\u0061\u0072\u0063AR\u0068\u0061\u003e"
                + "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
                + "\u0000\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"
                + "\u0000\u0012test.txt\u0000\u0000\u0000\u0002\u0000\u0014\u0010\u0000\u0000"}.toString().getBytes(), 28));

        Assert.assertFalse(ArArchiveInputStream.matches(new byte[]{"\u003c\u0021AR\u0061\u0072"}.toString().getBytes(), 7));
    }
}