package org.apache.commons.codec.language.bm;

import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Basic test cases
        Assert.assertEquals("en", lang.guessLanguage("hello"));
        Assert.assertEquals("fr", lang.guessLanguage("bonjour"));
        Assert.assertEquals("es", lang.guessLanguage("hola"));
        
        // Edge cases
        Assert.assertEquals("und", lang.guessLanguage("1234567890")); // Non-alphabetic characters
        Assert.assertEquals("und", lang.guessLanguage("")); // Empty string
        Assert.assertEquals("und", lang.guessLanguage(null)); // Null input
    }

}

