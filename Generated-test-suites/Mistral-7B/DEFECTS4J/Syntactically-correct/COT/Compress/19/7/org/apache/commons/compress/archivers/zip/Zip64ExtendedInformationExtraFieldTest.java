import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthWithValidSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(new ZipLong(1024)),
            new ZipEightByteInteger(512)
        );
        ZipShort expected = new ZipShort(2 * DWORD);
        assertEquals(expected, field.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLengthWithoutSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        assertEquals(expected, field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithValidSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(new ZipLong(1024)),
            new ZipEightByteInteger(512),
            new ZipEightByteInteger(new ZipLong(42)),
            new ZipLong(1)
        );
        ZipShort expected = new ZipShort(
            2 * DWORD + DWORD + DWORD + WORD
        );
        assertEquals(expected, field.getCentralDirectoryLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithoutSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        assertEquals(expected, field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataDataWithValidSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(new ZipLong(1024)),
            new ZipEightByteInteger(512)
        );
        byte[] data = field.getLocalFileDataData();
        byte[] expected = new byte[2 * DWORD];
        assertEquals(expected.length, data.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], data[i]);
        }
    }

    @Test
    public void testGetLocalFileDataDataWithoutSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = field.getLocalFileDataData();
        assertArrayEquals(new byte[0], data);
    }

    @Test
    public void testGetCentralDirectoryDataWithValidSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(new ZipLong(1024)),
            new ZipEightByteInteger(512),
            new ZipEightByteInteger(new ZipLong(42)),
            new ZipLong(1)
        );
        byte[] data = field.getCentralDirectoryData();
        int expectedLength = 2 * DWORD + DWORD + DWORD + WORD;
        assertEquals(expectedLength, data.length);
    }

    @Test
    public void testParseFromLocalFileDataWithValidSizes() throws ZipException {
        byte[] buffer = new byte[2 * DWORD];
        ZipShort sizeByteLength = new ZipShort(2 * DWORD);
        ZipEightByteInteger size = new ZipEightByteInteger(new ZipLong(1024));
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(new ZipLong(512));

        // Write sizes to buffer
        for (int i = 0; i < DWORD; i++) {
            buffer[i] = (byte) (sizeByteLength.getValue() >> (8 * (3 - i)));
        }
        for (int i = 0; i < DWORD; i++) {
            buffer[DWORD + i] = (byte) (size.getValue() >> (8 * (3 - i)));
        }
        for (int i = 0; i < DWORD; i++) {
            buffer[DWORD * 2 + i] = (byte) (compressedSize.getValue() >> (8 * (3 - i)));
        }

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, buffer.length);

        ZipEightByteInteger expectedSize = new ZipEightByteInteger(new ZipLong(1024));
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(new ZipLong(512));
        assertEquals(expectedSize, field.size);
        assertEquals(expectedCompressedSize, field.compressedSize);
        assertNull(field.relativeHeaderOffset);
        assertNull(field.diskStart);
    }

    @Test
    public void testParseFromLocalFileDataWithoutSizes() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[0];
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertNull(field.size);
        assertNull(field.compressedSize);
        assertNull(field.relativeHeaderOffset);
        assertNull(field.diskStart);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(field.size);
        assertNull(field.compressedSize);
        assertNull(field.relativeHeaderOffset);
        assertNull(field.diskStart);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithValidData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        ZipEightByteInteger expectedSize = new ZipEightByteInteger(new ZipLong(1024));
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(new ZipLong(512));
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(new ZipLong(42));
        ZipLong expectedDiskStart = new ZipLong(1);

        assertEquals(expectedSize, field.size);
        assertEquals(expectedCompressedSize, field.compressedSize);
        assertEquals(expectedRelativeHeaderOffset, field.relativeHeaderOffset);
        assertEquals(expectedDiskStart, field.diskStart);
    }

    @Test
    public void testReparseCentralDirectoryDataWithValidData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        field.reparseCentralDirectoryData(
            true, true, true, true
        );

        ZipEightByteInteger expectedSize = new ZipEightByteInteger(new ZipLong(1024));
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(new ZipLong(512));
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(new ZipLong(42));
        ZipLong expectedDiskStart = new ZipLong(1);

        assertEquals(expectedSize, field.size);
        assertEquals(expectedCompressedSize, field.compressedSize);
        assertEquals(expectedRelativeHeaderOffset, field.relativeHeaderOffset);
        assertEquals(expectedDiskStart, field.diskStart);
    }

    @Test
    public void testReparseCentralDirectoryDataMissingSizes() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        assertThrows(ZipException.class, () -> {
            field.reparseCentralDirectoryData(
                false, true, true, true
            );
        });
    }

    @Test
    public void testReparseCentralDirectoryDataMissingOffset() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        assertThrows(ZipException.class, () -> {
            field.reparseCentralDirectoryData(
                true, false, true, true
            );
        });
    }

    @Test
    public void testReparseCentralDirectoryDataMissingDiskStart() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        field.reparseCentralDirectoryData(
            true, true, false, true
        );

        assertNull(field.diskStart);
    }

}