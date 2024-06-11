package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] filemode = new byte[]{(byte) 0644};
        byte[] length = new byte[]{(byte) 0x12, 0x34, 0x56, 0x78, 0x90, 0xAB, 0xCD, 0xEF};
        byte[] userid = "user".getBytes();
        byte[] groupid = "group".getBytes();
        byte[] lastmodified = new byte[]{(byte) 0x11, 0x22, 0x33, 0x44, 0x55, 0x66};

        InputStream inputStream = new ByteArrayInputStream(new byte[]{}, 0, 0);
        ArArchiveInputStream archiverInputStream = new ArArchiveInputStream(inputStream);

        // test EOF
        assertNull(archiverInputStream.getNextArEntry());

        byte[] testData = new byte[header.length + name.length + trailer.length + filemode.length + length.length
                + userid.length + groupid.length + lastmodified.length];
        System.arraycopy(header, 0, testData, 0, header.length);
        System.arraycopy(name, 0, testData, header.length, name.length);
        System.arraycopy(trailer, 0, testData, header.length + name.length, trailer.length);
        System.arraycopy(filemode, 0, testData, header.length + name.length + trailer.length, filemode.length);
        System.arraycopy(length, 0, testData, header.length + name.length + trailer.length + filemode.length, length.length);
        System.arraycopy(userid, 0, testData, header.length + name.length + trailer.length + filemode.length + length.length, userid.length);
        System.arraycopy(groupid, 0, testData, header.length + name.length + trailer.length + filemode.length + length.length + userid.length, groupid.length);
        System.arraycopy(lastmodified, 0, testData, header.length + name.length + trailer.length + filemode.length + length.length + userid.length + groupid.length, lastmodified.length);

        InputStream inputStreamWithData = new ByteArrayInputStream(testData);
        ArArchiveInputStream archiverInputStreamWithData = new ArArchiveInputStream(inputStreamWithData);

        ArArchiveEntry arArchiveEntry = archiverInputStreamWithData.getNextArEntry();
        assertEquals("test.txt", arArchiveEntry.getName());
        assertEquals(11 * 256L + 22 * 256L + 33L + 44L + 55L + 66L + 12L * 256L + 34L * 256L + 56L * 256L + 78L * 256L + 90L * 256L + 102 * 256L + 161L, arArchiveEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        byte[] testData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InputStream inputStreamWithData = new ByteArrayInputStream(testData);
        ArArchiveInputStream archiverInputStreamWithData = new ArArchiveInputStream(inputStreamWithData);

        archiverInputStreamWithData.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithNoData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        ArArchiveInputStream archiverInputStream = new ArArchiveInputStream(inputStream);

        archiverInputStream.getNextArEntry();
    }
}