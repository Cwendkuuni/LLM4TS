import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testZip64ExtendedInformationExtraFieldConstructor() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        assertNotNull(zipField);
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldWithSizesConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertNotNull(zipField);
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldAllValuesConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(200);
        ZipLong diskStart = new ZipLong(1);
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertNotNull(zipField);
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
        assertEquals(relativeHeaderOffset, zipField.getRelativeHeaderOffset());
        assertEquals(diskStart, zipField.getDiskStartNumber());
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldLocalDataLength() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), zipField.getLocalFileDataLength());
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldParseFromLocalFileData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        byte[] localData = new byte[16];
        size.putTo(localData, 0);
        compressedSize.putTo(localData, 8);
        zipField.parseFromLocalFileData(localData, 0, 16);
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldCentralDirectoryDataLength() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), zipField.getCentralDirectoryLength());
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldParseFromCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(200);
        ZipLong diskStart = new ZipLong(1);
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        byte[] centralData = new byte[24];
        size.putTo(centralData, 0);
        compressedSize.putTo(centralData, 8);
        relativeHeaderOffset.putTo(centralData, 16);
        diskStart.putTo(centralData, 24);
        zipField.parseFromCentralDirectoryData(centralData, 0, 28);
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
        assertEquals(relativeHeaderOffset, zipField.getRelativeHeaderOffset());
        assertEquals(diskStart, zipField.getDiskStartNumber());
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldReparseCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(200);
        ZipLong diskStart = new ZipLong(1);
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        byte[] centralData = new byte[24];
        size.putTo(centralData, 0);
        compressedSize.putTo(centralData, 8);
        relativeHeaderOffset.putTo(centralData, 16);
        diskStart.putTo(centralData, 24);
        zipField.parseFromCentralDirectoryData(centralData, 0, 28);
        zipField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
        assertEquals(relativeHeaderOffset, zipField.getRelativeHeaderOffset());
        assertEquals(diskStart, zipField.getDiskStartNumber());
    }
    
}