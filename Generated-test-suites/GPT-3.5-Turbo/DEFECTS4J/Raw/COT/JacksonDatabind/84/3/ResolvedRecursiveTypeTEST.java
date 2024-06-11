import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;

public class ResolvedRecursiveTypeTest {

  @Test
  public void testSetReference() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    JavaType ref = null; // update with appropriate value
    resolvedRecursiveType.setReference(ref);

    assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
  }

  @Test
  public void testGetGenericSignature() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    StringBuilder sb = new StringBuilder();
    StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

    // assert the correctness of the result
  }

  @Test
  public void testGetErasedSignature() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    StringBuilder sb = new StringBuilder();
    StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

    // assert the correctness of the result
  }

  @Test
  public void testWithContentType() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    JavaType contentType = null; // update with appropriate value
    JavaType result = resolvedRecursiveType.withContentType(contentType);

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void testWithTypeHandler() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    Object h = null; // update with appropriate value
    JavaType result = resolvedRecursiveType.withTypeHandler(h);

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void testWithContentTypeHandler() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    Object h = null; // update with appropriate value
    JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void testWithValueHandler() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    Object h = null; // update with appropriate value
    JavaType result = resolvedRecursiveType.withValueHandler(h);

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void testWithContentValueHandler() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    Object h = null; // update with appropriate value
    JavaType result = resolvedRecursiveType.withContentValueHandler(h);

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void testWithStaticTyping() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    JavaType result = resolvedRecursiveType.withStaticTyping();

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void test_narrow() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    Class<?> subclass = null; // update with appropriate value
    JavaType result = resolvedRecursiveType._narrow(subclass);

    assertEquals(resolvedRecursiveType, result);
  }

  @Test
  public void testRefine() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    Class<?> rawType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    JavaType superClass = null; // update with appropriate value
    JavaType[] superInterfaces = null; // update with appropriate value
    JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

    // assert the correctness of the result
  }

  @Test
  public void testIsContainerType() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    boolean result = resolvedRecursiveType.isContainerType();

    // assert the correctness of the result
  }

  @Test
  public void testToString() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

    String result = resolvedRecursiveType.toString();

    // assert the correctness of the result
  }

  @Test
  public void testEquals() {
    Class<?> erasedType = null; // update with appropriate value
    TypeBindings bindings = null; // update with appropriate value
    ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
    ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);

    boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

    // assert the correctness of the result
  }

}