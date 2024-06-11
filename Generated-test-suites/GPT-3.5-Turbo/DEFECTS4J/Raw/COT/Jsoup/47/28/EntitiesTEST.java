
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String string = "This <tag> contains &lt; and &gt;";
        String expected = "This &lt;tag&gt; contains &amp;lt; and &amp;gt;";
        Assert.assertEquals(expected, Entities.escape(string));
    }

    @Test
    public void testUnescape() {
        String string = "This &lt;tag&gt; contains &amp;lt; and &amp;gt;";
        String expected = "This <tag> contains &lt; and &gt;";
        Assert.assertEquals(expected, Entities.unescape(string));
    }

    @Test
    public void testUnescapeStrict() {
        String string = "This &lt;tag&gt; contains &amp;lt; and &amp;gt;;";
        String expected = "This <tag> contains &lt; and &gt;;";
        Assert.assertEquals(expected, Entities.unescape(string, true));
    }
}

