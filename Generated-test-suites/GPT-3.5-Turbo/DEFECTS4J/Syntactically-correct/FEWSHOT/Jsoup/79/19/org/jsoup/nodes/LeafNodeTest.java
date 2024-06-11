
package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        leafNode.ensureAttributes();
        assertFalse(leafNode.hasAttributes());
        
        leafNode.value = "value";
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes().size() == 1);
        assertTrue(leafNode.attributes().get("test").equals("value"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertNull(leafNode.coreValue());
        
        leafNode.attr("test", "value");
        assertTrue(leafNode.coreValue().equals("value"));
        
        leafNode.coreValue("updated value");
        assertTrue(leafNode.coreValue().equals("updated value"));
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertTrue(leafNode.attr("key").equals(leafNode.nodeName()));
        
        leafNode.value = new Attributes();
        assertTrue(leafNode.attr("key").equals(""));
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.attr("key").equals("value"));
    }

    @Test
    public void testNodeMethods() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertNull(leafNode.baseUri());
        assertFalse(leafNode.hasParent());
        assertTrue(leafNode.childNodeSize() == 0);
        
        leafNode.doSetBaseUri("baseUri");
        assertNull(leafNode.baseUri());
        
        try {
            leafNode.ensureChildNodes();
            fail();
       } catch (UnsupportedOperationException e) {
            assertTrue(true);
       }
    }
}
