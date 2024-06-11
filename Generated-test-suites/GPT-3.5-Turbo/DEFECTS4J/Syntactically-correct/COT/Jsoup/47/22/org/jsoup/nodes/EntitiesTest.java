
import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Test case for basic functionality
        String escapedString = Entities.escape("<tag> & \"", Document.OutputSettings.html());
        assertEquals("&lt;tag&gt; &amp; &quot;", escapedString);
        
        // Test case for normalizing white space
        escapedString = Entities.escape("   abc   ", Document.OutputSettings.html(), false, true, true);
        assertEquals("abc", escapedString);
        
        // Test case for stripLeadingWhite option
        escapedString = Entities.escape("   abc   ", Document.OutputSettings.html(), false, false, true);
        assertEquals(" abc   ", escapedString);
    }

    @Test
    public void testUnescape() {
        // Test case for unescaping
        String unescapedString = Entities.unescape("&lt;tag&gt; &amp; &quot;");
        assertEquals("<tag> & \"", unescapedString);

        // Test case for strict unescaping
        unescapedString = Entities.unescape("&lt;tag&gt; &amp; &quot;", true);
        assertEquals("<tag> & \"", unescapedString);
    }

}
