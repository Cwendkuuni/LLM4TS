package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 // test basic methods on empty reader
 Assert.assertEquals(0, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 Assert.assertEquals(CharacterReader.EOF, reader.consume());
 }

 @Test
 public void testBasicConsumer() {
 CharacterReader reader = new CharacterReader("abcdef");
 // test basic consumption
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals(1, reader.pos());
 Assert.assertEquals('b', reader.current());
 // test consumption up to a specific character
 Assert.assertEquals("abc", reader.consumeTo('c'));
 Assert.assertEquals(3, reader.pos());
 // test consumption up to end of string
 Assert.assertEquals("def", reader.consumeToEnd());
 Assert.assertEquals("def", reader.toString());
 Assert.assertEquals(6, reader.pos());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 // test mark and rewind
 reader.mark();
 reader.advance(); // mark is at pos 1
 Assert.assertEquals('b', reader.current());
 reader.rewindToMark(); // pos is back at 0
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testScanForSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 // test scanning for a single character
 Assert.assertEquals(2, reader.nextIndexOf('c'));
 Assert.assertEquals('c', reader.consume());
 Assert.assertEquals(3, reader.pos());
 // test scanning for a character that is not present
 Assert.assertEquals(-1, reader.nextIndexOf('x'));
 }

 @Test
 public void testScanForSequence() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 // test scanning for a specific sequence
 Assert.assertEquals("abcd", reader.consumeTo("abcd"));
 Assert.assertEquals(5, reader.pos());
 // test scanning for a sequence that is not present
 Assert.assertEquals("", reader.consumeTo("xyz"));
 }

 @Test
 public void testScanForAnyCharacter() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEF");
 // test scanning for any character in a group
 Assert.assertEquals("345", reader.consumeToAny(new char[]{'3', '4', '5'}));
 // test scanning for any character in a sorted group
 Assert.assertEquals("56789ABCDEF", reader.consumeToAnySorted(new char[]{'1', '2', '3', '4', '5'}));
 }

 @Test
 public void testScanForData() {
 CharacterReader reader = new CharacterReader("&lt;xml version='1.0' encoding='UTF-8'?&gt;");
 // test scanning for data between &lt; and &gt;
 Assert.assertEquals("xml version='1.0' encoding='UTF-8'", reader.consumeData());
 }

 @Test
 public void testScanForTagName() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 // test scanning for tag names
 Assert.assertEquals("html", reader.consumeTagName());
 Assert.assertEquals("head", reader.consumeTagName());
 Assert.assertEquals("title", reader.consumeTagName());
 }

 @Test
 public void testCacheBehavior() {
 CharacterReader reader = new CharacterReader("abcdefabcdef");
 // test string caching behavior
 String cached = reader.consumeTo('f');
 Assert.assertEquals(6, reader.pos());
 Assert.assertEquals(cached, reader.consumeTo('f'));
 Assert.assertEquals(6, reader.pos());
 String cached2 = reader.consumeTo('f');
 // test that new strings are created on hash collisions
 Assert.assertNotSame(cached, cached2);
 Assert.assertEquals(6, reader.pos());
 }
}