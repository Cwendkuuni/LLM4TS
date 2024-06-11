import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_Positive() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue("Failed: Expected true, but got false", result);
    }

    @Test
    public void testIsNamedEntity_Negative() {
        boolean result = Entities.isNamedEntity("xyz");
        Assert.assertFalse("Failed: Expected false, but got true", result);
    }

    @Test
    public void testIsBaseNamedEntity_Positive() {
        boolean result = Entities.isBaseNamedEntity("amp");
        Assert.assertTrue("Failed: Expected true, but got false", result);
    }

    @Test
    public void testIsBaseNamedEntity_Negative() {
        boolean result = Entities.isBaseNamedEntity("xyz");
        Assert.assertFalse("Failed: Expected false, but got true", result);
    }

    @Test
    public void testGetCharacterByName_Positive() {
        Character result = Entities.getCharacterByName("amp");
        Character expected = '&';
        Assert.assertEquals("Failed: Expected character '&', but got " + result, expected, result);
    }

    @Test
    public void testGetCharacterByName_Negative() {
        Character result = Entities.getCharacterByName("xyz");
        Assert.assertNull("Failed: Expected null, but got " + result, result);
    }

    @Test
    public void testEscape_InternalCharacters() {
        String input = "<script>alert('Hello World!');</script>";
        String result = Entities.escape(input, null);
        String expected = "&lt;script&gt;alert(&apos;Hello World!&apos;);&lt;/script&gt;";
        Assert.assertEquals("Failed: Expected escaped string '" + expected + "', but got '" + result + "'", expected, result);
    }

    @Test
    public void testEscape_ExternalCharacters() {
        String input = "©";
        Entities.EscapeMode mode = Entities.EscapeMode.xhtml;
        String result = Entities.escape(input, mode.getMap());
        String expected = "&copy;";
        Assert.assertEquals("Failed: Expected escaped string '" + expected + "', but got '" + result + "'", expected, result);
    }

    @Test
    public void testUnescape_NormalCase() {
        String input = "&lt;script&gt;alert('Hello World!');&lt;/script&gt;";
        String result = Entities.unescape(input);
        String expected = "<script>alert('Hello World!');</script>";
        Assert.assertEquals("Failed: Expected unescaped string '" + expected + "', but got '" + result + "'", expected, result);
    }

    @Test
    public void testUnescape_StrictCase() {
        String input = "&copy"; // Missing trailing ';'
        String result = Entities.unescape(input, true);
        String expected = "&copy"; // Expected to remain unchanged
        Assert.assertEquals("Failed: Expected unescaped string '" + expected + "', but got '" + result + "'", expected, result);
    }

    @Test
    public void testUnescape_LowerCaseEntity() {
        String input = "&amp;";
        String result = Entities.unescape(input);
        String expected = "&";
        Assert.assertEquals("Failed: Expected unescaped string '" + expected + "', but got '" + result + "'", expected, result);
    }

    @Test
    public void testUnescape_InvalidEntity() {
        String input = "&xyz;";
        String result = Entities.unescape(input);
        String expected = "&xyz;"; // Expected to remain unchanged
        Assert.assertEquals("Failed: Expected unescaped string '" + expected + "', but got '" + result + "'", expected, result);
    }

}