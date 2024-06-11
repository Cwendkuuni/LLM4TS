package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructor() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        zipEntry.setUnixMode(755);
        assertEquals(755, zipEntry.getUnixMode());
    }

    // Add more test cases for other methods

}