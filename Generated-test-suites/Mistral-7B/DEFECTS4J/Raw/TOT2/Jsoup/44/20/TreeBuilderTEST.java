import org.jsoup.parser.TreeBuilder;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.processStartTag("div", new Attributes());
        Validate.assertTrue(treeBuilder.process(treeBuilder.start));
    }
}