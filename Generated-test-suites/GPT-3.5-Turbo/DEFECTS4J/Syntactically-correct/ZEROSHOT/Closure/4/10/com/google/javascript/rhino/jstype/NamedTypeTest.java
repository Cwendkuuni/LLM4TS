import junit.framework.TestCase;

public class NamedTypeTest extends TestCase {

  public void testGetReferencedType() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    namedType.setReferencedType(null);
    assertNull(namedType.getReferencedType());
  }

  public void testGetReferenceName() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertEquals("reference", namedType.getReferenceName());
  }

  public void testHasReferenceName() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertTrue(namedType.hasReferenceName());
  }

  public void testIsNamedType() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertTrue(namedType.isNamedType());
  }

  public void testIsNominalType() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  public void testToStringHelper() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertNull(namedType.toStringHelper(false));
  }

  public void testIsNominalType() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  public void testHashCode() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertEquals(610373379, namedType.hashCode());
  }
  
  public void testResolveInternal() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    namedType.resolveInternal(null, null);
    assertNull(namedType.getResovedTypeInternal());
  }

  public void testSetValidator() {
    NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
    assertTrue(namedType.setValidator(null));
  }

}