
package org.junit.test; // Replace with your test package

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.StringCharacterEncoder;

public class EntitiesTest {

    @org.junit.Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @org.junit.Test
    public void testGetCharacterByName() {
        assertEquals( '<', Entities.getCharacterByName("lt") );
        assertNull( Entities.getCharacterByName("unknown") );
    }

    @org.junit.Test
    public void testEscape() {
        String original = "&amp;lt;Hello&apos; World&gt;";
        String expected = "&amp;amp;lt;Hello&apos; World&apos; &gt;";

        assertEquals( expected, Entities.escape(original, new Document.OutputSettings(), Entities.EscapeMode.base) );
    }

    @org.junit.Test
    public void testEscapeWithEncoder() {
        String original = "&amp;lt;Hello&apos; World&gt;";
        String expected = "&amp;lt;Hello&apos; World&gt;"; // no change, since the given String already contains entities

        assertEquals( expected, Entities.escape(original, new StringCharacterEncoder("US-ASCII"), Entities.EscapeMode.base) );
    }

    @org.junit.Test
    public void testUnescape() {
        String original = "&amp;lt;Hello&apos; World&apos; &gt;";
        String expected = "&lt;Hello&apos; World&gt;";

        assertEquals( expected, Entities.unescape(original) );
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEscapeNullString() {
        try {
            Entities.escape(null, new Document.OutputSettings(), Entities.EscapeMode.base);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @org.junit.Test
    public void testIsNamedEntityEmptyString() {
        assertFalse(Entities.isNamedEntity(""));
    }

    @org.junit.Test
    public void testIsNamedEntityWhitespace() {
        String str = "   ";
        assertFalse(Entities.isNamedEntity(str));
    }

    @org.junit.Test(expected = MissingResourceException.class)
    public void testLoadEntitiesFailed() {
        try {
            Map<String, Character> entities = Entities.loadEntities("non-existent-file.properties");
        } catch (MissingResourceException e) {
        }
    }

    @org.junit.Test
    public void testIsNamedEntityInvalidEntityName() {
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @org.junit.Test
    public void testGetCharacterByNameNonExistentEntity() {
        assertNull(Entities.getCharacterByName("non-existent"));
    }

    @org.junit.Test
    public void testEscapeEmptyString() {
        assertEquals("", Entities.escape("", new Document.OutputSettings(), Entities.EscapeMode.base));
    }

    @org.junit.Test
    public void testEscapeStringWithOnlyWhitespace() {
        String str = "   ";
        assertEquals(str, Entities.escape(str, new Document.OutputSettings(), Entities.EscapeMode.base));
    }
}