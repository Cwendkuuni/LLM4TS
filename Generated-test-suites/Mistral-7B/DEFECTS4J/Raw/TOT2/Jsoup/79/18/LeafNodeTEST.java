import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

import java.util.EmptyStackException;

public class LeafNodeTest {

    @Test
    public void testHasAttributes_Normal() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_NullValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = null;
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_NonAttributesValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Object() {};
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_Normal() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testKey", "testValue");
            }
        };
        Attributes attributes = leafNode.attributes();
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributes_NoAttributes() {
        LeafNode leafNode = new LeafNode();
        leafNode.attributes();
    }

    @Test
    public void testCoreValue_Normal() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_NullValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = null;
            }
        };
        assertEquals(EmptyString, leafNode.coreValue());
    }

    @Test
    public void testCoreValue_AttributesValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertEquals(EmptyString, leafNode.coreValue());
    }

    @Test
    public void testCoreValue_SetValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };
        leafNode.coreValue("newTestValue");
        assertEquals("newTestValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_SetValueToNull() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "testValue";
            }
        };
        leafNode.coreValue(null);
        assertEquals(EmptyString, leafNode.coreValue());
    }
}