import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class ZipArchiveInputStreamTest {
    private static final byte[] TEST_FILE_CONTENTS = new byte[10];
    private ZipArchiveInputStream zipIn;

    @Before
    public void setup() throws IOException {
        zipIn = new ZipArchiveInputStream(new ByteArrayInputStream(TEST_FILE_CONTENTS));
    }

    @After
    public void tearDown() throws IOException {
        zipIn.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry1 = zipIn.getNextZipEntry();
        assertNotNull(entry1);
        assertEquals(0, entry1.getSize());
        assertEquals(0, entry1.getCompressedSize());
        assertEquals(0, entry1.getCrc());

        ZipArchiveEntry entry2 = zipIn.getNextZipEntry();
        assertNull(entry2);
    }

    @Test
    public void testRead() throws IOException {
        byte [] buffer = new byte[10];
        int readBytes = zipIn.read(buffer, 0, 10);
        assertEquals(10, readBytes);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        zipIn.close();
        zipIn.getNextZipEntry();
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = zipIn.skip(5);
        assertEquals(5, skipped);
    }

    @Test
    public void testMatches() {
        byte[] emptyZipSignature = { 0x50, 0x4B, 0x05, 0x06 };
        byte[] normalFileSignature = { 0x50, 0x4B, 0x03, 0x04 };
        assertTrue(ZipArchiveInputStream.matches(emptyZipSignature, 4));
        assertTrue(ZipArchiveInputStream.matches(normalFileSignature, 4));
    }
}