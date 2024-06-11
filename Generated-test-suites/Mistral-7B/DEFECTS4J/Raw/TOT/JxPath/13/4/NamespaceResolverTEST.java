import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer("root");
 resolver.setNamespaceContextPointer(pointer);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)} when the prefix is defined.
 */
 @Test
 public void testGetNamespaceURIWithDefinedPrefix() {
 resolver.registerNamespace("x", "http://test.com/x");
 String uri = resolver.getNamespaceURI("x");
 assertEquals("http://test.com/x", uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)} when the prefix is not defined.
 */
 @Test
 public void testGetNamespaceURIWithUndefinedPrefix() {
 String uri = resolver.getNamespaceURI("x");
 assertNull(uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)} when the prefix is defined in the pointer.
 */
 @Test
 public void testGetNamespaceURIWithDefinedPrefixInPointer() {
 NodePointer pointerWithNamespace = new NodePointer("root", "ns:childElement", "http://test.com/ns");
 resolver.setNamespaceContextPointer(pointerWithNamespace);
 String uri = resolver.getNamespaceURI("ns");
 assertEquals("http://test.com/ns", uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)} when the prefix is defined in the parent resolver.
 */
 @Test
 public void testGetNamespaceURIWithDefinedPrefixInParentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver(resolver);
 parentResolver.registerNamespace("x", "http://test.com/x");
 resolver.setParent(parentResolver);
 String uri = resolver.getNamespaceURI("x");
 assertEquals("http://test.com/x", uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)} when the namespace URI is defined.
 */
 @Test
 public void testGetPrefixWithDefinedNamespaceURI() {
 resolver.registerNamespace("x", "http://test.com/x");
 String prefix = resolver.getPrefix("http://test.com/x");
 assertEquals("x", prefix);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)} when the namespace URI is not defined.
 */
 @Test
 public void testGetPrefixWithUndefinedNamespaceURI() {
 String prefix = resolver.getPrefix("http://test.com/x");
 assertNull(prefix);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)} when the namespace URI is defined in the pointer.
 */
 @Test
 public void testGetPrefixWithDefinedNamespaceURIPointer() {
 NodePointer pointerWithNamespace = new NodePointer("root", "ns:childElement", "http://test.com/ns");
 resolver.setNamespaceContextPointer(pointerWithNamespace);
 String prefix = resolver.getPrefix("http://test.com/ns");
 assertEquals("ns", prefix);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)} when the namespace URI is defined in the parent resolver.
 */
 @Test
 public void testGetPrefixWithDefinedNamespaceURIParentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver(resolver);
 parentResolver.registerNamespace("x", "http://test.com/x");
 resolver.setParent(parentResolver);
 String prefix = resolver.getPrefix("http://test.com/x");
 assertEquals("x", prefix);
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)} when the prefix is already registered.
 */
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceWithAlreadyRegisteredPrefix() {
 resolver.registerNamespace("x", "http://test.com/x");
 resolver.registerNamespace("x", "http://test.com/x");
 }

 /**
 * Test method for {@link NamespaceResolver#seal()} when the resolver is already sealed.
 */
 @Test(expected = IllegalStateException.class)
 public void testSealAlreadySealed() {
 resolver.seal();
 resolver.seal();
 }

 /**
 * Test method for {@link NamespaceResolver#seal()} when the resolver has a parent.
 */
 @Test
 public void testSealWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver(resolver);
 resolver.seal();
 assertEquals(false, parentResolver.isSealed());
 }

 /**
 * Test method for {@link NamespaceResolver#clone()} when the resolver is not sealed.
 */
 @Test
 public void testCloneNotSealed() {
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertNotEquals(resolver, clonedResolver);
 assertEquals(false, clonedResolver.isSealed());
 }

 /**
 * Test method for {@link NamespaceResolver#clone()} when the resolver is sealed.
 */
 @Test(expected = CloneNotSupportedException.class)
 public void testCloneSealed() {
 resolver.seal();
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 }
}