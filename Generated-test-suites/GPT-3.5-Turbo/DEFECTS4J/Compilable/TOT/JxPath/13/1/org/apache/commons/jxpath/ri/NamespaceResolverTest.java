package org.apache.commons.jxpath.ri;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

  private NamespaceResolver nsResolver;

  @Before
  public void setUp() {
    nsResolver = new NamespaceResolver();
  }

  @Test
  public void testRegisterNamespace() {
    nsResolver.registerNamespace("prefix1", "http://www.example1.com");

    String actualUri = nsResolver.getNamespaceURI("prefix1");
    assertEquals("http://www.example1.com", actualUri);
  }

  @Test(expected = IllegalStateException.class)
  public void testSealedNamespaceResolver() {
    nsResolver.seal();
    nsResolver.registerNamespace("prefix1", "http://www.example1.com");
  }

  @Test
  public void testGetNamespaceURI() {
    nsResolver.registerNamespace("prefix1", "http://www.example1.com");
    nsResolver.registerNamespace("prefix2", "http://www.example2.com");

    String actualUri1 = nsResolver.getNamespaceURI("prefix1");
    assertEquals("http://www.example1.com", actualUri1);

    String actualUri2 = nsResolver.getNamespaceURI("prefix2");
    assertEquals("http://www.example2.com", actualUri2);

    String actualUri3 = nsResolver.getNamespaceURI("prefix3");
    assertNull(actualUri3);
  }

  @Test
  public void testGetPrefix() {
    nsResolver.registerNamespace("prefix1", "http://www.example1.com");
    nsResolver.registerNamespace("prefix2", "http://www.example2.com");

    String actualPrefix1 = nsResolver.getPrefix("http://www.example1.com");
    assertEquals("prefix1", actualPrefix1);

    String actualPrefix2 = nsResolver.getPrefix("http://www.example2.com");
    assertEquals("prefix2", actualPrefix2);

    String actualPrefix3 = nsResolver.getPrefix("http://www.example3.com");
    assertNull(actualPrefix3);
  }

  @Test
  public void testClone() {
    nsResolver.registerNamespace("prefix1", "http://www.example1.com");
    nsResolver.registerNamespace("prefix2", "http://www.example2.com");

    NamespaceResolver clonedResolver = (NamespaceResolver)nsResolver.clone();
    assertFalse(clonedResolver.isSealed());

    String actualUri1 = clonedResolver.getNamespaceURI("prefix1");
    assertEquals("http://www.example1.com", actualUri1);

    String actualUri2 = clonedResolver.getNamespaceURI("prefix2");
    assertEquals("http://www.example2.com", actualUri2);
  }

  @Test
  public void testGetNamespaceContextPointer() {
    // This test case requires a mock object for NodePointer.
    // The mock object should be set to the namespaceContextPointer of the NamespaceResolver
    // and then we need to verify if the correct NodePointer is returned by the getter method.
  }

  @Test
  public void testSetNamespaceContextPointer() {
    // This test case requires a mock object for NodePointer.
    // We need to set the mock object to the namespaceContextPointer of the NamespaceResolver
    // and then we need to verify if the correct NodePointer is set.
  }

}