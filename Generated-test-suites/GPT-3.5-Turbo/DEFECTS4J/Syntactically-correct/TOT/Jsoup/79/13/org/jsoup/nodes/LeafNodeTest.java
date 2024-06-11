import org.jsoup.nodes.LeafNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void testHasAttributes_WhenValueIsNull_ReturnsFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_WhenValueIsString_ReturnsFalse() {
        leafNode.value = "testValue";
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_WhenValueIsAttributes_ReturnsTrue() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_WhenValueIsNull_ReturnsEmptyAttributes() {
        assertEquals(0, leafNode.attributes().size());
    }

    @Test
    public void testAttributes_WhenValueIsString_ReturnsAttributesWithSingleEntry() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attributes().get("testNode"));
    }

    @Test
    public void testAttributes_WhenValueIsAttributes_ReturnsSameAttributes() {
        leafNode.value = new Attributes();
        assertEquals(leafNode.value, leafNode.attributes());
    }

    // Other test cases for remaining methods...

}