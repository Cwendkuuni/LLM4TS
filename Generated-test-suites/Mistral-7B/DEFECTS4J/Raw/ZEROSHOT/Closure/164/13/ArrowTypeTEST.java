import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    Node params = Node.newList();
    Node param1 = Node.newName("x");
    Node param2 = Node.newName("y");
    params.setJSType(registry.createParameters(param1, param2));

    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals("Expected correct ArrowType instance", arrowType.getClass(), ArrowType.class);
    assertEquals("Expected correct number of parameters", 2, arrowType.parameters.getChildCount());
    assertEquals("Expected correct parameter types", INT.getClass(), arrowType.parameters.getChild(0).getJSType().getClass());
    assertEquals("Expected correct parameter types", INT.getClass(), arrowType.parameters.getChild(1).getJSType().getClass());
    assertEquals("Expected correct returnType", returnType, arrowType.returnType);
    assertFalse("Expected returnType not inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    Node params = Node.newList();
    Node param1 = Node.newName("x");

    ArrowType arrowType = new ArrowType(registry, params, null, true);

    assertEquals("Expected correct ArrowType instance", arrowType.getClass(), ArrowType.class);
    assertEquals("Expected correct number of parameters", 1, arrowType.parameters.getChildCount());
    assertEquals("Expected null parameter types", null, arrowType.parameters.getChild(0).getJSType());
    assertTrue("Expected returnTypeInferred to be true", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithEmptyParameterList() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertEquals("Expected correct ArrowType instance", arrowType.getClass(), ArrowType.class);
    assertNull("Expected empty parameter list", arrowType.parameters);
    assertNull("Expected null returnType", arrowType.returnType);
  }

  @Test
  public void testArrowTypeCreationWithVarArgs() {
    Node params = Node.newList();
    Node param1 = Node.newName("x");
    Node varargs = Node.newName("args");
    params.setJSType(registry.createParametersWithVarArgs(param1, varargs));

    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals("Expected correct ArrowType instance", arrowType.getClass(), ArrowType.class);
    assertEquals("Expected correct number of parameters", 2, arrowType.parameters.getChildCount());
    assertEquals("Expected correct parameter types", INT.getClass(), arrowType.parameters.getChild(0).getJSType().getClass());
    assertTrue("Expected params to contain varargs", arrowType.parameters.getChild(1).isVarArgs());
    assertEquals("Expected correct returnType", returnType, arrowType.returnType);
    assertFalse("Expected returnType not inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"));
    Node params2 = Node.newList(Node.newName("x"));

    JSType returnType1 = INT;
    JSType returnType2 = VOID;

    ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, params2, returnType2);

    assertFalse("Expected arrowType1 not to be a subtype of arrowType2", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithVarArgs() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"), Node.newName("args"));
    Node params2 = Node.newList(Node.newName("x"), Node.newName("args"));

    JSType returnType1 = INT;
    JSType returnType2 = VOID;

    ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, params2, returnType2);

    assertTrue("Expected arrowType1 to be a subtype of arrowType2", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithInferredReturnType() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"));
    Node params2 = Node.newList(Node.newName("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, null, true);

    assertTrue("Expected arrowType1 to be a subtype of arrowType2", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithSingleParameter() {
    Node params1 = Node.newList(Node.newName("x"));
    Node params2 = Node.newList();

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, null, null);

    assertTrue("Expected arrowType1 to be a subtype of arrowType2", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"));
    Node params2 = Node.newList(Node.newName("y"), Node.newName("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertFalse("Expected arrowType1 not to have equal parameters to arrowType2", arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeHasEqualParametersWithVarArgs() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"), Node.newName("args"));
    Node params2 = Node.newList(Node.newName("x"), Node.newName("args"), Node.newName("y"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertFalse("Expected arrowType1 not to have equal parameters to arrowType2", arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeHasEqualParametersWithSingleParameter() {
    Node params1 = Node.newList(Node.newName("x"));
    Node params2 = Node.newList(Node.newName("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertTrue("Expected arrowType1 to have equal parameters to arrowType2", arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"));
    Node params2 = Node.newList(Node.newName("y"), Node.newName("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertFalse("Expected arrowType1 not to be equivalent to arrowType2", arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeIsEquivalentToWithVarArgs() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"), Node.newName("args"));
    Node params2 = Node.newList(Node.newName("x"), Node.newName("args"), Node.newName("y"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertFalse("Expected arrowType1 not to be equivalent to arrowType2", arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeIsEquivalentToWithSingleParameter() {
    Node params1 = Node.newList(Node.newName("x"));
    Node params2 = Node.newList(Node.newName("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertTrue("Expected arrowType1 to be equivalent to arrowType2", arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node params1 = Node.newList(Node.newName("x"), Node.newName("y"));
    Node params2 = Node.newList(Node.newName("y"), Node.newName("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, INT);

    assertNotEquals("Expected arrowType1 and arrowType2 hashcodes to be equal", arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeHashCodeWithInferredReturnType() {
    Node params = Node.newList(Node.newName("x"), Node.newName("y"));

    ArrowType arrowType1 = new ArrowType(registry, params, INT);
    ArrowType arrowType2 = new ArrowType(registry, params, null, true);

    assertNotEquals("Expected arrowType1 and arrowType2 hashcodes to be equal", arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeHashCodeWithEmptyParameterList() {
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);

    assertEquals("Expected arrowType1 and arrowType2 hashcodes to be equal", arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeToString() {
    Node params = Node.newList(Node.newName("x"), Node.newName("y"));
    JSType returnType = INT;

    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals(
        "Expected correct ArrowType string representation",
        "function(x: any, y: any) => any",
        arrowType.toString());
  }

  @Test
  public void testArrowTypeToStringWithInferredReturnType() {
    Node params = Node.newList(Node.newName("x"), Node.newName("y"));

    ArrowType arrowType = new ArrowType(registry, params, null, true);

    assertEquals(
        "Expected correct ArrowType string representation",
        "function(x: any, y: any)",
        arrowType.toString());
  }

  @Test
  public void testArrowTypeToStringWithEmptyParameterList() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertEquals(
        "Expected correct ArrowType string representation",
        "function() => any",
        arrowType.toString());
  }

  @Test
  public void testArrowTypeToStringWithVarArgs() {
    Node params = Node.newList(Node.newName("x"), Node.newName("y"), Node.newName("args"));
    JSType returnType = INT;

    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals(
        "Expected correct ArrowType string representation",
        "function(x: any, y: any, ...args: any[]) => any",
        arrowType.toString());
  }
}