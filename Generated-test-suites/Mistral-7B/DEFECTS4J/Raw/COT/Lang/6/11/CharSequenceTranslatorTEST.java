package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * JUnit test file for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

 /**
  * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
  */
 @Test
 public void testTranslate() {
 CharSequence input = "testCharSequence";
 DefaultTranslator translator = new DefaultTranslator();
 StringWriter outputWriter = new StringWriter();
 int consumed = translator.translate(input, 0, outputWriter);
 assertEquals(input.length(), consumed);
 assertEquals(input, outputWriter.toString());
 }

 /**
 * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
 */
 @Test
 public void testTranslateToString() {
 CharSequence input = "testCharSequence";
 DefaultTranslator translator = new DefaultTranslator();
 String output = translator.translate(input);
 assertEquals(input, output);
 }

 /**
 * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
 */
 @Test
 public void testTranslateToWriterWithNullWriter() {
 CharSequence input = "testCharSequence";
 DefaultTranslator translator = new DefaultTranslator();
 assertThrows(IllegalArgumentException.class, () -> translator.translate(input, null));
 }

 /**
 * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
 * with Writer producing IOException
 */
 @Test(expected = RuntimeException.class)
 public void testTranslateToWriterWithIOException() {
 CharSequence input = "testCharSequence";
 DefaultTranslator translator = new DefaultTranslator();
 StringWriter outputWriter = new StringWriter() {
 @Override
 public void write(int c, Writer writer) throws IOException {
 throw new IOException();
 }
 };
 translator.translate(input, outputWriter);
 }

 /**
 * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
 * with surrogate pair
 */
 @Test
 public void testTranslateWithSurrogatePair() {
 CharSequence input = "\uD840\uDC00\uD840\uDC01";
 DefaultTranslator translator = new DefaultTranslator();
 String output = translator.translate(input);
 assertEquals("\uD840\uDC00\uD840\uDC01", output);
 }

 /**
 * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}
 */
 @Test
 public void testWithTranslation() {
 CharSequenceTranslator translator1 = new DefaultTranslator();
 CharSequenceTranslator translator2 = new DefaultTranslator();
 CharSequenceTranslator translator3 = new DefaultTranslator();
 CharSequenceTranslator merged = translator1.with(translator2, translator3);
 AggregateTranslator expectedMerged = new AggregateTranslator(new CharSequenceTranslator[] { translator1, translator2, translator3 });
 assertEquals(merged.getClass(), expectedMerged.getClass());
 }

}