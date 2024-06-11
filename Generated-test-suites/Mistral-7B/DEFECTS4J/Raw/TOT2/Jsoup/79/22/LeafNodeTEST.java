import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertTrue(leafNode.hasAttributes());

        LeafNode leafNodeWithoutAttributes = new LeafNode();
        assertFalse(leafNodeWithoutAttributes.hasAttributes());
    }
}