package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArchiveUtilsTest {
	
	private ArchiveEntry entry;
	private byte[] buffer;
	
	@Before
	public void setUp() {
		entry = new ArchiveEntry("test", false, 100);
		buffer = new byte[] { 1, 2, 3, 4, 5 };
	}
	
	@Test
	public void testToString() {
		assertEquals("-    100 test", ArchiveUtils.toString(entry));
		
		entry.setDirectory(true);
		assertEquals("d    100 test", ArchiveUtils.toString(entry));
	}
	
	@Test
	public void testMatchAsciiBuffer() {
		assertTrue(ArchiveUtils.matchAsciiBuffer("12345", buffer));
		
		assertFalse(ArchiveUtils.matchAsciiBuffer("23456", buffer));
	}
	
	@Test
	public void testToAsciiBytes() {
		assertArrayEquals(new byte[] { 49, 50, 51, 52, 53 }, ArchiveUtils.toAsciiBytes("12345"));
	}
	
	@Test
	public void testToAsciiString() {
		assertEquals("12345", ArchiveUtils.toAsciiString(new byte[] { 49, 50, 51, 52, 53 }));
	}
	
	@Test
	public void testToAsciiStringWithOffsetAndLength() {
		assertEquals("23", ArchiveUtils.toAsciiString(new byte[] { 49, 50, 51, 52, 53 }, 1, 2));
	}
	
	@Test
	public void testIsEqual() {
		assertTrue(ArchiveUtils.isEqual(new byte[] { 1, 2, 3 }, new byte[] { 1, 2, 3 }));
		
		assertFalse(ArchiveUtils.isEqual(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 }));
	}
	
	@Test
	public void testIsEqualWithOffsetAndLength() {
		assertTrue(ArchiveUtils.isEqual(new byte[] { 1, 2, 3 }, 0, 3, new byte[] { 1, 2, 3 }, 0, 3));
		
		assertFalse(ArchiveUtils.isEqual(new byte[] { 1, 2, 3 }, 0, 3, new byte[] { 4, 5, 6 }, 0, 3));
	}
	
	@Test
	public void testIsEqualWithIgnoreTrailingNulls() {
		assertTrue(ArchiveUtils.isEqual(new byte[] { 1, 2, 3, 0, 0 }, new byte[] { 1, 2, 3 }));
		
		assertFalse(ArchiveUtils.isEqual(new byte[] { 1, 2, 3, 4 }, new byte[] { 1, 2, 3 }));
	}
	
	@Test
	public void testIsArrayZero() {
		assertTrue(ArchiveUtils.isArrayZero(new byte[] { 0, 0, 0, 0, 0 }, 5));
		
		assertFalse(ArchiveUtils.isArrayZero(new byte[] { 0, 0, 0, 0, 1 }, 5));
	}
	
	@Test
	public void testSanitize() {
		assertEquals("test", ArchiveUtils.sanitize("test"));
		
		assertEquals("?", ArchiveUtils.sanitize("\u0000"));
	}
}