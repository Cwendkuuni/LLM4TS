package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @org.junit.Test
    public void testInstance() {
        NameType nameType = NameType.ENGLISH;
        Lang lang = Lang.instance(nameType);
        assertNotNull(lang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} and {@link Lang#guessLanguages(String)}
     */
    @org.junit.Test
    public void testGuessLanguageAndLanguages() {
        NameType nameType = NameType.ENGLISH;
        Lang lang = Lang.instance(nameType);

        // Test with valid input
        String text = "test";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "English"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(NameType.ENGLISH, lang.guessLanguage(text));

        // Test with empty input
        text = "";
        expectedLanguages = Collections.emptySet();
        actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(Languages.ANY, lang.guessLanguage(text));

        // Test with null input
        text = null;
        assertNull(lang.guessLanguage(text));
        assertFalse(lang.guessLanguages(text).isPresent());

        // Test with various inputs
        text = "Hello, World!";
        expectedLanguages = new HashSet<String>(Arrays.asList("en", "English", "cs", "Czech", "da", "Danish", "de",
                "German", "es", "Spanish", "fr", "French", "it", "Italian", "nl", "Dutch", "pl", "Polish",
                "pt", "Portuguese", "ru", "Russian", "sv", "Swedish", "tr", "Turkish"));
        actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(NameType.ANY, lang.guessLanguage(text));

        text = "Hola, Mundo!";
        expectedLanguages = new HashSet<String>(Arrays.asList("es", "Spanish"));
        actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(NameType.ANY, lang.guessLanguage(text));

        text = "Hello, Monde!";
        assertFalse(lang.guessLanguages(text).isPresent());
        assertEquals(Languages.ANY, lang.guessLanguage(text));

        text = "你好，世界!";
        expectedLanguages = new HashSet<String>(Arrays.asList("zh", "Chinese", "zh_CN", "Chinese_Simplified",
                "zh_TW", "Chinese_Traditional"));
        actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(NameType.ANY, lang.guessLanguage(text));
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)} constructor
     */
    @org.junit.Test
    public void testLangConstruction() {
        NameType nameType = NameType.ENGLISH;
        Languages languages = Languages.getInstance(nameType);

        List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("^[aeiou]+$"), new HashSet<String>(Arrays.asList("fr")), false),
                new LangRule(Pattern.compile("^[bcdfghjklmnpqrstvwxyz]+$"), new HashSet<String>(Arrays.asList("de")),
                        true), new LangRule(Pattern.compile("^[a-z]+[aeiou]+[a-z]+$"), new HashSet<String>(
                                Arrays.asList("en")), true));
        Lang lang = new Lang(rules, languages);

        String text = "test";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "English"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(NameType.ENGLISH, lang.guessLanguage(text));

        text = "test123";
        assertFalse(lang.guessLanguages(text).isPresent());
        assertEquals(Languages.ANY, lang.guessLanguage(text));

        text = "testët";
        Set<String> actualLanguages2 = lang.guessLanguages(text);
        assertEquals(Collections.emptySet(), actualLanguages2);
        assertEquals(Languages.ANY, lang.guessLanguage(text));

        text = "testöö";
        assertFalse(lang.guessLanguages(text).isPresent());
        assertEquals(Languages.ANY, lang.guessLanguage(text));

        text = "testüü";
        Set<String> actualLanguages3 = lang.guessLanguages(text);
        assertEquals(Collections.singleton("fr"), actualLanguages3.getLanguages());
        assertEquals(NameType.FRENCH, lang.guessLanguage(text));

        text = "testääöö";
        Set<String> actualLanguages4 = lang.guessLanguages(text);
        assertEquals(Collections.singleton("de"), actualLanguages4.getLanguages());
        assertEquals(NameType.GERMAN, lang.guessLanguage(text));

        text = "testæøå";
        Set<String> actualLanguages5 = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages5);
        assertEquals(NameType.ENGLISH, lang.guessLanguage(text));
    }

    /**
     * Test method for {@link Lang#loadFromResource(String, Languages)}
     */
    @org.junit.Test
    public void testLoadFromResource() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_test.txt";
        Languages languages = EnumSet.allOf(Languages.class);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);

        String text = "test";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "English"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
        assertEquals(NameType.ENGLISH, lang.guessLanguage(text));

        text = "test123";
        assertFalse(lang.guessLanguages(text).isPresent());
        assertEquals(Languages.ANY, lang.guessLanguage(text));

        text = "testët";
        Set<String> actualLanguages2 = lang.guessLanguages(text);
        assertEquals(Collections.singleton("fr"), actualLanguages2.getLanguages());
        assertEquals(NameType.FRENCH, lang.guessLanguage(text));

        text = "testöö";
        Set<String> actualLanguages3 = lang.guessLanguages(text);
        assertEquals(Collections.singleton("de"), actualLanguages3.getLanguages());
        assertEquals(NameType.GERMAN, lang.guessLanguage(text));

        text = "testüü";
        Set<String> actualLanguages4 = lang.guessLanguages(text);
        assertEquals(Collections.singleton("fr"), actualLanguages4.getLanguages());
        assertEquals(NameType.FRENCH, lang.guessLanguage(text));

        text = "testääöö";
        Set<String> actualLanguages5 = lang.guessLanguages(text);
        assertEquals(Collections.singleton("de"), actualLanguages5.getLanguages());
        assertEquals(NameType.GERMAN, lang.guessLanguage(text));

        text = "testæøå";
        Set<String> actualLanguages6 = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages6);
        assertEquals(NameType.ENGLISH, lang.guessLanguage(text));
    }
}