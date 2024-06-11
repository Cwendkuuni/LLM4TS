package org.apache.commons.codec.language.bm;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LangTest {
    

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(Lang.NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceWithInvalidResourceName() {
        Lang.loadFromResource("org/apache/commons/codec/language/bm/invalidResourceName.txt", Languages.getInstance(Lang.NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullResourceStream() {
        Lang.loadFromResource(null, Languages.getInstance(Lang.NameType.ENGLISH));
    }

    @Test
    public void testGuessLanguage_EmptyString() {
        Lang lang = Lang.instance(Lang.NameType.ENGLISH);
        Set<String> expectedLanguages = Collections.singleton("any");
        Set<String> actualLanguages = lang.guessLanguages("");
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguage_SimpleCase() {
        Lang lang = Lang.instance(Lang.NameType.ENGLISH);
        String text = "hello";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "hi"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguage_MixedLanguages() {
        Lang lang = Lang.instance(Lang.NameType.ENGLISH);
        String text = "Hola! Hola, ¿cómo estás?";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("es", "en"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguage_MultipleMatches() {
        Lang lang = Lang.instance(Lang.NameType.ENGLISH);
        String text = "Monsieur Dupont";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("fr", "en"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguage_NoMatches() {
        Lang lang = Lang.instance(Lang.NameType.ENGLISH);
        String text = "Xyzzy123";
        Set<String> expectedLanguages = Collections.singleton("any");
        Set<String> actualLanguages = lang.guessLanguages(text);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguage_ExceptionHandling() {
        Lang lang = null;
        try {
            lang = Lang.instance(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Lang lang2 = new Lang(Collections.emptyList(null), Languages.getInstance(Lang.NameType.ENGLISH));
        try {
            lang2.guessLanguages("test");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    
}