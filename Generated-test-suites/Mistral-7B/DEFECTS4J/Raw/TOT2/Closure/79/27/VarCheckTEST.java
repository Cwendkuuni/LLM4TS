import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.VarCheck.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.io.IOException;
import java.util.Set;

public class VarCheckTest {

  @org.junit.Test
  public void testValidVariable() throws IOException {
    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScope(Token.BLOCK);
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Add a valid variable declaration
    Node variableNode = NodeFactory.createName("validVar");
    root.addChild(new Node(Token.VAR, variableNode));

    // Traverse the AST to check for variable declarations
    varCheck.process(null, root);

    // Assert that the variable was declared without any errors
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testUndefinedVariable() throws IOException {
    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScope(Token.BLOCK);
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Add an undefined variable reference
    Node variableNode = NodeFactory.createName("undefinedVar");
    root.addChild(new Node(Token.GETPROP, NodeFactory.createName("undefined"), variableNode));

    // Traverse the AST to check for variable declarations
    varCheck.process(null, root);

    // Assert that an error was reported for the undefined variable
    String errorMessage = compiler.getDiagnostics().getErrorMessage(0);
    assertEquals("variable undefinedVar is undefined", errorMessage);
  }

  @org.junit.Test
  public void testModuleDependency() throws IOException {
    Compiler compiler = new TestCompiler();
    Node rootModuleA = NodeFactory.createScope(Token.BLOCK);
    Node rootModuleB = NodeFactory.createScope(Token.BLOCK);
    VarCheck varCheck = new VarCheck(compiler);

    // Define a variable in module A and reference it in module B
    Node variableNodeA = NodeFactory.createName("varA");
    Node variableNodeB = NodeFactory.createName("varA");
    rootModuleA.addChild(new Node(Token.VAR, variableNodeA));
    rootModuleB.addChild(new Node(Token.GETPROP, NodeFactory.createName("moduleA"), variableNodeB));

    Node moduleANode = NodeFactory.createName("moduleA");
    Node moduleBNode = NodeFactory.createName("moduleB");

    // Create module graph with dependencies
    Node moduleGraph = NodeFactory.createScope(Token.BLOCK);
    moduleGraph.addChild(new Node(Token.ASSIGN, NodeFactory.createName("moduleA"), moduleANode));
    moduleGraph.addChild(new Node(Token.ASSIGN, NodeFactory.createName("moduleB"), moduleBNode));
    Node moduleGraphRoot = NodeFactory.createScope(Token.BLOCK);
    moduleGraphRoot.addChild(moduleGraph);

    // Traverse the AST for both modules to check for variable declarations and dependencies
    varCheck.process(null, rootModuleA);
    varCheck.process(null, rootModuleB);

    // Assert that no errors were reported
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testMissingModuleDependency() throws IOException {
    Compiler compiler = new TestCompiler();
    Node rootModuleA = NodeFactory.createScope(Token.BLOCK);
    Node rootModuleB = NodeFactory.createScope(Token.BLOCK);
    VarCheck varCheck = new VarCheck(compiler);

    // Define a variable in module A and reference it in module B without declaring the dependency
    Node variableNodeA = NodeFactory.createName("varA");
    Node variableNodeB = NodeFactory.createName("varA");
    rootModuleA.addChild(new Node(Token.VAR, variableNodeA));
    rootModuleB.addChild(new Node(Token.GETPROP, NodeFactory.createName("moduleA"), variableNodeB));

    Node moduleANode = NodeFactory.createName("moduleA");
    Node moduleBNode = NodeFactory.createName("moduleB");

    // Create module graph without the dependency
    Node moduleGraph = NodeFactory.createScope(Token.BLOCK);
    moduleGraph.addChild(NodeFactory.createName("moduleA"));
    moduleGraph.addChild(moduleBNode);
    Node moduleGraphRoot = NodeFactory.createScope(Token.BLOCK);
    moduleGraphRoot.addChild(moduleGraph);

    // Traverse the AST for both modules to check for variable declarations and dependencies
    varCheck.process(null, rootModuleA);
    varCheck.process(null, rootModuleB);

    // Assert that a warning was reported for the missing module dependency
    String warningMessage = compiler.getDiagnostics().getWarningMessage(0);
    assertEquals("missing module dependency; moduleB should depend on moduleA", warningMessage);
  }

  @org.junit.Test
  public void testStrictModuleDependency() throws IOException {
    Compiler compiler = new TestCompiler();
    Node rootModuleA = NodeFactory.createScope(Token.BLOCK);
    Node rootModuleB = NodeFactory.createScope(Token.BLOCK);
    VarCheck varCheck = new VarCheck(compiler, true);

    // Define a variable in module A and reference it in module B with strict dependency checking enabled
    Node variableNodeA = NodeFactory.createName("varA");
    Node variableNodeB = NodeFactory.createName("varA");
    rootModuleA.addChild(new Node(Token.VAR, variableNodeA));
    rootModuleB.addChild(new Node(Token.GETPROP, NodeFactory.createName("moduleA"), variableNodeB));

    Node moduleANode = NodeFactory.createName("moduleA");
    Node moduleBNode = NodeFactory.createName("moduleB");

    // Create module graph with dependencies
    Node moduleGraph = NodeFactory.createScope(Token.BLOCK);
    moduleGraph.addChild(new Node(Token.ASSIGN, NodeFactory.createName("moduleA"), moduleANode));
    moduleGraph.addChild(new Node(Token.ASSIGN, NodeFactory.createName("moduleB"), moduleBNode));
    Node moduleGraphRoot = NodeFactory.createScope(Token.BLOCK);
    moduleGraphRoot.addChild(moduleGraph);

    // Traverse the AST for both modules to check for variable declarations and dependencies
    varCheck.process(null, rootModuleA);
    varCheck.process(null, rootModuleB);

    // Assert that no errors or warnings were reported
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testDuplicateDeclaration() throws IOException {
    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScope(Token.BLOCK);
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Add a duplicate variable declaration
    Node variableNode1 = NodeFactory.createName("duplicateVar");
    Node variableNode2 = NodeFactory.createName("duplicateVar");
    root.addChild(new Node(Token.VAR, variableNode1));
    root.addChild(new Node(Token.VAR, variableNode2));

    // Traverse the AST to check for variable declarations
    varCheck.process(null, root);

    // Assert that no errors were reported
    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}