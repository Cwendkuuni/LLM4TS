package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.Languages;
import org.apache.commons.codec.language.bm.Lang.NameType;

/**
 * Test class for Lang.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#Lang(List, Languages)}.
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testLang() {
        final List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile("^[aeiou]"), new HashSet<>(Arrays.asList("english")), true),
                new LangRule(Pattern.compile("^[\\u0410-\\u044f]+"), new HashSet<>(Arrays.asList("russian")), false),
                new LangRule(Pattern.compile("^\\p{Han}"), new HashSet<>(Arrays.asList("chinese")), true));
        final Languages languages = Languages.getInstance(NameType.DEFAULT);
        final Lang lang = new Lang(rules, languages);

        assertEquals("expected Lang instance", lang, Lang.instance(NameType.DEFAULT));
        assertEquals("expected rules", rules, lang.rules);
        assertEquals("expected languages", languages, lang.languages);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final String englishWord = "hello";
        final String russianWord = "привет";
        final String chineseWord = "你好";

        final Lang lang = Lang.instance(NameType.DEFAULT);

        assertEquals("English word should be English", Languages.ENGLISH, lang.guessLanguage(englishWord));
        assertFalse("English word should not be Russian", lang.guessLanguage(englishWord).equals(Languages.RUSSIAN));
        assertFalse("English word should not be Chinese", lang.guessLanguage(englishWord).equals(Languages.CHINESE));

        assertEquals("Russian word should be Russian", Languages.RUSSIAN, lang.guessLanguage(russianWord));
        assertFalse("Russian word should not be English", lang.guessLanguage(russianWord).equals(Languages.ENGLISH));
        assertFalse("Russian word should not be Chinese", lang.guessLanguage(russianWord).equals(Languages.CHINESE));

        assertEquals("Chinese word should be Chinese", Languages.CHINESE, lang.guessLanguage(chineseWord));
        assertFalse("Chinese word should not be English", lang.guessLanguage(chineseWord).equals(Languages.ENGLISH));
        assertFalse("Chinese word should not be Russian", lang.guessLanguage(chineseWord).equals(Languages.RUSSIAN));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final String englishWord = "hello";
        final String russianWord = "привет";
        final String chineseWord = "你好";

        final Lang lang = Lang.instance(NameType.DEFAULT);

        final Set<String> expectedLanguages = new HashSet<>(Arrays.asList("english", "russian", "chinese"));
        final Set<String> guessedLanguages = lang.guessLanguages(englishWord);
        assertTrue("expected English to be in result", guessedLanguages.contains(Languages.ENGLISH.getName()));
        assertFalse("expected Russian to not be in result", guessedLanguages.contains(Languages.RUSSIAN.getName()));
        assertFalse("expected Chinese to not be in result", guessedLanguages.contains(Languages.CHINESE.getName()));

        expectedLanguages.clear();
        expectedLanguages.addAll(Arrays.asList("russian", "chinese", "english"));
        final Set<String> guessedLanguagesRussian = lang.guessLanguages(russianWord);
        assertTrue("expected Russian to be in result", guessedLanguagesRussian.contains(Languages.RUSSIAN.getName()));
        assertFalse("expected English to not be in result", guessedLanguagesRussian.contains(Languages.ENGLISH.getName()));
        assertFalse("expected Chinese to not be in result", guessedLanguagesRussian.contains(Languages.CHINESE.getName()));

        expectedLanguages.clear();
        expectedLanguages.addAll(Arrays.asList("chinese", "english", "russian"));
        final Set<String> guessedLanguagesChinese = lang.guessLanguages(chineseWord);
        assertTrue("expected Chinese to be in result", guessedLanguagesChinese.contains(Languages.CHINESE.getName()));
        assertFalse("expected English to not be in result", guessedLanguagesChinese.contains(Languages.ENGLISH.getName()));
        assertFalse("expected Russian to not be in result", guessedLanguagesChinese.contains(Languages.RUSSIAN.getName()));

        // check for empty result
        final Set<String> emptyResult = lang.guessLanguages("test");
        assertTrue("expected empty result to not contain any languages", emptyResult.isEmpty());

        // check for non-unique result
        final Set<String> nonUniqueResult = lang.guessLanguages("testtest");
        assertEquals("expected result to contain 2 elements", 2, nonUniqueResult.size());
        assertTrue("expected non-unique result to contain English", nonUniqueResult.contains(Languages.ENGLISH.getName()));
        assertTrue("expected non-unique result to contain some other language", nonUniqueResult.contains(Languages.RUSSIAN.getName()));
    }
}