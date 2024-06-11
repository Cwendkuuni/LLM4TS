import static org.junit.Assert.*;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    CpioArchiveOutputStream cpioOut;

    @Before
    public void setUp() throws Exception {
        cpioOut = new CpioArchiveOutputStream(new OutputStream() {
            @Override
            public void write(int arg0) throws IOException {}
        });
    }

    @After
    public void tearDown() throws Exception {
        cpioOut.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        assertTrue(cpioOut.getArchiveEntry() == entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryTwice() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("file1.txt");
        entry1.setFileSize(3);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("file2.txt");
        entry2.setFileSize(4);

        cpioOut.putNextEntry(entry1);
        cpioOut.putNextEntry(entry2);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());
        cpioOut.closeArchiveEntry();

        assertTrue(cpioOut.getArchiveEntry() == null);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("1234".getBytes()); // Writing less than the specified file size
        cpioOut.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());

        assertTrue(cpioOut.getBytesWritten() == 5);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());
        cpioOut.finish();

        assertTrue(cpioOut.getArchiveEntry().getName().equals("TRAILER!!!"));
    }

    @Test
    public void testClose() throws IOException {
        cpioOut.close();
        assertTrue(cpioOut.isClosed());
    }

}