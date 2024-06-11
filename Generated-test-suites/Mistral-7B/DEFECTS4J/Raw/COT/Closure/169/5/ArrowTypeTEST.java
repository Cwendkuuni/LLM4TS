import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertTrue(arrowType.parameters.equals(parametersNode));
    assertTrue(arrowType.returnType.equals(returnType));
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithEmptyParametersAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    ArrowType arrowType = new ArrowType(registry, null, null);

    assertTrue(arrowType.parameters == null);
    assertTrue(arrowType.returnType.equals(JSTypeNative.UNKNOWN_TYPE));
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithParametersButNoReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    assertTrue(arrowType.parameters.equals(parametersNode));
    assertTrue(arrowType.returnType.equals(JSTypeNative.UNKNOWN_TYPE));
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtypeWithSameArrowType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode1 = mock(Node.class);
    Node parametersNode2 = mock(Node.class);
    JSType returnType1 = mock(JSType.class);
    JSType returnType2 = mock(JSType.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType2);

    when(returnType1.equals(returnType2)).thenReturn(true);
    when(parametersNode1.equals(parametersNode2)).thenReturn(true);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithDifferentArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode1 = mock(Node.class);
    Node parametersNode2 = mock(Node.class);
    JSType returnType1 = mock(JSType.class);
    JSType returnType2 = mock(JSType.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType2);

    when(returnType1.equals(returnType2)).thenReturn(false);
    when(parametersNode1.equals(parametersNode2)).thenReturn(false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithArrowTypeAndFunctionType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);
    FunctionType functionType = new FunctionType(registry, null, null, null);

    assertFalse(arrowType.isSubtype(functionType));
  }

  @Test
  public void testIsSubtypeWithArrowTypeAndUnknownType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);
    JSType unknownType = JSTypeNative.UNKNOWN_TYPE;

    assertFalse(arrowType.isSubtype(unknownType));
  }

  @Test
  public void testHasEqualParametersWithEqualArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode1 = mock(Node.class);
    Node parametersNode2 = mock(Node.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    when(parametersNode1.getFirstChild()).thenReturn(null);
    when(parametersNode2.getFirstChild()).thenReturn(null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParametersWithUnequalArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode1 = mock(Node.class);
    Node parametersNode2 = mock(Node.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    Node paramNode1 = mock(Node.class);
    Node paramNode2 = mock(Node.class);
    JSType paramType1 = mock(JSType.class);
    JSType paramType2 = mock(JSType.class);

    when(parametersNode1.getFirstChild()).thenReturn(paramNode1);
    when(parametersNode2.getFirstChild()).thenReturn(paramNode2);
    when(paramNode1.getJSType()).thenReturn(paramType1);
    when(paramNode2.getJSType()).thenReturn(paramType2);

    when(paramType1.isUnknownType()).thenReturn(false);
    when(paramType2.isUnknownType()).thenReturn(true);

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithEqualArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode1 = mock(Node.class);
    Node parametersNode2 = mock(Node.class);
    JSType returnType1 = mock(JSType.class);
    JSType returnType2 = mock(JSType.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType2);

    when(returnType1.equals(returnType2)).thenReturn(true);
    when(parametersNode1.equals(parametersNode2)).thenReturn(true);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithUnequalArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode1 = mock(Node.class);
    Node parametersNode2 = mock(Node.class);
    JSType returnType1 = mock(JSType.class);
    JSType returnType2 = mock(JSType.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType2);

    when(returnType1.equals(returnType2)).thenReturn(false);
    when(parametersNode1.equals(parametersNode2)).thenReturn(false);

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testHashCodeWithEqualArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType);

    int expectedHashCode = 1;

    when(returnType.hashCode()).thenReturn(expectedHashCode);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCodeWithDifferentArrowTypes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType);

    JSType differentReturnType = mock(JSType.class);

    when(returnType.hashCode()).thenReturn(1);
    when(differentReturnType.hashCode()).thenReturn(2);

    ArrowType arrowType3 = new ArrowType(registry, parametersNode, differentReturnType);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType3.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetLeastSupertypeWithNull() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertNull(arrowType.getLeastSupertype(null));
  }

  @Test
  public void testGetGreatestSubtypeWithNull() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertNull(arrowType.getGreatestSubtype(null));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    arrowType.getGreatestSubtype(null);
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    String expectedResult = "[ArrowType]";

    String result = arrowType.toStringHelper(false);

    assertEquals(expectedResult, result);
  }

  @Test
  public void testHasAnyTemplateInternalWithArrowTypeWithNoTemplate() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithArrowTypeWithTemplatedReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    TemplateType templateType = mock(TemplateType.class);

    when(returnType.hasAnyTemplate()).thenReturn(true);
    when(returnType.getTemplate()).thenReturn(templateType);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithArrowTypeWithTemplatedParameters() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    TemplateType templateType = mock(TemplateType.class);
    Node templatedParamNode = mock(Node.class);
    JSType templatedParamType = mock(JSType.class);

    when(parametersNode.getFirstChild()).thenReturn(templatedParamNode);
    when(templatedParamNode.getJSType()).thenReturn(templatedParamType);
    when(templatedParamType.hasAnyTemplate()).thenReturn(true);
    when(templatedParamType.getTemplate()).thenReturn(templateType);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithArrowTypeWithBothTemplatedParamsAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    TemplateType templateType = mock(TemplateType.class);
    Node templatedParamNode1 = mock(Node.class);
    Node templatedParamNode2 = mock(Node.class);
    JSType templatedParamType1 = mock(JSType.class);
    JSType templatedParamType2 = mock(JSType.class);

    when(parametersNode.getFirstChild()).thenReturn(templatedParamNode1);
    when(templatedParamNode1.getJSType()).thenReturn(templatedParamType1);
    when(templatedParamType1.hasAnyTemplate()).thenReturn(true);
    when(templatedParamType1.getTemplate()).thenReturn(templateType);
    when(parametersNode.getNextChild()).thenReturn(templatedParamNode2);
    when(templatedParamNode2.getJSType()).thenReturn(templatedParamType2);
    when(templatedParamType2.hasAnyTemplate()).thenReturn(true);
    when(templatedParamType2.getTemplate()).thenReturn(templateType);

    when(returnType.hasAnyTemplate()).thenReturn(true);
    when(returnType.getTemplate()).thenReturn(templateType);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  private void assertEquals(int expectedHashCode, int actualHashCode) {
    assertEquals("Unexpected hash code", expectedHashCode, actualHashCode);
  }

  private void assertNotEquals(int expectedHashCode, int actualHashCode) {
    assertFalse("Unexpected hash code equality", expectedHashCode == actualHashCode);
  }
}