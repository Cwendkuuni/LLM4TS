import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {
    @Mock
    private InputStream mockInputStream;
    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        zipArchiveInputStream = new ZipArchiveInputStream(mockInputStream);
        assertEquals(mockInputStream, zipArchiveInputStream.in);
    }

    @Test
    public void testConstructorWithEncoding() {
        zipArchiveInputStream = new ZipArchiveInputStream(mockInputStream, "UTF-8", true);
        assertEquals(mockInputStream, zipArchiveInputStream.in);
        assertEquals("UTF-8", zipArchiveInputStream.zipEncoding.getEncoding());
        assertEquals(true, zipArchiveInputStream.useUnicodeExtraFields);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] lfh = new byte[ZipArchiveInputStream.LFH_LEN];
        lfh[0] = 0x04;
        lfh[1] = 0x03;
        lfh[2] = 0x4b;
        lfh[3] = 0x50;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(lfh);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals(0x04, entry.getMethod());
        assertEquals(0x00, entry.getPlatform());
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] lfh = new byte[ZipArchiveInputStream.LFH_LEN];
        lfh[0] = 0x04;
        lfh[1] = 0x03;
        lfh[2] = 0x4b;
        lfh[3] = 0x50;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(lfh);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertEquals(0x04, entry.getMethod());
        assertEquals(0x00, entry.getPlatform());
        assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(10, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        zipArchiveInputStream.close();
        assertEquals(true, zipArchiveInputStream.closed);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        long bytesSkipped = zipArchiveInputStream.skip(5);
        assertEquals(5, bytesSkipped);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadWithInvalidStart() throws IOException {
        byte[] buffer = new byte[10];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        zipArchiveInputStream.read(buffer, -1, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(new byte[10], 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        zipArchiveInputStream.skip(-1);
    }

    @Test
    public void testMatchesLFH() {
        byte[] signature = new byte[4];
        signature[0] = 0x04;
        signature[1] = 0x03;
        signature[2] = 0x4b;
        signature[3] = 0x50;
        assertEquals(true, ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesEOCD() {
        byte[] signature = new byte[4];
        signature[0] = 0x50;
        signature[1] = 0x4b;
        signature[2] = 0x05;
        signature[3] = 0x06;
        assertEquals(true, ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchFalse() {
        byte[] signature = new byte[4];
        signature[0] = 0x04;
        signature[1] = 0x03;
        signature[2] = 0x4b;
        signature[3] = 0x07;
        assertEquals(false, ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test(expected = EOFException.class)
    public void testReadFully() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
        zipArchiveInputStream.readFully(new byte[20]);
    }
}