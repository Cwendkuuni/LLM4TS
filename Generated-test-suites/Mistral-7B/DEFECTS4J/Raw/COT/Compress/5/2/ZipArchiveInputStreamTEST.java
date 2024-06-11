import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertNull(inputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_NormalFile() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_EOF() throws IOException {
        String testFile = "TestFile.zip";
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_InvalidSignature() throws IOException {
        byte[] signature = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signature);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_CentralDirectory() throws IOException {
        String testFile = "TestFile.zip";
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testRead_ClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        int bytesRead = zipArchiveInputStream.read(new byte[1]);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_EmptyFile() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_DataDescriptorPresent() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertGreaterThan(bytesRead, 0);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testRead_InvalidCompressionMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[] {0x00, 0x00, 0x08, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        outputStream.write(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1];
        int bytesRead = zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_Stored() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertGreaterThan(bytesRead, 0);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_Deflated() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        InfTestUtil.setInflaterToDeflate(zipArchiveInputStream.inf);
        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertGreaterThan(bytesRead, 0);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = DataFormatException.class)
    public void testRead_DataFormatException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[] {0x00, 0x00, 0x08, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF};
        outputStream.write(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1];
        int bytesRead = zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = ZipException.class)
    public void testRead_ZipException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[] {0x00, 0x00, 0x08, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        outputStream.write(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1];
        int bytesRead = zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testRead_EOF() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        String testFile = "TestFile.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        long value = 100L;
        long bytesSkipped = zipArchiveInputStream.skip(value);
        Assert.assertEquals(value, bytesSkipped);
        zipArchiveInputStream.close();
        inputStream.close();
    }
}

class ZipFileTestUtil {
    public static InputStream getInputStreamForTest(String fileName) throws IOException {
        return ZipFile.open(fileName);
    }
}

class InfTestUtil {
    public static void setInflaterToDeflate(Inflater inflater) {
        inflater.setInput(new ByteArrayInputStream(new byte[] {0x78, 0x01, 0x00}));
    }
}