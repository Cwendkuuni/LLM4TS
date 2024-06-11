import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

public class TarArchiveOutputStreamTest {

    @Mock
    private OutputStream outputStreamMock;
    
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tarOutputStream = new TarArchiveOutputStream(outputStreamMock);
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        Mockito.verify(outputStreamMock).close();
        Assert.assertTrue(tarOutputStream.isClosed());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        TarArchiveEntry tarEntry = Mockito.mock(TarArchiveEntry.class);
        
        Mockito.when(entry.getName()).thenReturn("testEntry");
        
        tarOutputStream.putArchiveEntry(entry);
        
        Mockito.verify(tarEntry).writeEntryHeader(Mockito.any());
        Mockito.verify(outputStreamMock).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        Assert.assertTrue(tarOutputStream.haveUnclosedEntry());
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        TarArchiveEntry tarEntry = Mockito.mock(TarArchiveEntry.class);
        tarOutputStream.finish();
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        
        Mockito.when(entry.getName()).thenReturn("testEntry");
        
        tarOutputStream.putArchiveEntry(entry);
        
        Mockito.verify(tarEntry, Mockito.never()).writeEntryHeader(Mockito.any());
        Mockito.verify(outputStreamMock, Mockito.never()).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        Assert.assertFalse(tarOutputStream.haveUnclosedEntry());
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarOutputStream.haveUnclosedEntry());
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        byte[] bytes = {1, 2, 3, 4, 5};
        tarOutputStream.write(bytes);
        Mockito.verify(outputStreamMock).write(Mockito.eq(bytes));
    }
    
    @Test
    public void testWriteWithOffsetAndLength() throws IOException {
        byte[] bytes = {1, 2, 3, 4, 5};
        tarOutputStream.write(bytes, 1, 3);
        Mockito.verify(outputStreamMock).write(Mockito.eq(bytes), Mockito.eq(1), Mockito.eq(3));
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();
        Mockito.verify(outputStreamMock).flush();
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = Mockito.mock(File.class);
        String entryName = "testEntry";
        
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, entryName);
        
        Assert.assertEquals(TarArchiveEntry.class, entry.getClass());
        Assert.assertEquals(file, entry.getFile());
        Assert.assertEquals(entryName, entry.getName());
    }
}