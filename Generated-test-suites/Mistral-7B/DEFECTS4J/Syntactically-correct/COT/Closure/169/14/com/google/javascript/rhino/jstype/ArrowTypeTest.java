import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = JSTypeNative.getBooleanType(registry);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(registry));
    ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));

    assertFalse(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, JSTypeNative.getBooleanType(registry));
    ArrowType arrowType4 = new ArrowType(registry, null, arrowType3);
    assertTrue(arrowType3.isSubtype(arrowType4));

    ArrowType arrowType5 = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));
    ArrowType arrowType6 = new ArrowType(registry, null, JSTypeNative.getBooleanType(registry));
    assertFalse(arrowType5.isSubtype(arrowType6));

    ArrowType arrowType7 = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));
    ArrowType arrowType8 = new ArrowType(registry, null, arrowType7);
    assertTrue(arrowType7.isSubtype(arrowType8));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters1 = Node.newList(
        Node.newName("a"), Node.newName("b")
    );
    JSType type1 = new ArrowType(registry, parameters1, JSTypeNative.getNumberType(registry));

    Node parameters2 = Node.newList(
        Node.newName("a"), Node.newName("b")
    );
    ArrowType type2 = new ArrowType(registry, parameters2, JSTypeNative.getNumberType(registry));

    assertTrue(type1.hasEqualParameters(type2, false));

    Node parameters3 = Node.newList(
        Node.newName("a"), Node.newName("c")
    );
    ArrowType type3 = new ArrowType(registry, parameters3, JSTypeNative.getNumberType(registry));

    assertFalse(type1.hasEqualParameters(type3, false));

    Node parameters4 = Node.newList(
        Node.newName("a"), Node.newName("b"), Node.newName("c")
    );
    ArrowType type4 = new ArrowType(registry, parameters4, JSTypeNative.getNumberType(registry));

    assertFalse(type1.hasEqualParameters(type4, false));
  }

  @Test
  public void testArrowTypeNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));
    ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testArrowTypeErrorReporting() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message) {
        throw new AssertionError("Unexpected error: " + message);
      }
    };

    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getNumberType(registry), errorReporter);
    assertNotNull(arrowType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));
    arrowType.getLeastSupertype(JSTypeNative.getStringType(registry));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));
    arrowType.getGreatestSubtype(JSTypeNative.getStringType(registry));
  }

  @Test
  public void testArrowTypeToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getNumberType(registry));
    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }
}