import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = new byte[]{0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        byte[] trailer = new byte[]{0x3e, 0x0a};
        byte[] name = new byte[16];
        byte[] lastmodified = new byte[12];
        byte[] userid = new byte[6];
        byte[] groupid = new byte[6];
        byte[] filemode = new byte[8];
        byte[] length = new byte[10];
        
        byte[] entryHeader = new byte[8];
        System.arraycopy(header, 0, entryHeader, 0, 8);
        
        ByteArrayInputStream input = new ByteArrayInputStream(entryHeader);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        
        ArArchiveEntry entry = arInput.getNextArEntry();
        
        assertNull(entry);
        
        input = new ByteArrayInputStream(header);
        arInput = new ArArchiveInputStream(input);
        
        entry = arInput.getNextArEntry();
        
        assertNull(entry);
        
        input = new ByteArrayInputStream(trailer);
        arInput = new ArArchiveInputStream(input);
        
        entry = arInput.getNextArEntry();
        
        assertNull(entry);
        
        byte[] validEntry = new byte[80];
        System.arraycopy(header, 0, validEntry, 0, 8);
        System.arraycopy(name, 0, validEntry, 8, name.length);
        System.arraycopy(lastmodified, 0, validEntry, 24, lastmodified.length);
        System.arraycopy(userid, 0, validEntry, 36, userid.length);
        System.arraycopy(groupid, 0, validEntry, 42, groupid.length);
        System.arraycopy(filemode, 0, validEntry, 48, filemode.length);
        System.arraycopy(length, 0, validEntry, 56, length.length);
        System.arraycopy(trailer, 0, validEntry, 66, 2);
        
        input = new ByteArrayInputStream(validEntry);
        arInput = new ArArchiveInputStream(input);
        
        entry = arInput.getNextArEntry();
        
        assertEquals(new String(name).trim(), entry.getName());
        assertEquals(Long.parseLong(new String(length).trim()), entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] header = new byte[]{0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        byte[] trailer = new byte[]{0x3e, 0x0a};
        byte[] name = new byte[16];
        byte[] lastmodified = new byte[12];
        byte[] userid = new byte[6];
        byte[] groupid = new byte[6];
        byte[] filemode = new byte[8];
        byte[] length = new byte[10];
        
        byte[] entryHeader = new byte[8];
        System.arraycopy(header, 0, entryHeader, 0, 8);
        
        ByteArrayInputStream input = new ByteArrayInputStream(entryHeader);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        
        ArchiveEntry entry = arInput.getNextEntry();
        
        assertNull(entry);
        
        input = new ByteArrayInputStream(header);
        arInput = new ArArchiveInputStream(input);
        
        entry = arInput.getNextEntry();
        
        assertNull(entry);
        
        input = new ByteArrayInputStream(trailer);
        arInput = new ArArchiveInputStream(input);
        
        entry = arInput.getNextEntry();
        
        assertNull(entry);
        
        byte[] validEntry = new byte[80];
        System.arraycopy(header, 0, validEntry, 0, 8);
        System.arraycopy(name, 0, validEntry, 8, name.length);
        System.arraycopy(lastmodified, 0, validEntry, 24, lastmodified.length);
        System.arraycopy(userid, 0, validEntry, 36, userid.length);
        System.arraycopy(groupid, 0, validEntry, 42, groupid.length);
        System.arraycopy(filemode, 0, validEntry, 48, filemode.length);
        System.arraycopy(length, 0, validEntry, 56, length.length);
        System.arraycopy(trailer, 0, validEntry, 66, 2);
        
        input = new ByteArrayInputStream(validEntry);
        arInput = new ArArchiveInputStream(input);
        
        entry = arInput.getNextEntry();
        
        assertEquals(new String(name).trim(), entry.getName());
        assertEquals(Long.parseLong(new String(length).trim()), entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        byte[] header = new byte[]{0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        
        ByteArrayInputStream input = new ByteArrayInputStream(header);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        
        arInput.close();
        
        assertTrue(arInput.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        
        assertEquals(0x01, arInput.read());
        assertEquals(0x02, arInput.read());
        assertEquals(0x03, arInput.read());
        assertEquals(-1, arInput.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        byte[] buffer = new byte[2];
        
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        
        int bytesRead = arInput.read(buffer);
        
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        byte[] buffer = new byte[2];
        
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        
        int bytesRead = arInput.read(buffer, 1, 2);
        
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0x00, 0x01}, buffer);
    }

    @Test
    public void testMatches() throws IOException {
        byte[] header = new byte[]{0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        byte[] invalidHeader = new byte[]{0x21, 0x72, 0x61, 0x68, 0x63};
        
        assertTrue(ArArchiveInputStream.matches(header, header.length));
        assertFalse(ArArchiveInputStream.matches(invalidHeader, invalidHeader.length));
    }

}