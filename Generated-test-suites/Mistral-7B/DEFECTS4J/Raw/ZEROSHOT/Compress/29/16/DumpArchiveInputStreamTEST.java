import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] headerBytes = DumpArchiveUtil.createHeader();
        outputStream.write(headerBytes);

        byte[] testData = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
        outputStream.write(testData);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testDumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        DumpArchiveSummary summary = testDumpArchiveInputStream.getSummary();
        Assert.assertEquals(2, summary.getMagicNumber());
        Assert.assertEquals(1, summary.getVolSize());
        Assert.assertEquals(2, summary.getVolBlockSize());

        DumpArchiveEntry entry = testDumpArchiveInputStream.getNextEntry();
        Assert.assertEquals(".", entry.getName());
    }

    @Test
    public void testGetNextEntryWithMultipleEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] headerBytes = DumpArchiveUtil.createHeader();
        outputStream.write(headerBytes);

        byte[] testData1 = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
        outputStream.write(testData1);

        byte[] testData2 = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
        outputStream.write(testData2);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testDumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        DumpArchiveEntry entry1 = testDumpArchiveInputStream.getNextEntry();
        Assert.assertEquals(".", entry1.getName());
        Assert.assertEquals(".", entry1.getSimpleName());
        entry1.close();

        DumpArchiveEntry entry2 = testDumpArchiveInputStream.getNextEntry();
        Assert.assertEquals(".", entry2.getName());
        Assert.assertEquals(".", entry2.getSimpleName());
        entry2.close();

        DumpArchiveEntry entry3 = testDumpArchiveInputStream.getNextEntry();
        Assert.assertEquals(".", entry3.getName());
        Assert.assertEquals(".", entry3.getSimpleName());
        entry3.close();

        DumpArchiveEntry entry4 = testDumpArchiveInputStream.getNextEntry();
        Assert.assertEquals(".", entry4.getName());
        Assert.assertEquals(".", entry4.getSimpleName());
        entry4.close();

        DumpArchiveEntry entry5 = testDumpArchiveInputStream.getNextEntry();
        Assert.assertEquals(".", entry5.getName());
        Assert.assertEquals(".", entry5.getSimpleName());
        entry5.close();

        DumpArchiveEntry entry6 = testDumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry6);

        testDumpArchiveInputStream.close();
    }

    @Test
    public void testReadBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] headerBytes = DumpArchiveUtil.createHeader();
        outputStream.write(headerBytes);

        byte[] testData = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
        outputStream.write(testData);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testDumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        byte[] buffer = new byte[20];
        int bytesRead = testDumpArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(20, bytesRead);

        testDumpArchiveInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testReadBytesAtEndOfStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] headerBytes = DumpArchiveUtil.createHeader();
        outputStream.write(headerBytes);

        byte[] testData = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
        outputStream.write(testData);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testDumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        byte[] buffer = new byte[1];
        int bytesRead = testDumpArchiveInputStream.read(buffer, 0, buffer.length);

        testDumpArchiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
        outputStream.write(testData);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testDumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        testDumpArchiveInputStream.close();
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormat2() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] headerBytes = DumpArchiveUtil.createHeader();
        outputStream.write(headerBytes);

        byte[] testData = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0};
        outputStream.write(testData);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        DumpArchiveInputStream testDumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        testDumpArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] header = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', 0, 0, 0, 8, 0, 0, 4, 4, 4, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, '.', '.', 0, 0, 0, 0, 1, '.', '.', 4, 4, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0};

        Assert.assertTrue(DumpArchiveInputStream.matches(header, header.length));
        Assert.assertFalse(DumpArchiveInputStream.matches(header, header.length - 1));
    }

    @Test
    public void testMatchesWithPartialHeader() {
        byte[] header = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};

        Assert.assertFalse(DumpArchiveInputStream.matches(header, header.length));
    }

    @Test
    public void testMatchesWithInvalidMagicValue() {
        byte[] header = new byte[]{'0', '1', '2', '3', 'F', 'F', 'F', 'F'};

        Assert.assertFalse(DumpArchiveInputStream.matches(header, header.length));
    }
}