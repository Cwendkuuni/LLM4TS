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
import java.util.ByteArrayInputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testArArchiveInputStreamConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        Assert.assertFalse(arArchiveInputStream.isClosed());
    }

    @Test
    public void testGetNextArEntryNormalCase() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                0x63, 0x72, 0x61, 0x73, 0x74, 0x08, 0x74, 0x79, 0x70, 0x65, 0x00, 0x00, 0x00, 0x00,
                0x3c, 0x63, 0x61, 0x6e, 0x6e, 0x65, 0x64, 0x0a, 0x63, 0x72, 0x61, 0x73, 0x74, 0x08, 0x73, 0x79, 0x73, 0x74, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x61, 0x74, 0x74, 0x54, 0x72, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x00, 0x00, 0x00, 0x00, 0x3e, 0x0a, 0x61, 0x74, 0x74, 0x54, 0x65, 0x78, 0x74, 0x2d, 0x61, 0x74, 0x74, 0x08, 0x65, 0x78, 0x74, 0x2d, 0x68, 0x6d, 0x6f, 0x72, 0x79, 0x00, 0x00, 0x00, 0x0a, 0x65, 0x78, 0x74, 0x2d, 0x74, 0x6f, 0x53, 0x75, 0x72, 0x63, 0x68, 0x41, 0x6d, 0x69, 0x64, 0x6f, 0x6e, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x64, 0x65, 0x76, 0x65, 0x72, 0x69, 0x61, 0x74, 0x65, 0x02, 0x00, 0x00, 0x00, 0x00, 0x3e, 0x0a});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(arArchiveEntry);
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(10, arArchiveEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry(); // this should throw an IOException
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.getNextArEntry(); // this should read the valid header and continue
        arArchiveInputStream.getNextArEntry(); // this should throw an IOException
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test(expected = IOException.class)
    public void testMatchesInvalidLength() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        Assert.assertFalse(ArArchiveInputStream.matches(signature, signature.length + 1));
    }

    @Test(expected = IOException.class)
    public void testMatchesInvalidSignature() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x64, 0x3e, 0x0a};
        Assert.assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.close();
        arArchiveInputStream.getNextArEntry(); // this should throw an IOException
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        int result = arArchiveInputStream.read();
        Assert.assertEquals(0x41, result);
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        int len = arArchiveInputStream.read(output.toByteArray(), 0, output.size());
        Assert.assertEquals(3, len);
        Assert.assertEquals(0x41, output.toByteArray()[0]);
        Assert.assertEquals(0x42, output.toByteArray()[1]);
        Assert.assertEquals(0x43, output.toByteArray()[2]);
    }
}