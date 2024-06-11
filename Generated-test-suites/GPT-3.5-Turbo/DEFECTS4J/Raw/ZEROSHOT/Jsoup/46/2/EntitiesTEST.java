import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("This is a <test> string", new Document.OutputSettings());
        Assert.assertEquals("This is a &lt;test&gt; string", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("This is a &lt;test&gt; string");
        Assert.assertEquals("This is a <test> string", unescaped);
    }

}