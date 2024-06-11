
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue("Test case failed for isNamedEntity(\"lt\")", Entities.isNamedEntity("lt"));
        assertTrue("Test case failed for isNamedEntity(\"amp\")", Entities.isNamedEntity("amp"));
        assertFalse("Test case failed for isNamedEntity(\"random\")", Entities.isNamedEntity("random"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue("Test case failed for isBaseNamedEntity(\"lt\")", Entities.isBaseNamedEntity("lt"));
        assertTrue("Test case failed for isBaseNamedEntity(\"amp\")", Entities.isBaseNamedEntity("amp"));
        assertFalse("Test case failed for isBaseNamedEntity(\"random\")", Entities.isBaseNamedEntity("random"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals("Test case failed for getCharacterByName(\"lt\")", Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals("Test case failed for getCharacterByName(\"amp\")", Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull("Test case failed for getCharacterByName(\"random\")", Entities.getCharacterByName("random"));
    }

    @Test
    public void testEscape() {
        String string = "<html>";
        String expected = "&lt;html&gt;";
        assertEquals("Test case failed for escape(\"<html>\")", expected, Entities.escape(string, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;html&gt;";
        String expected = "<html>";
        assertEquals("Test case failed for unescape(\"&lt;html&gt;\")", expected, Entities.unescape(string));
    }
}
