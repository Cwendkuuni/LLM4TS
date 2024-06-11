package org.apache.commons.jxpath.ri;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

  private NamespaceResolver resolver;

  @Before
  public void setUp() {
    resolver = new NamespaceResolver();
  }

  @Test
  public void testRegisterNamespace() {
    resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
    resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
    
    String prefix1 = resolver.getPrefix("http://www.example.com/namespace1");
    String prefix2 = resolver.getPrefix("http://www.example.com/namespace2");
    
    Assert.assertEquals("prefix1", prefix1);
    Assert.assertEquals("prefix2", prefix2);
  }

  @Test
  public void testGetNamespaceURI() {
    resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
    resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");

    String namespaceURI1 = resolver.getNamespaceURI("prefix1");
    String namespaceURI2 = resolver.getNamespaceURI("prefix2");
    String namespaceURI3 = resolver.getNamespaceURI("prefix3");

    Assert.assertEquals("http://www.example.com/namespace1", namespaceURI1);
    Assert.assertEquals("http://www.example.com/namespace2", namespaceURI2);
    Assert.assertNull(namespaceURI3);
  }

  @Test
  public void testGetPrefix() {
    resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
    resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");

    String prefix1 = resolver.getPrefix("http://www.example.com/namespace1");
    String prefix2 = resolver.getPrefix("http://www.example.com/namespace2");
    String prefix3 = resolver.getPrefix("http://www.example.com/namespace3");

    Assert.assertEquals("prefix1", prefix1);
    Assert.assertEquals("prefix2", prefix2);
    Assert.assertNull(prefix3);
  }

  @Test
  public void testSeal() {
    resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
    resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");

    resolver.seal();
    boolean isSealed = resolver.isSealed();

    Assert.assertTrue(isSealed);
  }
}