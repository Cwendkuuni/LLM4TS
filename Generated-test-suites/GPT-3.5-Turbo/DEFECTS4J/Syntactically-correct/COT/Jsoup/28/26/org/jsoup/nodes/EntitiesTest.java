
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 0x00026), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 0x00027), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf((char) 0x0003E), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf((char) 0x00022), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div onmouseover=\"alert(document.cookie);\">Hello, world!</div>", Entities.EscapeMode.base);
        assertEquals("&lt;div onmouseover=\"alert(document.cookie);\"&gt;Hello, world!&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div onmouseover=\"alert(document.cookie);\"&gt;Hello, world!&lt;/div&gt;");
        assertEquals("<div onmouseover=\"alert(document.cookie);\">Hello, world!</div>", unescapedString);
    }
    
    @Test
    public void testUnescapeWithStrict() {
        String unescapedString = Entities.unescape("&lt;div onmouseover=\"alert(document.cookie);\"&gt;Hello, world!&lt;/div&gt;", true);
        assertEquals("<div onmouseover=\"alert(document.cookie);\">Hello, world!</div>", unescapedString);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here
}

