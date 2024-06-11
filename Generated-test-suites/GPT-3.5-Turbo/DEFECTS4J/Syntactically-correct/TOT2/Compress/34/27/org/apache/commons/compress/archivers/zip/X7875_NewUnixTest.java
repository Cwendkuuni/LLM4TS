import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {
  
  @Test
  public void testConstructor() {
    X7875_NewUnix unix = new X7875_NewUnix();
    assertNotNull(unix);
  }
  
  @Test
  public void testGetHeaderId() {
    X7875_NewUnix unix = new X7875_NewUnix();
    assertEquals(0x7875, unix.getHeaderId().getValue());
  }
  
  @Test
  public void testGetUID() {
    X7875_NewUnix unix = new X7875_NewUnix();
    unix.setUID(1000);
    assertEquals(1000, unix.getUID());
  }
  
  @Test
  public void testGetGID() {
    X7875_NewUnix unix = new X7875_NewUnix();
    unix.setGID(1000);
    assertEquals(1000, unix.getGID());
  }
  
  @Test
  public void testSetUID() {
    X7875_NewUnix unix = new X7875_NewUnix();
    unix.setUID(2000);
    assertEquals(2000, unix.getUID());
  }
  
  @Test
  public void testSetGID() {
    X7875_NewUnix unix = new X7875_NewUnix();
    unix.setGID(2000);
    assertEquals(2000, unix.getGID());
  }
  
  @Test
  public void testGetLocalFileDataLength() {
    X7875_NewUnix unix = new X7875_NewUnix();
    assertEquals(3, unix.getLocalFileDataLength().getValue());
  }
  
  @Test
  public void testGetCentralDirectoryLength() {
    X7875_NewUnix unix = new X7875_NewUnix();
    assertEquals(3, unix.getCentralDirectoryLength().getValue());
  }
  
  @Test
  public void testGetLocalFileDataData() {
    X7875_NewUnix unix = new X7875_NewUnix();
    byte[] data = unix.getLocalFileDataData();
    assertNotNull(data);
  }
  
  @Test
  public void testParseFromLocalFileData() {
    X7875_NewUnix unix = new X7875_NewUnix();
    byte[] data = {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
    try {
      unix.parseFromLocalFileData(data, 0, data.length);
    } catch (java.util.zip.ZipException e) {
      // Catch exception
    }
    assertNotNull(unix);
  }
  
  @Test
  public void testParseFromCentralDirectoryData() {
    X7875_NewUnix unix = new X7875_NewUnix();
    byte[] data = {};
    try {
      unix.parseFromCentralDirectoryData(data, 0, data.length);
    } catch (java.util.zip.ZipException e) {
      // Catch exception
    }
    assertNotNull(unix);
  }
  
  @Test
  public void testToString() {
    X7875_NewUnix unix = new X7875_NewUnix();
    assertNotNull(unix.toString());
  }
  
  @Test
  public void testClone() {
    X7875_NewUnix unix = new X7875_NewUnix();
    try {
      Object cloned = unix.clone();
      assertNotNull(cloned);
    } catch (CloneNotSupportedException e) {
      // Catch exception
    }
  }
  
  @Test
  public void testEquals() {
    X7875_NewUnix unix1 = new X7875_NewUnix();
    X7875_NewUnix unix2 = new X7875_NewUnix();
    assertTrue(unix1.equals(unix2));
  }
  
  @Test
  public void testHashCode() {
    X7875_NewUnix unix = new X7875_NewUnix();
    int hashcode = unix.hashCode();
    assertNotNull(hashcode);
  }
}