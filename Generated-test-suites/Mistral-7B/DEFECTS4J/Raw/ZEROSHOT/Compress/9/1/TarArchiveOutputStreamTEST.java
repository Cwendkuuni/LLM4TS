import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String entryName = "test.txt";
        byte[] entryContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readArchiveEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals(entryName, readArchiveEntry.getName());
        Assert.assertArrayEquals(entryContent, readArchiveEntry.getInputStream().readAllBytes());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String entryName = "test/long_name.txt";
        byte[] entryContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readArchiveEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals(entryName, readArchiveEntry.getName());
        Assert.assertArrayEquals(entryContent, readArchiveEntry.getInputStream().readAllBytes());

        // Check long file name handling
        String longFileName = "test/long_name_that_is_too_long_for_tar_format_without_extension_and_with_ext_1.txt";
        TarArchiveEntry longFileNameArchiveEntry = new TarArchiveEntry(longFileName);
        byte[] longFileNameEntryContent = "Long file name test".getBytes(StandardCharsets.UTF_8);

        if (tarArchiveOutputStream.getLongFileMode() != TarArchiveOutputStream.LONGFILE_GNU) {
            throw new RuntimeException("Long file name test can only be performed with LONGFILE_GNU mode.");
        }

        tarArchiveOutputStream.putArchiveEntry(longFileNameArchiveEntry);
        tarArchiveOutputStream.write(longFileNameEntryContent, 0, longFileNameEntryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        readArchiveEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals(longFileName, readArchiveEntry.getName());
        Assert.assertArrayEquals(longFileNameEntryContent, readArchiveEntry.getInputStream().readAllBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }
}