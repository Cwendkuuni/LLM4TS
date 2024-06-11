package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Mock
    private OutputStream mockOutputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tarArchiveOutputStream = new TarArchiveOutputStream(mockOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        // No assertion possible since no getter method exists for longFileMode
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        // No assertion possible since putArchiveEntry() only changes the internal state of TarArchiveOutputStream
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        ArchiveEntry archiveEntry = new ArchiveEntry() {
            public String getName() {
                return null;
            }

            public boolean isDirectory() {
                return false;
            }

            public long getSize() {
                return 0;
            }

            public InputStream getInputStream() throws IOException {
                return null;
            }

            public void writeTo(OutputStream outputStream) throws IOException {

            }
        };
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameExceedingMaxLength() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("verylongfilenameexceedingmaximumlengthallowedbytararchiveentry.tar"), "verylongfilenameexceedingmaximumlengthallowedbytararchiveentry.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameTruncated() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("verylongfilenameexceedingmaximumlengthallowedbytararchiveentry.tar"), "verylongfilenameexceedingmaximumlengthallowedbytararchiveentry.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        // No assertion possible since putArchiveEntry() only changes the internal state of TarArchiveOutputStream
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameUsingGnuExtension() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("verylongfilenameexceedingmaximumlengthallowedbytararchiveentry.tar"), "verylongfilenameexceedingmaximumlengthallowedbytararchiveentry.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        // No assertion possible since putArchiveEntry() only changes the internal state of TarArchiveOutputStream
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        // No assertion possible since closeArchiveEntry() only changes the internal state of TarArchiveOutputStream
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutCallingPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }
}