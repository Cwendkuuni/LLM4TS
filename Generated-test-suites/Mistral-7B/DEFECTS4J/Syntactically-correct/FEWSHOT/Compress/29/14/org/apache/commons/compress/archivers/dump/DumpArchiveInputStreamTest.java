package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test case for DumpArchiveInputStream.
 */
public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        byte[] buffer = new byte[DumpArchiveConstants.TP_SIZE * 2];

        // fill the header with valid values
        for (int i = 0; i < header.length; i++) {
            header[i] = (byte) 0x01;
        }
        byte[] dumpArchive = new byte[header.length + buffer.length];
        System.arraycopy(header, 0, dumpArchive, 0, header.length);
        System.arraycopy(buffer, 0, dumpArchive, header.length, buffer.length);

        assertTrue(DumpArchiveInputStream.matches(dumpArchive, dumpArchive.length));

        // test with a short header
        byte[] shortHeader = new byte[32];
        System.arraycopy(header, 0, shortHeader, 0, 32);
        assertFalse(DumpArchiveInputStream.matches(shortHeader, shortHeader.length));

        // test with random data
        byte[] randomData = new byte[buffer.length];
        for (int i = 0; i < randomData.length; i++) {
            randomData[i] = (byte) (i % 0xFF);
        }
        assertFalse(DumpArchiveInputStream.matches(buffer, buffer.length));
        assertFalse(DumpArchiveInputStream.matches(buffer, buffer.length + randomData.length));
        assertFalse(DumpArchiveInputStream.matches(buffer, randomData.length));
    }

    @Test
    public void testReadEntries() throws IOException, ArchiveException {
        byte[] headerBytes = DumpArchiveUtil.writeMagicNumber();

        // write CLRI record
        byte[] clriBytes = DumpArchiveEntry.writeCLRI(1, 5, 16);
        ByteArrayOutputStream clriStream = new ByteArrayOutputStream();
        clriStream.write(clriBytes);
        headerBytes = DumpArchiveUtil.mergeRecords(headerBytes, clriBytes);

        // write BITS record
        byte[] bitsBytes = DumpArchiveEntry.writeBITS(1, 2, 512);
        ByteArrayOutputStream bitsStream = new ByteArrayOutputStream();
        bitsStream.write(bitsBytes);
        headerBytes = DumpArchiveUtil.mergeRecords(headerBytes, bitsBytes);

        ByteArrayOutputStream archiveStream = new ByteArrayOutputStream();
        archiveStream.write(headerBytes);
        archiveStream.write(DumpArchiveUtil.writeMagicNumber());

        // write ADD record for root directory
        byte[] addBytes = DumpArchiveEntry.writeADD(1, 1, 0, null, ".", 1);
        ByteArrayOutputStream addStream = new ByteArrayOutputStream();
        addStream.write(addBytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(addBytes.length));
        archiveStream.write(addBytes);

        // write ADD record for file1
        byte[] addFile1Bytes = DumpArchiveEntry.writeADD(2, 2, 10, null, "file1", 1);
        ByteArrayOutputStream addFile1Stream = new ByteArrayOutputStream();
        addFile1Stream.write(addFile1Bytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(addFile1Bytes.length));
        archiveStream.write(addFile1Bytes);

        // write ADD record for file2
        byte[] addFile2Bytes = DumpArchiveEntry.writeADD(3, 3, 20, null, "file2", 1);
        ByteArrayOutputStream addFile2Stream = new ByteArrayOutputStream();
        addFile2Stream.write(addFile2Bytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(addFile2Bytes.length));
        archiveStream.write(addFile2Bytes);

        // write INODE record for file1
        byte[] inodeFile1Bytes = DumpArchiveEntry.writeINODE(2, 1, 16, 8, 1, new byte[]{3, 1, 1, 1, 1, 1, 1, 1});
        ByteArrayOutputStream inodeFile1Stream = new ByteArrayOutputStream();
        inodeFile1Stream.write(inodeFile1Bytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(inodeFile1Bytes.length));
        archiveStream.write(inodeFile1Bytes);

        // write INODE record for file2
        byte[] inodeFile2Bytes = DumpArchiveEntry.writeINODE(3, 2, 16, 8, 1, new byte[]{3, 2, 1, 1, 1, 1, 1, 1});
        ByteArrayOutputStream inodeFile2Stream = new ByteArrayOutputStream();
        inodeFile2Stream.write(inodeFile2Bytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(inodeFile2Bytes.length));
        archiveStream.write(inodeFile2Bytes);

        // write DATA record for file1
        byte[] dataFile1Bytes = DumpArchiveEntry.writeDATA(2, 1, 16, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        ByteArrayOutputStream dataFile1Stream = new ByteArrayOutputStream();
        dataFile1Stream.write(dataFile1Bytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(dataFile1Bytes.length));
        archiveStream.write(dataFile1Bytes);

        // write DATA record for file2
        byte[] dataFile2Bytes = DumpArchiveEntry.writeDATA(3, 2, 16, new byte[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        ByteArrayOutputStream dataFile2Stream = new ByteArrayOutputStream();
        dataFile2Stream.write(dataFile2Bytes);
        archiveStream.write(DumpArchiveUtil.writeRecordLength(dataFile2Bytes.length));
        archiveStream.write(dataFile2Bytes);

        byte[] archiveData = archiveStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData);
        ArchiveInputStream ais = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry;
        for (int i = 0; i < 3; i++) {
            entry = ais.getNextEntry();
            assertNotNull(entry);
            assertEquals("file" + i, entry.getName());
        }
        assertNull(ais.getNextEntry());

        ais.close();
        IOUtils.closeQuietly(inputStream);
    }
}