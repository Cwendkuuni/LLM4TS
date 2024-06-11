import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  private NamedType namedType;

  @Before
  public void setUp() {
    namedType = new NamedType(null, "com.example.mytype", "", 0, 0);
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("com.example.mytype", namedType.getReferenceName());
  }

  @Test
  public void testToStringHelper() {
    assertEquals("com.example.mytype", namedType.toStringHelper(false));
  }

  @Test
  public void testHasReferenceName() {
    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testHashCode() {
    assertEquals("com.example.mytype".hashCode(), namedType.hashCode());
  }
  
  @Test
  public void testResolveInternal_WithValidType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    registry.addType("com.example.mytype", objType);
    namedType = new NamedType(registry, "com.example.mytype", "", 0, 0);
    
    namedType.resolveInternal(null, null);
    
    assertEquals(objType, namedType.getReferencedType());
    assertEquals(objType, namedType.getResolvedType());
  }
  
  @Test
  public void testResolveInternal_WithInvalidType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    namedType = new NamedType(registry, "com.example.mytype", "", 0, 0);
    
    namedType.resolveInternal(null, null);
    
    assertEquals(JSTypeNative.UNKNOWN_TYPE,
                 namedType.getReferencedType());
    assertEquals(JSTypeNative.UNRESOLVED_TYPE,
                 namedType.getResolvedType());
  }
  
  @Test
  public void testResolveInternal_WithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType unresolvedType =
        new NamedType(registry, "com.example.unresolvedtype", "", 0, 0);
    namedType = new NamedType(registry, "com.example.mytype", "", 0, 0);
    namedType.setResolvedType(unresolvedType);
    
    namedType.resolveInternal(null, null);
    
    assertEquals(unresolvedType.getReferencedType(),
                 namedType.getReferencedType());
    assertEquals(unresolvedType.getResolvedType(),
                 namedType.getResolvedType());
  }

}