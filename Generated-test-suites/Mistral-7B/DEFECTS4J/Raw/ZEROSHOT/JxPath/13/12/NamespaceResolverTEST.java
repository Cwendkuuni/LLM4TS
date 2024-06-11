import org.apache.commons.jxpath.NamespaceContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.StandaloneContext;
import org.apache.commons.jxpath.ri. NamespaceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NamespaceResolver parentResolver;
 private static final String PREFIX = "test";
 private static final String URI = "http://test.com";

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 parentResolver = new NamespaceResolver();
 }

 @Test
 public void testEmptyResolver() {
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertNull(resolver.getNamespaceURI(PREFIX));
 Assert.assertNull(resolver.getPrefix(URI));
 }

 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace(PREFIX, URI);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceSealedResolver() {
 resolver.seal();
 resolver.registerNamespace(PREFIX, URI);
 }

 @Test
 public void testRegisterSameNamespaceTwice() {
 resolver.registerNamespace(PREFIX, URI);
 resolver.registerNamespace(PREFIX, URI);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 }

 @Test
 public void testRegisterParentNamespace() {
 parentResolver.registerNamespace(PREFIX, URI);
 resolver = new NamespaceResolver(parentResolver);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 }

 @Test
 public void testSealResolver() {
 resolver.registerNamespace(PREFIX, URI);
 resolver.seal();
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertNotNull(resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 }

 @Test(expected = IllegalStateException.class)
 public void testSealParentResolver() {
 resolver.registerNamespace(PREFIX, URI);
 parentResolver.seal();
 }

 @Test
 public void testGetParentNamespace() {
 parentResolver.registerNamespace(PREFIX, URI);
 resolver = new NamespaceResolver(parentResolver);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(parentResolver, resolver.getParent());
 }

 @Test
 public void testGetParentNamespaceTwice() {
 parentResolver.registerNamespace(PREFIX, URI);
 NamespaceResolver parentResolver2 = new NamespaceResolver(parentResolver);
 NamespaceResolver resolver3 = new NamespaceResolver(parentResolver2);
 NamespaceContext context = new StandaloneContext(resolver3);
 Assert.assertEquals(URI, resolver3.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver3.getPrefix(URI));
 Assert.assertEquals(parentResolver2, resolver3.getParent());
 Assert.assertEquals(parentResolver, parentResolver2.getParent());
 }

 @Test
 public void testGetParentNamespaceDeeper() {
 parentResolver.registerNamespace(PREFIX, URI);
 NamespaceResolver parentResolver2 = new NamespaceResolver(parentResolver);
 NamespaceResolver resolver3 = new NamespaceResolver(parentResolver2);
 NamespaceResolver parentResolver4 = new NamespaceResolver(parentResolver3);
 NamespaceContext context = new StandaloneContext(parentResolver4);
 Assert.assertEquals(URI, parentResolver4.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, parentResolver4.getPrefix(URI));
 Assert.assertEquals(parentResolver3, parentResolver4.getParent());
 Assert.assertEquals(parentResolver2, parentResolver3.getParent());
 Assert.assertEquals(parentResolver, parentResolver2.getParent());
 }

 @Test
 public void testGetNamespaceURIWithPointer() {
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 resolver.setNamespaceContextPointer(pointer);
 resolver.registerNamespace(PREFIX, URI);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 }

 @Test
 public void testGetNamespaceURIWithParentPointer() {
 NodePointer parentPointer = new NodePointer("test:root", new Object[] {});
 resolver.setParent(new NamespaceResolver());
 NamespaceResolver parentResolver = new NamespaceResolver(resolver);
 parentResolver.setNamespaceContextPointer(parentPointer);
 parentResolver.registerNamespace(PREFIX, URI);
 resolver = new NamespaceResolver(parentResolver);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(parentResolver, resolver.getParent());
 }

 @Test
 public void testGetNamespaceURIWithNamespaceContext() {
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 NamespaceContext context = new StandaloneContext(resolver);
 context.setNamespaceContext(new NamespaceContext() {
 @Override
 public String getNamespaceURI(String prefix) {
 if (PREFIX.equals(prefix)) {
 return URI;
 }
 return null;
 }

 @Override
 public String getPrefix(String namespaceURI) {
 if (URI.equals(namespaceURI)) {
 return PREFIX;
 }
 return null;
 }
 }, pointer);
 NamespaceContext context2 = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(context, context2);
 }

 @Test
 public void testGetNamespaceURIWithPointerAndNamespaceContext() {
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 NamespaceContext context = new StandaloneContext(resolver);
 context.setNamespaceContext(new NamespaceContext() {
 @Override
 public String getNamespaceURI(String prefix) {
 if (PREFIX.equals(prefix)) {
 return URI;
 }
 return null;
 }

 @Override
 public String getPrefix(String namespaceURI) {
 if (URI.equals(namespaceURI)) {
 return PREFIX;
 }
 return null;
 }

 @Override
 public String getNamespaceURI(NodePointer nodePointer) {
 return URI;
 }

 @Override
 public String getPrefix(NodePointer nodePointer) {
 return PREFIX;
 }
 }, pointer);
 NamespaceContext context2 = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(context, context2);
 Assert.assertEquals(URI, resolver.getNamespaceURI(pointer));
 Assert.assertEquals(PREFIX, resolver.getPrefix(pointer));
 }

 @Test
 public void testGetNamespaceURIWithNamespaceIterator() {
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 NodeIterator nodeIterator = new NodeIterator() {
 @Override
 public NodePointer next() {
 return pointer;
 }

 @Override
 public boolean hasNext() {
 return true;
 }
 };
 resolver.setNamespaceContextPointer(pointer);
 resolver.registerNamespace(PREFIX, URI);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 }

 @Test
 public void testGetNamespaceURIWithNamespaceIteratorAndPointer() {
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 NodePointer childPointer = new NodePointer("test:child", new Object[] {});
 NodeIterator nodeIterator = new NodeIterator() {
 @Override
 public NodePointer next() {
 return pointer;
 }

 @Override
 public boolean hasNext() {
 return true;
 }
 };
 resolver.setNamespaceContextPointer(pointer);
 resolver.registerNamespace(PREFIX, URI);
 NamespaceContext context = new StandaloneContext(resolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(URI, resolver.getNamespaceURI(childPointer));
 Assert.assertEquals(PREFIX, resolver.getPrefix(childPointer));
 }

 @Test
 public void testGetNamespaceURIWithNamespaceIteratorAndPointerAndParent() {
 NodePointer parentPointer = new NodePointer("test:parent", new Object[] {});
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 NodePointer childPointer = new NodePointer("test:child", new Object[] {});
 NodeIterator nodeIterator = new NodeIterator() {
 @Override
 public NodePointer next() {
 return pointer;
 }

 @Override
 public boolean hasNext() {
 return true;
 }
 };
 resolver.setNamespaceContextPointer(pointer);
 parentResolver = new NamespaceResolver();
 parentResolver.setNamespaceContextPointer(parentPointer);
 parentResolver.registerNamespace(PREFIX, URI);
 resolver = new NamespaceResolver(parentResolver);
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(URI, resolver.getNamespaceURI(childPointer));
 Assert.assertEquals(PREFIX, resolver.getPrefix(childPointer));
 Assert.assertEquals(parentResolver, resolver.getParent());
 }

 @Test
 public void testGetNamespaceURIWithNamespaceIteratorAndPointerAndParentAndSealed() {
 NodePointer parentPointer = new NodePointer("test:parent", new Object[] {});
 NodePointer pointer = new NodePointer("test:root", new Object[] {});
 NodePointer childPointer = new NodePointer("test:child", new Object[] {});
 NodeIterator nodeIterator = new NodeIterator() {
 @Override
 public NodePointer next() {
 return pointer;
 }

 @Override
 public boolean hasNext() {
 return true;
 }
 };
 resolver.setNamespaceContextPointer(pointer);
 parentResolver = new NamespaceResolver();
 parentResolver.setNamespaceContextPointer(parentPointer);
 parentResolver.registerNamespace(PREFIX, URI);
 resolver = new NamespaceResolver(parentResolver);
 resolver.seal();
 Assert.assertEquals(URI, resolver.getNamespaceURI(PREFIX));
 Assert.assertEquals(PREFIX, resolver.getPrefix(URI));
 Assert.assertEquals(URI, resolver.getNamespaceURI(childPointer));
 Assert.assertEquals(PREFIX, resolver.getPrefix(childPointer));
 Assert.assertEquals(parentResolver, resolver.getParent());
 }
}