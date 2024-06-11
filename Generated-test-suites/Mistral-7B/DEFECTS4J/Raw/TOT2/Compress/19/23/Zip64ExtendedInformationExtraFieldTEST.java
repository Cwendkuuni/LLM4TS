import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(12345L);
        ZipLong diskStart = new ZipLong(67890L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), extraField.getLocalFileDataLength());
        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), null);
        assertEquals(new ZipShort(DWORD), extraField.getLocalFileDataLength());
        extraField = new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(512L));
        assertEquals(new ZipShort(DWORD), extraField.getLocalFileDataLength());
        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        assertEquals(new ZipShort(2 * DWORD), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), extraField.getCentralDirectoryLength());
        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), null);
        assertEquals(new ZipShort(DWORD), extraField.getCentralDirectoryLength());
        extraField = new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(512L));
        assertEquals(new ZipShort(DWORD), extraField.getCentralDirectoryLength());
        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        assertEquals(new ZipShort(3 * DWORD), extraField.getCentralDirectoryLength());
        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L), null, null);
        assertEquals(new ZipShort(3 * DWORD + WORD), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        byte[] data = extraField.getLocalFileDataData();
        assertEquals(2 * DWORD, data.length);
        extraField = new Zip64ExtendedInformationExtraField();
        data = extraField.getLocalFileDataData();
        assertEquals(0, data.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        System.arraycopy(size.getBytes(), 0, buffer, 0, DWORD);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        System.arraycopy(compressedSize.getBytes(), 0, buffer, DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromLocalFileDataWithMissingValues() {
        byte[] buffer = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(buffer, 0, buffer.length));
        buffer = new byte[2 * DWORD - 1];
        assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(buffer, 0, buffer.length));
        buffer = new byte[2 * DWORD + 1];
        assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(buffer, 0, buffer.length));
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllValues() {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(1024L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(512L), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithMissingValues() {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length));
        buffer = new byte[3 * DWORD + WORD - 1];
        assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length));
        buffer = new byte[3 * DWORD + WORD + 1];
        assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length));
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlySize() {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyCompressedSize() {
        byte[] buffer = new byte[3 * DWORD + DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
        assertEquals(new ZipEightByteInteger(512L), extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyRelativeHeaderOffset() {
        byte[] buffer = new byte[3 * DWORD + DWORD + DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(12345L), extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStart() {
        byte[] buffer = new byte[3 * DWORD + DWORD + DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(67890L), extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(1024L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(512L), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingValues() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
        extraField.reparseCentralDirectoryData(false, true, true, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
        extraField.reparseCentralDirectoryData(false, false, false, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testSetters() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(1024L));
        assertEquals(new ZipEightByteInteger(1024L), extraField.getSize());
        extraField.setCompressedSize(new ZipEightByteInteger(512L));
        assertEquals(new ZipEightByteInteger(512L), extraField.getCompressedSize());
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(12345L));
        assertEquals(new ZipEightByteInteger(12345L), extraField.getRelativeHeaderOffset());
        extraField.setDiskStartNumber(new ZipLong(67890L));
        assertEquals(new ZipLong(67890L), extraField.getDiskStartNumber());
    }

    @Test
    public void testToString() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        assertEquals("Zip64ExtendedInformationExtraField(size=1024, compressedSize=512)", extraField.toString());
        extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L), new ZipEightByteInteger(12345L), new ZipLong(67890L));
        assertEquals("Zip64ExtendedInformationExtraField(size=1024, compressedSize=512, relativeHeaderOffset=12345, diskStartNumber=67890)", extraField.toString());
        extraField = new Zip64ExtendedInformationExtraField();
        assertEquals("Zip64ExtendedInformationExtraField()", extraField.toString());
    }
}