package org.jsoup.parser;
import org.junit.Test;
import org.junit.Assert;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        ParseSettings preserveCase = new ParseSettings(true, true);

        Assert.assertFalse(htmlDefault.preserveTagCase());
        Assert.assertTrue(preserveCase.preserveTagCase());
    }
}