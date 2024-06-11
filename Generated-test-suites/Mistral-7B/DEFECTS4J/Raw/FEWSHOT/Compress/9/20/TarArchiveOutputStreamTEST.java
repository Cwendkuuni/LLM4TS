package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarBuffer;
import org.apache.commons.compress.utils.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        tarOS.finish();

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);

        tarOS.finish();

        byte[] expectedTarHeader = ArchiveUtils.toBytes(entry.getHeader(true));
        byte[] expectedTwoEOFRecords = new byte[TarConstants.STANDARD_BLOCKSIZE * 2];
        for (int i = 0; i < expectedTwoEFRecords.length; i++) {
            expectedTwoEFRecords[i] = 0;
        }

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        byte[] actualTarHeader = new byte[expectedTarHeader.length + expectedTwoEFRecords.length];
        System.arraycopy(expectedTarHeader, 0, actualTarHeader, 0, expectedTarHeader.length);
        System.arraycopy(expectedTwoEFRecords, 0, actualTarHeader, expectedTarHeader.length, expectedTwoEFRecords.length);

        assertArrayEquals(actualTarHeader, actualBytes);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.write(new byte[] { 0x01, 0x02, 0x03 });
        tarOS.closeArchiveEntry();

        byte[] expectedTarHeader = ArchiveUtils.toBytes(entry.getHeader(true));
        byte[] expectedData = new byte[] { 0x01, 0x02, 0x03 };

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        byte[] actualTarHeader = new byte[expectedTarHeader.length + expectedData.length];
        System.arraycopy(expectedTarHeader, 0, actualTarHeader, 0, expectedTarHeader.length);
        System.arraycopy(expectedData, 0, actualTarHeader, expectedTarHeader.length, expectedData.length);

        assertArrayEquals(actualTarHeader, actualBytes);

        // Test that closeArchiveEntry actually writes the data to the TarOutputStream
        byte[] expectedTarHeaderAndData = new byte[expectedTarHeader.length + expectedData.length + 1];
        System.arraycopy(expectedTarHeader, 0, expectedTarHeaderAndData, 0, expectedTarHeader.length);
        System.arraycopy(expectedData, 0, expectedTarHeaderAndData, expectedTarHeader.length, expectedData.length);
        expectedTarHeaderAndData[expectedTarHeader.length + expectedData.length] = 0;

        actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        assertArrayEquals(expectedTarHeaderAndData, actualBytes);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setName("verylongfilename");
        tarOS.putArchiveEntry(entry);
        tarOS.write(new byte[] { 0x01, 0x02, 0x03 });
        tarOS.closeArchiveEntry();

        byte[] expectedTarHeader = ArchiveUtils.toBytes(entry.getHeader(true));
        byte[] expectedData = new byte[] { 0x01, 0x02, 0x03 };

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        byte[] actualTarHeader = new byte[expectedTarHeader.length + expectedData.length];
        byte[] actualName = new byte[entry.getName().length()];
        System.arraycopy(expectedTarHeader, 0, actualTarHeader, 0, expectedTarHeader.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes(entry.getName()), 0, actualName, 0, entry.getName().length());
        System.arraycopy(actualName, 0, actualTarHeader, expectedTarHeader.length, actualName.length);
        System.arraycopy(expectedData, 0, actualTarHeader, expectedTarHeader.length + actualName.length, expectedData.length);

        assertArrayEquals(actualTarHeader, actualBytes);

        assertTrue(actualBytes.length < expectedTarHeader.length + expectedData.length);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setName("verylongfilename");
        tarOS.putArchiveEntry(entry);

        // This test should fail with a RuntimeException
        tarOS.finish();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileTruncate() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setName("verylongfilename");
        tarOS.putArchiveEntry(entry);
        tarOS.write(new byte[] { 0x01, 0x02, 0x03 });
        tarOS.closeArchiveEntry();

        byte[] expectedLongName = ArchiveUtils.toAsciiBytes("verylongfilename");
        byte[] truncatedLongName = new byte[TarConstants.NAMELEN];
        System.arraycopy(expectedLongName, 0, truncatedLongName, 0, TarConstants.NAMELEN);

        byte[] expectedTarHeader = ArchiveUtils.toBytes(entry.getHeader(true));
        byte[] expectedData = new byte[] { 0x01, 0x02, 0x03 };

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        byte[] actualTarHeader = new byte[expectedTarHeader.length + truncatedLongName.length + expectedData.length];
        byte[] actualName = new byte[truncatedLongName.length];
        System.arraycopy(expectedTarHeader, 0, actualTarHeader, 0, expectedTarHeader.length);
        System.arraycopy(truncatedLongName, 0, actualName, 0, truncatedLongName.length);
        System.arraycopy(actualName, 0, actualTarHeader, expectedTarHeader.length, actualName.length);
        System.arraycopy(expectedData, 0, actualTarHeader, expectedTarHeader.length + actualName.length, expectedData.length);

        assertArrayEquals(actualTarHeader, actualBytes);

        assertTrue(actualBytes.length < expectedTarHeader.length + expectedData.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileGNU() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setName("verylongfilename");
        tarOS.putArchiveEntry(entry);
        tarOS.write(new byte[] { 0x01, 0x02, 0x03 });
        tarOS.closeArchiveEntry();

        byte[] expectedLongName = ArchiveUtils.toAsciiBytes("verylongfilename");
        byte[] longLinkName = new byte[TarConstants.NAMELEN];
        byte longLinkType = TarConstants.GNU_LONGLINK;

        byte[] expectedTarHeader = ArchiveUtils.toBytes(entry.getHeader(true));
        byte[] expectedData = new byte[] { 0x01, 0x02, 0x03 };
        byte[] longLinkHeader = new byte[TarConstants.LONGNAME_BLOCKSIZE + Long.SIZE];

        System.arraycopy(expectedLongName, 0, longLinkName, 0, TarConstants.NAMELEN);
        longLinkHeader[0] = longLinkType;
        longToByteArray(Long.parseLong(Long.toHexString(expectedLongName.length + Long.SIZE)), longLinkHeader, 1);

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        int totalLength = expectedTarHeader.length + longLinkName.length + longLinkHeader.length + expectedData.length;

        byte[] actualTarHeader = new byte[expectedTarHeader.length];
        byte[] actualLongLinkName = new byte[longLinkName.length];
        byte[] actualLongLinkHeader = new byte[longLinkHeader.length];
        byte[] actualData = new byte[expectedData.length];

        System.arraycopy(expectedTarHeader, 0, actualTarHeader, 0, expectedTarHeader.length);
        System.arraycopy(longLinkName, 0, actualLongLinkName, 0, longLinkName.length);
        System.arraycopy(longLinkHeader, 0, actualLongLinkHeader, 0, longLinkHeader.length);
        System.arraycopy(expectedData, 0, actualData, 0, expectedData.length);

        assertArrayEquals(actualTarHeader, actualBytes, 0, expectedTarHeader.length);
        assertArrayEquals(actualLongLinkName, actualBytes, expectedTarHeader.length, longLinkName.length);
        assertArrayEquals(actualLongLinkHeader, actualBytes, expectedTarHeader.length + longLinkName.length, longLinkHeader.length);
        assertArrayEquals(actualData, actualBytes, expectedTarHeader.length + longLinkName.length + longLinkHeader.length, expectedData.length);
    }

    private void longToByteArray(long value, byte[] dest, int offset) {
        for (int i = 0; i < Long.SIZE / Byte.SIZE; i++) {
            dest[offset + i] = (byte) (value >>> (i * Byte.SIZE));
        }
    }
}