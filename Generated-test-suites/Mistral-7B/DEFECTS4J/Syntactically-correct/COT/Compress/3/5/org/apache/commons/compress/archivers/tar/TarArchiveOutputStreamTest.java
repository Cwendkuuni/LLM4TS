import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        // Verify the output
        byte[] output = tarArchiveOutputStream.getBuffer().getBuffer();
        int headerLength = TarConstants.HEADER_LENGTH;

        // Verify the header record
        for (int i = 0; i < headerLength; i++) {
            Assert.assertEquals(0, output[i]);
        }

        int nameLength = fileName.length();
        for (int i = 0; i < nameLength; i++) {
            int index = TarConstants.NAME_FIELD_START + i;
            Assert.assertEquals((byte) fileName.charAt(i), output[index]);
        }

        Assert.assertEquals(TarConstants.FILE_TYPE_REGULAR, output[headerLength + TarConstants.FILE_TYPE_FIELD_START]);
        Assert.assertEquals(0, output[headerLength + TarConstants.FILE_NAME_LENGTH_FIELD_START]);
        Assert.assertEquals(fileContent.length, output[headerLength + TarConstants.FILE_SIZE_FIELD_START]);
        Assert.assertEquals(0, output[headerLength + TarConstants.MOD_TIME_FIELD_START]);
        Assert.assertEquals(0, output[headerLength + TarConstants.CHECKSUM_FIELD_START]);
        Assert.assertEquals(0, output[headerLength + TarConstants.NAME_FIELD_START]);

        int recordLength = TarConstants.RECORD_LENGTH;
        for (int i = 0; i < recordLength; i++) {
            if (i < fileContent.length) {
                Assert.assertEquals(fileContent[i], output[headerLength + TarConstants.HEADER_LENGTH + i]);
            } else {
                Assert.assertEquals(0, output[headerLength + TarConstants.HEADER_LENGTH + i]);
            }
        }

        // Verify the second header record and EOF records
        for (int i = 0; i < TarConstants.HEADER_LENGTH; i++) {
            Assert.assertEquals(0, output[headerLength * 2 + i]);
        }
        for (int i = 0; i < TarConstants.HEADER_LENGTH; i++) {
            Assert.assertEquals(0, output[headerLength * 2 + TarConstants.HEADER_LENGTH + i]);
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFileNameLength() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFile".repeat(TarConstants.NAMELEN + 1);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Object invalidArchiveEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(invalidArchiveEntry);
    }
}