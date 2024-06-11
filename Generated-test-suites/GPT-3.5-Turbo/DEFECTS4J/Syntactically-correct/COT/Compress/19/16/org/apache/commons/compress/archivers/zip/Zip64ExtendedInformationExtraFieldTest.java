
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

	private Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField;

	@Before
	public void setUp() {
		zip64ExtendedInformationExtraField = new Zip64ExtendedInformationExtraField();
	}

	@Test
	public void testGetHeaderId() {
		assertEquals(Zip64ExtendedInformationExtraField.HEADER_ID, zip64ExtendedInformationExtraField.getHeaderId());
	}

	@Test
	public void testGetLocalFileDataLength() {
		ZipShort expected = new ZipShort(16);
		assertEquals(expected, zip64ExtendedInformationExtraField.getLocalFileDataLength());
	}

	@Test
	public void testGetCentralDirectoryLength() {
		ZipShort expected = new ZipShort(24);
		assertEquals(expected, zip64ExtendedInformationExtraField.getCentralDirectoryLength());
	}

	@Test
	public void testGetLocalFileDataData() {
		byte[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		assertArrayEquals(expected, zip64ExtendedInformationExtraField.getLocalFileDataData());
	}

	@Test
	public void testGetCentralDirectoryData() {
		byte[] expected = new byte[24];
		assertArrayEquals(expected, zip64ExtendedInformationExtraField.getCentralDirectoryData());
	}

	@Test
	public void testParseFromLocalFileData() {
		byte[] buffer = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int offset = 0;
		int length = 12;

		zip64ExtendedInformationExtraField.parseFromLocalFileData(buffer, offset, length);

		assertEquals(new ZipEightByteInteger(buffer, offset), zip64ExtendedInformationExtraField.getSize());
		assertEquals(new ZipEightByteInteger(buffer, offset + 8), zip64ExtendedInformationExtraField.getCompressedSize());
	}

	@Test
	public void testParseFromCentralDirectoryData() {
		byte[] buffer = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int offset = 0;
		int length = 12;

		zip64ExtendedInformationExtraField.parseFromCentralDirectoryData(buffer, offset, length);

		assertEquals(new ZipEightByteInteger(buffer, offset), zip64ExtendedInformationExtraField.getSize());
		assertEquals(new ZipEightByteInteger(buffer, offset + 8), zip64ExtendedInformationExtraField.getCompressedSize());
	}

	@Test
	public void testSetSize() {
		zip64ExtendedInformationExtraField.setSize(new ZipEightByteInteger(100L));
		assertEquals(new ZipEightByteInteger(100L), zip64ExtendedInformationExtraField.getSize());
	}

	@Test
	public void testSetCompressedSize() {
		zip64ExtendedInformationExtraField.setCompressedSize(new ZipEightByteInteger(100L));
		assertEquals(new ZipEightByteInteger(100L), zip64ExtendedInformationExtraField.getCompressedSize());
	}

	@Test
	public void testSetRelativeHeaderOffset() {
		zip64ExtendedInformationExtraField.setRelativeHeaderOffset(new ZipEightByteInteger(100L));
		assertEquals(new ZipEightByteInteger(100L), zip64ExtendedInformationExtraField.getRelativeHeaderOffset());
	}

	@Test
	public void testSetDiskStartNumber() {
		zip64ExtendedInformationExtraField.setDiskStartNumber(new ZipLong(1));
		assertEquals(new ZipLong(1), zip64ExtendedInformationExtraField.getDiskStartNumber());
	}

}
