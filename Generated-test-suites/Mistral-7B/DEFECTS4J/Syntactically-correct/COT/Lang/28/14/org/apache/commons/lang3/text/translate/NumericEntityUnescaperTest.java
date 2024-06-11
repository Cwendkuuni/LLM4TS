package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Random;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test case for NumericEntityUnescaper class
 */
public class NumericEntityUnescaperTest {

	private static final String LF = System.lineSeparator();
	private static final Random RANDOM = new Random();

	/**
	 * Test translate method with valid numeric entity
	 *
	 * @throws IOException
	 */
	@Test
	public void testTranslateValidNumericEntity() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&#x1234;Hello &#13;World" + LF;
		int index = 0;

		int length = unescaper.translate(input, index, out);
		String result = out.toString();

		assertEquals("Enter codepoint 1234, 13 and 10 here to see the result: " + LF + result, CharSequenceUtils.subsequence(input, 0, index) + "\u0012\u0034\u0048\u0065\u006c\u006c\u006f\u0020\u0068\u0065\u006c\u006c\u006f\u0020\u0057\u006f\u0072\u006c\u0064\u0020\u0068\u0065\u006c\u006c\u006f", CharSequenceUtils.subsequence(input, 0, index + length));
	}

	/**
	 * Test translate method with invalid numeric entity
	 *
	 * @throws IOException
	 */
	@Test
	public void testTranslateInvalidNumericEntity() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&invalidEntity;Hello &#x1234;World" + LF;
		int index = 0;

		int length = unescaper.translate(input, index, out);
		String result = out.toString();

		assertEquals("Hello &#x1234;World" + LF, CharSequenceUtils.subsequence(input, 0, index + length), result);
	}

	/**
	 * Test translate method with empty input
	 *
	 * @throws IOException
	 */
	@Test
	public void testTranslateEmptyInput() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "";
		int index = 0;

		int length = unescaper.translate(input, index, out);
		String result = out.toString();

		assertEquals("", result);
	}

	/**
	 * Test translate method with null input
	 *
	 * @throws IOException
	 */
	@Test(expected = NullPointerException.class)
	public void testTranslateNullInput() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = null;
		int index = 0;

		unescaper.translate(input, index, out);
	}

	/**
	 * Test translate method with invalid index
	 *
	 * @throws IOException
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testTranslateInvalidIndex() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&#x1234;";
		int index = input.length();

		unescaper.translate(input, index, out);
	}

	/**
	 * Test translate method with null writer
	 *
	 * @throws IOException
	 */
	@Test(expected = NullPointerException.class)
	public void testTranslateNullWriter() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = "&#x1234;";
		int index = 0;
		Writer out = null;

		unescaper.translate(input, index, out);
	}

	/**
	 * Test translate method with exception for hexadecimal entity that is too long
	 *
	 * @throws IOException
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateExceptionHexadecimalEntityTooLong() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&&#x1234567890ABCDEF1234567890ABCDEF1234567890ABCDEF13;";
		int index = 0;

		unescaper.translate(input, index, out);
	}

	/**
	 * Test translate method with exception for decimal entity that is too long
	 *
	 * @throws IOException
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateExceptionDecimalEntityTooLong() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&&#123456789012345678901234567890123456789013;";
		int index = 0;

		unescaper.translate(input, index, out);
	}

	/**
	 * Test translate method with exception for hexadecimal entity that contains invalid character
	 *
	 * @throws IOException
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateExceptionHexadecimalEntityInvalidCharacter() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&&#x1234AB;";
		int index = 0;

		unescaper.translate(input, index, out);
	}

	/**
	 * Test translate method with exception for decimal entity that contains non-numeric character
	 *
	 * @throws IOException
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateExceptionDecimalEntityNonNumericCharacter() throws IOException {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		StringWriter out = new StringWriter();
		CharSequence input = "&&#123ab;";
		int index = 0;

		unescaper.translate(input, index, out);
	}
}