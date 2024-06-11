import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    // Test when arrowType2 is a subtype of arrowType1
    boolean result1 = arrowType1.isSubtype(arrowType2);
    Assert.assertTrue(result1);

    // Test when arrowType1 is not a subtype of arrowType2
    boolean result2 = arrowType2.isSubtype(arrowType1);
    Assert.assertFalse(result2);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    // Test when the parameters of arrowType1 and arrowType2 are equal
    boolean result1 = arrowType1.hasEqualParameters(arrowType2, true);
    Assert.assertTrue(result1);

    // Test when the parameters of arrowType1 and arrowType2 are not equal
    boolean result2 = arrowType1.hasEqualParameters(arrowType2, false);
    Assert.assertFalse(result2);
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    // Test when arrowType1 and arrowType2 are equivalent
    boolean result1 = arrowType1.checkArrowEquivalenceHelper(arrowType2, true);
    Assert.assertTrue(result1);

    // Test when arrowType1 and arrowType2 are not equivalent
    boolean result2 = arrowType1.checkArrowEquivalenceHelper(arrowType2, false);
    Assert.assertFalse(result2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    arrowType.getGreatestSubtype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    arrowType.testForEquality(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    arrowType.visit(null);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> scope = mock(StaticScope.class);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test when returnType and parameters are resolved
    JSType resolvedType = mock(JSType.class);
    when(returnType.resolve(errorReporter, scope)).thenReturn(resolvedType);
    when(parameters.getFirstChild().getJSType().resolve(errorReporter, scope)).thenReturn(resolvedType);
    when(scope.<JSType>safeResolve(resolvedType, errorReporter)).thenReturn(resolvedType);
    arrowType.resolveInternal(errorReporter, scope);
    
    // verify that resolve method was called for returnType and parameters
    verify(returnType, times(1)).resolve(errorReporter, scope);
    verify(parameters.getFirstChild(), times(1)).getJSType().resolve(errorReporter, scope);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test when returnType and parameters don't have any template
    when(returnType.hasAnyTemplate()).thenReturn(false);
    when(parameters.getFirstChild().getJSType().hasAnyTemplate()).thenReturn(false);
    boolean result1 = arrowType.hasAnyTemplateInternal();
    Assert.assertFalse(result1);

    // Test when returnType has a template but parameters don't have any template
    when(returnType.hasAnyTemplate()).thenReturn(true);
    boolean result2 = arrowType.hasAnyTemplateInternal();
    Assert.assertTrue(result2);

    // Test when returnType doesn't have a template but parameters have a template
    when(parameters.getFirstChild().getJSType().hasAnyTemplate()).thenReturn(true);
    boolean result3 = arrowType.hasAnyTemplateInternal();
    Assert.assertTrue(result3);

    // Test when both returnType and parameters have a template
    when(returnType.hasAnyTemplate()).thenReturn(true);
    boolean result4 = arrowType.hasAnyTemplateInternal();
    Assert.assertTrue(result4);
  }

}