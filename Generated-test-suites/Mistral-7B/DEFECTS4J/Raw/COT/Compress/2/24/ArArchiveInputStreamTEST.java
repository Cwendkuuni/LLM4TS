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
    public void testGetNextArEntry() throws IOException {
        byte[] inputData = new byte[] {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, // AR header
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // entry name "abcdefg.txt"
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0b, // last modified
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, // user id
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, // group id
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // file mode
                0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x0f, // entry length
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, // AR header
                0x02, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // entry name "defghijk.txt"
                0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x11, // last modified
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x10, // user id
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x10, // group id
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // file mode
                0x00, 0x00, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x12, // entry length
                0x3c, 0x3e // AR trailer
        };

        InputStream inputStream = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry1 = arArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry1);
        Assert.assertTrue(entry1 instanceof ArArchiveEntry);
        ArArchiveEntry arEntry1 = (ArArchiveEntry) entry1;
        Assert.assertEquals("abcdefg.txt", arEntry1.getName());
        Assert.assertEquals(15L, arEntry1.getSize());

        ArchiveEntry entry2 = arArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry2);
        Assert.assertTrue(entry2 instanceof ArArchiveEntry);
        ArArchiveEntry arEntry2 = (ArArchiveEntry) entry2;
        Assert.assertEquals("defghijk.txt", arEntry2.getName());
        Assert.assertEquals(13L, arEntry2.getSize());

        ArchiveEntry entry3 = arArchiveInputStream.getNextEntry();
        Assert.assertNull(entry3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNextArEntryWithNullInput() {
        new ArArchiveInputStream(null);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidInput() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(0x21);
        output.write(0x3c);
        output.write(0x61);
        output.write(0x72);
        output.write(0x63);
        output.write(0x68);
        output.write(0x3e);
        output.write(0x0a);
        output.write(0x21);
        output.write(0x3e);
        output.write(0x3c);
        output.write(0x61);
        output.write(0x72);
        output.write(0x63);
        output.write(0x68);
        output.write(0x3e);
        output.write(0x0a);
        InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        new ArArchiveInputStream(inputStream);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        byte[] inputData = new byte[] {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, // AR header
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // entry name "abcdefg.txt"
                0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x0f, // last modified
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, // user id
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, // group id
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, // file mode
                0x00, 0x00, 0x00, 0x0c, 0x01, 0x00, 0x00, 0x0f, // entry length
                0x3c, 0x3e // AR trailer
        };

        InputStream inputStream = new ByteArrayInputStream(inputData);
        new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidLength() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        Assert.assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }
}