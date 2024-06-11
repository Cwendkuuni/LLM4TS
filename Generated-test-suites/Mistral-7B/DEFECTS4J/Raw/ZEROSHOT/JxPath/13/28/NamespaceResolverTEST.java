package org.apache.commons.jxpath.ri;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver nestedNamespaceResolver;
 private String defaultNamespaceURI = "http://default.namespace.uri";
 private String prefixOne = "prefix1";
 private String prefixTwo = "prefix2";
 private String uriOne = "http://uri1.com";
 private String uriTwo = "http://uri2.com";
 private String unregisteredPrefix = "unregisteredPrefix";
 private String unregisteredURI = "http://unregistered.uri";

 @Before
 public void setUp() {
 namespaceResolver = new NamespaceResolver();
 nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 namespaceResolver.registerNamespace(prefixOne, uriOne);
 namespaceResolver.registerNamespace(prefixTwo, uriTwo);
 }

 @Test
 public void testGetNamespaceURI() {
 // test with known prefixes
 assertEquals(uriOne, namespaceResolver.getNamespaceURI(prefixOne));
 assertEquals(uriTwo, namespaceResolver.getNamespaceURI(prefixTwo));

 // test with unknown prefix
 assertNull(namespaceResolver.getNamespaceURI(unregisteredPrefix));

 // test with nested NamespaceResolver
 namespaceResolver.setNamespaceContextPointer(new NodePointer("rootElement"));
 nestedNamespaceResolver.registerNamespace(prefixOne, uriOne);
 Pointer pointer = new NodePointer("namespace:test");
 pointer.setNamespaceContext(namespaceResolver);
 assertEquals(uriOne, namespaceResolver.getNamespaceURI(prefixOne));
 assertEquals(uriOne, pointer.getNamespaceURI(prefixOne));
 }

 @Test
 public void testGetPrefix() {
 // test with known uris
 assertEquals(prefixOne, namespaceResolver.getPrefix(uriOne));
 assertEquals(prefixTwo, namespaceResolver.getPrefix(uriTwo));

 // test with unknown uri
 assertNull(namespaceResolver.getPrefix(unregisteredURI));

 // test with nested NamespaceResolver
 namespaceResolver.setNamespaceContextPointer(new NodePointer("rootElement"));
 nestedNamespaceResolver.registerNamespace(prefixOne, uriOne);
 Pointer pointer = new NodePointer("namespace:test");
 pointer.setNamespaceContext(namespaceResolver);
 assertEquals(prefixOne, namespaceResolver.getPrefix(uriOne));
 assertEquals(prefixOne, pointer.getPrefix(uriOne));
 }

 @Test
 public void testRegisterNamespace() {
 // test registering same prefix twice
 namespaceResolver.registerNamespace(prefixOne, uriOne);
 namespaceResolver.registerNamespace(prefixOne, uriOne);
 assertEquals(uriOne, namespaceResolver.getNamespaceURI(prefixOne));

 // test registering prefix with empty string as uri
 namespaceResolver.registerNamespace(prefixOne, "");
 assertNull(namespaceResolver.getNamespaceURI(prefixOne));

 // test registering prefix with null as uri
 namespaceResolver.registerNamespace(prefixOne, null);
 assertNull(namespaceResolver.getNamespaceURI(prefixOne));

 // test registering prefix with null as prefix
 namespaceResolver.registerNamespace(null, uriOne);
 assertNull(namespaceResolver.getNamespaceURI(prefixOne));

 // test registering prefix with null as parent
 NamespaceResolver nullParentResolver = null;
 namespaceResolver = new NamespaceResolver(nullParentResolver);
 namespaceResolver.registerNamespace(prefixOne, uriOne);
 assertNull(namespaceResolver.getNamespaceURI(prefixOne));
 }

 @Test
 public void testSeal() {
 // test sealing a new NamespaceResolver
 NamespaceResolver newNamespaceResolver = new NamespaceResolver();
 newNamespaceResolver.seal();
 assertTrue(newNamespaceResolver.isSealed());

 // test sealing a NamespaceResolver with parent
 namespaceResolver.seal();
 assertTrue(namespaceResolver.isSealed());
 assertTrue(nestedNamespaceResolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 namespaceResolver.registerNamespace(prefixOne, uriOne);
 namespaceResolver.seal();
 namespaceResolver.registerNamespace(prefixOne, uriOne);
 }
}