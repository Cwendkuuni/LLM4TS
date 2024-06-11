
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        // Arrange
        ParseSettings settings = new ParseSettings(true, false);

        // Act
        boolean result = settings.preserveTagCase();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testPreserveAttributeCase() {
        // Arrange
        ParseSettings settings = new ParseSettings(false, true);

        // Act
        boolean result = settings.preserveAttributeCase();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testNormalizeTag() {
        // Arrange
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "h1";

        // Act
        String result = settings.normalizeTag(tagName);

        // Assert
        Assert.assertEquals("h1", result);
    }

    @Test
    public void testNormalizeAttribute() {
        // Arrange
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "HREF";

        // Act
        String result = settings.normalizeAttribute(attributeName);

        // Assert
        Assert.assertEquals("href", result);
    }

    @Test
    public void testNormalizeAttributes() {
        // Arrange
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "myId");
        attributes.put("CLASS", "myClass");

        // Act
        Attributes result = settings.normalizeAttributes(attributes);

        // Assert
        Assert.assertEquals("myId", result.get("id"));
        Assert.assertEquals("myClass", result.get("class"));
    }
}

