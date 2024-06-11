import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    assertEquals(true, arrowType1.isSubtype(arrowType2));
    assertEquals(false, arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    assertEquals(true, arrowType1.hasEqualParameters(arrowType2, true));
    assertEquals(false, arrowType2.hasEqualParameters(arrowType1, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    assertEquals(true, arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    assertEquals(false, arrowType2.checkArrowEquivalenceHelper(arrowType1, false));
  }

  // Add more test methods to comprehensively test all methods in ArrowType

}