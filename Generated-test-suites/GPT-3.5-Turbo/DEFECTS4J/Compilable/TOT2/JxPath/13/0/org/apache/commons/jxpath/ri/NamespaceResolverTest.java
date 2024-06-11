package org.apache.commons.jxpath.ri;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "http://example.com");
        assertEquals("http://example.com", namespaceResolver.getNamespaceURI("prefix"));
    }

    // Add more test cases for other methods here

}