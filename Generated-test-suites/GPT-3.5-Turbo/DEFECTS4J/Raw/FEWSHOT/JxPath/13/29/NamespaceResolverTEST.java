import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class NamespaceResolverTest {
    private NamespaceResolver parent;
    private NodePointer pointer;
    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        parent = mock(NamespaceResolver.class);
        pointer = mock(NodePointer.class);
        namespaceResolver = new NamespaceResolver(parent);
        namespaceResolver.setNamespaceContextPointer(pointer);
    }
    
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("http://www.example.com", namespaceResolver.getNamespaceURI("prefix"));
        verify(namespaceResolver, never()).getNamespaceURI("prefix");
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_sealed() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "http://www.example.com");
    }

    @Test
    public void testGetNamespaceURI() {
        when(parent.getNamespaceURI("prefix")).thenReturn("http://www.example.com");
        assertEquals("http://www.example.com", namespaceResolver.getNamespaceURI("prefix"));
        verify(parent).getNamespaceURI("prefix");
    }

    @Test
    public void testGetNamespaceURI_external() {
        namespaceResolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("http://www.example.com", namespaceResolver.getNamespaceURI("prefix"));
        verify(parent, never()).getNamespaceURI("prefix");
    }

    @Test
    public void testGetNamespaceURI_null() {
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
        verify(parent).getNamespaceURI("prefix");
    }

    @Test
    public void testGetPrefix() {
        when(parent.getPrefix("http://www.example.com")).thenReturn("prefix");
        assertEquals("prefix", namespaceResolver.getPrefix("http://www.example.com"));
        verify(parent).getPrefix("http://www.example.com");
    }

    @Test
    public void testGetPrefix_external() {
        namespaceResolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("prefix", namespaceResolver.getPrefix("http://www.example.com"));
        verify(parent, never()).getPrefix("http://www.example.com");
    }

    @Test
    public void testGetPrefix_externalNull() {
        assertNull(namespaceResolver.getPrefix("http://www.example.com"));
        verify(parent).getPrefix("http://www.example.com");
    }
}