package com.google.javascript.rhino.jstype;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class NamedTypeTest {

  private static NamedType namedType;

  @BeforeClass
  public static void setUp() {
    namedType = new NamedType(null, "reference", "sourceName", 1, 1);
  }

  @Test
  public void testGetReferencedType() {
    assertEquals(null, namedType.getReferencedType());
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("reference", namedType.getReferenceName());
  }

  @Test
  public void testToStringHelper() {
    assertEquals("reference", namedType.toStringHelper(true));
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
    assertEquals(84122231, namedType.hashCode());
  }
}