package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Langs;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.ResourceConstants;
import org.apache.commons.lang3.EnumSetUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LangTest {

    private final Lang lang;
    private final String input;
    private final Languages.LanguageSet expected;

    public LangTest(String input, Languages.LanguageSet expected) {
        this.input = input;
        this.expected = expected;
        this.lang = Lang.instance(NameType.DEFAULT);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"apple", Languages.ENGLISH},
            {"banane", Languages.FRENCH},
            {"Wasser", Languages.GERMAN},
            {"eau", Languages.FRENCH},
            {"pomme", Languages.FRENCH},
            {"Orange", Languages.ENGLISH},
            {"orange", Languages.FRENCH},
            {"Pomme", Languages.FRENCH},
            {"pommeau", Languages.FRENCH},
            {"pommeau champagne", Languages.FRENCH},
            {"pommeau de Normandie", Languages.FRENCH},
            {"", Languages.ANY},
            {"12345", Languages.ANY},
            {"あ", Languages.JAPANESE},
            {"Test", Languages.ANY},
            {"测试", Languages.CHINESE},
            {"测试测试", Languages.CHINESE},
            {"测试测试测试", Languages.CHINESE},
            {"hello world", Languages.ENGLISH},
            {"hola mundo", Languages.SPANISH},
            {"привет мир", Languages.RUSSIAN},
            {"你好世界", Languages.CHINESE},
            {"你好世界你好", Languages.CHINESE},
            {"你好世界你好你好", Languages.CHINESE},
            {"你好世界你好你好你好", Languages.CHINESE}
        });
    }

    @Test
    public void testGuessLanguage() {
        assertEquals(this.expected, this.lang.guessLanguages(this.input));
    }

    @Test
    public void testGuessLanguage_MultipleMatches() {
        Set<String> actual = this.lang.guessLanguages(this.input).getLanguages();
        assertTrue(actual.containsAll(this.expected.getLanguages()));
    }

    @Test
    public void testGuessLanguage_NoMatch() {
        Set<String> actual = this.lang.guessLanguages("12345").getLanguages();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGuessLanguage_EmptyInput() {
        Set<String> actual = this.lang.guessLanguages("").getLanguages();
        assertEquals(Languages.ANY.getLanguages(), actual.toArray(new String[0]));
    }

    @Test
    public void testGuessLanguage_NullInput() {
        assertFalse(this.lang.guessLanguages(null) instanceof Languages.LanguageSet);
    }

    @Test
    public void testLang() {
        assertNotNull(this.lang);
        assertEquals(NameType.DEFAULT, this.lang.getNameType());
        assertFalse(this.lang.isInstance(NameType.ALTERNATIVE));
        assertTrue(this.lang.isInstance(NameType.DEFAULT));
    }

}