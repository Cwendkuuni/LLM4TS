package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;


public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private List<Attr> attributes;
    private int position;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("test");
        attributes = new ArrayList<>();
        position = 0;
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        assertNull(iterator.getNodePointer());
        attributes.add(createAttribute());
        iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        position = 1;
        iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        position = 2;
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        position = 1;
        assertEquals(1, iterator.getPosition());
        position = 2;
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(3));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(3));

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(3));
    }

    private Attr createAttribute() {
        return new Attr() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public void setValue(String value) {
            }

            @Override
            public boolean getSpecified() {
                return false;
            }

            @Override
            public Element getOwnerElement() {
                return null;
            }

            @Override
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public boolean isId() {
                return false;
            }
        };
    }
}