package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

	@Test
	public void testGetHeaderId() {
		ZipShort expected = new ZipShort(0x0001);
		ZipShort actual = new Zip64ExtendedInformationExtraField().getHeaderId();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetLocalFileDataLength() {
		ZipShort expected = new ZipShort(16);
		ZipShort actual = new Zip64ExtendedInformationExtraField().getLocalFileDataLength();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCentralDirectoryLength() {
		ZipShort expected = new ZipShort(20);
		ZipShort actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryLength();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetLocalFileDataData() {
		byte[] expected = new byte[16];
		byte[] actual = new Zip64ExtendedInformationExtraField().getLocalFileDataData();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testGetCentralDirectoryData() {
		byte[] expected = new byte[20];
		byte[] actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryData();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testParseFromLocalFileData() throws Exception {
		byte[] buffer = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.parseFromLocalFileData(buffer, 0, buffer.length);
	}

	@Test
	public void testParseFromCentralDirectoryData() throws Exception {
		byte[] buffer = new byte[] { 0, 0, 0, 0 };
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
	}

	@Test
	public void testReparseCentralDirectoryData() throws Exception {
		boolean hasUncompressedSize = true;
		boolean hasCompressedSize = true;
		boolean hasRelativeHeaderOffset = true;
		boolean hasDiskStart = true;
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
	}

	@Test
	public void testGetSize() {
		ZipEightByteInteger expected = new ZipEightByteInteger(0);
		ZipEightByteInteger actual = new Zip64ExtendedInformationExtraField().getSize();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetSize() {
		ZipEightByteInteger size = new ZipEightByteInteger(100L);
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.setSize(size);
		assertEquals(size, extraField.getSize());
	}

	@Test
	public void testGetCompressedSize() {
		ZipEightByteInteger expected = new ZipEightByteInteger(0);
		ZipEightByteInteger actual = new Zip64ExtendedInformationExtraField().getCompressedSize();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetCompressedSize() {
		ZipEightByteInteger compressedSize = new ZipEightByteInteger(100L);
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.setCompressedSize(compressedSize);
		assertEquals(compressedSize, extraField.getCompressedSize());
	}

	@Test
	public void testGetRelativeHeaderOffset() {
		ZipEightByteInteger expected = null;
		ZipEightByteInteger actual = new Zip64ExtendedInformationExtraField().getRelativeHeaderOffset();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetRelativeHeaderOffset() {
		ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(100L);
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.setRelativeHeaderOffset(relativeHeaderOffset);
		assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
	}

	@Test
	public void testGetDiskStartNumber() {
		ZipLong expected = null;
		ZipLong actual = new Zip64ExtendedInformationExtraField().getDiskStartNumber();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetDiskStartNumber() {
		ZipLong diskStartNumber = new ZipLong(1);
		Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
		extraField.setDiskStartNumber(diskStartNumber);
		assertEquals(diskStartNumber, extraField.getDiskStartNumber());
	}

}
