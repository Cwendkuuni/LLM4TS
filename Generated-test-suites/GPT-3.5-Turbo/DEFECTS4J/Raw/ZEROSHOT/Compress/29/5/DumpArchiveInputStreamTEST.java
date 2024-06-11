package org.apache.commons.compress.archivers.dump;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() throws Exception {
        String dumpArchiveHeader = "0123456789abcdefghijklmnopqr";

        ByteArrayInputStream is = new ByteArrayInputStream(dumpArchiveHeader.getBytes());
        dumpArchiveInputStream = new DumpArchiveInputStream(is);
    }

    @After
    public void tearDown() throws Exception {
        dumpArchiveInputStream.close();
    }

    @Test
    public void getNextDumpEntry() throws IOException {
        DumpArchiveEntry dumpArchiveEntry = dumpArchiveInputStream.getNextDumpEntry();
        assertNotNull(dumpArchiveEntry);
    }

    @Test
    public void getNextEntry() throws IOException {
        DumpArchiveEntry dumpArchiveEntry = dumpArchiveInputStream.getNextEntry();
        assertNotNull(dumpArchiveEntry);
    }

    @Test
    public void getSummary() throws IOException {
        DumpArchiveSummary dumpArchiveSummary = dumpArchiveInputStream.getSummary();
        assertNotNull(dumpArchiveSummary);
        assertEquals(DumpArchiveConstants.DUMP_MAGIC,
                dumpArchiveSummary.getTPMagic());
    }

    @Test
    public void read() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = dumpArchiveInputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
    }

    @Test
    public void close() throws IOException {
        dumpArchiveInputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void readWithoutEntry() throws IOException {
        dumpArchiveInputStream.getNextEntry();
        byte[] buffer = new byte[10];
        dumpArchiveInputStream.read(buffer, 0, 10);
    }
}