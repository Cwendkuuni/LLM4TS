import org.junit.Test;
import org.apache.commons.compress.archivers.zip.*;

public class Zip64ExtendedInformationExtraFieldTest {

  @Test
  public void testGetHeaderId() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    assert(field.getHeaderId().equals(new ZipShort(0x0001)));
  }

  @Test
  public void testGetLocalFileDataLength() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    assert(field.getLocalFileDataLength().equals(new ZipShort(16)));
  }

  @Test
  public void testGetCentralDirectoryLength() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    assert(field.getCentralDirectoryLength().equals(new ZipShort(16)));
  }

  @Test
  public void testGetLocalFileDataData() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    assert(field.getLocalFileDataData().length == 0);
  }

  @Test
  public void testGetCentralDirectoryData() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    assert(field.getCentralDirectoryData().length == 16);
  }

  @Test
  public void testParseFromLocalFileData() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    field.parseFromLocalFileData(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, 16);
    assert(field.getSize().equals(new ZipEightByteInteger(0)));
    assert(field.getCompressedSize().equals(new ZipEightByteInteger(0)));
  }

  @Test
  public void testParseFromCentralDirectoryData() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    field.parseFromCentralDirectoryData(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, 24);
    assert(field.getSize().equals(new ZipEightByteInteger(0)));
    assert(field.getCompressedSize().equals(new ZipEightByteInteger(0)));
    assert(field.getRelativeHeaderOffset().equals(new ZipEightByteInteger(0)));
  }

  @Test
  public void testReparseCentralDirectoryData() {
    Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
    field.parseFromCentralDirectoryData(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, 24);
    field.reparseCentralDirectoryData(true, true, true, true);
    assert(field.getSize().equals(new ZipEightByteInteger(0)));
    assert(field.getCompressedSize().equals(new ZipEightByteInteger(0)));
    assert(field.getRelativeHeaderOffset().equals(new ZipEightByteInteger(0)));
    assert(field.getDiskStartNumber().equals(new ZipLong(0)));
  }
}