package org.apache.commons.jxpath.ri;

import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

   @Test
   public void testRegisterNamespace() {
       NamespaceResolver namespaceResolver = new NamespaceResolver();
       namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
       
       assertEquals("http://www.example.com/namespace1", namespaceResolver.getNamespaceURI("prefix1"));
   }
   
   @Test(expected = IllegalStateException.class)
   public void testRegisterNamespaceSealed() {
       NamespaceResolver parentResolver = new NamespaceResolver();
       parentResolver.seal();
       
       NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
       childResolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
   }

   @Test
   public void testSetNamespaceContextPointer() {
       NamespaceResolver namespaceResolver = new NamespaceResolver();
       
       assertNull(namespaceResolver.getNamespaceContextPointer());
       
       NodePointer pointer = new NodePointer();
       namespaceResolver.setNamespaceContextPointer(pointer);
       
       assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
   }

   @Test
   public void testGetNamespaceURI() {
       NamespaceResolver parentResolver = new NamespaceResolver();
       parentResolver.registerNamespace("prefix3", "http://www.example.com/namespace3");
       
       NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
       
       assertEquals("http://www.example.com/namespace3", childResolver.getNamespaceURI("prefix3"));
   }

   @Test
   public void testGetPrefix() {
       NamespaceResolver parentResolver = new NamespaceResolver();
       parentResolver.registerNamespace("prefix4", "http://www.example.com/namespace4");
       
       NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
       
       assertEquals("prefix4", childResolver.getPrefix("http://www.example.com/namespace4"));
   }

   @Test
   public void testIsSealed() {
       NamespaceResolver namespaceResolver = new NamespaceResolver();
       
       assertFalse(namespaceResolver.isSealed());
       
       namespaceResolver.seal();
       
       assertTrue(namespaceResolver.isSealed());
   }

   @Test
   public void testClone() {
       NamespaceResolver parentResolver = new NamespaceResolver();
       NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
       
       NamespaceResolver clonedChildResolver = (NamespaceResolver) childResolver.clone();
       
       assertEquals(parentResolver.isSealed(), clonedChildResolver.isSealed());
   }

}