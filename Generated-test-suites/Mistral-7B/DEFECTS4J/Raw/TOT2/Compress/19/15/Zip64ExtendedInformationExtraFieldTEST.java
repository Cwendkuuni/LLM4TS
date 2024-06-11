import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(5L),
            new ZipEightByteInteger(2L)
        );
        assertEquals(2 * DWORD, extraField.getLocalFileDataLength().getValue());

        extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(5L),
            new ZipEightByteInteger(2L),
            new ZipEightByteInteger(3L),
            new ZipLong(7L)
        );
        assertEquals(
            2 * DWORD + DWORD + DWORD + WORD,
            extraField.getCentralDirectoryLength().getValue()
        );

        extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testConstructorWithNullSizes() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(null, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(5L), null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(2L));
        });
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizes() {
        byte[] data = new byte[2 * DWORD];
        new ZipShort(0x0001).writeTo(data, 0);
        new ZipEightByteInteger(5L).writeTo(data, DWORD);
        new ZipEightByteInteger(2L).writeTo(data, 2 * DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(5, extraField.getSize().getValue());
        assertEquals(2, extraField.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromLocalFileDataWithoutBothSizes() {
        byte[] data = new byte[DWORD];
        new ZipShort(0x0001).writeTo(data, 0);
        new ZipEightByteInteger(5L).writeTo(data, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        // should not throw an exception
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
    }

    @Test
    public void testGetLocalFileDataDataWithBothSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(5L),
            new ZipEightByteInteger(2L)
        );

        byte[] data = extraField.getLocalFileDataData();
        assertEquals(2 * DWORD, data.length);
    }

    @Test
    public void testGetLocalFileDataDataWithoutBothSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        byte[] data = extraField.getLocalFileDataData();
        assertEquals(0, data.length);
    }

    @Test
    public void testGetCentralDirectoryDataWithAllFieldsPresent() {
        byte[] data = new byte[4 * DWORD + WORD];
        new ZipShort(0x0001).writeTo(data, 0);
        new ZipEightByteInteger(5L).writeTo(data, DWORD);
        new ZipEightByteInteger(2L).writeTo(data, 2 * DWORD);
        new ZipEightByteInteger(3L).writeTo(data, 3 * DWORD);
        new ZipLong(7L).writeTo(data, 4 * DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        assertEquals(5, extraField.getSize().getValue());
        assertEquals(2, extraField.getCompressedSize().getValue());
        assertEquals(3, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(7, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testGetCentralDirectoryDataWithNoSizes() {
        byte[] data = new byte[2 * DWORD];
        new ZipShort(0x0001).writeTo(data, 0);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
    }

    @Test
    public void testGetCentralDirectoryDataWithOnlyRelativeHeaderOffsetAndDiskStart() {
        byte[] data = new byte[3 * DWORD + WORD];
        new ZipShort(0x0001).writeTo(data, 0);
        new ZipLong(3L).writeTo(data, 3 * DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(3, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(3, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testGetCentralDirectoryDataWithOnlyDiskStart() {
        byte[] data = new byte[2 * DWORD + WORD];
        new ZipShort(0x0001).writeTo(data, 0);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertEquals(0, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        new ZipShort(0x0001).writeTo(centralDirectoryData, 0);
        new ZipEightByteInteger(5L).writeTo(centralDirectoryData, DWORD);
        new ZipEightByteInteger(2L).writeTo(centralDirectoryData, 2 * DWORD);
        new ZipEightByteInteger(3L).writeTo(centralDirectoryData, 3 * DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(5, extraField.getSize().getValue());
        assertEquals(2, extraField.getCompressedSize().getValue());
        assertEquals(3, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoSizes() throws ZipException {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        new ZipShort(0x0001).writeTo(centralDirectoryData, 0);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, false, false);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithOnlyRelativeHeaderOffsetAndDiskStart() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        new ZipShort(0x0001).writeTo(centralDirectoryData, 0);
        new ZipLong(3L).writeTo(centralDirectoryData, 3 * DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, true, true);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(3, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(3, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithOnlyDiskStart() throws ZipException {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];
        new ZipShort(0x0001).writeTo(centralDirectoryData, 0);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, false, true);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }
}