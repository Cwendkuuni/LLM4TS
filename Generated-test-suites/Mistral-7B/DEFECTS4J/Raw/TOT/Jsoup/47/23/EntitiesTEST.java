import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

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
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // quot is in extended set
        Assert.assertFalse(Entities.isBaseNamedEntity("gt")); // gt is not in base set
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "&lt;foo&amp;bar&quot;";
        String expectedOutput = "&lt;foo&amp;amp;amp;bar&quot;"; // entity encoded input
        String output = Entities.escape(input, Document.OutputSettings.base());
        Assert.assertEquals(expectedOutput, output);

        String encodedOutput = "&#x3C;foo&#x26;amp;&#x26;amp;amp;bar&#x22;"; // character encoded input
        String inputFromEncoded = Entities.unescape(encodedOutput, false);
        Assert.assertEquals(input, inputFromEncoded);

        String strictEncodedOutput = "&lt;foo&amp;amp;amp;bar&quot;"; // strict entity encoded input
        String inputFromStrictEncoded = Entities.unescape(strictEncodedOutput, true);
        Assert.assertEquals(input, inputFromStrictEncoded);
    }

    @Test
    public void testCanEncode() {
        // Test with common ASCII characters
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, ' ', null));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, '!', null));

        // Test with surrogate pairs and non-ASCII characters
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\uD800', null));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\uDC00', null));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\uD800', null));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\uDC00', null));
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingEntitiesFile() {
        Entities.loadEntities("non-existent-file.properties");
    }
}