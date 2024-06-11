
package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {
    
    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.WIKI_COUNTRY_ISO3);
        assertTrue(lang instanceof Lang);
    }
    
    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.WIKI_COUNTRY_ISO3));
        assertTrue(lang instanceof Lang);        
    }
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.WIKI_COUNTRY_ISO3);
        String result = lang.guessLanguage("ger");
        assertEquals("eng", result);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.WIKI_COUNTRY_ISO3);
        Languages.LanguageSet ls = lang.guessLanguages("ger");
        assertEquals(1, ls.size());
        assertTrue(ls.contains("eng"));
    }

}
