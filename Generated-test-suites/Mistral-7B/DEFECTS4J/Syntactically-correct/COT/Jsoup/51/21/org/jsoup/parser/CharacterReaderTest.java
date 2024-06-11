import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

private CharacterReader reader;

@Before
public void setUp() {
char input[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>".toCharArray();
reader = new CharacterReader(new String(input));
}

@Test
public void testConstructor() {
Assert.assertNotNull(reader);
}

@Test
public void testIsEmpty() {
Assert.assertFalse(reader.isEmpty());
}

@Test
public void testPos() {
Assert.assertEquals(0, reader.pos());
}

@Test
public void testCurrent() {
Assert.assertEquals('a', reader.current());
}

@Test
public void testConsume() {
Assert.assertEquals('a', reader.consume());
Assert.assertEquals(1, reader.pos());
}

@Test
public void testUnconsume() {
reader.unconsume();
Assert.assertEquals('a', reader.current());
Assert.assertEquals(0, reader.pos());
}

@Test
public void testAdvance() {
reader.advance();
Assert.assertEquals('b', reader.current());
Assert.assertEquals(1, reader.pos());
}

@Test
public void testMark() {
reader.mark();
Assert.assertEquals(1, reader.pos());
}

@Test
public void testRewindToMark() {
reader.rewindToMark();
Assert.assertEquals('a', reader.current());
Assert.assertEquals(0, reader.pos());
}

@Test
public void testConsumeAsString() {
String str = reader.consumeAsString();
Assert.assertEquals("a", str);
Assert.assertEquals(1, reader.pos());
}

@Test
public void testNextIndexOf() {
Assert.assertEquals(-1, reader.nextIndexOf('z'));
Assert.assertEquals(10, reader.nextIndexOf('j'));
}

@Test
public void testConsumeTo() {
String str = reader.consumeTo('n');
Assert.assertEquals("abcdef", str);
Assert.assertEquals(6, reader.pos());
}

@Test
public void testConsumeToEnd() {
String str = reader.consumeToEnd();
Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>", str);
Assert.assertEquals(33, reader.pos());
}

@Test
public void testConsumeToAny() {
String str = reader.consumeToAny('a', 'e');
Assert.assertEquals("abce", str);
Assert.assertEquals(4, reader.pos());
}

@Test
public void testConsumeToAnySorted() {
String str = reader.consumeToAnySorted('e', 'a');
Assert.assertEquals("ae", str);
Assert.assertEquals(2, reader.pos());
}

@Test
public void testConsumeData() {
String str = reader.consumeData();
Assert.assertEquals("&<", str);
Assert.assertEquals(3, reader.pos());
}

@Test
public void testConsumeTagName() {
String str = reader.consumeTagName();
Assert.assertEquals("/title>", str);
Assert.assertEquals(9, reader.pos());
}

@Test(expected = AssertionError.class)
public void testConsumeLetterSequenceError() {
String str = reader.consumeLetterSequence();
Assert.fail("Expected an error but it didn't occur");
}

@Test
public void testConsumeLetterThenDigitSequence() {
String str = reader.consumeLetterThenDigitSequence();
Assert.assertEquals("abc123", str);
Assert.assertEquals(7, reader.pos());
}

@Test
public void testMatches() {
Assert.assertTrue(reader.matches('e'));
Assert.assertFalse(reader.matches('x'));
}

@Test
public void testMatchesSequence() {
Assert.assertTrue(reader.matches("ab"));
Assert.assertFalse(reader.matches("cd"));
}

@Test
public void testMatchesIgnoreCase() {
Assert.assertTrue(reader.matchesIgnoreCase("AB"));
Assert.assertFalse(reader.matchesIgnoreCase("XY"));
}

@Test
public void testMatchesAny() {
Assert.assertTrue(reader.matchesAny('e'));
Assert.assertFalse(reader.matchesAny('x'));
}

@Test
public void testMatchesAnySorted() {
Assert.assertTrue(reader.matchesAnySorted('e'));
Assert.assertFalse(reader.matchesAnySorted('x'));
}

@Test
public void testMatchesLetter() {
Assert.assertTrue(reader.matchesLetter());
Assert.assertFalse(reader.matchesLetter());
}

@Test
public void testMatchesDigit() {
Assert.assertTrue(reader.matchesDigit());
Assert.assertFalse(reader.matchesDigit());
}

@Test
public void testMatchConsume() {
String str = reader.matchConsume("cd");
Assert.assertEquals("cd", str);
Assert.assertEquals(3, reader.pos());
}

@Test
public void testMatchConsumeIgnoreCase() {
String str = reader.matchConsumeIgnoreCase("AB");
Assert.assertEquals("AB", str);
Assert.assertEquals(3, reader.pos());
}

@Test
public void testContainsIgnoreCase() {
Assert.assertTrue(reader.containsIgnoreCase("/title"));
Assert.assertFalse(reader.containsIgnoreCase("/style"));
}

@Test
public void testToString() {
String str = reader.toString();
Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>", str);
}
}