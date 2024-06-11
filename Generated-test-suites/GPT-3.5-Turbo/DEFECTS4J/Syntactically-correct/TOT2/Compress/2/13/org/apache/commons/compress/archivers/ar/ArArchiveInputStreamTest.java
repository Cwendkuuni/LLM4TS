import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        byte[] header = "#!<header>".getBytes();
        byte[] body = "Some content for testing".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] arEntryBytes = new byte[header.length + body.length + trailer.length];
        System.arraycopy(header, 0, arEntryBytes, 0, header.length);
        System.arraycopy(body, 0, arEntryBytes, header.length, body.length);
        System.arraycopy(trailer, 0, arEntryBytes, header.length + body.length, trailer.length);

        InputStream inputStream = new ByteArrayInputStream(arEntryBytes);
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() {
        try {
            arInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNextArEntry() {
        try {
            assertNotNull(arInputStream.getNextArEntry());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetNextEntry() {
        try {
            assertNotNull(arInputStream.getNextEntry());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testClose() {
        try {
            arInputStream.close();
            assertTrue(arInputStream.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testRead() {
        try {
            assertEquals('S', arInputStream.read());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] buffer = new byte[10];
        try {
            assertEquals(10, arInputStream.read(buffer));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadByteArrayIntInt() {
        byte[] buffer = new byte[15];
        try {
            assertEquals(15, arInputStream.read(buffer, 0, 15));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = "#!<arch>\n".getBytes();
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
}