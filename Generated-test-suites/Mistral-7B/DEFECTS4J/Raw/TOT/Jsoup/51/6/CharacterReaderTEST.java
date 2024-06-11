package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest class for testing the CharacterReader class.
 */
public class CharacterReaderTest {

 private static final String TEST_INPUT = "This is a test <html>\n" +
         "<head>\n" +
         "<title>Test Title</title>\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>This is a test paragraph.</p>\n" +
         "</body>\n" +
         "</html>";

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderWithValidInput() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);

 // Test basic functionality
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('T', reader.current());
 Assert.assertEquals('T', reader.consume());

 // Test mark and rewind
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());

 // Test nextIndexOf and consumeAsString
 Assert.assertEquals(4, reader.nextIndexOf(' '));
 String string = reader.consumeAsString();
 Assert.assertEquals("is", string);

 // Test consumeTo and consumeToEnd
 string = reader.consumeTo(' ');
 Assert.assertEquals("is a test ", string);
 string = reader.consumeToEnd();
 Assert.assertEquals("<html>\n" +
         "<head>\n" +
         "<title>Test Title</title>\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>This is a test paragraph.</p>\n" +
         "</body>\n" +
         "</html>", string);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

}