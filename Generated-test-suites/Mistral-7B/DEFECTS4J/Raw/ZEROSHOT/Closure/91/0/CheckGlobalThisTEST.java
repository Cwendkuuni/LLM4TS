import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.javascript.jscomp.Checker$RoundedError.error;
import static com.google.javascript.rhino.NodeTree.makeFunction;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Checker.RoundedError;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeType;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, DiagnosticType.warning);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node node = makeFunction("evil() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(node, checker);

    checker.process(node, null, null);
    traversal.traverse(node);

    List<RoundedError> errors = Lists.newArrayList(checker.getErrors());
    assertThat(errors.size(), equalTo(1));
    RoundedError error = errors.get(0);
    assertThat(error.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThis_Constructor() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, DiagnosticType.warning);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node node = makeFunction("function Evil() {}");
    NodeTraversal traversal = new NodeTraversal(node, checker);

    checker.process(node, null, null);
    traversal.traverse(node);

    List<RoundedError> errors = Lists.newArrayList(checker.getErrors());
    assertThat(errors.isEmpty(), is(true));
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, DiagnosticType.warning);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node node = makeFunction("var Evil = function() {}; Evil.prototype.evil = function() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(node, checker);

    checker.process(node, null, null);
    traversal.traverse(node);

    List<RoundedError> errors = Lists.newArrayList(checker.getErrors());
    assertThat(errors.isEmpty(), is(true));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, DiagnosticType.warning);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node node = makeFunction("@this function Evil() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(node, checker);

    checker.process(node, null, null);
    traversal.traverse(node);

    List<RoundedError> errors = Lists.newArrayList(checker.getErrors());
    assertThat(errors.isEmpty(), is(true));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoThisAnnotation() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, DiagnosticType.warning);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node node = makeFunction("function Evil() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(node, checker);

    checker.process(node, null, null);
    traversal.traverse(node);

    List<RoundedError> errors = Lists.newArrayList(checker.getErrors());
    // In this test case we want to ensure that no error is reported,
    // even though the code is technically violating the rules.
    // This is because the CheckGlobalThis rule is intended to be
    // a best practice guideline, and not a hard error.
    //
    // In practice, the code below is equivalent to:
    // var a = this;
    // a.useful = undefined;
    //
    // Which is semantically equivalent to:
    // this.useful = undefined;
    //
    // However, since the code above is technically not allowed by the
    // rules defined in CheckGlobalThis, we want to ensure that no error is reported.
    assertThat(errors.isEmpty(), is(true));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignLhsChild() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, DiagnosticType.warning);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node node = makeFunction("var a = this; a.useful = undefined;");
    NodeTraversal traversal = new NodeTraversal(node, checker);

    checker.process(node, null, null);
    traversal.traverse(node);

    List<RoundedError> errors = Lists.newArrayList(checker.getErrors());
    // In this test case we want to ensure that no error is reported,
    // even though the code is technically violating the rules.
    // This is because the code below is equivalent to:
    // this.useful = undefined;
    //
    // However, since the code above is technically not allowed by the
    // rules defined in CheckGlobalThis, we want to ensure that no error is reported.
    //
    // In practice, this code would be better written as:
    // var a = {useful: undefined};
    // this.a = a;
    //
    // This way, the 'this' keyword is only used on the left side of an assignment
    // (to assign the 'a' property to 'this'), which is allowed according to the rules.
    assertThat(errors.isEmpty(), is(true));
  }
}