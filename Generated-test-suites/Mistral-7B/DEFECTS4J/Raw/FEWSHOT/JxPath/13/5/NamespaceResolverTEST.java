package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.cli.NodeIterator;
import org.apache.commons.cli.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorStub;
import org.apache.commons.jxpath.ri.model.NodePointerStub;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodeIterator nodeIterator;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        parentNamespaceResolver = mock(NamespaceResolver.class);
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        nodeIterator = new NodeIteratorStub();
        nodePointer = new NodePointerStub();
    }

    @Test
    public void testGetNamespaceURI_explicitlyRegisteredPrefix() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
    }

    @Test
    public void testGetNamespaceURI_contextNodeRegisteredPrefix() {
        when(nodePointer.getNamespaceURI("xsd")).thenReturn("http://www.w3.org/2001/XMLSchema");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
    }

    @Test
    public void testGetNamespaceURI_parentRegisteredPrefix() {
        when(parentNamespaceResolver.getNamespaceURI("xsd")).thenReturn("http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
    }

    @Test
    public void testGetNamespaceURI_unregisteredPrefix() {
        assertNull(namespaceResolver.getNamespaceURI("unregistered"));
    }

    @Test
    public void testGetPrefix_explicitlyRegisteredNamespaceURI() {
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testGetPrefix_contextNodeRegisteredNamespaceURI() {
        when(nodePointer.getNamespaceURI("xsd")).thenReturn("http://www.w3.org/2001/XMLSchema");
        when(nodePointer.getName().getName()).thenReturn("xsd");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testGetPrefix_parentRegisteredNamespaceURI() {
        when(parentNamespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema")).thenReturn("xsd");
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testGetPrefix_unregisteredNamespaceURI() {
        assertNull(namespaceResolver.getPrefix("unregistered"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testSeal() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_sealed() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }
}

class NodePointerStub implements NodePointer {
    @Override
    public String getNamespaceURI() {
        return null;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getNodeValue() {
        return null;
    }

    @Override
    public Iterator getAttributeNames() {
        return null;
    }

    @Override
    public Object getAttributeValue(String name) {
        return null;
    }

    @Override
    public String getAttributeNamespaceURI(String name) {
        return null;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public NamespaceResolver getNamespaceResolver() {
        return null;
    }

    @Override
    public NodeIterator getNodeIterator() {
        return nodeIterator;
    }
}

class NodeIteratorStub implements NodeIterator {
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public NodePointer currentNode() {
        return nodePointer;
    }

    @Override
    public NodePointer firstNode() {
        return nodePointer;
    }

    @Override
    public NodePointer lastNode() {
        return nodePointer;
    }

    @Override
    public NodePointer nextNode() {
        return nodePointer;
    }

    @Override
    public NodePointer previousNode() {
        return nodePointer;
    }

    @Override
    public void removeCurrentNode() {

    }

    @Override
    public void setPosition(int position) {

    }
}