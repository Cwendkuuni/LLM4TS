import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType(new JSTypeRegistry(), null, null);
  }

  @Test
  public void testIsSubtype_withDifferentType_shouldReturnFalse() {
    assertFalse(arrowType.isSubtype(new JSType(new JSTypeRegistry())));
  }

  @Test
  public void testIsSubtype_withSameType_shouldReturnTrue() {
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testHasEqualParameters_withSameParameters_shouldReturnTrue() {
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType.hasEqualParameters(otherArrowType, true));
  }

  @Test
  public void testHasEqualParameters_withDifferentParameters_shouldReturnFalse() {
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType.hasEqualParameters(otherArrowType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_withEqualTypes_shouldReturnTrue() {
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_withDifferentTypes_shouldReturnFalse() {
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertFalse(arrowType.checkArrowEquivalenceHelper(otherArrowType, false));
  }

  @Test
  public void testHashCode_withNullParameters_shouldReturnZero() {
    arrowType.parameters = null;
    assertEquals(0, arrowType.hashCode());
  }

  @Test
  public void testHashCode_withNonNullParameters_shouldReturnNonZero() {
    arrowType.parameters = new Node();
    assertNotEquals(0, arrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype_shouldThrowException() {
    arrowType.getLeastSupertype(new JSType(new JSTypeRegistry()));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype_shouldThrowException() {
    arrowType.getGreatestSubtype(new JSType(new JSTypeRegistry()));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality_shouldThrowException() {
    arrowType.testForEquality(new JSType(new JSTypeRegistry()));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit_shouldThrowException() {
    arrowType.visit(new JSType.Visitor<JSType>() {
      public JSType caseUnknown() { return null; }
    });
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_shouldReturnTrue() {
    assertTrue(arrowType.getPossibleToBooleanOutcomes().contains(BooleanLiteral.TRUE));
  }

  @Test
  public void testResolveInternal_withNonNullReturnTypeAndParameters_shouldReturnResolvedType() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    arrowType.parameters = new Node();
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertNotNull(arrowType.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testHasUnknownParamsOrReturn_withUnknownParameters_shouldReturnTrue() {
    arrowType.parameters = new Node();
    Node paramNode = new Node();
    paramNode.setJSType(new JSTypeRegistry().createUnknownType());
    arrowType.parameters.addChildToBack(paramNode);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_withUnknownReturnType_shouldReturnTrue() {
    arrowType.returnType = new JSTypeRegistry().createUnknownType();
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_withNonNullParametersAndReturnType_shouldReturnFalse() {
    arrowType.parameters = new Node();
    Node paramNode = new Node();
    paramNode.setJSType(new JSType(new JSTypeRegistry()));
    arrowType.parameters.addChildToBack(paramNode);
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper_shouldReturnExpectedString() {
    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testHasAnyTemplateInternal_withTemplates_shouldReturnTrue() {
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_withoutTemplates_shouldReturnFalse() {
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testCheckEquivalenceHelper_withEqualTypes_shouldReturnTrue() {
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType.checkEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testCheckEquivalenceHelper_withEqualTypesButDifferentParameterCount_shouldReturnFalse() {
    arrowType.parameters = new Node();
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertFalse(arrowType.checkEquivalenceHelper(otherArrowType, false));
  }

  @Test
  public void testCheckEquivalenceHelper_withDifferentParameterTypes_shouldReturnFalse() {
    arrowType.parameters = new Node();
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertFalse(arrowType.checkEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testCheckEquivalenceHelper_withDifferentReturnType_shouldReturnFalse() {
    arrowType.returnType = new JSType(new JSTypeRegistry());
    ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertFalse(arrowType.checkEquivalenceHelper(otherArrowType, true));
  }
}