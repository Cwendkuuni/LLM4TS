import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.Dirent;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.PriorityQueue;

public class DumpArchiveInputStreamTest {

    @Test
    public void testSimpleNextEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry nextEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("test data", nextEntry.getName());

        dumpArchiveInputStream.close();
    }

    @Test
    public void testMultipleEntries() throws IOException {
        byte[] testData = new byte[1024];
        Arrays.fill(testData, (byte) 0xFF);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        int entriesCount = 5;
        for (int i = 0; i < entriesCount; i++) {
            DumpArchiveEntry nextEntry = dumpArchiveInputStream.getNextEntry();
            Assert.assertNotNull(nextEntry);
            Assert.assertTrue(nextEntry.isDirectory());
            dumpArchiveInputStream.closeEntry();
        }

        DumpArchiveEntry lastEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(lastEntry);
        Assert.assertFalse(lastEntry.isDirectory());
        Assert.assertEquals("test" + (entriesCount + 1), lastEntry.getName());

        dumpArchiveInputStream.close();
    }

    @Test
    public void testZeroLengthEntry() throws IOException {
        byte[] testData = new byte[1024];
        Arrays.fill(testData, (byte) 0xFF);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();

        DumpArchiveEntry zeroLengthEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(zeroLengthEntry);
        Assert.assertFalse(zeroLengthEntry.isDirectory());
        Assert.assertEquals("", zeroLengthEntry.getName());

        dumpArchiveInputStream.close();
    }

    @Test
    public void testMissingParentEntry() throws IOException {
        String testData = "dir/file.txt\n" +
                "200 040945 dir/\n" +
                "0 0000000 dir/\n" +
                "100 040755 file.txt\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry dirEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(dirEntry);
        Assert.assertTrue(dirEntry.isDirectory());
        Assert.assertEquals("dir", dirEntry.getName());

        DumpArchiveEntry fileEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(fileEntry);
        Assert.assertFalse(fileEntry.isDirectory());
        Assert.assertEquals("file.txt", fileEntry.getName());

        dumpArchiveInputStream.close();
    }

    @Test
    public void testGzipCompressedArchive() throws IOException {
        String testData = "07040100 00014ed608014e546c69627221000000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "0600080700 00014ed608014e546c6962722100000030000\n" +
                "415441544154010000000100010000010000\n" +
                "5842535342534253584253534253010000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "0000000000000000010000\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        InputStream compressedStream = new GzipCompressorInputStream(inputStream);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(compressedStream, null);

        DumpArchiveEntry nextEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("file1.gz", nextEntry.getName());
        Assert.assertTrue(nextEntry.isDirectory());

        nextEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("file2.gz", nextEntry.getName());
        Assert.assertFalse(nextEntry.isDirectory());

        dumpArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testMalformedArchive() throws IOException {
        String testData = "07040100 00014ed608014e546c69627221000000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "0600080700 00014ed608014e546c6962722100000030000\n" +
                "415441544154010000000100010000010000\n" +
                "5842535342534253584253534253010000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "1c 01010000010140002b554e49542d313336332e3231383536376578\n" +
                "00014ed608014e546c69627221000000010000\n" +
                "0000000000000000010000\n" +
                "01 00000005\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        dumpArchiveInputStream.getNextEntry();

        // Malformed archive, the last entry has an unexpected format
    }
}