import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

  private NamedType namedType;

  @Before
  public void setUp() {
    namedType = new NamedType(null, "reference", "sourceName", 1, 1);
  }

  @Test
  public void testGetReferencedType() {
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("reference", namedType.getReferenceName());
  }

  @Test
  public void testToStringHelper() {
    assertEquals("reference", namedType.toStringHelper(false));
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
    int expectedHashCode = "reference".hashCode();
    assertEquals(expectedHashCode, namedType.hashCode());
  }
}