package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testEmptyReader() {
 reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleConsume() {
 reader = new CharacterReader("abc");
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 reader.consume();
 Assert.assertEquals('c', reader.current());
 reader.consume(); // EOF
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsumeString() {
 reader = new CharacterReader("abcdabcdabcd");
 String str = reader.consumeAsString();
 Assert.assertEquals("abcd", str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInput() {
 reader = new CharacterReader(""); // empty reader for assertions
 reader.consume();
 reader.consume();
 try {
 reader.consume(); // EOF
 } catch (Exception e) {
 // expected EOF exception
 }
 reader.consume(); // should throw an exception
 }

 @Test
 public void testConsumeToCharacter() {
 reader = new CharacterReader("abcdabcdabcd");
 char c = 'd';
 String str = reader.consumeTo(c);
 Assert.assertEquals("abcd", str);
 Assert.assertEquals(c, reader.current());
 }

 @Test
 public void testConsumeToString() {
 reader = new CharacterReader("abcdabcdabcd");
 String str = reader.consumeTo("cd");
 Assert.assertEquals("ab", str);
 Assert.assertEquals('c', reader.current());
 }

 @Test
 public void testConsumeToAnySingleCharacter() {
 reader = new CharacterReader("1234567890");
 char c = '5';
 String str = reader.consumeToAny(c);
 Assert.assertEquals("", str);
 Assert.assertEquals('5', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 reader = new CharacterReader("abcdabcdabcd");
 String str = reader.consumeToEnd();
 Assert.assertEquals("abcdabcdabcd", str);
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeLetterSequenceWithLetters() {
 reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 String str = reader.consumeLetterSequence();
 Assert.assertFalse(str.isEmpty());
 Assert.assertTrue(str.matches("^[a-zA-Z]+"));
 }

 @Test
 public void testConsumeHexSequence() {
 reader = new CharacterReader("0123456789ABCDEFabcdefABCDEF");
 String str = reader.consumeHexSequence();
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeHexSequenceWithHex() {
 reader = new CharacterReader("0123456789ABCDEFabcdefABCDEF");
 String str = reader.consumeHexSequence();
 Assert.assertFalse(str.isEmpty());
 Assert.assertTrue(str.matches("^[0-9A-Fa-f]+"));
 }

 @Test
 public void testMatches() {
 reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
 Assert.assertTrue(reader.matchesIgnoreCase("AbC"));
 Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testMatchesAny() {
 reader = new CharacterReader("0123456789ABCDEFabcdefABCDEF");
 Assert.assertTrue(reader.matchesAny('0', '1', '2'));
 Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
 }

 @Test
 public void testMatchesLetter() {
 reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 reader = new CharacterReader("0123456789ABCDEFabcdefABCDEF");
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchConsume() {
 reader = new CharacterReader("abcabc");
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("abc", str);
 Assert.assertTrue(reader.matchConsume("abc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader = new CharacterReader("ABCABC");
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("ABC", str);
 Assert.assertTrue(reader.matchConsumeIgnoreCase("AbC"));
 }

 @Test
 public void testContainsIgnoreCase() {
 reader = new CharacterReader("abcd<title>The Test</title>efgh");
 Assert.assertTrue(reader.containsIgnoreCase("<title>"));
 Assert.assertTrue(reader.containsIgnoreCase("THE TEST"));
 Assert.assertFalse(reader.containsIgnoreCase("<title>Test"));
 }
}