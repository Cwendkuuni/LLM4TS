import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

  @Test
  public void testGetName() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertEquals(new QName("*"), pointer.getName());
  }

  @Test
  public void testSetValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    
    // Test setting value with null parent
    try {
      pointer.setValue("test");
      fail("Expected JXPathInvalidAccessException");
    } catch (JXPathInvalidAccessException e) {
      // Exception is expected
    }
    
    // Test setting value with parent that does not support dynamic property declaration
    NodePointer parent = new PropertyOwnerPointer(null);
    pointer.setParent(parent);
    
    try {
      pointer.setValue("test");
      fail("Expected JXPathInvalidAccessException");
    } catch (JXPathInvalidAccessException e) {
      // Exception is expected
    }
    
    // Test setting value with parent that supports dynamic property declaration
    parent.setDynamicPropertyDeclarationSupported(true);
    
    try {
      pointer.setValue("test");
      // No exception thrown, test passes
    } catch (JXPathInvalidAccessException e) {
      fail("Unexpected JXPathInvalidAccessException");
    }
  }

  @Test
  public void testCreatePath() {
    JXPathContext context = new JXPathContext();

    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setParent(new PropertyOwnerPointer(null));
    
    // Test createPath with null parent
    try {
      pointer.createPath(context);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Exception is expected
    }
    
    // Test createPath with parent that returns PropertyOwnerPointer
    pointer.setParent(new PropertyOwnerPointer(null));
    
    try {
      pointer.createPath(context);
      // No exception thrown, test passes
    } catch (Exception e) {
      fail("Unexpected exception occurred");
    }
    
    // Test createPath with parent that returns non-PropertyOwnerPointer
    pointer.setParent(new NullPointer(null));
    
    try {
      pointer.createPath(context);
      // No exception thrown, test passes
    } catch (Exception e) {
      fail("Unexpected exception occurred");
    }
  }

  @Test
  public void testCreatePathWithValue() {
    JXPathContext context = new JXPathContext();

    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setParent(new PropertyOwnerPointer(null));
    
    // Test createPath with null parent
    try {
      pointer.createPath(context, "test");
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Exception is expected
    }
    
    // Test createPath with parent that returns PropertyOwnerPointer
    pointer.setParent(new PropertyOwnerPointer(null));
    
    try {
      pointer.createPath(context, "test");
      // No exception thrown, test passes
    } catch (Exception e) {
      fail("Unexpected exception occurred");
    }
    
    // Test createPath with parent that returns non-PropertyOwnerPointer
    pointer.setParent(new NullPointer(null));
    
    try {
      pointer.createPath(context, "test");
      // No exception thrown, test passes
    } catch (Exception e) {
      fail("Unexpected exception occurred");
    }
  }

  @Test
  public void testCreateChild() {
    JXPathContext context = new JXPathContext();

    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setParent(new PropertyOwnerPointer(null));
    
    // Test createChild with null parent
    try {
      pointer.createChild(context, new QName("*"), 0);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Exception is expected
    }
    
    // Test createChild with parent that returns PropertyOwnerPointer
    pointer.setParent(new PropertyOwnerPointer(null));
    
    try {
      pointer.createChild(context, new QName("*"), 0);
      // No exception thrown, test passes
    } catch (Exception e) {
      fail("Unexpected exception occurred");
    }
    
    // Test createChild with parent that returns non-PropertyOwnerPointer
    pointer.setParent(new NullPointer(null));
    
    try {
      pointer.createChild(context, new QName("*"), 0);
      // No exception thrown, test passes
    } catch (Exception e) {
      fail("Unexpected exception occurred");
    }
  }
  
  @Test
  public void testIsCollection() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    
    // Test when index is set to WHOLE_COLLECTION
    pointer.setIndex(PropertyPointer.WHOLE_COLLECTION);
    assertFalse(pointer.isCollection());
    
    // Test when index is set to a value other than WHOLE_COLLECTION
    pointer.setIndex(0);
    assertTrue(pointer.isCollection());
  }
  
  @Test
  public void testGetPropertyCount() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertEquals(0, pointer.getPropertyCount());
  }
  
  @Test
  public void testGetPropertyNames() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertArrayEquals(new String[0], pointer.getPropertyNames());
  }
  
  @Test
  public void testAsPath() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    
    pointer.setPropertyName("*");
    assertEquals("/", pointer.asPath());
    
    pointer.setNameAttributeValue("test");
    assertEquals("/", pointer.asPath());
    
    pointer.setIndex(0);
    assertEquals("/", pointer.asPath());
    
    pointer.setByAttribute(true);
    assertEquals("/", pointer.asPath());
    
    pointer.setNameAttributeValue("test");
    pointer.setIndex(0);
    pointer.setByAttribute(true);
    assertEquals("/[@name='test']", pointer.asPath());
  }
  
  @Test
  public void testEscape() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    
    assertEquals("test", pointer.escape("test"));
    assertEquals("te&quot;st", pointer.escape("te\"st"));
    assertEquals("apos;testapos;", pointer.escape("test'"));
    assertEquals("apos;testapos; and &quot;text&quot;", pointer.escape("test' and \"text\""));
  }
}