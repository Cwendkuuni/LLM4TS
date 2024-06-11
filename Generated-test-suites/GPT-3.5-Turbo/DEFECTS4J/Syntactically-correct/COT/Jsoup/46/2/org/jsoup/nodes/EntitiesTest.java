
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals(Character.valueOf('<'), result);
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("<html>", new Document.OutputSettings());
        Assert.assertEquals("&lt;html&gt;", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;html&gt;");
        Assert.assertEquals("<html>", result);
    }

}
