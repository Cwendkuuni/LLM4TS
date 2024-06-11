package org.apache.commons.jxpath.ri;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        String prefix = "x";
        String expected = "http://example.com/x";
        String result = resolver.getNamespaceURI(prefix);
        assertEquals(expected, result);
    }
}