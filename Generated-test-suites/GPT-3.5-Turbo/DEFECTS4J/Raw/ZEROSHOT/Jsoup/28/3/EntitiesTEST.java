import org.junit.*;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("foobar"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("foobar"));
    }
    
    @Test
    public void testEscape() {
        String string1 = "Hello < World";
        String string2 = "Hello & World";
        String string3 = "Hello ' World";
        String string4 = "Hello \" World";
        String string5 = "Hello > World";
        String expected1 = "Hello &lt; World";
        String expected2 = "Hello &amp; World";
        String expected3 = "Hello &apos; World";
        String expected4 = "Hello &quot; World";
        String expected5 = "Hello &gt; World";
        
        assertEquals(expected1, Entities.escape(string1, Document.OutputSettings.base));
        assertEquals(expected2, Entities.escape(string2, Document.OutputSettings.base));
        assertEquals(expected3, Entities.escape(string3, Document.OutputSettings.base));
        assertEquals(expected4, Entities.escape(string4, Document.OutputSettings.base));
        assertEquals(expected5, Entities.escape(string5, Document.OutputSettings.base));
    }
    
    @Test
    public void testUnescape() {
        String string1 = "Hello &lt; World";
        String string2 = "Hello &amp; World";
        String string3 = "Hello &apos; World";
        String string4 = "Hello &quot; World";
        String string5 = "Hello &gt; World";
        String expected1 = "Hello < World";
        String expected2 = "Hello & World";
        String expected3 = "Hello ' World";
        String expected4 = "Hello \" World";
        String expected5 = "Hello > World";
        
        assertEquals(expected1, Entities.unescape(string1));
        assertEquals(expected2, Entities.unescape(string2));
        assertEquals(expected3, Entities.unescape(string3));
        assertEquals(expected4, Entities.unescape(string4));
        assertEquals(expected5, Entities.unescape(string5));
    }
}