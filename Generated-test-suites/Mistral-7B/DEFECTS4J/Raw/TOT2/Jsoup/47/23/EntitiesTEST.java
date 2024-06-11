import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullMap = Entities.full;
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseMap = Entities.base;
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("x"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullMap = Entities.full;
        Character lt = Entities.getCharacterByName("lt");
        assertEquals('<', lt.charValue());
        Character quot = Entities.getCharacterByName("quot");
        assertEquals('"', quot.charValue());
    }

    @Test
    public void testEscapeMethodBasicUseCase() {
        String string = "This is a test string & with some <special> characters & also with &amp; & &quot; and '&apos;.";
        String expected = "This is a test string &amp; &lt;special&gt; &amp; &quot; and &apos;";
        String escapedString = Entities.escape(string, Document.OutputSettings.xhtml());
        assertEquals(expected, escapedString);
    }

    @Test
    public void testEscapeMethodEdgeCases() {
        String string = "This is a test string <script> with some &script& special characters &lt;script&gt; &amp; &quot; and &apos; in it.";
        String expected = "This is a test string &amp; &lt;script&gt; &amp; &lt;script&gt; &amp; &quot; and &apos;";
        String escapedString = Entities.escape(string, Document.OutputSettings.xhtml());
        assertEquals(expected, escapedString);

        string = "This is a test string & with some <script> special characters &lt;script&gt; &amp; &quot; and &apos; in it.";
        expected = "This is a test string &amp; &lt;script&gt;&gt; &amp; &lt;script&gt;&gt; &amp; &quot; and &apos;";
        escapedString = Entities.escape(string, Document.OutputSettings.html());
        assertEquals(expected, escapedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodNullInput() {
        String nullString = null;
        Entities.escape(nullString, Document.OutputSettings.xhtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityNullInput() {
        String nullString = null;
        Entities.isNamedEntity(nullString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameNullInput() {
        String nullString = null;
        Entities.getCharacterByName(nullString);
    }
}