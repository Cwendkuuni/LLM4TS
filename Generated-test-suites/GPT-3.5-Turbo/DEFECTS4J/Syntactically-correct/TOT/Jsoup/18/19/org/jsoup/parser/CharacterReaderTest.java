import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World!");
    }
    
    @Test
    public void testPos() {
        assertThat(characterReader.pos(), is(0));
    }
    
    @Test
    public void testIsEmpty() {
        assertThat(characterReader.isEmpty(), is(false));
    }
    
    @Test
    public void testCurrent() {
        assertThat(characterReader.current(), is('H'));
    }
    
    @Test
    public void testConsume() {
        assertThat(characterReader.consume(), is('H'));
        assertThat(characterReader.pos(), is(1));
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertThat(characterReader.pos(), is(0));
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertThat(characterReader.pos(), is(1));
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        assertThat(characterReader.pos(), is(0));
        characterReader.consume();
        characterReader.advance();
        assertThat(characterReader.pos(), is(2));
        characterReader.rewindToMark();
        assertThat(characterReader.pos(), is(0));
    }
    
    @Test
    public void testConsumeAsString() {
        assertThat(characterReader.consumeAsString(), is("H"));
        assertThat(characterReader.pos(), is(1));
    }
    
    @Test
    public void testConsumeTo() {
        assertThat(characterReader.consumeTo('o'), is("Hell"));
        assertThat(characterReader.pos(), is(4));
    }
    
    @Test
    public void testConsumeTo_withNonexistentChar() {
        assertThat(characterReader.consumeTo('z'), is("Hello World!"));
        assertThat(characterReader.pos(), is(12));
    }
    
    @Test
    public void testConsumeTo_withString() {
        assertThat(characterReader.consumeTo("lo"), is("Hel"));
        assertThat(characterReader.pos(), is(4));
    }
    
    @Test
    public void testConsumeTo_withNonexistentString() {
        assertThat(characterReader.consumeTo("abc"), is("Hello World!"));
        assertThat(characterReader.pos(), is(12));
    }
    
    @Test
    public void testConsumeToAny() {
        assertThat(characterReader.consumeToAny('o', 'W'), is("Hell"));
        assertThat(characterReader.pos(), is(4));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertThat(characterReader.consumeToEnd(), is("Hello World!"));
        assertThat(characterReader.pos(), is(12));
    }
    
    @Test
    public void testConsumeLetterSequence() {
        characterReader = new CharacterReader("Hello123");
        assertThat(characterReader.consumeLetterSequence(), is("Hello"));
        assertThat(characterReader.pos(), is(5));
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("ABC123");
        assertThat(characterReader.consumeHexSequence(), is("ABC"));
        assertThat(characterReader.pos(), is(3));
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("123456");
        assertThat(characterReader.consumeDigitSequence(), is("123456"));
        assertThat(characterReader.pos(), is(6));
    }
    
    @Test
    public void testMatches() {
        assertThat(characterReader.matches('H'), is(true));
    }
    
    @Test
    public void testMatches_withWrongChar() {
        assertThat(characterReader.matches('W'), is(false));
    }
    
    @Test
    public void testMatches_withString() {
        assertThat(characterReader.matches("Hello"), is(true));
    }
    
    @Test
    public void testMatches_withWrongString() {
        assertThat(characterReader.matches("World"), is(false));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertThat(characterReader.matchesIgnoreCase("hello"), is(true));
    }
    
    @Test
    public void testMatchesIgnoreCase_withWrongString() {
        assertThat(characterReader.matchesIgnoreCase("world"), is(false));
    }
    
    @Test
    public void testMatchesAny() {
        assertThat(characterReader.matchesAny('H', 'W'), is(true));
    }
    
    @Test
    public void testMatchesAny_withWrongChar() {
        assertThat(characterReader.matchesAny('A', 'B'), is(false));
    }
    
    @Test
    public void testMatchesLetter() {
        assertThat(characterReader.matchesLetter(), is(true));
    }
    
    @Test
    public void testMatchesLetter_withWrongChar() {
        characterReader = new CharacterReader("123");
        assertThat(characterReader.matchesLetter(), is(false));
    }
    
    @Test
    public void testMatchesDigit() {
        characterReader = new CharacterReader("123");
        assertThat(characterReader.matchesDigit(), is(true));
    }
    
    @Test
    public void testMatchesDigit_withWrongChar() {
        assertThat(characterReader.matchesDigit(), is(false));
    }
    
    @Test
    public void testMatchConsume() {
        assertThat(characterReader.matchConsume("Hello"), is(true));
        assertThat(characterReader.pos(), is(5));
    }
    
    @Test
    public void testMatchConsume_withWrongString() {
        assertThat(characterReader.matchConsume("World"), is(false));
        assertThat(characterReader.pos(), is(0));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertThat(characterReader.matchConsumeIgnoreCase("hello"), is(true));
        assertThat(characterReader.pos(), is(5));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase_withWrongString() {
        assertThat(characterReader.matchConsumeIgnoreCase("world"), is(false));
        assertThat(characterReader.pos(), is(0));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertThat(characterReader.containsIgnoreCase("o W"), is(true));
    }
    
    @Test
    public void testContainsIgnoreCase_withWrongString() {
        assertThat(characterReader.containsIgnoreCase("abc"), is(false));
    }
    
    @Test
    public void testToString() {
        assertThat(characterReader.toString(), is("Hello World!"));
    }
}