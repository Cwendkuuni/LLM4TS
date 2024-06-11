import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

  @Test
  public void testConstructor() {
    X7875_NewUnix obj = new X7875_NewUnix();
    Assert.assertEquals(1, obj.version);
    Assert.assertEquals(1000, obj.getUID());
    Assert.assertEquals(1000, obj.getGID());
  }

  @Test
  public void testSetGetUID() {
    X7875_NewUnix obj = new X7875_NewUnix();
    obj.setUID(2000);
    Assert.assertEquals(2000, obj.getUID());
  }

  @Test
  public void testSetGetGID() {
    X7875_NewUnix obj = new X7875_NewUnix();
    obj.setGID(3000);
    Assert.assertEquals(3000, obj.getGID());
  }

  @Test
  public void testGetLocalFileDataLength() {
    X7875_NewUnix obj = new X7875_NewUnix();
    byte[] data = obj.getLocalFileDataData();
    Assert.assertEquals(data.length, obj.getLocalFileDataLength().getValue());
  }

  @Test
  public void testGetCentralDirectoryLength() {
    X7875_NewUnix obj = new X7875_NewUnix();
    byte[] data = obj.getCentralDirectoryData();
    Assert.assertEquals(data.length, obj.getCentralDirectoryLength().getValue());
  }

  @Test
  public void testParseFromLocalFileData() {
    X7875_NewUnix obj = new X7875_NewUnix();
    byte[] testData = new byte[] {0x01, 0x01, 0x01, 0x01};
    obj.parseFromLocalFileData(testData, 0, testData.length);
    Assert.assertEquals(1, obj.version);
    Assert.assertEquals(1, obj.getUID());
    Assert.assertEquals(1, obj.getGID());
  }

  @Test
  public void testEquals() {
    X7875_NewUnix obj1 = new X7875_NewUnix();
    X7875_NewUnix obj2 = new X7875_NewUnix();
    Assert.assertTrue(obj1.equals(obj2));
    obj2.setUID(2000);
    Assert.assertFalse(obj1.equals(obj2));
  }

  @Test
  public void testHashCode() {
    X7875_NewUnix obj1 = new X7875_NewUnix();
    X7875_NewUnix obj2 = new X7875_NewUnix();
    Assert.assertEquals(obj1.hashCode(), obj2.hashCode());
    obj2.setUID(2000);
    Assert.assertNotEquals(obj1.hashCode(), obj2.hashCode());
  }

}