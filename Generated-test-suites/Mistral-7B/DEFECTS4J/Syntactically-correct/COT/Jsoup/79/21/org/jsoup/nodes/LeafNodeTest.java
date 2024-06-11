import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        assertFalse(leafNode.hasAttributes());

        Element element = new Element("html");
        element.attr("class", "test");
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        assertNull(leafNode.attributes());

        Element element = new Element("html");
        element.attr("class", "test");
        Attributes attributes = element.attributes();
        assertEquals("class", attributes.getKey(0));
        assertEquals("test", attributes.getValue(0));
    }

    @Test
    public void testEnsureAttributes() {
        TextNode textNode = new TextNode("Hello World");
        textNode.ensureAttributes();
        assertNull(textNode.value);

        Element element = new Element("html");
        element.attr("class", "test");
        textNode = new TextNode("Testing");;
        textNode.ensureAttributes();
        assertEquals("Testing", textNode.coreValue());
        assertEquals("Testing", textNode.attr("nodeName"));
    }

    @Test
    public void testCoreValue() {
        TextNode textNode = new TextNode("Hello World");
        assertEquals("Hello World", textNode.coreValue());

        Element element = new Element("html");
        element.attr("name", "value");
        TextNode textNode2 = new TextNode("Testing");
        textNode2.coreValue("Testing2");
        assertEquals("Testing2", textNode2.coreValue());
        assertEquals("Testing", textNode2.attr("nodeName"));
    }

    @Test
    public void testCoreValueSetter() {
        TextNode textNode = new TextNode("Hello World");
        textNode.coreValue("Testing");
        assertEquals("Testing", textNode.coreValue());
        assertEquals("Testing", textNode.attr("nodeName"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttrNullKey() {
        TextNode textNode = new TextNode("Hello World");
        textNode.attr(null, "value");
    }

    @Test
    public void testAttrEmptyString() {
        TextNode textNode = new TextNode("Hello World");
        textNode.attr("name", "");
        assertEquals("", textNode.attr("name"));
    }

    @Test
    public void testAttrSameKeyTwice() {
        Element element = new Element("html");
        element.attr("class", "test");
        element.attr("class", "test2");
        List<Map.Entry<String, String>> entries = element.attributes().asList();
        assertEquals(2, entries.size());
        String firstClass = entries.get(0).getValue();
        String secondClass = entries.get(1).getValue();
        assertNotSame(firstClass, secondClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrDuplicateKey() {
        Element element = new Element("html");
        element.attr("name", "value1");
        element.attr("name", "value2");
    }

    @Test
    public void testAttrValueNull() {
        TextNode textNode = new TextNode("Hello World");
        textNode.attr("name", null);
        assertNull(textNode.attr("name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrValueEmptyString() {
        TextNode textNode = new TextNode("Hello World");
        textNode.attr("name", "");
    }

    @Test
    public void testAttrValueEqualsNode() {
        TextNode textNode = new TextNode("Hello World");
        Element element = new Element("html");
        textNode.attr("name", element);
        assertNotNull(textNode.attr("name"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasChildNodes() {
        TextNode textNode = new TextNode("Hello World");
        textNode.childNodeSize();
    }
}