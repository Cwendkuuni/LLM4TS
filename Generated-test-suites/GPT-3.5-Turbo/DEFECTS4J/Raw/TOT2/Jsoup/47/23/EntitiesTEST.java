import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fakeEntity"));
    }

    @Test
    public void testEscape() {
        String input = "This is a <test> string &amp;";
        String expectedOutput = "This is a &lt;test&gt; string &amp;amp;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String escapedString = "This is a &lt;test&gt; string &amp;amp;";
        String expectedOutput = "This is a <test> string &amp;";
        assertEquals(expectedOutput, Entities.unescape(escapedString));
    }

    @Test
    public void testEscapeEdgeCases() {
        String input = "This is a <test> string &amp;";
        String expectedOutput = "This is a &lt;test&gt; string &amp;amp;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
        
        String emptyString = "";
        assertEquals("", Entities.escape(emptyString, new Document.OutputSettings()));
        
        String stringWithNoSpecialChars = "Hello World";
        assertEquals("Hello World", Entities.escape(stringWithNoSpecialChars, new Document.OutputSettings()));
        
        String stringWithAllSpecialChars = "&&&&<<>>'\"";
        assertEquals("&amp;&amp;&amp;&amp;&lt;&lt;&gt;&gt;'\"", Entities.escape(stringWithAllSpecialChars, new Document.OutputSettings()));
        
    }

    @Test
    public void testUnescapeEdgeCases() {
        String escapedString = "This is a &lt;test&gt; string &amp;amp;";
        String expectedOutput = "This is a <test> string &amp;";
        assertEquals(expectedOutput, Entities.unescape(escapedString));
        
        String emptyString = "";
        assertEquals("", Entities.unescape(emptyString));
        
        String stringWithNoSpecialChars = "Hello World";
        assertEquals("Hello World", Entities.unescape(stringWithNoSpecialChars));
        
        String stringWithAllSpecialChars = "&amp;&amp;&amp;&amp;&lt;&lt;&gt;&gt;'\"";
        assertEquals("&&&&<<>>'\"", Entities.unescape(stringWithAllSpecialChars));
    }
}