import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isNamedEntity("lt"));
        Assert.assertTrue(entities.isNamedEntity("amp"));
        Assert.assertFalse(entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Assert.assertEquals('<', entities.getCharacterByName("lt"));
        Assert.assertEquals('&', entities.getCharacterByName("amp"));
        Assert.assertNull(entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&amp;<lt>Hello & World&gt; <>&\"'";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "&amp;amp; &amp;lt;Hello &amp;amp; World&amp;gt; &amp;gt; &amp;amp; &amp;quot; &amp;#39;";
        String output = entities.escape(input, settings);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;amp; &amp;lt;Hello &amp;amp; World&amp;gt; &amp;gt; &amp;amp; &amp;quot; &amp;#39;";
        String expectedOutput = "&amp;<lt>Hello &amp;World&gt; > &amp;amp; \" '";
        String output = entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingEntitiesFile() {
        Entities entities = new Entities();
        entities.loadEntities("non-existent-file.properties");
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        Assert.assertTrue(entities.canEncode(Entities.CoreCharset.utf, 'π'));
        Assert.assertFalse(entities.canEncode(Entities.CoreCharset.ascii, 'π'));
    }
}