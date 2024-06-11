package org.apache.commons.codec.language.bm;

import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED_CHINESE);
        Assert.assertEquals("zh", lang.guessLanguage("中国"));
        lang = Lang.instance(NameType.YUE);
        Assert.assertEquals("yue", lang.guessLanguage("香港"));
        lang = Lang.instance(NameType.ENGLISH);
        Assert.assertEquals("en", lang.guessLanguage("hello"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED_CHINESE);
        Assert.assertEquals("zh", lang.guessLanguages("中国").getAny());
        lang = Lang.instance(NameType.YUE);
        Assert.assertEquals("yue", lang.guessLanguages("香港").getAny());
        lang = Lang.instance(NameType.ENGLISH);
        Assert.assertEquals("en", lang.guessLanguages("hello").getAny());
    }
}