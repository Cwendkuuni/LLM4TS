package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang english = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.Languages.ENGLISH, english.guessLanguage("hello"));
        assertEquals(Lang.Languages.ENGLISH, english.guessLanguage("world"));
        assertEquals(Lang.Languages.ENGLISH, english.guessLanguage("hello world"));

        Lang german = Lang.instance(NameType.GERMAN);
        assertEquals(Lang.Languages.GERMAN, german.guessLanguage("hallo"));
        assertEquals(Lang.Languages.GERMAN, german.guessLanguage("welt"));
        assertEquals(Lang.Languages.GERMAN, german.guessLanguage("hallo welt"));

        Lang french = Lang.instance(NameType.FRENCH);
        assertEquals(Lang.Languages.FRENCH, french.guessLanguage("bonjour"));
        assertEquals(Lang.Languages.FRENCH, french.guessLanguage("monde"));
        assertEquals(Lang.Languages.FRENCH, french.guessLanguage("bonjour monde"));

        Lang russian = Lang.instance(NameType.RUSSIAN);
        assertEquals(Lang.Languages.RUSSIAN, russian.guessLanguage("привет"));
        assertEquals(Lang.Languages.RUSSIAN, russian.guessLanguage("мир"));
        assertEquals(Lang.Languages.RUSSIAN, russian.guessLanguage("привет мир"));

        Lang any = Lang.instance(NameType.ANY);
        assertEquals(Lang.Languages.ANY, any.guessLanguage(""));
        assertEquals(Lang.Languages.ANY, any.guessLanguage("foo"));
        assertEquals(Lang.Languages.ANY, any.guessLanguage("foo bar"));
    }

    @Test
    public void testGuessLanguages() {
        Lang english = Lang.instance(NameType.ENGLISH);
        Set<String> expected = new HashSet<String>(Arrays.asList("en", "english"));
        Set<String> result = english.guessLanguages("hello");
        assertEquals(expected, result);

        expected.add("de");
        result = english.guessLanguages("hallo");
        assertEquals(expected, result);

        expected.remove("de");
        result = english.guessLanguages("hello hallo");
        assertEquals(expected, result);

        Set<String> empty = Collections.emptySet();
        result = english.guessLanguages("");
        assertEquals(empty, result);

        Lang german = Lang.instance(NameType.GERMAN);
        result = german.guessLanguages("hallo");
        assertEquals(Collections.singleton("de"), result);

        result = german.guessLanguages("hallo hallo");
        assertEquals(Collections.singleton("de"), result);

        result = german.guessLanguages("hallo hallo foo");
        assertEquals(Collections.singleton("de"), result);

        result = german.guessLanguages("foo");
        assertEquals(Collections.emptySet(), result);

        result = german.guessLanguages("foo bar");
        assertEquals(Collections.emptySet(), result);

        Set<String> all = Lang.Languages.getAllLanguages();
        result = german.guessLanguages("foo bar baz");
        assertTrue(all.containsAll(result));
    }
}