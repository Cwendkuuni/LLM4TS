package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReader_emptyString() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(-1, reader.nextIndexOf('a'));
 }

 @Test
 public void testCharacterReader_singleCharacter() {
 CharacterReader reader = new CharacterReader("a");
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals(-1, reader.nextIndexOf('b'));
 }

 @Test
 public void testCharacterReader_multipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 Assert.assertEquals('b', reader.consume());
 Assert.assertEquals('c', reader.current());
 Assert.assertEquals(-1, reader.nextIndexOf('d'));
 }

 @Test
 public void testCharacterReader_consumeSubString() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeTo('c');
 Assert.assertEquals("ab", consumed);
 Assert.assertEquals('c', reader.current());
 }

 @Test
 public void testCharacterReader_consumeSubSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo("def");
 Assert.assertEquals("abc", consumed);
 Assert.assertEquals('d', reader.current());
 }

 @Test
 public void testCharacterReader_consumeAny() {
 CharacterReader reader = new CharacterReader("123abcd");
 String consumed = reader.consumeToAny('1', '2', '3');
 Assert.assertEquals("123", consumed);
 Assert.assertEquals('a', reader.current());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReader_nullInput() {
 new CharacterReader(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCharacterReader_consumePastEnd() {
 CharacterReader reader = new CharacterReader("abc");
 reader.advance();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals(-1, reader.nextIndexOf('a'));
 }
}