import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.junit.Assert;
import org.junit.Test;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new Elements.SingleTagElement("test") {};
        Assert.assertFalse(leafNode.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("testAttribute", "testValue");
        leafNode.coreValue("testAttribute");
        Assert.assertTrue(leafNode.hasAttributes());

        Assert.assertEquals("testValue", leafNode.attributes().get("testAttribute"));
    }
}