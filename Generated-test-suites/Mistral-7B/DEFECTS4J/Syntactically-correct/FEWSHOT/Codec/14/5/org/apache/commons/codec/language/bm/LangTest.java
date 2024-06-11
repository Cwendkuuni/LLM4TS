package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        assertNotNull(Lang.instance(NameType.DETECTED_LANGUAGE));
    }

    @Test
    public void testGuessLanguageNullInput() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);
        assertNull(lang.guessLanguage(null));
    }

    @Test
    public void testGuessLanguageEmptyInput() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);
        assertEquals(Languages.ANY, lang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguageSingleLanguageRule() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);

        final String input = "foo";
        final Set<String> expected = Collections.singletonList("en").toString().split("\\+");
        final Set<String> actual = lang.guessLanguages(input).getLanguages();

        assertEquals(expected[0], actual.iterator().next());
        assertEquals(1, actual.size());
    }

    @Test
    public void testGuessLanguageMultipleMatches() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);

        final String input = "barfooqux";
        final Set<String> expected = Arrays.asList("en", "de", "nl").toString().split("\\+");
        final Set<String> actual = lang.guessLanguages(input).getLanguages();

        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testGuessLanguageNoMatch() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);

        final String input = "qux";
        final Set<String> expected = Collections.singletonList(Languages.ANY.getName());
        final Set<String> actual = lang.guessLanguages(input).getLanguages();

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGuessLanguageIgnoreCase() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);

        final String input = "BARFOOQUX";
        final Set<String> expected = Arrays.asList("en", "de", "nl").toString().split("\\+");
        final Set<String> actual = lang.guessLanguages(input).getLanguages();

        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testGuessLanguageAcceptOnMatch() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE);

        final String input = "qux";
        final Set<String> expected = Collections.emptySet();
        final Set<String> actual = Lang.instance(NameType.DETECTED_LANGUAGE_ACCEPT_ON_MATCH).guessLanguages(input).getLanguages();

        assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguageRejectOnMatch() {
        final Lang lang = Lang.instance(NameType.DETECTED_LANGUAGE_REJECT_ON_MATCH);

        final String input = "qux";
        final Set<String> expected = Collections.singletonList(Languages.ANY.getName());
        final Set<String> actual = lang.guessLanguages(input).getLanguages();

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGuessLanguageCustomLanguageTable() {
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/customlang.txt",
                EnumSet.of(Languages.DE, Languages.FR, Languages.NL));

        final String input = "barfooqux";
        final Set<String> expected = Arrays.asList("de", "fr", "nl").toString().split("\\+");
        final Set<String> actual = lang.guessLanguages(input).getLanguages();

        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }
}