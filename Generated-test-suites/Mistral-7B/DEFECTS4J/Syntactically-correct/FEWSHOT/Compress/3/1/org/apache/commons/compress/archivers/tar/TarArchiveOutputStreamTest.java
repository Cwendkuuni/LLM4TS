package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/baa/bba/bcá/bda/beá/bfá/bgá/bhá/biá/bjá/bká/blá/bmá/bná/boá/bpá/bqá/brá/bsá/btá/buá/bvá/bwá/bxá/byá/bzá/baaa/bbaa/bcáa/bdaa/beáa/bfáa/bgáa/bháa/biáa/bjáa/bkáa/bláa/bmáa/bnáa/boáa/bpáa/bqáa/bráa/bsáa/btáa/buáa/bváa/bwáa/bxáa/byáa/bzáa";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(longFileName.getBytes(), 0, longFileName.getBytes().length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] bytes = outputStream.toByteArray();

        // The first 512 bytes of the output are expected to be zeros, which represents the EOF record
        byte[] eofRecord = new byte[512];
        Assert.assertArrayEquals(eofRecord, Arrays.copyOfRange(bytes, 0, 512));

        // The next 512 bytes of the output are expected to be the header of the last entry, which includes the long file name
        byte[] lastEntryHeader = new byte[512];
        System.arraycopy(bytes, 512, lastEntryHeader, 0, lastEntryHeader.length);
        TarEntryHeader lastEntryHeaderTar = new TarEntryHeader(lastEntryHeader);
        Assert.assertEquals(lastEntryHeaderTar.getMagic(), TarConstants.MAGIC_TAR);
        Assert.assertEquals(lastEntryHeaderTar.getVersion(), TarConstants.VERSION_STANDARD);
        Assert.assertEquals(lastEntryHeaderTar.getUserID(), 0);
        Assert.assertEquals(lastEntryHeaderTar.getGroupID(), 0);
        Assert.assertEquals(lastEntryHeaderTar.getFileSize(), tarArchiveEntry.getSize());
        Assert.assertEquals(lastEntryHeaderTar.getModificationTime(), tarArchiveEntry.getTimeCreated().getTime());
        Assert.assertEquals(lastEntryHeaderTar.getFileNameLength(), (short) (longFileName.getBytes().length + 1)); // +1 for NUL terminator
        Assert.assertEquals(lastEntryHeaderTar.getNameIndex(), (short) 0); // assuming no name index is used in this test case
        Assert.assertEquals(lastEntryHeaderTar.getFileType(), (byte) TarEntryType.REGULAR_FILE.getValue());
        Assert.assertEquals(lastEntryHeaderTar.getLinkNameIndex(), (short) 0);
        Assert.assertEquals(lastEntryHeaderTar.getLinkNameLength(), (short) 0);
        Assert.assertEquals(lastEntryHeaderTar.getMagicTail(), TarConstants.MAGIC_TAR);

        // The rest of the output is expected to be the long file name, followed by zeros to make up the record size
        int expectedLongFileNameLength = longFileName.getBytes().length + 1; // +1 for NUL terminator
        int recordSize = tarArchiveOutputStream.getRecordSize();
        int bytesToTest = recordSize - (512 + expectedLongFileNameLength);
        byte[] longFileNameBytes = Arrays.copyOfRange(bytes, 512 + expectedLongFileNameLength, bytes.length);
        Assert.assertArrayEquals(Arrays.fill(new byte[bytesToTest], (byte) 0), longFileNameBytes);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");

        // Test with the default LONGFILE_ERROR mode
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        byte[] bytes = outputStream.toByteArray();
        byte[] eofRecord = new byte[512];
        Assert.assertArrayEquals(eofRecord, Arrays.copyOfRange(bytes, 0, 512));

        // Test with LONGFILE_TRUNCATE mode
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        bytes = outputStream.toByteArray();
        eofRecord = new byte[512];
        Assert.assertArrayEquals(eofRecord, Arrays.copyOfRange(bytes, 0, 512));
        Assert.assertEquals(bytes.length, 512);

        // Test with LONGFILE_GNU mode
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt", "longFileName");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        bytes = outputStream.toByteArray();
        eofRecord = new byte[512];
        Assert.assertArrayEquals(eofRecord, Arrays.copyOfRange(bytes, 0, 512));
        Assert.assertEquals(bytes.length, 1024);

        int expectedLongFileNameLength = "longFileName".getBytes().length + 1; // +1 for NUL terminator
        int recordSize = tarArchiveOutputStream.getRecordSize();
        int bytesToTest = recordSize - (512 + expectedLongFileNameLength);
        byte[] longFileNameBytes = Arrays.copyOfRange(bytes, 512 + expectedLongFileNameLength, bytes.length - 512);
        Assert.assertArrayEquals(Arrays.copyOf("longFileName".getBytes(), "longFileName".getBytes().length), longFileNameBytes);
        byte[] lastEntryHeader = new byte[512];
        System.arraycopy(bytes, bytes.length - 512, lastEntryHeader, 0, lastEntryHeader.length);
        TarEntryHeader lastEntryHeaderTar = new TarEntryHeader(lastEntryHeader);
        Assert.assertEquals(lastEntryHeaderTar.getNameIndex(), (short) 0); // assuming no name index is used in this test case
        Assert.assertEquals(lastEntryHeaderTar.getLinkNameLength(), (short) 0);
    }
}