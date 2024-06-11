package org.junit.tests.jxpath.ri.model.jdom;

import static org.junit.Assert.*;import org.apache.commons.jxpath.ri.NodeIterator;import org.apache.commons.jxpath.ri.NodePointer;import org.apache.commons.jxpath.ri.QName;import org.jdom.Element;import org.junit.*;import static org.junit.Assert.assertNotNull;

public class JDOMAttributeIteratorTest {

    @BeforeClass
    public static void beforeClass() {
        // Set up any static initializations or pre-test configurations here
    }

    @AfterClass
    public static void afterClass() {
        // Teardown any static resources or post-test configurations here
    }

    @Before
    public void before() {
        // Set up any test-specific initializations here
    }

    @After
    public void after() {
        // Teardown any test-specific resources here
    }

    @Test
    public void testJDOMAttributeIteratorConstructor() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    // Test empty list of attributes
    @Test
    public void testJDOMAttributeIteratorEmptyList() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assert (iterator.attributes.isEmpty());
    }

    // Test null parent NodePointer
    @Test(expected = NullPointerException.class)
    public void testJDOMAttributeIteratorNullParent() {
        NodePointer parent = null;
        QName name = new QName("test:attribute");
        new JDOMAttributeIterator(parent, name);
    }

    // Test null QName
    @Test(expected = NullPointerException.class)
    public void testJDOMAttributeIteratorNullQName() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = null;
        new JDOMAttributeIterator(parent, name);
    }

    // Test non-Element NodePointer
    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorNonElementNodePointer() {
        NodePointer parent = new NodePointer(new Text("root"));
        QName name = new QName("test:attribute");
        new JDOMAttributeIterator(parent, name);
    }

    // Test attribute with the same name but different namespaces
    @Test
    public void testJDOMAttributeIteratorDifferentNamespaces() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name1 = new QName("test:attribute");
        QName name2 = new QName("test2:attribute");
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, name2);
        assert (iterator1.getNodePointer().getNode().equals(iterator2.getNodePointer().getNode()));
        assert (iterator1.getNodePointer().getAttribute().getName().equals(iterator2.getNodePointer().getAttribute().getName()));
        assert (iterator1.getNodePointer().getAttribute().getNamespace().equals(iterator1.getNodePointer().getAttribute().getNamespace()));
        assert (iterator1.getNodePointer().getAttribute().getNamespace().equals(iterator2.getNodePointer().getAttribute().getNamespace()) != true);
    }

    // Test out-of-bounds position
    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIteratorOutOfBoundsPosition() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(iterator.attributes.size() + 1);
    }

    // Test illegal position
    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorIllegalPosition() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(-1);
    }
}