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
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        assertEquals(Lang.NameType.ENGLISH, Lang.instance(Lang.NameType.ENGLISH).getNameType());
    }

    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(Lang.NameType.ENGLISH);

        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("world"));
        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("hello world"));

        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("hola"));
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("bonjour"));
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage(""));
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage(null));
    }

    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(Lang.NameType.ENGLISH);

        final Set<Lang.Languages.Language> expectedEnglish = Collections.singleton(Lang.Languages.ENGLISH);
        final Set<Lang.Languages.Language> actualEnglish = englishLang.guessLanguages("hello");
        assertEquals(expectedEnglish, actualEnglish);

        final Set<Lang.Languages.Language> expectedWorld = Collections.singleton(Lang.Languages.ENGLISH);
        final Set<Lang.Languages.Language> actualWorld = englishLang.guessLanguages("world");
        assertEquals(expectedWorld, actualWorld);

        final Set<Lang.Languages.Language> expectedHelloWorld = new HashSet<Lang.Languages.Language>(Arrays.asList(
                Lang.Languages.ENGLISH, Lang.Languages.GERMAN));
        final Set<Lang.Languages.Language> actualHelloWorld = englishLang.guessLanguages("hello world");
        assertTrue(expectedHelloWorld.containsAll(actualHelloWorld));
        assertTrue(actualHelloWorld.containsAll(expectedHelloWorld));

        final Set<Lang.Languages.Language> expectedEmpty = Collections.emptySet();
        final Set<Lang.Languages.Language> actualEmpty = englishLang.guessLanguages("");
        assertEquals(expectedEmpty, actualEmpty);

        final Set<Lang.Languages.Language> expectedNull = Collections.emptySet();
        final Set<Lang.Languages.Language> actualNull = englishLang.guessLanguages(null);
        assertEquals(expectedNull, actualNull);

        final Set<Lang.Languages.Language> expectedHola = EnumSet.of(Lang.Languages.SPANISH);
        final Set<Lang.Languages.Language> actualHola = englishLang.guessLanguages("hola");
        assertEquals(expectedHola, actualHola);

        final Set<Lang.Languages.Language> expectedBonjour = EnumSet.of(Lang.Languages.FRENCH);
        final Set<Lang.Languages.Language> actualBonjour = englishLang.guessLanguages("bonjour");
        assertEquals(expectedBonjour, actualBonjour);

        final Set<Lang.Languages.Language> expectedAny = Lang.Languages.ANY_LANGUAGE;
        final Set<Lang.Languages.Language> actualAny = englishLang.guessLanguages("это русское слово");
        assertEquals(expectedAny, actualAny);
    }
}