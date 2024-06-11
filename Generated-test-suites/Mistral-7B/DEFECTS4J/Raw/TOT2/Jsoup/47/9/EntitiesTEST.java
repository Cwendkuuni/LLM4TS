import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic test
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge cases
        Assert.assertFalse(Entities.isNamedEntity(""));
        Assert.assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic test
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Edge cases
        Assert.assertFalse(Entities.isBaseNamedEntity(""));
        Assert.assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Basic test
        Assert.assertEquals('>', Entities.getCharacterByName("gt"));
        
        // Edge cases
        Assert.assertNull(Entities.getCharacterByName(""));
        Assert.assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        // Basic test
        Assert.assertEquals("&lt;hello&gt;", Entities.escape("<hello>", Entities.EscapeMode.base));
        
        // Edge cases
        Assert.assertEquals("", Entities.escape("", Entities.EscapeMode.base));
        Assert.assertEquals("&nbsp;", Entities.escape(" ", Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        // Basic test
        Assert.assertEquals("<hello>", Parser.parseHtml("<![CDATA[<hello>]]>").text());
        Assert.assertEquals("<hello>", Entities.unescape(Parser.parseHtml("<![CDATA[<hello>]]>").text()));
        
        // Edge cases
        Assert.assertEquals("", Entities.unescape(""));
        Assert.assertEquals("&nbsp;", Entities.unescape(" "));
    }

    @Test
    public void testCanEncode() {
        // Basic test
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, ' ', null));
        
        // Edge cases
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '€', null));
    }

    @Test
    public void testLoadEntities() {
        // Basic test
        Properties properties = new Properties();
        properties.setProperty("lt", "003C");
        Map<String, Character> loadedEntities = Entities.loadEntities("entities-test.properties");
        Character lt = loadedEntities.get("lt");
        Assert.assertEquals('lt', lt);
        
        // Edge cases
        Assert.assertNull(Entities.loadEntities(""));
        Assert.assertThrows(MissingResourceException.class, () -> Entities.loadEntities("invalid-entities.properties"));
    }

    @Test
    public void testToCharacterKey() {
        // Basic test
        Map<String, Character> entities = new HashMap<String, Character>();
        entities.put("lt", '>');
        Map<Character, String> toCharacterKeyMap = Entities.toCharacterKey(entities);
        String lt = toCharacterKeyMap.get('>');
        Assert.assertEquals("lt", lt);
        
        // Edge cases
        Assert.assertNull(Entities.toCharacterKey(null));
        Assert.assertThrows(NullPointerException.class, () -> Entities.toCharacterKey(new HashMap<>()));
    }
}