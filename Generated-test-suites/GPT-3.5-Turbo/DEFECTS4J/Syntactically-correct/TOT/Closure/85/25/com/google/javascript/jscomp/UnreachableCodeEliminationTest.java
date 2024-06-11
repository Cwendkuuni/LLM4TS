import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadCode_ReturnStatement() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

    // When
    Node externs = null;
    Node root = null;
    uce.process(externs, root);

    // Then
    // Verify that dead code following a return statement is removed
    // test case 1: if (x) { return; alert('unreachable'); }
    String expected1 = 
        "// Original code\n" +
        "if (x) { return; alert('unreachable'); }\n" +
        "\n" +
        "// Transformed code\n" +
        "if (x) { return; }";
    assertEquals(expected1, root.toString());
  }

  @Test
  public void testRemoveDeadCode_NoOpStatement() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

    // When
    Node externs = null;
    Node root = null;
    uce.process(externs, root);

    // Then
    // Verify that statements with no side effects are removed
    // test case 1: a.b.MyClass.prototype.propertyName;
    String expected1 = 
        "// Original code\n" +
        "a.b.MyClass.prototype.propertyName;\n" +
        "\n" +
        "// Transformed code\n" +
        "";
    assertEquals(expected1, root.toString());

    // test case 2: true;
    String expected2 = 
        "// Original code\n" +
        "true;\n" +
        "\n" +
        "// Transformed code\n" +
        "";
    assertEquals(expected2, root.toString());
  }

  @Test
  public void testRemoveDeadCode_KeepUsefulStatements() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

    // When
    Node externs = null;
    Node root = null;
    uce.process(externs, root);

    // Then
    // Verify that useful statements are not removed
    // test case 1: if (x) { return; }
    String expected1 = 
        "// Original code\n" +
        "if (x) { return; }\n" +
        "\n" +
        "// Transformed code\n" +
        "if (x) { return; }";
    assertEquals(expected1, root.toString());

    // test case 2: if (x) { return; } else { alert('reachable'); }
    String expected2 = 
        "// Original code\n" +
        "if (x) { return; } else { alert('reachable'); }\n" +
        "\n" +
        "// Transformed code\n" +
        "if (x) { return; } else { alert('reachable'); }";
    assertEquals(expected2, root.toString());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

    // When
    Node externs = null;
    Node root = null;
    uce.process(externs, root);

    // Then
    // Verify that unconditional branching statements are removed
    // test case 1: break
    String expected1 = 
        "// Original code\n" +
        "break;\n" +
        "\n" +
        "// Transformed code\n" +
        "";
    assertEquals(expected1, root.toString());

    // test case 2: return
    String expected2 = 
        "// Original code\n" +
        "return;\n" +
        "\n" +
        "// Transformed code\n" +
        "";
    assertEquals(expected2, root.toString());

    // test case 3: continue
    String expected3 = 
        "// Original code\n" +
        "continue;\n" +
        "\n" +
        "// Transformed code\n" +
        "";
    assertEquals(expected3, root.toString());
  }
}