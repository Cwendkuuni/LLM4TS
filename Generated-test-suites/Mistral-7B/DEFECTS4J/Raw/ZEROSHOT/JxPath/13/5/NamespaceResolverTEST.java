package org.apache.commons.jxpath.ri;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NamespaceResolverTest {

    @Mock
    private NodePointer pointer;

    @Mock
    private NodeIterator nodeIterator;

    @InjectMocks
    private NamespaceResolver namespaceResolver;

    @Mock
    private NamespaceResolver parentNamespaceResolver;

    @Before
    public void setUp() {
        when(pointer.getNamespaceURI("prefix")).thenReturn("namespaceURI");
        when(pointer.getNamespaceURI("prefix2")).thenReturn("namespaceURI2");
        when(pointer.namespaceIterator()).thenReturn(nodeIterator);
        when(nodeIterator.getNodePointer()).thenReturn(pointer);
        when(nodeIterator.hasNext()).thenReturn(true, false);
        when(nodeIterator.setPosition(0)).thenReturn(true);

        namespaceResolver.setNamespaceContextPointer(pointer);
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        namespaceResolver.registerNamespace("prefix2", "namespaceURI2");

        namespaceResolver.seal();
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
        assertEquals("namespaceURI2", namespaceResolver.getNamespaceURI("prefix2"));
        assertNull(namespaceResolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
        assertEquals("prefix2", namespaceResolver.getPrefix("namespaceURI2"));
        assertNull(namespaceResolver.getPrefix("namespaceURI3"));
    }

    @Test
    public void testIsSealed() {
        assertTrue(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSeal() {
        namespaceResolver.registerNamespace("prefix3", "namespaceURI3");
    }
}