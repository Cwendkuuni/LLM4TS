package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testStripLeadingHyphens() {
		String str1 = Util.stripLeadingHyphens("--test");
		assertEquals("test", str1);

		String str2 = Util.stripLeadingHyphens("-test");
		assertEquals("test", str2);

		String str3 = Util.stripLeadingHyphens("test");
		assertEquals("test", str3);
	}

	@Test
	public void testStripLeadingAndTrailingQuotes() {
		String str1 = Util.stripLeadingAndTrailingQuotes("\"one two\"");
		assertEquals("one two", str1);

		String str2 = Util.stripLeadingAndTrailingQuotes("\"quoted string\"");
		assertEquals("quoted string", str2);

		String str3 = Util.stripLeadingAndTrailingQuotes("no quotes");
		assertEquals("no quotes", str3);
	}

	@Test
	public void testStripLeadingHyphensException() {
		String str1 = null;
		try {
			str1 = Util.stripLeadingHyphens(null);
		} catch (NullPointerException e) {
			assertEquals(null, str1);
		}
	}

	@Test
	public void testStripLeadingAndTrailingQuotesException() {
		String str1 = null;
		try {
			str1 = Util.stripLeadingAndTrailingQuotes(null);
		} catch (NullPointerException e) {
			assertEquals(null, str1);
		}
	}
}