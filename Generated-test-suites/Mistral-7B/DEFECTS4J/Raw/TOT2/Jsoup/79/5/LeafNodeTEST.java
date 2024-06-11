import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new LeafNode() {
            {
                value = "Test Value";
            }
        };
        assertFalse(node.hasAttributes());

        LeafNode nodeWithAttributes = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertTrue(nodeWithAttributes.hasAttributes());
    }
}