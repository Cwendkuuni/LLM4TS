import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Test
 public void testCharacterReaderConstruction() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 Assert.assertEquals(5, characterReader.length());
 }

 @Test
 public void testCurrentCharacter() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeCharacter() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 characterReader.consume();
 Assert.assertEquals(EOF, characterReader.current());
 }

 @Test
 public void testIsEmpty() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.advance();
 characterReader.consume();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeyondLimit() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 characterReader.advance();
 characterReader.consume(); // consumes 's'
 characterReader.consume(); // consumes 't'
 characterReader.consume(); // consumes '\n'
 characterReader.consume(); // consumes EOF
 characterReader.consume(); // expected IllegalStateException
 }

 @Test
 public void testConsumeString() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 String expected = "est";
 characterReader.advance();
 characterReader.advance();
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals(expected, consumed);
 }

 @Test
 public void testConsumeToCharacter() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 't', 'a', 'b'};
 characterReader = new CharacterReader(new String(inputArray1));
 characterReader.advance();
 characterReader.advance();
 char c = 'a';
 String consumed = characterReader.consumeTo(c);
 Assert.assertEquals("t", consumed);
 characterReader = new CharacterReader(new String(inputArray2));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 consumed = characterReader.consumeTo(c);
 Assert.assertEquals("a", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToCharacterOutOfBounds() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 characterReader.advance();
 characterReader.consumeTo(EOF); // consume 's' and 't'
 characterReader.consumeTo(EOF); // consume '\n' and EOF
 characterReader.consumeTo(EOF); // expected IndexOutOfBoundsException
 }

 @Test
 public void testConsumeToString() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 't', 'a', 'b'};
 characterReader = new CharacterReader(new String(inputArray1));
 characterReader.advance();
 characterReader.advance();
 String consumed = characterReader.consumeTo("a");
 Assert.assertEquals("st", consumed);
 characterReader = new CharacterReader(new String(inputArray2));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 consumed = characterReader.consumeTo("a");
 Assert.assertEquals("a", consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToStringIllegalArgument() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 characterReader.advance();
 String emptySeq = "";
 String consumed = characterReader.consumeTo(emptySeq);
 // expected IllegalArgumentException
 }

 @Test
 public void testConsumeToAnyCharacter() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', 'b', 'c'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 't', 'a'};
 characterReader = new CharacterReader(new String(inputArray1));
 char c = 'b';
 String consumed = characterReader.consumeToAny(c);
 Assert.assertEquals("t", consumed);
 characterReader = new CharacterReader(new String(inputArray2));
 consumed = characterReader.consumeToAny(c);
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("tes\n", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', 'a', 'b', 'c'};
 characterReader = new CharacterReader(new String(inputArray));
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("est", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '0', 'x', '1', 'f', 'f', '.'};
 characterReader = new CharacterReader(new String(inputArray));
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("0x1f", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '0', '1', '2', '3', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("012", consumed);
 }

 @Test
 public void testMatchesCharacter() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.matches('s'));
 }

 @Test
 public void testMatchesString() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray1));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.matches("st"));
 characterReader = new CharacterReader(new String(inputArray2));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 Assert.assertFalse(characterReader.matches("st"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray1));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.matchesIgnoreCase("st"));
 characterReader = new CharacterReader(new String(inputArray2));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.matchesIgnoreCase("st"));
 }

 @Test
 public void testMatchesAnyCharacter() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', 'b', 'c'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 't', 'a'};
 characterReader = new CharacterReader(new String(inputArray1));
 char c = 'b';
 Assert.assertTrue(characterReader.matchesAny(c));
 characterReader = new CharacterReader(new String(inputArray2));
 Assert.assertFalse(characterReader.matchesAny(c));
 }

 @Test
 public void testMatchesLetter() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 char[] inputArray = new char[]{'t', 'e', 's', 't', '0', '\n'};
 characterReader = new CharacterReader(new String(inputArray));
 characterReader.advance();
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray1));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 String seq = "st";
 String consumed = characterReader.matchConsume(seq);
 Assert.assertEquals(seq, consumed);
 characterReader = new CharacterReader(new String(inputArray2));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 Assert.assertFalse(characterReader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 char[] inputArray1 = new char[]{'t', 'e', 's', 't', 'a', '\n'};
 char[] inputArray2 = new char[]{'t', 'e', 's', 's', 't', '\n'};
 characterReader = new CharacterReader(new String(inputArray1));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 String seq = "st";
 String consumed = characterReader.matchConsumeIgnoreCase(seq);
 Assert.assertEquals(seq, consumed);
 characterReader = new CharacterReader(new String(inputArray2));
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCaseString() {
 char[] inputArray1 = new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'i', 't', 'l', 'e', '>', '\n', 's', 't', 'y', 'l', 'e', 's', '\n'};
 characterReader = new CharacterReader(new String(inputArray1));
 String seq = "</title>";
 Assert.assertTrue(characterReader.containsIgnoreCase(seq));
 characterReader = new CharacterReader(new String(inputArray1));
 seq = "</style>";
 Assert.assertTrue(characterReader.containsIgnoreCase(seq));
 }

 @Override
 public String toString() {
 return super.toString();
 }
}