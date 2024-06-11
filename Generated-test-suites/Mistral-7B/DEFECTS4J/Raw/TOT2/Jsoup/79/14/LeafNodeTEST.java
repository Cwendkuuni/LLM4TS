import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode();
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new LeafNode();
        Attributes attributes = new Attributes();
        leafNodeWithAttributes.value = attributes;
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }
}