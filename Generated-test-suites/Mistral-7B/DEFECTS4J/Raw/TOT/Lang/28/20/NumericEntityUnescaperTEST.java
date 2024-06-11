package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * NumericEntityUnescaperTest.java
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_DecimalEntity() throws IOException {
 String input = "&#10;abcd";
 Writer out = new StringBuilder();
 int length = new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 assertEquals("abcd\n", out.toString());
 assertEquals(5, length);
 }

 @Test
 public void testTranslate_HexEntity() throws IOException {
 String input = "&#[xCA];abcd";
 Writer out = new StringBuilder();
 int length = new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 assertEquals("abcd\u00CA", out.toString());
 assertEquals(6, length);
 }

 @Test
 public void testTranslate_InvalidDecimalEntity() throws IOException {
 String input = "&#xyz;abcd";
 Writer out = new StringBuilder();
 int length = new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 assertEquals("abcd", out.toString());
 assertEquals(4, length);
 }

 @Test
 public void testTranslate_InvalidHexEntity() throws IOException {
 String input = "&#[xABC];abcd";
 Writer out = new StringBuilder();
 int length = new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 assertEquals("abcd", out.toString());
 assertEquals(5, length);
 }

 @Test
 public void testTranslate_EmptyEntity() throws IOException {
 String input = "&#[x];abcd";
 Writer out = new StringBuilder();
 int length = new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 assertEquals("abcd", out.toString());
 assertEquals(4, length);
 }

 @Test
 public void testTranslate_EntityOutsideInput() throws IOException {
 String input = "abc&#[xyz];def";
 Writer out = new StringBuilder();
 int length = new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 assertEquals("abcdef", out.toString());
 assertEquals(6, length);
 }

 @Test(expected = IOException.class)
 public void testTranslate_ErrorInput() throws IOException {
 String input = "abcd&#[xyz";
 Writer out = new StringBuilder();
 new NumericEntityUnescaper().translate(new StringReader(input), 0, out);
 fail("Expected IOException");
 }
}