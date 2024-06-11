import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.zip.constraint.ZipEntrySize;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

 @Test
 public void testGetHeaderId() {
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 assertEquals(HEADER_ID, zip64.getHeaderId());
 }

 @Test
 public void testGetLocalFileDataLengthWithNoSizes() {
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 assertEquals(0, zip64.getLocalFileDataLength().getValue());
 }

 @Test
 public void testGetLocalFileDataLengthWithSizes() {
 ZipEightByteInteger size = new ZipEightByteInteger(100);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField(size, compressedSize);
 assertEquals(2 * DWORD, zip64.getLocalFileDataLength().getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLocalFileDataLengthWithMissingSizes() {
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.setSize(null);
 zip64.setCompressedSize(null);
 zip64.getLocalFileDataLength();
 }

 @Test
 public void testGetCentralDirectoryLengthWithNoSizes() {
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 assertEquals(0, zip64.getCentralDirectoryLength().getValue());
 }

 @Test
 public void testGetCentralDirectoryLengthWithSizes() {
 ZipEightByteInteger size = new ZipEightByteInteger(100);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
 ZipEightByteInteger rho = new ZipEightByteInteger(1234);
 ZipLong ds = new ZipLong(5678);
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField(size, compressedSize, rho, ds);
 assertEquals(2 * DWORD + DWORD + DWORD + WORD, zip64.getCentralDirectoryLength().getValue());
 }

 @Test
 public void testParseFromLocalFileDataWithNoSizes() {
 byte[] data = new byte[0];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromLocalFileData(data, 0, 0);
 assertNull(zip64.getSize());
 assertNull(zip64.getCompressedSize());
 }

 @Test
 public void testParseFromLocalFileDataWithSizes() {
 byte[] data = new byte[2 * DWORD];
 ZipEightByteInteger size = new ZipEightByteInteger(100);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromLocalFileData(data, 0, 2 * DWORD);
 assertEquals(size, zip64.getSize());
 assertEquals(compressedSize, zip64.getCompressedSize());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithMissingSizes() {
 byte[] data = new byte[2 * DWORD];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromLocalFileData(data, 0, 2 * DWORD - DWORD);
 }

 @Test
 public void testGetLocalFileDataDataWithNoSizes() {
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 byte[] result = zip64.getLocalFileDataData();
 assertEquals(0, result.length);
 }

 @Test
 public void testGetLocalFileDataDataWithSizes() {
 ZipEightByteInteger size = new ZipEightByteInteger(100);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField(size, compressedSize);
 byte[] result = zip64.getLocalFileDataData();
 assertArrayEquals(new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                             0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                             0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0,
                             0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, result);
 }

 @Test
 public void testParseFromCentralDirectoryDataWithNoExtraField() {
 byte[] data = new byte[0];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 0);
 assertNull(zip64.getSize());
 assertNull(zip64.getCompressedSize());
 assertNull(zip64.getRelativeHeaderOffset());
 assertNull(zip64.getDiskStartNumber());
 }

 @Test
 public void testParseFromCentralDirectoryDataWithSizesAndOffset() {
 byte[] data = new byte[3 * DWORD];
 ZipEightByteInteger size = new ZipEightByteInteger(100);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
 ZipEightByteInteger rho = new ZipEightByteInteger(1234);
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD);
 assertEquals(size, zip64.getSize());
 assertEquals(compressedSize, zip64.getCompressedSize());
 assertEquals(rho, zip64.getRelativeHeaderOffset());
 assertNull(zip64.getDiskStartNumber());
 }

 @Test
 public void testParseFromCentralDirectoryDataWithDiskStartNumber() {
 byte[] data = new byte[3 * DWORD + WORD];
 ZipEightByteInteger size = new ZipEightByteInteger(100);
 ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
 ZipEightByteInteger rho = new ZipEightByteInteger(1234);
 ZipLong ds = new ZipLong(5678);
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
 assertEquals(size, zip64.getSize());
 assertEquals(compressedSize, zip64.getCompressedSize());
 assertEquals(rho, zip64.getRelativeHeaderOffset());
 assertEquals(ds, zip64.getDiskStartNumber());
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryDataWithInvalidLength() {
 byte[] data = new byte[3 * DWORD - 1];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD - 1);
 }

 @Test
 public void testReparseCentralDirectoryData() throws ZipException {
 byte[] data = new byte[3 * DWORD + WORD];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
 ZipEntrySize entrySize = new ZipEntrySize(100, 50);
 zip64.reparseCentralDirectoryData(true, true, true, true);
 assertEquals(entrySize.getSize(), zip64.getSize());
 assertEquals(entrySize.getCompressedSize(), zip64.getCompressedSize());
 assertEquals(entrySize.getRelativeHeaderOffset(), zip64.getRelativeHeaderOffset());
 assertEquals(entrySize.getDiskStartNumber(), zip64.getDiskStartNumber());
 }

 @Test(expected = ZipException.class)
 public void testReparseCentralDirectoryDataWithMissingFields() throws ZipException {
 byte[] data = new byte[3 * DWORD + WORD];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
 zip64.reparseCentralDirectoryData(true, false, true, true);
 }

 @Test(expected = ZipException.class)
 public void testReparseCentralDirectoryDataWithMissingFields2() throws ZipException {
 byte[] data = new byte[3 * DWORD + WORD];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
 zip64.reparseCentralDirectoryData(false, true, true, true);
 }

 @Test(expected = ZipException.class)
 public void testReparseCentralDirectoryDataWithMissingFields3() throws ZipException {
 byte[] data = new byte[3 * DWORD + WORD];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
 zip64.reparseCentralDirectoryData(true, true, false, true);
 }

 @Test(expected = ZipException.class)
 public void testReparseCentralDirectoryDataWithMissingFields4() throws ZipException {
 byte[] data = new byte[3 * DWORD + WORD];
 Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
 zip64.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
 zip64.reparseCentralDirectoryData(true, true, true, false);
 }
}