package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set
        assertFalse(Entities.isBaseNamedEntity("amp")); // not in base set
        assertFalse(Entities.isBaseNamedEntity("lt")); // not in base set
        assertFalse(Entities.isBaseNamedEntity("gt")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        Map<String, Character> base = Entities.base;

        // test named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // test base named entities
        assertEquals('<', Entities.getCharacterByName("lt")); // identical to named entity
        assertEquals('>', Entities.getCharacterByName("gt")); // identical to named entity
        assertEquals('&', Entities.getCharacterByName("amp")); // identical to named entity
        assertEquals('"', Entities.getCharacterByName("quot")); // identical to named entity

        // test non-entities
        assertNull(Entities.getCharacterByName("test"));
        assertNull(Entities.getCharacterByName(" "));
        assertNull(Entities.getCharacterByName("_"));

        // test entities not in base set
        assertNull(Entities.getCharacterByName("copy;"));
        assertNull(Entities.getCharacterByName("reg;"));
        assertNull(Entities.getCharacterByName("rsqb;"));

        // test base entities
        assertEquals('\u00A0', Entities.getCharacterByName("nbsp;")); // non-breaking space
        assertEquals('\u0096', Entities.getCharacterByName("ensp;")); // en space
        assertEquals('\u00A1', Entities.getCharacterByName("iexcl;")); // inverted exclamation mark
        assertEquals('\u00A3', Entities.getCharacterByName("cent;")); // cent sign
        assertEquals('\u00A5', Entities.getCharacterByName("pound;")); // pound sign
        assertEquals('\u00A7', Entities.getCharacterByName("section;")); // section sign
        assertEquals('\u00AA', Entities.getCharacterByName("deg;")); // degree sign
        assertEquals('\u00AB', Entities.getCharacterByName("sup;")); // superscript
        assertEquals('\u00AD', Entities.getCharacterByName("sub;")); // subscript
        assertEquals('\u00AE', Entities.getCharacterByName("acute;")); // acute accent
        assertEquals('\u00B0', Entities.getCharacterByName("deg;")); // degree sign (different than &#xA0;)
        assertEquals('\u00BF', Entities.getCharacterByName("invisibles;")); // soft hyphen

        // test extended entities
        assertEquals('\u02C6', Entities.getCharacterByName("circ;")); // modifier letter circumflex
        assertEquals('\u02DC', Entities.getCharacterByName("tilde;")); // small tilde
        assertEquals('\u0323', Entities.getCharacterByName("cedil;")); // comma below
        assertEquals('\u0325', Entities.getCharacterByName("cedil;")); // comma below
        assertEquals('\u0327', Entities.getCharacterByName("cedil;")); // comma below
        assertEquals('\u0329', Entities.getCharacterByName("cedil;")); // comma below
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String input = "Lorem &amp; ipsum dolor &quot;sit &amp; amet, consectetur &lt;adipiscing&gt; elit.";
        String expected = "Lorem &amp;amp; amp; ipsum dolor &amp;quot;sit &amp;amp; amp; amet, consectetur &amp;lt;adipiscing&amp;gt; elit.";

        assertEquals(expected, Entities.escape(input, settings));
    }

    @Test
    public void testEscapeWithXhtmlMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.xhtml);
        String input = "Lorem &amp;&#xA0; ipsum dolor &quot;sit &amp; amet, consectetur &lt;adipiscing&gt; elit.";
        String expected = "Lorem &amp; #xA0; ipsum dolor &quot;sit &amp; amet, consectetur &lt;adipiscing&gt; elit.";

        assertEquals(expected, Entities.escape(input, settings));
    }

    @Test
    public void testEscapeWithBaseMode() {
        Document.OutputSettings setting = new Document.OutputSettings();
        setting.setEscapeMode(Entities.EscapeMode.base);
        String input = "Lorem &amp; ipsum dolor &quot;sit &amp; amet, consectetur &lt;adipiscing&gt; elit.";
        String expected = "Lorem &amp;amp; #38; ipsum dolor &amp;quot;sit &amp;amp; #38; amet, consectetur &amp;lt;adipiscing&amp;gt; elit.";

        assertEquals(expected, Entities.escape(input, setting));
    }

    @Test
    public void testEscapeWithExtendedMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.extended);
        String input = "Lorem &amp; ipsum dolor &quot;sit &amp; amet, consectetur &lt;adipiscing&gt; elit.";
        String expected = "Lorem &amp;amp; #38; ipsum dolor &amp;quot;sit &amp;amp; #38; amet, consectetur &lt;adipiscing&gt; elit.";

        assertEquals(expected, Entities.escape(input, settings));
    }
}