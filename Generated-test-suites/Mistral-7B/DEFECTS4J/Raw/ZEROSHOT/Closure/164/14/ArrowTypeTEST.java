import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.Property.*;
import static com.google.javascript.rhino.jstype.JSType.ANY;
import static com.google.javascript.rhino.jstype.JSType.ARRAY;
import static com.google.javascript.rhino.jstype.JSType.BOOLEAN;
import static com.google.javascript.rhino.jstype.JSType.NUMBER;
import static com.google.javascript.rhino.jstype.JSType.STRING;
import static com.google.javascript.rhino.jstype.JSType.VOID;

import org.junit.Test;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.node.Node;
import com.google.javascript.rhino.testing.TestBase;

public class ArrowTypeTest extends TestBase {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeCreation() {
    Node node = newNode("function() {}");
    ArrowType arrowType = new ArrowType(registry, node.params, VOID);
    assertNotNull(arrowType);
    assertEquals(ARROW_TYPE, arrowType.getTypeClass());

    Node nodeWithParams = newNode("function(x, y) {}", node.params);
    ArrowType arrowTypeWithParams = new ArrowType(registry, nodeWithParams, NUMBER, false);
    assertNotNull(arrowTypeWithParams);
    assertEquals(ARROW_TYPE, arrowTypeWithParams.getTypeClass());

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, node, NUMBER, true);
    assertNotNull(arrowTypeWithInferredReturn);
    assertEquals(ARROW_TYPE, arrowTypeWithInferredReturn.getTypeClass());
    assertTrue(arrowTypeWithInferredReturn.returnTypeInferred);

    Node nodeWithVarArgs = newNode("function(x, ...y) {}", node.params);
    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, nodeWithVarArgs, ARRAY(NUMBER), false);
    assertNotNull(arrowTypeWithVarArgs);
    assertEquals(ARROW_TYPE, arrowTypeWithVarArgs.getTypeClass());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node node = newNode("function(x, y) {}");
    Node nodeWithOneParam = newNode("function(x) {}");

    ArrowType arrowType = new ArrowType(registry, node.params, NUMBER, false);
    ArrowType arrowTypeWithOneParam = new ArrowType(registry, nodeWithOneParam.params, NUMBER, false);

    assertTrue(arrowType.isSubtype(arrowTypeWithOneParam));

    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, node.params, ARRAY(NUMBER), false);
    assertTrue(arrowType.isSubtype(arrowTypeWithVarArgs));

    ArrowType arrowTypeWithDifferentReturn = new ArrowType(registry, node.params, BOOLEAN, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, true);
    assertTrue(arrowType.isSubtype(arrowTypeWithInferredReturn));

    ArrowType arrowTypeWithMutlipleParameters = new ArrowType(registry,
        newNode("function(x, y, z) {}", newNode("x", NUMBER), newNode("y", NUMBER),
            newNode("z", NUMBER)).params, NUMBER, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithMutlipleParameters));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node node = newNode("function(x, y) {}");
    Node nodeWithOneParam = newNode("function(x) {}");
    ArrowType arrowType = new ArrowType(registry, node.params, NUMBER, false);
    ArrowType arrowTypeWithOneParam = new ArrowType(registry, nodeWithOneParam.params, NUMBER, false);

    assertTrue(arrowType.hasEqualParameters(arrowTypeWithOneParam));

    Node nodeWithVarArgs = newNode("function(x, ...y) {}", node.params);
    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, nodeWithVarArgs, ARRAY(NUMBER), false);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithVarArgs));

    ArrowType arrowTypeWithDifferentReturn = new ArrowType(registry, node.params, BOOLEAN, false);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, false);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, true);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithMutlipleParameters = new ArrowType(registry,
        newNode("function(x, y, z) {}", newNode("x", NUMBER), newNode("y", NUMBER),
            newNode("z", NUMBER)).params, NUMBER, false);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithMutlipleParameters));
  }

  @Test
  public void testArrowTypeIsEquivalent() {
    Node node = newNode("function(x, y) {}");
    Node nodeWithOneParam = newNode("function(x) {}");

    ArrowType arrowType = new ArrowType(registry, node.params, NUMBER, false);
    ArrowType arrowTypeWithOneParam = new ArrowType(registry, nodeWithOneParam.params, NUMBER, false);

    assertTrue(arrowType.isEquivalentTo(arrowTypeWithOneParam));

    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, node.params, ARRAY(NUMBER), false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithVarArgs));

    ArrowType arrowTypeWithDifferentReturn = new ArrowType(registry, node.params, BOOLEAN, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, true);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturn));

    ArrowType arrowTypeWithMutlipleParameters = new ArrowType(registry,
        newNode("function(x, y, z) {}", newNode("x", NUMBER), newNode("y", NUMBER),
            newNode("z", NUMBER)).params, NUMBER, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithMutlipleParameters));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node node = newNode("function(x, y) {}");
    Node nodeWithOneParam = newNode("function(x) {}");

    ArrowType arrowType = new ArrowType(registry, node.params, NUMBER, false);
    ArrowType arrowTypeWithOneParam = new ArrowType(registry, nodeWithOneParam.params, NUMBER, false);

    int hashCodeArrowType = arrowType.hashCode();
    int hashCodeArrowTypeWithOneParam = arrowTypeWithOneParam.hashCode();

    assertEquals(hashCodeArrowType, hashCodeArrowTypeWithOneParam);

    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, node.params, ARRAY(NUMBER), false);
    assertNotEquals(hashCodeArrowType, arrowTypeWithVarArgs.hashCode());

    ArrowType arrowTypeWithDifferentReturn = new ArrowType(registry, node.params, BOOLEAN, false);
    assertNotEquals(hashCodeArrowType, arrowTypeWithDifferentReturn.hashCode());

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, false);
    assertNotEquals(hashCodeArrowType, arrowTypeWithUnknownReturn.hashCode());

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, node.params, UNKNOWN_TYPE, true);
    assertNotEquals(hashCodeArrowType, arrowTypeWithInferredReturn.hashCode());

    ArrowType arrowTypeWithMutlipleParameters = new ArrowType(registry,
        newNode("function(x, y, z) {}", newNode("x", NUMBER), newNode("y", NUMBER),
            newNode("z", NUMBER)).params, NUMBER, false);
    assertNotEquals(hashCodeArrowType, arrowTypeWithMutlipleParameters.hashCode());
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    Node nodeWithUnknownParam = newNode("function(x, ...y) {}", node.params);
    ArrowType arrowTypeWithUnknownParam = new ArrowType(registry, nodeWithUnknownParam, ARRAY(NUMBER), false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
    assertTrue(arrowTypeWithUnknownParam.hasUnknownParamsOrReturn());

    Node nodeWithUnknownReturn = newNode("function() {}", node.params);
    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, nodeWithUnknownReturn.params, UNKNOWN_TYPE, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
    assertTrue(arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn());

    Node nodeWithBoth = newNode("function(x, ...y) {return;}", node.params);
    ArrowType arrowTypeWithBoth = new ArrowType(registry, nodeWithBoth.params, UNKNOWN_TYPE, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
    assertTrue(arrowTypeWithBoth.hasUnknownParamsOrReturn());

    Node nodeWithNoUnknown = newNode("function(x, y) {}", node.params);
    ArrowType arrowTypeWithNoUnknown = new ArrowType(registry, nodeWithNoUnknown.params, NUMBER, false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertFalse(arrowTypeWithNoUnknown.hasUnknownParamsOrReturn());
  }
}