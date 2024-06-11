import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

   private CollapseVariableDeclarations collapseVariableDeclarations;

   @Before
   public void setup() {
      collapseVariableDeclarations = new CollapseVariableDeclarations();
   }

   @Test
   public void testCollapseVariableDeclarations_Success() {
      // Typical Use Case
      String input = "var a; var b = 1; var c = 2;";
      String expectedOutput = "var a, b = 1, c = 2;";
      assertEquals(expectedOutput, collapseVariableDeclarations.collapseVariableDeclarations(input));

      // Edge Cases
      input = "var a;";
      expectedOutput = "var a;";
      assertEquals(expectedOutput, collapseVariableDeclarations.collapseVariableDeclarations(input));

      input = "var a = 1;";
      expectedOutput = "var a = 1;";
      assertEquals(expectedOutput, collapseVariableDeclarations.collapseVariableDeclarations(input));

      input = "";
      expectedOutput = "";
      assertEquals(expectedOutput, collapseVariableDeclarations.collapseVariableDeclarations(input));

      // Error Scenarios
      input = null;
      assertNull(collapseVariableDeclarations.collapseVariableDeclarations(input));

      // More Error Scenarios can be added here
   }

}