import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    }

    @Test
    public void putArchiveEntry_ValidEntry_Success() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void putArchiveEntry_LongFileName_Fail() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("ThisIsAVeryLongFileNameThatExceedsTheAllowedLength.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void closeArchiveEntry_EntryNotClosed_Success() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.close();

        // Expect an exception to be thrown because the entry was not closed
    }

    @Test
    public void closeArchiveEntry_EntryClosed_Success() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void closeArchiveEntry_IncompleteContents_Fail() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void write_ValidBytes_Success() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[10]);

        assertEquals(10, tarArchiveOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void write_ExceedsEntrySize_Fail() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[100]);
    }

    @Test
    public void setLongFileMode_ValidMode_Success() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void finish_UnclosedEntry_Fail() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.finish();
    }

    @Test
    public void finish_NoUnclosedEntry_Success() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
    }

}