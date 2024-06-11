import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test case for isNamedEntity() method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // Testing with a known named entity
        assertFalse(Entities.isNamedEntity("abc")); // Testing with an unknown named entity
    }

    // Test case for getCharacterByName() method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // Testing with a known named entity
        assertNull(Entities.getCharacterByName("abc")); // Testing with an unknown named entity
    }

    // Test case for escape() method
    @Test
    public void testEscape() {
        String input = "<div>Test &amp; Test</div>";
        String expectedOutput = "&lt;div&gt;Test &amp;amp; Test&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.xhtml()));
    }

    // Test case for unescape() method
    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Test &amp;amp; Test&lt;/div&gt;";
        String expectedOutput = "<div>Test & Test</div>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    // Test case for unescape() method with strict flag
    @Test
    public void testUnescapeWithStrictFlag() {
        String input = "&lt;div&gt;Test &amp&amp;;; Test&lt;/div&gt;";
        String expectedOutput = "&lt;div&gt;Test &amp&amp;; Test&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    // Test case for loadEntities() method
    @Test
    public void testLoadEntities() {
        assertNotNull(Entities.loadEntities("entities-base.properties")); // Testing with a valid file
        // Testing with an invalid file (should throw an exception)
        assertThrows(MissingResourceException.class, () -> Entities.loadEntities("invalid-file.properties"));
    }

    // Test case for toCharacterKey() method
    @Test
    public void testToCharacterKey() {
        assertNotNull(Entities.toCharacterKey(Entities.loadEntities("entities-base.properties"))); // Testing with a valid map
    }
}