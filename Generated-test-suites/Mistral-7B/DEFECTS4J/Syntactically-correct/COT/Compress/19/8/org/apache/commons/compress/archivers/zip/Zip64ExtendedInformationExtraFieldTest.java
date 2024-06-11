import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = testObject.getHeaderId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithNullSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        ZipShort actual = testObject.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(new ZipLong(10)));
        ZipShort expected = new ZipShort(2 * DWORD);
        ZipShort actual = testObject.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithNullSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(null), new ZipEightByteInteger(new ZipLong(10)));
    }

    @Test
    public void testGetCentralDirectoryLengthWithNullSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        ZipShort actual = testObject.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLengthWithSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(new ZipLong(10)), new ZipEightByteInteger(new ZipLong(20)), new ZipEightByteInteger(new ZipLong(30)), new ZipLong(40));
        ZipShort expected = new ZipShort((2 * DWORD) + (3 * DWORD));
        ZipShort actual = testObject.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCentralDirectoryLengthWithNullSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(new ZipLong(10)), null, new ZipEightByteInteger(new ZipLong(30)), new ZipLong(40));
    }

    @Test
    public void testGetLocalFileDataDataWithNullSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] expected = new byte[0];
        byte[] actual = testObject.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataDataWithSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(new ZipLong(10)), new ZipEightByteInteger(new ZipLong(20)));
        byte[] expected = new byte[2 * DWORD];
        byte[] actual = testObject.getLocalFileDataData();
        assertArrayEquals(expected, actual);
        testObject.addSizes(expected);
        byte[] newExpected = new byte[2 * DWORD];
        System.arraycopy(testObject.size.getBytes(), 0, newExpected, 0, DWORD);
        System.arraycopy(testObject.compressedSize.getBytes(), 0, newExpected, DWORD, DWORD);
        assertArrayEquals(newExpected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataDataWithNullSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(null), new ZipEightByteInteger(new ZipLong(20)));
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = new byte[4 * DWORD];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        new ZipEightByteInteger(new ZipLong(10)).getBytes(buffer, 0);
        testObject.parseFromLocalFileData(buffer, 0, 4 * DWORD);
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.size);
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.compressedSize);
        assertNull(testObject.relativeHeaderOffset);
        assertNull(testObject.diskStart);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidSize() throws ZipException {
        byte[] buffer = new byte[4 * DWORD - 1];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromLocalFileData(buffer, 0, 4 * DWORD - 1);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        new ZipEightByteInteger(new ZipLong(10)).getBytes(buffer, 0);
        new ZipEightByteInteger(new ZipLong(20)).getBytes(buffer, DWORD);
        testObject.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD + WORD);
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.size);
        assertEquals(new ZipEightByteInteger(new ZipLong(20)), testObject.compressedSize);
        assertNull(testObject.relativeHeaderOffset);
        assertNull(testObject.diskStart);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD - 3];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD + WORD - 3);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        new ZipEightByteInteger(new ZipLong(10)).getBytes(buffer, 0);
        new ZipEightByteInteger(new ZipLong(20)).getBytes(buffer, DWORD);
        testObject.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        testObject.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.size);
        assertEquals(new ZipEightByteInteger(new ZipLong(20)), testObject.compressedSize);
        assertNull(testObject.relativeHeaderOffset);
        assertNull(testObject.diskStart);
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setSize(new ZipEightByteInteger(new ZipLong(10)));
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSizeWithNullSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setSize(null);
    }

    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setCompressedSize(new ZipEightByteInteger(new ZipLong(10)));
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCompressedSizeWithNullSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setCompressedSize(null);
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setRelativeHeaderOffset(new ZipEightByteInteger(new ZipLong(10)));
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), testObject.relativeHeaderOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRelativeHeaderOffsetWithNullOffset() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setRelativeHeaderOffset(null);
    }

    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setDiskStartNumber(new ZipLong(10));
        assertEquals(new ZipLong(10), testObject.diskStartNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDiskStartNumberWithNullDiskStart() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.setDiskStartNumber(null);
    }

}