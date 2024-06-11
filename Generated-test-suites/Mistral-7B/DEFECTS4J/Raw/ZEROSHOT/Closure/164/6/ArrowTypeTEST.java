import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray();
    JSType returnType = getNativeType(INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals(INT.getJSType(), arrowType.getLeastSupertype(ANY));
    assertEquals(INT.getJSType(), arrowType.getGreatestSubtype(INT.getJSType()));
    assertEquals(INT.getJSType(), arrowType.getGreatestSubtype(ANY));
    assertTrue(arrowType.isSubtype(new ArrowType(registry, Node.newArray(), INT.getJSType())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, Node.newArray(INT.getJSType()), INT.getJSType())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, Node.newArray(INT.getJSType(), INT.getJSType()), INT.getJSType())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, Node.newArray(), STRING.getJSType())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, Node.newArray(), VOID.getJSType())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, Node.newArray(INT.getJSType()), VOID.getJSType())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, Node.newArray(INT.getJSType(), INT.getJSType()), VOID.getJSType())));
  }

  @Test
  public void testArrowTypeWithUnknownTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray();
    JSType returnType = UNKNOWN_TYPE.getJSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray();
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeWithSameParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node firstParameters = Node.newArray();
    Node secondParameters = Node.newArray(Node.newIdent("a"));
    JSType returnType = INT.getJSType();
    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeWithDifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node firstParameters = Node.newArray(Node.newIdent("a"), Node.newIdent("b"));
    Node secondParameters = Node.newArray(Node.newIdent("c"));
    JSType returnType = INT.getJSType();
    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType);
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node firstParameters = Node.newArray(Node.newIdent("a"), Node.newIdent("b"), Node.newIdent("c"), Node.newIdent("d"));
    Node secondParameters = Node.newArray(Node.newIdent("a"), Node.newIdent("b"), Node.newIdent("e"));
    JSType returnType = INT.getJSType();
    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeWithInferredReturnTypeAndSameParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray();
    JSType returnType = null;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, true);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, true);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeWithInferredReturnTypeAndDifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node firstParameters = Node.newArray(Node.newIdent("a"), Node.newIdent("b"));
    Node secondParameters = Node.newArray(Node.newIdent("c"));
    JSType returnType = null;
    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType, true);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType, true);
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }
}