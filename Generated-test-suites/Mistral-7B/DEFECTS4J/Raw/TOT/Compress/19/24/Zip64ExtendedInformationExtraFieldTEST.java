import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidConstruction() {
        // valid sizes
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x11223344L);
        ZipLong diskStartNumber = new ZipLong(0x00000001L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStartNumber);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
        assertEquals(2 * ZipConstants.DWORD + ZipConstants.WORD, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testEmptyConstruction() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getLocalFileDataLength().getValue());
        assertEquals(0, extraField.getCentralDirectoryLength().getValue());
        assertArrayEquals(new byte[0], extraField.getLocalFileDataData());
        assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSizeValueInLocalFileData() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] localFileData = extraField.getLocalFileDataData();
        assertEquals(2 * ZipConstants.DWORD, localFileData.length);

        // remove the size value
        byte[] newData = new byte[localFileData.length - ZipConstants.DWORD];
        System.arraycopy(localFileData, ZipConstants.DWORD, newData, 0, newData.length);
        extraField.setLocalFileDataData(newData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSizeValueInCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x11223344L);

        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        // remove the size value
        byte[] newData = new byte[centralDirectoryData.length - (ZipConstants.DWORD * 3)];
        System.arraycopy(centralDirectoryData, ZipConstants.DWORD * 3, newData, 0, newData.length);
        extraField.setCentralDirectoryData(newData);
    }

    @Test
    public void testParseLocalFileData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);

        byte[] localFileData = new byte[2 * ZipConstants.DWORD];
        addSizes(localFileData);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testParseCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x11223344L);
        ZipLong diskStartNumber = new ZipLong(0x00000001L);

        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD * 2, ZipConstants.DWORD);
        System.arraycopy(diskStartNumber.getBytes(), 0, centralDirectoryData, centralDirectoryData.length - ZipConstants.WORD, ZipConstants.WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingEmptyCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[0];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(0, extraField.getSize().getValue());
        assertEquals(0, extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingTruncatedCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x11223344L);
        ZipLong diskStartNumber = new ZipLong(0x00000001L);

        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingPartialCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x11223344L);
        ZipLong diskStartNumber = new ZipLong(0x00000001L);

        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD + ZipConstants.WORD];
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD * 2, ZipConstants.DWORD);
        System.arraycopy(diskStartNumber.getBytes(), 0, centralDirectoryData, centralDirectoryData.length - ZipConstants.WORD * 2, ZipConstants.WORD * 2);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
    }

}