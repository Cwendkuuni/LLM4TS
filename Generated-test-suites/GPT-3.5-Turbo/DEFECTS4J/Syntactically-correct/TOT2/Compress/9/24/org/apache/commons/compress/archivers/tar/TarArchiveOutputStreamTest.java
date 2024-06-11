import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    private ByteArrayOutputStream outputStream;
    private TarBuffer tarBuffer;
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarBuffer = mock(TarBuffer.class);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() {
        try {
            tarOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        tarOutputStream.putArchiveEntry(entry);
        
        verify(tarBuffer).writeRecord(any(byte[].class));
        assertFalse(tarOutputStream.isClosed());
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        
        verify(tarBuffer).writeRecord(any(byte[].class));
        assertFalse(tarOutputStream.isEntryOpen());
    }
    
    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        
        tarOutputStream.write(data, 0, data.length);
        
        verify(tarBuffer).writeRecord(data);
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(2);
        
        assertEquals(2, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        
        assertTrue(tarOutputStream.isFinished());
    }

}