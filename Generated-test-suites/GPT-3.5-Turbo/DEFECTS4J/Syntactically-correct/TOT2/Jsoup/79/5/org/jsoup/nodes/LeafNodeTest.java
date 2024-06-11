import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        assertEquals(0, leafNode.attributes().size());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        assertNull(leafNode.coreValue());
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttrAndGetExistingAttribute() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        leafNode.attr("testKey", "testValue");
        assertEquals("testValue", leafNode.attr("testKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttrWithNullKey() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        leafNode.attr(null);
    }
    
    // Additional test cases for other methods can be added here

}