import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ZipArchiveInputStreamTest {

    private InputStream inputStream;
    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() throws Exception {
        byte[] testData = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4B, (byte) 0x50,
                (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                (byte) 0x00, (byte) 0x03, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x08,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 'Z', (byte) 'i', (byte) 'p', (byte) ' ',
                (byte) 'A', (byte) 'r', (byte) 'c', (byte) 'h', (byte) 'i', (byte) 'v', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'z',
                (byte) 'i', (byte) 'p'};

        inputStream = new ByteArrayInputStream(testData);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (zipArchiveInputStream != null) {
            zipArchiveInputStream.close();
        }
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testConstructor() {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_EmptyFile() throws Exception {
        assertFalse(zipArchiveInputStream.getNextZipEntry() == null);
    }

    @Test
    public void testGetNextZipEntry_InvalidSignature() throws Exception {
        byte[] invalidSignature = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4B, (byte) 0x51,
                (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                (byte) 0x00, (byte) 0x03, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x08,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 'Z', (byte) 'i', (byte) 'p', (byte) ' ',
                (byte) 'A', (byte) 'r', (byte) 'c', (byte) 'h', (byte) 'i', (byte) 'v', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'z',
                (byte) 'i', (byte) 'p'};

        inputStream = new ByteArrayInputStream(invalidSignature);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_CentralDirectory() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[0]);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        ZipArchiveEntry centralDirectory = zipArchiveInputStream.getNextZipEntry();
        assertNull(centralDirectory);
    }

    @Test
    public void testGetNextEntry() throws Exception {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("test.zip", entry.getName());
        entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("", entry.getName());
        assertFalse(zipArchiveInputStream.getNextZipEntry() == null);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_IOException() throws Exception {
        byte[] testData = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4B, (byte) 0x50,
                (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                (byte) 0x00, (byte) 0x03, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x08,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 'Z', (byte) 'i', (byte) 'p', (byte) ' ',
                (byte) 'A', (byte) 'r', (byte) 'c', (byte) 'h', (byte) 'i', (byte) 'v', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'z',
                (byte) 'i', (byte) 'p'};

        inputStream = new ByteArrayInputStream(testData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();

        byte[] testData2 = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4B, (byte) 0x50,
                (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                (byte) 0x00, (byte) 0x03, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x08,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 'Z', (byte) 'i', (byte) 'p', (byte) ' ',
                (byte) 'A', (byte) 'r', (byte) 'c', (byte) 'h', (byte) 'i', (byte) 'v', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'z',
                (byte) 'i', (byte) 'p', (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                (byte) 0x00, (byte) 0x03, (byte) 0x04, (byte) 0x04, (byte) 0x04, (byte) 0x08,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 'Z', (byte) 'i', (byte) 'p', (byte) ' ',
                (byte) 'A', (byte) 'r', (byte) 'c', (byte) 'h', (byte) 'i', (byte) 'v', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'z',
                (byte) 'i', (byte) 'p'};

        inputStream = new ByteArrayInputStream(testData2);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();

        assertThrows(IOException.class, () -> zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testRead() throws Exception {
        byte[] buffer = new byte[10];
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(3, read);
        assertEquals((byte) 0x04, buffer[0]);
        assertEquals((byte) 0x03, buffer[1]);
        assertEquals((byte) 0x4B, buffer[2]);

        read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(4, read);
        assertEquals((byte) 0x50, buffer[0]);
        assertEquals((byte) 0x00, buffer[1]);
        assertEquals((byte) 0x02, buffer[2]);
        assertEquals((byte) 0x01, buffer[3]);

        read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(1, read);
        assertEquals((byte) 0x01, buffer[0]);

        read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    @Test
    public void testClose() throws Exception {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
        assertNull(entry);
    }

    @Test
    public void testCloseEntry() throws Exception {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        assertNull(entry);
    }
}