import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscapeXhtml() {
        String escaped = Entities.escape("<>&\"'", Entities.EscapeMode.xhtml);
        assertEquals("&lt;&gt;&amp;&quot;&apos;", escaped);
    }
    
    @Test
    public void testEscapeBase() {
        String escaped = Entities.escape("<>&\"'", Entities.EscapeMode.base);
        assertEquals("<>&\"'", escaped);
    }
    
    @Test
    public void testEscapeExtended() {
        String escaped = Entities.escape("<>&\"'", Entities.EscapeMode.extended);
        assertEquals("&lt;&gt;&amp;&quot;&apos;", escaped);
    }
    
    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;&quot;&apos;");
        assertEquals("<>&\"'", unescaped);
    }

}