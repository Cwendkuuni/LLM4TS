
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructor() {
        assertEquals(512, tarOutput.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        assertEquals(1024, outputStream.size());
        byte[] eofRecord = outputStream.toByteArray();
        for (byte b : eofRecord) {
            assertEquals(0, b);
        }
    }

    @Test
    public void testClose() throws IOException {
        tarOutput.close();
        assertTrue(tarOutput.closed);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(entry);
        assertEquals(entry.getName(), tarOutput.currName);
        assertEquals(0, tarOutput.currBytes);
        tarOutput.closeArchiveEntry();
        
        TarArchiveEntry longEntry = new TarArchiveEntry("veryLongFileName");
        tarOutput.putArchiveEntry(longEntry);
        assertEquals(longEntry.getName(), tarOutput.currName);
        assertEquals(0, tarOutput.currBytes);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutput.closeArchiveEntry();
        assertEquals(0, tarOutput.assemLen);
        assertEquals(0, tarOutput.currBytes);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidBytes() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[10], 0, 20);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = new byte[512];
        tarOutput.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutput.write(data, 0, data.length);
        assertEquals(512, tarOutput.assemLen);
        assertEquals(512, tarOutput.currBytes);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile");
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, "testFile");
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("testFile", entry.getName());
    }
}
