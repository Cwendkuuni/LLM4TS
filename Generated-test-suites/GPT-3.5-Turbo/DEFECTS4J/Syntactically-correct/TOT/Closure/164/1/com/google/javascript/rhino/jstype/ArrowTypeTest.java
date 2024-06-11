import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    
    boolean result = arrowType1.isSubtype(arrowType2);
    
    assertEquals(true, result);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    
    boolean result = arrowType1.hasEqualParameters(arrowType2);
    
    assertEquals(true, result);
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    ArrowType arrowType2 = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    
    BooleanLiteralSet outcomes = arrowType.getPossibleToBooleanOutcomes();
    
    assertEquals(BooleanLiteralSet.TRUE, outcomes);
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    
    JSType resolvedType = arrowType.resolveInternal(t, scope);
    
    assertEquals(arrowType, resolvedType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(
        new JSTypeRegistry(),
        new Node(Token.PARAMETERS),
        new JSType(new JSTypeRegistry()));
    
    boolean result = arrowType.hasUnknownParamsOrReturn();
    
    assertEquals(false, result);
  }

}