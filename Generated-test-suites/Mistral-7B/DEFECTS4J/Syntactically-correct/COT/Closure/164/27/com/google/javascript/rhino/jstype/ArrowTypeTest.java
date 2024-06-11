import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Visitable;
import com.google.javascript.rhino.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ArrowType arrowType;
  private ArrowType arrowTypeWithNullReturn;
  private ArrowType arrowTypeWithNullParameters;
  private ArrowType arrowTypeWithInferredReturn;
  private ArrowType arrowTypeWithInferredAndNullReturn;
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testArrowTypeWithValidParametersAndReturnType() {
    Node node = new Node("function() {return 1; }");
    arrowType = new ArrowType(registry, node.getFirstChild().getNext(), UNKNOWN_TYPE);
    JSType expected = new FunctionType(registry, new NativeType(VoidType.getInstance()),
        new NativeType(NumberType.getInstance()));
    assertEquals(expected, arrowType.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testArrowTypeWithNullReturnType() {
    Node node = new Node("function() {return null; }");
    arrowTypeWithNullReturn = new ArrowType(registry, node.getFirstChild().getNext(), null);
    JSType expected = new FunctionType(registry, new NativeType(VoidType.getInstance()),
        new NativeType(UnknownType.getInstance()));
    assertEquals(expected, arrowTypeWithNullReturn.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testArrowTypeWithNullParameters() {
    Node node = new Node("function() {}");
    arrowTypeWithNullParameters = new ArrowType(registry, null, null);
    JSType expected = new FunctionType(registry, new NativeType(VoidType.getInstance()),
        new NativeType(UnknownType.getInstance()));
    assertEquals(expected, arrowTypeWithNullParameters.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node node = new Node("function(x) {return x; }");
    arrowTypeWithInferredReturn = new ArrowType(registry, node.getFirstChild(), null, true);
    JSType expected = new FunctionType(registry, registry.createParameterTypes(node.getFirstChild()),
        new NativeType(UnknownType.getInstance()), true);
    assertEquals(expected, arrowTypeWithInferredReturn.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testArrowTypeWithInferredAndNullReturnType() {
    Node node = new Node("function() {return; }");
    arrowTypeWithInferredAndNullReturn = new ArrowType(registry, null, null, true);
    JSType expected = new FunctionType(registry, new NativeType(VoidType.getInstance()),
        new NativeType(UnknownType.getInstance()), true);
    assertEquals(expected, arrowTypeWithInferredAndNullReturn.resolveInternal(errorReporter, scope));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    arrowType.getLeastSupertype(new NativeType(StringType.getInstance()));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    arrowType.getGreatestSubtype(new NativeType(StringType.getInstance()));
  }

  @Test
  public void testIsSubtype() {
    JSType jstype = new NativeType(NumberType.getInstance());
    ArrowType arrowType1 = new ArrowType(registry, null, jstype);
    ArrowType arrowType2 = new ArrowType(registry, null, jstype);
    ArrowType arrowType3 = new ArrowType(registry, null, new NativeType(BooleanType.getInstance()));
    ArrowType arrowType4 = new ArrowType(registry, null, UNKNOWN_TYPE);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
  }

  @Test
  public void testHasEqualParameters() {
    Node node1 = new Node("function(x: number, y: number) {}", null);
    Node node2 = new Node("function(x: number) {}", null);
    ArrowType arrowType1 = new ArrowType(registry, node1, null);
    ArrowType arrowType2 = new ArrowType(registry, node2, null);
    ArrowType arrowType3 = new ArrowType(registry, node1, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType1.hasEqualParameters(arrowType3));
  }

  @Test
  public void testIsEquivalentTo() {
    Node node1 = new Node("function(x: number, y: number) {}", null);
    Node node2 = new Node("function(x: number, y: number) {}", null);
    ArrowType arrowType1 = new ArrowType(registry, node1, null);
    ArrowType arrowType2 = new ArrowType(registry, node2, null);
    ArrowType arrowType3 = new ArrowType(registry, node1, new NativeType(StringType.getInstance()));

    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    ArrowType arrowType3 = new ArrowType(registry, null, new NativeType(NumberType.getInstance()));

    assertEquals(arrowType1.hashCode(), arrowType1.hashCode());
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
  }

  @Test
  public void testToString() {
    Node node = new Node("function(x: number) { return x; }");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), null);
    assertEquals("ArrowType(NumberType, ...)", arrowType.toString());
  }

  @Test
  public void testVisit() {
    Visitor<VoidType> visitor = new Visitor<VoidType>() {
      @Override
      public VoidType visitArrowType(ArrowType arrowType) {
        return null;
      }
    };

    Node node = new Node("function(x: number) { return x; }");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), null);
    arrowType.visit(visitor);

    assertNull(visitor.getResult());
  }
}