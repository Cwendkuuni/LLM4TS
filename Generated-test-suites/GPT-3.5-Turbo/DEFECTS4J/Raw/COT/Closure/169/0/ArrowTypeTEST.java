import static org.junit.Assert.*;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertFalse(arrowType.isSubtype(new ArrowType(null, null, null)));
  }

  @Test
  public void testHasEqualParameters() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertFalse(arrowType.hasEqualParameters(new ArrowType(null, null, null), false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertFalse(arrowType.checkArrowEquivalenceHelper(new ArrowType(null, null, null), false));
  }

  @Test
  public void testHashCode() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertEquals(0, arrowType.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
     ArrowType arrowType = new ArrowType(null, null, null);
     try {
         arrowType.getLeastSupertype(null);
     } catch(UnsupportedOperationException e) {
          assertEquals("Unsupported operation", e.getMessage());
     }
  }

  @Test
  public void testGetGreatestSubtype() {
     ArrowType arrowType = new ArrowType(null, null, null);
     try {
         arrowType.getGreatestSubtype(null);
     } catch(UnsupportedOperationException e) {
          assertEquals("Unsupported operation", e.getMessage());
     }
  }

  @Test
  public void testTestForEquality() {
     ArrowType arrowType = new ArrowType(null, null, null);
     try {
         arrowType.testForEquality(null);
     } catch(UnsupportedOperationException e) {
          assertEquals("Unsupported operation", e.getMessage());
     }
  }

  @Test
  public void testVisit() {
     ArrowType arrowType = new ArrowType(null, null, null);
     try {
         arrowType.visit(null);
     } catch(UnsupportedOperationException e) {
          assertEquals("Unsupported operation", e.getMessage());
     }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
     ArrowType arrowType = new ArrowType(null, null, null);
     try {
         arrowType.resolveInternal(null, null);
     } catch(UnsupportedOperationException e) {
          assertEquals("Unsupported operation", e.getMessage());
     }
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testHasAnyTemplateInternal() {
     ArrowType arrowType = new ArrowType(null, null, null);
     assertFalse(arrowType.hasAnyTemplateInternal());
  }

}