import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;
    private OutputStream gzipOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        gzipOutputStream = new GZIPOutputStream(outputStream);
        tarArchiveOutputStream = new TarArchiveOutputStream(gzipOutputStream);
    }

    @Test
    public void testPutArchiveEntryWithRegularFile() throws IOException {
        File inputFile = new File("testFile.txt");
        inputFile.createNewFile();
        TarArchiveOutputStream tarArchive = new TarArchiveOutputStream(outputStream);

        tarArchive.putArchiveEntry(tarArchive.createArchiveEntry(inputFile, "testFile.txt"));
        tarArchive.write(Files.readAllBytes(inputFile.toPath()));
        tarArchive.closeArchiveEntry();
        tarArchive.close();

        byte[] actualOutput = outputStream.toByteArray();
        byte[] expectedOutput = Files.readAllBytes(Path.of("expectedOutput.tar.gz"));

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPutArchiveEntryWithEmptyDirectory() throws IOException {
        File inputDirectory = new File("testDirectory");
        inputDirectory.mkdir();
        TarArchiveOutputStream tarArchive = new TarArchiveOutputStream(outputStream);

        tarArchive.putArchiveEntry(tarArchive.createArchiveEntry(inputDirectory, "testDirectory/"));
        tarArchive.closeArchiveEntry();
        tarArchive.close();

        byte[] actualOutput = outputStream.toByteArray();
        byte[] expectedOutput = Files.readAllBytes(Path.of("expectedOutput.tar.gz"));

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPutArchiveEntryWithNestedDirectory() throws IOException {
        File nestedDirectory = new File("nestedDirectory");
        nestedDirectory.mkdir();
        File inputFile = new File("nestedDirectory/testFile.txt");
        inputFile.createNewFile();
        TarArchiveOutputStream tarArchive = new TarArchiveOutputStream(outputStream);

        tarArchive.putArchiveEntry(tarArchive.createArchiveEntry(nestedDirectory, "nestedDirectory/"));
        tarArchive.write(Files.readAllBytes(inputFile.toPath()));
        tarArchive.closeArchiveEntry();
        tarArchive.close();

        byte[] actualOutput = outputStream.toByteArray();
        byte[] expectedOutput = Files.readAllBytes(Path.of("expectedOutput.tar.gz"));

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test (expected = IOException.class)
    public void testPutArchiveEntryWithFinishedStream() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt"));
    }

    @Test (expected = IOException.class)
    public void testCloseArchiveEntryWithoutCallingPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test (expected = IOException.class)
    public void testCloseWithoutCallingFinish() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testFinishAndClose() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        assertTrue(tarArchiveOutputStream.isFinished());
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test (expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test (expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt"));
    }

    @Test (expected = IOException.class)
    public void testCloseAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test (expected = IOException.class)
    public void testPutArchiveEntryAfterClose() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.putArchiveEntry(tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt"));
    }

    @Test (expected = IOException.class)
    public void testCloseAfterClose() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testFlush() throws IOException {
        tarArchiveOutputStream.flush();
        assertTrue(tarArchiveOutputStream.isFlushed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        inputFile.createNewFile();

        tarArchiveOutputStream.putArchiveEntry(tarArchiveOutputStream.createArchiveEntry(inputFile, "testFile.txt"));
        tarArchiveOutputStream.write(Files.readAllBytes(inputFile.toPath()));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] actualOutput = outputStream.toByteArray();
        byte[] expectedOutput = Files.readAllBytes(Path.of("expectedOutput.tar.gz"));

        assertArrayEquals(expectedOutput, actualOutput);
    }
}