import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    private static final String TEST_ARCHIVE = "test.dump";

    @Test
    public void testNextEntry() throws IOException, ArchiveException {
        // create test archive
        ByteArrayOutputStream testArchiveStream = new ByteArrayOutputStream();
        DumpArchiveOutputStream testArchiveOutput = new DumpArchiveOutputStream(testArchiveStream);

        testArchiveOutput.putNextEntry(new DumpArchiveEntry("test1.txt", "test data"));
        testArchiveOutput.write(new byte[]{"test data".getBytes(Charset.forName("UTF-8"))});
        testArchiveOutput.finishStream();

        testArchiveOutput.close();

        // create input stream from test archive
        ByteArrayInputStream testArchiveInputStream = new ByteArrayInputStream(testArchiveStream.toByteArray());
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(testArchiveInputStream);

        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertEquals("test1.txt", entry.getName());
        Assert.assertEquals("test data", entry.getSize());

        // read entry data
        byte[] buffer = new byte[entry.getSize()];
        int bytesRead = dumpArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(bytesRead, entry.getSize());
        Assert.assertEquals("test data", new String(buffer, 0, bytesRead, Charset.forName("UTF-8")));

        // test next entry
        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry);

        // close input stream
        dumpArchiveInputStream.close();
    }

    @Test
    public void testErrorScenarios() throws IOException, ArchiveException {
        // create test archive
        RandomAccessFile testArchiveFile = new RandomAccessFile(TEST_ARCHIVE, "rw");
        FileChannel testArchiveChannel = testArchiveFile.getChannel();

        testArchiveChannel.position(0);
        testArchiveChannel.write(new byte[]{"test".getBytes(Charset.forName("UTF-8"))});
        testArchiveChannel.write(new byte[]{-128}); // write invalid byte

        testArchiveChannel.position(0);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(testArchiveFile);

        try {
            dumpArchiveInputStream.getNextEntry();
            Assert.fail("Expecting ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        testArchiveChannel.close();
    }

    @Test
    public void testValidateArchive() throws IOException, ArchiveException {
        // create test archive
        ByteArrayOutputStream testArchiveStream = new ByteArrayOutputStream();
        DumpArchiveOutputStream testArchiveOutput = new DumpArchiveOutputStream(testArchiveStream);

        testArchiveOutput.putNextEntry(new DumpArchiveEntry("test1.txt", "test data"));
        testArchiveOutput.write(new byte[]{"test data".getBytes(Charset.forName("UTF-8"))});
        testArchiveOutput.finishStream();

        testArchiveOutput.putNextEntry(new DumpArchiveEntry("test2.txt", "test data"));
        testArchiveOutput.write(new byte[]{"test data".getBytes(Charset.forName("UTF-8"))});
        testArchiveOutput.finishStream();

        testArchiveOutput.close();

        // read test archive
        ByteArrayInputStream testArchiveInputStream = new ByteArrayInputStream(testArchiveStream.toByteArray());
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(testArchiveInputStream);

        // test archive summary information
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertEquals(2, summary.getNumberOfEntries());

        // test reading all entries
        DumpArchiveEntry entry;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            // test entry data
            byte[] buffer = new byte[entry.getSize()];
            int bytesRead = dumpArchiveInputStream.read(buffer, 0, buffer.length);
            Assert.assertEquals(bytesRead, entry.getSize());
            Assert.assertEquals("test data", new String(buffer, 0, bytesRead, Charset.forName("UTF-8")));
        }

        // test end of archive
        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry);

        // close input stream
        dumpArchiveInputStream.close();
    }
}