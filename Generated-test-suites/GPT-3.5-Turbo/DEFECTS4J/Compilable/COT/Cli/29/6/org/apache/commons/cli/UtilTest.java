package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testStripLeadingHyphens() {
		// Test when str starts with "--"
		String str1 = "--abc";
		String expected1 = "abc";
		String result1 = Util.stripLeadingHyphens(str1);
		assertEquals(expected1, result1);
		
		// Test when str starts with "-"
		String str2 = "-def";
		String expected2 = "def";
		String result2 = Util.stripLeadingHyphens(str2);
		assertEquals(expected2, result2);
		
		// Test when str starts with neither "--" nor "-"
		String str3 = "ghij";
		String expected3 = "ghij";
		String result3 = Util.stripLeadingHyphens(str3);
		assertEquals(expected3, result3);
		
		// Test when str is null
		String str4 = null;
		String expected4 = null;
		String result4 = Util.stripLeadingHyphens(str4);
		assertEquals(expected4, result4);
	}

	@Test
	public void testStripLeadingAndTrailingQuotes() {
		String str1 = "\"one two\"";
		String expected1 = "one two";
		String result1 = Util.stripLeadingAndTrailingQuotes(str1);
		assertEquals(expected1, result1);
		
		String str2 = "three";
		String expected2 = "three";
		String result2 = Util.stripLeadingAndTrailingQuotes(str2);
		assertEquals(expected2, result2);
		
		String str3 = "\"four\"";
		String expected3 = "four";
		String result3 = Util.stripLeadingAndTrailingQuotes(str3);
		assertEquals(expected3, result3);
	}

}
