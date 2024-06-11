import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

   @Test
   public void testGetSize() {
      // Test case 1
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      field.setSize(new ZipEightByteInteger(100));
      assertEquals(new ZipEightByteInteger(100), field.getSize());

      // Test case 2
      Zip64ExtendedInformationExtraField field2 = new Zip64ExtendedInformationExtraField();
      assertEquals(null, field2.getSize());
   }

   @Test
   public void testGetCompressedSize() {
      // Test case 1
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      field.setCompressedSize(new ZipEightByteInteger(200));
      assertEquals(new ZipEightByteInteger(200), field.getCompressedSize());

      // Test case 2
      Zip64ExtendedInformationExtraField field2 = new Zip64ExtendedInformationExtraField();
      assertEquals(null, field2.getCompressedSize());
   }

   @Test
   public void testGetRelativeHeaderOffset() {
      // Test case 1
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      field.setRelativeHeaderOffset(new ZipEightByteInteger(300));
      assertEquals(new ZipEightByteInteger(300), field.getRelativeHeaderOffset());

      // Test case 2
      Zip64ExtendedInformationExtraField field2 = new Zip64ExtendedInformationExtraField();
      assertEquals(null, field2.getRelativeHeaderOffset());
   }

   @Test
   public void testGetDiskStartNumber() {
      // Test case 1
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      field.setDiskStartNumber(new ZipLong(400));
      assertEquals(new ZipLong(400), field.getDiskStartNumber());

      // Test case 2
      Zip64ExtendedInformationExtraField field2 = new Zip64ExtendedInformationExtraField();
      assertEquals(null, field2.getDiskStartNumber());
   }

   @Test
   public void testGetHeaderId() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      assertEquals(new ZipShort(0x0001), field.getHeaderId());
   }

   @Test
   public void testGetLocalFileDataLength() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      assertEquals(new ZipShort(16), field.getLocalFileDataLength());
   }

   @Test
   public void testGetCentralDirectoryLength() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      assertEquals(new ZipShort(16), field.getCentralDirectoryLength());
   }

   @Test
   public void testGetLocalFileDataData() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      assertEquals(0, field.getLocalFileDataData().length);
   }

   @Test
   public void testGetCentralDirectoryData() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      assertEquals(16, field.getCentralDirectoryData().length);
   }

   @Test
   public void testParseFromLocalFileData() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      byte[] buffer = new byte[16];
      field.parseFromLocalFileData(buffer, 0, 16);
      // Test that parsing does not throw an exception, no further functionality to test
   }

   @Test
   public void testParseFromCentralDirectoryData() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      byte[] buffer = new byte[16];
      field.parseFromCentralDirectoryData(buffer, 0, 16);
      // Test that parsing does not throw an exception, no further functionality to test
   }

   @Test
   public void testReparseCentralDirectoryData() {
      Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
      boolean hasUncompressedSize = true;
      boolean hasCompressedSize = true;
      boolean hasRelativeHeaderOffset = true;
      boolean hasDiskStart = true;
      field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
      // Test that reparsing does not throw an exception, no further functionality to test
   }

}