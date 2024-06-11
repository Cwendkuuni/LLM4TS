import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    private static final String VALID_ZIP_FILE = "validZipFile.zip";
    private static final String INVALID_ZIP_FILE = "invalidZipFile.zip";

    /**
     * Test for reading valid entries from a Zip file using the default encoding.
     */
    @Test
    public void testReadValidEntriesWithDefaultEncoding() throws IOException {
        try (ZipFile zipFile = new ZipFile(VALID_ZIP_FILE)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("validEntry.txt")));
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("validEntry.txt", entry.getName());
            Assert.assertEquals("UTF-8", entry.getEncoding());
            String expectedContent = "This is the content of a valid entry.";
            byte[] buffer = new byte[1024];
            int bytesRead;
            String actualContent = "";
            while ((bytesRead = zipArchiveInputStream.read(buffer)) > 0) {
                actualContent += new String(buffer, 0, bytesRead);
            }
            Assert.assertEquals(expectedContent, actualContent);
            zipArchiveInputStream.close();
        }
    }

    /**
     * Test for reading valid entries from a Zip file using a custom encoding.
     */
    @Test
    public void testReadValidEntriesWithCustomEncoding() throws IOException {
        try (ZipFile zipFile = new ZipFile(VALID_ZIP_FILE)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("validEntry.txt")));
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("validEntry.txt", entry.getName());
            ZipEncoding customEncoding = ZipEncodingHelper.getZipEncoding("UTF-16BE");
            ZipArchiveInputStream zipArchiveInputStreamWithCustomEncoding = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("validEntry.txt")), null, customEncoding);
            String expectedContent = "This is the content of a valid entry.";
            byte[] buffer = new byte[1024];
            int bytesRead;
            String actualContent = "";
            while ((bytesRead = zipArchiveInputStreamWithCustomEncoding.read(buffer)) > 0) {
                actualContent += new String(buffer, 0, bytesRead, "UTF-16BE");
            }
            Assert.assertEquals(expectedContent, actualContent);
            zipArchiveInputStreamWithCustomEncoding.close();
            zipArchiveInputStream.close();
        }
    }

    /**
     * Test for reading an empty Zip file.
     */
    @Test
    public void testReadEmptyZipFile() throws IOException {
        try (ZipFile zipFile = new ZipFile(INVALID_ZIP_FILE)) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("emptyEntry.txt")));
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNull(entry);
            Assert.assertFalse(zipArchiveInputStream.read(new byte[1]) > 0);
            zipArchiveInputStream.close();
        }
    }

    /**
     * Test for reading an invalid Zip file.
     */
    @Test(expected = IOException.class)
    public void testReadInvalidZipFile() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write("PK\u0005\u0001\u0001\u0000\u0000LOCALHEAD\u0000local file header signature".getBytes("UTF-8"));
            outputStream.write("version needed to extract\u0000\u0000\u0000\u0000last mod file time".getBytes("UTF-8"));
            outputStream.write("\u0000\u0000crc-32\u0000compressed size\u0000uncompressed size".getBytes("UTF-8"));
            outputStream.write("\u0000file name length".getBytes("UTF-8"));
            outputStream.write("\u0000".getBytes("UTF-8"));
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                new ZipArchiveInputStream(inputStream);
            }
        }
    }
}