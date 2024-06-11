import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    public void setUp() throws IOException {
        byte[] zipFileSignature = new byte[]{"PK\u0003\u0004"};
        byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '\u0000'};
        byte[] testExtraField = new byte[]{'E', 'x', 'a', 'm', 'p', 'l', 'e', '\u0000'};

        outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream);
        zipOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipOutputStream.write(testData, 0, testData.length);
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        zipOutputStream.close();

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
        Assertions.assertNotNull(zipEntry);
        Assertions.assertEquals("test.txt", zipEntry.getName());
        zipEntry = zipInputStream.getNextZipEntry();
        Assertions.assertNull(zipEntry);

        zipInputStream.close();
        zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        zipEntry = zipInputStream.getNextZipEntry();
        Assertions.assertEquals("test.txt", zipEntry.getName());
        zipEntry = zipInputStream.getNextZipEntry();
        Assertions.assertNull(zipEntry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        int readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(-1, readBytes);

        byte[] expected = new byte[]{'T', 'e', 's', 't', '\u0000'};
        readBytes = zipInputStream.read(buffer, 0, expected.length);
        Assertions.assertEquals(expected.length, readBytes);
        Assertions.assertArrayEquals(expected, buffer);

        readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(-1, readBytes);

        zipInputStream.close();
        zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(-1, readBytes);
    }

    @Test
    public void testReadWithInflate() throws IOException {
        byte[] buffer = new byte[10];
        int readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(-1, readBytes);

        byte[] expected = new byte[]{'T', 'e', 's', 't', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'};
        readBytes = zipInputStream.read(buffer, 0, expected.length);
        Assertions.assertEquals(expected.length, readBytes);
        Assertions.assertArrayEquals(expected, buffer);

        zipInputStream.close();
        zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(-1, readBytes);
    }

    @Test
    public void testSkip() throws IOException {
        zipInputStream.skip(10);
        byte[] buffer = new byte[5];
        int readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(-1, readBytes);
    }

    @Test
    public void testMatchesSignature() {
        boolean result = ZipArchiveInputStream.matches(new byte[]{"PK\u0003\u0004"}, 8);
        Assertions.assertTrue(result);
    }

    @Test
    public void testDoesNotMatchSignature() {
        boolean result = ZipArchiveInputStream.matches(new byte[]{"PK\u0003\u0005"}, 8);
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetNameAndCommentFromExtraFields() throws IOException {
        byte[] extraData = new byte[8];
        System.arraycopy(testExtraField, 0, extraData, 0, testExtraField.length);

        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setExtra(extraData);
        zipEntry.setName("test.txt");

        String name = ZipUtil.getNameFromExtraFields(zipEntry, null);
        Assertions.assertEquals("test.txt", name);

        String comment = ZipUtil.getCommentFromExtraFields(zipEntry, null);
        Assertions.assertEquals("Example file", comment);
    }

    @Test
    public void testCloseEntry() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.closeEntry();
        Assertions.assertFalse(zipInputStream.current.isOpen());
    }

    @Test
    public void testFill() throws IOException {
        byte[] buffer = new byte[1024];
        zipInputStream.fill();
        Assertions.assertEquals(0, zipInputStream.read(buffer, 0, buffer.length));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] expected = new byte[]{'P', 'K', '\u0003', '\u0004', '\u0000', '\u0000', 'P', 'K'};
        byte[] buffer = new byte[expected.length];

        zipInputStream.readFully(buffer);
        Assertions.assertArrayEquals(expected, buffer);
    }
}