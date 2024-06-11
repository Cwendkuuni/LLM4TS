import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private static final String TEST_STRING = "This is a test string";
    private static final String TEST_FILE_NAME = "test.txt";

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_STRING.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
        ArchiveEntry entry = ais.getNextEntry();
        assertNotNull(entry);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertTrue(ais.canReadEntryData(entry));

        try {
            ArchiveInputStream invalidAIS = factory.createArchiveInputStream("unknown", in);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(aos);
        ArchiveEntry entry = aos.createArchiveEntry(null, TEST_FILE_NAME);
        assertNotNull(entry);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertEquals(0L, entry.getSize());

        try {
            ArchiveOutputStream invalidAOS = factory.createArchiveOutputStream("unknown", out);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_STRING.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
        ArchiveEntry entry = ais.getNextEntry();
        assertNotNull(entry);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertTrue(ais.canReadEntryData(entry));

        in.reset();
        byte[] buffer = new byte[TEST_STRING.length()];
        int bytesRead = ais.read(buffer);
        assertEquals(TEST_STRING.length(), bytesRead);
        assertEquals(TEST_STRING, new String(buffer));

        in.reset();
        ArchiveInputStream invalidAIS = factory.createArchiveInputStream(in);
        assertNull(invalidAIS);
    }

}