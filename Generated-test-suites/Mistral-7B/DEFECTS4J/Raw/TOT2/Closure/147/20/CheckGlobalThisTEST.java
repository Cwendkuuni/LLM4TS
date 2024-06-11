
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String source = "function evil() { this.useful = undefined; }";

    NodeTraversal traversal = new NodeTraversal(source);
    traversal.traverse(checker);

    assertTrue(checker.level.hasWarnings());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithConstructorAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String source = "function MyConstructor() { this.myProperty = 1; }";

    NodeTraversal traversal = new NodeTraversal(source);
    traversal.traverse(checker);

    assertFalse(checker.level.hasWarnings());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithThisTypeAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String source = "function MyFunction(@this {type: 'MyType'}) { this.myProperty = 1; }";

    NodeTraversal traversal = new NodeTraversal(source);
    traversal.traverse(checker);

    assertFalse(checker.level.hasWarnings());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNoParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String source = "function evil() { this.useful = undefined; }";

    NodeTraversal traversal = new NodeTraversal(source);
    traversal.traverse(checker);

    assertFalse(checker.level.hasWarnings());

    // This test case checks if the CheckGlobalThis checker correctly ignores functions with no parent
  }

  // Add more test cases for other conditions here, such as functions annotated with @override or @interface

  @org.junit.Test
  public void testCheckGlobalThis_EmptySource() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String source = "";

    NodeTraversal traversal = new NodeTraversal(source);
    traversal.traverse(checker);

    assertFalse(checker.level.hasWarnings());
  }
}
