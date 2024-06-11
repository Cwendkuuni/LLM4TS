import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        Pointer result = resolver.getNamespaceContextPointer();
        assertEquals(pointer, result);
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        String result = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }

    @Test
    public void testGetNamespaceURIWithContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("namespaceURI");
        resolver.setNamespaceContextPointer(pointer);
        String result = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        String result = resolver.getPrefix("namespaceURI");
        assertEquals("prefix", result);
    }

    @Test
    public void testGetPrefixWithContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        pointer.setName(new Name("prefix"));
        resolver.setNamespaceContextPointer(pointer);
        String result = resolver.getPrefix("namespaceURI");
        assertEquals("prefix", result);
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        Object result = resolver.clone();
        assertEquals(resolver, result);
    }

}