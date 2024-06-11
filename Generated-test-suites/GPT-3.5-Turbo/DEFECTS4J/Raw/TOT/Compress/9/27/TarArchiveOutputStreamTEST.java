import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private static final String ENTRY_NAME = "test-entry.txt";
    private static final byte[] ENTRY_CONTENTS = "This is a test entry".getBytes();

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;
    private ArchiveEntry entry;
    
    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        
        entry = tarOutputStream.createArchiveEntry(new File(ENTRY_NAME), ENTRY_NAME);
        entry.setSize(ENTRY_CONTENTS.length);
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(ENTRY_CONTENTS);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        
        byte[] output = outputStream.toByteArray();
        
        // Verify that the entry contents are correctly written
        int headerSize = TarConstants.HEADER_BLOCK * 2;
        byte[] entryData = Arrays.copyOfRange(output, headerSize, headerSize + ENTRY_CONTENTS.length);
        assertArrayEquals(ENTRY_CONTENTS, entryData);
        
        // Verify that the entry size matches the expected size
        int expectedSize = headerSize + ENTRY_CONTENTS.length;
        assertEquals(expectedSize, output.length);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryTwice() throws IOException {
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(ENTRY_CONTENTS);
        tarOutputStream.closeArchiveEntry();
        
        byte[] output = outputStream.toByteArray();
        
        // Verify that the entry size matches the expected size
        int expectedSize = TarConstants.HEADER_BLOCK * 2;
        assertEquals(expectedSize, output.length);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPut() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(ENTRY_CONTENTS);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        
        byte[] expectedOutput = outputStream.toByteArray();
        
        // Read the output stream into a new input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedOutput);
        
        // Use Apache Commons Compress library to read the tar archive
        try (TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream)) {
            ArchiveEntry[] readEntries = new ArchiveEntry[1];
            ArchiveEntry readEntry = tarInputStream.getNextEntry();
            readEntries[0] = readEntry;
            
            assertEquals(ENTRY_NAME, readEntry.getName());
            assertTrue(readEntry.isFile());
            assertEquals(ENTRY_CONTENTS.length, readEntry.getSize());
            
            byte[] readContents = new byte[ENTRY_CONTENTS.length];
            IOUtils.readFully(tarInputStream, readContents);
            
            assertArrayEquals(ENTRY_CONTENTS, readContents);
            
            assertNull(tarInputStream.getNextEntry());
        }
    }

}