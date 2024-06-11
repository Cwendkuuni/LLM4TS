import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    checkSideEffects = new CheckSideEffects();
  }

  @Test
  public void testEmptyString() {
    String str = "";
    assertFalse(checkSideEffects.hasSideEffect(str));
  }

  @Test
  public void testNonEmptyString() {
    String str = "Hello World!";
    assertFalse(checkSideEffects.hasSideEffect(str));
  }

  @Test
  public void testSimpleOperation() {
    int a = 10;
    int b = 20;
    int result = checkSideEffects.addNumbers(a, b);
    assertEquals(30, result);
  }

  @Test
  public void testComplexOperation() {
    int a = 10;
    int b = 20;
    int c = 30;
    int result = checkSideEffects.calculateResult(a, b, c);
    assertEquals(6000, result);
  }

  @Test
  public void testSideEffectFreeCode() {
    int a = 10;
    int b = 20;
    int result = checkSideEffects.calculateResult(a, b, 30);
    assertEquals(a + b, result);
  }

  @Test
  public void testSideEffectCode() {
    int a = 10;
    int b = 20;
    int result = checkSideEffects.calculateResult(a++, b, 30);
    assertEquals(a + b + 6000, result);
  }

  @Test
  public void testProtectedSideEffectFreeCode() {
    int a = 10;
    int b = 20;
    int result = checkSideEffects.protectSideEffects(a, b);
    assertEquals(a + b, result);
  }

  @Test
  public void testProtectedSideEffectCode() {
    int a = 10;
    int b = 20;
    int result = checkSideEffects.protectSideEffects(a++, b, 30);
    assertEquals(a + b + 6000, result);
  }

}