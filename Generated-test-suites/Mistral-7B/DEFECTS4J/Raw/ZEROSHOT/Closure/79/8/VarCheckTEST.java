import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.newName;
import static com.google.javascript.rhino.NodeFactory.newVariable;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class VarCheckTest {

  @Test
  public void testUndefinedVar() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.UNDEFINED_VARIABLE, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    CompilerInput input = NodeFactory.newInput("a = b;");
    Node root = NodeFactory.newProgram();
    root.setLine(1);
    root.setSourceFile("test.js");
    input.setASTRoot(root);

    VarCheck varChecker = new VarCheck(compiler);
    traverse(compiler, input, varChecker);

    Node bNode = findNodeByName(root, "b");
    assertNotNull(bNode);

    Scope scope = varChecker.getScope();
    Scope.Var var = scope.getVar("a");
    assertNull(var);

    try {
      compiler.compile(Lists.newArrayList(input), null);
      fail();
    } catch (CompilerException e) {
      assertEquals(1, e.diagnostics().size());
      assertEquals("variable a is undefined", e.diagnostics().get(0).getMessage());
    }
  }

  @Test
  public void testDuplicateVar() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.UNDEFINED_VARIABLE, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    CompilerInput input = NodeFactory.newInput("var a = 1;\nvar a = 2;");
    Node root = NodeFactory.newProgram();
    root.setLine(1);
    root.setSourceFile("test.js");
    input.setASTRoot(root);

    VarCheck varChecker = new VarCheck(compiler);
    traverse(compiler, input, varChecker);

    Scope scope = varChecker.getScope();
    Scope.Var var = scope.getVar("a");
    assertNotNull(var);
    assertEquals(1, var.lineNumber());

    try {
      compiler.compile(Lists.newArrayList(input), null);
      fail();
    } catch (CompilerException e) {
      // No error expected
    }
  }

  @Test
  public void testMissingModuleDependency() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.MISSING_MODULE_DEPENDENCY, CheckLevel.ERROR);
    options.setWarningLevel(JSError.UNDEFINED_VARIABLE, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    CompilerInput moduleAInput = NodeFactory.newInput("module A {\n  export function foo() {} \n}");
    Node moduleARoot = NodeFactory.newProgram();
    moduleARoot.setLine(1);
    moduleARoot.setSourceFile("A.js");
    moduleAInput.setASTRoot(moduleARoot);

    CompilerInput input = NodeFactory.newInput("import 'A';\nvar a = A.foo;");
    Node root = NodeFactory.newProgram();
    root.setLine(1);
    root.setSourceFile("test.js");
    input.setASTRoot(root);

    VarCheck varChecker = new VarCheck(compiler);
    traverse(compiler, moduleAInput, varChecker);

    traverse(compiler, input, varChecker);

    Node aNode = findNodeByName(root, "a");
    assertNotNull(aNode);

    Scope scope = varChecker.getScope();
    Scope.Var var = scope.getVar("a");
    assertNotNull(var);

    try {
      compiler.compile(Lists.newArrayList(input, moduleAInput), null);
      fail();
    } catch (CompilerException e) {
      assertEquals(1, e.diagnostics().size());
      assertEquals("missing module dependency; module test should depend on A",
          e.diagnostics().get(0).getMessage());
    }
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.STRICT_MODULE_DEPENDENCY, CheckLevel.ERROR);
    options.setWarningLevel(JSError.UNDEFINED_VARIABLE, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    CompilerInput moduleAInput = NodeFactory.newInput("module A {\n  export function foo() {} \n}");
    Node moduleARoot = NodeFactory.newProgram();
    moduleARoot.setLine(1);
    moduleARoot.setSourceFile("A.js");
    moduleAInput.setASTRoot(moduleARoot);

    CompilerInput input = NodeFactory.newInput("import 'A';\nvar a = A.foo;");
    Node root = NodeFactory.newProgram();
    root.setLine(1);
    root.setSourceFile("test.js");
    input.setASTRoot(root);

    VarCheck varChecker = new VarCheck(compiler);
    traverse(compiler, moduleAInput, varChecker);

    traverse(compiler, input, varChecker);

    Node aNode = findNodeByName(root, "a");
    assertNotNull(aNode);

    Scope scope = varChecker.getScope();
    Scope.Var var = scope.getVar("a");
    assertNotNull(var);

    try {
      compiler.compile(Lists.newArrayList(input, moduleAInput), null);
      fail();
    } catch (CompilerException e) {
      // No error expected
    }

    options.setWarningLevel(JSError.STRICT_MODULE_DEPENDENCY, CheckLevel.ERROR);

    try {
      compiler.compile(Lists.newArrayList(input, moduleAInput), null);
      fail();
    } catch (CompilerException e) {
      assertEquals(1, e.diagnostics().size());
      assertEquals("module test cannot reference A, defined in A",
          e.diagnostics().get(0).getMessage());
    }
  }

  @Test
  public void testNameReferenceInExterns() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.UNDEFINED_VARIABLE, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    CompilerInput input = NodeFactory.newInput("/** @suppress {namecheck} */\nvar a = b;");
    Node root = NodeFactory.newProgram();
    root.setLine(1);
    root.setSourceFile("test.js");
    input.setASTRoot(root);

    VarCheck varChecker = new VarCheck(compiler);
    traverse(compiler, input, varChecker);

    Node bNode = findNodeByName(root, "b");
    assertNotNull(bNode);

    Scope scope = varChecker.getScope();
    Scope.Var var = scope.getVar("a");
    assertNull(var);

    try {
      compiler.compile(Lists.newArrayList(input), null);
      fail();
    } catch (CompilerException e) {
      // No error expected
    }
  }

  @Test
  public void testUndefinedExternVar() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.UNDEFINED_EXTERN_VAR_ERROR, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    CompilerInput input = NodeFactory.newInput("/** @extern */\nvar a;");
    Node root = NodeFactory.newProgram();
    root.setLine(1);
    root.setSourceFile("test.js");
    input.setASTRoot(root);

    VarCheck varChecker = new VarCheck(compiler);
    traverse(compiler, input, varChecker);

    try {
      compiler.compile(Lists.newArrayList(input), null);
      fail();
    } catch (CompilerException e) {
      assertEquals(1, e.diagnostics().size());
      assertEquals("name a is not undefined in the externs.",
          e.diagnostics().get(0).getMessage());
    }
  }

  private Node findNodeByName(Node node, String name) {
    if (node.getType() == Token.NAME && node.getString().equals(name)) {
      return node;
    }
    if (node.getFirstChild() != null) {
      for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
        Node found = findNodeByName(child, name);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private Scope getScope() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(null)) {
      @Override
      public Scope getScope() {
        return super.getScope();
      }
    };
    VarCheck varChecker = new VarCheck(null, true);
    traverse(new AbstractCompiler(null), NodeFactory.newProgram(), varChecker);
    return varChecker.getScope();
  }
}