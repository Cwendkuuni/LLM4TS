package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Info;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParserUtils;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test class for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, Info.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  /**
   * Tests the constructor.
   */
  @Test
  public void testConstructor() {
    assertTrue(checker != null);
  }

  /**
   * Tests {@link #shouldTraverse(NodeTraversal, Node, Node)}.
   */
  @Test
  public void testShouldTraverse() {
    Node parent = ParserUtils.parseJavaScript("function Foo() {}\n", null).getLastChild();
    Node n = ParserUtils.parseJavaScript("function Bar() {}\n", null).getLastChild();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);
    when(compiler.getContext()).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, n, parent));
  }

  /**
   * Tests {@link #shouldTraverse(NodeTraversal, Node, Node)} with a constructor.
   */
  @Test
  public void testShouldTraverseConstructor() {
    Node constructor = ParserUtils.parseJavaScript("function Foo() {}\n", null).getLastChild();

    when(traversal.getCurrentNode()).thenReturn(constructor);
    when(traversal.getParent()).thenReturn(null);
    when(compiler.getContext()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, ParserUtils.parseJavaScript("this = 1;\n", null).getLastChild(), constructor));
  }

  /**
   * Tests {@link #shouldTraverse(NodeTraversal, Node, Node)} with a method annotated as {@code @constructor}.
   */
  @Test
  public void testShouldTraverseConstructorAnnotation() {
    JSDocInfo doc = new JSDocInfo();
    doc.setConstructor(true);
    Node constructor = ParserUtils.parseJavaScript("/**@constructor */ function Foo() {}\n", null).getLastChild();
    constructor.setJSDocInfo(doc);

    when(traversal.getCurrentNode()).thenReturn(constructor);
    when(traversal.getParent()).thenReturn(null);
    when(compiler.getContext()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, ParserUtils.parseJavaScript("this = 1;\n", null).getLastChild(), constructor));
  }

  /**
   * Tests {@link #shouldTraverse(NodeTraversal, Node, Node)} with a method annotated as {@code @this}.
   */
  @Test
  public void testShouldTraverseThisAnnotation() {
    JSDocInfo doc = new JSDocInfo();
    doc.setThis(true);
    Node method = ParserUtils.parseJavaScript("/**@this {Type} */ function Foo() {}\n", null).getLastChild();
    method.setJSDocInfo(doc);

    when(traversal.getCurrentNode()).thenReturn(method);
    when(traversal.getParent()).thenReturn(null);
    when(compiler.getContext()).thenReturn(traversal);

    when(traversal.makeError(NodeUtil.findFirstParentOfType(method, Token.FUNCTION), level, GLOBAL_THIS))
        .thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, ParserUtils.parseJavaScript("this = 1;\n", null).getLastChild(), method));
  }

  /**
   * Tests {@link #shouldTraverse(NodeTraversal, Node, Node)} with a method that can have a {@code this} annotation.
   */
  @Test
  public void testShouldTraverseMethod() {
    Node method = ParserUtils.parseJavaScript("function Foo() {}\n", null).getLastChild();

    when(traversal.getCurrentNode()).thenReturn(method);
    when(traversal.getParent()).thenReturn(null);
    when(compiler.getContext()).thenReturn(traversal);

    Node parent = ParserUtils.parseJavaScript("var a = {x: function() {}};\n", null).getLastChild();
    Node n = ParserUtils.parseJavaScript("a.x = this;\n", null).getLastChild();

    when(traversal.getCurrentNode()).thenReturn(n);
    when(traversal.getParent()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(traversal, n, method));
  }

  /**
   * Tests {@link #shouldTraverse(NodeTraversal, Node, Node)} with a prototype getter.
   */
  @Test
  public void testShouldTraversePrototypeGetter() {
    Node obj = ParserUtils.parseJavaScript("var Proto = {\n" +
        "  get x() {\n" +
        "    return this;\n" +
        "  }\n" +
        "};\n", null).getLastChild();

    when(traversal.getCurrentNode()).thenReturn(ParserUtils.parseJavaScript("var a = Proto.x;\n", null).getLastChild());
    when(traversal.getParent()).thenReturn(null);
    when(compiler.getContext()).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, ParserUtils.parseJavaScript("this = 1;\n", null).getLastChild(), obj));
  }

  /**
   * Tests {@link #shouldReportThis(Node, Node)}.
   */
  @Test
  public void testShouldReportThis() {
    Node n = ParserUtils.parseJavaScript("this = 1;\n", null).getLastChild();
    Node parent = ParserUtils.parseJavaScript("function Foo() {}\n", null).getLastChild();

    assertTrue(checker.shouldReportThis(n, parent));
  }

  /**
   * Tests {@link #shouldReportThis(Node, Node)} with a property access.
   */
  @Test
  public void testShouldReportThisPropertyAccess() {
    Node n = ParserUtils.parseJavaScript("obj.prototype.x = this;\n", null).getLastChild();
    Node parent = ParserUtils.parseJavaScript("var obj = {prototype: {x: 1}};\n", null).getLastChild();

    assertTrue(checker.shouldReportThis(n, parent));
  }

  /**
   * Tests {@link #visit(NodeTraversal, Node, Node)}.
   */
  @Test
  public void testVisit() {
    Node n = ParserUtils.parseJavaScript("this = 1;\n", null).getLastChild();
    Node parent = ParserUtils.parseJavaScript("function Foo() {}\n", null).getLastChild();

    when(compiler.report(any(Error.class))).thenReturn(null);

    checker.visit(traversal, n, parent);

    assertTrue(compiler.report(traversal.makeError(parent, Info.WARNING, GLOBAL_THIS)) != null);
  }
}