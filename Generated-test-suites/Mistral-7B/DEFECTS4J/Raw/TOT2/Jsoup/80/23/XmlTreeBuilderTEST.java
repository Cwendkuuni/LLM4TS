import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
}