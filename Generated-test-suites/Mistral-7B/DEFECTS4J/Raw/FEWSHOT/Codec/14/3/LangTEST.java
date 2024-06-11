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

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        final Lang german = Lang.instance(NameType.GERMAN);
        assertNotNull(german);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstanceInvalidNameType() {
        Lang.instance(NameType.values()[(NameType.values.length + 1)]);
    }

    @Test
    public void testLoadFromResource() {
        final Lang english = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.ENGLISH);
        assertNotNull(english);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceNullResourceName() {
        Lang.loadFromResource(null, Languages.ENGLISH);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceNullLanguages() {
        Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, null);
    }

    @Test
    public void testGuessLanguage() {
        final Lang german = Lang.instance(NameType.GERMAN);
        final String text1 = "Hallo";
        final String text2 = "Welt";
        final String text3 = "test";

        final String guessedLanguage1 = german.guessLanguage(text1);
        assertEquals(NameType.GERMAN.getShortName(), guessedLanguage1);

        final String guessedLanguage2 = german.guessLanguage(text2);
        assertEquals(NameType.GERMAN.getShortName(), guessedLanguage2);

        final String guessedLanguage3 = german.guessLanguage(text3);
        assertEquals(Languages.ANY.getShortName(), guessedLanguage3);
    }

    @Test
    public void testGuessLanguages() {
        final Lang german = Lang.instance(NameType.GERMAN);
        final String text1 = "Hallo";
        final String text2 = "Welt";
        final String text3 = "test";
        final String text4 = "Schokolade";
        final String text5 = "Monday";

        final Set<String> guessedLanguages1 = german.guessLanguages(text1);
        assertEquals(EnumSet.of(NameType.GERMAN.getShortName()), guessedLanguages1);

        final Set<String> guessedLanguages2 = german.guessLanguages(text2);
        assertEquals(EnumSet.of(NameType.GERMAN.getShortName()), guessedLanguages2);

        final Set<String> guessedLanguages3 = german.guessLanguages(text3);
        assertEquals(Collections.emptySet(), guessedLanguages3);

        final Set<String> guessedLanguages4 = german.guessLanguages(text4);
        assertTrue(guessedLanguages4.containsAll(Arrays.asList(NameType.GERMAN.getShortName(), NameType.DUTCH.getShortName(), NameType.ENGLISH.getShortName())));

        final Set<String> guessedLanguages5 = german.guessLanguages(text5);
        assertEquals(Collections.emptySet(), guessedLanguages5);
    }
}