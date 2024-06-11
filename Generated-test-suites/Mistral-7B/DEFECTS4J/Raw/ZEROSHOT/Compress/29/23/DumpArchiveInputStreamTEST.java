import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.DumpArchiveConstants;
import org.apache.commons.compress.utils.DumpArchiveUtil;
import org.apache.commons.compress.utils.TapeInputStream;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DumpArchiveInputStreamTest {

    @Test
    public void testDumpArchiveInputStream() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // write header
        bos.write(DumpArchiveConstants.MAGIC);
        bos.write(0x00, 1, DumpArchiveConstants.TP_SIZE - 1);
        bos.write(DumpArchiveConstants.NFS_MAGIC);
        bos.write(0x00, 1, 12);
        bos.write(0x00, 1, 12);
        bos.write(0x01, 1, 10);
        bos.write(DumpArchiveConstants.TP_SIZE, 4, 1);
        bos.write(0x01, 1, 1);
        bos.write(DumpArchiveConstants.TP_SIZE, 4, 0);
        bos.write(0x00, 1, 2);
        bos.write(DumpArchiveConstants.TP_SIZE, 4, 2);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);
        bos.write(0x00, 1, 4);

        headerBytes = bos.toByteArray();

        // set up input stream
        InputStream is = new ByteArrayInputStream(headerBytes);
        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding(null);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, encoding);

        // test getSummary()
        DumpArchiveSummary summary = inputStream.getSummary();
        assertEquals(1, summary.getVolumeLabelCount());
        assertEquals("", summary.getVolumeLabel(0));
        assertEquals(1, summary.getArchiveFormatVersion());
        assertEquals(1, summary.getBlockSize());
        assertEquals(1, summary.getNumberOfRecords());
        assertEquals(0, summary.getTotalBytes());
        assertEquals(0, summary.getTotalFiles());

        // test getNextEntry()
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(0, entry.getHeaderOffset());
        assertEquals(0, entry.getSize());
        assertEquals(DumpArchiveConstants.S_IFDIR, entry.getFileType());
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getInode());
        assertEquals(0, entry.getParentIno());

        // test getNextEntry() again (should be null)
        entry = inputStream.getNextEntry();
        assertNull(entry);

        // test close()
        inputStream.close();
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(headerBytes), encoding);
        entry = inputStream.getNextEntry();
        assertEquals(0, entry.getHeaderOffset());
        assertEquals(0, entry.getSize());
        assertEquals(DumpArchiveConstants.S_IFDIR, entry.getFileType());
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getInode());
        assertEquals(0, entry.getParentIno());
        inputStream.close();

        // test matches()
        byte[] wrongMagic = new byte[]{0x01};
        assertFalse(DumpArchiveInputStream.matches(wrongMagic, 1));

        byte[] wrongLength = new byte[1];
        assertFalse(DumpArchiveInputStream.matches(headerBytes, wrongLength.length));

        byte[] correctLength = new byte[DumpArchiveConstants.TP_SIZE];
        System.arraycopy(headerBytes, 0, correctLength, 0, DumpArchiveConstants.TP_SIZE);
        assertTrue(DumpArchiveInputStream.matches(correctLength, DumpArchiveConstants.TP_SIZE));

        // test getPath()
        Map<Integer, DumpArchiveEntry> pending = new HashMap<>();
        DumpArchiveEntry testEntry = new DumpArchiveEntry(1, 1, 0, "test");
        pending.put(1, testEntry);

        String result = DumpArchiveInputStreamTest.getPath(pending, testEntry);
        assertNull(result);
    }

    private static String getPath(Map<Integer, DumpArchiveEntry> pending,
                                DumpArchiveEntry entry) {
        Stack<String> elements = new Stack<>();
        Dirent dirent = null;

        for (int i = entry.getIno();; i = dirent.getParentIno()) {
            if (!pending.containsKey(i)) {
                elements.clear();
                break;
            }

            dirent = pending.get(i);
            elements.push(dirent.getName());

            if (dirent.getIno() == dirent.getParentIno()) {
                break;
            }
        }

        if (elements.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder(elements.pop());

        while (!elements.isEmpty()) {
            sb.append('/');
            sb.append(elements.pop());
        }

        return sb.toString();
    }
}