package org.apache.commons.jxpath.ri.test;

import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer("namespaceURI", "localName", null);
 }

 /**
 * Test method for {@link NamespaceResolver#NamespaceResolver()}.
 */
 @Test
 public void testConstructor() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertEquals(resolver.getClass(), NamespaceResolver.class);
 }

 /**
 * Test method for {@link NamespaceResolver#NamespaceResolver(NamespaceResolver)}.
 */
 @Test
 public void testConstructorWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 assertEquals(resolver.getParent(), parentResolver);
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 */
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI("xsd");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 * @expected: IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testRegisterNamespaceNullPrefix() {
 resolver.registerNamespace(null, "http://www.w3.org/2001/XMLSchema");
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 * @expected: IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testRegisterNamespaceNullUri() {
 resolver.registerNamespace("xsd", null);
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 * @expected: IllegalStateException
 */
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceSealed() {
 resolver.seal();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 }

 /**
 * Test method for {@link NamespaceResolver#setNamespaceContextPointer(NodePointer)}.
 */
 @Test
 public void testSetNamespaceContextPointer() {
 NodeIterator ni = pointer.namespaceIterator();
 if (ni != null) {
 for (int position = 1; ni.setPosition(position); position++) {
 NodePointer nsPointer = ni.getNodePointer();
 String uri = nsPointer.getNamespaceURI();
 String prefix = nsPointer.getName().getName();
 resolver.registerNamespace(prefix, uri);
 }
 }
 NodePointer pointer = new NodePointer("namespaceURI", "localName", resolver);
 String uri = resolver.getNamespaceURI("xsd");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceContextPointer()}.
 */
 @Test
 public void testGetNamespaceContextPointer() {
 assertNull(resolver.getNamespaceContextPointer());
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceContextPointer()}.
 */
 @Test
 public void testGetNamespaceContextPointerWithSet() {
 pointer = new NodePointer("namespaceURI", "localName", null);
 resolver.setNamespaceContextPointer(pointer);
 Pointer expectedPointer = pointer;
 Pointer actualPointer = resolver.getNamespaceContextPointer();
 assertSame(expectedPointer, actualPointer);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURI() {
 String prefix = "xsd";
 resolver.registerNamespace(prefix, "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI(prefix);
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURIUnknownPrefix() {
 String uri = resolver.getNamespaceURI("unknownPrefix");
 assertNull(uri);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 * @expected: IllegalStateException
 */
 @Test(expected = IllegalStateException.class)
 public void testGetNamespaceURISealed() {
 resolver.seal();
 String uri = resolver.getNamespaceURI("xsd");
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefix() {
 String namespaceURI = "http://www.w3.org/2001/XMLSchema";
 resolver.registerNamespace("xsd", namespaceURI);
 String prefix = resolver.getPrefix(namespaceURI);
 assertEquals("xsd", prefix);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 * @expected: IllegalArgumentException
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetPrefixNullUri() {
 String prefix = resolver.getPrefix(null);
 }

 /**
 * Test method for {@link NamespaceResolver#isSealed()}.
 */
 @Test
 public void testIsSealed() {
 assertEquals(false, resolver.isSealed());
 }

 /**
 * Test method for {@link NamespaceResolver#seal()}.
 */
 @Test
 public void testSeal() {
 NamespaceResolver copy = (NamespaceResolver) resolver.clone();
 resolver.seal();
 assertFalse(copy.isSealed());
 assertTrue(resolver.isSealed());
 }

 /**
 * Test method for {@link NamespaceResolver#clone()}.
 */
 @Test
 public void testClone() {
 NamespaceResolver clone = (NamespaceResolver) resolver.clone();
 assertEquals(clone.getClass(), resolver.getClass());
 assertNotSame(clone, resolver);
 }

}