// EntitiesTest.java will contain all the test cases for the Entities class
package org.junit.platform.commons.annotation.Generated;

import org.junit.Test;
import static org.junit.Assert.*;

@Generated("org.jsoup.nodes.EntitiesTest")
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        String namedEntity = "lt";
        assertTrue(Entities.isNamedEntity(namedEntity));
    }

    @Test
    public void testIsNamedEntity_UnknownEntity() {
        String unknownEntity = "unkownEntity";
        assertFalse(Entities.isNamedEntity(unknownEntity));
    }

    @Test
    public void testGetCharacterByName() {
        String namedEntity = "amp";
        Character expectedCharacter = '&';
        Character actualCharacter = Entities.getCharacterByName(namedEntity);
        assertEquals(expectedCharacter, actualCharacter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByName_InvalidEntityName() {
        String invalidEntityName = "";
        try {
            Entities.getCharacterByName(invalidEntityName);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEscape() {
        String input = "Hello <world>";
        String output = Entities.escape(input, Document.OutputSettings.createPrettyPrint(), EscapeMode.base);
        String expectedOutput = "Hello &lt;world&gt;";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_EmptyString() {
        String input = "";
        String output = Entities.escape(input, Document.OutputSettings.createPrettyPrint(), EscapeMode.base);
        String expectedOutput = "";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "Hello &lt;world&gt;";
        String output = Entities.unescape(input);
        String expectedOutput = "Hello <world>";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape_EmptyString() {
        String input = "";
        String output = Entities.unescape(input);
        String expectedOutput = "";
        assertEquals(expectedOutput, output);
    }

    // Test cases for other methods in Entities class go here
}