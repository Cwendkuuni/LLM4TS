package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x41});
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        stream.getNextArEntry();
    }

    @Test
    public void testNextEntry() throws IOException {
        String testData =
                "\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00 01 01 01 01 01 01 01 01 0000005362616d000002002000" +
                "0001 01 01 01 01 01 01 01 01 0000005362616d00000200200000 01 01 01 01 01 01 01 01 01 0000004b6579" +
                "730000 01 01 01 01 01 01 01 01 0000004b6579730000 01 01 01 01 01 01 01 01 01 0000002e3130312e313031" +
                "00 02 01 01 01 01 01 01 01 000000415343420001 01 01 01 01 01 01 01 01 01 000000536f6d0001 01 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000536f6d0001 01 01 01 01 01 01 01 01 01 00000007425343420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000074d5343420501 01 01 01 01 01 01 01 01 01 00000007505343420501 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075353420501 01 01 01 01 01 01 01 01 01 000000075356420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075453420501 01 01 01 01 01 01 01 01 01 000000075456420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075553420501 01 01 01 01 01 01 01 01 01 000000075556420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000";

        ByteArrayInputStream input = new ByteArrayInputStream(testData.getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        ArchiveEntry entry;
        for (int i = 0; i < 7; i++) {
            entry = stream.getNextArEntry();
            assertTrue(entry != null);
            assertTrue(entry instanceof ArArchiveEntry);
            assertTrue(entry.getName().length() > 0);
            assertTrue(entry.getSize() >= 0);
        }

        entry = stream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        String testData =
                "\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00 01 01 01 01 01 01 01 01 0000005362616d000002002000" +
                "00 01 01 01 01 01 01 01 01 0000005362616d00000200200000 01 01 01 01 01 01 01 01 01 0000004b6579730000" +
                "01 01 01 01 01 01 01 01 01 0000004b6579730000 01 01 01 01 01 01 01 01 01 0000002e3130312e31303100" +
                " 02 01 01 01 01 01 01 01 01 000000415343420001 01 01 01 01 01 01 01 01 01 000000536f6d0001 01 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000536f6d0001 01 01 01 01 01 01 01 01 01 00000007425343420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000074d5343420501 01 01 01 01 01 01 01 01 01 00000007505343420501 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075353420501 01 01 01 01 01 01 01 01 01 000000075356420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075453420501 01 01 01 01 01 01 01 01 01 000000075456420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075553420501 01 01 01 01 01 01 01 01 01 000000075556420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075653420501 01 01 01 01 01 01 01 01 01 000000";

        ByteArrayInputStream input = new ByteArrayInputStream(testData.getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[1024];
        int bytesRead;

        for (int i = 0; i < 7; i++) {
            bytesRead = stream.read(buffer);
            assertTrue(bytesRead > 0);
        }

        bytesRead = stream.read(buffer);
        assertTrue(bytesRead == -1);
    }

    @Test
    public void testClose() throws IOException {
        String testData =
                "\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00 01 01 01 01 01 01 01 01 0000005362616d000002002000" +
                "00 01 01 01 01 01 01 01 01 0000005362616d00000200200000 01 01 01 01 01 01 01 01 01 0000004b6579730000" +
                "01 01 01 01 01 01 01 01 01 0000004b6579730000 01 01 01 01 01 01 01 01 01 0000002e3130312e31303100" +
                " 02 01 01 01 01 01 01 01 01 000000415343420001 01 01 01 01 01 01 01 01 01 000000536f6d0001 01 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000536f6d0001 01 01 01 01 01 01 01 01 01 00000007425343420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000074d5343420501 01 01 01 01 01 01 01 01 01 00000007505343420501 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075353420501 01 01 01 01 01 01 01 01 01 000000075356420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075453420501 01 01 01 01 01 01 01 01 01 000000075456420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075553420501 01 01 01 01 01 01 01 01 01 000000075556420501 01 01 01" +
                " 01 01 01 01 01 01 01 01 01 000000075653420501 01 01 01 01 01 01 01 01 01 000000";

        ByteArrayInputStream input = new ByteArrayInputStream(testData.getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        stream.close();

        try {
            stream.getNextArEntry();
            fail("Should have thrown an IOException after calling close()");
        } catch (IOException e) {
            // expected
        }
    }
}