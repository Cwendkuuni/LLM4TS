package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.archiveentry.GenericArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ArArchiveInputStreamTest {

    @Test
    public void testArHeaderMatches() {
        final byte[] arHeader = new byte[] {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        assertTrue(ArArchiveInputStream.matches(arHeader, arHeader.length));
    }

    @Test
    public void testGetNextArEntry_validEntry() throws IOException {
        final String testFilePath = "src/test/resources/testarfile.tar.gz";
        final InputStream testInputStream = ArArchiveInputStream.class.getResourceAsStream(testFilePath);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(testInputStream);

        final ArArchiveEntry expectedEntry = new ArArchiveEntry("testentry.txt", 12);
        final ArArchiveEntry actualEntry = arArchiveInputStream.getNextArEntry();

        assertTrue(actualEntry instanceof ArArchiveEntry);
        assertTrue(actualEntry.getName().equals(expectedEntry.getName()));
        assertEquals(expectedEntry.getSize(), actualEntry.getSize());
    }

    @Test
    public void testGetNextArEntry_nullEntry() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntry_invalidEntry() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] invalidHeader = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        byteArrayOutputStream.write(invalidHeader);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntry_incompleteEntry() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] incompleteHeader = new byte[] {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00
        };
        byteArrayOutputStream.write(incompleteHeader);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);
        arArchiveInputStream.close();

        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testRead() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] testBytes = new byte[] {0x41, 0x42, 0x43};
        final int readBytes = arArchiveInputStream.read(testBytes);

        assert (readBytes == 3);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] testBytes = new byte[] {0x41, 0x42, 0x43};
        final int offset = 1;
        final int length = 2;
        final int readBytes = arArchiveInputStream.read(testBytes, offset, length);

        assert (readBytes == 2);
        assert (testBytes[0] == 0x42);
        assert (testBytes[1] == 0x43);
    }

    @Test
    public void testReadWithOffset_Negative() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] testBytes = new byte[] {0x41, 0x42, 0x43};
        final int offset = -1;
        final int length = 2;
        final int readBytes = arArchiveInputStream.read(testBytes, offset, length);

        assert (readBytes == -1);
    }

    @Test
    public void testReadWithOffset_Length() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] testBytes = new byte[] {0x41, 0x42, 0x43};
        final int offset = 0;
        final int length = 5;
        final int readBytes = arArchiveInputStream.read(testBytes, offset, length);

        assert (readBytes == 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeOffset() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] testBytes = new byte[] {0x41, 0x42, 0x43};
        final int offset = -1;
        final int length = 2;
        arArchiveInputStream.read(testBytes, offset, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeLength() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] testBytes = new byte[] {0x41, 0x42, 0x43};
        final int offset = 0;
        final int length = -2;
        arArchiveInputStream.read(testBytes, offset, length);
    }

    @Test
    public void testMatches() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] signature = new byte[] {0x61, 0x72, 0x63, 0x68};
        final int length = 4;
        final boolean matches = ArArchiveInputStream.matches(signature, length);

        assertTrue(matches);
    }

    @Test
    public void testMatches_Mismatch() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(byteArrayOutputStream);

        final byte[] signature = new byte[] {0x61, 0x72, 0x63, 0x69};
        final int length = 4;
        final boolean matches = ArArchiveInputStream.matches(signature, length);

        assertFalse(matches);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        final String testFilePath = "src/test/resources/testarfile.tar.gz";
        final InputStream testInputStream = ArArchiveInputStream.class.getResourceAsStream(testFilePath);
        final byte[] testBytes = IOUtils.toByteArray(testInputStream);

        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(testBytes));

        final byte[] readBytes = arArchiveInputStream.readAllBytes();

        assertArrayEquals(testBytes, readBytes);
    }
}