import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;

import static org.junit.Assert.*;


public class CpioArchiveOutputStreamTest {

    private static final String TEST_OUTPUT_FILE = "test.cpio";
    private static final String TEST_FILE_CONTENTS = "12345";

    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        cpioOutputStream.close();
    }

    @Test
    public void testCreateCpioArchiveEntry() throws IOException {
        File inputFile = new File("input.txt");
        String entryName = "testfile";
        
        ArchiveEntry entry = cpioOutputStream.createArchiveEntry(inputFile, entryName);
        
        assertNotNull(entry);
        assertTrue(entry instanceof CpioArchiveEntry);
        assertEquals(inputFile, ((CpioArchiveEntry)entry).getFile());
        assertEquals(entryName, entry.getName());
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(TEST_FILE_CONTENTS.length());
        entry.setMode(CpioConstants.C_ISREG);
        entry.setTime(System.currentTimeMillis());
        
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(TEST_FILE_CONTENTS.getBytes("UTF-8"));
        cpioOutputStream.closeArchiveEntry();
        
        byte[] output = outputStream.toByteArray();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output);
        CheckedInputStream checkedInputStream = new CheckedInputStream(inputStream, new CRC32());
        
        CpioArchiveInputStream cpioInputStream = new CpioArchiveInputStream(checkedInputStream);
        CpioArchiveEntry readEntry = cpioInputStream.getNextEntry();
        
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getFileSize(), readEntry.getFileSize());
        
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while((bytesRead = cpioInputStream.read(buffer)) != -1) {
            byteStream.write(buffer, 0, bytesRead);
        }
        
        String fileContents = new String(byteStream.toByteArray(), "UTF-8");
        
        assertEquals(TEST_FILE_CONTENTS, fileContents);
        
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        cpioOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();
    }

}