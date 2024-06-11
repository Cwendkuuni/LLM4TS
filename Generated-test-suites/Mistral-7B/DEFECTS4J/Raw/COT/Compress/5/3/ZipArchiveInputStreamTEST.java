import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryNormalCase() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("asdf.txt", zipArchiveEntry.getName());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryNoEntry() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010test.zip\000\000\000\004text\000".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryMalformed() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\005".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testGetNextZipEntryEndOfStream() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testReadNormalCase() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        byte[] buffer = new byte[5];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(5, length);
        Assert.assertArrayEquals(new byte[]{"a", "s", "d", "f", '\0'}, buffer);
        zipArchiveInputStream.close();
    }

    @Test
    public void testReadEndOfStream() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        byte[] buffer = new byte[5];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(5, length);
        Assert.assertArrayEquals(new byte[]{"a", "s", "d", "f", '\0'}, buffer);
        length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadClosedStream() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        byte[] buffer = new byte[5];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(5, length);
        Assert.assertArrayEquals(new byte[]{"a", "s", "d", "f", '\0'}, buffer);
        zipArchiveInputStream.close();
        length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
        zipArchiveInputStream.read(buffer);
    }

    @Test
    public void testClose() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        zipArchiveInputStream.close();
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testReadUnsupportedMethod() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\004\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        byte[] buffer = new byte[5];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(5, length);
        zipArchiveInputStream.close();
        length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, length);
    }

    @Test
    public void testSkip() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        zipArchiveInputStream.skip(10);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{"PK\003\012".getBytes()};
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000".getBytes()};
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, fileContent.length));
    }

    @Test
    public void testMatchesEmptyZip() {
        byte[] signature = new byte[]{"PK\003\012".getBytes()};
        byte[] fileContent = new byte[]{"PK\003\012\000\000\001\000\000\000\000".getBytes()};
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, fileContent.length));
    }

    @Test
    public void testFill() throws Exception {
        byte[] fileContent = new byte[1024];
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(fileContent), null, false);
        zipArchiveInputStream.fill();
        zipArchiveInputStream.read(buffer);
        byte[] expected = new byte[1024];
        System.arraycopy(fileContent, 0, expected, 0, fileContent.length);
        Assert.assertArrayEquals(expected, buffer);
        zipArchiveInputStream.close();
    }

    @Test(expected = ZipException.class)
    public void testInflateDataDescriptorError() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.inf.reset();
        zipArchiveInputStream.readFully(new byte[1]);
        zipArchiveInputStream.close();
    }

    @Test(expected = ZipException.class)
    public void testInflateDataDescriptorEof() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.inf.reset();
        zipArchiveInputStream.readFully(new byte[1]);
        zipArchiveInputStream.inf.end();
        zipArchiveInputStream.close();
    }

    @Test(expected = ZipException.class)
    public void testInflateDataDescriptortooMuchData() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\010text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.inf.reset();
        zipArchiveInputStream.readFully(new byte[1]);
        zipArchiveInputStream.inf.inflate(new byte[1024 * 10]);
        zipArchiveInputStream.close();
    }

    @Test
    public void testCloseEntry() throws Exception {
        byte[] fileContent = new byte[]{"PK\003\012\001\002\001\000\020\000\014\000\000\010asdf.txt\000\000\000\004text\000asdf".getBytes()};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveInputStream.closeEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }
}