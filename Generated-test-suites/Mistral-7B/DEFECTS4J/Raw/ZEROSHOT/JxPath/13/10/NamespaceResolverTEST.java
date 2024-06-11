package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri. NamespaceResolver;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test file for NamespaceResolver class.
 */
public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NamespaceResolver parentResolver;
 private NodePointer pointer;
 private Map<String, String> namespaceMap;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 parentResolver = new NamespaceResolver(null);
 namespaceMap = new HashMap<>();
 pointer = new NodePointer(new Object(), null, null);
 }

 @Test
 public void testCreateNewNamespaceResolver() {
 NamespaceResolver newResolver = new NamespaceResolver();
 assertNull(newResolver.getParent());
 assertSame(newResolver, newResolver.clone());
 }

 @Test
 public void testCreateNamespaceResolverWithParent() {
 NamespaceResolver newResolver = new NamespaceResolver(parentResolver);
 assertSame(parentResolver, newResolver.getParent());
 assertSame(newResolver, newResolver.clone());
 }

 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 @Test
 public void testRegisterNamespaceWithSamePrefix() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("xsd", "http://www.w3.org/2000/XMLNS/");
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 resolver.seal();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 }

 @Test
 public void testGetNamespaceURI() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 @Test
 public void testGetNamespaceURIForUnknownPrefix() {
 assertNull(resolver.getNamespaceURI("xsd"));
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 @Test
 public void testGetNamespaceURIWithParentAndRegistered() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("xsd", "http://example.com/xsd");
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("http://example.com/xsd", resolver.getNamespaceURI("xsd"));
 }

 @Test
 public void testGetPrefix() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 @Test
 public void testGetPrefixForUnknownNamespaceURI() {
 assertNull(resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 @Test
 public void testGetPrefixWithParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 @Test
 public void testGetPrefixWithRegisteredParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("xsd", "http://example.com/xsd");
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("xsd", resolver.getPrefix("http://example.com/xsd"));
 }

 @Test
 public void testIsSealed() {
 assertFalse(resolver.isSealed());
 }

 @Test
 public void testSealResolver() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertEquals(resolver, clonedResolver);
 }

 @Test
 public void testCloneWithParent() {
 NamespaceResolver clonedResolver = (NamespaceResolver) parentResolver.clone();
 assertEquals(parentResolver, clonedResolver);
 }

 @Test
 public void testGetNamespaceContextPointer() {
 assertNull(resolver.getNamespaceContextPointer());
 }

 @Test
 public void testSetNamespaceContextPointer() {
 resolver.setNamespaceContextPointer(pointer);
 assertSame(pointer, resolver.getNamespaceContextPointer());
 }

 @Test
 public void testGetNamespaceContextPointerWithParent() {
 parentResolver.setNamespaceContextPointer(pointer);
 resolver.setNamespaceContextPointer(pointer);
 assertSame(pointer, resolver.getNamespaceContextPointer());
 }

 @Test
 public void testNamespaceIterator() {
 NodeIterator ni = pointer.namespaceIterator();
 NamespaceResolver clone = (NamespaceResolver) resolver.clone();
 clone.setNamespaceContextPointer(pointer);
 assertEquals(ni.getNodePointer(), clone.getNamespaceContextPointer());
 }
}