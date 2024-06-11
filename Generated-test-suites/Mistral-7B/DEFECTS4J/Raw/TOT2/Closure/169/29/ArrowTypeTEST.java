import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithValidParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    JSType returnType = registry.createNamedType("String");
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType expectedArrowType = new ArrowType(registry, parameters, returnType, false);
    assertEquals(expectedArrowType, arrowType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    ArrowType arrowType = new ArrowType(registry, parameters, null);
  }

  @Test
  public void testArrowTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    Node parameters2 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    JSType returnType1 = registry.createNamedType("String");
    JSType returnType2 = registry.createNamedType("String");

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, false);

    ArrowType expectedArrowType = new ArrowType(registry, parameters1, returnType1, false);
    assertTrue(arrowType1.equals(expectedArrowType));
    assertTrue(expectedArrowType.equals(arrowType1));

    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType2, false);
    assertFalse(arrowType1.equals(arrowType3));
    assertFalse(arrowType3.equals(arrowType1));

    ArrowType arrowType4 = new ArrowType(registry, parameters2, returnType1, false);
    assertFalse(arrowType1.equals(arrowType4));
    assertFalse(arrowType4.equals(arrowType1));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    Node parameters2 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    JSType returnType1 = registry.createNamedType("String");
    JSType returnType2 = registry.createNamedType("String");

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, false);
    int hashCode1 = arrowType1.hashCode();

    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType1, false);
    int hashCode2 = arrowType2.hashCode();

    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType2, false);
    int hashCode3 = arrowType3.hashCode();

    ArrowType arrowType4 = new ArrowType(registry, parameters2, returnType2, false);
    int hashCode4 = arrowType4.hashCode();

    ArrowType arrowType5 = new ArrowType(registry, parameters1, returnType1, true);
    int hashCode5 = arrowType5.hashCode();

    assertEquals(hashCode1, arrowType1.hashCode());
    assertEquals(hashCode2, arrowType2.hashCode());
    assertNotEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode1, hashCode4);
    assertNotEquals(hashCode1, hashCode5);
    assertNotEquals(hashCode2, hashCode3);
    assertNotEquals(hashCode2, hashCode4);
    assertNotEquals(hashCode2, hashCode5);
    assertNotEquals(hashCode3, hashCode4);
    assertNotEquals(hashCode3, hashCode5);
    assertNotEquals(hashCode4, hashCode5);
  }
}