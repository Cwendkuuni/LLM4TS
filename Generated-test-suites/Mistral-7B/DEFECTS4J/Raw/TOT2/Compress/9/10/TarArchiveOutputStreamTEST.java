import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream outputStream;
    private ByteArrayOutputStream buffer;

    @Before
    public void setup() throws IOException {
        buffer = new ByteArrayOutputStream();
        outputStream = new TarArchiveOutputStream(buffer, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();

        Assert.assertEquals("Hello World!", new String(outputStream.getBuffer().toByteArray(), StandardCharsets.UTF_8));
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        int nameLength = TarConstants.NAMELEN + 1;
        String longFileName = "a".repeat(nameLength);
        File testFile = new File("testFile.txt");
        String fileName = longFileName;
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        int nameLength = TarConstants.NAMELEN + 1;
        String longFileName = "a".repeat(nameLength);
        File testFile = new File("testFile.txt");
        String fileName = longFileName;
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTruncatedFileName() throws IOException {
        String fileName = "testFile.txt";
        File testFile = new File("testFile" + ".truncated");
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        File testDirectory = new File("testDirectory");
        String dirName = "testDirectory";
        ArchiveEntry entry = new TarArchiveEntry(testDirectory, dirName);

        outputStream.putArchiveEntry(entry);
        outputStream.finish();
        outputStream.close();

        Assert.assertEquals(0, outputStream.getBuffer().size());
    }

    @Test
    public void testWrite() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();

        Assert.assertEquals("Hello World!", new String(outputStream.getBuffer().toByteArray(), StandardCharsets.UTF_8));
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        outputStream.write("Goodbye World!".getBytes());
    }

    @Test
    public void testFlush() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.flush();

        Assert.assertEquals("Hello World!", new String(outputStream.getBuffer().toByteArray(), StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";

        ArchiveEntry entry = outputStream.createArchiveEntry(testFile, fileName);

        Assert.assertEquals(fileName, entry.getName());
        Assert.assertEquals(testFile.length(), entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        Assert.assertEquals(0, outputStream.getBuffer().size());
    }

    @Test
    public void testFinish() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(testFile, fileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Hello World!".getBytes());
        outputStream.closeArchiveEntry();

        outputStream.finish();
        outputStream.close();

        Assert.assertEquals(0, outputStream.getBuffer().size());
    }
}