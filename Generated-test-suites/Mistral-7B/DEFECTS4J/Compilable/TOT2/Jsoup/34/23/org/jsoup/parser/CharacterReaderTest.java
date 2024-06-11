package org.jsoup.parser;
import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test case for constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", characterReader.toString());
 }

 // Test case for method pos()
 @Test
 public void testPos() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.advance();
 characterReader.advance();
 Assert.assertEquals(2, characterReader.pos());
 }

 // Test case for method isEmpty()
 @Test
 public void testIsEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 Assert.assertTrue(characterReader.isEmpty());
 }

 // Test case for method current()
 @Test
 public void testCurrent() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals('H', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('H', characterReader.current());
 }

 // Test case for method consume()
 @Test
 public void testConsume() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.consume();
 Assert.assertEquals('H', characterReader.current());
 }

 // Test case for method unconsume()
 @Test
 public void testUnconsume() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('H', characterReader.current());
 }

 // Test case for method advance()
 @Test
 public void testAdvance() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.advance();
 Assert.assertEquals('H', characterReader.current());
 }

 // Test case for method mark()
 @Test
 public void testMark() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('H', characterReader.current());
 }

 // Test case for method consumeAsString()
 @Test
 public void testConsumeAsString() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello", characterReader.consumeAsString());
 }

 // Test case for method nextIndexOf() with char
 @Test
 public void testNextIndexOfChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals(4, characterReader.nextIndexOf('W'));
 }

 // Test case for method nextIndexOf() with CharSequence
 @Test
 public void testNextIndexOfCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals(7, characterReader.nextIndexOf("llo"));
 }

 // Test case for method consumeTo() with char
 @Test
 public void testConsumeToChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hell", characterReader.consumeTo('l'));
 }

 // Test case for method consumeTo() with CharSequence
 @Test
 public void testConsumeToCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hell", characterReader.consumeTo("ll"));
 }

 // Test case for method consumeToAny() with char array
 @Test
 public void testConsumeToAnyChars() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hell", characterReader.consumeToAny('l', 'o'));
 }

 // Test case for method consumeToEnd()
 @Test
 public void testConsumeToEnd() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("World", characterReader.consumeToEnd());
 }

 // Test case for method consumeLetterSequence()
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello", characterReader.consumeLetterSequence());
 }

 // Test case for method consumeLetterThenDigitSequence()
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader characterReader = new CharacterReader("Hello123World");
 Assert.assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
 }

 // Test case for method consumeHexSequence()
 @Test
 public void testConsumeHexSequence() {
 CharacterReader characterReader = new CharacterReader("0x1A2B3C");
 Assert.assertEquals("1A2B3C", characterReader.consumeHexSequence());
 }

 // Test case for method consumeDigitSequence()
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader characterReader = new CharacterReader("123456");
 Assert.assertEquals("123456", characterReader.consumeDigitSequence());
 }

 // Test case for method matches() with char
 @Test
 public void testMatchesChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matches('H'));
 }

 // Test case for method matches() with CharSequence
 @Test
 public void testMatchesSeq() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matches("ll"));
 }

 // Test case for method matchesIgnoreCase() with CharSequence
 @Test
 public void testMatchesIgnoreCaseSeq() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
 }

 // Test case for method matchesAny() with char array
 @Test
 public void testMatchesAnyChars() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesAny('o', 'l'));
 }

 // Test case for method matchesLetter()
 @Test
 public void testMatchesLetter() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesLetter());
 }

 // Test case for method matchesDigit()
 @Test
 public void testMatchesDigit() {
 CharacterReader characterReader = new CharacterReader("Hello123World");
 Assert.assertTrue(characterReader.matchesDigit());
 }

 // Test case for method matchConsume() with String
 @Test
 public void testMatchConsumeSeq() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchConsume("ll"));
 }

 // Test case for method matchConsumeIgnoreCase() with String
 @Test
 public void testMatchConsumeIgnoreCaseSeq() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchConsumeIgnoreCase("LL"));
 }

 // Test case for method containsIgnoreCase() with String
 @Test
 public void testContainsIgnoreCaseSeq() {
 CharacterReader characterReader = new CharacterReader("Hello <title>World</title>");
 Assert.assertTrue(characterReader.containsIgnoreCase("title"));
 }

 // Test case for exception handling when input is null
 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderConstructorNullInput() {
 new CharacterReader(null);
 }
}