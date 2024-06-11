import org.jsoup.parser.TreeBuilder;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class TreeBuilderTest {
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.processStartTag("div");
        Element currentElement = treeBuilder.currentElement();
        Validate.notNull(currentElement);
        assertNotNull(currentElement);
    }
}