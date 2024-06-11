import org.jsoup.parser.ParseSettings;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }
}