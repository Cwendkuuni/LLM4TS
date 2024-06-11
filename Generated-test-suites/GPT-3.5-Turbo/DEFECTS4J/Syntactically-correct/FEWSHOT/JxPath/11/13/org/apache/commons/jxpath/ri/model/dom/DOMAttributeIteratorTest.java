package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        parent = new DOMNodePointer();
        attributeIterator = new DOMAttributeIterator(parent, new QName());
    }

    @Test
    public void testConstructor() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointerNoAttributes() {
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithAttributes() {
        Element element = new ElementStub();
        NamedNodeMap attributes = new NamedNodeMapStub();
        element.setAttributes(attributes);

        Attr attr1 = new AttrStub();
        Attr attr2 = new AttrStub();
        attributes.append(attr1);
        attributes.append(attr2);

        DOMAttributePointer expectedPointer = new DOMAttributePointer(parent, attr1);
        parent.setNode(element);

        assertEquals(expectedPointer, attributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionValid() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        assertFalse(attributeIterator.setPosition(-1));
        assertEquals(0, attributeIterator.getPosition());

        assertFalse(attributeIterator.setPosition(2));
        assertEquals(0, attributeIterator.getPosition());
    }
}

class ElementStub implements Element {

    private NamedNodeMap attributes;

    @Override
    public String getAttribute(String name) {
        return null;
    }

    @Override
    public String getNodeName() {
        return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return attributes;
    }

    @Override
    public Node appendChild(Node newChild) {
        return null;
    }

    @Override
    public Element cloneNode(boolean deep) {
        return null;
    }

    @Override
    public short getNodeType() {
        return 0;
    }

    @Override
    public void normalize() {

    }

    public void setAttributes(NamedNodeMap attributes) {
        this.attributes = attributes;
    }

    // ... other methods are not implemented ...

}

class NamedNodeMapStub implements NamedNodeMap {

    private Node[] nodes;

    @Override
    public Node item(int index) {
        return nodes[index];
    }

    @Override
    public int getLength() {
        return nodes.length;
    }

    // ... other methods are not implemented ...
}

class AttrStub implements Attr {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getNodeValue() {
        return null;
    }

    @Override
    public void setNodeValue(String nodeValue) {

    }

    // ... other methods are not implemented ...

}