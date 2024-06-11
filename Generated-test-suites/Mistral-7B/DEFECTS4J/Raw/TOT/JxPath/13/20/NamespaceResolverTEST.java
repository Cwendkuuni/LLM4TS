import org.apache.commons.jxpath.NodeContext;
import org.apache.commons.jxpath.ri.Namespaces;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 @Test
 public void testNamespaceRegistration() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithUnregisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetNamespaceURIWithContext() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(new NodeContext(new NodePointer("root"), new Namespaces()));
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithContextAndRegisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodeContext context = new NodeContext(new NodePointer("root"), new Namespaces());
 resolver.setNamespaceContextPointer(context);
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertEquals("", context.lookupVariableValue("x:foo").getNamespaceURI().stringValue());
 }

 @Test
 public void testGetNamespaceURIWithContextAndUnregisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodeContext context = new NodeContext(new NodePointer("root"), new Namespaces());
 resolver.setNamespaceContextPointer(context);
 assertNull(resolver.getNamespaceURI("y"));
 assertEquals("", context.lookupVariableValue("y:foo").getNamespaceURI().stringValue());
 }

 @Test
 public void testGetPrefixWithRegisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetPrefixWithExternallyRegisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(new NodeContext(new NodePointer("root"), new Namespaces()));
 NodeIterator ni = ((NodePointer) resolver.getNamespaceContextPointer()).namespaceIterator();
 NodePointer nsPointer = (NodePointer) ni.next();
 resolver.registerNamespace("x", nsPointer.getNamespaceURI());
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetPrefixWithUnregisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testGetPrefixWithContextAndRegisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodeContext context = new NodeContext(new NodePointer("root"), new Namespaces());
 resolver.setNamespaceContextPointer(context);
 NodeIterator ni = context.namespaceIterator();
 NodePointer nsPointer = (NodePointer) ni.next();
 resolver.registerNamespace("x", nsPointer.getNamespaceURI());
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertEquals("x", context.lookupVariableValue("x:foo").getNamespaceURI().stringValue());
 }

 @Test
 public void testGetPrefixWithContextAndUnregisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodeContext context = new NodeContext(new NodePointer("root"), new Namespaces());
 assertNull(resolver.getPrefix("http://example.com/y"));
 assertEquals("", context.lookupVariableValue("y:foo").getNamespaceURI().stringValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 try {
 resolver.registerNamespace("y", "http://example.com/y");
 fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("x", "http://example.com/x");
 NamespaceResolver cloned = (NamespaceResolver) original.clone();
 assertEquals("http://example.com/x", cloned.getNamespaceURI("x"));
 assertEquals("x", cloned.getPrefix("http://example.com/x"));
 }
}