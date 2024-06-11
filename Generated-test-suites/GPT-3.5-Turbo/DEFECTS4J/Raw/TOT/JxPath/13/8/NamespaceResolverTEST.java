import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    
    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        String uri = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example.com/ns1", uri);
    }

    @Test
    public void testRegisterNamespace_multiplePrefixes() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");
        resolver.registerNamespace("prefix3", "http://www.example.com/ns3");
        
        String uri1 = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example.com/ns1", uri1);
        
        String uri2 = resolver.getNamespaceURI("prefix2");
        assertEquals("http://www.example.com/ns2", uri2);
        
        String uri3 = resolver.getNamespaceURI("prefix3");
        assertEquals("http://www.example.com/ns3", uri3);
    }

    @Test
    public void testRegisterNamespace_sealedResolver() {
        resolver.seal();
        
        try {
            resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }
    
    @Test
    public void testGetNamespaceURI_definedByPrefix() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        String uri = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example.com/ns1", uri);
    }

    @Test
    public void testGetNamespaceURI_definedByContextNode() {
        NodePointer pointer = new MockNodePointer();
        pointer.addNamespace("prefix1", "http://www.example.com/ns1");
        resolver.setNamespaceContextPointer(pointer);
        
        String uri = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example.com/ns1", uri);
    }

    @Test
    public void testGetNamespaceURI_definedByParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        resolver = new NamespaceResolver(parentResolver);
        
        String uri = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example.com/ns1", uri);
    }
    
    @Test
    public void testGetNamespaceURI_prefixUndefined() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        String uri = resolver.getNamespaceURI("prefix2");
        assertNull(uri);
    }
    
    @Test
    public void testGetPrefix_definedByNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        String prefix = resolver.getPrefix("http://www.example.com/ns1");
        assertEquals("prefix1", prefix);
    }

    @Test
    public void testGetPrefix_definedByContextNode() {
        NodePointer pointer = new MockNodePointer();
        pointer.addNamespace("prefix1", "http://www.example.com/ns1");
        resolver.setNamespaceContextPointer(pointer);
        
        String prefix = resolver.getPrefix("http://www.example.com/ns1");
        assertEquals("prefix1", prefix);
    }

    @Test
    public void testGetPrefix_definedByParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        resolver = new NamespaceResolver(parentResolver);
        
        String prefix = resolver.getPrefix("http://www.example.com/ns1");
        assertEquals("prefix1", prefix);
    }

    @Test
    public void testGetPrefix_namespaceURINotDefined() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        
        String prefix = resolver.getPrefix("http://www.example.com/ns2");
        assertNull(prefix);
    }
    
    private class MockNodePointer extends NodePointer {
        
        private Map<String, String> namespaces = new HashMap<>();
        
        public void addNamespace(String prefix, String uri) {
            namespaces.put(prefix, uri);
        }

        @Override
        public NodePointer namespacePointer() {
            return this;
        }

        @Override
        public NodeIterator namespaceIterator() {
            Iterator<String> iterator = namespaces.keySet().iterator();
            return new MockNodeIterator(iterator);
        }
        
    }
    
    private class MockNodeIterator implements NodeIterator {
        
        private Iterator<String> iterator;
        private int position;
        
        public MockNodeIterator(Iterator<String> iterator) {
            this.iterator = iterator;
            this.position = -1;
        }

        @Override
        public boolean setPosition(int position) {
            if (position > iterator.size()) {
                return false;
            }
            this.position = position;
            return true;
        }

        @Override
        public NodePointer getNodePointer() {
            String prefix = iterator.get(position - 1);
            String uri = namespaces.get(prefix);
            return createNodePointer(prefix, uri);
        }
        
        private NodePointer createNodePointer(String prefix, String uri) {
            NodePointer pointer = new NodePointer();
            pointer.setNamespaceURI(uri);
            NodeName name = new NodeName();
            name.setName(prefix);
            pointer.setName(name);
            return pointer;
        }
        
    }
}