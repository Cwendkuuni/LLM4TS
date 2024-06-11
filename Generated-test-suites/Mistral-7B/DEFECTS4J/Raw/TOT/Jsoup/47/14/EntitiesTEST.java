import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set, but in full set
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        Character amp = Entities.getCharacterByName("amp");
        Character quot = Entities.getCharacterByName("quot");

        Assert.assertEquals('<', lt.charValue());
        Assert.assertEquals('&', amp.charValue());
        Assert.assertEquals('"', quot.charValue());
    }

    @Test
    public void testEscape() {
        String input = "<&'>\t >";
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder actual = new StringBuilder();

        Entities.escape(actual, input, settings, false, false, false);

        String expected = "&lt;&amp;&apos;&gt; &gt;";
        Assert.assertEquals(expected, actual.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&lt;&amp;&apos;&gt; &gt;";
        String expected = "<&'>\t >";

        String actual = Entities.unescape(input, false);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullInput() {
        Entities.unescape(null, false);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        char c = 'ç';
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

        boolean result = Entities.canEncode(charset, c, encoder);

        Assert.assertTrue(result);
    }

    @Test
    public void testCanEncodeWithInvalidCharset() {
        CoreCharset charset = CoreCharset.ascii;
        char c = 'ç';
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

        boolean result = Entities.canEncode(charset, c, encoder);

        Assert.assertFalse(result);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        Character entity = entities.get("nbsp");

        Assert.assertNotNull(entity);
        Assert.assertEquals(Character.forInt(160), entity.charValue());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        Map<Character, String> reversedEntities = Entities.toCharacterKey(entities);

        Character character = reversedEntities.get(Character.forInt(160));
        String name = character.toString();

        Assert.assertEquals("nbsp", name);
    }
}