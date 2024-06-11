import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.jscomp.CompilerInput.make;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createVar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class VarCheckTest {

  @Test
  public void testVarCheck() {
    CompilerInput input = make("test.js", "", "/*empty*/", null, null);
    AbstractCompiler compiler = new Compiler(input);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new VarCheck(compiler));

    Node root = Node.newProgram(Token.PROGRAM, null);
    traverse(compiler, root, null);
    passManager.runPasses(root);

    Scope globalScope = compiler.getGlobalScope();
    assertFalse("Expected no undefined variables",
        globalScope.hasVar("undefinedVar"));
    assertTrue("Expected variable 'foo' to be declared",
        globalScope.hasVar("foo"));
  }

  @Test(expected = AssertionError.class)
  public void testUndefinedVarError() {
    CompilerInput input = make("test.js", "", "var x = 1;\nvar undefinedVar;", null, null);
    AbstractCompiler compiler = new Compiler(input);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new VarCheck(compiler));

    Node root = Node.newProgram(Token.PROGRAM, null);
    Node programRoot = NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getAstRoot(compiler)), this).get(0);
    NodeUtil.cloneSubTreeInto(programRoot, root);

    traverse(compiler, root, null);
    passManager.runPasses(root);

    Scope globalScope = compiler.getGlobalScope();
    assertFalse("Expected undefinedVar to be reported",
        globalScope.hasVar("undefinedVar"));
  }

  @Test
  public void testModuleDependencyError() {
    CompilerInput input1 = make("module1.js", "module1", "", null, null);
    CompilerInput input2 = make("module2.js", "module2", "", null, null);
    AbstractCompiler compiler = new Compiler(input1);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new VarCheck(compiler));

    Node root1 = Node.newProgram(Token.PROGRAM, null);
    Node module1Root = Node.newBlock(Token.BLOCK, null);
    module1Root.addChildToBack(new Node(Token.NAME, createName("foo")));
    root1.addChildToBack(module1Root);

    Node root2 = Node.newProgram(Token.PROGRAM, null);
    Node module2Root = Node.newBlock(Token.BLOCK, null);
    module2Root.addChildToBack(new Node(Token.NAME, createName("module1")));
    module2Root.addChildToBack(new Node(Token.NAME, createName("foo")));
    root2.addChildToBack(module2Root);

    traverse(compiler, root1, null);
    passManager.runPasses(root1);
    Node moduleGraph = compiler.getModuleGraph();

    CompilerInput input3 = make("test.js", "test", "", input1, input2);
    Node root3 = Node.newProgram(Token.PROGRAM, null);
    Node testRoot = Node.newBlock(Token.BLOCK, null);
    testRoot.addChildToBack(new Node(Token.NAME, createName("module2")));
    root3.addChildToBack(testRoot);

    traverse(compiler, root3, null);
    passManager.runPasses(root3);

    Scope globalScope = compiler.getGlobalScope();
    assertTrue("Expected foo in module2 to be reported",
        globalScope.hasError(VarCheck.VIOLATED_MODULE_DEP_ERROR, 
            "module1 cannot reference foo, defined in module2, since module1 loads after module2"));
  }

  @Test
  public void testMissingModuleDependencyWarning() {
    CompilerInput input1 = make("module1.js", "module1", "", null, null);
    CompilerInput input2 = make("module2.js", "module2", "", null, null);
    AbstractCompiler compiler = new Compiler(input1);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new VarCheck(compiler));

    Node root1 = Node.newProgram(Token.PROGRAM, null);
    Node module1Root = Node.newBlock(Token.BLOCK, null);
    module1Root.addChildToBack(new Node(Token.NAME, createName("foo")));
    root1.addChildToBack(module1Root);

    Node root2 = Node.newProgram(Token.PROGRAM, null);
    Node module2Root = Node.newBlock(Token.BLOCK, null);
    module2Root.addChildToBack(new Node(Token.NAME, createName("module1")));
    module2Root.addChildToBack(new Node(Token.NAME, createName("foo")));
    root2.addChildToBack(module2Root);

    traverse(compiler, root1, null);
    passManager.runPasses(root1);
    Node moduleGraph = compiler.getModuleGraph();

    CompilerInput input3 = make("test.js", "test", "", input1, input2);
    Node root3 = Node.newProgram(Token.PROGRAM, null);
    Node testRoot = Node.newBlock(Token.BLOCK, null);
    testRoot.addChildToBack(new Node(Token.NAME, createName("module2")));
    root3.addChildToBack(testRoot);

    traverse(compiler, root3, null);
    passManager.runPasses(root3);

    Scope globalScope = compiler.getGlobalScope();
    // Expected warning because module1 is not listed as a dependency for module2
    assertTrue("Expected missing dependency warning",
        globalScope.hasWarning(VarCheck.MISSING_MODULE_DEP_ERROR,
            "missing module dependency; module test should depend on module1 because it references foo"));
  }
}