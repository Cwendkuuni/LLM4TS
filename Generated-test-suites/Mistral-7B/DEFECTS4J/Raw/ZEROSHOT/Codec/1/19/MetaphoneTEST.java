import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MetaphoneTest {

	@Test
	public void testMetaphoneEmptyString() {
		Metaphone metaphone = new Metaphone();
		String actual = metaphone.metaphone("");
		assertNull(actual);
	}

	@Test
	public void testMetaphoneSingleCharacterString() {
		Metaphone metaphone = new Metaphone();
		String actual = metaphone.metaphone("A");
		assertEquals("A", actual);
	}

	@Test
	public void testMetaphoneSimpleString() {
		Metaphone metaphone = new Metaphone();
		String actual = metaphone.metaphone("APPLE");
		assertEquals("A1P1L1E", actual);
	}

	@Test
	public void testMetaphoneComplexString() {
		Metaphone metaphone = new Metaphone();
		String actual = metaphone.metaphone("BANANAS");
		assertEquals("B2N1A1N1S", actual);
	}

	@Test
	public void testMetaphoneStringsWithSameCode() {
		Metaphone metaphone = new Metaphone();
		String actual1 = metaphone.metaphone("APPLE");
		String actual2 = metaphone.metaphone("BANANA");
		assertEquals(actual1, actual2);
	}

	@Test
	public void testMetaphoneStringsWithDifferentCodes() {
		Metaphone metaphone = new Metaphone();
		String actual1 = metaphone.metaphone("APPLE");
		String actual2 = metaphone.metaphone("BANANA");
		assertFalse(actual1.equals(actual2));
	}

	@Test(expected = DecoderException.class)
	public void testMetaphoneNullString() {
		Metaphone metaphone = new Metaphone();
		metaphone.metaphone(null);
	}

	@Test
	public void testIsMetaphoneEqualEmptyString1() {
		Metaphone metaphone = new Metaphone();
		boolean actual = metaphone.isMetaphoneEqual("", "");
		assertTrue(actual);
	}

	@Test
	public void testIsMetaphoneEqualEmptyString2() {
		Metaphone metaphone = new Metaphone();
		boolean actual = metaphone.isMetaphoneEqual("", "APPLE");
		assertFalse(actual);
	}

	@Test
	public void testIsMetaphoneEqualSimpleStrings() {
		Metaphone metaphone = new Metaphone();
		boolean actual = metaphone.isMetaphoneEqual("APPLE", "APPLE");
		assertTrue(actual);
	}

	@Test
	public void testIsMetaphoneEqualComplexStrings() {
		Metaphone metaphone = new Metaphone();
		boolean actual = metaphone.isMetaphoneEqual("BANANA", "APPLE");
		assertFalse(actual);
	}

	@Test
	public void testEncodeEncode() throws EncoderException {
		Metaphone metaphone = new Metaphone();
		String input = "APPLE";
		assertEquals(metaphone.metaphone(input), metaphone.encode(input));
	}

	@Test
	public void testEncodeNullString() throws EncoderException {
		Metaphone metaphone = new Metaphone();
		assertNull(metaphone.encode(null));
	}

	@Test
	public void testEncodeString() throws EncoderException {
		Metaphone metaphone = new Metaphone();
		String input = "APPLE";
		String actual = metaphone.encode(input);
		assertEquals(metaphone.metaphone(input), actual);
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