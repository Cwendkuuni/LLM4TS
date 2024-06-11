import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarArchiveOutputStream tarStream;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.tar");
        tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile));
    }

    @After
    public void tearDown() throws Exception {
        if (tarStream != null) {
            tarStream.close();
        }
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testSetLongFileMode() {
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "long_" + TarConstants.NAMELEN + "_filename.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarStream.putArchiveEntry(entry);
        tarStream.write("Long file name".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();
        tarStream.finish();
        tarStream.close();

        byte[] bytes = FileUtils.readByteArray(testFile);
        // Here we assume that the long file name is stored at the end of the tar file
        int longFileNameIndex = bytes.length - TarConstants.TAR_HEADER_SIZE;
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(longFileName));
        byte[] longFileNameBytes = longFileNameEntry.getName().getBytes(StandardCharsets.UTF_8);
        int longFileNameLength = longFileNameBytes.length;
        assertEquals("Unexpected long file name length", longFileNameLength, bytes.length - longFileNameIndex);
        assertNull("Unexpected data before long file name", Arrays.copyOfRange(bytes, 0, longFileNameIndex));
        assertArrayEquals("Unexpected long file name header", longFileNameBytes, Arrays.copyOfRange(bytes, longFileNameIndex, longFileNameIndex + longFileNameLength));
    }

    @Test
    public void testWriteLongFileNameWithLongFileModeGNU() throws IOException {
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "long_" + TarConstants.NAMELEN + "_filename.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarStream.putArchiveEntry(entry);
        tarStream.write("Long file name".getBytes(StandardCharsets.UTF_8));
        tarStream.closeArchiveEntry();
        tarStream.finish();
        tarStream.close();

        byte[] bytes = FileUtils.readByteArray(testFile);
        // Here we assume that the long file name is stored at the end of the tar file
        int longFileNameIndex = bytes.length - TarConstants.TAR_HEADER_SIZE;
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(longFileName));
        byte[] longFileNameBytes = longFileNameEntry.getName().getBytes(StandardCharsets.UTF_8);
        int longFileNameLength = longFileNameBytes.length;
        assertEquals("Unexpected long file name length", longFileNameLength, bytes.length - longFileNameIndex);
        assertArrayEquals("Unexpected long file name header", longFileNameBytes, Arrays.copyOfRange(bytes, longFileNameIndex, longFileNameIndex + longFileNameLength));
        assertArrayEquals("Unexpected long file name link header", TarConstants.GNU_LONGLINK, Arrays.copyOfRange(bytes, longFileNameIndex - TarConstants.TAR_LONGNAME_HEADER_SIZE, longFileNameIndex));
        assertArrayEquals("Unexpected long file name link data", longFileNameBytes, Arrays.copyOfRange(bytes, longFileNameIndex - TarConstants.TAR_LONGNAME_HEADER_SIZE + TarConstants.TAR_LF_GNULINK_NAME_LEN, longFileNameIndex));
    }

    @Test
    public void testWriteLongFileNameWithLongFileModeError() {
        tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        String longFileName = "long_" + TarConstants.NAMELEN + "_filename.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        assertThrows(RuntimeException.class, () -> tarStream.putArchiveEntry(entry));
        tarStream.finish();
        tarStream.close();
    }

    @Test
    public void testWriteEmptyFile() throws IOException {
        String fileName = "empty_file.txt";
        ArchiveEntry entry = new TarArchiveEntry(fileName);
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        tarStream.finish();
        tarStream.close();

        byte[] bytes = FileUtils.readByteArray(testFile);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName));
        byte[] expectedHeader = archiveEntry.getHeader(TarConstants.TAR_GNU).getBytes(StandardCharsets.UTF_8);
        assertEquals("Unexpected header", expectedHeader, Arrays.copyOfRange(bytes, 0, TarConstants.TAR_HEADER_SIZE));
        assertArrayEquals("Unexpected file data", new byte[0], Arrays.copyOfRange(bytes, TarConstants.TAR_HEADER_SIZE, bytes.length));
    }

    @Test
    public void testWriteFile() throws IOException {
        String fileName = "file.txt";
        ArchiveEntry entry = new TarArchiveEntry(fileName);
        byte[] content = "Content of the file".getBytes(StandardCharsets.UTF_8);
        tarStream.putArchiveEntry(entry);
        tarStream.write(content);
        tarStream.closeArchiveEntry();
        tarStream.finish();
        tarStream.close();

        byte[] bytes = FileUtils.readByteArray(testFile);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName));
        byte[] expectedHeader = archiveEntry.getHeader(TarConstants.TAR).getBytes(StandardCharsets.UTF_8);
        byte[] expectedContent = "Content of the file".getBytes(StandardCharsets.UTF_8);
        assertEquals("Unexpected header", expectedHeader, Arrays.copyOfRange(bytes, 0, TarConstants.TAR_HEADER_SIZE));
        assertArrayEquals("Unexpected file data", expectedContent, Arrays.copyOfRange(bytes, TarConstants.TAR_HEADER_SIZE, bytes.length));
    }

    @Test
    public void testWriteDirectory() throws IOException {
        String dirName = "directory";
        String fileName = "file.txt";
        ArchiveEntry dirEntry = new TarArchiveEntry(dirName);
        dirEntry.setDirectory(true);
        tarStream.putArchiveEntry(dirEntry);
        tarStream.finish();
        tarStream.close();

        TarArchiveOutputStream tarStream2 = new TarArchiveOutputStream(new FileOutputStream(new File(testFile, dirName + "/")), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        ArchiveEntry fileEntry = new TarArchiveEntry(new File(fileName));
        tarStream2.putArchiveEntry(fileEntry);
        byte[] content = "Content of the file".getBytes(StandardCharsets.UTF_8);
        tarStream2.write(content);
        tarStream2.closeArchiveEntry();
        tarStream2.finish();
        tarStream2.close();

        byte[] bytes = FileUtils.readByteArray(new File(testFile, dirName + "/" + fileName));
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File(fileName));
        byte[] expectedHeader = archiveEntry.getHeader(TarConstants.TAR).getBytes(StandardCharsets.UTF_8);
        byte[] expectedContent = "Content of the file".getBytes(StandardCharsets.UTF_8);
        assertEquals("Unexpected header", expectedHeader, Arrays.copyOfRange(bytes, 0, TarConstants.TAR_HEADER_SIZE));
        assertArrayEquals("Unexpected file data", expectedContent, Arrays.copyOfRange(bytes, TarConstants.TAR_HEADER_SIZE, bytes.length));

        bytes = FileUtils.readByteArray(testFile);
        TarArchiveEntry archiveDirEntry = new TarArchiveEntry(new File(dirName));
        byte[] expectedDirHeader = archiveDirEntry.getHeader(TarConstants.TAR_DIR).getBytes(StandardCharsets.UTF_8);
        assertEquals("Unexpected directory header", expectedDirHeader, Arrays.copyOfRange(bytes, 0, TarConstants.TAR_HEADER_SIZE));
    }
}