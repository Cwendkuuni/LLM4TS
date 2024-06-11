import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_NormalCase() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArHeaderSzArNameSzArTimeSzArUserIdSzArGidSzArPermsSzArSizeSzArTrailer".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry nextArEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(nextArEntry);

        inputStream.reset(); // reset the input stream

        byte[] header = "ArArchiveHeaderSzArName:test.txtSzArTime:1234567890123 SzArUserId:user SzArGid:group SzArPerms:rwxrwxrwx SzArSize:1024 SzArTrailer".getBytes();
        inputStream.read(header, 0, header.length);
        nextArEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(nextArEntry);
        Assert.assertEquals("test.txt", nextArEntry.getName());
        Assert.assertEquals(1024L, nextArEntry.getSize());

        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArHeaderSzArNameSzArTimeSzArUserIdSzArGidSzArPermsSzArSizeSzArTrailerInvalid".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = ArFormatException.class)
    public void testGetNextArEntry_InvalidEntryData() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArName:test.txtSzArTime:1234567890123 SzArUserId:user SzArGid:group SzArPerms:rwxrwxrwx SzArSize: invalid SzArTrailer".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_EmptyInput() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry nextArEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(nextArEntry);
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testGetNextArEntry_NoMoreEntries() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArHeaderSz".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry nextArEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNull(nextArEntry);
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArName:test.txtSzArTime:1234567890123 SzArUserId:user SzArGid:group SzArPerms:rwxrwxrwx SzArSize:1024 SzArTrailer".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        Assert.assertEquals(new ArArchiveEntry("test.txt", 1024L), nextEntry);
        arArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_InvalidEntryData() throws IOException, ArFormatException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArName:test.txtSzArTime:1234567890123 SzArUserId:user SzArGid:group SzArPerms:rwxrwxrwx SzArSize: invalid SzArTrailer".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextEntry();
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("ArArchiveHeaderSzArHeaderSz".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.close();

        InputStream testInput = arArchiveInputStream.input;
        Assert.assertTrue(testInput.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[10];

        InputStream inputStream = new ByteArrayInputStream("abcdefghij".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(10, read);
        Assert.assertArrayEquals("abcdefghij".getBytes(), readData);

        arArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testRead_NegativeOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[10];

        InputStream inputStream = new ByteArrayInputStream("abcdefghij".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read(readData, -5, readData.length);
        Assert.assertEquals(-1, read);

        arArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testRead_ZeroLengthArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[0];

        InputStream inputStream = new ByteArrayInputStream("abcdefghij".getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read(readData);
        Assert.assertEquals(-1, read);

        arArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testRead_EmptyInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[10];

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read(readData);
        Assert.assertEquals(-1, read);

        arArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }
}