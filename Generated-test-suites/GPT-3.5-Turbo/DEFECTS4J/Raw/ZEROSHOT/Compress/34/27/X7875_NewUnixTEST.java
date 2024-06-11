import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {
  
  private X7875_NewUnix x7875;

  @Before
  public void setUp() {
    x7875 = new X7875_NewUnix();
  }

  @Test
  public void testGetHeaderId() {
    assertEquals(0x7875, x7875.getHeaderId().getValue());
  }

  @Test
  public void testGetUID() {
    x7875.setUID(12345L);
    assertEquals(12345L, x7875.getUID());
  }

  @Test
  public void testGetGID() {
    x7875.setGID(67890L);
    assertEquals(67890L, x7875.getGID());
  }

  @Test
  public void testSetUID() {
    x7875.setUID(54321L);
    assertEquals(54321L, x7875.getUID());
  }

  @Test
  public void testSetGID() {
    x7875.setGID(98765L);
    assertEquals(98765L, x7875.getGID());
  }

  @Test
  public void testGetLocalFileDataLength() {
    x7875.setUID(12345L);
    x7875.setGID(67890L);
    assertEquals(6, x7875.getLocalFileDataLength().getValue());
  }

  @Test
  public void testGetCentralDirectoryLength() {
    assertEquals(6, x7875.getCentralDirectoryLength().getValue());
  }

  @Test
  public void testGetLocalFileDataData() {
    x7875.setUID(12345L);
    x7875.setGID(67890L);
    byte[] expectedData = {(byte) 1, (byte) 4, (byte) 57, (byte) 0, (byte) 0, (byte) 0, (byte) 55, (byte) 9, (byte) 21, (byte) 0};
    assertArrayEquals(expectedData, x7875.getLocalFileDataData());
  }

  @Test
  public void testGetCentralDirectoryData() {
    byte[] expectedData = {};
    assertArrayEquals(expectedData, x7875.getCentralDirectoryData());
  }

  @Test
  public void testParseFromLocalFileData() throws ZipException {
    byte[] data = {(byte) 1, (byte) 4, (byte) 57, (byte) 0, (byte) 0, (byte) 0, (byte) 55, (byte) 9, (byte) 21, (byte) 0};
    x7875.parseFromLocalFileData(data, 0, data.length);
    assertEquals(1, x7875.getUID());
    assertEquals(12345L, x7875.getUID());
    assertEquals(67890L, x7875.getGID());
  }

  @Test
  public void testParseFromCentralDirectoryData() throws ZipException {
    byte[] buffer = {};
    x7875.parseFromCentralDirectoryData(buffer, 0, buffer.length);
  }

  @Test
  public void testReset() {
    x7875.setUID(12345L);
    x7875.setGID(67890L);
    x7875.reset();
    assertEquals(1000L, x7875.getUID());
    assertEquals(1000L, x7875.getGID());
  }

  @Test
  public void testToString() {
    x7875.setUID(12345L);
    x7875.setGID(67890L);
    assertEquals("0x7875 Zip Extra Field: UID=12345 GID=67890", x7875.toString());
  }
}