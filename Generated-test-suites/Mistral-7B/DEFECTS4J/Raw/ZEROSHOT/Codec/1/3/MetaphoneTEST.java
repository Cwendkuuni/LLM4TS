package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

/**
 * JUnit test file for Metaphone class
 */
public class MetaphoneTest {

	@Test
	public void testMetaphoneInstance() {
		Metaphone metaphone = new Metaphone();
		assertNotNull(metaphone);
	}

	@Test
	public void testEmptyString() {
		Metaphone metaphone = new Metaphone();
		assertEquals("", metaphone.metaphone(""));
	}

	@Test
	public void testSingleCharacter() {
		Metaphone metaphone = new Metaphone();
		assertEquals("A", metaphone.metaphone("A"));
		assertEquals("B", metaphone.metaphone("B"));
		// test for lowercase character
		assertEquals("A", metaphone.metaphone("a"));
	}

	@Test(expected = EncoderException.class)
	public void testNullString() {
		Metaphone metaphone = new Metaphone();
		metaphone.metaphone(null); // should throw an exception
	}

	@Test
	public void testBasicMetaphone() {
		Metaphone metaphone = new Metaphone();
		assertEquals("A000", metaphone.metaphone("Apple"));
		assertEquals("B325", metaphone.metaphone("Banana"));
		assertEquals("C330", metaphone.metaphone("Cat"));
		assertEquals("A200", metaphone.metaphone("Auto"));
		assertEquals("C536", metaphone.metaphone("Cabbage"));
		assertEquals("E000", metaphone.metaphone("Elephant"));
		assertEquals("A630", metaphone.metaphone("Angle"));
		assertEquals("S536", metaphone.metaphone("Sting"));
		assertEquals("S350", metaphone.metaphone("String"));
		assertEquals("S370", metaphone.metaphone("Singing"));
		assertEquals("T220", metaphone.metaphone("Theater"));
		assertEquals("T240", metaphone.metaphone("Thesis"));
	}

	@Test
	public void testComplexMetaphone() {
		Metaphone metaphone = new Metaphone();
		assertEquals("C530", metaphone.metaphone("Academician"));
		assertEquals("C530", metaphone.metaphone("Administrator"));
		assertEquals("C536", metaphone.metaphone("Aneurysm"));
		assertEquals("C536", metaphone.metaphone("Cannibalism"));
		assertEquals("C330", metaphone.metaphone("Civilian"));
		assertEquals("X260", metaphone.metaphone("Encapsulation"));
		assertEquals("C370", metaphone.metaphone("Cryptography"));
		assertEquals("C330", metaphone.metaphone("Dictator"));
		assertEquals("S536", metaphone.metaphone("Exasperate"));
		assertEquals("S536", metaphone.metaphone("Expertise"));
		assertEquals("S536", metaphone.metaphone("Exaggerate"));
		assertEquals("C330", metaphone.metaphone("Gastronome"));
		assertEquals("C530", metaphone.metaphone("Hebrew"));
		assertEquals("S370", metaphone.metaphone("Inscrutable"));
		assertEquals("S370", metaphone.metaphone("Instigator"));
		assertEquals("S370", metaphone.metaphone("Investigator"));
		assertEquals("S370", metaphone.metaphone("Juxtapose"));
		assertEquals("S370", metaphone.metaphone("Perseverance"));
		assertEquals("S370", metaphone.metaphone("Persistence"));
		assertEquals("S370", metaphone.metaphone("Prescription"));
		assertEquals("S370", metaphone.metaphone("Procrastinate"));
		assertEquals("S370", metaphone.metaphone("Regenerate"));
		assertEquals("S370", metaphone.metaphone("Reinstantiate"));
		assertEquals("S370", metaphone.metaphone("Rejuvenate"));
		assertEquals("S370", metaphone.metaphone("Repertoire"));
		assertEquals("S370", metaphone.metaphone("Repudiate"));
		assertEquals("S370", metaphone.metaphone("Resilience"));
		assertEquals("S370", metaphone.metaphone("Restitution"));
		assertEquals("S370", metaphone.metaphone("Retrospect"));
		assertEquals("S370", metaphone.metaphone("Reverberate"));
		assertEquals("S370", metaphone.metaphone("Reverse"));
		assertEquals("S370", metaphone.metaphone("Transcend"));
		assertEquals("S370", metaphone.metaphone("Transient"));
		assertEquals("S370", metaphone.metaphone("Translate"));
		assertEquals("S370", metaphone.metaphone("Transmission"));
		assertEquals("S370", metaphone.metaphone("Transpire"));
		assertEquals("S370", metaphone.metaphone("Transverse"));
		assertEquals("S370", metaphone.metaphone("Trepidation"));
		assertEquals("S370", metaphone.metaphone("Vacillation"));
	}

	@Test
	public void testMaxCodeLen() {
		Metaphone metaphone = new Metaphone();
		metaphone.setMaxCodeLen(6);
		assertEquals("A00000", metaphone.metaphone("Apple"));
		assertEquals("B32525", metaphone.metaphone("Banana"));
		assertEquals("C33033", metaphone.metaphone("Cat"));
		assertEquals("A20020", metaphone.metaphone("Auto"));
		assertEquals("C53653", metaphone.metaphone("Cabbage"));
		assertEquals("E00000", metaphone.metaphone("Elephant"));
		assertEquals("A63063", metaphone.metaphone("Angle"));
		assertEquals("S53653", metaphone.metaphone("Sting"));
		assertEquals("S35035", metaphone.metaphone("String"));
		assertEquals("S37037", metaphone.metaphone("Singing"));
		assertEquals("T22022", metaphone.metaphone("Theater"));
		assertEquals("T24024", metaphone.metaphone("Thesis"));
	}

	@Test
	public void testIsMetaphoneEqual() {
		Metaphone metaphone = new Metaphone();
		assertEquals(true, metaphone.isMetaphoneEqual("Apple", "Apple"));
		assertEquals(true, metaphone.isMetaphoneEqual("Banana", "Banana"));
		assertEquals(true, metaphone.isMetaphoneEqual("Cat", "Cat"));
		assertEquals(true, metaphone.isMetaphoneEqual("Auto", "Auto"));
		assertEquals(true, metaphone.isMetaphoneEqual("Cabbage", "Cabbage"));
		assertEquals(true, metaphone.isMetaphoneEqual("Elephant", "Elephant"));
		assertEquals(true, metaphone.isMetaphoneEqual("Angle", "Angle"));
		assertEquals(true, metaphone.isMetaphoneEqual("Sting", "Sting"));
		assertEquals(true, metaphone.isMetaphoneEqual("String", "String"));
		assertEquals(true, metaphone.isMetaphoneEqual("Singing", "Singing"));
		assertEquals(true, metaphone.isMetaphoneEqual("Theater", "Theater"));
		assertEquals(true, metaphone.isMetaphoneEqual("Thesis", "Thesis"));
		assertEquals(false, metaphone.isMetaphoneEqual("Apple", "Banana"));
		assertEquals(false, metaphone.isMetaphoneEqual("Apple", "Cabbage"));
		assertEquals(false, metaphone.isMetaphoneEqual("Apple", "Theater"));
		assertEquals(false, metaphone.isMetaphoneEqual("Banana", "Cat"));
		assertEquals(false, metaphone.isMetaphoneEqual("Cabbage", "Apple"));
		assertEquals(false, metaphone.isMetaphoneEqual("Cabbage", "Theater"));
		assertEquals(false, metaphone.isMetaphoneEqual("Theater", "Apple"));
		assertEquals(false, metaphone.isMetaphoneEqual("Theater", "Cabbage"));
	}
}