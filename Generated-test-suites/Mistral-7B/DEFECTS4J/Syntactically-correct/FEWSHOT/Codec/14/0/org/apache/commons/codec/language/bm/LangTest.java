package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        assertSame(Lang.class, english.getClass());
    }

    @Test
    public void testGuessLanguage_English() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final String text = "hello world";
        final String language = english.guessLanguage(text);
        assertEquals(NameType.ENGLISH.getCode(), language);
    }

    @Test
    public void testGuessLanguage_NoMatch() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final String text = "こんにちは世界";
        assertSame(Languages.ANY, english.guessLanguage(text));
    }

    @Test
    public void testGuessLanguages_English() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final String text = "hello world";
        final Set<String> expected = new HashSet<String>(Arrays.asList("en", "eng"));
        final Set<String> actual = english.guessLanguages(text);
        assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguages_NoMatch() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final String text = "こんにちは世界";
        final Set<String> actual = english.guessLanguages(text);
        assertSame(Collections.emptySet(), actual);
    }

    @Test
    public void testGuessLanguages_Mixed() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final Lang german = Lang.instance(NameType.GERMAN);
        final String text = "Hallo Welt";
        final Set<String> expected = new HashSet<String>(EnumSet.of("de", "ger", "en", "eng"));
        final Set<String> actual = getGuessedLanguages(english, german, text);
        assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguages_Mixed_MultipleMatches() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final Lang german = Lang.instance(NameType.GERMAN);
        final Lang spanish = Lang.instance(NameType.SPANISH);
        final String text = "hola mundo";
        final Set<String> expected = new HashSet<String>(EnumSet.of("es", "spa", "en", "eng"));
        final Set<String> actual = getGuessedLanguages(english, german, spanish, text);
        assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguages_NoMatch_AllLanguages() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final Lang german = Lang.instance(NameType.GERMAN);
        final Lang spanish = Lang.instance(NameType.SPANISH);
        final String text = "こんにちは世界";
        final Set<String> actual = getGuessedLanguages(english, german, spanish, text);
        assertSame(Collections.emptySet(), actual);
    }

    private Set<String> getGuessedLanguages(Lang... languages) {
        final String text = "Hallo Welt";
        final Languages languagesObj = Languages.getInstance(Arrays.asList(languages[0].getLanguages(),
                languages[1].getLanguages(), languages[2].getLanguages()));
        final Set<String> actual = languages[0].guessLanguages(text);
        return languagesObj.getLanguages();
    }
}