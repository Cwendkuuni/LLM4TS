import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic functionality test
        assertTrue(isNamedEntity("lt"));
        assertFalse(isNamedEntity("gt"));

        // Edge cases and exception handling
        try {
            isNamedEntity(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            isNamedEntity("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetCharacterByName() {
        // Basic functionality test
        assertEquals('<', getCharacterByName("lt"));
        assertEquals('&', getCharacterByName("amp"));

        // Edge cases and exception handling
        try {
            getCharacterByName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            getCharacterByName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            getCharacterByName("unknownEntity");
            fail("Expected Exception");
        } catch (IllegalArgumentException e) {
            // Expected, but with different message
            String message = e.getMessage();
            assertTrue(message.contains("unknownEntity is not a known named entity"));
        }
    }

    @Test
    public void testEscape() {
        // Basic functionality test
        assertEquals("&lt; &gt; &amp; &apos; &quot;", escape("<<& &>' && '\\& \"", EscapeMode.base));

        // Edge cases and exception handling
        try {
            escape(null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            escape("", null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            escape("&", null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testUnescape() {
        // Basic functionality test
        assertEquals("<<& &gt; &amp; '&apos; \"&quot;\r\n", unescape("&lt;&gt;&amp;apos;&quot;\\r\\n", false));
        assertEquals("<<&gt;&amp;apos;&quot;", unescape("&lt;&gt;&amp;apos;&quot;", true));

        // Edge cases and exception handling
        try {
            unescape(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            unescape("", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            unescape("", true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            unescape("&", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            unescape("&foo;", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsNamedEntityWithXhtmlMode() {
        // Basic functionality test
        assertFalse(isNamedEntity("lt", EscapeMode.xhtml));
        assertTrue(isNamedEntity("quot", EscapeMode.xhtml));

        // Edge cases and exception handling
        try {
            isNamedEntity(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            isNamedEntity("", EscapeMode.xhtml);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            isNamedEntity("foo", EscapeMode.xhtml);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, but with different message
            String message = e.getMessage();
            assertTrue(message.contains("foo is not a known named entity in the xhtml entity set"));
        }
    }

    @Test
    public void testGetCharacterByNameWithXhtmlMode() {
        // Basic functionality test
        assertEquals('<', getCharacterByName("lt", EscapeMode.xhtml));
        assertEquals('&', getCharacterByName("amp", EscapeMode.xhtml));

        // Edge cases and exception handling
        try {
            getCharacterByName(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            getCharacterByName("", EscapeMode.xhtml);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            getCharacterByName("foo", EscapeMode.xhtml);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, but with different message
            String message = e.getMessage();
            assertTrue(message.contains("foo is not a known named entity in the xhtml entity set"));
        }
    }

    @Test
    public void testLoadEntities() {
        // Basic functionality test
        Map<String, Character> entities = loadEntities("entities-xhtml.properties");
        assertEquals(5, entities.size());
        assertEquals('&', entities.get("amp"));
        assertEquals('>', entities.get("gt"));
        // ... and so on

        // Edge cases and exception handling
        try {
            loadEntities(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            loadEntities("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}