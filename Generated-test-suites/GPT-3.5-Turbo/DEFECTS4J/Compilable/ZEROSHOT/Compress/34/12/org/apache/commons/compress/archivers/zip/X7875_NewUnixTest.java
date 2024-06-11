package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

  @Test
  public void testGetHeaderId() {
    X7875_NewUnix x = new X7875_NewUnix();
    assertEquals(0x7875, x.getHeaderId().getValue());
  }

  @Test
  public void testGetUID() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setUID(1000);
    assertEquals(1000, x.getUID());
  }

  @Test
  public void testGetGID() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setGID(1000);
    assertEquals(1000, x.getGID());
  }

  @Test
  public void testSetUID() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setUID(1000);
    assertEquals(1000, x.getUID());
  }

  @Test
  public void testSetGID() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setGID(1000);
    assertEquals(1000, x.getGID());
  }

  @Test
  public void testGetLocalFileDataLength() {
    X7875_NewUnix x = new X7875_NewUnix();
    assertEquals(4, x.getLocalFileDataLength().getValue());
  }

  @Test
  public void testGetCentralDirectoryLength() {
    X7875_NewUnix x = new X7875_NewUnix();
    assertEquals(4, x.getCentralDirectoryLength().getValue());
  }

  @Test
  public void testGetLocalFileDataData() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setUID(1000);
    x.setGID(1000);
    byte[] expected = {1, 1, 0, 0};
    assertArrayEquals(expected, x.getLocalFileDataData());
  }

  @Test
  public void testGetCentralDirectoryData() {
    X7875_NewUnix x = new X7875_NewUnix();
    byte[] expected = {};
    assertArrayEquals(expected, x.getCentralDirectoryData());
  }

  @Test
  public void testParseFromLocalFileData() throws Exception {
    X7875_NewUnix x = new X7875_NewUnix();
    byte[] data = {1, 1, 0, 0};
    x.parseFromLocalFileData(data, 0, 4);
    assertEquals(1000, x.getUID());
    assertEquals(1000, x.getGID());
  }

  @Test
  public void testParseFromCentralDirectoryData() throws Exception {
    X7875_NewUnix x = new X7875_NewUnix();
    byte[] data = {};
    x.parseFromCentralDirectoryData(data, 0, 0);
    // Nothing to assert
  }

  @Test
  public void testToString() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setUID(1000);
    x.setGID(1000);
    assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x.toString());
  }

  @Test
  public void testEquals() {
    X7875_NewUnix x1 = new X7875_NewUnix();
    X7875_NewUnix x2 = new X7875_NewUnix();
    x1.setUID(1000);
    x1.setGID(1000);
    x2.setUID(1000);
    x2.setGID(1000);
    assertTrue(x1.equals(x2));
  }

  @Test
  public void testHashCode() {
    X7875_NewUnix x = new X7875_NewUnix();
    x.setUID(1000);
    x.setGID(1000);
    int expected = 284143252;
    assertEquals(expected, x.hashCode());
  }
  
  @Test
  public void testTrimLeadingZeroesForceMinLength() {
    byte[] array = {0, 0, 1};
    byte[] expected = {1};
    byte[] result = X7875_NewUnix.trimLeadingZeroesForceMinLength(array);
    assertArrayEquals(expected, result);
  }
}