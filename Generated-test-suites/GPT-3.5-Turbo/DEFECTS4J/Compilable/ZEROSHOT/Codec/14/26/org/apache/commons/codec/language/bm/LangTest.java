package org.apache.commons.codec.language.bm;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.language.bm.*;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test with word "hello"
        String lang1 = lang.guessLanguage("hello");
        Assert.assertEquals("eng", lang1);
        
        // Test with word "hola"
        String lang2 = lang.guessLanguage("hola");
        Assert.assertEquals("spa", lang2);
        
        // Test with word "bonjour"
        String lang3 = lang.guessLanguage("bonjour");
        Assert.assertEquals("fra", lang3);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test with word "hello"
        Languages.LanguageSet lang1 = lang.guessLanguages("hello");
        Assert.assertEquals("eng", lang1.getAny());
    }
}