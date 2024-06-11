import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foobar"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("foobar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foobar"));
    }

    @Test
    public void testEscape() {
        String expected = "&lt;&amp;&gt;&quot;";
        assertEquals(expected, Entities.escape("<&>\""));
    }

    @Test
    public void testUnescape() {
        String expected = "<&>\"";
        assertEquals(expected, Entities.unescape("&lt;&amp;&gt;&quot;"));
    }
}