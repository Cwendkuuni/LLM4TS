import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for NamespaceResolver
 */
public class NamespaceResolverTest {

 private NamespaceResolver resolver;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}
 */
 @Test
 public void testGetNamespaceURI() {
 // Test with explicit registration
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));

 // Test with context node registration
 NodePointer context = new NodePointer("root", new HashMap());
 resolver.setNamespaceContextPointer(context);
 context.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));

 // Test with parent registration
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 resolver = new NamespaceResolver(parent);
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));

 // Test with null prefix
 assertNull(resolver.getNamespaceURI(null));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}
 */
 @Test
 public void testGetPrefix() {
 // Test with explicit registration
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));

 // Test with context node registration
 NodePointer context = new NodePointer("root", new HashMap());
 context.registerNamespace("x", "http://example.com/x");
 resolver.setNamespaceContextPointer(context);
 assertEquals("x", resolver.getPrefix("http://example.com/x"));

 // Test with parent registration
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 resolver = new NamespaceResolver(parent);
 assertEquals("x", resolver.getPrefix("http://example.com/x"));

 // Test with null URI
 assertNull(resolver.getPrefix(null));
 }

 /**
 * Test method for {@link NamespaceResolver#isSealed()}
 */
 @Test
 public void testIsSealed() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }
}