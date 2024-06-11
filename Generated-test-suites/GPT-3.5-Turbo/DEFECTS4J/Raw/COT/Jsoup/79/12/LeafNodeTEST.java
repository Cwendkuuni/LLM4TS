
import org.jsoup.helper.Validate;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Test when value is an instance of Attributes
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = new Attributes();
        assert leafNode.hasAttributes() == true;
        
        // Test when value is not an instance of Attributes
        leafNode.value = "Some value";
        assert leafNode.hasAttributes() == false;
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = new Attributes();
        Attributes attributes = leafNode.attributes();
        assert attributes != null;
        assert attributes instanceof Attributes;
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureAttributes();
        assert leafNode.value instanceof Attributes;
        
        leafNode.value = "Some value";
        leafNode.ensureAttributes();
        assert leafNode.value instanceof Attributes;
        assert ((Attributes) leafNode.value).size() == 1;
        assert ((Attributes) leafNode.value).get(leafNode.nodeName()).equals("Some value");
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.attr(leafNode.nodeName(), "Some value");
        assert leafNode.coreValue().equals("Some value");
    }

    @Test
    public void testCoreValue2() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.coreValue("Some value");
        assert leafNode.attr(leafNode.nodeName()).equals("Some value");
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = "Some value";
        assert leafNode.attr(leafNode.nodeName()).equals("Some value");
        
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        assert leafNode.attr("key").equals("value");
    }

    @Test
    public void testAttr2() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = "Some value";
        leafNode.attr(leafNode.nodeName(), "Updated value");
        assert leafNode.value.equals("Updated value");
        
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        assert ((Attributes) leafNode.value).containsKey("key");
        assert ((Attributes) leafNode.value).get("key").equals("value");
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assert leafNode.hasAttr("key") == false;
        
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("key", "value");
        assert leafNode.hasAttr("key") == true;
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("key", "value");
        leafNode.removeAttr("key");
        assert leafNode.hasAttr("key") == false;
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("key", "value");

        assert leafNode.absUrl("key").equals("value");
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();

        assert leafNode.baseUri().equals("");
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();

        // Test that method does not throw any exception
        leafNode.doSetBaseUri("Test baseUri");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeImpl();

        assert leafNode.childNodeSize() == 0;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeImpl();

        leafNode.ensureChildNodes();
    }
    
    private class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "testNode";
        }
    }
}
