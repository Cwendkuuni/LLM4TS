package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(null, checkGlobalThis, null);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node globalContext = new Node(Token.SCRIPT, null);
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    globalContext.addChildToFront(functionDeclaration);

    assertThat(checkGlobalThis.shouldTraverse(traversal, functionDeclaration, globalContext)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructor = new Node(Token.FUNCTION, null);
    Node constructorKeyword = new Node(Token.THIS, null);
    constructor.addChildToFront(constructorKeyword);
    constructor.addChildToFront(new Node(Token.NAME, "ConstructorFunction"));

    JSDocInfo constructorJsDocInfo = new JSDocInfo();
    constructorJsDocInfo.setConstructor(true);

    constructor.setJSDocInfo(constructorJsDocInfo);

    Node globalContext = new Node(Token.SCRIPT, null);
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    globalContext.addChildToFront(functionDeclaration);

    globalContext.addChildToFront(constructor);

    assertThat(checkGlobalThis.shouldTraverse(traversal, functionDeclaration, globalContext)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithAtThis() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node functionKeyword = new Node(Token.FUNCTION, null);
    Node thisAnnotation = new Node(Token.THIS, null);
    Node thisAnnotationKeyword = new Node(Token.KEYWORD, "@this");

    thisAnnotation.addChildToFront(thisAnnotationKeyword);
    functionKeyword.addChildToFront(thisAnnotation);
    functionKeyword.addChildToFront(new Node(Token.NAME, "FunctionAnnotatedWithAtThis"));
    functionDeclaration.addChildToFront(functionKeyword);

    JSDocInfo functionJsDocInfo = new JSDocInfo();
    functionJsDocInfo.setThisType(true);

    functionDeclaration.setJSDocInfo(functionJsDocInfo);

    Node globalContext = new Node(Token.SCRIPT, null);

    globalContext.addChildToFront(functionDeclaration);

    assertThat(checkGlobalThis.shouldTraverse(traversal, functionDeclaration, globalContext)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithAtConstructor() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node functionKeyword = new Node(Token.FUNCTION, null);
    Node constructorAnnotation = new Node(Token.THIS, null);
    Node constructorAnnotationKeyword = new Node(Token.KEYWORD, "@constructor");

    constructorAnnotation.addChildToFront(constructorAnnotationKeyword);
    functionKeyword.addChildToFront(constructorAnnotation);
    functionKeyword.addChildToFront(new Node(Token.NAME, "FunctionAnnotatedWithAtConstructor"));
    functionDeclaration.addChildToFront(functionKeyword);

    JSDocInfo functionJsDocInfo = new JSDocInfo();
    functionJsDocInfo.setConstructor(true);

    functionDeclaration.setJSDocInfo(functionJsDocInfo);

    Node globalContext = new Node(Token.SCRIPT, null);

    globalContext.addChildToFront(functionDeclaration);

    assertThat(checkGlobalThis.shouldTraverse(traversal, functionDeclaration, globalContext)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutJSDoc() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node functionKeyword = new Node(Token.FUNCTION, null);
    functionKeyword.addChildToFront(new Node(Token.NAME, "FunctionWithoutJSDoc"));
    functionDeclaration.addChildToFront(functionKeyword);

    Node globalContext = new Node(Token.SCRIPT, null);

    globalContext.addChildToFront(functionDeclaration);

    assertThat(checkGlobalThis.shouldTraverse(traversal, functionDeclaration, globalContext)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithPropertyAccess() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node functionKeyword = new Node(Token.FUNCTION, null);
    Node thisKeyword = new Node(Token.THIS, null);
    Node getProperty = new Node(Token.GETPROP, null);
    getProperty.setString("property");

    thisKeyword.addChildToFront(functionKeyword);
    functionKeyword.addChildToFront(thisKeyword);
    functionKeyword.addChildToFront(getProperty);
    functionDeclaration.addChildToFront(functionKeyword);

    Node globalContext = new Node(Token.SCRIPT, null);

    Node parent = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.GETPROP, null);
    Node rhs = functionDeclaration;
    Node getPrototype = new Node(Token.GETPROP, null);
    getPrototype.setString("prototype");
    lhs.addChildToFront(new Node(Token.GETPROP, null).setString("someObject"));
    lhs.addChildToFront(getPrototype);

    parent.addChildToFront(lhs);
    parent.addChildToFront(rhs);

    globalContext.addChildToFront(parent);

    assertThat(checkGlobalThis.shouldTraverse(traversal, functionDeclaration, globalContext)).isTrue();
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node functionKeyword = new Node(Token.FUNCTION, null);
    Node thisKeyword = new Node(Token.THIS, null);

    functionKeyword.addChildToFront(thisKeyword);
    functionKeyword.addChildToFront(new Node(Token.NAME, "FunctionWithGlobalThis"));
    functionDeclaration.addChildToFront(functionKeyword);

    Node globalContext = new Node(Token.SCRIPT, null);

    globalContext.addChildToFront(functionDeclaration);

    traversal.traverse(globalContext);

    ImmutableList<DiagnosticPosition> diagnostics = compiler.getDiagnostics();

    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0).getLineNumber()).isEqualTo(1);
  }

  @Test
  public void testShouldReportGlobalThisInPrototypeChain() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node functionKeyword = new Node(Token.FUNCTION, null);
    Node thisKeyword = new Node(Token.THIS, null);

    functionKeyword.addChildToFront(thisKeyword);

    Node prototype = new Node(Token.ASSIGN, null);
    Node prototypeValue = new Node(Token.GETPROP, null);
    prototypeValue.setString("prototype");

    Node prototypeObject = new Node(Token.NAME, "PrototypeObject");

    prototype.addChildToFront(prototypeValue);
    prototype.addChildToFront(prototypeObject);

    functionKeyword.addChildToFront(prototype);

    functionKeyword.addChildToFront(new Node(Token.NAME, "FunctionWithGlobalThisInPrototype"));
    functionDeclaration.addChildToFront(functionKeyword);

    Node globalContext = new Node(Token.SCRIPT, null);

    globalContext.addChildToFront(functionDeclaration);

    traversal.traverse(globalContext);

    ImmutableList<DiagnosticPosition> diagnostics = compiler.getDiagnostics();

    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0).getLineNumber()).isEqualTo(1);
  }
}