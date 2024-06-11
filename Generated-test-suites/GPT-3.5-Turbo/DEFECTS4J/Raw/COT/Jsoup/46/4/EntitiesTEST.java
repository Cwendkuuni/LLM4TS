
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("fakeEntity"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("fakeEntity"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }
	
    @Test
    public void testEscape() {
        String input = "hello < world & sample";
        Document.OutputSettings out = new Document.OutputSettings();
        String expected = "hello &lt; world &amp; sample";
        assertEquals(expected, Entities.escape(input, out));
    }

    @Test
    public void testUnescape() {
        String input = "hello &lt; world &amp; sample";
        String expected = "hello < world & sample";
        assertEquals(expected, Entities.unescape(input));
    }
	
    @Test
    public void testUnescapeWithStrict() {
        String input = "hello &lt; world &amp; sample";
        String expected = "hello < world & sample";
        assertEquals(expected, Entities.unescape(input, true));
    }
}
