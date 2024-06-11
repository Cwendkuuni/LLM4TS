package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        String entityName = "lt";
        assertTrue(Entities.isNamedEntity(entityName));
    }

    @Test
    public void testGetCharacterByName() {
        String entityName = "lt";
        Character expectedCharacter = '<';
        assertEquals(expectedCharacter, Entities.getCharacterByName(entityName));
    }

    @Test
    public void testIsNamedEntity_NotRecognizedEntityName_ReturnsFalse() {
        String entityName = "notanentity";
        assertFalse(Entities.isNamedEntity(entityName));
    }

    @Test
    public void testIsNamedEntity_MixedCaseEntityName_ReturnsTrue() {
        String entityName = "lT";
        assertTrue(Entities.isNamedEntity(entityName));
    }

    @Test
    public void testIsNamedEntity_EntityNameWithNumber_ReturnsTrue() {
        String entityName = "e123";
        assertTrue(Entities.isNamedEntity(entityName));
    }

    @Test
    public void testIsNamedEntity_IntegerValueEntityName_ReturnsFalse() {
        String entityName = "123";
        assertFalse(Entities.isNamedEntity(entityName));
    }

    @Test
    public void testGetCharacterByName_NotRecognizedEntityName_ReturnsNull() {
        String entityName = "notanentity";
        assertNull(Entities.getCharacterByName(entityName));
    }

    @Test
    public void testGetCharacterByName_MixedCaseEntityName_ReturnsCharacter() {
        String entityName = "Lt";
        Character expectedCharacter = '<';
        assertEquals(expectedCharacter, Entities.getCharacterByName(entityName));
    }

    @Test
    public void testGetCharacterByName_EntityNameWithNumber_ReturnsCharacter() {
        String entityName = "e123";
        Character expectedCharacter = 'ε'; // Example character for testing purposes
        assertEquals(expectedCharacter, Entities.getCharacterByName(entityName));
    }

    @Test
    public void testGetCharacterByName_IntegerValueEntityName_ReturnsNull() {
        String entityName = "123";
        assertNull(Entities.getCharacterByName(entityName));
    }

}