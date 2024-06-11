package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
}