package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

public class ParseSettingsTest {
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(false, parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertEquals(false, parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        String name = "testTag";
        assertEquals("testtag", parseSettings.normalizeTag(name));

        parseSettings = new ParseSettings(true, false);
        assertEquals("testTag", parseSettings.normalizeTag(name));
    }

    @Test
    public void testNormalizeAttribute() {
        String name = "testAttribute";
        assertEquals("testattribute", parseSettings.normalizeAttribute(name));

        parseSettings = new ParseSettings(false, true);
        assertEquals("testattribute", parseSettings.normalizeAttribute(name));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = mock(Attributes.class);
        parseSettings.normalizeAttributes(attributes);

        verify(attributes, times(1)).normalize();
    }
}