

import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    // Test isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("test")); // Test with non-existent entity name
        assertTrue(Entities.isNamedEntity("lt")); // Test with existing entity name
    }

    // Test getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("test")); // Test with non-existent entity name
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // Test with existing entity name
    }

    // Test escape method
    @Test
    public void testEscape() {
        String string = "The less than sign < and the greater than sign >";
        String expected = "The less than sign &lt; and the greater than sign &gt;";
        assertEquals(expected, Entities.escape(string, null));
    }

    // Test unescape method
    @Test
    public void testUnescape() {
        String string = "The less than sign &lt; and the greater than sign &gt;";
        String expected = "The less than sign < and the greater than sign >";
        assertEquals(expected, Entities.unescape(string));
    }

    // Test loadEntities method
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }
}