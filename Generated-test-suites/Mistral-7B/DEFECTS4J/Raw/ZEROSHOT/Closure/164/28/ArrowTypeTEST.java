import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    Node params = Node.newList();
    Node param1 = Node.newName("x");
    Node param2 = Node.newName("y");
    params.addChild(param1);
    params.addChild(param2);

    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.getIntType());
    assertEquals("(function(x, y) => number)", arrowType.toString());

    ArrowType arrowTypeWithReturnTypeInferred = new ArrowType(registry, null, null, true);
    assertEquals("(function() => number)", arrowTypeWithReturnTypeInferred.toString());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSType type1 = registry.createFunctionType(null, null, JSTypeNative.getStringType());
    JSType type2 = registry.createFunctionType(null, null, JSTypeNative.getIntType());

    ArrowType arrowType1 = new ArrowType(registry, null, type1);
    ArrowType arrowType2 = new ArrowType(registry, null, type2);

    // int is a subtype of string
    assertTrue(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, JSTypeNative.getIntType());
    ArrowType arrowType4 = new ArrowType(registry, null, JSTypeNative.getIntType());

    // same arrow type is a subtype of itself
    assertTrue(arrowType3.isSubtype(arrowType4));
    assertTrue(arrowType4.isSubtype(arrowType3));

    ArrowType arrowType5 = new ArrowType(registry, null, JSTypeNative.getIntType());
    ArrowType arrowType6 = new ArrowType(registry, null, JSTypeNative.getStringType());

    // different arrow types are not subtypes of each other
    assertFalse(arrowType5.isSubtype(arrowType6));
    assertFalse(arrowType6.isSubtype(arrowType5));

    Node params1 = Node.newList();
    Node param1 = Node.newName("x");
    Node param2 = Node.newName("y");
    params1.addChild(param1);
    params1.addChild(param2);

    ArrowType arrowTypeParam1 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType arrowTypeParam2 = new ArrowType(registry, null, JSTypeNative.getIntType());

    // arrow type with parameters is a subtype of arrow type without parameters
    // if the parameter types are assignable
    ArrowType typeWithParamsInt = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType typeWithoutParamsInt = new ArrowType(registry, null, JSTypeNative.getIntType());
    assertTrue(arrowTypeParam1.isSubtype(typeWithoutParamsInt));

    ArrowType typeWithParamsString = new ArrowType(registry, params1, JSTypeNative.getStringType());
    ArrowType typeWithoutParamsInt = new ArrowType(registry, null, JSTypeNative.getIntType());
    assertFalse(arrowTypeParam1.isSubtype(typeWithoutParamsInt));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node params1 = Node.newList();
    Node param1 = Node.newName("x");
    Node param2 = Node.newName("y");
    params1.addChild(param1);
    params1.addChild(param2);

    Node params2 = Node.newList();
    Node param3 = Node.newName("x");
    Node param4 = Node.newName("y");
    params2.addChild(param3);
    params2.addChild(param4);

    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType arrowType2 = new ArrowType(registry, params2, JSTypeNative.getIntType());
    assertFalse(arrowType1.hasEqualParameters(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType arrowType4 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    assertTrue(arrowType3.hasEqualParameters(arrowType4));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    Node params1 = Node.newList();
    Node param1 = Node.newName("x");
    Node param2 = Node.newName("y");
    params1.addChild(param1);
    params1.addChild(param2);

    Node params2 = Node.newList();
    Node param3 = Node.newName("x");
    Node param4 = Node.newName("y");
    params2.addChild(param3);
    params2.addChild(param4);

    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType arrowType2 = new ArrowType(registry, params2, JSTypeNative.getIntType());
    assertFalse(arrowType1.isEquivalentTo(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType arrowType4 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    assertTrue(arrowType3.isEquivalentTo(arrowType4));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node params1 = Node.newList();
    Node param1 = Node.newName("x");
    Node param2 = Node.newName("y");
    params1.addChild(param1);
    params1.addChild(param2);

    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.getIntType());
    ArrowType arrowType2 = new ArrowType(registry, params1, JSTypeNative.getIntType());

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);

    ArrowType arrowType3 = new ArrowType(registry, null, JSTypeNative.getIntType());
    int hashCode3 = arrowType3.hashCode();

    assertNotEquals(hashCode1, hashCode3);
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    assertFalse(arrowType1.hasUnknownParamsOrReturn());

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), null);
    assertFalse(arrowType2.hasUnknownParamsOrReturn());

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(), JSTypeNative.getUnknownType());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());

    ArrowType arrowType4 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertTrue(arrowType4.hasUnknownParamsOrReturn());
  }
}