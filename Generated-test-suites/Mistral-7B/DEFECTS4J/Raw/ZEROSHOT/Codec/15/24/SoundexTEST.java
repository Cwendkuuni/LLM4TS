package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SoundexTest extends TestCase {

	private static Soundex usEnglishSoundex = new Soundex();

	public SoundexTest(String name) {
		super(name);
	}

	/**
	 * Test method for {@link Soundex#Soundex()}.
	 */
	public void testDefaultConstructor() {
		assertEquals("01230120022455012623010202", new String(Soundex.US_ENGLISH_MAPPING));
		assertEquals("01230120022455012623010202", new String(usEnglishSoundex.getSoundexMapping()));
	}

	/**
	 * Test method for {@link Soundex#Soundex(char[])}.
	 */
	public void testCustomMappingConstructor() {
		char[] customMapping = {'0', '1', '2', '1', '2', '0', '2', '1', '0', '2', '3', '1', '0', '1', '1', '5'};
		Soundex customSoundex = new Soundex(customMapping);
		assertEquals("0123101023115", new String(customSoundex.getSoundexMapping()));
	}

	/**
	 * Test method for {@link Soundex#Soundex(String)}.
	 */
	public void testStringMappingConstructor() {
		Soundex soundexWithW = new Soundex("01230W012002245W0126W301020W");
		assertEquals("01230W012002245W0126W301020W", new String(soundexWithW.getSoundexMapping()));
	}

	/**
	 * Test method for {@link Soundex#encode(Object)}.
	 */
	public void testEncodeObject() throws EncoderException {
		assertEquals("K250", usEnglishSoundex.encode("Kathleen M. O'Sullivan"));
		assertEquals("K250", usEnglishSoundex.encode("Kathleen M. O'Sullivan"));
		assertNull(usEnglishSoundex.encode(null));
		assertNull(usEnglishSoundex.encode(""));
		assertNull(usEnglishSoundex.encode("12345"));
	}

	/**
	 * Test method for {@link Soundex#encode(String)}.
	 */
	public void testEncodeString() {
		assertEquals("K250", usEnglishSoundex.encode("Kathleen M. O'Sullivan"));
		assertEquals("K250", usEnglishSoundex.encode("Kathleen M. O'Sullivan"));
		assertNull(usEnglishSoundex.encode(null));
		assertNull(usEnglishSoundex.encode(""));
		assertNull(usEnglishSoundex.encode("12345"));
	}

	/**
	 * Test method for {@link Soundex#difference(StringEncoder, String, String)}.
	 */
	public void testDifference() throws EncoderException {
		Soundex soundexA = new Soundex();
		Soundex soundexB = new Soundex();
		assertEquals(4, soundexA.difference("Apple", "Apricot"));
		assertEquals(4, soundexA.difference("Apple", "Apricot"));
		assertEquals(0, soundexA.difference("Apple", "Apple"));
		assertEquals(4, soundexA.difference("Apple", "Apricot"));
		assertEquals(0, soundexA.difference(soundexA, "Apple", "Apple"));
		assertEquals(4, soundexA.difference(soundexA, "Apple", "Apricot"));
		assertNull(soundexA.difference(soundexA, null, "Apple"));
		assertNull(soundexA.difference(soundexA, "Apple", null));
		assertNull(soundexA.difference(soundexA, null, null));
	}

	/**
	 * Test method for {@link Soundex#soundex(String)}.
	 */
	public void testSoundex() {
		assertEquals("K250", usEnglishSoundex.soundex("Kathleen M. O'Sullivan"));
		assertEquals("K250", usEnglishSoundex.soundex("Kathleen M. O'Sullivan"));
		assertNull(usEnglishSoundex.soundex(null));
		assertNull(usEnglishSoundex.soundex(""));
		assertNull(usEnglishSoundex.soundex("12345"));
	}

	/**
	 * Test method for {@link SoundexUtils#clean(String)}.
	 */
	public void testClean() {
		assertEquals("Apple", SoundexUtils.clean("Apple"));
		assertEquals("Apricot", SoundexUtils.clean("Apricot"));
		assertEquals("Apple", SoundexUtils.clean("Apple"));
		assertEquals("Apricot", SoundexUtils.clean("Apricot"));
		assertEquals("Apple", SoundexUtils.clean("Apple"));
		assertEquals("Apricot", SoundexUtils.clean("Apricot"));
		assertNull(SoundexUtils.clean(null));
		assertNull(SoundexUtils.clean(""));
		assertNull(SoundexUtils.clean("12345"));
	}

	public static Test suite() {
		return new TestSuite(SoundexTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}