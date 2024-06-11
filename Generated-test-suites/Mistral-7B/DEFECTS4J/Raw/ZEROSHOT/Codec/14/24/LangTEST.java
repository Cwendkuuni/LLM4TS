package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test file for Lang class.
 *
 */
public class LangTest {

    /**
     * Test instance creation.
     */
    @org.junit.Test
    public void testInstance() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEnglish);
    }

    /**
     * Test loading rules from resource file.
     */
    @org.junit.Test
    public void testLoadFromResource() {
        Lang langEnglish = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.getInstance(NameType.ENGLISH));
        assertNotNull(langEnglish);
        List<LangRule> rules = langEnglish.getRules();
        assertEquals(4, rules.size());

        LangRule rule1 = rules.get(0);
        assertEquals("^[Hh]i[\\s.$]+$", rule1.getPattern().pattern());
        List<String> languages1 = rule1.getLanguages();
        assertEquals(1, languages1.size());
        assertEquals("hi", languages1.get(0));

        LangRule rule2 = rules.get(1);
        assertEquals("^[Aa]re|[Aa]rent$", rule2.getPattern().pattern());
        List<String> languages2 = rule2.getLanguages();
        assertEquals(2, languages2.size());
        assertEquals("nl", languages2.get(0));
        assertEquals("en", languages2.get(1));
    }

    /**
     * Test language guessing.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        String word1 = "hello";
        String language1 = langEnglish.guessLanguage(word1);
        assertEquals(NameType.ENGLISH.toString(), language1);

        String word2 = "hallo";
        String language2 = langEnglish.guessLanguage(word2);
        assertEquals("nl", language2);

        String word3 = "are you there";
        String language3 = langEnglish.guessLanguage(word3);
        assertEquals("nl", language3); // false positive, should be "en"

        String word4 = "are not you there";
        String language4 = langEnglish.guessLanguage(word4);
        assertEquals("en", language4);

        String word5 = "你好";
        String language5 = langEnglish.guessLanguage(word5);
        assertEquals(Languages.ANY.toString(), language5);
    }
}