package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * JUnit 4 test file for NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

 private static final String TEST_CASE_1 = "&amp; &lt; &gt; &apos; &quot; &#x27; &#xA0; &#160; &#x3C3; &#x26; &#xFFFD;";
 private static final String TEST_CASE_2 = "&amp;#123;&#x41F; &#x2212; &#x2213; &#x2211; &#x2248; &#x2249; &#x2260; &#x2261; &#x2245; &#x2247; &#x2242;";

 @Test
 public void testTranslate_Basic() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = "&amp; &lt; &gt; &apos; &quot; &#x27; &#xA0; &#160; &#x3C3; &#x26; &#xFFFD;";
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 int charsWritten = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);

 assertEquals("Unexpected number of characters written", 1, charsWritten);
 }

 assertEquals("Unexpected output",
 "&amp; &lt; &gt; &apos; &quot; &#x27; &nbsp; &nbsp; Γέρας &% &&#xFFFD;",
 outWriter.toString());
 }

 @Test(expected = IOException.class)
 public void testTranslate_IOException() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = "&amp;";
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);
 }
 }

 @Test
 public void testTranslate_InvalidEntityStart() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = "&a" + "&amp;";
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 int charsWritten = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);

 assertEquals("Unexpected number of characters written", 1, charsWritten);
 }

 assertEquals("Unexpected output", "&a&amp;", outWriter.toString());
 }

 @Test
 public void testTranslate_InvalidEntityEnd() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = "&amp;&";
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 int charsWritten = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);

 assertEquals("Unexpected number of characters written", 0, charsWritten);
 }

 assertEquals("Unexpected output", "&amp;&", outWriter.toString());
 }

 @Test
 public void testTranslate_InvalidHexEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = "&amp;#xz";
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 int charsWritten = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);

 assertEquals("Unexpected number of characters written", 0, charsWritten);
 }

 assertEquals("Unexpected output", "&amp;#xz", outWriter.toString());
 }

 @Test
 public void testTranslate_InvalidDecimalEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = "&amp;#abc";
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 int charsWritten = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);

 assertEquals("Unexpected number of characters written", 0, charsWritten);
 }

 assertEquals("Unexpected output", "&amp;#abc", outWriter.toString());
 }

 @Test
 public void testTranslate_ValidEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer outWriter = new StringBuilder();
 String testInput = TEST_CASE_1 + TEST_CASE_2;
 int index = 0;
 int length = testInput.length();

 for (; index < length; index++) {
 char c = testInput.charAt(index);
 int charsWritten = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return length;
 }

 @Override
 public char charAt(int index) {
 return c;
 }
 }, index, outWriter);

 assertEquals("Unexpected number of characters written", 1, charsWritten);
 }

 String expectedOutput = CharSequenceUtils.subsequence(TEST_CASE_1, 0, TEST_CASE_1.length())
 + CharSequenceUtils.subsequence(TEST_CASE_2, 0, TEST_CASE_2.length());

 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 }
}