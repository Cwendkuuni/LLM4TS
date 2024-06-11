import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tao;

    @Before
    public void setUp() throws Exception {
        tao = new TarArchiveOutputStream(new OutputStream() {
            public void write(int b) throws IOException {}
        });
    }

    @After
    public void tearDown() throws Exception {
        tao.close();
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
    }

    @Test
    public void testWriteByteArrayIntInt() throws Exception {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        byte[] data = "Hello World".getBytes();
        tao.write(data, 0, data.length);
        tao.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteByteArrayIntInt_InvalidDataLength() throws Exception {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        byte[] data = "Hello World".getBytes();
        tao.write(data, 0, data.length + 1);
        tao.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws Exception {
        tao.finish();
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntry() throws Exception {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        tao.finish();
    }

    @Test
    public void testClose() throws Exception {
        tao.close();
    }

    @Test
    public void testGetRecordSize() throws Exception {
        assertEquals(512, tao.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tao.createArchiveEntry(inputFile, "test.txt");
        assertEquals(TarArchiveEntry.class, entry.getClass());
        assertEquals("test.txt", entry.getName());
    }
}