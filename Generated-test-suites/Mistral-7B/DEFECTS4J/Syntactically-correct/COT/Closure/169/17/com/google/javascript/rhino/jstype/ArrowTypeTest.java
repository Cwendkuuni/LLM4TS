import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  @SuppressWarnings("unchecked")
  private static <T> ArrowType<T> createArrowType(Node parameters, JSType<T> returnType) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return (ArrowType<T>) new ArrowType<>(registry, parameters, returnType, false);
  }

  @SuppressWarnings("unchecked")
  private static <T> ArrowType<T> createArrowType(Node parameters, JSTYPE returnTypeInferred) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return (ArrowType<T>) new ArrowType<>(registry, parameters, null, returnTypeInferred);
  }

  @SuppressWarnings("unchecked")
  private static <T> ArrowType<T> createArrowType(Node parameters) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return (ArrowType<T>) new ArrowType<>(registry, parameters, null, false);
  }

  @Test
  public void testArrowType_withParametersAndReturnType_shouldCreateInstance() {
    Node parameters = new Node(new JSListType(new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType = new IntType();

    ArrowType<Integer> arrowType = createArrowType(parameters, returnType);

    assertNotNull(arrowType);
    assertEquals(INT_TYPE, arrowType.returnType);
    assertEquals(parameters, arrowType.parameters);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowType_withParametersOnly_shouldCreateInstance() {
    Node parameters = new Node(new JSListType(new JSFunctionType(ANY, ANY, ANY), true));

    ArrowType<Void> arrowType = createArrowType(parameters);

    assertNotNull(arrowType);
    assertNull(arrowType.returnType);
    assertEquals(parameters, arrowType.parameters);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowType_withOnlyReturnType_shouldCreateInstance() {
    ArrowType<Integer> arrowType = createArrowType(null, new IntType(), false);

    assertNotNull(arrowType);
    assertEquals(INT_TYPE, arrowType.returnType);
    assertNull(arrowType.parameters);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowType_withOnlyReturnTypeAndInferred_shouldCreateInstance() {
    ArrowType<Integer> arrowType = createArrowType(null, new IntType(), true);

    assertNotNull(arrowType);
    assertEquals(INT_TYPE, arrowType.returnType);
    assertNull(arrowType.parameters);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_shouldReturnTrueForSameTypes() {
    Node parameters1 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType1 = new IntType();

    ArrowType<Integer> arrowType1 = createArrowType(parameters1, returnType1);

    Node parameters2 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType2 = new IntType();

    ArrowType<Integer> arrowType2 = createArrowType(parameters2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_shouldReturnFalseForDifferentReturnTypes() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType1 = new IntType();
    JSType returnType2 = new StringType();

    ArrowType<Integer> arrowType1 = createArrowType(parameters, returnType1);
    ArrowType<String> arrowType2 = createArrowType(parameters, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_shouldReturnFalseForDifferentParameters() {
    Node parameters1 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    Node parameters2 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true).append(new IdentifierNode("x")));

    JSType returnType = new IntType();

    ArrowType<Integer> arrowType1 = createArrowType(parameters1, returnType);
    ArrowType<Integer> arrowType2 = createArrowType(parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype_shouldThrowException() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType = new IntType();

    ArrowType<Integer> arrowType = createArrowType(parameters, returnType);

    arrowType.getLeastSupertype(ANY_TYPE);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype_shouldThrowException() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType = new IntType();

    ArrowType<Integer> arrowType = createArrowType(parameters, returnType);

    arrowType.getGreatestSubtype(ANY_TYPE);
  }

  @Test
  public void testHasEqualParameters_shouldReturnTrueForEqualParameterLists() {
    Node thisParameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    Node thatParameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));

    ArrowType<Integer> thisArrowType = createArrowType(thisParameters, new IntType());
    ArrowType<Integer> thatArrowType = createArrowType(thatParameters, new IntType());

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, true));
    assertTrue(thatArrowType.hasEqualParameters(thisArrowType, true));
  }

  @Test
  public void testHasEqualParameters_shouldReturnFalseForUnequalParameterLists() {
    Node thisParameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    Node thatParameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true).append(new IdentifierNode("x")));

    ArrowType<Integer> thisArrowType = createArrowType(thisParameters, new IntType());
    ArrowType<Integer> thatArrowType = createArrowType(thatParameters, new IntType());

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, true));
    assertFalse(thatArrowType.hasEqualParameters(thisArrowType, true));
  }

  @Test
  public void testHasEqualParameters_shouldReturnTrueForEqualParameterListsWithUnknownTypes() {
    Node thisParameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    Node thatParameters = new Node(new JSListType(
        new JSFunctionType(UNKNOWN_TYPE, ANY, ANY), true));

    ArrowType<Integer> thisArrowType = createArrowType(thisParameters, new IntType());
    ArrowType<Integer> thatArrowType = createArrowType(thatParameters, new IntType());

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, true));
    assertTrue(thatArrowType.hasEqualParameters(thisArrowType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_shouldReturnTrueForEqualTypes() {
    Node parameters1 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType1 = new IntType();

    ArrowType<Integer> arrowType1 = createArrowType(parameters1, returnType1);

    Node parameters2 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType2 = new IntType();

    ArrowType<Integer> arrowType2 = createArrowType(parameters2, returnType2);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    assertTrue(arrowType2.checkArrowEquivalenceHelper(arrowType1, true));
  }

  @Test
  public void testHashCode_shouldReturnEqualHashCodesForEqualTypes() {
    Node parameters1 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType1 = new IntType();

    ArrowType<Integer> arrowType1 = createArrowType(parameters1, returnType1);

    Node parameters2 = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType2 = new IntType();

    ArrowType<Integer> arrowType2 = createArrowType(parameters2, returnType2);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testToStringHelper_shouldReturnCorrectString() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType = new IntType();

    ArrowType<Integer> arrowType = createArrowType(parameters, returnType);

    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testHasAnyTemplateInternal_shouldReturnFalseForNoTemplates() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType = new IntType();

    ArrowType<Integer> arrowType = createArrowType(parameters, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_shouldReturnTrueForReturnTypeTemplate() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, ANY), true));
    JSType returnType = new JSArrayType(new IntType());

    ArrowType<JSArray<Integer>> arrowType = createArrowType(parameters, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_shouldReturnTrueForParameterTypeTemplate() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, ANY, new JSArrayType(new IntType())), true));
    JSType returnType = new IntType();

    ArrowType<JSArray<Integer>> arrowType = createArrowType(parameters, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_shouldReturnTrueForReturnAndParameterTemplates() {
    Node parameters = new Node(new JSListType(
        new JSFunctionType(ANY, new JSArrayType(new IntType()), new JSArrayType(new IntType())), true));
    JSType returnType = new JSArrayType(new IntType());

    ArrowType<JSArray<Integer[]>> arrowType = createArrowType(parameters, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }
}