
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assert(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assert(arrowType.hashCode() == 0);
  }

  @Test
  public void testGetLeastSupertype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assert(arrowType1.getLeastSupertype(arrowType2) == null);
  }

  @Test
  public void testGetGreatestSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assert(arrowType1.getGreatestSubtype(arrowType2) == null);
  }

  @Test
  public void testTestForEquality() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assert(arrowType1.testForEquality(arrowType2) == null);
  }

  @Test
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.visit(new Visitor<>());
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assert(arrowType.getPossibleToBooleanOutcomes() == BooleanLiteralSet.TRUE);
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.resolveInternal(null, null);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assert(!arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assert(arrowType.toStringHelper(false) == "[ArrowType]");
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assert(!arrowType.hasAnyTemplateInternal());
  }

}
