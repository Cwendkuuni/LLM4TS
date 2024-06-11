import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for NamespaceResolver class.
 */
public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver parentResolver;

 @Before
 public void setUp() {
 namespaceResolver = new NamespaceResolver();
 parentResolver = new NamespaceResolver(null);
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 */
 @Test
 public void testRegisterNamespace() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURI() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURIWrongPrefix() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertNull(namespaceResolver.getNamespaceURI("wrongPrefix"));
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURIWithoutPrefix() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertNull(namespaceResolver.getNamespaceURI(""));
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURIWithParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 namespaceResolver = new NamespaceResolver(parentResolver);
 assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefix() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWrongURI() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertNull(namespaceResolver.getPrefix("wrongURI"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixEmptyURI() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertNull(namespaceResolver.getPrefix(""));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 parentResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
 namespaceResolver = new NamespaceResolver(parentResolver);
 assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
 assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test method for {@link NamespaceResolver#isSealed()}.
 */
 @Test
 public void testIsSealed() {
 NamespaceResolver unsealedResolver = new NamespaceResolver();
 assertFalse(unsealedResolver.isSealed());
 NamespaceResolver sealedResolver = new NamespaceResolver();
 sealedResolver.seal();
 assertTrue(sealedResolver.isSealed());
 }

 /**
 * Test method for {@link NamespaceResolver#seal()}.
 */
 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.seal();
 try {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 /**
 * Test method for {@link NamespaceResolver#clone()}.
 */
 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertEquals("xsd", clone.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }
}