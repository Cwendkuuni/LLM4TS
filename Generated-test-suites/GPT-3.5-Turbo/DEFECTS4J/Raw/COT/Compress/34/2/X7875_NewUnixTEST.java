
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipException;

public class X7875_NewUnixTest {

  @Test
  public void testConstructor() {
    X7875_NewUnix unix = new X7875_NewUnix();

    // Ensure the object is constructed correctly
    assert (unix.getUID() == 1000);
    assert (unix.getGID() == 1000);
  }

  @Test
  public void testSetUID() {
    X7875_NewUnix unix = new X7875_NewUnix();
    long uid = 2000;

    unix.setUID(uid);

    // Check if the UID is set correctly
    assert (unix.getUID() == uid);
  }

  @Test
  public void testSetGID() {
    X7875_NewUnix unix = new X7875_NewUnix();
    long gid = 2000;

    unix.setGID(gid);

    // Check if the GID is set correctly
    assert (unix.getGID() == gid);
  }

  @Test
  public void testGetHeaderId() {
    X7875_NewUnix unix = new X7875_NewUnix();
    assert (unix.getHeaderId().equals(new ZipShort(0x7875)));
  }

  @Test
  public void testGetLocalFileDataLength() {
    X7875_NewUnix unix = new X7875_NewUnix();

    // First four bytes are for version, UID size, and GID size
    ZipShort expectedLength = new ZipShort(4);
    assert (unix.getLocalFileDataLength().equals(expectedLength));
  }

  @Test
  public void testGetCentralDirectoryLength() {
    X7875_NewUnix unix = new X7875_NewUnix();
    
    // First four bytes are for version, UID size, and GID size
    ZipShort expectedLength = new ZipShort(4);
    assert (unix.getCentralDirectoryLength().equals(expectedLength));
  }

  @Test
  public void testGetLocalFileDataData() {
    X7875_NewUnix unix = new X7875_NewUnix();

    // First four bytes are for version, UID size, and GID size
    byte[] expectedData = new byte[] { 1, 0, 0, 0 };
    assert (Arrays.equals(unix.getLocalFileDataData(), expectedData));
  }

  @Test
  public void testGetCentralDirectoryData() {
    X7875_NewUnix unix = new X7875_NewUnix();

    // Central directory data is always empty
    byte[] expectedData = new byte[0];
    assert (Arrays.equals(unix.getCentralDirectoryData(), expectedData));
  }

  @Test
  public void testParseFromLocalFileData() throws ZipException {
    X7875_NewUnix unix = new X7875_NewUnix();

    // First four bytes are for version, UID size, and GID size
    byte[] data = new byte[] { 1, 0, 0, 0 };
    unix.parseFromLocalFileData(data, 0, data.length);

    // Check if the data was parsed correctly
    assert (unix.getVersion() == 1);
    assert (unix.getUID() == 0);
    assert (unix.getGID() == 0);
  }

  @Test
  public void testParseFromCentralDirectoryData() throws ZipException {
    X7875_NewUnix unix = new X7875_NewUnix();
    byte[] data = new byte[0];

    // Doesn't do anything, so no need to test
    unix.parseFromCentralDirectoryData(data, 0, data.length);
  }

  @Test
  public void testToString() {
    X7875_NewUnix unix = new X7875_NewUnix();
    String expectedString = "0x7875 Zip Extra Field: UID=1000 GID=1000";
    assert (unix.toString().equals(expectedString));
  }

  @Test
  public void testEquals() throws CloneNotSupportedException {
    X7875_NewUnix unix1 = new X7875_NewUnix();
    X7875_NewUnix unix2 = (X7875_NewUnix) unix1.clone();

    // Check if the objects are equal
    assert (unix1.equals(unix2));
  }

  @Test
  public void testHashCode() throws CloneNotSupportedException {
    X7875_NewUnix unix1 = new X7875_NewUnix();
    X7875_NewUnix unix2 = (X7875_NewUnix) unix1.clone();

    // Check if the hash codes are equal
    assert (unix1.hashCode() == unix2.hashCode());
  }
}
