import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void hasAttributes_shouldReturnTrueIfValueIsInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertFalse(leafNode.hasAttributes());

        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void attributes_shouldReturnAttributesIfValueIsInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.attributes());

        leafNode.value = new Attributes();
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void ensureAttributes_shouldCreateAttributesAndSetCoreValueIfValueIsNotInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.value = null;

        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertNotNull(leafNode.attributes());
        assertEquals(1, leafNode.attributes().size());
        assertNull(leafNode.attributes().get(leafNode.nodeName()));

        leafNode.value = "123";

        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertNotNull(leafNode.attributes());
        assertEquals(1, leafNode.attributes().size());
        assertEquals("123", leafNode.attributes().get(leafNode.nodeName()));
    }

    @Test
    public void coreValue_shouldReturnAttributeValueWithNodeName() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.coreValue());

        leafNode.value = "value";
        assertNull(leafNode.coreValue());

        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void attr_shouldReturnAttributeValueForKeyIfValueIsNotInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.value = null;

        String key = "key";

        assertNull(leafNode.attr(key));

        leafNode.value = "value";
        assertEquals(null, leafNode.attr(key));

        leafNode.attr(key, "value");
        assertEquals("value", leafNode.attr(key));
    }

    @Test
    public void attr_shouldDelegateToSuperAttrIfValueIsInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.value = new Attributes();

        String key = "key";
        String value = "value";

        leafNode.attr(key, value);

        assertTrue(((Attributes)leafNode.value).hasKey(key));
        assertEquals(value, ((Attributes)leafNode.value).get(key));
    }

    @Test
    public void hasAttr_shouldReturnTrueIfValueIsInstanceOfAttributesAndSuperHasAttrReturnsTrue() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.value = new Attributes();

        String key = "key";
        ((Attributes)leafNode.value).put(key, "value");

        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    public void removeAttr_shouldDelegateToSuperRemoveAttrIfValueIsInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.value = new Attributes();

        String key = "key";
        ((Attributes)leafNode.value).put(key, "value");

        leafNode.removeAttr(key);

        assertFalse(((Attributes)leafNode.value).hasKey(key));
    }

    @Test
    public void absUrl_shouldDelegateToSuperAbsUrlIfValueIsInstanceOfAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.value = new Attributes();

        String key = "key";
        String value = "value";
        ((Attributes)leafNode.value).put(key, value);

        assertEquals(value, leafNode.absUrl(key));
    }

    @Test
    public void baseUri_shouldReturnParentBaseUriIfHasParent() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.baseUri());

        Node parent = new Element("parent");
        ((Element)parent).setBaseUri("http://www.example.com");

        leafNode.setParent(parent);

        assertEquals("http://www.example.com", leafNode.baseUri());
    }

    @Test
    public void doSetBaseUri_shouldDoNothing() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.doSetBaseUri("http://www.example.com");
    }

    @Test
    public void childNodeSize_shouldReturnZero() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureChildNodes_shouldThrowException() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.ensureChildNodes();
    }

    private class LeafNodeTestImpl extends LeafNode {
        @Override
        protected String nodeName() {
            return "test";
        }
    }

}