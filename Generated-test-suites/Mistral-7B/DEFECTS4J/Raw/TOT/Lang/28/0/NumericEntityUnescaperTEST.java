package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateXHexEntity() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;&#x61;abc&lt;/entity&gt;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);

 assertEquals("&xml<entity>&#xA1;abc</entity>", input.subSequence(0, 22).toString());
 assertEquals("&xml<entity>&#x61;abc</entity>", input.subSequence(0, 22).toString());
 assertEquals("xml<entity>\u00A1abc</entity>", out.getBuffer().toString().substring(0, length));
 }

 @Test
 public void testTranslateXDecEntity() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;&#x41;abc&lt;/entity&gt;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);

 assertEquals("&xml<entity>&#x41;abc</entity>", input.subSequence(0, 22).toString());
 assertEquals("&xml<entity>&#x0041;abc</entity>", input.subSequence(0, 22).toString());
 assertEquals("xml<entity>\u0041abc</entity>", out.getBuffer().toString().substring(0, length));
 }

 @Test
 public void testTranslateInvalidHexEntity() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;&#xabc;abc&lt;/entity&gt;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);

 assertEquals("&xml<entity>&#xabc;abc</entity>", input.subSequence(0, 19).toString());
 assertEquals("xml<entity>&#xabc;abc</entity>", out.getBuffer().toString().substring(0, length));
 }

 @Test
 public void testTranslateInvalidDecEntity() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;&#abc;abc&lt;/entity&gt;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);

 assertEquals("&xml<entity>&#abc;abc</entity>", input.subSequence(0, 19).toString());
 assertEquals("xml<entity>&#abc;abc</entity>", out.getBuffer().toString().substring(0, length));
 }

 @Test(expected = IOException.class)
 public void testTranslateMissingSemicolon() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;&#x61";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, out);
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidCharacterAfterEntity() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;&#x61;x";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateEmptyString() throws IOException {
 CharSequence input = "";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("", out.getBuffer().toString().substring(0, length));
 }

 @Test
 public void testTranslateNoEntity() throws IOException {
 CharSequence input = "&xml&lt;entity&gt;abc&lt;/entity&gt;";
 Writer out = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("&xml<entity>abc</entity>", input.subSequence(0, input.length()).toString());
 assertEquals("xml<entity>abc</entity>", out.getBuffer().toString().substring(0, length));
 }
}