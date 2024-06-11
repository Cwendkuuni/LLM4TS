import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NodePointer nodePointer;

 @Before
 public void setUp() {
 namespaceResolver = new NamespaceResolver();
 nodePointer = new NodePointer("namespaceURI", "prefix", null, null);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURI() {
 namespaceResolver.registerNamespace("prefix1", "namespaceURI1");
 namespaceResolver.setNamespaceContextPointer(nodePointer);

 assertEquals("namespaceURI1", namespaceResolver.getNamespaceURI("prefix1"));
 assertNull(namespaceResolver.getNamespaceURI("prefix2"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefix() {
 namespaceResolver.registerNamespace("prefix1", "namespaceURI1");
 namespaceResolver.setNamespaceContextPointer(nodePointer);

 assertEquals("prefix1", namespaceResolver.getPrefix("namespaceURI1"));
 assertNull(namespaceResolver.getPrefix("namespaceURI2"));
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 */
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceWhenSealed() {
 namespaceResolver.seal();
 namespaceResolver.registerNamespace("prefix", "namespaceURI");
 }

 /**
 * Test method for {@link NamespaceResolver#setNamespaceContextPointer(NodePointer)}.
 */
 @Test
 public void testSetNamespaceContextPointer() {
 NodeIterator nodeIterator = getMockNodeIterator();
 nodePointer.setNodeIterator(nodeIterator);
 namespaceResolver.setNamespaceContextPointer(nodePointer);
 namespaceResolver.registerNamespace("prefix1", "namespaceURI1");

 assertEquals("namespaceURI1", namespaceResolver.getNamespaceURI("prefix1"));
 }

 /**
 * Test method for {@link NamespaceResolver#seal()}.
 */
 @Test
 public void testSeal() {
 namespaceResolver.seal();
 namespaceResolver.registerNamespace("prefix", "namespaceURI");

 assertFalse(namespaceResolver.isSealed());
 }

 /**
 * Mock NodeIterator for testing purposes.
 * @return NodeIterator
 */
 private NodeIterator getMockNodeIterator() {
 throw new UnsupportedOperationException("Mock NodeIterator is not implemented yet.");
 }

}