package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    private static ByteArrayOutputStream outputStream;

    @BeforeClass
    public static void setup() {
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testConstructor1() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarOutput);
    }

    @Test
    public void testConstructor2() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream, 512);
        assertNotNull(tarOutput);
    }

    @Test
    public void testConstructor3() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream, 512, 1024);
        assertNotNull(tarOutput);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        tarOutput.setLongFileMode(0);
        assertEquals(0, tarOutput.getLongFileMode());
        tarOutput.setLongFileMode(1);
        assertEquals(1, tarOutput.getLongFileMode());
        tarOutput.setLongFileMode(2);
        assertEquals(2, tarOutput.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        tarOutput.finish();
        tarOutput.close();
        assertFalse(outputStream.toString().isEmpty());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        tarOutput.close();
        assertTrue(tarOutput.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        assertEquals(512, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("file.txt"), "file.txt");
        tarOutput.putArchiveEntry(entry);
        assertEquals("file.txt", tarOutput.getCurrentEntry().getName());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("file.txt"), "file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        assertNull(tarOutput.getCurrentEntry());
    }

    @Test
    public void testWrite1() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("file.txt"), "file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("This is a test".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        assertFalse(outputStream.toString().isEmpty());
    }

    @Test
    public void testWrite2() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("file.txt"), "file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("This is a test".getBytes(), 0, 4);
        tarOutput.write("This is a test".getBytes(), 4, 10);
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        assertFalse(outputStream.toString().isEmpty());
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        tarOutput.flush();
        tarOutput.close();
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutput.createArchiveEntry(new File("file.txt"), "file.txt");
        assertNotNull(entry);
    }
}