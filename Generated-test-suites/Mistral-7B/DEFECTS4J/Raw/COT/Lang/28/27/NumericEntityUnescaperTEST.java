package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

	private static final String NUMERIC_ENTITY = "&#[xAB]CD;";
	private static final String NUMERIC_ENTITY_DECIMAL = "&#123;";
	private static final String INVALID_NUMERIC_ENTITY = "&##;";
	private static final String INVALID_NUMERIC_ENTITY_HEX = "&x#;";
	private static final String INVALID_NUMERIC_ENTITY_MISSING_HASH = "&ABC;";
	private static final String INVALID_NUMERIC_ENTITY_MISSING_X = "&#xABC;";

	private Writer getWriter() {
		return new Writer() {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			@Override
			public void write(int c) throws IOException {
				os.write(c);
			}

			@Override
			public void flush() throws IOException {
				// no-op
			}

			@Override
			public void close() throws IOException {
				// no-op
			}

			String getOutput() {
				return os.toString();
			}
		};
	}

	/**
	 * Test translate method with hexadecimal numeric entity
	 */
	@Test
	public void testTranslateWithHexNumericEntity() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = CharSequenceUtils.subSequence(NUMERIC_ENTITY, 1, NUMERIC_ENTITY.length());
		Writer out = getWriter();
		int length = unescaper.translate(input, 0, out);
		assertEquals("Expected output: ABCD", out.getOutput().substring(0, length), "ABCD");
	}

	/**
	 * Test translate method with decimal numeric entity
	 */
	@Test
	public void testTranslateWithDecimalNumericEntity() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = CharSequenceUtils.subSequence(NUMERIC_ENTITY_DECIMAL, 1, NUMERIC_ENTITY_DECIMAL.length());
		Writer out = getWriter();
		int length = unescaper.translate(input, 0, out);
		assertEquals("Expected output: 123", out.getOutput().substring(0, length), "123");
	}

	/**
	 * Test translate method with invalid numeric entity
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateWithInvalidNumericEntity() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = CharSequenceUtils.subSequence(INVALID_NUMERIC_ENTITY, 1, INVALID_NUMERIC_ENTITY.length());
		Writer out = getWriter();
		unescaper.translate(input, 0, out);
	}

	/**
	 * Test translate method with invalid hexadecimal numeric entity
	 */
	@Test(expected = NumberFormatException.class)
	public void testTranslateWithInvalidHexNumericEntity() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = CharSequenceUtils.subSequence(INVALID_NUMERIC_ENTITY_HEX, 1, INVALID_NUMERIC_ENTITY_HEX.length());
		Writer out = getWriter();
		unescaper.translate(input, 0, out);
	}

	/**
	 * Test translate method with numeric entity missing a hash
	 */
	@Test(expected = IOException.class)
	public void testTranslateWithMissingHashNumericEntity() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = CharSequenceUtils.subSequence(INVALID_NUMERIC_ENTITY_MISSING_HASH, 0, INVALID_NUMERIC_ENTITY_MISSING_HASH.length());
		Writer out = getWriter();
		unescaper.translate(input, 0, out);
	}

	/**
	 * Test translate method with numeric entity missing 'x' for hexadecimal
	 */
	@Test(expected = IOException.class)
	public void testTranslateWithMissingXHexNumericEntity() {
		NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
		CharSequence input = CharSequenceUtils.subSequence(INVALID_NUMERIC_ENTITY_MISSING_X, 0, INVALID_NUMERIC_ENTITY_MISSING_X.length());
		Writer out = getWriter();
		unescaper.translate(input, 0, out);
	}
}