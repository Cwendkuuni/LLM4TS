import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscapeString() {
        String escaped = Entities.escape("\"Hello\" & 'World'", new Document.OutputSettings());
        assertEquals("&quot;Hello&quot; &amp; &#39;World&#39;", escaped);
    }

    @Test
    public void testUnescapeString() {
        String unescaped = Entities.unescape("&quot;Hello&quot; &amp; &#39;World&#39;");
        assertEquals("\"Hello\" & 'World'", unescaped);
    }

    // Add more test cases for other methods as needed

}