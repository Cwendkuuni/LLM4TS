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
import java.util.PriorityQueue;

/**
 * Unit tests for the DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    /**
     * Test reading a valid dump archive.
     *
     * @throws IOException On error.
     */
    @Test
    public void testValidArchive() throws IOException {
        // create test input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeDumpArchiveHeader(bos);
        writeCLRI(bos, 1, 10, 0L);
        writeBITS(bos, 2, 12);
        writeDirectoryEntry(bos, 1, "dir1", 1, 10, 'd', null);
        writeDirectoryEntry(bos, 2, "file1.txt", 3, 15, 'f', "dir1/");
        writeDirectoryEntry(bos, 3, "file2.txt", 4, 18, 'f', "dir1/");
        writeEndEntry(bos);

        ByteArrayInputStream is = new ByteArrayInputStream(bos.toByteArray());
        DumpArchiveInputStream in = new DumpArchiveInputStream(is, "UTF-8");

        // test getting summary information
        DumpArchiveSummary summary = in.getSummary();
        Assert.assertEquals(1, summary.getNumberOfEntries());
        Assert.assertEquals(1024L, summary.getSize());

        // test reading entries
        DumpArchiveEntry entry = in.getNextEntry();
        Assert.assertEquals("dir1", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertNull(entry.getSize());
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        entry = in.getNextEntry();
        Assert.assertEquals("file1.txt", entry.getName());
        Assert.assertEquals("file1.txt", entry.getSimpleName());
        Assert.assertEquals(15L, entry.getSize());
        Assert.assertEquals(15, in.read(new byte[15], 0, 15));
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        entry = in.getNextEntry();
        Assert.assertEquals("file2.txt", entry.getName());
        Assert.assertEquals("file2.txt", entry.getSimpleName());
        Assert.assertEquals(18L, entry.getSize());
        Assert.assertEquals(18, in.read(new byte[18], 0, 18));
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        Assert.assertNull(in.getNextEntry());

        // test closing the input stream
        in.close();

        // test reading empty archive
        is = new ByteArrayInputStream(new byte[0]);
        in = new DumpArchiveInputStream(is, "UTF-8");
        Assert.assertNull(in.getNextEntry());
        in.close();
    }

    /**
     * Test reading an archive with a missing directory entry.
     *
     * @throws IOException On error.
     */
    @Test
    public void testMissingDirectoryEntry() throws IOException {
        // create test input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeDumpArchiveHeader(bos);
        writeCLRI(bos, 1, 10, 0L);
        writeBITS(bos, 2, 12);
        writeDirectoryEntry(bos, 1, "dir1", 1, 10, 'd', null);
        writeDirectoryEntry(bos, 2, "file1.txt", 3, 15, 'f', "dir1/");
        writeEndEntry(bos);

        ByteArrayInputStream is = new ByteArrayInputStream(bos.toByteArray());
        DumpArchiveInputStream in = new DumpArchiveInputStream(is, "UTF-8");

        // test getting next entry
        DumpArchiveEntry entry = in.getNextEntry();
        Assert.assertEquals("dir1", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertNull(entry.getSize());
        entry = in.getNextEntry();
        Assert.assertEquals("file1.txt", entry.getName());
        Assert.assertEquals("file1.txt", entry.getSimpleName());
        Assert.assertEquals(15L, entry.getSize());
        Assert.assertEquals(15, in.read(new byte[15], 0, 15));
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));

        // test getting entry with missing directory
        entry = in.getNextEntry();
        Assert.assertEquals("file2.txt", entry.getName());
        Assert.assertEquals("file2.txt", entry.getSimpleName());
        Assert.assertEquals(18L, entry.getSize());
        Assert.assertEquals(18, in.read(new byte[18], 0, 18));
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        Assert.assertNull(in.getNextEntry());

        // test closing the input stream
        in.close();
    }

    /**
     * Test reading an archive with a sparse record.
     *
     * @throws IOException On error.
     */
    @Test
    public void testSparseRecord() throws IOException {
        // create test input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeDumpArchiveHeader(bos);
        writeCLRI(bos, 1, 10, 0L);
        writebits(bos, 2, 12, true);
        writeDirectoryEntry(bos, 1, "dir1", 1, 10, 'd', null);
        writeDirectoryEntry(bos, 2, "file1.txt", 3, 15, 'f', "dir1/");
        writeEndEntry(bos);

        ByteArrayInputStream is = new ByteArrayInputStream(bos.toByteArray());
        DumpArchiveInputStream in = new DumpArchiveInputStream(is, "UTF-8");

        // test reading entries
        DumpArchiveEntry entry = in.getNextEntry();
        Assert.assertEquals("dir1", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertNull(entry.getSize());
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        entry = in.getNextEntry();
        Assert.assertEquals("file1.txt", entry.getName());
        Assert.assertEquals("file1.txt", entry.getSimpleName());
        Assert.assertEquals(15L, entry.getSize());
        Assert.assertEquals(15, in.read(new byte[15], 0, 15));
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        entry = in.getNextEntry();
        Assert.assertNull(entry);

        // test closing the input stream
        in.close();
    }

    /**
     * Test reading an archive with an empty directory.
     *
     * @throws IOException On error.
     */
    @Test
    public void testEmptyDirectory() throws IOException {
        // create test input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeDumpArchiveHeader(bos);
        writeCLRI(bos, 1, 10, 0L);
        writeBITS(bos, 2, 12);
        writeDirectoryEntry(bos, 1, "dir1", 1, 10, 'd', null);
        writeEndEntry(bos);

        ByteArrayInputStream is = new ByteArrayInputStream(bos.toByteArray());
        DumpArchiveInputStream in = new DumpArchiveInputStream(is, "UTF-8");

        // test reading entries
        DumpArchiveEntry entry = in.getNextEntry();
        Assert.assertEquals("dir1", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertNull(entry.getSize());
        Assert.assertEquals(-1, in.read(new byte[1], 0, 1));
        Assert.assertNull(in.getNextEntry());

        // test closing the input stream
        in.close();
    }

    /**
     * Test reading a corrupted archive.
     *
     * @throws IOException On error.
     */
    @Test(expected = InvalidFormatException.class)
    public void testCorruptedArchive() throws IOException {
        // create test input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeDumpArchiveHeader(bos);
        writeCLRI(bos, 1, 10, 0L);
        writebits(bos, 2, 12, false);
        writeDirectoryEntry(bos, 1, "dir1", 1, 10, 'd', null);
        writeEndEntry(bos);

        ByteArrayInputStream is = new ByteArrayInputStream(bos.toByteArray());
        DumpArchiveInputStream in = new DumpArchiveInputStream(is, "UTF-8");

        // test getting next entry
        in.getNextEntry();
    }

    /**
     * Write a dump archive header to the given output stream.
     *
     * @param out The output stream to write to.
     * @throws IOException On error.
     */
    private static void writeDumpArchiveHeader(OutputStream out) throws IOException {
        byte[] magic = DumpArchiveConstants.NFS_MAGIC;
        byte[] version = DumpArchiveConstants.VERSION;
        byte[] format = DumpArchiveConstants.FORMAT;
        byte[] padding = new byte[12];

        out.write(magic);
        out.write(version);
        out.write(format);
        out.write(padding);
    }

    /**
     * Write a CLRI segment to the given output stream.
     *
     * @param out The output stream to write to.
     * @param n Entries in the segment.
     * @param len Length of each entry.
     * @param offset Offset to the first entry.
     * @throws IOException On error.
     */
    private static void writeCLRI(OutputStream out, int n, int len, long offset)
        throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        byte[] inodes = new byte[n * DumpArchiveConstants.TP_SIZE];

        DumpArchiveUtil.write32(header, 0, DumpArchiveConstants.SEGMENT_TYPE.CLRI);
        DumpArchiveUtil.write32(header, 4, n);
        DumpArchiveUtil.write32(header, 8, len);
        DumpArchiveUtil.write64(header, 12, offset);

        out.write(header);
        out.write(inodes);
    }

    /**
     * Write a BITS segment to the given output stream.
     *
     * @param out The output stream to write to.
     * @param n Entries in the segment.
     * @throws IOException On error.
     */
    private static void writeBITS(OutputStream out, int n, int len) throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        byte[] lengths = new byte[n * DumpArchiveConstants.TP_SIZE];

        DumpArchiveUtil.write32(header, 0, DumpArchiveConstants.SEGMENT_TYPE.BITS);
        DumpArchiveUtil.write32(header, 4, n);

        out.write(header);
        out.write(lengths);
    }

    /**
     * Write a directory entry to the given output stream.
     *
     * @param out The output stream to write to.
     * @param ino Inode number.
     * @param name File name.
     * @param type File type.
     * @param parentIno Parent inode number.
     * @param len Length of the entry.
     * @param offset File offset.
     * @param padding Padding.
     * @throws IOException On error.
     */
    private static void writeDirectoryEntry(OutputStream out, int ino, String name,
                                          int parentIno, long len, char type, String padding)
        throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        byte[] inoBytes = DumpArchiveUtil.intToBytes(ino);
        byte[] parentInoBytes = DumpArchiveUtil.intToBytes(parentIno);
        byte[] nameBytes = DumpArchiveUtil.encode(name, ZipEncoding.UTF_8).getBytes();
        byte[] lenBytes = DumpArchiveUtil.intToBytes((int) len);
        byte[] typeByte = new byte[]{(byte) type};

        DumpArchiveUtil.write32(header, 0, DumpArchiveConstants.SEGMENT_TYPE.INODE);
        DumpArchiveUtil.write32(header, 4, 1);
        DumpArchiveUtil.write32(header, 8, (int) len + DumpArchiveConstants.TP_SIZE);

        out.write(header);
        out.write(inoBytes);
        out.write(parentInoBytes);
        out.write(typeByte);
        out.write(nameBytes);
        out.write(lenBytes);

        if (padding != null) {
            byte[] paddingBytes = DumpArchiveUtil.encode(padding, ZipEncoding.UTF_8)
                .getBytes();
            out.write(paddingBytes, 0, paddingBytes.length);
        }
    }

    /**
     * Write an end record to the given output stream.
     *
     * @param out The output stream to write to.
     * @throws IOException On error.
     */
    private static void writeEndEntry(OutputStream out) throws IOException {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];

        DumpArchiveUtil.write32(header, 0, DumpArchiveConstants.SEGMENT_TYPE.END);

        out.write(header);
    }

    /**
     * The encoding to use for filenames and labels.
     */
    private static final ZipEncoding zipEncoding = ZipEncoding.UTF_8;
}