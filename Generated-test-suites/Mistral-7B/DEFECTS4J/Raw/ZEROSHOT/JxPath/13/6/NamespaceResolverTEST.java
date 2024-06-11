import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver parentNamespaceResolver;

 @Test
 public void testNewInstance() {
 NamespaceResolver newInstance = new NamespaceResolver();
 assertNull(newInstance.getParent());
 assertNull(newInstance.getNamespaceMap());
 assertNull(newInstance.getReverseMap());
 assertNull(newInstance.getPointer());
 assertFalse(newInstance.isSealed());
 }

 @Test
 public void testInstanceWithParent() {
 parentNamespaceResolver = new NamespaceResolver();
 namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
 assertEquals(parentNamespaceResolver, namespaceResolver.getParent());
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
 public void testRegisterNamespaceAfterSeal() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.seal();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 fail("IllegalStateException expected");
 }

 @Test
 public void testGetNamespaceURI() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
 assertNull(namespaceResolver.getNamespaceURI("prefix1"));
 }

 @Test
 public void testGetPrefix() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
 assertNull(namespaceResolver.getPrefix("prefix1"));
 }

 @Test
 public void testGetNamespaceContextPointer() {
 NodePointer nodePointer = new NodePointer();
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.setNamespaceContextPointer(nodePointer);
 assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
 assertNull(namespaceResolver.getNamespaceContextPointer());
 }

 @Test
 public void testSeal() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.seal();
 assertTrue(namespaceResolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 NamespaceResolver cloned = (NamespaceResolver) original.clone();
 assertEquals(original, cloned);
 assertNotSame(original.getNamespaceMap(), cloned.getNamespaceMap());
}

 @Test
 public void testNamespaceIterator() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 NodePointer nodePointer = new NodePointer();
 namespaceResolver.setNamespaceContextPointer(nodePointer);
 Pointer expectedPointer = new NodePointer("http://www.example.com:8080/test.xml", null, null);
 namespaceResolver.registerNamespace("test", "http://www.example.com:8080/test.xml", expectedPointer);
 NodeIterator nodeIterator = namespaceResolver.getNamespaceIterator();
 assertEquals(expectedPointer, nodeIterator.next().getNodePointer());
 assertEquals("test", nodeIterator.next().getName().getName());
 assertEquals("http://www.example.com:8080/test.xml", nodeIterator.next().getNodePointer().getNamespaceURI());
 assertEquals(null, nodeIterator.next());
}

}