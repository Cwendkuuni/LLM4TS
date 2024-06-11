package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String entryName = "test.txt";
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream, entryName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        tarArchiveOutputStream.write("Hello TarArchiveOutputStream".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(in);

        assertEquals(entryName, readEntry.getName());
        assertEquals(10, readEntry.getSize());

        byte[] entryData = new byte[10];
        readEntry.getData(entryData);
        assertArrayEquals("Hello TarArchiveOutputStream".getBytes(StandardCharsets.UTF_8), entryData);
    }

    @Test
    public void testLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String entryName = "long_file_name_" + new AtomicInteger(1).getAndIncrement() + ".txt";
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream, entryName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] longFileNameBytes = entryName.getBytes(StandardCharsets.UTF_8);
        long fileNameLength = (long) longFileNameBytes.length + 1;

        byte[] longLinkRecord = new byte[fileNameLength + 1];
        System.arraycopy(longFileNameBytes, 0, longLinkRecord, 0, (int) fileNameLength);
        longLinkRecord[fileNameLength] = 0; // NUL terminator

        tarArchiveOutputStream.write(longLinkRecord);
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(in);
        assertEquals(entryName, readEntry.getName());
    }

    @Test
    public void testLongFileNameTruncation() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        String entryName = "long_file_name_" + new AtomicInteger(1).getAndIncrement() + ".txt";
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream, entryName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    @Test
    public void testCloseArchiveEntryWritesAllData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String entryName = "test.txt";
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream, entryName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] dataToWrite = "Additional data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(dataToWrite);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(in);

        byte[] entryData = new byte[10 + dataToWrite.length];
        readEntry.getData(entryData);

        assertArrayEquals("Hello WorldAdditional data".getBytes(StandardCharsets.UTF_8), entryData);
    }
}