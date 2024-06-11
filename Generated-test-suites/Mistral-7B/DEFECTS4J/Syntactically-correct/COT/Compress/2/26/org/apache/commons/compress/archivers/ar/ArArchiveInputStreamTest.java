import org.apache.commons.compress.archivers.ArArchiveEntry;
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
    public void testGetNextArEntry_ValidEntry_ReturnsExpectedEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Ar\u0001\u0012name\u0000\u0000\u0010user\u0000\u0000group\u0000\u0006\u0002\u0012length\u0000\u0000\u0012\u0000\u0000\u0012\u0000\u0000\u0012\u0000\u0000\u0012\u0000\u0000\u0012",
                "\u03c2\u0011\u0072\u0061\u0068\u0063\u000a\u003e"});
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry expectedEntry = new ArArchiveEntry("name", 12L);
        ArArchiveEntry actualEntry = archiveInputStream.getNextArEntry();

        Assert.assertNotNull(actualEntry);
        Assert.assertEquals(expectedEntry.getName(), actualEntry.getName());
        Assert.assertEquals(expectedEntry.getSize(), actualEntry.getSize());

        archiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidEntry_ThrowsExpectedException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Ar\u0001\u0012name\u0000\u0000\u0010user\u0000\u0000group\u0000\u0006\u0002\u0012length\u0000\u0000\u0012\u0000\u0000\u0012\u0000\u0000\u0012\u0000\u0000\u0012\u0000\u0000\u0012",
                "\u03c2\u0011\u0072\u0061\u0068\u0063\u000a\u0012"}); // invalid trailer
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_EndOfStream_ReturnsNull() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        Assert.assertNull(entry);

        archiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_NoHeader_ThrowsException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testMatches_MatchingSignature_ReturnsTrue() {
        byte[] signature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};

        Assert.assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatches_NonMatchingSignature_ReturnsFalse() {
        byte[] signature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3f};

        Assert.assertFalse(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testRead_ReadSingleByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        int actual = archiveInputStream.read();
        int expected = 'A';

        Assert.assertEquals(expected, actual);

        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_ReadMultipleBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Hello".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] expected = "Hello".getBytes();
        byte[] actual = new byte[expected.length];
        int length = archiveInputStream.read(actual);

        Assert.assertEquals(expected.length, length);
        Assert.assertArrayEquals(expected, actual);

        archiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testRead_NoBytesAvailable_ThrowsException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[1];
        archiveInputStream.read(buffer);
        inputStream.close();
        archiveInputStream.close();
        outputStream.close();

        archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.read(buffer);
    }
}