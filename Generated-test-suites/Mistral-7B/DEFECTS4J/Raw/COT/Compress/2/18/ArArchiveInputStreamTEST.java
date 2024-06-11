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
import java.util.ArrayList;
import java.util.List;

public class ArArchiveInputStreamTest {

    private InputStream input;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        String testData = "AR\n" +
                " 0001\n" +
                " MyTestFile.txt\n" +
                "                                                   \n" +
                " 0123456789\n" +
                " 0331\n" +
                " owner\n" +
                " group\n" +
                " 0644\n" +
                " 12345\n" +
                " 0000\n" +
                " 0000\n" +
                " 0000\n" +
                " 0000\n" +
                " 0000\n" +
                " AR\n" +
                " 0002\n" +
                " TestAnotherFile.txt\n" +
                "                                                   \n" +
                " 0987654321\n" +
                " 0332\n" +
                " owner2\n" +
                " group2\n" +
                " 0644\n" +
                " 56789\n" +
                " 0000\n" +
                " 0000\n" +
                " 0000\n" +
                " 0000\n" +
                " AR\n";

        input = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        archiveInputStream = new ArArchiveInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry expectedEntry1 = new ArArchiveEntry("MyTestFile.txt", 12345);
        ArArchiveEntry actualEntry1 = archiveInputStream.getNextArEntry();
        Assert.assertEquals(expectedEntry1, actualEntry1);

        ArArchiveEntry expectedEntry2 = new ArArchiveEntry("TestAnotherFile.txt", 56789);
        ArArchiveEntry actualEntry2 = archiveInputStream.getNextArEntry();
        Assert.assertEquals(expectedEntry2, actualEntry2);

        ArArchiveEntry nullEntry = archiveInputStream.getNextArEntry();
        Assert.assertNull(nullEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        input = new ByteArrayInputStream(new byte[16]); // Truncate the header
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        archiveInputStream.getNextArEntry(); // Should throw an exception
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_UnexpectedEndOfStream() throws IOException {
        input = new ByteArrayInputStream(new byte[0]); // Empty input stream
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        archiveInputStream.getNextArEntry(); // Should throw an exception
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry expectedEntry1 = new ArArchiveEntry("MyTestFile.txt", 12345);
        ArchiveEntry actualEntry1 = archiveInputStream.getNextEntry();
        Assert.assertEquals(expectedEntry1, actualEntry1);

        ArchiveEntry expectedEntry2 = new ArArchiveEntry("TestAnotherFile.txt", 56789);
        ArchiveEntry actualEntry2 = archiveInputStream.getNextEntry();
        Assert.assertEquals(expectedEntry2, actualEntry2);

        ArchiveEntry nullEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(nullEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_InvalidHeader() throws IOException {
        input = new ByteArrayInputStream(new byte[16]); // Truncate the header
        ArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        archiveInputStream.getNextEntry(); // Should throw an exception
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_UnexpectedEndOfStream() throws IOException {
        input = new ByteArrayInputStream(new byte[0]); // Empty input stream
        ArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        archiveInputStream.getNextEntry(); // Should throw an exception
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int read = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
    }

    @Test
    public void testRead_BufferFilled() throws IOException {
        byte[] buffer = new byte[5];
        int read = archiveInputStream.read(buffer, 0, buffer.length);
        List<Byte> expectedBytes = new ArrayList<>();
        expectedBytes.add((byte) 0x3c);
        expectedBytes.add((byte) 0x61);
        expectedBytes.add((byte) 0x72);
        expectedBytes.add((byte) 0x63);
        expectedBytes.add((byte) 0x68);
        for (int i = 0; i < read; i++) {
            Assert.assertEquals(expectedBytes.get(i), buffer[i]);
        }
    }

    @Test
    public void testRead_BufferNotFilled() throws IOException {
        byte[] buffer = new byte[5];
        int read = archiveInputStream.read(buffer, 0, 3);
        List<Byte> expectedBytes = new ArrayList<>();
        expectedBytes.add((byte) 0x3c);
        expectedBytes.add((byte) 0x61);
        expectedBytes.add((byte) 0x72);
        for (int i = 0; i < read; i++) {
            Assert.assertEquals(expectedBytes.get(i), buffer[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testRead_UnexpectedEndOfStream() throws IOException {
        byte[] buffer = new byte[1];
        archiveInputStream.read(buffer, 0, buffer.length); // Consume one byte
        archiveInputStream.read(buffer, 0, buffer.length); // Should throw an exception
    }

    @Test
    public void testMatches() {
        byte[] signature1 = new byte[8];
        byte[] signature2 = new byte[16];
        Assert.assertTrue(ArArchiveInputStream.matches(signature1, 8));
        Assert.assertFalse(ArArchiveInputStream.matches(signature2, 16));
    }
}