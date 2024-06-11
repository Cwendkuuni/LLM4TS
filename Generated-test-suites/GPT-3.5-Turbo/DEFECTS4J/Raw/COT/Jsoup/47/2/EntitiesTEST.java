
import org.junit.Test;
import java.util.MissingResourceException;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        assertEquals(Character.valueOf('<'), character);
        character = Entities.getCharacterByName("amp");
        assertEquals(Character.valueOf('&'), character);
        character = Entities.getCharacterByName("abc");
        assertNull(character);
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div>hello</div>", Document.OutputSettings.html());
        assertEquals("&lt;div&gt;hello&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;hello&lt;/div&gt;");
        assertEquals("<div>hello</div>", unescapedString);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesMissingResource() {
        Entities.loadEntities("non-existent-file.properties");
    }
}
