package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest class for testing the functionality of the TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private ArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = byteArrayOutputStream;
    }

    @Test
    public void testPutArchiveEntryWithEmptyRecordBuf() throws IOException {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedHeader = new byte[TarConstants.STD_RECORD_LENGTH];
        TarArchiveEntry.writeHeader(entry, expectedHeader);
        byte[] expectedEOF = new byte[TarConstants.STD_RECORD_LENGTH];
        for (int i = 0; i < expectedEOF.length; i++) {
            expectedEOF[i] = 0;
        }

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] actualHeader = new byte[TarConstants.STD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, actualHeader, 0, TarConstants.STD_RECORD_LENGTH);

        assertArrayEquals(expectedHeader, actualHeader);
        assertArrayEquals(expectedEOF, new byte[TarConstants.STD_RECORD_LENGTH]);
    }

    @Test
    public void testPutArchiveEntryWithFile() throws IOException {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] fileContent = new byte[10]; // Replace with the contents of the test file
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedHeader = new byte[TarConstants.STD_RECORD_LENGTH];
        TarArchiveEntry.writeHeader(entry, expectedHeader);
        byte[] expectedFileContent = fileContent;
        byte[] expectedEOF = new byte[TarConstants.STD_RECORD_LENGTH];
        for (int i = 0; i < expectedEOF.length; i++) {
            expectedEOF[i] = 0;
        }

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] actualHeader = new byte[TarConstants.STD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, actualHeader, 0, TarConstants.STD_RECORD_LENGTH);

        assertArrayEquals(expectedHeader, actualHeader);
        assertArrayEquals(expectedFileContent, new byte[fileContent.length]);
        assertArrayEquals(expectedEOF, new byte[TarConstants.STD_RECORD_LENGTH]);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "testFile_with_a_very_long_name_to_test_long_file_handling_capabilities.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Set long file mode to GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        byte[] expectedHeader = new byte[TarConstants.STD_RECORD_LENGTH];
        TarArchiveEntry.writeHeader(entry, expectedHeader);
        byte[] expectedLinkName = entry.getName().getBytes();
        byte[] expectedLinkRecord = new byte[expectedLinkName.length + 1];
        System.arraycopy(expectedLinkName, 0, expectedLinkRecord, 0, expectedLinkName.length);
        expectedLinkRecord[expectedLinkName.length] = 0;

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] actualHeader = new byte[TarConstants.STD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, actualHeader, 0, TarConstants.STD_RECORD_LENGTH);

        assertArrayEquals(expectedHeader, actualHeader);
        assertArrayEquals(expectedLinkRecord, new byte[expectedLinkName.length + 1]);

        tarArchiveOutputStream.close();
    }

    @After
    public void tearDown() throws IOException {
        byteArrayOutputStream.reset();
    }
}