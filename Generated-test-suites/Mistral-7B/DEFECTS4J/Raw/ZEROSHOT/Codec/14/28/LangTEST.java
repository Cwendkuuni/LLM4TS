package org.apache.commons.codec.language.bm;

import static org.apache.commons.codec.language.bm.Lang.Languages.ANY;
import static org.apache.commons.codec.language.bm.Lang.Languages.EN;
import static org.apache.commons.codec.language.bm.Lang.Languages.FR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang.LangRule.AcceptOnMatch;

public class LangTest {

    @org.junit.Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(Lang.NameType.DEFAULT);
        String englishWord = "hello";
        String frenchWord = "bonjour";
        String englishGuess = lang.guessLanguage(englishWord);
        String frenchGuess = lang.guessLanguage(frenchWord);
        assertEquals(EN, englishGuess);
        assertEquals(FR, frenchGuess);
    }

    @org.junit.Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(Lang.NameType.DEFAULT);
        String englishWord = "hello";
        String frenchWord = "bonjour";
        Set<String> englishLangs = lang.guessLanguages(englishWord).getLanguages();
        Set<String> frenchLangs = lang.guessLanguages(frenchWord).getLanguages();
        assertEquals(Arrays.asList(EN), englishLangs);
        assertNotEquals(EN, frenchLangs);
        assertEquals(Arrays.asList(FR), frenchLangs);
    }

    @org.junit.Test
    public void testGuessLanguagesWithMultipleMatches() {
        Lang lang = Lang.instance(Lang.NameType.DEFAULT);
        String word = "banana";
        Set<String> languages = lang.guessLanguages(word).getLanguages();
        assertEquals(Arrays.asList("ar", "de", "en", "es", "fr"), languages);
    }

    @org.junit.Test
    public void testGuessLanguagesWithNoMatch() {
        Lang lang = Lang.instance(Lang.NameType.DEFAULT);
        String word = "xxyz";
        Set<String> languages = lang.guessLanguages(word).getLanguages();
        assertEquals(Arrays.asList(Lang.Languages.ANY), languages);
    }

    @org.junit.Test
    public void testAcceptingRule() {
        Lang frenchLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_fr.txt", Lang.Languages.FRENCH);
        String frenchWord = "bonjour";
        Set<String> languages = frenchLang.guessLanguages(frenchWord).getLanguages();
        assertEquals(Arrays.asList(Lang.Languages.FRENCH), languages);
    }

    @org.junit.Test
    public void testRejectingRule() {
        Lang englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Lang.Languages.ENGLISH);
        String englishWord = "allo";
        Set<String> languages = englishLang.guessLanguages(englishWord).getLanguages();
        assertEquals(Arrays.asList(Lang.Languages.ANY), languages);
    }

    @org.junit.BeforeClass
    public static void setup() {
        // Initialize Languages instance only once for all tests
        Lang.Languages.setDefault(Lang.Languages.getInstance(Lang.NameType.DEFAULT));
        Lang.Languages.getDefault().addLanguage(Lang.Languages.FRENCH, new Lang.Languages.SimpleLanguageInfo("French", "fr"));
        Lang.Languages.getDefault().addLanguage(Lang.Languages.ENGLISH, new Lang.Languages.SimpleLanguageInfo("English", "en"));
    }
}