import static com.google.javascript.rhino.testing.TypeEquals.jstypeEqual;
import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeEquals;

import org.junit.Test;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter t;

  @SuppressWarnings("unused")
  static void setUp() {
    registry = new JSTypeRegistry();
    t = ErrorReporter.NULL;
  }

  @Test
  public void testArrowTypeConstruction() {
    Node parameters = Node.newList();
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newIdent("y");
    Node paramList = Node.newList(param1, param2);
    parameters.setJSType(registry.createParameters(paramList));

    ArrowType arrowType = new ArrowType(registry, parameters,
        JSTypeFactory.createStringType(), false);

    assertEquals("ArrowType[Function(String, String)]", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructionWithVarArgs() {
    Node parameters = Node.newList();
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newList(Node.newIdent("a"), Node.newIdent("b"));
    Node paramList = Node.newList(param1, param2);
    parameters.setJSType(registry.createParametersWithVarArgs(paramList));

    ArrowType arrowType = new ArrowType(registry, parameters,
        JSTypeFactory.createNumberType(), false);

    assertEquals("ArrowType[Function(*, Number)]", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructionWithOptionalParams() {
    Node parameters = Node.newList();
    Node param1 = Node.newIdent("x");
    Node optParam = Node.newOptionalArg(Node.newIdent("y"));
    Node paramList = Node.newList(param1, optParam);
    parameters.setJSType(registry.createParameters(paramList));

    ArrowType arrowType = new ArrowType(registry, parameters,
        JSTypeFactory.createNumberType(), false);

    assertEquals("ArrowType[Function(Number, ?Number)]", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructionWithEmptyParamList() {
    Node emptyParamList = Node.newList();
    ArrowType arrowType = new ArrowType(registry, emptyParamList,
        JSTypeFactory.createStringType(), false);

    assertEquals("ArrowType[Function()]", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructionWithNullParams() {
    Node nullParamList = Node.newNull();
    ArrowType arrowType = new ArrowType(registry, nullParamList,
        JSTypeFactory.createStringType(), false);

    assertEquals("ArrowType[Function()]", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructionWithNoReturnType() {
    Node parameters = Node.newList();
    Node param1 = Node.newIdent("x");
    Node paramList = Node.newList(param1);
    parameters.setJSType(registry.createParameters(paramList));

    ArrowType arrowType = new ArrowType(registry, parameters, null, false);

    assertEquals("ArrowType[Function]", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructionWithInferredReturnType() {
    Node parameters = Node.newList();
    Node param1 = Node.newIdent("x");
    Node paramList = Node.newList(param1);
    parameters.setJSType(registry.createParametersWithVarArgs(paramList));

    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals("ArrowType[Function(*, ?)]", arrowType.toString());
  }

  @Test
  public void testArrowTypeSubtyping() {
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newIdent("y");
    Node paramList = Node.newList(param1, param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList,
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowType2 = new ArrowType(registry, paramList,
        JSTypeFactory.createStringType(), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeSubtypingWithDifferentReturnTypes() {
    Node param1 = Node.newIdent("x");
    Node paramList = Node.newList(param1);

    ArrowType arrowType1 = new ArrowType(registry, paramList,
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowType2 = new ArrowType(registry, paramList,
        JSTypeFactory.createStringType(), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeSubtypingWithDifferentParameterTypes() {
    Node param1Int = Node.newIdent("x");
    Node param1String = Node.newIdent("x");
    param1String.setJSType(JSTypeFactory.createStringType());

    Node param2Int = Node.newIdent("y");
    Node param2String = Node.newIdent("y");
    param2String.setJSType(JSTypeFactory.createStringType());

    Node paramListInt = Node.newList(param1Int, param2Int);
    Node paramListString = Node.newList(param1String, param2String);

    ArrowType arrowTypeInt = new ArrowType(registry, paramListInt,
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowTypeString = new ArrowType(registry, paramListString,
        JSTypeFactory.createStringType(), false);

    assertFalse(arrowTypeInt.isSubtype(arrowTypeString));
  }

  @Test
  public void testArrowTypeSubtypingWithVarArgs() {
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newList(Node.newIdent("a"), Node.newIdent("b"));

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(param1),
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(param1),
        JSTypeFactory.createArrayType(JSTypeFactory.createNumberType()), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeSubtypingWithOptionalParams() {
    Node param1 = Node.newIdent("x");
    Node optParam = Node.newOptionalArg(Node.newIdent("y"));

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(param1),
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(param1, optParam),
        JSTypeFactory.createNumberType(), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newIdent("y");
    Node paramList = Node.newList(param1, param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList, null, false);

    ArrowType arrowType2 = new ArrowType(registry, paramList, null, true);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeEquals() {
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newIdent("y");
    Node paramList = Node.newList(param1, param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList,
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowType2 = new ArrowType(registry, paramList,
        JSTypeFactory.createNumberType(), false);

    assertTrue(jstypeEqual(arrowType1, arrowType2));
  }

  @Test
  public void testArrowTypeUnequalWithDifferentReturnTypes() {
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newIdent("y");
    Node paramList = Node.newList(param1, param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList,
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowType2 = new ArrowType(registry, paramList,
        JSTypeFactory.createStringType(), false);

    assertFalse(jstypeEqual(arrowType1, arrowType2));
  }

  @Test
  public void testArrowTypeUnequalWithDifferentParameterTypes() {
    Node param1Int = Node.newIdent("x");
    Node param1String = Node.newIdent("x");
    param1String.setJSType(JSTypeFactory.createStringType());

    Node param2Int = Node.newIdent("y");
    Node param2String = Node.newIdent("y");
    param2String.setJSType(JSTypeFactory.createStringType());

    Node paramListInt = Node.newList(param1Int, param2Int);
    Node paramListString = Node.newList(param1String, param2String);

    ArrowType arrowTypeInt = new ArrowType(registry, paramListInt,
        JSTypeFactory.createNumberType(), false);

    ArrowType arrowTypeString = new ArrowType(registry, paramListString,
        JSTypeFactory.createStringType(), false);

    assertFalse(jstypeEqual(arrowTypeInt, arrowTypeString));
  }

  @Test
  public void testArrowTypeUnequalWithDifferentParameterLists() {
    Node param1 = Node.newIdent("x");
    Node param1List = Node.newList(param1);

    ArrowType arrowType1 = new ArrowType(registry, param1List,
        JSTypeFactory.createNumberType(), false);

    Node param2 = Node.newIdent("y");
    Node param2List = Node.newList(param2);

    ArrowType arrowType2 = new ArrowType(registry, param2List,
        JSTypeFactory.createNumberType(), false);

    assertFalse(jstypeEqual(arrowType1, arrowType2));
  }

  @Test
  public void testArrowTypeWithInferredReturnTypeEqualsWithExplicitReturnType() {
    Node param1 = Node.newIdent("x");
    Node param2 = Node.newIdent("y");
    Node paramList = Node.newList(param1, param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList, null, true);

    ArrowType arrowType2 = new ArrowType(registry, paramList,
        JSTypeFactory.createNumberType(), false);

    assertTrue(jstypeEqual(arrowType1, arrowType2));
  }
}