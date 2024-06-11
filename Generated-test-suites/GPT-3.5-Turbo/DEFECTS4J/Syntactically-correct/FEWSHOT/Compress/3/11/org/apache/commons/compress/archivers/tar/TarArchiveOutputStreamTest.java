
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    private File testFile;
    private ArchiveOutputStream tarOut;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.tar");
        tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
    }

    @After
    public void tearDown() {
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        ArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        ArchiveEntry readEntry = null;
        while ((readEntry = tarIn.getNextEntry()) != null) {
            assertEquals("testFile.txt", readEntry.getName());
            assertFalse(readEntry.isDirectory());
            assertEquals(0, readEntry.getSize());
            assertEquals(new Date(0), readEntry.getLastModifiedDate());
        }
        tarIn.close();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        byte[] data = "Hello, world!".getBytes();
        entry.setSize(data.length);
        tarOut.putArchiveEntry(entry);
        tarOut.write(data);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        ArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        ArchiveEntry readEntry = null;
        while ((readEntry = tarIn.getNextEntry()) != null) {
            assertEquals("testFile.txt", readEntry.getName());
            assertFalse(readEntry.isDirectory());
            assertEquals(data.length, readEntry.getSize());

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = tarIn.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            byte[] readData = outStream.toByteArray();
            assertEquals(new String(data), new String(readData));
        }
        tarIn.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        byte[] data = "Hello, world!".getBytes();
        entry.setSize(data.length);
        tarOut.putArchiveEntry(entry);
        tarOut.write(data);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        ArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        ArchiveEntry readEntry = null;
        while ((readEntry = tarIn.getNextEntry()) != null) {
            assertEquals("testFile.txt", readEntry.getName());
            assertFalse(readEntry.isDirectory());
            assertEquals(data.length, readEntry.getSize());

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = tarIn.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            byte[] readData = outStream.toByteArray();
            assertEquals(new String(data), new String(readData));
        }
        tarIn.close();
    }

    @Test
    public void testFinish() throws IOException {
        tarOut.finish();
        tarOut.close();
        ArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(testFile));
        ArchiveEntry readEntry = null;
        while ((readEntry = tarIn.getNextEntry()) != null) {
            assertTrue(readEntry.isDirectory());
            assertNull(readEntry.getName());
        }
        tarIn.close();
    }

    @Test
    public void testClose() throws IOException {
        tarOut.close();
        assertTrue(testFile.exists());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarOut.createArchiveEntry(inputFile, "testFile.txt");
        assertNull(entry.getLastModifiedDate());
        assertEquals(0, entry.getSize());
        assertFalse(entry.isDirectory());
        assertEquals("testFile.txt", entry.getName());
    }

}
