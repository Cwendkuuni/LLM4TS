import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        Document doc = createTestDocument();
        parentContext = new EvalContext(null, doc.getDocumentElement());
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContext_EmptyDocument() {
        attributeContext.nextNode();
        assertThat(attributeContext.getCurrentNodePointer(), nullValue());
    }

    @Test
    public void testAttributeContext_ValidAttribute() {
        Element element = (Element) parentContext.getCurrentNodePointer().getNode();
        element.setAttribute("test", "value");

        attributeContext.nextNode();
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertThat(nodePointer, instanceOf(NodePointer.class));
        assertThat(nodePointer.getNode().getNodeName().getNodeValue(), is("test"));
        assertThat(nodePointer.getNode().getNodeValue(), is("value"));
    }

    @Test
    public void testAttributeContext_MultipleAttributes() {
        Element element = (Element) parentContext.getCurrentNodePointer().getNode();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        attributeContext.nextNode(); // test1
        attributeContext.nextNode(); // test2

        NodePointer nodePointer1 = attributeContext.getCurrentNodePointer();
        NodePointer nodePointer2 = attributeContext.getCurrentNodePointer();

        assertThat(nodePointer1, instanceOf(NodePointer.class));
        assertThat(nodePointer1.getNode().getNodeName().getNodeValue(), is("test1"));
        assertThat(nodePointer1.getNode().getNodeValue(), is("value1"));

        assertThat(nodePointer2, instanceOf(NodePointer.class));
        assertThat(nodePointer2.getNode().getNodeName().getNodeValue(), is("test2"));
        assertThat(nodePointer2.getNode().getNodeValue(), is("value2"));
    }

    @Test
    public void testAttributeContext_AttributeWithSameName() {
        Element element = (Element) parentContext.getCurrentNodePointer().getNode();
        element.setAttribute("test", "value1");
        element.setAttribute("test", "value2");

        attributeContext.nextNode();
        NodePointer nodePointer1 = attributeContext.getCurrentNodePointer();
        attributeContext.nextNode();
        NodePointer nodePointer2 = attributeContext.getCurrentNodePointer();

        assertThat(nodePointer1, instanceOf(NodePointer.class));
        assertThat(nodePointer1.getNode().getNodeName().getNodeValue(), is("test"));
        assertThat(nodePointer1.getNode().getNodeValue(), is("value1"));

        assertThat(nodePointer2, instanceOf(NodePointer.class));
        assertThat(nodePointer2.getNode().getNodeName().getNodeValue(), is("test"));
        assertThat(nodePointer2.getNode().getNodeValue(), is("value2"));
    }

    @Test
    public void testAttributeContext_EmptyAttributeNameTest() {
        Element element = (Element) parentContext.getCurrentNodePointer().getNode();
        element.setAttribute("test", "value");

        NodeTest emptyNodeTest = new NodeNameTest(new QName(""));
        AttributeContext emptyAttributeContext = new AttributeContext(parentContext, emptyNodeTest);

        emptyAttributeContext.nextNode();
        NodePointer nodePointer = emptyAttributeContext.getCurrentNodePointer();
        assertThat(nodePointer, nullValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAttributeContext_InvalidAttributeNameTest() {
        Element element = (Element) parentContext.getCurrentNodePointer().getNode();
        element.setAttribute("test", "value");

        NodeTest invalidNodeTest = new NodeTest() {
            @Override
            public boolean matches(NodeIterator nodeIterator, int position) {
                return false;
            }
        };
        AttributeContext invalidAttributeContext = new AttributeContext(parentContext, invalidNodeTest);

        invalidAttributeContext.nextNode();
    }

    @Test
    public void testAttributeContext_EndOfDocument() {
        Element element = (Element) parentContext.getCurrentNodePointer().getNode();
        element.setAttribute("test", "value");

        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();

        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertThat(nodePointer, nullValue());
    }

    private Document createTestDocument() {
        Document doc = DocumentBuilderFactory.newInstance().newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);
        return doc;
    }
}