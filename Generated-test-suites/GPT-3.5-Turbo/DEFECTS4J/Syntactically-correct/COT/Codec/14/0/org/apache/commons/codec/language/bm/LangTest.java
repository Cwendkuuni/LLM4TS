import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Test basic functionality
        String text1 = "hello";
        String expected1 = "en";
        assertEquals(expected1, lang.guessLanguage(text1));

        String text2 = "hola";
        String expected2 = "es";
        assertEquals(expected2, lang.guessLanguage(text2));

        // Test edge cases and exception handling
        String text3 = "";
        String expected3 = "und";
        assertEquals(expected3, lang.guessLanguage(text3));

        String text4 = "@#$%^&*()";
        String expected4 = "und";
        assertEquals(expected4, lang.guessLanguage(text4));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Test basic functionality
        String text1 = "hello";
        Languages.LanguageSet expected1 = Languages.getInstance(NameType.GENERIC).getLanguageSet("en");
        assertEquals(expected1, lang.guessLanguages(text1));

        String text2 = "hola";
        Languages.LanguageSet expected2 = Languages.getInstance(NameType.GENERIC).getLanguageSet("es");
        assertEquals(expected2, lang.guessLanguages(text2));

        // Test edge cases and exception handling
        String text3 = "";
        Languages.LanguageSet expected3 = Languages.ANY_LANGUAGE;
        assertEquals(expected3, lang.guessLanguages(text3));

        String text4 = "@#$%^&*()";
        Languages.LanguageSet expected4 = Languages.ANY_LANGUAGE;
        assertEquals(expected4, lang.guessLanguages(text4));
    }
}