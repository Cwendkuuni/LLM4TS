import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream input;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setup() throws IOException {
        byte[] header = {(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a};
        byte[] name = "test.txt".getBytes(StandardCharsets.UTF_8);
        byte[] trailer = ArArchiveEntry.TRAILER;
        byte[] lastmodified = "1999-01-01 00:00:00".getBytes(StandardCharsets.UTF_8);
        byte[] userid = "user".getBytes(StandardCharsets.UTF_8);
        byte[] groupid = "group".getBytes(StandardCharsets.UTF_8);
        byte[] filemode = "0644".getBytes();
        byte[] length = "123".getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(name);
        byteArrayOutputStream.write(lastmodified);
        byteArrayOutputStream.write(userid);
        byteArrayOutputStream.write(groupid);
        byteArrayOutputStream.write(filemode);
        byteArrayOutputStream.write(length);
        byteArrayOutputStream.write(trailer);

        input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        if (arArchiveInputStream != null) {
            arArchiveInputStream.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry expectedEntry = new ArArchiveEntry("test.txt", 123L);
        ArArchiveEntry actualEntry = arArchiveInputStream.getNextArEntry();
        assertEquals(expectedEntry, actualEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry expectedEntry = new ArArchiveEntry("test.txt", 123L);
        ArchiveEntry actualEntry = arArchiveInputStream.getNextEntry();
        assertEquals(expectedEntry, actualEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithEOF() throws IOException {
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry(); // should throw an exception
    }

    @Test
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        ArArchiveInputStream invalidArArchiveInputStream = new ArArchiveInputStream(invalidInputStream);
        assertNull(invalidArArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertEquals(true, ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatchesWithInvalidLength() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        assertEquals(false, ArArchiveInputStream.matches(signature, 8));
    }
}