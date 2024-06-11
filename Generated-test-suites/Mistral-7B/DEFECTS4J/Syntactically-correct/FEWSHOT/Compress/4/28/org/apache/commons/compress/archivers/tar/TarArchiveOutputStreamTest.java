package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/bb/cc/dd/ee/ff/gg/hh/ii/jj/kk/ll/mm/nn/oo/pp/qq/rr/ss/tt/uu/vv/ww/xx/yy/aaa/bbb/ccc";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Verify that the long file name has been written correctly
        byte[] expectedLongFileNameBytes = longFileName.getBytes();
        byte[] recordBuf = tarArchiveOutputStream.recordBuf;
        byte[] assemBuf = tarArchiveOutputStream.assemBuf;
        int assemLen = tarArchiveOutputStream.assemLen;

        // The first record should contain the LongName header
        assertEquals(TarConstants.LF_HDR_MAGIC, recordBuf[0]);
        assertEquals(TarConstants.LF_GNUTYPE_LONGNAME, recordBuf[1]);
        assertEquals(TarConstants.LF_FLAVOR_USTAR, recordBuf[2]);
        assertEquals(TarConstants.LF_FILETYPE_REG, recordBuf[3]);

        // The second record should contain the long file name
        assertEquals(expectedLongFileNameBytes.length + 1, recordBuf[4]); // +1 for NUL terminator
        assertEquals(longFileName.length(), (int) TarArchiveOutputStream.readUnsignedShort(recordBuf, 5));
        assertEquals(expectedLongFileNameBytes, new String(recordBuf, 7).getBytes());

        // The third record should contain the file data (it's empty in this case)
        assertEquals(0, recordBuf[0]);
        assertEquals(0, recordBuf[1]);
    }

    @Test
    public void testPutArchiveEntryWithFileNameLongerThanNameLengthLimit()
            throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/bb/cc/dd/ee/ff/gg/hh/ii/jj/kk/ll/mm/nn/oo/pp/qq/rr/ss/tt/uu/vv/ww/xx/yy/aaa/bbb/ccc/ddd/eee/ffff";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Verify that an exception is thrown
        assertThrows(RuntimeException.class, () -> {
            tarArchiveOutputStream.close();
        });

        // Verify that the data in the output stream is empty
        byte[] expectedEmptyOutput = new byte[0];
        byte[] output = ((ByteArrayOutputStream) tarArchiveOutputStream.out).toByteArray();
        assertArrayEquals(expectedEmptyOutput, output);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        File directory = new File("src/test/resources/testDirectory");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(directory, directory.getName());
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Verify that the directory header has been written correctly
        byte[] recordBuf = tarArchiveOutputStream.recordBuf;
        byte[] assemBuf = tarArchiveOutputStream.assemBuf;
        int assemLen = tarArchiveOutputStream.assemLen;

        assertEquals(TarConstants.LF_HDR_MAGIC, recordBuf[0]);
        assertEquals(TarConstants.LF_FILETYPE_DIR, recordBuf[1]);
        assertEquals(TarConstants.LF_FLAVOR_USTAR, recordBuf[2]);
        assertEquals(TarConstants.LF_NAME_IS_GNULONGNAME, recordBuf[3]);
        assertEquals(0, recordBuf[4]);
        assertEquals(directory.getName().length(), (int) TarArchiveOutputStream.readUnsignedShort(recordBuf, 5));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] dataToWrite = "data".getBytes();

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt"),
                                                "testFile.txt");
        tarArchiveOutputStream.write(dataToWrite, 0, dataToWrite.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify that the data has been written correctly
        byte[] expectedData = dataToWrite;
        byte[] output = ((ByteArrayOutputStream) tarArchiveOutputStream.out).toByteArray();
        assertArrayEquals(expectedData, output);
    }

    @Test
    public void testWriteWithAssembly() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] dataToWrite = new byte[TarArchiveOutputStream.recordSize - 1];

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt"),
                                                "testFile.txt");

        // Write data in small chunks
        for (int i = 0; i < dataToWrite.length; i += 10) {
            int numToWrite = Math.min(dataToWrite.length - i, TarArchiveOutputStream.recordSize - i);
            tarArchiveOutputStream.write(dataToWrite, i, numToWrite);
        }

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        // Verify that the data has been written correctly
        byte[] expectedData = dataToWrite;
        byte[] output = ((ByteArrayOutputStream) tarArchiveOutputStream.out).toByteArray();
        assertArrayEquals(expectedData, output);
    }

    @Test(expected = IOException.class)
    public void testWriteWithDataBeyondEntrySize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] dataToWrite = new byte[TarArchiveOutputStream.recordSize + 1];

        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(dataToWrite, 0, dataToWrite.length);
        tarArchiveOutputStream.closeArchiveEntry();
    }
}