import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        // Test a known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test an unknown named entity
        assertFalse(Entities.isNamedEntity("unknownEntity"));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Test a known named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Test an unknown named entity
        assertNull(Entities.getCharacterByName("unknownEntity"));
    }
    
    @Test
    public void testEscape() {
        // Test escaping a string with regular characters
        String escapedString = Entities.escape("Hello, world!", Document.OutputSettings.html());
        assertEquals("Hello, world!", escapedString);
        
        // Test escaping a string with special characters
        escapedString = Entities.escape("<script>alert('Hello, world!');</script>", Document.OutputSettings.html());
        assertEquals("&lt;script&gt;alert(&apos;Hello, world!&apos;);&lt;/script&gt;", escapedString);
        
        // Test escaping a string with non-ASCII characters
        escapedString = Entities.escape("你好，世界！", Document.OutputSettings.html());
        assertEquals("你好，世界！", escapedString);
    }
    
    @Test
    public void testUnescape() {
        // Test unescaping a string with regular characters
        String unescapedString = Entities.unescape("Hello, world!");
        assertEquals("Hello, world!", unescapedString);
        
        // Test unescaping a string with special escaped characters
        unescapedString = Entities.unescape("&lt;script&gt;alert(&apos;Hello, world!&apos;);&lt;/script&gt;");
        assertEquals("<script>alert('Hello, world!');</script>", unescapedString);
        
        // Test unescaping a string with named entities
        unescapedString = Entities.unescape("&#92;&#36;&#42;&#64;&#124;&#92;&#34;&#59;&#60;&#62;");
        assertEquals("\\$*@|\";<>", unescapedString);
    }
    
    @Test
    public void testUnescapeStrict() {
        // Test unescaping a string with regular characters using strict mode
        String unescapedString = Entities.unescape("Hello, world!", true);
        assertEquals("Hello, world!", unescapedString);
        
        // Test unescaping a string with special escaped characters using strict mode
        unescapedString = Entities.unescape("&lt;script&gt;alert(&apos;Hello, world!&apos;);&lt;/script&gt;", true);
        assertEquals("<script>alert('Hello, world!');</script>", unescapedString);
        
        // Test unescaping a string with named entities using strict mode
        unescapedString = Entities.unescape("&copy;", true);
        assertEquals("©", unescapedString);
        
        // Test unescaping a string with invalid entities using strict mode
        unescapedString = Entities.unescape("&invalidEntity;", true);
        assertEquals("&invalidEntity;", unescapedString);
    }
}