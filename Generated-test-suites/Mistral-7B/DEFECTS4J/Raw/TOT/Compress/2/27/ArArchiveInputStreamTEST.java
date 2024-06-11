import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.archiveentry.GenericArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        String archiveContent = "00000003c21 7261 6863 0a3e 410000000000 000000006261 636f6d79 7a62 6f6f74202020 " +
                "53657276657273696f6e3e 53696e676e65742061737420696e666f 00000000140000210000 " +
                "0000000000010000001e 303132333435 303132333435 00000200000000000020 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "536f6e66657273696f6e6700000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "417574686572656e000000000000000000000000140000210000 0000000000010000001e " +
                "3132333435303132333435 3132333435303132333435 00000200000000000020 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "417574696e66657273696f6e670000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "537461626c65617374617274000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "537461626c65617374617274000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "4275617574656e7452617374100000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "537461626c65617374617274000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "537461626c65617374617274000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000 " +
                "000000000000000000000000000000000000000000000000000000000000000000000000" +
                "\n";

        inputStream = new ByteArrayInputStream(archiveContent.getBytes(StandardCharsets.UTF_8));
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (archiveInputStream != null) {
            archiveInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testReadArEntrySuccessfully() throws IOException {
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        assertEquals(ArArchiveEntry.class, nextEntry.getClass());
        assertEquals("test-entry1.txt", nextEntry.getName());
        assertEquals(300L, nextEntry.getSize());
        nextEntry.close();
    }

    @Test
    public void testReadArEntryWithInvalidHeader() throws IOException {
        inputStream.skip(11); // read 11 bytes to simulate incorrect header
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testReadArEntryWithNoEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("00000003c21 7261 6863 0a3e".getBytes(StandardCharsets.UTF_8));
        InputStream corruptedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream corruptedArchiveInputStream = new ArArchiveInputStream(corruptedInputStream);

        ArchiveEntry nextEntry = corruptedArchiveInputStream.getNextEntry();
        assertNull(nextEntry);
    }
}