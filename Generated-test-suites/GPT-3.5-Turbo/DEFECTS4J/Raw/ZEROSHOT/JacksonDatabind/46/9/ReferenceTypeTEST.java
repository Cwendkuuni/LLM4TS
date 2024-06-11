import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

  @Test
  public void testConstruct() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertNotNull(refType);
    assertEquals(String.class, refType.getReferencedType().getRawClass());
  }

  @Test
  public void testWithTypeHandler() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    Object handler = new Object();
    ReferenceType refTypeWithHandler = refType.withTypeHandler(handler);
    assertNotSame(refType, refTypeWithHandler);
    assertEquals(handler, refTypeWithHandler.getTypeHandler());
  }

  @Test
  public void testWithContentTypeHandler() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    Object handler = new Object();
    ReferenceType refTypeWithHandler = refType.withContentTypeHandler(handler);
    assertNotSame(refType, refTypeWithHandler);
    assertEquals(handler, refTypeWithHandler.getReferencedType().getTypeHandler());
  }

  @Test
  public void testWithValueHandler() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    Object handler = new Object();
    ReferenceType refTypeWithHandler = refType.withValueHandler(handler);
    assertNotSame(refType, refTypeWithHandler);
    assertEquals(handler, refTypeWithHandler.getValueHandler());
  }

  @Test
  public void testWithContentValueHandler() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    Object handler = new Object();
    ReferenceType refTypeWithHandler = refType.withContentValueHandler(handler);
    assertNotSame(refType, refTypeWithHandler);
    assertEquals(handler, refTypeWithHandler.getReferencedType().getValueHandler());
  }

  @Test
  public void testWithStaticTyping() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    ReferenceType refTypeWithStaticTyping = refType.withStaticTyping();
    assertNotSame(refType, refTypeWithStaticTyping);
    assertTrue(refTypeWithStaticTyping.isStaticTyping());
  }

  @Test
  public void testBuildCanonicalName() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    String canonicalName = refType.buildCanonicalName();
    assertEquals("java.lang.String", canonicalName);
  }

  @Test
  public void testNarrow() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    ReferenceType narrowedRefType = refType._narrow(CharSequence.class);
    assertNotSame(refType, narrowedRefType);
    assertEquals(CharSequence.class, narrowedRefType.getRawClass());
  }

  @Test
  public void testGetReferencedType() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertNotNull(refType.getReferencedType());
    assertEquals(String.class, refType.getReferencedType().getRawClass());
  }

  @Test
  public void testIsReferenceType() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertTrue(refType.isReferenceType());
  }

  @Test
  public void testContainedTypeCount() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertEquals(1, refType.containedTypeCount());
  }

  @Test
  public void testContainedType() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertEquals(String.class, refType.containedType(0).getRawClass());
    assertEquals(null, refType.containedType(1));
  }

  @Test
  public void testContainedTypeName() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertEquals("T", refType.containedTypeName(0));
    assertEquals(null, refType.containedTypeName(1));
  }

  @Test
  public void testGetParameterSource() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    assertEquals(String.class, refType.getParameterSource());
  }

  @Test
  public void testGetErasedSignature() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    StringBuilder sb = new StringBuilder();
    StringBuilder erasedSignature = refType.getErasedSignature(sb);
    assertNotNull(erasedSignature);
    assertTrue(erasedSignature.length() > 0);
  }

  @Test
  public void testGetGenericSignature() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    StringBuilder sb = new StringBuilder();
    StringBuilder genericSignature = refType.getGenericSignature(sb);
    assertNotNull(genericSignature);
    assertTrue(genericSignature.length() > 0);
  }

  @Test
  public void testToString() {
    ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
    String toString = refType.toString();
    assertNotNull(toString);
    assertTrue(toString.length() > 0);
  }

  @Test
  public void testEquals() {
    ReferenceType refType1 = ReferenceType.construct(String.class, null, null, null);
    ReferenceType refType2 = ReferenceType.construct(String.class, null, null, null);
    assertTrue(refType1.equals(refType1));
    assertFalse(refType1.equals(null));
    assertFalse(refType1.equals(new Object()));
    assertFalse(refType1.equals(refType2));
  }
}