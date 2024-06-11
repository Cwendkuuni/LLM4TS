package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPosMethod() {
        String input = "abcde";
        CharacterReader reader = new CharacterReader(input);
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmptyMethod() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        boolean expected = true;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmptyMethodWithNonEmptyString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        boolean expected = false;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrentMethod() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        char expected = 'a';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrentMethodWithEmptyString() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        char expected = CharacterReader.EOF;
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeMethod() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        char expected = 'a';
        char actual = reader.consume();
        assertEquals(expected, actual);
        expected = 'b';
        actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeMethodWithEmptyString() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        char expected = CharacterReader.EOF;
        char actual = reader.consume();
        assertEquals(expected, actual);
    }
    
    // Similar test cases can be written for the remaining methods

}