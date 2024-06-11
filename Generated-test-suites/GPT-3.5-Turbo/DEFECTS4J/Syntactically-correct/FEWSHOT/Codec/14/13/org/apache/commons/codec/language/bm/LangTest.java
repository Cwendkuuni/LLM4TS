package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang.guessLanguage("hello"));
        assertNotNull(lang.guessLanguage("world"));
        assertNotNull(lang.guessLanguage("foo"));
        assertNotNull(lang.guessLanguage("bar"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang.guessLanguages("hello"));
        assertNotNull(lang.guessLanguages("world"));
        assertNotNull(lang.guessLanguages("foo"));
        assertNotNull(lang.guessLanguages("bar"));
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GENERIC));
        assertNotNull(lang);
    }
}