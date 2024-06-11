import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("test"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("This is a test", new Document.OutputSettings());
        assertEquals("This is a test", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("This is a test");
        assertEquals("This is a test", unescaped);
    }
}