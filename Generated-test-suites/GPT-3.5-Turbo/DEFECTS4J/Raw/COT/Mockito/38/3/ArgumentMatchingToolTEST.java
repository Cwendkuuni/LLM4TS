import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

   ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

   // Test getSuspiciouslyNotMatchingArgsIndexes method
   @Test
   public void testGetSuspiciouslyNotMatchingArgsIndexes() {
      List<Matcher> matchers = new ArrayList<>();
      matchers.add(new ContainsExtraTypeInformation());
      Object[] arguments = new Object[1];
      arguments[0] = new Object();

      Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
      assertEquals(1, result.length);
      assertEquals(0, result[0].intValue());
   }

   // Test safelyMatches method
   @Test
   public void testSafelyMatches() {
      Matcher matcher = new ContainsExtraTypeInformation();
      Object arg = new Object();

      boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
      assertFalse(result);
   }

   // Test toStringEquals method
   @Test
   public void testToStringEquals() {
      Matcher matcher = new ContainsExtraTypeInformation();
      Object arg = new Object();

      boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
      assertFalse(result);
   }

   // Additional test cases for edge cases and exception handling scenarios
   // ...

}