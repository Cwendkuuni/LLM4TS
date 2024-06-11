import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TarArchiveOutputStreamTest {

    @Mock
    private OutputStream os;
    @Mock
    private TarBuffer buffer;
    
    @InjectMocks
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tarArchiveOutputStream = new TarArchiveOutputStream(os);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        verify(os, times(1)).write(any(byte[].class), eq(0), eq(entry.getHeaderSize()));
        verify(buffer, times(1)).writeRecord(any(byte[].class));
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testCloseArchiveEntry_withDirectoryEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDirectory/");
        entry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        verify(buffer, times(1)).writeRecord(any(byte[].class));
        assertTrue(!tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_withoutWritingAllData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite_withValidData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "This is a test".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        verify(buffer, times(1)).writeRecord(data);
    }

    @Test(expected = IOException.class)
    public void testWrite_withInvalidData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "This is a test".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length + 1);
    }

    @Test
    public void testWrite_withAssemblingData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "This is a test".getBytes(StandardCharsets.UTF_8);
        byte[] partialData = "This is".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(partialData, 0, partialData.length);
        tarArchiveOutputStream.write(data, partialData.length, data.length - partialData.length);
        verify(buffer, times(1)).writeRecord(data);
    }

    @Test
    public void testSetLongFileMode_withValidMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(tarArchiveOutputStream.longFileMode, TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(tarArchiveOutputStream.longFileMode, TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(tarArchiveOutputStream.longFileMode, TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test
    public void testSetLongFileMode_withInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(-1);
        assertEquals(tarArchiveOutputStream.longFileMode, -1);
    }

    @Test
    public void testFinish_withUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        verify(buffer, times(2)).writeRecord(any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testFinish_withUnclosedEntry_throwsIOException() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        doThrow(new IOException()).when(buffer).writeRecord(any(byte[].class));
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
        verify(buffer, times(3)).writeRecord(any(byte[].class));
        verify(buffer, times(1)).close();
        verify(os, times(1)).close();
    }

    @Test(expected = IOException.class)
    public void testClose_withIOError() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        doThrow(new IOException()).when(os).close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        assertEquals(recordSize, TarBuffer.DEFAULT_RCDSIZE);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        String entryName = "testEntry";
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(((TarArchiveEntry) entry).getFile(), inputFile);
        assertEquals(entry.getName(), entryName);
    }

}