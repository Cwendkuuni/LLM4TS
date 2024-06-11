package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 *
 */
public class CharSequenceTranslatorTest {

	/**
	 * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
	 *
	 * @throws IOException
	 */
	@Test
	public void testTranslate() throws IOException {
		CharSequenceTranslator translator = new DefaultTranslator();
		StringInput input = new StringInput("Test String");
		StringWriter output = new StringWriter();
		int translatedLength = translator.translate(input, 0, output);
		assertEquals("Test String", output.toString());
		assertEquals(input.length(), translatedLength);
	}

	/**
	 * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
	 */
	@Test
	public void testTranslateToString() {
		CharSequenceTranslator translator = new DefaultTranslator();
		StringInput input = new StringInput("Test String");
		String output = translator.translate(input);
		assertEquals("Test String", output);
	}

	/**
	 * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTranslateWithNullWriter() throws IOException {
		CharSequenceTranslator translator = new DefaultTranslator();
		StringInput input = new StringInput("Test String");
		Writer output = null;
		translator.translate(input, output);
	}

	/**
	 * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null CharSequence
	 */
	@Test
	public void testTranslateWithNullCharSequence() {
		CharSequenceTranslator translator = new DefaultTranslator();
		CharSequence input = null;
		String output = translator.translate(input);
		assertEquals(null, output);
	}

	/**
	 * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}
	 */
	@Test
	public void testWith() {
		CharSequenceTranslator translator1 = new DefaultTranslator();
		CharSequenceTranslator translator2 = new DefaultTranslator();
		AggregateTranslator aggregateTranslator = translator1.with(translator2);
		assertEquals(3, aggregateTranslator.getClass().getDeclaredFields().length);
	}

	/**
	 * Test helper class for String input as CharSequence
	 */
	private static class StringInput implements CharSequence {

		private final String string;

		public StringInput(String string) {
			this.string = string;
		}

		@Override
		public int length() {
			return string.length();
		}

		@Override
		public char charAt(int index) {
			return string.charAt(index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return new StringInput(string.substring(start, end));
		}

		@Override
		public CharSequence asSubSequence(int start, int end) {
			return new StringInput(string.substring(start, end));
		}
	}
}