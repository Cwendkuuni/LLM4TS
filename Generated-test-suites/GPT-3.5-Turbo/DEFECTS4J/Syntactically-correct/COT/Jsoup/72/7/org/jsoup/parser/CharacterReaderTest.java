import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos() method
        CharacterReader reader = new CharacterReader("Test");
        assert reader.pos() == 0;
        reader.advance();
        assert reader.pos() == 1;
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty() method
        CharacterReader reader = new CharacterReader("Test");
        assert !reader.isEmpty();
        reader.consume();
        assert reader.isEmpty();
    }

    @Test
    public void testCurrent() {
        // Test the current() method
        CharacterReader reader = new CharacterReader("Test");
        assert reader.current() == 'T';
    }

    @Test
    public void testConsume() {
        // Test the consume() method
        CharacterReader reader = new CharacterReader("Test");
        assert reader.consume() == 'T';
        assert reader.current() == 'e';
    }

    @Test
    public void testUnconsume() {
        // Test the unconsume() method
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        assert reader.current() == 'e';
        reader.unconsume();
        assert reader.current() == 'T';
    }

    @Test
    public void testAdvance() {
        // Test the advance() method
        CharacterReader reader = new CharacterReader("Test");
        assert reader.current() == 'T';
        reader.advance();
        assert reader.current() == 'e';
    }

    // Add more test cases for the other methods in the CharacterReader class

}