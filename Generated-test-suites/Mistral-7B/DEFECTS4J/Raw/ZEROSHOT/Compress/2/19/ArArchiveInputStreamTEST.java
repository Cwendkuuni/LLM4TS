package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for ArArchiveInputStream
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;
    private ArchiveEntry entry;

    @Before
    public void setUp() throws IOException {
        String testHeader = "!<arch>\n" +
                "  Creator: test\n" +
                "  Size: 123\n" +
                "  Name: test.txt\n" +
                "  Time: 1970-01-01 00:00:01\n" +
                "  Uid: user:test\n" +
                "  Gid: group:test\n" +
                "  Mode: 0644\n" +
                "  Size: 42\n" +
                "  Name: test2.txt\n" +
                "  Time: 1970-01-01 00:00:02\n" +
                "  Uid: user:test2\n" +
                "  Gid: group:test2\n" +
                "  Mode: 0666\n" +
                "  Data:\n" +
                "!!<eoh>\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testHeader.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        inputStream = byteArrayInputStream;
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (arArchiveInputStream != null) {
            arArchiveInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link ArArchiveInputStream#ArArchiveInputStream(InputStream)}
     *
     * @throws IOException
     */
    @Test
    public void testArArchiveInputStream() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertEquals(inputStream, arArchiveInputStream.getInputStream());
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextArEntry()}
     *
     * @throws IOException
     */
    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry1 = arArchiveInputStream.getNextArEntry();
        assertEquals("test.txt", entry1.getName());
        assertEquals(42L, entry1.getSize());

        ArArchiveEntry entry2 = arArchiveInputStream.getNextArEntry();
        assertEquals("test2.txt", entry2.getName());
        assertEquals(42L, entry2.getSize());

        assertNull(arArchiveInputStream.getNextArEntry());
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextEntry()}
     *
     * @throws IOException
     */
    @Test
    public void testGetNextEntry() throws IOException {
        entry = arArchiveInputStream.getNextArEntry();
        assertEquals(ArArchiveEntry.class, entry.getClass());

        entry = arArchiveInputStream.getNextEntry();
        assertEquals(ArArchiveEntry.class, entry.getClass());

        assertNull(arArchiveInputStream.getNextEntry());
    }

    /**
     * Test method for {@link ArArchiveInputStream#matches(byte[], int)}
     */
    @Test
    public void testMatches() {
        byte[] header = new byte[8];
        byte[] expectedSignature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        assertArrayEquals(expectedSignature, header);
        assertTrue(ArArchiveInputStream.matches(header, header.length));
    }
}