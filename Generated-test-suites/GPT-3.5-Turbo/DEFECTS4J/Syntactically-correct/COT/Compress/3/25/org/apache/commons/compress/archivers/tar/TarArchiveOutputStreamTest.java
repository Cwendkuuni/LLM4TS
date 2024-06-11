import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(output);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        output.close();
    }

    @Test
    public void testConstructor() {
        assertEquals(output, tarOutputStream.out);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertEquals(1024, output.toByteArray().length);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.closed);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("very-long-file-name-that-exceeds-the-maximum",
                                                    TarArchiveEntry.DEFAULT_FILE_MODE);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("valid-file-name.txt",
                                                    TarArchiveEntry.DEFAULT_FILE_MODE);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertEquals(512, output.toByteArray().length);
    }

    // Add additional test cases for other methods

}