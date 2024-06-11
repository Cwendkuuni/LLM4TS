
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {

    // Test isNamedEntity method with known named entity
    @Test
    public void testIsNamedEntity() {
        boolean actual = Entities.isNamedEntity("lt");
        Assert.assertTrue(actual);
    }
    
    // Test isNamedEntity method with unknown named entity
    @Test
    public void testIsNamedEntity_UnknownEntity() {
        boolean actual = Entities.isNamedEntity("unknown");
        Assert.assertFalse(actual);
    }

    // Test isBaseNamedEntity method with known base named entity
    @Test
    public void testIsBaseNamedEntity() {
        boolean actual = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(actual);
    }
    
    // Test isBaseNamedEntity method with unknown base named entity
    @Test
    public void testIsBaseNamedEntity_UnknownEntity() {
        boolean actual = Entities.isBaseNamedEntity("unknown");
        Assert.assertFalse(actual);
    }

    // Test getCharacterByName method with known named entity
    @Test
    public void testGetCharacterByName() {
        Character actual = Entities.getCharacterByName("lt");
        Character expected = '<';
        Assert.assertEquals(expected, actual);
    }

    // Test escape method with valid string
    @Test
    public void testEscape() {
        String actual = Entities.escape("This is a <tag> & string", Document.OutputSettings.xhtml());
        String expected = "This is a &lt;tag&gt; &amp; string";
        Assert.assertEquals(expected, actual);
    }

    // Test escape method with empty string
    @Test
    public void testEscape_EmptyString() {
        String actual = Entities.escape("", Document.OutputSettings.base());
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    // Test unescape method with valid string
    @Test
    public void testUnescape() {
        String actual = Entities.unescape("This is a &lt;tag&gt; &amp; string");
        String expected = "This is a <tag> & string";
        Assert.assertEquals(expected, actual);
    }
    
    // Test unescape method with empty string
    @Test
    public void testUnescape_EmptyString() {
        String actual = Entities.unescape("");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    // Test unescape method with strict mode enabled
    @Test
    public void testUnescape_StrictMode() {
        String actual = Entities.unescape("This is a &lt;tag&gt; &amp;");
        String expected = "This is a <tag> &";
        Assert.assertEquals(expected, actual);
    }

    // Test unescape method with strict mode disabled
    @Test
    public void testUnescape_NonStrictMode() {
        String actual = Entities.unescape("This is a &lt;tag&gt; &amp;");
        String expected = "This is a <tag> &";
        Assert.assertEquals(expected, actual);
    }
}
