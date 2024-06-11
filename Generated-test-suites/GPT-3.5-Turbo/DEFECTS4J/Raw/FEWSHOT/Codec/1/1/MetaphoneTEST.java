package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetaphoneTest {
	
	private static Metaphone metaphone;
	
	@BeforeClass
	public static void setUp() {
		metaphone = new Metaphone();
	}
	
	@Test(expected=EncoderException.class)
	public void testEncodeInvalidType() throws EncoderException {
		metaphone.encode(123);
	}
	
	@Test
	public void testEncodeEmptyString() {
		assertEquals("", metaphone.encode(""));
	}
	
	@Test
	public void testEncodeSingleCharacter() {
		assertEquals("A", metaphone.encode("a"));
		assertEquals("S", metaphone.encode("s"));
		assertEquals("Z", metaphone.encode("z"));
	}
	
	@Test
	public void testEncodeValidString() {
		assertEquals("PRSTN", metaphone.encode("person"));
		assertEquals("ALF", metaphone.encode("alpha"));
	}
	
	@Test
	public void testMetaphoneEqual() {
		assertTrue(metaphone.isMetaphoneEqual("person", "pursin"));
		assertTrue(metaphone.isMetaphoneEqual("alpha", "alf"));
		assertTrue(metaphone.isMetaphoneEqual("cat", "kat"));
		assertFalse(metaphone.isMetaphoneEqual("test", "best"));
		assertFalse(metaphone.isMetaphoneEqual("apple", "banana"));
	}
	
	@Test
	public void testDefaultMaxCodeLen() {
		assertEquals(4, metaphone.getMaxCodeLen());
	}
	
	@Test
	public void testSetMaxCodeLen() {
		metaphone.setMaxCodeLen(6);
		assertEquals(6, metaphone.getMaxCodeLen());
	}
}