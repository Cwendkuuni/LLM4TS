import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("junit-test-", ".tar").toFile();
        outputStream = new FileOutputStream(tempFile);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
        Files.deleteIfExists(Paths.get(tempFile.getAbsolutePath()));
    }

    @Test
    public void testPutArchiveEntryAndWrite() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "Test Content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test Content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedContent = "Test Content".getBytes();
        byte[] readContent = new byte[expectedContent.length];
        Files.newByteChannel(Paths.get(tempFile.getAbsolutePath())).position(testEntry.getSize());
        Files.newByteChannel(Paths.get(tempFile.getAbsolutePath())).read(ByteBuffer.wrap(readContent));
        assertArrayEquals(expectedContent, readContent);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongName() throws IOException {
        final String tooLongFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(tooLongFileName, "Content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testSetLongFileModeWithoutGivenValue() throws IOException {
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinishAndClose() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "Test Content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test Content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        // check file size
        long fileSize = Files.size(tempFile);
        assertTrue(fileSize > 0);

        // check file content
        byte[] expectedContent = "Test Content".getBytes();
        byte[] readContent = new byte[expectedContent.length];
        Files.newByteChannel(Paths.get(tempFile.getAbsolutePath())).position(testEntry.getSize());
        Files.newByteChannel(Paths.get(tempFile.getAbsolutePath())).read(ByteBuffer.wrap(readContent));
        assertArrayEquals(expectedContent, readContent);
    }
}