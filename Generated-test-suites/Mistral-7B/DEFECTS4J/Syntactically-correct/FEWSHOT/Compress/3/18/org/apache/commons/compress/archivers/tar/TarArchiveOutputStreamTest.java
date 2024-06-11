package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
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
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String entryName = "test.txt";
        String longFileName = "longTestFileName_" + TarConstants.NAMELEN + "long";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("test.txt"), entryName);
        TarArchiveEntry longFileNameTarArchiveEntry = new TarArchiveEntry(new File("test.txt"), longFileName);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        assertEquals(entryName, tarArchiveOutputStream.currName);
        assertEquals(tarArchiveEntry.getSize(), tarArchiveOutputStream.currSize);

        // long file name test
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(longFileNameTarArchiveEntry);
        assertEquals(longFileName, tarArchiveOutputStream.currName);
        assertEquals(-1, tarArchiveOutputStream.currSize);

        byte[] longFileNameBytes = longFileName.getBytes();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream.write(longFileNameBytes);
        byte[] longFileNameWrittenBytes = byteArrayOutputStream.toByteArray();
        assertEquals(longFileNameBytes.length + 1, longFileNameWrittenBytes.length);
        for (int i = 0; i < longFileNameBytes.length; i++) {
            assertEquals(longFileNameBytes[i], longFileNameWrittenBytes[i]);
        }
        assertEquals('\0', longFileNameWrittenBytes[longFileNameBytes.length]);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarArchiveEntry = mock(TarArchiveEntry.class);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] recordBytes = new byte[TarConstants.STANDARD_RECORD_LEN];
        when(tarArchiveEntry.getRecordBytes(recordBytes)).thenReturn(recordBytes.length);

        byte[] assemBuf = new byte[TarConstants.STANDARD_RECORD_LEN];
        tarArchiveOutputStream.assemBuf = assemBuf;

        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(recordBytes.length, tarArchiveOutputStream.assemLen);
        assertNull(tarArchiveOutputStream.currName);
        assertEquals(0, tarArchiveOutputStream.currBytes);
        assertEquals(0, tarArchiveOutputStream.currSize);

        // write record
        tarArchiveOutputStream.write(assemBuf);
        assertEquals(0, tarArchiveOutputStream.assemLen);
        assertNull(tarArchiveOutputStream.currName);
        assertEquals(0, tarArchiveOutputStream.currBytes);
        assertEquals(recordBytes.length, tarArchiveOutputStream.currSize);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        byte[] writeBuf = "test".getBytes();
        byte[] assemBuf = new byte[TarConstants.STANDARD_RECORD_LEN];

        tarArchiveOutputStream.write(writeBuf);
        assertEquals(writeBuf.length, tarArchiveOutputStream.currBytes);
        assertEquals(writeBuf.length, tarArchiveOutputStream.currSize);
        assertEquals(0, tarArchiveOutputStream.assemLen);
        assertNull(tarArchiveOutputStream.currName);

        // write multiple records
        byte[] writeBuf2 = "test2".getBytes();
        tarArchiveOutputStream.write(writeBuf2);
        assertEquals(writeBuf.length + writeBuf2.length, tarArchiveOutputStream.currBytes);
        assertEquals(writeBuf.length + writeBuf2.length, tarArchiveOutputStream.currSize);
        assertEquals(0, tarArchiveOutputStream.assemLen);
        assertNull(tarArchiveOutputStream.currName);

        // write record smaller than record size
        byte[] writeBuf3 = "test3".getBytes();
        tarArchiveOutputStream.assemBuf = assemBuf;
        tarArchiveOutputStream.write(writeBuf3);
        assertEquals(writeBuf.length + writeBuf2.length + writeBuf3.length, tarArchiveOutputStream.currBytes);
        assertEquals(writeBuf.length + writeBuf2.length + writeBuf3.length, tarArchiveOutputStream.currSize);
        assertEquals(writeBuf3.length, tarArchiveOutputStream.assemLen);
        assertNull(tarArchiveOutputStream.currName);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.finish();
        assertEquals(0, tarArchiveOutputStream.currBytes);
        assertEquals(0, tarArchiveOutputStream.currSize);
        assertEquals(0, tarArchiveOutputStream.assemLen);
        assertNull(tarArchiveOutputStream.currName);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        assertNull(tarArchiveOutputStream.currName);
        assertNull(tarArchiveOutputStream.currBytes);
        assertNull(tarArchiveOutputStream.currSize);
        assertNull(tarArchiveOutputStream.assemLen);
        assertEquals(false, tarArchiveOutputStream.closed);

        tarArchiveOutputStream.close();
        assertNull(tarArchiveOutputStream.currName);
        assertNull(tarArchiveOutputStream.currBytes);
        assertNull(tarArchiveOutputStream.currSize);
        assertNull(tarArchiveOutputStream.assemLen);
        assertEquals(true, tarArchiveOutputStream.closed);
    }
}