import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a});
        ArArchiveInputStream testee = new ArArchiveInputStream(input);
        Assert.assertFalse(testee.isClosed());
    }

    @Test
    public void testGetNextArEntryNormalCase() throws IOException {
        String expectedName = "test.txt";
        long expectedSize = 12345;
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, // magic number
                (byte) 0x61, (byte) 0x72, (byte) 0x68, (byte) 0x65, // name length
                (byte) 't', (byte) 'e', (byte) 'st', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // last modified
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // group id
                (byte) 0x78, (byte) 0x53, (byte) 0x34, (byte) 0x35, (byte) 0x0a, // file mode
                (byte) 0x3e, (byte) 0x0a, (byte) 0x00, (byte) 0x00, // length
                (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, // trailer
                (byte) 0x0a});

        ArArchiveInputStream testee = new ArArchiveInputStream(input);
        ArArchiveEntry actualEntry = testee.getNextArEntry();
        Assert.assertNotNull(actualEntry);
        Assert.assertEquals(expectedName, actualEntry.getName());
        Assert.assertEquals(expectedSize, actualEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryEndOfStreamReached() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, // magic number
                (byte) 0x61, (byte) 0x72, (byte) 0x68, (byte) 0x65, // name length
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // last modified
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // group id
                (byte) 0x78, (byte) 0x53, (byte) 0x34, (byte) 0x35, (byte) 0x0a, // file mode
                (byte) 0x3e, (byte) 0x0a, (byte) 0x00, (byte) 0x00, // length
                (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, // trailer
                (byte) 0x0a, (byte) 0x00});

        ArArchiveInputStream testee = new ArArchiveInputStream(input);
        ArArchiveEntry actualEntry = testee.getNextArEntry();
        ArArchiveEntry nextEntry = testee.getNextArEntry();
        Assert.assertNotNull(actualEntry);
        Assert.assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidEntryHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, // magic number
                (byte) 0x61, (byte) 0x72, (byte) 0x68, (byte) 0x65, // name length
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // last modified
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // group id
                (byte) 0x78, (byte) 0x53, (byte) 0x34, (byte) 0x35, (byte) 0x0a, // file mode
                (byte) 0x3e, (byte) 0x0a, (byte) 0x00, (byte) 0x00, // length
                (byte) 0x0a, (byte) 0x0a, (byte) 0x0a, (byte) 0x0a, // trailer
                (byte) 0x0a});

        ArArchiveInputStream testee = new ArArchiveInputStream(input);
        testee.getNextArEntry();
        testee.getNextArEntry();
        testee.getNextArEntry();
        testee.getNextArEntry(); // should throw an exception
    }

    @Test
    public void testGetNextEntryDelegatesToGetNextArEntry() throws IOException {
        String expectedName = "test.txt";
        long expectedSize = 12345;
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, // magic number
                (byte) 0x61, (byte) 0x72, (byte) 0x68, (byte) 0x65, // name length
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // last modified
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // group id
                (byte) 0x78, (byte) 0x53, (byte) 0x34, (byte) 0x35, (byte) 0x0a, // file mode
                (byte) 0x3e, (byte) 0x0a, (byte) 0x00, (byte) 0x00, // length
                (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, // trailer
                (byte) 0x0a});

        ArArchiveInputStream testee = new ArArchiveInputStream(input);
        ArchiveEntry actualEntry = testee.getNextEntry();
        ArArchiveEntry entry = (ArArchiveEntry) actualEntry;
        Assert.assertNotNull(entry);
        Assert.assertEquals(expectedName, entry.getName());
        Assert.assertEquals(expectedSize, entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, // magic number
                (byte) 0x61, (byte) 0x72, (byte) 0x68, (byte) 0x65, // name length
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // last modified
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // user id
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, // group id
                (byte) 0x78, (byte) 0x53, (byte) 0x34, (byte) 0x35, (byte) 0x0a, // file mode
                (byte) 0x3e, (byte) 0x0a, (byte) 0x00, (byte) 0x00, // length
                (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, (byte) 0x21, // trailer
                (byte) 0x0a});

        ArArchiveInputStream testee = new ArArchiveInputStream(input);
        testee.close();
        testee.getNextArEntry(); // should throw an exception
    }

    @Test
    public void testMatches() throws IOException {
        byte[] signature = new byte[8];
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 0));
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x30, (byte) 0x3e});
        Assert.assertTrue(ArArchiveInputStream.matches(input.readNBytes(8), 8));
    }
}