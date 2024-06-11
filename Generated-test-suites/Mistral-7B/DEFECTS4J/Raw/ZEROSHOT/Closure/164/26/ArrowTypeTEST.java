import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createIdent;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createNativeNode;
import static com.google.javascript.rhino.NodeFactory.createNull;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createTypeName;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private JSTypeFactory factory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    factory = new JSTypeFactory(registry);
  }

  @Test
  public void testArrowTypeCreation() {
    // ArrowType with no parameters and no inferred return type
    JSType arrowType1 = createArrowType(registry, null, null);
    assertEquals(UNKNOWN_TYPE, arrowType1.getTypeID());
    assertNull(arrowType1.getParameters());
    assertNull(arrowType1.getReturnType());
    assertFalse(arrowType1.isReturnTypeInferred());

    // ArrowType with no parameters and an inferred return type
    JSType arrowType2 = createArrowType(registry, null, getVoidType());
    assertEquals(UNKNOWN_TYPE, arrowType2.getTypeID());
    assertNull(arrowType2.getParameters());
    assertTrue(arrowType2.isReturnTypeInferred());

    // ArrowType with two required parameters and a specific return type
    JSType arrowType3 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    assertEquals("function(int, int)", arrowType3.toString());
    assertEquals(INT_TYPE, arrowType3.getParameters().getJSType(0).getTypeID());
    assertEquals(INT_TYPE, arrowType3.getParameters().getJSType(1).getTypeID());
    assertEquals(NUMBER_TYPE, arrowType3.getReturnType().getTypeID());
    assertFalse(arrowType3.isReturnTypeInferred());

    // ArrowType with two required parameters and an inferred return type
    JSType arrowType4 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), NULL_TYPE);
    assertEquals("function(int, int)", arrowType4.toString());
    assertEquals(INT_TYPE, arrowType4.getParameters().getJSType(0).getTypeID());
    assertEquals(INT_TYPE, arrowType4.getParameters().getJSType(1).getTypeID());
    assertTrue(arrowType4.isReturnTypeInferred());

    // ArrowType with one required parameter and a variable number of parameters
    JSType arrowType5 = createArrowType(registry, createList(createTypeName("int")), NULL_TYPE);
    assertEquals("function(int, ...)", arrowType5.toString());
    assertEquals(INT_TYPE, arrowType5.getParameters().getJSType(0).getTypeID());
    assertTrue(arrowType5.getParameters().isVarArgs());
    assertNull(arrowType5.getReturnType());
    assertTrue(arrowType5.isReturnTypeInferred());

    // ArrowType with two required parameters and a void return type
    JSType arrowType6 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getVoidType());
    assertEquals("function(int, int)", arrowType6.toString());
    assertEquals(INT_TYPE, arrowType6.getParameters().getJSType(0).getTypeID());
    assertEquals(INT_TYPE, arrowType6.getParameters().getJSType(1).getTypeID());
    assertEquals(VOID_TYPE, arrowType6.getReturnType().getTypeID());
    assertFalse(arrowType6.isReturnTypeInferred());

    // ArrowType with one required parameter and a specific return type
    JSType arrowType7 = createArrowType(registry, createList(createTypeName("int")), getNumberType());
    assertEquals("function(int)", arrowType7.toString());
    assertEquals(INT_TYPE, arrowType7.getParameters().getJSType(0).getTypeID());
    assertEquals(NUMBER_TYPE, arrowType7.getReturnType().getTypeID());
    assertFalse(arrowType7.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSType arrowType1 = createArrowType(registry, null, getNumberType());
    JSType arrowType2 = createArrowType(registry, null, getBooleanType());
    JSType arrowType3 = createArrowType(registry, null, getVoidType());

    // ArrowType with a specific return type is a subtype of ArrowType with an inferred return type
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType1.isSubtype(arrowType3));

    // ArrowType with inferred return type is a subtype of ArrowType with a specific return type
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType3.isSubtype(arrowType1));

    // ArrowType with a specific return type is not a subtype of ArrowType with a different return type
    assertFalse(arrowType1.isSubtype(createArrowType(registry, null, getStringType())));

    // ArrowType with a specific return type and required parameters is not a subtype of ArrowType with variable arguments
    JSType arrowType4 = createArrowType(registry, null, getNumberType());
    JSType arrowType5 = createArrowType(registry, null, getNumberType());
    arrowType5 = (ArrowType) arrowType5.setVarArgs(true);
    assertFalse(arrowType4.isSubtype(arrowType5));

    // ArrowType with variable arguments is a subtype of ArrowType with a fixed number of parameters
    JSType arrowType6 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType7 = createArrowType(registry, null, getNumberType());
    arrowType7 = (ArrowType) arrowType7.setVarArgs(true);
    assertTrue(arrowType6.isSubtype(arrowType7));

    // ArrowType with required parameters is not a subtype of ArrowType with different required parameters
    JSType arrowType8 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType9 = createArrowType(registry, createList(createTypeName("int"), createTypeName("boolean")), getNumberType());
    assertFalse(arrowType8.isSubtype(arrowType9));

    // ArrowType with required parameters is not a subtype of ArrowType with missing required parameters
    JSType arrowType10 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType11 = createArrowType(registry, createList(createTypeName("int"), createNull()), getNumberType());
    assertFalse(arrowType10.isSubtype(arrowType11));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSType arrowType1 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType2 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType3 = createArrowType(registry, createList(createTypeName("int"), createTypeName("boolean")), getNumberType());
    JSType arrowType4 = createArrowType(registry, createList(createTypeName("int"), createNull()), getNumberType());

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    JSType arrowType1 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType2 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType3 = createArrowType(registry, createList(createTypeName("int"), createTypeName("boolean")), getNumberType());
    JSType arrowType4 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getBooleanType());

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
    assertFalse(arrowType1.isEquivalentTo(arrowType4));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSType arrowType1 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType2 = createArrowType(registry, createList(createTypeName("int"), createTypeName("int")), getNumberType());
    JSType arrowType3 = createArrowType(registry, createList(createTypeName("int"), createTypeName("boolean")), getNumberType());

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();
    assertEquals(hashCode1, hashCode2);
    assertNotSame(hashCode1, arrowType2.hashCode());
    assertNotSame(hashCode1, arrowType3.hashCode());
  }

  private ArrowType createArrowType(JSTypeRegistry registry, Node node, JSType returnType) {
    Scope scope = new StaticScope<>();
    ArrowType arrowType = (ArrowType) factory.createTypeFromNode(registry, node, errorReporter);
    arrowType.resolveInternal(errorReporter, scope);
    arrowType.setReturnType(returnType);
    return arrowType;
  }

  private ArrowType createArrowType(JSTypeRegistry registry, Node node, JSType returnType, boolean returnTypeInferred) {
    Scope scope = new StaticScope<>();
    ArrowType arrowType = (ArrowType) factory.createTypeFromNode(registry, node, errorReporter);
    arrowType.resolveInternal(errorReporter, scope);
    arrowType.setReturnType(returnType);
    arrowType.setReturnTypeInferred(returnTypeInferred);
    return arrowType;
  }
}