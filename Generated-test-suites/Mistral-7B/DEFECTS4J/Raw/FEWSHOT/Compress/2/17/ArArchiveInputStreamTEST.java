package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(createTestInputStream());
        ArArchiveEntry entry = inputStream.getNextArEntry();
        assertEquals("test", entry.getName());
        assertEquals(10L, entry.getSize());

        entry = inputStream.getNextArEntry();
        assertEquals("anotherTest", entry.getName());
        assertEquals(20L, entry.getSize());

        entry = inputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(createInvalidTestInputStream());
        inputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryNoHeader() throws IOException {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(createEmptyTestInputStream());
        inputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(createTestInputStream());
        ArchiveEntry entry = inputStream.getNextEntry();
        assertEquals("test", ((ArArchiveEntry) entry).getName());
        assertEquals(10L, ((ArArchiveEntry) entry).getSize());

        entry = inputStream.getNextEntry();
        assertEquals("anotherTest", ((ArArchiveEntry) entry).getName());
        assertEquals(20L, ((ArArchiveEntry) entry).getSize());

        entry = inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesInvalidLength() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] signature = new byte[]{0x21, 0x43, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    private InputStream createTestInputStream() {
        return new byteArrayInputStream(createTestArArchiveBytes());
    }

    private InputStream createInvalidTestInputStream() {
        byte[] invalidTestArArchiveBytes = createTestArArchiveBytes();
        invalidTestArArchiveBytes[0] = (byte) 0x43; // N
        return new ByteArrayInputStream(invalidTestArArchiveBytes);
    }

    private InputStream createEmptyTestInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    private byte[] createTestArArchiveBytes() {
        return new byte[]{
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                0x68, 't'.getId(), 'e'.getId(), 's'.getId(), 't'.getId(), 0x0, 0x0, 0x10,
                0x00, 0x00, 0x00, 0x0a, 0x68, 'a'.getId(), 'n'.getId(), 'o'.getId(), 't'.getId(), 'h'.getId(), 'e'.getId(), 'r'.getId(), 'e'.getId(), 's'.getId(), 0x0, 0x0, 0x10, 0x14,
                0x00, 0x00, 0x00, 0x0a, 0x00
        };
    }
}