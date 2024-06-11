import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArrowTypeTest {
  private static JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeWithNoParamsAndNoReturnType() {
    Node parameters = null;
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
    assertEquals(UNKNOWN_TYPE, arrowType.getNativeType());
  }

  @Test
  public void testArrowTypeWithOneParamAndNoReturnType() {
    Node parameters = registry.createParameter("number");
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSTypeNative.NUMBER, arrowType.getParameters().getFirstChild().getJSType().getNativeType());
    assertEquals(UNKNOWN_TYPE, arrowType.getNativeType());
  }

  @Test
  public void testArrowTypeWithOneParamAndReturnType() {
    Node parameters = registry.createParameter("number");
    JSType returnType = registry.createPrimitiveType("boolean");
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSTypeNative.NUMBER, arrowType.getParameters().getFirstChild().getJSType().getNativeType());
    assertEquals(JSTypeNative.BOOLEAN, arrowType.getNativeType());
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node parameters = registry.createParameter("number...");
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSTypeNative.NUMBER, arrowType.getParameters().getFirstChild().getJSType().getNativeType());
    assertEquals(UNKNOWN_TYPE, arrowType.getNativeType());
  }

  @Test
  public void testArrowTypeWithNoParamAndReturnTypeInferred() {
    Node parameters = null;
    JSType returnType = registry.createPrimitiveType("boolean");
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);

    assertEquals(UNKNOWN_TYPE, arrowType.getNativeType());
  }

  @Test
  public void testArrowTypeWithOneParamAndReturnTypeInferred() {
    Node parameters = registry.createParameter("number");
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);

    assertEquals(UNKNOWN_TYPE, arrowType.getNativeType());
    assertEquals(JSTypeNative.NUMBER, arrowType.getParameters().getFirstChild().getJSType().getNativeType());
  }

  @Test
  public void testArrowTypeWithOneParamAndReturnTypeBothInferred() {
    Node parameters = registry.createParameter("number");
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true, true);

    assertEquals(UNKNOWN_TYPE, arrowType.getNativeType());
    assertEquals(UNKNOWN_TYPE, arrowType.getParameters().getFirstChild().getJSType().getNativeType());
  }

  @Test
  public void testArrowTypeEquality() {
    Node p1 = registry.createParameter("number");
    Node p2 = registry.createParameter("number");
    JSType r1 = registry.createPrimitiveType("boolean");
    JSType r2 = registry.createPrimitiveType("boolean");

    ArrowType arrowType1 = new ArrowType(registry, p1, r1);
    ArrowType arrowType2 = new ArrowType(registry, p2, r2);
    ArrowType arrowType3 = new ArrowType(registry, p1, r2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType1));
  }
}