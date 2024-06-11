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
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    private static Lang getLangInstance(NameType nameType) {
        return Lang.instance(nameType);
    }

    @org.junit.Test
    public void testGetInstance() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Lang langGerman = getLangInstance(NameType.GERMAN);
        assertNotNull(langEnglish);
        assertNotNull(langGerman);
        assertNotSame(langEnglish, langGerman);
    }

    @org.junit.Test
    public void testGuessLanguageBasic() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Lang langGerman = getLangInstance(NameType.GERMAN);

        String englishWord = "hello";
        String germanWord = "gutenTag";

        assertEquals("English", langEnglish.guessLanguage(englishWord));
        assertEquals("German", langGerman.guessLanguage(germanWord));
    }

    @org.junit.Test
    public void testGuessLanguageMultipleMatches() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Lang langGerman = getLangInstance(NameType.GERMAN);
        Lang langFrench = getLangInstance(NameType.FRENCH);

        String englishWord = "hello";
        String germanWord = "gutenTag";
        String frenchWord = "bonjour";

        Set<String> englishLanguages = langEnglish.guessLanguages(englishWord);
        Set<String> germanLanguages = langGerman.guessLanguages(germanWord);
        Set<String> frenchLanguages = langFrench.guessLanguages(frenchWord);

        assertEquals(Collections.singleton("English"), englishLanguages);
        assertEquals(Collections.singleton("German"), germanLanguages);
        assertEquals(Collections.singleton("French"), frenchLanguages);

        Set<String> mixedLanguages = langEnglish.guessLanguages("gutenTag");
        assertTrue(mixedLanguages.containsAll(Arrays.asList("English", "German")));
    }

    @org.junit.Test
    public void testGuessLanguageNoMatch() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Lang langGerman = getLangInstance(NameType.GERMAN);

        String englishWord = "hello";
        String germanWord = "gutenTag";
        String unknownWord = "xyz";

        assertEquals("English", langEnglish.guessLanguage(englishWord));
        assertEquals("German", langGerman.guessLanguage(germanWord));

        assertNull(langEnglish.guessLanguage(unknownWord));
        assertNull(langGerman.guessLanguage(unknownWord));
    }

    @org.junit.Test
    public void testGuessLanguageAcceptOnMatch() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Lang langGerman = getLangInstance(NameType.GERMAN);

        String englishWord = "hello";
        String germanWord = "gutenTag";
        String englishAcceptanceWord = "hello, world";

        Set<String> englishLanguages = langEnglish.guessLanguages(englishWord);
        Set<String> germanLanguages = langGerman.guessLanguages(germanWord);

        assertEquals(Collections.singleton("English"), englishLanguages);
        assertEquals(Collections.singleton("German"), germanLanguages);

        Set<String> englishAcceptanceLanguages = langEnglish.guessLanguages(englishAcceptanceWord);
        assertEquals(EnumSet.of(NameType.ENGLISH), langEnglish.getLanguages());
        assertTrue(englishAcceptanceLanguages.containsAll(Arrays.asList("English")));
    }

    @org.junit.Test
    public void testGuessLanguageRejectOnMatch() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Lang langGerman = getLangInstance(NameType.GERMAN);

        String englishWord = "hello";
        String germanWord = "gutenTag";
        String germanRejectionWord = "gutenTag, world";

        Set<String> englishLanguages = langEnglish.guessLanguages(englishWord);
        Set<String> germanLanguages = langGerman.guessLanguages(germanWord);

        assertEquals(Collections.singleton("English"), englishLanguages);
        assertEquals(Collections.singleton("German"), germanLanguages);

        Set<String> germanRejectionLanguages = langGerman.guessLanguages(germanRejectionWord);
        assertEquals(EnumSet.of(NameType.GERMAN), langGerman.getLanguages());
        assertFalse(germanRejectionLanguages.contains("German"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGuessLanguageMalformedRule() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        String malformedRuleLine = "abc def ghi true";

        List<LangRule> rules = langEnglish.getRules();
        List<LangRule> updatedRules = new ArrayList<>(rules);
        updatedRules.add(new LangRule(Pattern.compile(malformedRuleLine), new HashSet<>(), true));

        try {
            langEnglish = new Lang(updatedRules, langEnglish.getLanguages());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}