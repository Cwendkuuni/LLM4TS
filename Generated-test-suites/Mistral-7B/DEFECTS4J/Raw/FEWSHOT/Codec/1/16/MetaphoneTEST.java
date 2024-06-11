package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class MetaphoneTest {

	private Metaphone metaphoneInstance;

	@Before
	public void setup() {
		metaphoneInstance = new Metaphone();
	}

	@Test
	public void testIsMetaphoneEqual() {
		assertEquals("KNIGHT, KNIGHT", metaphoneInstance.isMetaphoneEqual("KNIGHT", "KNIGHT"));
		assertEquals("APPLE, APPLE", metaphoneInstance.isMetaphoneEqual("APPLE", "APPLE"));
		assertEquals("BANANA, BANANA", metaphoneInstance.isMetaphoneEqual("BANANA", "BANANA"));
		assertEquals("AARDVARK, AARDVARK", metaphoneInstance.isMetaphoneEqual("AARDVARK", "AARDVARK"));
		assertEquals("APPLE, ORANGE", assertFalse(metaphoneInstance.isMetaphoneEqual("APPLE", "ORANGE")));
		assertEquals("AARDVARK, ELEPHANT", assertFalse(metaphoneInstance.isMetaphoneEqual("AARDVARK", "ELEPHANT")));
		assertEquals("CAT, DOG", assertFalse(metaphoneInstance.isMetaphoneEqual("CAT", "DOG")));
		assertEquals("ACTION, EXPLOIT", assertFalse(metaphoneInstance.isMetaphoneEqual("ACTION", "EXPLOIT")));
		assertEquals("ALICE, AARON", assertFalse(metaphoneInstance.isMetaphoneEqual("ALICE", "AARON")));
	}

	@Test
	public void testEncoder() throws EncoderException {
		assertEquals("K2N2T", metaphoneInstance.encode("KNIGHT"));
		assertEquals("A1P2L2E", metaphoneInstance.encode("APPLE"));
		assertEquals("B1N1N1A1N1A1", metaphoneInstance.encode("BANANA"));
		assertEquals("A1R1D1V1A1R1K", metaphoneInstance.encode("AARDVARK"));
		assertEquals(null, metaphoneInstance.encode(null));
		assertEquals(null, metaphoneInstance.encode(""));
	}

	@Test
	public void testEncodedStringEqual() {
		assertEquals(metaphoneInstance.encode("KNIGHT"), metaphoneInstance.encode("KNIGHT"));
		assertEquals(metaphoneInstance.encode("APPLE"), metaphoneInstance.encode("APPLE"));
		assertEquals(metaphoneInstance.encode("BANANA"), metaphoneInstance.encode("BANANA"));
		assertEquals(metaphoneInstance.encode("AARDVARK"), metaphoneInstance.encode("AARDVARK"));
		assertFalse(metaphoneInstance.encode("APPLE").equals(metaphoneInstance.encode("ORANGE")));
		assertFalse(metaphoneInstance.encode("AARDVARK").equals(metaphoneInstance.encode("ELEPHANT")));
		assertFalse(metaphoneInstance.encode("CAT").equals(metaphoneInstance.encode("DOG")));
		assertFalse(metaphoneInstance.encode("ACTION").equals(metaphoneInstance.encode("EXPLOIT")));
		assertFalse(metaphoneInstance.encode("ALICE").equals(metaphoneInstance.encode("AARON")));
	}

	private boolean assertFalse(boolean condition) {
		if (condition) {
			fail();
		}
		return condition;
	}

	@Test
	public void testMaxCodeLen() {
		assertEquals(4, metaphoneInstance.getMaxCodeLen());
		metaphoneInstance.setMaxCodeLen(2);
		assertEquals(2, metaphoneInstance.getMaxCodeLen());
		metaphoneInstance.setMaxCodeLen(10);
		assertEquals(10, metaphoneInstance.getMaxCodeLen());
	}

}