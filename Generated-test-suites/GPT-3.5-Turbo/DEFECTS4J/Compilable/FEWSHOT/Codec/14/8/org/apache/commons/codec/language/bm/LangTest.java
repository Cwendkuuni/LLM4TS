package org.apache.commons.codec.language.bm;
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Assert.assertEquals("eng", lang.guessLanguage("hello"));
        Assert.assertEquals("spa", lang.guessLanguage("hola"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        Assert.assertEquals("eng", result.getAny());

        result = lang.guessLanguages("hola");
        Assert.assertEquals("spa", result.getAny());
    }
}