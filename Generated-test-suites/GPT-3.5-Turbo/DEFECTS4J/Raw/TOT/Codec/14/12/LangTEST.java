
import org.junit.Test;

import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_WithValidInput_ShouldReturnExpectedLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String text1 = "hello";
        String expected1 = "en";
        assertEquals(expected1, lang.guessLanguage(text1));

        String text2 = "bonjour";
        String expected2 = "fr";
        assertEquals(expected2, lang.guessLanguage(text2));

        String text3 = "hola";
        String expected3 = "es";
        assertEquals(expected3, lang.guessLanguage(text3));
    }

    @Test
    public void testGuessLanguage_WithEmptyString_ShouldReturnAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String text = "";
        String expected = Languages.ANY;
        assertEquals(expected, lang.guessLanguage(text));
    }

    @Test
    public void testGuessLanguage_WithInvalidInput_ShouldReturnAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Invalid characters
        String text1 = "*&^%";
        String expected1 = Languages.ANY;
        assertEquals(expected1, lang.guessLanguage(text1));

        // Invalid word
        String text2 = "123";
        String expected2 = Languages.ANY;
        assertEquals(expected2, lang.guessLanguage(text2));
    }

    @Test
    public void testGuessLanguages_WithValidInput_ShouldReturnExpectedLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String text1 = "hello";
        String[] expected1 = {"en"};
        assertArrayEquals(expected1, lang.guessLanguages(text1).getLanguages().toArray());

        String text2 = "bonjour";
        String[] expected2 = {"fr"};
        assertArrayEquals(expected2, lang.guessLanguages(text2).getLanguages().toArray());

        String text3 = "hola";
        String[] expected3 = {"es"};
        assertArrayEquals(expected3, lang.guessLanguages(text3).getLanguages().toArray());
    }

    @Test
    public void testGuessLanguages_WithEmptyString_ShouldReturnAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String text = "";
        String[] expected = {Languages.ANY};
        assertArrayEquals(expected, lang.guessLanguages(text).getLanguages().toArray());
    }

    @Test
    public void testGuessLanguages_WithInvalidInput_ShouldReturnAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Invalid characters
        String text1 = "*&^%";
        String[] expected1 = {Languages.ANY};
        assertArrayEquals(expected1, lang.guessLanguages(text1).getLanguages().toArray());

        // Invalid word
        String text2 = "123";
        String[] expected2 = {Languages.ANY};
        assertArrayEquals(expected2, lang.guessLanguages(text2).getLanguages().toArray());
    }
}
