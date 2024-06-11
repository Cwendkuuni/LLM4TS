package com.google.javascript.jscomp.tests;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // When: traversing a valid NodeTree
    NodeTraversal nodeTraversal = new NodeTraversal(compiler.getRootNode(), checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisWithAssignment() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with an assignment to this
    Node assignmentNode = new Node(Token.ASSIGN);
    Node thisNode = new Node(Token.THIS);
    Node assignmentValue = new Node(Token.STRING, "value");
    assignmentNode.addChildToEnd(thisNode);
    thisNode.addChildToEnd(assignmentValue);

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToEnd(assignmentNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: a diagnostic message is reported
    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    List<DiagnosticPosition> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.stream().anyMatch(d -> d.getType() == diagnosticType));
  }

  @Test
  public void testCheckGlobalThisWithFunction() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with a function
    Node functionNode = new Node(Token.FUNCTION, null);
    Node functionName = new Node(Token.IDENT, "functionName");
    functionNode.addChildToEnd(functionName);
    Node functionBody = new Node(Token.BLOCK, null);
    functionNode.addChildToEnd(functionBody);

    rootNode.addChildToEnd(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisWithFunctionWithConstructorAnnotated() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with a constructor function
    Node functionNode = new Node(Token.FUNCTION, null);
    Node functionName = new Node(Token.IDENT, "FunctionName");
    functionNode.addChildToEnd(functionName);
    functionNode.setIsConstructor(true);

    Node functionBody = new Node(Token.BLOCK, null);
    functionNode.addChildToEnd(functionBody);

    rootNode.addChildToEnd(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisWithFunctionWithThisAnnotation() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with a function annotated with @this
    Node functionNode = new Node(Token.FUNCTION, null);
    Node functionName = new Node(Token.IDENT, "functionName");
    functionNode.addChildToEnd(functionName);
    Node jsDoc = new Node(Token.COMMENT, "/** @this {MyType} */");
    functionNode.addChildToEnd(jsDoc);
    Node functionBody = new Node(Token.BLOCK, null);
    functionNode.addChildToEnd(functionBody);

    rootNode.addChildToEnd(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisWithFunctionWithNoLogicalPlaceForThisAnnotation() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with a function where there's no logical place to put a this annotation
    Node functionNode = new Node(Token.FUNCTION, null);
    Node functionName = new Node(Token.IDENT, "functionName");
    functionNode.addChildToEnd(functionName);
    Node functionBody = new Node(Token.BLOCK, null);
    functionNode.addChildToEnd(functionBody);

    rootNode.addChildToEnd(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisWithPrototypePropertyAccess() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with a prototype property access
    Node prototypePropertyAccessNode = new Node(Token.GETPROP, null);
    Node prototypeNode = new Node(Token.IDENT, "prototype");
    prototypePropertyAccessNode.addChildToEnd(prototypeNode);
    Node thisNode = new Node(Token.THIS);
    prototypePropertyAccessNode.addChildToEnd(thisNode);

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToEnd(prototypePropertyAccessNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeSubpropertyAccess() {
    // Given: an instance of CheckGlobalThis
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Given: a NodeTree with a prototype subproperty access
    Node prototypeSubpropertyAccessNode = new Node(Token.GETPROP, null);
    Node prototypeNode = new Node(Token.IDENT, "prototype");
    prototypeNode.addChildToEnd(new Node(Token.IDENT, "property"));
    prototypeSubpropertyAccessNode.addChildToEnd(prototypeNode);
    Node thisNode = new Node(Token.THIS);
    prototypeSubpropertyAccessNode.addChildToEnd(thisNode);

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToEnd(prototypeSubpropertyAccessNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, checkGlobalThis);
    nodeTraversal.traverse();

    // Then: no diagnostic messages are reported
    assertEmpty(compiler.getDiagnostics());
  }
}