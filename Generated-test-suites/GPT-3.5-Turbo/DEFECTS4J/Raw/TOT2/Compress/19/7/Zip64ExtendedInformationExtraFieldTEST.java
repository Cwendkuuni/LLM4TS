import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;

import java.util.zip.ZipException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(80);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertNotNull(extraField);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(80);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(50);
        ZipLong diskStart = new ZipLong(1);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertNotNull(extraField);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSizes() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    @Test
    public void testLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(100));
        extraField.setCompressedSize(new ZipEightByteInteger(80));

        assertEquals(16, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(100));
        extraField.setCompressedSize(new ZipEightByteInteger(80));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(50));
        extraField.setDiskStartNumber(new ZipLong(1));

        assertEquals(26, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        int length = 16;
        byte[] data = {0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 80};

        extraField.parseFromLocalFileData(data, 0, length);

        assertEquals(100, extraField.getSize().getValue());
        assertEquals(80, extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        int length = 8;
        byte[] data = {0, 0, 0, 100, 0, 0, 0, 80};

        extraField.parseFromCentralDirectoryData(data, 0, length);

        assertEquals(100, extraField.getSize().getValue());
        assertEquals(80, extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(new byte[]{0, 0, 0, 100, 0, 0, 0, 80}, 0, 8);

        extraField.reparseCentralDirectoryData(true, true, false, false);

        assertEquals(100, extraField.getSize().getValue());
        assertEquals(80, extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }
}