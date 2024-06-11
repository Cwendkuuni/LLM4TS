package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        byte[] entry1 = {'e', 'n', 't', 'r', 'y', '1', '\n'};
        byte[] entry2 = {'e', 'n', 't', 'r', 'y', '2', '\n'};
        byte[] trailer = {'*', 't', 'r', 'a', 'i', 'l', 'e', 'r', '\n'};

        byte[] data = new byte[header.length + entry1.length + header.length + entry2.length + trailer.length + header.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(entry1, 0, data, header.length, entry1.length);
        System.arraycopy(header, 0, data, header.length + entry1.length, header.length);
        System.arraycopy(entry2, 0, data, header.length + entry1.length + header.length, entry2.length);
        System.arraycopy(trailer, 0, data, header.length + entry1.length + header.length + entry2.length, trailer.length);
        System.arraycopy(header, 0, data, header.length + entry1.length + header.length + entry2.length + trailer.length, header.length);

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        ArArchiveEntry entry = ar.getNextArEntry();
        assertNotNull(entry);
        assertEquals("entry1", entry.getName());
        assertEquals(entry1.length, entry.getLength());

        entry = ar.getNextArEntry();
        assertNotNull(entry);
        assertEquals("entry2", entry.getName());
        assertEquals(entry2.length, entry.getLength());

        entry = ar.getNextArEntry();
        assertNull(entry);

        ar.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] header = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        byte[] entry1 = {'e', 'n', 't', 'r', 'y', '1', '\n'};
        byte[] entry2 = {'e', 'n', 't', 'r', 'y', '2', '\n'};
        byte[] trailer = {'*', 't', 'r', 'a', 'i', 'l', 'e', 'r', '\n'};

        byte[] data = new byte[header.length + entry1.length + header.length + entry2.length + trailer.length + header.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(entry1, 0, data, header.length, entry1.length);
        System.arraycopy(header, 0, data, header.length + entry1.length, header.length);
        System.arraycopy(entry2, 0, data, header.length + entry1.length + header.length, entry2.length);
        System.arraycopy(trailer, 0, data, header.length + entry1.length + header.length + entry2.length, trailer.length);
        System.arraycopy(header, 0, data, header.length + entry1.length + header.length + entry2.length + trailer.length, header.length);

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        ArchiveEntry entry = ar.getNextEntry();
        assertNotNull(entry);
        assertEquals("entry1", entry.getName());
        assertEquals(entry1.length, entry.getSize());

        entry = ar.getNextEntry();
        assertNotNull(entry);
        assertEquals("entry2", entry.getName());
        assertEquals(entry2.length, entry.getSize());

        entry = ar.getNextEntry();
        assertNull(entry);

        ar.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] header = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        byte[] entry1 = {'e', 'n', 't', 'r', 'y', '1', '\n'};
        byte[] entry2 = {'e', 'n', 't', 'r', 'y', '2', '\n'};
        byte[] trailer = {'*', 't', 'r', 'a', 'i', 'l', 'e', 'r', '\n'};

        byte[] data = new byte[header.length + entry1.length + header.length + entry2.length + trailer.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(entry1, 0, data, header.length, entry1.length);
        System.arraycopy(header, 0, data, header.length + entry1.length, header.length);
        System.arraycopy(entry2, 0, data, header.length + entry1.length + header.length, entry2.length);
        System.arraycopy(trailer, 0, data, header.length + entry1.length + header.length + entry2.length, trailer.length);

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        ar.close();

        assertNull(ar.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] entry1 = {'e', 'n', 't', 'r', 'y', '1', '\n'};

        ByteArrayInputStream in = new ByteArrayInputStream(entry1);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        assertEquals('e', ar.read());
        assertEquals('n', ar.read());
        assertEquals('t', ar.read());
        assertEquals('r', ar.read());
        assertEquals('y', ar.read());
        assertEquals('1', ar.read());
        assertEquals('\n', ar.read());
        assertEquals(-1, ar.read());

        ar.close();
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] entry1 = {'e', 'n', 't', 'r', 'y', '1', '\n'};

        ByteArrayInputStream in = new ByteArrayInputStream(entry1);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        byte[] buffer = new byte[2];
        int read = ar.read(buffer);
        assertEquals(2, read);
        assertEquals('e', buffer[0]);
        assertEquals('n', buffer[1]);

        read = ar.read(buffer);
        assertEquals(2, read);
        assertEquals('t', buffer[0]);
        assertEquals('r', buffer[1]);

        read = ar.read(buffer);
        assertEquals(2, read);
        assertEquals('y', buffer[0]);
        assertEquals('1', buffer[1]);

        read = ar.read(buffer);
        assertEquals(1, read);
        assertEquals('\n', buffer[0]);

        read = ar.read(buffer);
        assertEquals(-1, read);

        ar.close();
    }

    @Test
    public void testReadBytesWithOffset() throws IOException {
        byte[] entry1 = {'e', 'n', 't', 'r', 'y', '1', '\n'};

        ByteArrayInputStream in = new ByteArrayInputStream(entry1);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        byte[] buffer = new byte[4];
        int read = ar.read(buffer, 1, 2);
        assertEquals(2, read);
        assertEquals(0, buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('n', buffer[2]);
        assertEquals(0, buffer[3]);

        read = ar.read(buffer, 1, 2);
        assertEquals(2, read);
        assertEquals(0, buffer[0]);
        assertEquals('t', buffer[1]);
        assertEquals('r', buffer[2]);
        assertEquals(0, buffer[3]);

        read = ar.read(buffer, 1, 2);
        assertEquals(2, read);
        assertEquals(0, buffer[0]);
        assertEquals('y', buffer[1]);
        assertEquals('1', buffer[2]);
        assertEquals(0, buffer[3]);

        read = ar.read(buffer, 1, 2);
        assertEquals(1, read);
        assertEquals(0, buffer[0]);
        assertEquals('\n', buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);

        read = ar.read(buffer, 1, 2);
        assertEquals(-1, read);

        ar.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        int length = 8;

        boolean matches = ArArchiveInputStream.matches(signature, length);
        assertEquals(true, matches);
    }
}