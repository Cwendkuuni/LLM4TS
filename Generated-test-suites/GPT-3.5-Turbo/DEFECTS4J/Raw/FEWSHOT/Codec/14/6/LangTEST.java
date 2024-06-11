package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LangTest {
    
    @Test
    public void testInstance() {
        assertNotNull(Lang.instance(NameType.NAME));
        assertNotNull(Lang.instance(NameType.GENERIC));
        assertNotNull(Lang.instance(NameType.GENERIC_TM));
    }
    
    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.NAME));
        assertNotNull(lang);
    }
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.NAME);
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("es", lang.guessLanguage("hola"));
        assertEquals("fr", lang.guessLanguage("bonjour"));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.NAME);
        Languages.LanguageSet ls = lang.guessLanguages("hello");
        assertEquals(1, ls.size());
        assertEquals("en", ls.getAny());
        
        ls = lang.guessLanguages("hola");
        assertEquals(1, ls.size());
        assertEquals("es", ls.getAny());
        
        ls = lang.guessLanguages("bonjour");
        assertEquals(1, ls.size());
        assertEquals("fr", ls.getAny());
    }
}