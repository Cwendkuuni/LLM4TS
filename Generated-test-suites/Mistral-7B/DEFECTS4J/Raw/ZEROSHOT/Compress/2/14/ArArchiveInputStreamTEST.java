import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        Assertions.assertNull(arArchiveInputStream.getNextArEntry());
        arArchiveInputStream.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        String archiveData = "!<arch>\n" +
                " 1 2001-03-26 10:15:38 4755 owner group unx       0 lr-xr-xr-x  0 root root ArFile1.txt\n" +
                " 1 2001-03-26 10:15:39 2048 owner group unx       0 -w-r--r--  0 root root ArFile2.txt\n" +
                "!<eoh>\n" +
                "ArFile1.txt:12345\n" +
                "!<arch>\n" +
                " 1 2001-03-26 10:15:40 4755 owner group unx       0 lr-xr-xr-x  0 root root ArFile3.txt\n" +
                " 1 2001-03-26 10:15:41 2048 owner group unx       0 -w-r--r--  0 root root ArFile4.txt\n" +
                "!<eoh>\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry1 = arArchiveInputStream.getNextArEntry();
        Assertions.assertNotNull(entry1);
        Assertions.assertEquals("ArFile1.txt", entry1.getName());
        Assertions.assertEquals(12345L, entry1.getSize());

        ArArchiveEntry entry2 = arArchiveInputStream.getNextArEntry();
        Assertions.assertNotNull(entry2);
        Assertions.assertEquals("ArFile3.txt", entry2.getName());
        Assertions.assertNull(arArchiveInputStream.getNextArEntry());

        arArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        byte[] archiveHeader = "!<arch>".getBytes();
        System.arraycopy(archiveHeader, 0, signature, 0, archiveHeader.length);
        boolean result = ArArchiveInputStream.matches(signature, signature.length);
        Assertions.assertTrue(result);
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        InputStream testInputStream = new ByteArrayInputStream(new byte[]{0x01});
        int read = arArchiveInputStream.read(testInputStream.readAllBytes());
        Assertions.assertEquals(1, read);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.close();

        Assertions.assertThrows(IOException.class, () -> arArchiveInputStream.getNextArEntry());
    }

}