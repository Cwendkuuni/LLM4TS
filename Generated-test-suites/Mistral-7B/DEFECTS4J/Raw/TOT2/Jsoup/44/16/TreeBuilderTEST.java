import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import static org.junit.Assert.assertTrue;

public class TreeBuilderTest {
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processStartTag("div"));
    }
}