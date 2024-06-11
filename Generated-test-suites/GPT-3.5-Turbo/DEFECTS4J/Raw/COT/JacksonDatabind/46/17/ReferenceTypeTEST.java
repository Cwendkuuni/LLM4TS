
import org.junit.Test;

public class ReferenceTypeTest {
  
  @Test
  public void testConstruct() {
    Class<?> cls = String.class;
    JavaType refType = new SimpleType(String.class);
    Object valueHandler = null;
    Object typeHandler = null;

    ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

    assertEquals(cls, referenceType.getRawClass());
    assertEquals(refType, referenceType.getReferencedType());
    assertNull(referenceType.getValueHandler());
    assertNull(referenceType.getTypeHandler());
    assertFalse(referenceType.isContainerType());
    assertTrue(referenceType.isReferenceType());
  }

  @Test
  public void testWithTypeHandler() {
    Object typeHandler = new TypeHandler();

    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    ReferenceType result = referenceType.withTypeHandler(typeHandler);

    assertNotSame(referenceType, result);
    assertEquals(typeHandler, result.getTypeHandler());
    assertEquals(referenceType.getRawClass(), result.getRawClass());
    assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    assertEquals(referenceType.getValueHandler(), result.getValueHandler());
    assertFalse(result.isContainerType());
    assertTrue(result.isReferenceType());
  }

  @Test
  public void testWithContentTypeHandler() {
    Object contentTypeHandler = new TypeHandler();

    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    ReferenceType result = referenceType.withContentTypeHandler(contentTypeHandler);

    assertNotSame(referenceType, result);
    assertEquals(contentTypeHandler, result.getContentTypeHandler());
    assertEquals(referenceType.getRawClass(), result.getRawClass());
    assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    assertEquals(referenceType.getValueHandler(), result.getValueHandler());
    assertFalse(result.isContainerType());
    assertTrue(result.isReferenceType());
  }

  @Test
  public void testWithValueHandler() {
    Object valueHandler = new ValueHandler();

    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    ReferenceType result = referenceType.withValueHandler(valueHandler);

    assertNotSame(referenceType, result);
    assertEquals(valueHandler, result.getValueHandler());
    assertEquals(referenceType.getRawClass(), result.getRawClass());
    assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    assertEquals(referenceType.getTypeHandler(), result.getTypeHandler());
    assertFalse(result.isContainerType());
    assertTrue(result.isReferenceType());
  }

  @Test
  public void testWithContentValueHandler() {
    Object contentValueHandler = new ValueHandler();

    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    ReferenceType result = referenceType.withContentValueHandler(contentValueHandler);

    assertNotSame(referenceType, result);
    assertEquals(contentValueHandler, result.getContentValueHandler());
    assertEquals(referenceType.getRawClass(), result.getRawClass());
    assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    assertEquals(referenceType.getTypeHandler(), result.getTypeHandler());
    assertFalse(result.isContainerType());
    assertTrue(result.isReferenceType());
  }

  @Test
  public void testWithStaticTyping() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    ReferenceType result = referenceType.withStaticTyping();

    assertNotSame(referenceType, result);
    assertTrue(result.isStaticTyping());
    assertEquals(referenceType.getRawClass(), result.getRawClass());
    assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    assertEquals(referenceType.getTypeHandler(), result.getTypeHandler());
    assertEquals(referenceType.getValueHandler(), result.getValueHandler());
    assertFalse(result.isContainerType());
    assertTrue(result.isReferenceType());
  }

  @Test
  public void testGetReferencedType() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    JavaType referencedType = referenceType.getReferencedType();

    assertEquals(new SimpleType(String.class), referencedType);
  }

  @Test
  public void testIsReferenceType() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    assertTrue(referenceType.isReferenceType());
  }

  @Test
  public void testContainedTypeCount() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    int count = referenceType.containedTypeCount();

    assertEquals(1, count);
  }

  @Test
  public void testContainedType() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    JavaType containedType = referenceType.containedType(0);

    assertEquals(new SimpleType(String.class), containedType);
  }

  @Test
  public void testContainedTypeName() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    String typeName = referenceType.containedTypeName(0);

    assertEquals("T", typeName);
  }

  @Test
  public void testGetParameterSource() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    Class<?> parameterSource = referenceType.getParameterSource();

    assertEquals(String.class, parameterSource);
  }

  @Test
  public void testGetErasedSignature() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    StringBuilder sb = new StringBuilder();
    referenceType.getErasedSignature(sb);

    assertEquals("Ljava/lang/String;", sb.toString());
  }

  @Test
  public void testGetGenericSignature() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    StringBuilder sb = new StringBuilder();
    referenceType.getGenericSignature(sb);

    assertEquals("Ljava/lang/String<Ljava/lang/String;", sb.toString());
  }

  @Test
  public void testToString() {
    ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(String.class));

    String str = referenceType.toString();

    assertEquals("[reference type, class java.lang.String<java.lang.String>]", str);
  }

  @Test
  public void testEquals() {
    ReferenceType referenceType1 = new ReferenceType(String.class, new SimpleType(String.class));
    ReferenceType referenceType2 = new ReferenceType(String.class, new SimpleType(String.class));

    assertTrue(referenceType1.equals(referenceType2));
  }
  
}
