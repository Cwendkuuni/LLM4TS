package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @org.junit.Test
    public void testInstance() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);
        assertTrue(englishLang != null);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);
        final String word = "hello";

        final String guessedLanguage = englishLang.guessLanguage(word);
        assertEquals(English.getName(), guessedLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final NameType english = NameType.ENGLISH;
        final NameType german = NameType.GERMAN;
        final Lang englishLang = Lang.instance(english);
        final Lang germanLang = Lang.instance(german);
        final String word = "hallo";

        final Set<String> guessedLanguages = englishLang.guessLanguages(word);
        assertFalse(guessedLanguages.contains(German.getName()));

        final Set<String> germanGuessedLanguages = germanLang.guessLanguages(word);
        assertTrue(germanGuessedLanguages.contains(German.getName()));

        final Set<String> englishAndGermanGuessedLanguages = Lang.instance(NameType.ENGLISH_GERMAN)
                .guessLanguages(word);
        assertTrue(englishAndGermanGuessedLanguages.contains(English.getName()));
        assertTrue(englishAndGermanGuessedLanguages.contains(German.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguagesWithMultipleMatches() {
        final NameType english = NameType.ENGLISH;
        final NameType german = NameType.GERMAN;
        final NameType spanish = NameType.SPANISH;
        final Lang englishLang = Lang.instance(english);
        final Lang germanLang = Lang.instance(german);
        final Lang spanishLang = Lang.instance(spanish);
        final String word = "hola";

        final Set<String> guessedLanguages = englishLang.guessLanguages(word);
        assertFalse(guessedLanguages.contains(German.getName()));
        assertFalse(guessedLanguages.contains(Spanish.getName()));

        final Set<String> germanGuessedLanguages = germanLang.guessLanguages(word);
        assertFalse(germanGuessedLanguages.contains(English.getName()));
        assertFalse(germanGuessedLanguages.contains(Spanish.getName()));

        final Set<String> spanishGuessedLanguages = spanishLang.guessLanguages(word);
        assertTrue(spanishGuessedLanguages.contains(Spanish.getName()));
        assertFalse(spanishGuessedLanguages.contains(English.getName()));
        assertFalse(spanishGuessedLanguages.contains(German.getName()));

        final Set<String> englishAndGermanAndSpanishGuessedLanguages = Lang.instance(NameType.ENGLISH_GERMAN_SPANISH)
                .guessLanguages(word);
        assertTrue(englishAndGermanAndSpanishGuessedLanguages.contains(English.getName()));
        assertTrue(englishAndGermanAndSpanishGuessedLanguages.contains(German.getName()));
        assertTrue(englishAndGermanAndSpanishGuessedLanguages.contains(Spanish.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguagesWithNoMatches() {
        final NameType english = NameType.ENGLISH;
        final NameType german = NameType.GERMAN;
        final Lang englishLang = Lang.instance(english);
        final Lang germanLang = Lang.instance(german);
        final String word = "hallo Welt";

        final Set<String> guessedLanguages = englishLang.guessLanguages(word);
        assertFalse(guessedLanguages.contains(English.getName()));
        assertFalse(guessedLanguages.contains(German.getName()));

        final Set<String> germanGuessedLanguages = germanLang.guessLanguages(word);
        assertFalse(germanGuessedLanguages.contains(English.getName()));
        assertFalse(germanGuessedLanguages.contains(German.getName()));

        final Set<String> englishAndGermanGuessedLanguages = Lang.instance(NameType.ENGLISH_GERMAN)
                .guessLanguages(word);
        assertFalse(englishAndGermanGuessedLanguages.contains(English.getName()));
        assertFalse(englishAndGermanGuessedLanguages.contains(German.getName()));

        final Set<String> noLanguagesGuessed = Lang.instance(NameType.NO_LANGUAGES).guessLanguages(word);
        assertTrue(noLanguagesGuessed.isEmpty());
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguagesWithEmptyString() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);

        final Set<String> guessedLanguages = englishLang.guessLanguages("");
        assertEquals(Collections.emptySet(), guessedLanguages);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguagesWithNull() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);

        final Set<String> guessedLanguages = englishLang.guessLanguages(null);
        assertEquals(Collections.emptySet(), guessedLanguages);
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)}.
     */
    @org.junit.Test
    public void testLangConstruction() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);

        final List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("^hallo\\s+(?:[a-zäöüß]+|[A-ZÄÖÜß]+(?:[\\s-][\\w]+)+|[A-ZÄÖÜß]+)$"),
                        new HashSet<String>(Arrays.asList("de", "de_DE")), true),
                new LangRule(Pattern.compile("^[hH][eE][lL][lL][oO]$"),
                        new HashSet<String>(Arrays.asList("en", "en_US")), true));

        final Languages englishLanguages = Languages.getInstance(english);
        final Lang newEnglishLang = new Lang(rules, englishLanguages);

        assertTrue(newEnglishLang != null);
        assertTrue(newEnglishLang.rules.equals(englishLang.rules));
        assertTrue(newEnglishLang.languages.equals(englishLanguages));
    }

}

enum English {
    NAME
}

enum German {
    NAME
}

enum Spanish {
    NAME
}

enum NameType {
    ENGLISH,
    GERMAN,
    SPANISH,
    ENGLISH_GERMAN,
    ENGLISH_GERMAN_SPANISH,
    NO_LANGUAGES
}

class Languages {

    private static final EnumSet<NameType> ALL_LANGUAGES = EnumSet.allOf(NameType.class);

    private static final Languages INSTANCE = new Languages();

    private final Map<NameType, Set<String>> languagesByNameType;

    private Languages() {
        this.languagesByNameType = new EnumMap<NameType, Set<String>>(NameType.class);
        for (final NameType langType : ALL_LANGUAGES) {
            this.languagesByNameType.put(langType, new HashSet<String>());
        }
        this.languagesByNameType.get(NameType.ENGLISH).add("en");
        this.languagesByNameType.get(NameType.GERMAN).add("de");
        this.languagesByNameType.get(NameType.GERMAN).add("de_DE");
        this.languagesByNameType.get(NameType.SPANISH).add("es");
        this.languagesByNameType.get(NameType.ENGLISH_GERMAN).addAll(Arrays.asList("en", "de", "de_DE"));
        this.languagesByNameType.get(NameType.ENGLISH_GERMAN_SPANISH).addAll(Arrays.asList("en", "de", "de_DE", "es"));
        this.languagesByNameType.get(NameType.NO_LANGUAGES).addAll(Arrays.asList());
    }

    public static Languages getInstance(final NameType nameType) {
        return INSTANCE;
    }

    public Set<String> getLanguages() {
        return this.languagesByNameType.get(NameType.ALL);
    }

    public Set<String> getLanguages(final NameType nameType) {
        return this.languagesByNameType.get(nameType);
    }

    public static Set<String> from(final Set<String> langs) {
        final EnumSet<NameType> matchingTypes = EnumSet.noneOf(NameType.class);
        final Set<String> result = new HashSet<String>();
        for (final NameType langType : ALL_LANGUAGES) {
            final Set<String> languages = Languages.getInstance(langType).getLanguages();
            if (languages.containsAll(langs)) {
                matchingTypes.add(langType);
            }
            result.addAll(languages);
        }
        if (matchingTypes.isEmpty()) {
            return Languages.getInstance(NameType.NO_LANGUAGES).getLanguages();
        }
        return new EnumSet<NameType>(matchingTypes);
    }

    private static final NameType ALL = EnumSet.allOf(NameType.class).iterator().next();
}