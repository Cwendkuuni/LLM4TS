import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

private CharacterReader characterReader;

@Before
public void setUp() {
characterReader = new CharacterReader("This is a test string.");
}

@Test
public void testConstructor() {
Assert.assertNotNull(characterReader);
}

@Test
public void testPos() {
Assert.assertEquals(0, characterReader.pos());
}

@Test
public void testIsEmpty() {
Assert.assertFalse(characterReader.isEmpty());
}

@Test
public void testCurrent() {
Assert.assertEquals('T', characterReader.current());
characterReader.consume();
Assert.assertEquals('h', characterReader.current());
}

@Test
public void testConsume() {
Assert.assertEquals('T', characterReader.current());
characterReader.consume();
Assert.assertEquals('h', characterReader.current());
characterReader.consume();
Assert.assertEquals('i', characterReader.current());
}

@Test
public void testUnconsume() {
characterReader.consume();
characterReader.consume();
characterReader.unconsume();
Assert.assertEquals('h', characterReader.current());
}

@Test
public void testAdvance() {
characterReader.advance();
Assert.assertEquals('i', characterReader.current());
}

@Test
public void testMark() {
characterReader.mark();
Assert.assertEquals(2, characterReader.pos());
characterReader.consume();
Assert.assertEquals('i', characterReader.current());
characterReader.rewindToMark();
Assert.assertEquals('i', characterReader.current());
}

@Test
public void testConsumeAsString() {
Assert.assertEquals("is", characterReader.consumeAsString());
characterReader.consume();
characterReader.consume();
Assert.assertEquals("a", characterReader.consumeAsString());
}

@Test
public void testNextIndexOf() {
Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
Assert.assertEquals(1, characterReader.nextIndexOf('s'));
}

@Test
public void testNextIndexOfCharSequence() {
Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
Assert.assertEquals(2, characterReader.nextIndexOf("is"));
}

@Test(expected = IndexOutOfBoundsException.class)
public void testNextIndexOfOutOfBounds() {
characterReader.nextIndexOf('x', 10);
}

@Test
public void testConsumeTo() {
Assert.assertEquals("is", characterReader.consumeTo('s'));
Assert.assertEquals("test", characterReader.consumeTo("est"));
Assert.assertEquals("string.", characterReader.consumeTo("."));
}

@Test
public void testConsumeToEnd() {
Assert.assertEquals("string.", characterReader.consumeToEnd());
}

@Test
public void testConsumeLetterSequence() {
Assert.assertEquals("is", characterReader.consumeLetterSequence());
}

@Test
public void testConsumeLetterThenDigitSequence() {
Assert.assertEquals("is", characterReader.consumeLetterThenDigitSequence());
Assert.assertEquals("123", characterReader.consumeLetterThenDigitSequence());
}

@Test
public void testConsumeHexSequence() {
Assert.assertEquals("test", characterReader.consumeHexSequence());
}

@Test
public void testConsumeDigitSequence() {
Assert.assertEquals("123", characterReader.consumeDigitSequence());
}

@Test
public void testMatches() {
Assert.assertTrue(characterReader.matches('i'));
Assert.assertFalse(characterReader.matches('z'));
}

@Test
public void testMatchesString() {
Assert.assertTrue(characterReader.matches("is"));
Assert.assertFalse(characterReader.matches("xyz"));
}

@Test
public void testMatchesIgnoreCase() {
Assert.assertTrue(characterReader.matchesIgnoreCase("is"));
Assert.assertTrue(characterReader.matchesIgnoreCase("IS"));
Assert.assertFalse(characterReader.matchesIgnoreCase("xyz"));
}

@Test
public void testMatchesAny() {
Assert.assertTrue(characterReader.matchesAny('i', 's'));
Assert.assertTrue(characterReader.matchesAny('i', 'h'));
Assert.assertFalse(characterReader.matchesAny('z', 'i'));
}

@Test
public void testMatchesLetter() {
Assert.assertTrue(characterReader.matchesLetter());
Assert.assertFalse(characterReader.matchesLetter('5'));
}

@Test
public void testMatchesDigit() {
Assert.assertFalse(characterReader.matchesDigit());
Assert.assertTrue(characterReader.matchesDigit('3'));
}

@Test
public void testMatchConsume() {
Assert.assertEquals("is", characterReader.consumeTo('s'));
characterReader.matchConsume("is");
Assert.assertEquals("test", characterReader.consumeTo("est"));
characterReader.matchConsume("est");
Assert.assertEquals(".", characterReader.consumeToEnd());
characterReader.matchConsume(".");
Assert.assertEquals("", characterReader.consumeToEnd());
}

@Test
public void testMatchConsumeIgnoreCase() {
Assert.assertEquals("is", characterReader.consumeTo('s'));
characterReader.matchConsumeIgnoreCase("is");
Assert.assertEquals("test", characterReader.consumeTo("est"));
characterReader.matchConsumeIgnoreCase("EST");
Assert.assertEquals(".", characterReader.consumeToEnd());
characterReader.matchConsumeIgnoreCase(".");
Assert.assertEquals("", characterReader.consumeToEnd());
}

@Test
public void testContainsIgnoreCase() {
Assert.assertTrue(characterReader.containsIgnoreCase("is"));
Assert.assertTrue(characterReader.containsIgnoreCase("is"));
Assert.assertTrue(characterReader.containsIgnoreCase("EST"));
Assert.assertFalse(characterReader.containsIgnoreCase("xyz"));
}

@Test
public void testToString() {
Assert.assertEquals("string.", characterReader.toString());
}
}