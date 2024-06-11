package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath. Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver nestedNamespaceResolver;

 @Test
 public void testCreateNewNamespaceResolver() {
 namespaceResolver = new NamespaceResolver();
 assertNull(namespaceResolver.getParent());
 assertNull(namespaceResolver.getNamespaceMap());
 assertNull(namespaceResolver.getReverseMap());
 assertNull(namespaceResolver.getPointer());
 assertFalse(namespaceResolver.isSealed());
 }

 @Test
 public void testCreateNamespaceResolverWithParent() {
 namespaceResolver = new NamespaceResolver(new NamespaceResolver());
 assertSame(namespaceResolver.getParent(), namespaceResolver.getParent());
 assertNull(namespaceResolver.getNamespaceMap());
 assertNull(namespaceResolver.getReverseMap());
 assertNull(namespaceResolver.getPointer());
 assertFalse(namespaceResolver.isSealed());
 }

 @Test
 public void testRegisterNamespace() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 Map<String, String> namespaceMap = (Map<String, String>) namespaceResolver.getNamespaceMap().clone();
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceMap.get("xsi"));
 assertEquals(1, namespaceMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedNamespaceResolver() {
 NamespaceResolver sealedNamespaceResolver = new NamespaceResolver();
 sealedNamespaceResolver.seal();
 sealedNamespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 }

 @Test
 public void testGetNamespaceURI() {
 namespaceResolver = new NamespaceResolver();
 assertNull(namespaceResolver.getNamespaceURI("xsi"));

 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 NamespaceResolver nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", nestedNamespaceResolver.getNamespaceURI("xsi"));
 }

 @Test
 public void testGetNamespaceURIWithPointer() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.setNamespaceContextPointer(new NodePointer());
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 NodePointer nodePointer = new NodePointer();
 nodePointer.setNamespaceURI("http://www.w3.org/2001/XMLSchema-instance");
 nodePointer.setName("test");
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("", nodePointer));
 }

 @Test
 public void testGetNamespaceURIWithParentAndPointer() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 NamespaceResolver nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);

 NodePointer nodePointer = new NodePointer();
 nodePointer.setNamespaceURI("http://www.w3.org/2001/XMLSchema-instance");
 nodePointer.setName("test");

 assertEquals("http://www.w3.org/2001/XMLSchema-instance", nestedNamespaceResolver.getNamespaceURI("xsi"));
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", nestedNamespaceResolver.getNamespaceURI("", nodePointer));
 }

 @Test
 public void testGetPrefix() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
 assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
 }

 @Test
 public void testGetPrefixWithParent() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 NamespaceResolver nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 assertEquals("xsi", nestedNamespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
 assertNull(nestedNamespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
 assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
 assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
 }

 @Test
 public void testGetPrefixWithPointer() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.setNamespaceContextPointer(new NodePointer());
 NodePointer nodePointer = new NodePointer();
 nodePointer.setNamespaceURI("http://www.w3.org/2001/XMLSchema-instance");
 nodePointer.setName("test");
 assertEquals("xsi", namespaceResolver.getPrefix(""));
 assertEquals("xsi", namespaceResolver.getPrefix("", nodePointer));
 assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
 }

 @Test
 public void testGetPrefixWithParentAndPointer() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 NamespaceResolver nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);

 NodePointer nodePointer = new NodePointer();
 nodePointer.setNamespaceURI("http://www.w3.org/2001/XMLSchema-instance");
 nodePointer.setName("test");

 assertEquals("xsi", nestedNamespaceResolver.getPrefix(""));
 assertEquals("xsi", nestedNamespaceResolver.getPrefix("", nodePointer));
 assertNull(nestedNamespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
 assertEquals("xsi", namespaceResolver.getPrefix(""));
 assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
 }

 @Test
 public void testSeal() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.seal();
 assertSame(namespaceResolver, namespaceResolver.getParent());
 assertTrue(namespaceResolver.isSealed());
 }

 @Test
 public void testClone() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.seal();
 NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
 assertSame(namespaceResolver, namespaceResolver.getParent());
 assertSame(clonedNamespaceResolver, clonedNamespaceResolver.getParent());
 assertFalse(clonedNamespaceResolver.isSealed());
 }

 @Test
 public void testNamespaceIterator() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.registerNamespace("xpath", "http://www.w3.org/2001/XPath");
 NodeIterator nodeIterator = namespaceResolver.getPointer().namespaceIterator();
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", nodeIterator.next().getNodePointer().getNamespaceURI());
 assertEquals("xsi", nodeIterator.next().getNodePointer().getName().getName());
 assertEquals("http://www.w3.org/2001/XPath", nodeIterator.next().getNodePointer().getNamespaceURI());
 assertEquals("xpath", nodeIterator.next().getNodePointer().getName().getName());
 assertNull(nodeIterator.next());
 }

 @Test
 public void testNamespaceIteratorWithParent() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.registerNamespace("xpath", "http://www.w3.org/2001/XPath");
 NamespaceResolver nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 NodeIterator nodeIterator = nestedNamespaceResolver.getPointer().namespaceIterator();
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", nodeIterator.next().getNodePointer().getNamespaceURI());
 assertEquals("xsi", nodeIterator.next().getNodePointer().getName().getName());
 assertEquals("http://www.w3.org/2001/XPath", nodeIterator.next().getNodePointer().getNamespaceURI());
 assertEquals("xpath", nodeIterator.next().getNodePointer().getName().getName());
 assertNull(nodeIterator.next());
 }
}