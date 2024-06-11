import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {
    private ByteArrayInputStream inputStream;
    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = "Test Data".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        dumpArchiveInputStream.close();
    }

    @Test
    public void testGetCount() {
        assertEquals(0, dumpArchiveInputStream.getCount());
    }

    @Test
    public void testGetBytesRead() {
        assertEquals(0, dumpArchiveInputStream.getBytesRead());
        dumpArchiveInputStream.read();
        assertEquals(1, dumpArchiveInputStream.getBytesRead());
    }

    @Test
    public void testGetSummary() {
        assertNull(dumpArchiveInputStream.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        assertNull(dumpArchiveInputStream.getNextDumpEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(dumpArchiveInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('T', dumpArchiveInputStream.read());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(dumpArchiveInputStream.isClosed());
        dumpArchiveInputStream.close();
        assertTrue(dumpArchiveInputStream.isClosed());
    }
}