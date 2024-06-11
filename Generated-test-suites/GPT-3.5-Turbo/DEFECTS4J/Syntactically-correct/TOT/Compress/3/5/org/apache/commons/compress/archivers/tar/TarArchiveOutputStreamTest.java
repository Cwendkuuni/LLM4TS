import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private Path tempDirectory;

    @Before
    public void setup() throws IOException {
        tempDirectory = Files.createTempDirectory("test");
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempDirectory.toString() + "/test.tar"));
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        Files.deleteIfExists(tempDirectory.resolve("test.tar"));
        Files.deleteIfExists(tempDirectory);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals("test.txt", tarArchiveOutputStream.currName);
        assertEquals(0, tarArchiveOutputStream.currBytes);
        assertEquals(0, tarArchiveOutputStream.currSize);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_TruncateMode() throws IOException {
        String longFileName = "Lorem_Ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry." +
                "Lorem_It_has_survived_not_only_five_centuries._Ipsum_It_was_popularised_in_the_(in_the_released)" +
                " 1960s_by_the _num_of_Letras__reader_it_lembrar. The_1960_countless_opioes_" +
                "reader_dead".replace("_", "");

        ArchiveEntry entry = new TarArchiveEntry(longFileName);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals(longFileName, tarArchiveOutputStream.currName);
        assertEquals(0, tarArchiveOutputStream.currBytes);
        assertEquals(0, tarArchiveOutputStream.currSize);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileName_ErrorMode() throws IOException {
        String longFileName = "Lorem_Ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry." +
                "Lorem_It_has_survived_not_only_five_centuries._Ipsum_It_was_popularised_in_the_ (in_the_released)" +
                "1960s_by_ the_num_of_Letras__reader_it_lembrar._The_1960_countless_opioes_" +
                "reader_dead".replace("_", "");

        ArchiveEntry entry = new TarArchiveEntry(longFileName);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "This is a test file.".getBytes();
        tarArchiveOutputStream.write(data);

        int bufferSize = tarArchiveOutputStream.getRecordSize();
        assertEquals(bufferSize, tarArchiveOutputStream.assemBuf.length);
        assertEquals(bufferSize, tarArchiveOutputStream.recordBuf.length);

        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(data.length, tarArchiveOutputStream.currBytes);
        assertEquals(data.length, tarArchiveOutputStream.currSize);
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedEntrySize() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = new byte[1024 * 1024]; // 1 MB
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "test.txt");

        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(inputFile.length(), entry.getSize());
    }

    @Test
    public void testCreateArchiveEntry_Directory() throws IOException {
        File inputFile = new File("testDirectory");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "testDirectory");

        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("testDirectory", entry.getName());
        assertTrue(entry.isDirectory());
        assertEquals(0, entry.getSize());
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();

        assertEquals(1024, tarArchiveOutputStream.buffer.inputStream.available());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.longFileMode);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.longFileMode);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }
}