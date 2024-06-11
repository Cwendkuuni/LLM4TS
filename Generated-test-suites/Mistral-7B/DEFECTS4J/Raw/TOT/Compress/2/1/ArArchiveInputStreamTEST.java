import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

    private static final String VALID_AR_FILE_HEADER = "!<arch>\n" +
            "NAME   FHA1.TXT\n" +
            "SIZE  14\n" +
            "MODTIME 9601011231\n" +
            "UID   1001\n" +
            "GID   1001\n" +
            "MODE  0664\n" +
            "FILETYPE UNIX\n" +
            "MAGIC   text/plain\n" +
            "NAME   FHA2.TXT\n" +
            "SIZE  21\n" +
            "MODTIME 9601020511\n" +
            "UID   1001\n" +
            "GID   1001\n" +
            "MODE  0664\n" +
            "FILETYPE UNIX\n" +
            "MAGIC   text/plain\n" +
            "NAME   FHA3.TXT\n" +
            "SIZE  38\n" +
            "MODTIME 9601031231\n" +
            "UID   1001\n" +
            "GID   1001\n" +
            "MODE  0664\n" +
            "FILETYPE UNIX\n" +
            "MAGIC   text/plain\n" +
            "TRAILER\n";

    private static final String INVALID_AR_FILE_HEADER = "!<arch>\n" +
            "NAME   FHA1.TXT\n" +
            "SIZE  14\n" +
            "MODTIME 9601011231\n" +
            "UID   1001\n" +
            "GID   1001\n" +
            "MODE  0664\n" +
            "FILETYPE UNIX\n" +
            "MAGIC   text/plain\n" +
            "NAME   FHA2.TXT\n" +
            "SIZE  21\n" +
            "MODTIME 9601020511\n" +
            "UID   1001\n" +
            "GID   1001\n" +
            "MODE  0664\n" +
            "FILETYPE UNIX\n" +
            "MAGIC   text/plain\n" +
            "NAME FHA3.TXT\n" + // missing newline
            "SIZE  38\n" +
            "MODTIME 9601031231\n" +
            "UID   1001\n" +
            "GID   1001\n" +
            "MODE  0664\n" +
            "FILETYPE UNIX\n" +
            "MAGIC   text/plain\n" +
            "TRAILER\n";

    @Test
    public void testGetNextEntryWithValidArFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(VALID_AR_FILE_HEADER.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry1 = archiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry1);
        Assert.assertEquals("FHA1.TXT", entry1.getName());
        Assert.assertEquals(14L, entry1.getSize());

        ArArchiveEntry entry2 = archiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry2);
        Assert.assertEquals("FHA2.TXT", entry2.getName());
        Assert.assertEquals(21L, entry2.getSize());

        ArArchiveEntry entry3 = archiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry3);
        Assert.assertEquals("FHA3.TXT", entry3.getName());
        Assert.assertEquals(38L, entry3.getSize());

        ArArchiveEntry entry4 = archiveInputStream.getNextArEntry();
        Assert.assertNull(entry4);

        archiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithInvalidArFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INVALID_AR_FILE_HEADER.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.getNextArEntry();

        // The next call should throw an IOException because the header is invalid.
        archiveInputStream.getNextArEntry();

        archiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] sig = new byte[8];
        for (int i = 0; i < sig.length; i++) {
            sig[i] = (byte) 0x3c; // ASCII '!'
        }
        Assert.assertTrue(ArArchiveInputStream.matches(sig, 8));
    }
}