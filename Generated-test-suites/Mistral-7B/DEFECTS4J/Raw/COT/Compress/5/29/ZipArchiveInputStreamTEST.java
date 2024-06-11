import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.apache.commons.compress.utils.ZipShort;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.compress.utils.io.IOUtils.toByteArray;

public class ZipArchiveInputStreamTest {

    @Test
    public void testZipArchiveInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStreamConstructorWithEncoding() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF_8, true);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipEntry);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenEndOfStreamIsReached() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipEntry = null;
        while ((zipEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
            // Do nothing
        }

        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenStreamIsClosed() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();

        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWhenEndOfStreamIsReached() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = null;
        while ((archiveEntry = zipArchiveInputStream.getNextEntry()) != null) {
            // Do nothing
        }

        zipArchiveInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWhenStreamIsClosed() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();

        zipArchiveInputStream.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[256];

        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        byteArrayOutputStream.write(buffer, 0, read);

        zipArchiveInputStream.close();
        inputStream.close();

        byte[] expected = toByteArray(new File("test.bin"));
        Assert.assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testReadWhenEndOfStreamIsReached() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[256];

        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWhenStreamIsClosed() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();

        int read = zipArchiveInputStream.read(new byte[256], 0, 256);
        Assert.assertEquals(-1, read);

        inputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        long value = zipArchiveInputStream.skip(Long.MAX_VALUE);

        zipArchiveInputStream.close();
        inputStream.close();

        Assert.assertEquals(Long.MAX_VALUE, value);
    }

    @Test
    public void testSkipWhenNegativeValueIsPassed() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        long value = zipArchiveInputStream.skip(-1);
        Assert.assertEquals(-1, value);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testSkipWhenStreamIsClosed() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();

        zipArchiveInputStream.skip(1);
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        Arrays.fill(signature, (byte) 0x4b);
        Arrays.fill(signature, 4, signature.length, (byte) 0x50);

        Assert.assertFalse(ZipArchiveInputStream.matches(signature, 0));
        Assert.assertFalse(ZipArchiveInputStream.matches(signature, -1));
        Assert.assertFalse(ZipArchiveInputStream.matches(signature, signature.length + 1));

        byte[] eocdSignature = new byte[ZipArchiveOutputStream.EOCD_SIG.length];
        Arrays.fill(eocdSignature, (byte) 0x50);
        Arrays.fill(eocdSignature, 1, eocdSignature.length, (byte) 0x4d);
        Arrays.fill(eocdSignature, 5, eocdSignature.length, (byte) 0x44);
        Arrays.fill(eocdSignature, 6, eocdSignature.length, (byte) 0x0f);

        Assert.assertFalse(ZipArchiveInputStream.matches(signature, 0));
        Assert.assertFalse(ZipArchiveInputStream.matches(signature, -1));
        Assert.assertFalse(ZipArchiveInputStream.matches(signature, signature.length + 1));
        Assert.assertFalse(ZipArchiveInputStream.matches(signature, eocdSignature.length));
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, ZipArchiveOutputStream.LFH_SIG.length));
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, ZipArchiveOutputStream.LFH_SIG.length + eocdSignature.length));

        Assert.assertTrue(ZipArchiveInputStream.matches(eocdSignature, eocdSignature.length));
        Assert.assertFalse(ZipArchiveInputStream.matches(eocdSignature, -1));
        Assert.assertFalse(ZipArchiveInputStream.matches(eocdSignature, eocdSignature.length + 1));
        Assert.assertFalse(ZipArchiveInputStream.matches(eocdSignature, signature.length));
        Assert.assertFalse(ZipArchiveInputStream.matches(eocdSignature, ZipArchiveOutputStream.LFH_SIG.length));
    }
}