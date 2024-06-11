package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntity() throws IOException {
 String input = "&#[x3C];&#[x61];&#[x27];&#[x22];&#[x23];&#[x26];&#[x27];&#[x22];&#[x25];&#[x25];";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 String expectedOutput = "&#x3C;&#xA;&#x61;&#xA;&#x27;&#xA;&#x22;&#xA;&#x23;&#xA;&#x26;&#xA;&#x27;&#xA;&#x22;&#xA;&#x25;&#xA;&#x25;";
 String actualOutput = outputWriter.toString();
 assertEquals(expectedOutput, actualOutput.substring(0, translatedLength));
 }

 @Test(expected = IOException.class)
 public void testTranslateArrayIndexOutOfBounds() throws IOException {
 String input = "&";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("Expected IOException due to ArrayIndexOutOfBoundsException");
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidNumericEntity() throws IOException {
 String input = "&notanum&";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 fail("Expected IOException due to NumberFormatException");
 }

 @Test
 public void testTranslateInvalidHexNumericEntity() throws IOException {
 String input = "&#[xz]";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnscaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 assertEquals(0, translatedLength);
 }

 @Test
 public void testTranslateEmptyString() throws IOException {
 String input = "";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 assertEquals(0, translatedLength);
 }

 @Test
 public void testTranslateSingleCharacter() throws IOException {
 String input = "&A;";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnscaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 assertEquals("A", outputWriter.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateInvalidCharacterAfterNumericEntity() throws IOException {
 String input = "&#[x61];abc";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnscaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 assertEquals("a", outputWriter.toString().substring(0, translatedLength - 1));
 }

 @Test
 public void testTranslateTrailingSemiColon() throws IOException {
 String input = "&#[x3C];";
 Writer outputWriter = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnscaper();
 int translatedLength = unescaper.translate(new StringReader(input), 0, outputWriter);
 String expectedOutput = StringUtils.repeat("\u0003", translatedLength - 1) + "\u0003";
 String actualOutput = outputWriter.toString();
 assertEquals(expectedOutput, actualOutput);
 }
}