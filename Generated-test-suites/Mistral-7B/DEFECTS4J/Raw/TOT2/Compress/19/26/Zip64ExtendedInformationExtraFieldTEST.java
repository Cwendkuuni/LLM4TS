import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

 private static final ZipShort HEADER_ID = new ZipShort(0x0001);

 @Test
 public void testConstructorWithBothSizes() {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
 new ZipEightByteInteger(ZipConstants.DWORD, 100L),
 new ZipEightByteInteger(ZipConstants.DWORD, 50L));
 assertEquals(HEADER_ID, extraField.getHeaderId());
 assertEquals(100L, extraField.getSize().getValue());
 assertEquals(50L, extraField.getCompressedSize().getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithoutBothSizes() {
 new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(ZipConstants.DWORD, 100L));
 }

 @Test
 public void testParseFromLocalFileData() {
 byte[] localFileData = new byte[2 * ZipConstants.DWORD];
 addSizes(localFileData);
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
 assertEquals(HEADER_ID, extraField.getHeaderId());
 assertEquals(100L, extraField.getSize().getValue());
 assertEquals(50L, extraField.getCompressedSize().getValue());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidDataLength() {
 byte[] localFileData = new byte[1 * ZipConstants.DWORD];
 new Zip64ExtendedInformationExtraField().parseFromLocalFileData(localFileData, 0, localFileData.length);
 }

 @Test
 public void testParseFromCentralDirectoryData() {
 byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
 int offset = addSizes(centralDirectoryData);
 new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
 assertEquals(HEADER_ID, extraField.getHeaderId());
 assertEquals(100L, extraField.getSize().getValue());
 assertEquals(50L, extraField.getCompressedSize().getValue());
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryDataWithInvalidDataLength() {
 byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
 new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
 }

 @Test
 public void testGetLocalFileDataLength() {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 assertEquals(2 * ZipConstants.DWORD, extraField.getLocalFileDataLength().getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 assertEquals(2 * ZipConstants.DWORD + (size != null ? ZipConstants.DWORD : 0)
 + (compressedSize != null ? ZipConstants.DWORD : 0)
 + (relativeHeaderOffset != null ? ZipConstants.DWORD : 0)
 + (diskStart != null ? ZipConstants.WORD : 0),
 extraField.getCentralDirectoryLength().getValue());
 }

 @Test
 public void testGetLocalFileDataData() {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
 new ZipEightByteInteger(ZipConstants.DWORD, 100L),
 new ZipEightByteInteger(ZipConstants.DWORD, 50L));
 byte[] localFileDataData = extraField.getLocalFileDataData();
 assertEquals(2 * ZipConstants.DWORD, localFileDataData.length);
 }

 @Test
 public void testGetCentralDirectoryData() {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
 new ZipEightByteInteger(ZipConstants.DWORD, 100L),
 new ZipEightByteInteger(ZipConstants.DWORD, 50L),
 new ZipEightByteInteger(ZipConstants.DWORD, 5L),
 new ZipLong(ZipConstants.WORD, 3L));
 byte[] centralDirectoryData = extraField.getCentralDirectoryData();
 assertEquals(2 * ZipConstants.DWORD + DWORD + WORD, centralDirectoryData.length);
 }

 @Test
 public void testReparseCentralDirectoryData() throws ZipException {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 byte[] rawCentralDirectoryData = new byte[3 * ZipConstants.DWORD + WORD];
 int offset = addSizes(rawCentralDirectoryData);
 extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
 extraField.reparseCentralDirectoryData(true, true, true, true);
 assertEquals(100L, extraField.getSize().getValue());
 assertEquals(50L, extraField.getCompressedSize().getValue());
 assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
 assertEquals(3L, extraField.getDiskStartNumber().getValue());
 }

 @Test(expected = ZipException.class)
 public void testReparseCentralDirectoryDataWithMissingFields() throws ZipException {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 byte[] rawCentralDirectoryData = new byte[3 * ZipConstants.DWORD];
 extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
 extraField.reparseCentralDirectoryData(false, false, false, false);
 }

 @Test(expected = ZipException.class)
 public void testReparseCentralDirectoryDataWithInvalidDataLength() throws ZipException {
 Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
 byte[] rawCentralDirectoryData = new byte[3 * ZipConstants.DWORD + 1 * ZipConstants.WORD];
 extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
 extraField.reparseCentralDirectoryData(true, true, true, true);
 }
}