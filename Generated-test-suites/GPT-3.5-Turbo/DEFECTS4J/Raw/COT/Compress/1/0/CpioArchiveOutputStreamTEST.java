import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        // Verify the constructor sets the OutputStream correctly
        assertNotNull(out.out);
        assertEquals(FORMAT_NEW, out.entryFormat);
    }

    @Test
    public void testEnsureOpen() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        // Verify that ensureOpen does not throw an exception when the stream is not closed
        assertDoesNotThrow(out::ensureOpen);
    }

    @Test
    public void testSetFormat() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        // Verify that setFormat sets the entryFormat correctly
        out.setFormat(FORMAT_NEW);
        assertEquals(FORMAT_NEW, out.entryFormat);
    }

    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        // Verify that putNextEntry sets the cpioEntry and written fields correctly
        assertDoesNotThrow(() -> out.putNextEntry(entry));
        assertEquals(entry, out.cpioEntry);
        assertEquals(0, out.written);
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        assertDoesNotThrow(() -> out.putNextEntry(entry));
        // Verify that write increases the written field correctly
        assertDoesNotThrow(() -> out.write(new byte[]{1, 2, 3}, 0, 3));
        assertEquals(3, out.written);
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        assertDoesNotThrow(() -> out.putNextEntry(entry));
        // Verify that closeArchiveEntry throws an exception when the entry size is invalid
        assertThrows(IOException.class, out::closeArchiveEntry);
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        assertDoesNotThrow(out::finish);
        // Verify that finish creates a trailer entry
        assertNotNull(out.cpioEntry);
        assertEquals(FORMAT_NEW, out.cpioEntry.getFormat());
        assertEquals(0, out.cpioEntry.getMode());
        assertEquals("TRAILER!!!", out.cpioEntry.getName());
    }

    @Test
    public void testClose() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);
        assertDoesNotThrow(out::close);
        // Verify that close sets the closed field to true
        assertTrue(out.closed);
    }

}