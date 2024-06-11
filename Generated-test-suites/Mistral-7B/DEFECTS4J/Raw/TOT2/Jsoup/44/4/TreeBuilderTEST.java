import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    @Test
    public void testProcessStartTagWithValidName() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking());
        boolean result = treeBuilder.processStartTag("html");
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithNullName() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking());
        treeBuilder.processStartTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTagWithEmptyName() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking());
        treeBuilder.processStartTag("");
    }
}