import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class DumpArchiveInputStreamTest {

    @Test
    public void testDumpArchiveInputStream() throws IOException {
        byte[] inputData = new byte[] {
                0x00, 0x00, 0x00, 0x00, // Magic value
                0x00, 0x00, 0x00, 0x01, // Version major
                0x00, 0x00, 0x00, 0x01, // Version minor
                0x00, 0x00, 0x00, 0x01, // Flags
                0x01, 0x00, 0x00, 0x00, // Format major
                0x01, 0x00, 0x00, 0x00, // Format minor
                0x00, 0x00, 0x00, 0x00, // File size
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                ' ', '1', '2', '3',
                0x00, 0x00, 0x00, 0x01, // File type
                'd', 'i', 'r', 'e',
                ' ', '1', '2', '3',
                '.', 't', 'x', 't',
                ' ', 0, 0, 0, // File size
                0x01, 0x00, 0x00, 0x01, // Directory inode number
                0x01, 0x00, 0x00, 0x01, // File inode number
                0x01, 0x00, 0x00, 0x04, // File permissions
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                ' ', '1', '2', '4', // File size
                0x00, 0x00, 0x00, 0x00, // Checksum
                0x01, 0x00, 0x00, 0x01, // Next header offset
                0x00, 0x01, 0x00, 0x00, // Address segment header length
                0x00, 0x00, 0x00, 0x00 // Address segment length
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData, 0, inputData.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding(null);

        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, encoding.getName());

        DumpArchiveSummary summary = testStream.getSummary();
        Assert.assertEquals("Incorrect summary version major", 1, summary.getVersionMajor());
        Assert.assertEquals("Incorrect summary version minor", 0, summary.getVersionMinor());
        Assert.assertEquals("Incorrect number of entries", 1, summary.getNumberOfEntries());

        DumpArchiveEntry entry = testStream.getNextEntry();
        Assert.assertNotNull("Failed to read entry", entry);
        Assert.assertEquals("Unexpected entry name", "dat.txt", entry.getName());
        Assert.assertEquals("Unexpected entry type", DumpArchiveEntry.TYPE_FILE, entry.getType());
        Assert.assertEquals("Unexpected entry size", 123, entry.getSize());

        byte[] buffer = new byte[10];
        int read = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Unexpected number of bytes read", 123, testStream.getBytesRead());
        Assert.assertEquals("Unexpected number of bytes read", 123, read);

        testStream.close();
    }

    @Test(expected = EOFException.class)
    public void testEOFException() throws IOException {
        byte[] inputData = new byte[] {
                0x00, 0x00, 0x00, 0x00, // Magic value
                0x00, 0x00, 0x00, 0x01, // Version major
                0x00, 0x00, 0x00, 0x01, // Version minor
                0x00, 0x00, 0x00, 0x01, // Flags
                0x01, 0x00, 0x00, 0x00, // Format major
                0x01, 0x00, 0x00, 0x00, // Format minor
                0x00, 0x00, 0x00, 0x00, // File size
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                ' ', 0, 0, 0, // File size
                0x01, 0x00, 0x00, 0x01, // Directory inode number
                0x01, 0x00, 0x00, 0x01, // File inode number
                0x01, 0x00, 0x00, 0x04, // File permissions
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                0x00 // End of file marker
        };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding(null);

        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, encoding.getName());

        testStream.getNextEntry();

        byte[] buffer = new byte[1];
        int read = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Unexpected number of bytes read", -1, read);
    }

    @Test(expected = ArchiveException.class)
    public void testArchiveException() throws IOException {
        byte[] inputData = new byte[] {
                0x00, 0x00, 0x00, 0x00, // Magic value
                0x00, 0x00, 0x00, 0x01, // Version major
                0x00, 0x00, 0x00, 0x01, // Version minor
                0x00, 0x00, 0x00, 0x01, // Flags
                0x01, 0x00, 0x00, 0x00, // Format major
                0x01, 0x00, 0x00, 0x00, // Format minor
                0x00, 0x00, 0x00, 0x00, // File size
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                0x00, 0x01, 0x00, 0x00, // Address segment header length
                0x00, 0x00, 0x00, 0x00, // Address segment length
                0x01, 0x00, 0x00, 0x01, // Directory inode number
                0x01, 0x00, 0x00, 0x01, // File inode number
                0x01, 0x00, 0x00, 0x04, // File permissions
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                0x00, 0x01, 0x00, 0x01, // Next header offset
                0x00, 0x01, 0x00, 0x01, // CLRI segment header length
                0x00, 0x00, 0x00, 0x00, // CLRI segment length
                0x01, 0x00, 0x00, 0x01 // Invalid header type
        };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding(null);

        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, encoding.getName());

        testStream.getNextEntry();
    }

    @Test(expected = UnrecognizedFormatException.class)
    public void testUnrecognizedFormatException() throws IOException {
        byte[] inputData = new byte[] {
                0x00, 0x00, 0x00, 0x00, // Magic value
                0x00, 0x00, 0x00, 0x01, // Version major
                0x00, 0x00, 0x00, 0x01, // Version minor
                0x00, 0x00, 0x00, 0x01, // Flags
                0x01, 0x00, 0x00, 0x00, // Format major
                0x01, 0x00, 0x00, 0x00, // Format minor
                0x00, 0x00, 0x00, 0x00, // File size
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                0x00, 0x01, 0x00, 0x00, // Address segment header length
                0x00, 0x00, 0x00, 0x00, // Address segment length
                0x01, 0x00, 0x00, 0x01, // Directory inode number
                0x01, 0x00, 0x00, 0x01, // File inode number
                0x01, 0x00, 0x00, 0x04, // File permissions
                0x00, 0x00, 0x00, 0x01, // File name length
                'd', 'u', 'm', 'p',
                ' ', 'a', 'r', 'c',
                'h', 'i', 'v', 'e',
                '.', 'd', 'a', 't',
                '.', 't', 'x', 't',
                0x00, 0x01, 0x00, 0x01, // Next header offset
                0x00, 0x01, 0x00, 0x01, // BITS segment header length
                0x00, 0x00, 0x00, 0x00, // BITS segment length
                0x01, 0x00, 0x00, 0x01 // Invalid header type
        };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding(null);

        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, encoding.getName());

        testStream.getNextEntry();
    }
}