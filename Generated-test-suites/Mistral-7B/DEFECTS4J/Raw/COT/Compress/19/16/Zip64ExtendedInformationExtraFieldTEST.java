import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(ZipShort.valueOf((short) 0x0001), extraField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength_withSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(DWORD.valueOf(100)), null);
        Assert.assertEquals(new ZipShort(WORD.valueOf(16)), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLength_withoutSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null);
        Assert.assertEquals(new ZipShort(WORD.valueOf(0)), extraField.getLocalFileDataLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLength_missingSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null);
        extraField.getLocalFileDataData();
    }

    @Test
    public void testGetCentralDirectoryLength_withAllSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(DWORD.valueOf(100)),
                new ZipEightByteInteger(DWORD.valueOf(50)),
                new ZipEightByteInteger(DWORD.valueOf(15)),
                new ZipLong(WORD.valueOf(2)));
        Assert.assertEquals(new ZipShort(WORD.valueOf(28)), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetCentralDirectoryLength_withoutSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort(WORD.valueOf(0)), extraField.getCentralDirectoryLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCentralDirectoryLength_missingAllSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, 0);
        extraField.getCentralDirectoryData();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCentralDirectoryLength_missingSomeSizes() {
        byte[] rawCentralDirectoryData = new byte[12];
        System.arraycopy(new ZipShort(WORD.valueOf(12)).getBytes(), 0, rawCentralDirectoryData, 0, WORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(100)).getBytes(), 0, rawCentralDirectoryData, WORD.SIZE, DWORD.SIZE);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.getCentralDirectoryData();
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[16];
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(100)).getBytes(), 0, localFileData, 0, DWORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(50)).getBytes(), 0, localFileData, DWORD.SIZE, DWORD.SIZE);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.assertEquals(new ZipEightByteInteger(DWORD.valueOf(100)), extraField.size);
        Assert.assertEquals(new ZipEightByteInteger(DWORD.valueOf(50)), extraField.compressedSize);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileData_invalidDataSize() throws ZipException {
        byte[] localFileData = new byte[12];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[32];
        System.arraycopy(new ZipShort(WORD.valueOf(32)).getBytes(), 0, rawCentralDirectoryData, 0, WORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(100)).getBytes(), 0, rawCentralDirectoryData, WORD.SIZE, DWORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(50)).getBytes(), 0, rawCentralDirectoryData, DWORD.SIZE+WORD.SIZE, DWORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(15)).getBytes(), 0, rawCentralDirectoryData, 2 * DWORD.SIZE+WORD.SIZE, DWORD.SIZE);
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        Assert.assertEquals(new ZipEightByteInteger(DWORD.valueOf(100)), extraField.size);
        Assert.assertEquals(new ZipEightByteInteger(DWORD.valueOf(50)), extraField.compressedSize);
        Assert.assertEquals(new ZipEightByteInteger(DWORD.valueOf(15)), extraField.relativeHeaderOffset);
        Assert.assertEquals(new ZipLong(WORD.valueOf(2)), extraField.diskStart);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryData_invalidDataLength() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[12];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData_missingSizes() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[24];
        System.arraycopy(new ZipShort(WORD.valueOf(24)).getBytes(), 0, rawCentralDirectoryData, 0, WORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(100)).getBytes(), 0, rawCentralDirectoryData, WORD.SIZE, DWORD.SIZE);
        System.arraycopy(new ZipLong(WORD.valueOf(2)).getBytes(), 0, rawCentralDirectoryData, 2 * DWORD.SIZE, WORD.SIZE);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        Assert.assertNull(extraField.compressedSize);
        Assert.assertNull(extraField.relativeHeaderOffset);
    }

    @Test
    public void testParseFromCentralDirectoryData_missingDiskStart() throws ZipException {
        byte[] rawCentralDirectoryData = new byte[28];
        System.arraycopy(new ZipShort(WORD.valueOf(28)).getBytes(), 0, rawCentralDirectoryData, 0, WORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(100)).getBytes(), 0, rawCentralDirectoryData, WORD.SIZE, DWORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(50)).getBytes(), 0, rawCentralDirectoryData, 2 * DWORD.SIZE, DWORD.SIZE);
        System.arraycopy(new ZipEightByteInteger(DWORD.valueOf(15)).getBytes(), 0, rawCentralDirectoryData, 3 * DWORD.SIZE, DWORD.SIZE);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        Assert.assertNull(extraField.diskStart);
    }
}