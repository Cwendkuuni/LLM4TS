import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setUp() {
        mockOutputStream = mock(OutputStream.class);
        tarOutputStream = new TarArchiveOutputStream(mockOutputStream);
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntryMock entry = new TarArchiveEntryMock("test.txt", 100, TarArchiveEntryMock.TYPE_FILE);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[100]);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testWrite() {
        byte[] data = "Hello, world!".getBytes();
        try {
            tarOutputStream.write(data, 0, data.length);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testFinish() {
        try {
            tarOutputStream.finish();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testClose() {
        try {
            tarOutputStream.close();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    private class TarArchiveEntryMock extends TarArchiveEntry {

        public TarArchiveEntryMock(String name, long size, byte type) {
            super(name, type);
            this.setSize(size);
        }

        // Override isDirectory to always return false
        @Override
        public boolean isDirectory() {
            return false;
        }
    }

}