import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.Test
  public void testArrowTypeWithParametersAndReturnType() {
    Node parameters = Node.newList();
    parameters.addChild(Node.newIdentifier("param1"));
    parameters.addChild(Node.newIdentifier("param2"));

    JSType returnType = registry.createPrimitiveType("Number");

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubtype(arrowType));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testArrowTypeWithInvalidReturnType() {
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    assertFalse(arrowType.isSubtype(new JSTypeNative(JSTypeNative.UNKNOWN_TYPE)));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testArrowTypeWithNullReturnType() {
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    assertFalse(arrowType.isSubtype(new JSTypeNative(JSTypeNative.NUMBER_TYPE)));
  }

  @org.junit.Test
  public void testArrowTypeWithEqualParameters() {
    Node thisParameters = Node.newList();
    thisParameters.addChild(Node.newIdentifier("param1"));
    thisParameters.addChild(Node.newIdentifier("param2"));

    Node thatParameters = Node.newList();
    thatParameters.addChild(Node.newIdentifier("param1"));
    thatParameters.addChild(Node.newIdentifier("param2"));

    JSType returnType = registry.createPrimitiveType("Number");

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, returnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, returnType);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @org.junit.Test
  public void testArrowTypeWithUnequalParameters() {
    Node thisParameters = Node.newList();
    thisParameters.addChild(Node.newIdentifier("param1"));
    thisParameters.addChild(Node.newIdentifier("param2"));

    Node thatParameters = Node.newList();
    thatParameters.addChild(Node.newIdentifier("param2"));
    thatParameters.addChild(Node.newIdentifier("param1"));

    JSType returnType = registry.createPrimitiveType("Number");

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, returnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, returnType);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @org.junit.Test
  public void testArrowTypeWithMissingParameters() {
    Node thisParameters = Node.newList();
    thisParameters.addChild(Node.newIdentifier("param1"));

    Node thatParameters = Node.newList();

    JSType returnType = registry.createPrimitiveType("Number");

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, returnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, returnType);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @org.junit.Test
  public void testArrowTypeWithOptionalParameters() {
    Node thisParameters = Node.newList();
    thisParameters.addChild(Node.newIdentifier("param1"));
    thisParameters.addChild(Node.newOptionalArgNode("param2"));

    Node thatParameters = Node.newList();
    thatParameters.addChild(Node.newIdentifier("param1"));
    thatParameters.addChild(Node.newOptionalArgNode("param3"));

    JSType returnType = registry.createPrimitiveType("Number");

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, returnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, returnType);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @org.junit.Test
  public void testArrowTypeWithVarArgs() {
    Node thisParameters = Node.newList();
    thisParameters.addChild(Node.newIdentifier("param1"));
    thisParameters.addChild(Node.newVarArgNode());

    Node thatParameters = Node.newList();
    thatParameters.addChild(Node.newIdentifier("param1"));
    thatParameters.addChild(Node.newVarArgNode());

    JSType returnType = registry.createPrimitiveType("Number");

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, returnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, returnType);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @org.junit.Test
  public void testArrowTypeWithUnknownReturnType() {
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    assertTrue(arrowType.isSubtype(new ArrowType(registry, parameters, null)));
  }

  @org.junit.Test
  public void testArrowTypeWithEmptyParameterList() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertTrue(arrowType.isSubtype(new ArrowType(registry, null, null)));
  }
}