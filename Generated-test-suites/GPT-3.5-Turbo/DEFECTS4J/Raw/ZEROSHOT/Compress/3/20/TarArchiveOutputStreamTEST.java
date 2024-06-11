import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TarArchiveOutputStreamTest {

  @Mock
  private File mockFile;
  @Mock
  private OutputStream mockOutputStream;
  private TarArchiveOutputStream tarArchiveOutputStream;

  @Before
  public void setUp() {
    tarArchiveOutputStream = new TarArchiveOutputStream(mockOutputStream);
  }

  @Test
  public void testSetLongFileMode() {
    tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    
    assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
  }

  @Test
  public void testFinish() throws IOException {
    tarArchiveOutputStream.finish();

    verify(mockOutputStream, times(2)).write(any(), eq(0), eq(512));
  }

  @Test
  public void testClose() throws IOException {
    tarArchiveOutputStream.close();

    verify(mockOutputStream).close();
  }

  @Test
  public void testGetRecordSize() {
    int recordSize = tarArchiveOutputStream.getRecordSize();
    
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
  }

  @Test
  public void testPutArchiveEntry() throws IOException {
    TarArchiveEntry mockEntry = mock(TarArchiveEntry.class);
    when(mockEntry.getName()).thenReturn("testEntry");
    when(mockEntry.isDirectory()).thenReturn(false);
    when(mockEntry.getSize()).thenReturn(100L);
    byte[] headerBytes = new byte[512];
    when(mockEntry.writeEntryHeader(any())).then(InvocationOnMock -> {
      Object[] args = InvocationOnMock.getArguments();
      byte[] header = (byte[]) args[0];
      System.arraycopy(headerBytes, 0, header, 0, headerBytes.length);
      return null;
    });
    
    tarArchiveOutputStream.putArchiveEntry(mockEntry);
    
    verify(mockEntry).writeEntryHeader(any());
    verify(mockOutputStream, times(2)).write(any(), eq(0), eq(512));
  }

  @Test(expected = IOException.class)
  public void testPutArchiveEntry_LongFileNameError() throws IOException {
    TarArchiveEntry mockEntry = mock(TarArchiveEntry.class);
    when(mockEntry.getName()).thenReturn("testEntryNameWithMoreThan100Chars" +
            "testEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100Chars");
    when(mockEntry.isDirectory()).thenReturn(false);
    when(mockEntry.getSize()).thenReturn(100L);
    tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    
    tarArchiveOutputStream.putArchiveEntry(mockEntry);
  }

  @Test
  public void testPutArchiveEntry_LongFileNameTruncate() throws IOException {
    TarArchiveEntry mockEntry = mock(TarArchiveEntry.class);
    when(mockEntry.getName()).thenReturn("testEntryNameWithMoreThan100Chars" +
            "testEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100CharstestEntryNameWithMoreThan100Chars");
    when(mockEntry.isDirectory()).thenReturn(false);
    when(mockEntry.getSize()).thenReturn(100L);
    byte[] headerBytes = new byte[512];
    when(mockEntry.writeEntryHeader(any())).then(InvocationOnMock -> {
      Object[] args = InvocationOnMock.getArguments();
      byte[] header = (byte[]) args[0];
      System.arraycopy(headerBytes, 0, header, 0, headerBytes.length);
      return null;
    });
    tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    
    tarArchiveOutputStream.putArchiveEntry(mockEntry);
    
    verify(mockEntry).writeEntryHeader(any());
    verify(mockOutputStream, times(2)).write(any(), eq(0), eq(512));
  }

  @Test
  public void testCloseArchiveEntry() throws IOException {
    tarArchiveOutputStream.write(new byte[512], 0, 512);

    tarArchiveOutputStream.closeArchiveEntry();

    verify(mockOutputStream, times(1)).write(any(), eq(0), eq(512));
  }

  @Test(expected = IOException.class)
  public void testCloseArchiveEntry_IncompleteEntry() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(mock(TarArchiveEntry.class));
    
    tarArchiveOutputStream.closeArchiveEntry();
  }

  @Test
  public void testWrite() throws IOException {
    byte[] bytes = new byte[512];

    tarArchiveOutputStream.write(bytes, 0, 512);

    verify(mockOutputStream, times(1)).write(bytes, 0, 512);
  }

  @Test(expected = IOException.class)
  public void testWrite_ExceedEntrySize() throws IOException {
    TarArchiveEntry mockEntry = mock(TarArchiveEntry.class);
    when(mockEntry.getName()).thenReturn("testEntry");
    when(mockEntry.isDirectory()).thenReturn(false);
    when(mockEntry.getSize()).thenReturn(100L);
    tarArchiveOutputStream.putArchiveEntry(mockEntry);
    byte[] bytes = new byte[512];

    tarArchiveOutputStream.write(bytes, 0, 513);
  }

  @Test
  public void testWrite_EOFRecord() throws IOException {
    tarArchiveOutputStream.writeEOFRecord();

    verify(mockOutputStream).write(any(), eq(0), eq(512));
  }
  
  @Test
  public void testCreateArchiveEntry() throws IOException {
    ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(mockFile, "entryName");
    
    assertTrue(entry instanceof TarArchiveEntry);
  }
  
}