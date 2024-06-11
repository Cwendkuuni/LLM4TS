import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  @Test
  public void testConstructor() {
    SimpleType simpleType = new SimpleType(Double.class);
    assertNotNull(simpleType);
  }

  @Test
  public void testCopyConstructor() {
    SimpleType base = new SimpleType(Double.class);
    SimpleType simpleType = new SimpleType(base);
    assertNotNull(simpleType);
  }

  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(Double.class);
    assertNotNull(simpleType);
  }

  @Test
  public void testConstruct() {
    SimpleType simpleType = SimpleType.construct(Double.class);
    assertNotNull(simpleType);
  }

  @Test
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(Double.class);
    JavaType javaType = simpleType._narrow(Double.class);
    assertNotNull(javaType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(Double.class);
    simpleType.withContentType(new SimpleType(Integer.class));
  }

  @Test
  public void testWithTypeHandler() {
    SimpleType simpleType = new SimpleType(Double.class);
    SimpleType newSimpleType = simpleType.withTypeHandler("handler");
    assertNotNull(newSimpleType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(Double.class);
    simpleType.withContentTypeHandler("handler");
  }

  @Test
  public void testWithValueHandler() {
    SimpleType simpleType = new SimpleType(Double.class);
    SimpleType newSimpleType = simpleType.withValueHandler("handler");
    assertNotNull(newSimpleType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(Double.class);
    simpleType.withContentValueHandler("handler");
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(Double.class);
    SimpleType newSimpleType = simpleType.withStaticTyping();
    assertNotNull(newSimpleType);
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(Double.class);
    JavaType javaType = simpleType.refine(Double.class, TypeBindings.emptyBindings(), null, null);
    assertNull(javaType);
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(Double.class);
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(Double.class);
    StringBuilder sb = new StringBuilder();
    simpleType.getErasedSignature(sb);
    assertEquals("Ljava/lang/Double;", sb.toString());
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(Double.class);
    StringBuilder sb = new StringBuilder();
    simpleType.getGenericSignature(sb);
    assertEquals("Ljava/lang/Double;", sb.toString());
  }

  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(Double.class);
    assertEquals("[simple type, class java.lang.Double]", simpleType.toString());
  }

  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(Double.class);
    SimpleType simpleType2 = new SimpleType(Double.class);
    assertTrue(simpleType1.equals(simpleType2));
  }
}