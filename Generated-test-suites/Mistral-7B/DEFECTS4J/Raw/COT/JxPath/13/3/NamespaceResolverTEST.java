import org.apache.commons.jxpath. NamespaceContext;
import org.apache.commons.jxpath.ri.Namespaces;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver parentResolver;
 private NamespaceContext namespaceContext;

 @Before
 public void setUp() {
 parentResolver = Mockito.mock(NamespaceResolver.class);
 namespaceContext = Mockito.mock(NamespaceContext.class);
 namespaceResolver = new NamespaceResolver(parentResolver);
 PowerMockito.when(parentResolver.getNamespaceURI("prefix1")).thenReturn("uri1");
 PowerMockito.when(namespaceContext.getNamespace("prefix2")).thenReturn("uri2");
 }

 @Test
 public void testNewInstance() {
 NamespaceResolver instance = new NamespaceResolver();
 assertNull(instance.getNamespaceURI("prefix1"));
 assertNull(instance.getNamespaceURI("prefix2"));
 }

 @Test
 public void testRegisterNamespace() {
 namespaceResolver.registerNamespace("prefix3", "uri3");
 assertEquals("uri3", namespaceResolver.getNamespaceURI("prefix3"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 namespaceResolver.seal();
 namespaceResolver.registerNamespace("prefix4", "uri4");
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefix() {
 namespaceResolver.registerNamespace("prefix1", "uri1");
 assertEquals("uri1", namespaceResolver.getNamespaceURI("prefix1"));
 }

 @Test
 public void testGetNamespaceURIWithContextPrefix() {
 namespaceResolver.setNamespaceContextPointer(PowerMockito.mock(NodePointer.class));
 namespaceContext.addNamespace("prefix2", "uri2");
 assertEquals("uri2", namespaceResolver.getNamespaceURI("prefix2"));
 }

 @Test
 public void testGetNamespaceURIWithParentPrefix() {
 namespaceResolver.registerNamespace("prefix1", "uri1");
 PowerMockito.when(parentResolver.getNamespaceURI("prefix2")).thenReturn("uri2");
 assertEquals("uri2", namespaceResolver.getNamespaceURI("prefix2"));
 }

 @Test
 public void testGetPrefixWithRegisteredNamespace() {
 namespaceResolver.registerNamespace("prefix1", "uri1");
 assertEquals("prefix1", namespaceResolver.getPrefix("uri1"));
 }

 @Test
 public void testGetPrefixWithRegisteredPrefix() {
 namespaceResolver.registerNamespace("prefix1", "uri1");
 assertEquals("prefix1", namespaceResolver.getPrefix("prefix1"));
 }

 @Test
 public void testGetPrefixWithContextPrefix() {
 namespaceResolver.setNamespaceContextPointer(PowerMockito.mock(NodePointer.class));
 namespaceContext.addNamespace("prefix2", "uri2");
 assertEquals("prefix2", namespaceResolver.getPrefix("uri2"));
 }

 @Test
 public void testGetPrefixWithParentPrefix() {
 namespaceResolver.registerNamespace("prefix1", "uri1");
 PowerMockito.when(parentResolver.getPrefix("prefix2")).thenReturn("prefix2");
 assertEquals("prefix2", namespaceResolver.getPrefix("prefix2"));
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver instance = new NamespaceResolver();
 assertFalse(instance.isSealed());
 namespaceResolver.seal();
 assertTrue(namespaceResolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testSealAlreadySealedResolver() {
 NamespaceResolver instance = new NamespaceResolver();
 instance.seal();
 instance.seal();
 }

 @Test
 public void testSealParent() {
 namespaceResolver.seal();
 PowerMockito.verify(parentResolver, Mockito.times(1)).seal();
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("prefix1", "uri1");
 NamespaceResolver cloned = (NamespaceResolver) original.clone();
 assertEquals("uri1", cloned.getNamespaceURI("prefix1"));
 }
}