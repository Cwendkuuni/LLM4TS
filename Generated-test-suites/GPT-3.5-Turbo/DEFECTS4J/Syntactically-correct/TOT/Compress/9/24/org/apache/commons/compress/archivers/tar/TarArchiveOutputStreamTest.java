import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    private ByteArrayOutputStream byteOutputStream;
    private TarArchiveOutputStream tarOutput;
    
    @Before
    public void setUp() {
        byteOutputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(byteOutputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarOutput.close();
        byteOutputStream.close();
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        // Typical use case
        File inputFile = new File("testfile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, "testfile.txt");
        tarOutput.putArchiveEntry(entry);
        byte[] expectedHeader = entry.getEntryHeader();
        byte[] actualHeader = tarOutput.getRecordBuffer();
        assertArrayEquals("Header mismatch", expectedHeader, actualHeader);
        tarOutput.closeArchiveEntry();
        
        // Edge case with long file name
        File longInputFile = new File("this_is_a_very_long_file_name_that_exceeds_the_maximum_allowed_length_for_a_file_name_in_a_tar_archive_to_test_for_long_file_name_truncation.txt");
        TarArchiveEntry longEntry = new TarArchiveEntry(longInputFile, "this_is_a_very_long_file_name_that_exceeds_the_maximum_allowed_length_for_a_file_name_in_a_tar_archive_to_test_for_long_file_name_truncation.txt");
        tarOutput.putArchiveEntry(longEntry);
        byte[] expectedLongHeader = longEntry.getEntryHeader();
        byte[] actualLongHeader = tarOutput.getRecordBuffer();
        assertArrayEquals("Long Header mismatch", expectedLongHeader, actualLongHeader);
        tarOutput.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarOutput.finish();
        File inputFile = new File("testfile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, "testfile.txt");
        tarOutput.putArchiveEntry(entry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOutput.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testDoubleCloseArchiveEntry() throws IOException {
        File inputFile = new File("testfile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, "testfile.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseAfterFinish() throws IOException {
        tarOutput.finish();
        tarOutput.close();
    }
    
    @Test
    public void testWrite() throws IOException {
        // Typical use case
        File inputFile = new File("testfile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, "testfile.txt");
        tarOutput.putArchiveEntry(entry);
        FileInputStream fileInput = new FileInputStream(inputFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInput.read(buffer)) != -1) {
            tarOutput.write(buffer, 0, bytesRead);
        }
        tarOutput.closeArchiveEntry();
        assertEquals("Actual bytes written mismatch", inputFile.length(), tarOutput.getBytesWritten());
        
        // Edge case with empty input
        File emptyFile = new File("emptyfile.txt");
        TarArchiveEntry emptyEntry = new TarArchiveEntry(emptyFile, "emptyfile.txt");
        tarOutput.putArchiveEntry(emptyEntry);
        tarOutput.write(new byte[0], 0, 0);
        tarOutput.closeArchiveEntry();
        assertEquals("Actual bytes written mismatch for empty file", 0, tarOutput.getBytesWritten());
    }
    
    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        tarOutput.finish();
        tarOutput.write(new byte[1024], 0, 1024);
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        byte[] expectedEOF = new byte[512];
        byte[] actualEOF = new byte[512];
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = tarOutput.read(actualEOF, 0, 512)) != -1) {
            byteOutput.write(actualEOF, 0, bytesRead);
        }
        assertArrayEquals("EOF record mismatch", expectedEOF, byteOutput.toByteArray());
    }
    
    @Test(expected = IOException.class)
    public void testDoubleFinish() throws IOException {
        tarOutput.finish();
        tarOutput.finish();
    }
}