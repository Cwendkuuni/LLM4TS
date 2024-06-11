import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }
    
    @Test
    public void testEscape() {
        String escaped1 = Entities.escape("<script>alert('Hello World!')</script>", Document.OutputSettings.html());
        assertEquals("&lt;script&gt;alert('Hello World!')&lt;/script&gt;", escaped1);
        
        String escaped2 = Entities.escape("This is a &amp; test.", Document.OutputSettings.xhtml());
        assertEquals("This is a &amp;amp; test.", escaped2);
    }
    
    @Test
    public void testUnescape() {
        String unescaped1 = Entities.unescape("&lt;script&gt;alert('Hello World!')&lt;/script&gt;");
        assertEquals("<script>alert('Hello World!')</script>", unescaped1);
        
        String unescaped2 = Entities.unescape("This is a &amp;amp; test.");
        assertEquals("This is a &amp; test.", unescaped2);
    }
}