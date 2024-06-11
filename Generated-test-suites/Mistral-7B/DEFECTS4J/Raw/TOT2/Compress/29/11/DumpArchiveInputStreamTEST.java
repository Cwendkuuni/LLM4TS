import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class DumpArchiveInputStreamTest {

    @Test
    public void testValidInputStreamWithDefaultEncoding() throws IOException, ArchiveException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        DumpArchiveSummary summary = inputStream.getSummary();

        // When
        DumpArchiveEntry entry = inputStream.getNextEntry();

        // Then
        Assert.assertNotNull(entry);
        Assert.assertEquals(summary.getMagic(), DumpArchiveConstants.NFS_MAGIC);
        Assert.assertEquals(summary.getArchiveName(), "testArchive");

        // Cleanup
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidInputStream() throws IOException {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        // When
        inputStream.getNextEntry();

        // Then - exception should be thrown
    }

    @Test(expected = EOFException.class)
    public void testEndOfStream() throws IOException, ArchiveException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(DumpArchiveConstants.NFS_MAGIC);
        os.write(0); // header type
        os.write(0); // header length
        os.write(0); // record length
        os.write('E'); // end marker
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));

        // When
        DumpArchiveEntry entry = inputStream.getNextEntry();

        // Then - EOFException should be thrown
        inputStream.getNextEntry();
    }

    @Test
    public void testReadEntryData() throws IOException, ArchiveException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(DumpArchiveConstants.NFS_MAGIC);
        os.write(DumpArchiveConstants.SEGMENT_TYPE.FILE.getValue());
        os.write(0); // header length
        os.write(DumpArchiveConstants.TP_SIZE * 2); // record length
        os.write(1); // inode
        os.write("testFile".getBytes()); // filename
        os.write(0); // file type
        os.write(0); // file permissions
        os.write(4096); // file size
        os.write(0); // file size in blocks
        os.write(0); // block size
        os.write(0); // block size in bytes
        os.write(0); // block pointer
        os.write(0); // block pointer in bytes
        os.write(0); // checksum
        os.write(0); // magic value

        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        DumpArchiveEntry entry = inputStream.getNextEntry();

        // When
        byte[] buffer = new byte[4096];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        // Then
        Assert.assertEquals(bytesRead, 4096);
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFileType() throws IOException, ArchiveException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(DumpArchiveConstants.NFS_MAGIC);
        os.write(DumpArchiveConstants.SEGMENT_TYPE.DIRECTORY.getValue());
        os.write(0); // header length
        os.write(DumpArchiveConstants.TP_SIZE * 2); // record length
        os.write(1); // inode
        os.write("testFile".getBytes()); // filename
        os.write(0); // file type
        os.write(0); // file permissions
        os.write(4096); // file size
        os.write(0); // file size in blocks
        os.write(0); // block size
        os.write(0); // block size in bytes
        os.write(0); // block pointer
        os.write(0); // block pointer in bytes
        os.write(0); // checksum
        os.write(0); // magic value

        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        DumpArchiveEntry entry = inputStream.getNextEntry();

        // When
        inputStream.read(new byte[1], 0, 1);

        // Then - ArchiveException should be thrown
    }

    @Test
    public void testReadDirectoryEntry() throws IOException, ArchiveException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(DumpArchiveConstants.NFS_MAGIC);
        os.write(DumpArchiveConstants.SEGMENT_TYPE.DIR.getValue());
        os.write(0); // header length
        os.write(DumpArchiveConstants.TP_SIZE * 2); // record length
        os.write(1); // inode
        os.write("testDir".getBytes()); // directory name
        os.write(0); // parent inode
        os.write(0); // file type
        os.write(4096); // file size
        os.write(0); // file size in blocks
        os.write(0); // block size
        os.write(0); // block size in bytes
        os.write(0); // block pointer
        os.write(0); // block pointer in bytes
        os.write(0); // checksum
        os.write(0); // magic value

        ByteArrayOutputStream dsOs = new ByteArrayOutputStream();
        dsOs.write(DumpArchiveConstants.NFS_MAGIC);
        dsOs.write(DumpArchiveConstants.SEGMENT_TYPE.INODE.getValue());
        dsOs.write(0); // header length
        dsOs.write(DumpArchiveConstants.TP_SIZE * 2); // record length
        dsOs.write(2); // inode
        dsOs.write("testFile".getBytes()); // filename
        dsOs.write(1); // parent inode
        dsOs.write(4); // file type
        dsOs.write(0777); // file permissions
        dsOs.write(4096); // file size
        dsOs.write(1); // file size in blocks
        dsOs.write(512); // block size
        dsOs.write(512); // block size in bytes
        dsOs.write(0); // block pointer
        dsOs.write(0); // block pointer in bytes
        dsOs.write(0); // checksum
        dsOs.write(0); // magic value

        ByteArrayOutputStream dOs = new ByteArrayOutputStream();
        dOs.write(DumpArchiveConstants.NFS_MAGIC);
        dOs.write(DumpArchiveConstants.SEGMENT_TYPE.FILE.getValue());
        dOs.write(0); // header length
        dOs.write(DumpArchiveConstants.TP_SIZE * 2); // record length
        dOs.write(3); // inode
        dOs.write("testFile".getBytes()); // filename
        dOs.write(2); // parent inode
        dOs.write(4); // file type
        dOs.write(0777); // file permissions
        dOs.write(4096); // file size
        dOs.write(1); // file size in blocks
        dOs.write(512); // block size
        dOs.write(512); // block size in bytes
        dOs.write(0); // block pointer
        dOs.write(0); // block pointer in bytes
        dOs.write(0); // checksum
        dOs.write(0); // magic value

        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        DumpArchiveEntry dirEntry = inputStream.getNextEntry();
        DumpArchiveEntry fileEntry = inputStream.getNextEntry();
        DumpArchiveEntry childEntry = inputStream.getNextEntry();

        // When
        String dirPath = dirEntry.getName();
        String childPath = childEntry.getName();
        String filePath = fileEntry.getName();

        // Then
        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.DIR.getValue(), dirEntry.getHeaderType());
        Assert.assertEquals("testDir", dirPath);
        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE.getValue(), dirEntry.getNextHeaderType());

        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE.getValue(), fileEntry.getHeaderType());
        Assert.assertEquals("testFile", filePath);
        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.FILE.getValue(), fileEntry.getNextHeaderType());

        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE.getValue(), childEntry.getHeaderType());
        Assert.assertEquals("testFile", childPath.substring(0, childPath.lastIndexOf('/')));
        Assert.assertEquals("", childPath.substring(childPath.lastIndexOf('/')));
        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.FILE.getValue(), childEntry.getNextHeaderType());

        Assert.assertEquals(dirPath, childEntry.getParentDirectory().getName());
        Assert.assertEquals(filePath, childEntry.getName());
        Assert.assertEquals(filePath, childEntry.getSimpleName());
    }

    @Test
    public void testInvalidRecordType() throws IOException, ArchiveException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(DumpArchiveConstants.NFS_MAGIC);
        os.write(DumpArchiveConstants.SEGMENT_TYPE.ADDR.getValue());
        os.write(0); // header length
        os.write(DumpArchiveConstants.TP_SIZE * 2); // record length
        os.write(1); // inode
        os.write("testFile".getBytes()); // filename
        os.write(0); // file type
        os.write(4096); // file size
        os.write(1); // file size in blocks
        os.write(512); // block size
        os.write(512); // block size in bytes
        os.write(0); // block pointer
        os.write(0); // block pointer in bytes
        os.write(0); // checksum
        os.write(0); // magic value

        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(os.toByteArray()));
        DumpArchiveEntry entry = inputStream.getNextEntry();

        // Then - IllegalArgumentException should be thrown
    }
}