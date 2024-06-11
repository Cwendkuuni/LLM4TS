package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

/**
 * JUnit 4 test file for NumericEntityUnescaper class
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateSimpleNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test numeric entities with semicolon
 String input1 = "&amp;#160; &#161; &#162; &#x03A3; &#x20AC; &#xFF00; &#xFFFF;";
 String expectedOutput1 = "&amp; ā ā € artenewasigma € ⋅ ⋅";
 CharSequence inputCS1 = CharSequenceUtils.subsequence(input1, 1, input1.length() - 1);
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(inputCS1, 0, outputWriter);
 assertEquals(expectedOutput1.length(), length);

 // Test numeric entities without semicolon
 String input2 = "&amp;#160& #161& #162& #x03A3& #x20AC& #xFF00& #xFFFF";
 String expectedOutput2 = "&amp; ā ā € artenewasigma  € ⋅ ⋅";
 CharSequence inputCS2 = CharSequenceUtils.subsequence(input2, 1, input2.length() - 1);
 length = unescaper.translate(inputCS2, 0, outputWriter);
 assertEquals(expectedOutput2.length(), length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 String input = "&amp;#xzzz;";
 CharSequence inputCS = CharSequenceUtils.subsequence(input, 1, input.length() - 1);
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
}
 };

 unescaper.translate(inputCS, 0, outputWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNegativeEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 String input = "&amp;#-1;";
 CharSequence inputCS = CharSequenceUtils.subsequence(input, 1, input.length() - 1);
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
}
 };

 unescaper.translate(inputCS, 0, outputWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNonNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 String input = "&amp;#abcdef;";
 CharSequence inputCS = CharSequenceUtils.subsequence(input, 1, input.length() - 1);
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
}
 };

 unescaper.translate(inputCS, 0, outputWriter);
 }
}