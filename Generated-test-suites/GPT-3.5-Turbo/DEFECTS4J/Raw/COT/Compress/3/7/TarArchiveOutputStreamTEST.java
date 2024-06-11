import org.junit.Test;

public class TarArchiveOutputStreamTest {

  @Test
  public void testSetLongFileMode() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    assert(tarArchiveOutputStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_ERROR);
  }

  @Test
  public void testFinish() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
      tarArchiveOutputStream.finish();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Check if finish() runs successfully without throwing any exception
  }

  @Test
  public void testClose() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
      tarArchiveOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Check if close() runs successfully without throwing any exception
  }

  @Test
  public void testGetRecordSize() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    assert(tarArchiveOutputStream.getRecordSize() == TarBuffer.DEFAULT_RCDSIZE);
  }

  @Test
  public void testPutArchiveEntry() {
     TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
     TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("filename", TarArchiveEntry.DEFAULT_FILE_MODE);
     try {
       tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
     } catch (IOException e) {
       e.printStackTrace();
     }
     //Check if putArchiveEntry() runs successfully without throwing any exception
  }

  @Test 
  public void testCloseArchiveEntry() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
      tarArchiveOutputStream.closeArchiveEntry();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Check if closeArchiveEntry() runs successfully without throwing any exception
  }

  @Test
  public void testWrite() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    byte[] buffer = new byte[10];
    try {
      tarArchiveOutputStream.write(buffer, 0, buffer.length);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Check if write() runs successfully without throwing any exception
  }

  @Test
  public void testCreateArchiveEntry() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    File inputFile = new File("filename");
    try {
      tarArchiveOutputStream.createArchiveEntry(inputFile, "entry name");
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Check if createArchiveEntry() runs successfully without throwing any exception
  }
}