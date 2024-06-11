package org.junit.test;

import static org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import java.util.Properties;
import java.io.IOException;

public class EntitiesTest {

    // ... (Previous test cases here)

    @Test
    public void testLoadEntities() {
        // Given

        // When
        Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");

        // Then
        assertEquals(Entities.base.size(), loadedEntities.size());

        // Given
        Map<String, Character> loadedEntities2 = Entities.loadEntities("entities-full.properties");

        // Then
        assertEquals(Entities.full.size(), loadedEntities2.size());
    }

    @Test
    public void testToCharacterKey() {
        // Given
        Map<String, Character> stringToCharacterMap = new HashMap<>();
        stringToCharacterMap.put("name1", 'A');
        stringToCharacterMap.put("name2", 'B');

        // When
        Map<Character, String> characterToStringMap = Entities.toCharacterKey(stringToCharacterMap);

        // Then
        assertEquals(stringToCharacterMap.size(), characterToStringMap.size());
        assertEquals('A', characterToStringMap.get((char) 0x0041));
        assertEquals('B', characterToStringMap.get((char) 0x0042));
    }
}