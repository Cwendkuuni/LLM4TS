
import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to an Attributes object
        leafNode.value = new Attributes();

        // Test hasAttributes method
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to an Attributes object
        leafNode.value = new Attributes();

        // Test attributes method
        assertNotNull(leafNode.attributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testCoreValue() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to a string
        leafNode.value = "testValue";

        // Test coreValue method
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValueWithAttributes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to an Attributes object
        leafNode.value = new Attributes();

        // Test coreValue method with attributes
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to a string
        leafNode.coreValue("testValue");

        // Test coreValue method after setting value
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to a string
        leafNode.value = "testValue";

        // Test attr method
        assertEquals("testValue", leafNode.attr("testNode"));
    }

    @Test
    public void testAttrWithAttributes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to an Attributes object
        leafNode.value = new Attributes();

        // Test attr method with attributes
        assertNull(leafNode.attr("testNode"));
    }

    @Test
    public void testAttrWithDifferentKey() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to a string
        leafNode.value = "testValue";

        // Test attr method with different key
        assertNull(leafNode.attr("otherKey"));
    }

    @Test
    public void testAttrWithAttributesAndDifferentKey() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Set value to an Attributes object
        leafNode.value = new Attributes();

        // Test attr method with attributes and different key
        assertNull(leafNode.attr("otherKey"));
    }

    @Test
    public void testAttrSetter() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test attr setter method
        leafNode.attr("testKey", "testValue");
        assertEquals("testValue", leafNode.attr("testKey"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // Create a LeafNode instance
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        // Test ensureChildNodes method
        leafNode.ensureChildNodes();
    }

}
