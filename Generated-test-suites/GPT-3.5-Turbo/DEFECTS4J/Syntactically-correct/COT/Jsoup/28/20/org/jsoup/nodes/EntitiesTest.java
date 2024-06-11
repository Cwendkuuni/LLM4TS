import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foobar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foobar"));
    }
    
    @Test
    public void testEscape() {
        String input = "This is a <test> &amp; example";
        String expectedOutput = "This is a &lt;test&gt; &amp;amp; example";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.html()));
    }
    
    @Test
    public void testUnescape() {
        String input = "This is a &lt;test&gt; &amp;amp; example";
        String expectedOutput = "This is a <test> &amp; example";
        assertEquals(expectedOutput, Entities.unescape(input));
    }
    
    @Test
    public void testUnescapeStrict() {
        String input = "This is a &lt;test&gt; &amp;amp; example";
        String expectedOutput = "This is a <test> &amp;amp; example";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }
    
}