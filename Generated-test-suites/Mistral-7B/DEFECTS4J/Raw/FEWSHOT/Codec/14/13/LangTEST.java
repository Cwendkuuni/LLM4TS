package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @Test
    public void testInstance() {
        Lang langEN = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEN);
    }

    @Test
    public void testLoadFromResource() {
        Lang langDE = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_de.txt", Languages.getInstance(NameType.GERMAN));
        assertNotNull(langDE);
    }

    @Test
    public void testGuessLanguage() {
        Lang langEN = Lang.instance(NameType.ENGLISH);
        List<String> testCases = Arrays.asList("hello", "hallö", "hola", "hallo", "hej");

        for (String testCase : testCases) {
            String lang = langEN.guessLanguage(testCase);
            switch (testCase) {
                case "hello":
                    assertEquals(NameType.ENGLISH.toString(), lang);
                    break;
                case "hallö":
                    assertEquals(NameType.GERMAN.toString(), lang);
                    break;
                case "hola":
                    assertEquals(NameType.SPANISH.toString(), lang);
                    break;
                case "hallo":
                    assertEquals(NameType.GERMAN.toString(), lang);
                    break;
                case "hej":
                    assertEquals(NameType.SWEDISH.toString(), lang);
                    break;
                default:
                    throw new AssertionError("Unexpected test case: " + testCase);
            }
        }
    }

    @Test
    public void testGuessLanguages() {
        Lang langEN = Lang.instance(NameType.ENGLISH);
        List<String> testCases = Arrays.asList("hallö", "hola", "hallo", "hej");

        for (String testCase : testCases) {
            Collection<String> languages = langEN.guessLanguages(testCase);

            switch (testCase) {
                case "hallö":
                    assertEquals(Collections.singletonList(NameType.GERMAN.toString()), languages);
                    break;
                case "hola":
                    assertEquals(Collections.singletonList(NameType.SPANISH.toString()), languages);
                    break;
                case "hallo":
                    assertEquals(new HashSet<>(Arrays.asList(NameType.GERMAN.toString(), NameType.DUTCH.toString())), languages);
                    break;
                case "hej":
                    assertEquals(Collections.singletonList(NameType.SWEDISH.toString()), languages);
                    break;
                default:
                    throw new AssertionError("Unexpected test case: " + testCase);
            }
        }

        // Test case for no unique match
        String testCaseNoMatch = "schönen";
        Collection<String> languagesNoMatch = langEN.guessLanguages(testCaseNoMatch);
        assertTrue(languagesNoMatch.isEmpty());
    }

    @Test
    public void testAcceptOnMatch() {
        Lang langDE = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_de.txt", Languages.getInstance(NameType.GERMAN));
        List<String> testCases = Arrays.asList("schön", "guten", "tag");

        for (String testCase : testCases) {
            Collection<String> languages = langDE.guessLanguages(testCase);

            switch (testCase) {
                case "schön":
                    assertEquals(Collections.singletonList(NameType.GERMAN.toString()), languages);
                    break;
                case "guten":
                    assertTrue(languages.isEmpty());
                    break;
                case "tag":
                    assertTrue(languages.isEmpty());
                    break;
                default:
                    throw new AssertionError("Unexpected test case: " + testCase);
            }
        }
    }
}