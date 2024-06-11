package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Locale;

public class MetaphoneTest {

	@Test
	public void testMetaphoneEmptyString() {
		Metaphone metaphone = new Metaphone();
		assertEquals("", metaphone.metaphone(""));
	}

	@Test
	public void testMetaphoneSingleCharacter() {
		Metaphone metaphone = new Metaphone();
		assertEquals("A", metaphone.metaphone("A"));
		assertEquals("B", metaphone.metaphone("B"));
		// test for other single characters
	}

	@Test
	public void testMetaphoneTwoCharacters() {
		Metaphone metaphone = new Metaphone();
		assertEquals("BA", metaphone.metaphone("AB")); // A and B are next to each other
		assertEquals("KN", metaphone.metaphone("AK")); // initial K becomes KN
		assertEquals("AE", metaphone.metaphone("AE")); // AE becomes AE
		assertEquals("WR", metaphone.metaphone("WR")); // WR remains WR
		assertEquals("XS", metaphone.metaphone("X")); // initial X becomes S
		// other test cases
	}

	@Test(expected = EncoderException.class)
	public void testMetaphoneNullString() {
		Metaphone metaphone = new Metaphone();
		metaphone.metaphone(null);
	}

	@Test(expected = EncoderException.class)
	public void testMetaphoneEmptyArray() {
		Metaphone metaphone = new Metaphone();
		metaphone.metaphone(new String[0]);
	}

	@Test
	public void testMetaphoneMultipleCharacters() {
		Metaphone metaphone = new Metaphone();
		// edge cases
		assertEquals("BX", metaphone.metaphone("ABC")); // remove duplicate 'B'
		assertEquals("BXS", metaphone.metaphone("ABCDE")); // max code size of 4
		assertEquals("BXS", metaphone.metaphone("ABCDEFG")); // max code size of 4
		assertEquals("BXSJ", metaphone.metaphone("ABCDEFGHJ")); // detect 'DGE', 'DGI', 'DGY' -> 'J'
		assertEquals("BKJ", metaphone.metaphone("ABGH")); // silent 'G'
		assertEquals("BH", metaphone.metaphone("ABH")); // terminal 'H'
		// other test cases
	}

	@Test
	public void testMetaphoneUpperCase() {
		Metaphone metaphone = new Metaphone();
		assertEquals("BA", metaphone.metaphone("ab")); // case insensitive
	}

	@Test
	public void testMetaphoneSpecialCharacters() {
		Metaphone metaphone = new Metaphone();
		assertEquals("BX", metaphone.metaphone("A#B")); // remove duplicate 'B'
		assertEquals("BXS", metaphone.metaphone("A#B#C#DE")); // max code size of 4
		assertEquals("BXS", metaphone.metaphone("A#B#C#DEFG")); // max code size of 4
		// other test cases with special characters
	}

	@Test
	public void testMetaphoneVowels() {
		Metaphone metaphone = new Metaphone();
		assertEquals("AEAX", metaphone.metaphone("AEAIEOU"));
	}

	@Test
	public void testMetaphoneConsistent() {
		Metaphone metaphone = new Metaphone();
		assertEquals("BXS", metaphone.metaphone("Apple"));
		assertEquals("BXS", metaphone.metaphone("Apples"));
		assertEquals("BXS", metaphone.metaphone("Applesauce"));
		assertEquals("BXS", metaphone.metaphone("Applepie"));
	}

	// add more test cases as needed

	@Test
	public void testEncode() {
		Metaphone metaphone = new Metaphone();
		assertEquals("BA", metaphone.encode("AB"));
		assertEquals("KN", metaphone.encode("AK"));
		// other test cases
	}

	@Test
	public void testIsMetaphoneEqual() {
		Metaphone metaphone = new Metaphone();
		assertEquals(true, metaphone.isMetaphoneEqual("AB", "AB"));
		assertEquals(false, metaphone.isMetaphoneEqual("AB", "BA"));
		// other test cases
	}

	@Test
	public void testGetMaxCodeLen() {
		Metaphone metaphone = new Metaphone();
		assertEquals(4, metaphone.getMaxCodeLen());
	}

	@Test
	public void testSetMaxCodeLen() {
		Metaphone metaphone = new Metaphone();
		metaphone.setMaxCodeLen(5);
		assertEquals(5, metaphone.getMaxCodeLen());
	}

}