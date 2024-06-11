import org.junit.Test;

import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        ZipShort expectedResult = new ZipShort(0x0001);
        ZipShort result = Zip64ExtendedInformationExtraField.HEADER_ID;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expectedResult = new ZipShort(16);
        ZipShort result = new Zip64ExtendedInformationExtraField().getLocalFileDataLength();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expectedResult = new ZipShort(28);
        ZipShort result = new Zip64ExtendedInformationExtraField().getCentralDirectoryLength();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expectedResult = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] result = new Zip64ExtendedInformationExtraField().getLocalFileDataData();
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expectedResult = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] result = new Zip64ExtendedInformationExtraField().getCentralDirectoryData();
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        //No assertion as it doesn't return any value
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        //No assertion as it doesn't return any value
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        try {
            field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
            //No assertion as it doesn't return any value
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSize() {
        ZipEightByteInteger expectedResult = null;
        ZipEightByteInteger result = new Zip64ExtendedInformationExtraField().getSize();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetSize() {
        ZipEightByteInteger size = null;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(size);
        //No assertion as it doesn't return any value
    }

    @Test
    public void testGetCompressedSize() {
        ZipEightByteInteger expectedResult = null;
        ZipEightByteInteger result = new Zip64ExtendedInformationExtraField().getCompressedSize();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetCompressedSize() {
        ZipEightByteInteger compressedSize = null;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setCompressedSize(compressedSize);
        //No assertion as it doesn't return any value
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        ZipEightByteInteger expectedResult = null;
        ZipEightByteInteger result = new Zip64ExtendedInformationExtraField().getRelativeHeaderOffset();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        ZipEightByteInteger rho = null;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setRelativeHeaderOffset(rho);
        //No assertion as it doesn't return any value
    }

    @Test
    public void testGetDiskStartNumber() {
        ZipLong expectedResult = null;
        ZipLong result = new Zip64ExtendedInformationExtraField().getDiskStartNumber();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetDiskStartNumber() {
        ZipLong ds = null;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setDiskStartNumber(ds);
        //No assertion as it doesn't return any value
    }

    @Test
    public void testAddSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];
        int expectedResult = 0;
        int result = field.addSizes(data);
        assertEquals(expectedResult, result);
    }
}