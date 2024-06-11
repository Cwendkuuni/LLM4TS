package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyConstructor() {}");
    JSDocInfo jsDoc = createFunctionJsDocInfo("MyConstructor", "@constructor");

    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = createNode("var MyInstance = new MyConstructor();");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThisType() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    JSDocInfo jsDoc = createFunctionJsDocInfo("MyFunction", "@this {foo: string}");

    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = createNode("var MyInstance = new MyFunction();");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionNotAnnotatedWithThisOrConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");

    Node parentNode = createNode("var MyInstance = new MyFunction();");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideBlock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("{ MyFunction(); }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("(function() { MyFunction(); })();");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("{ prototype: { MyFunction: function() {} } }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypePropertySubProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo.bar = 'baz'; }");
    Node parentNode = createNode("{ prototype: { MyFunction: function() {}, foo: {} } }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideObjectLiteral() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("{ MyFunction: function() {} }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideObjectLiteralKey() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("{ 'MyFunction': function() {} }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInsideObjectLiteralKeyNumber() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("{ 0: function() { MyFunction(); } }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() {}");
    Node parentNode = createNode("var MyPrototype = { prototype: { MyFunction: function() {} } }");
    Node prototypeNode = parentNode.getLastChild();

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypeNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionGettingLentToPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node functionNode = createNode("function MyFunction() { this.foo = 'bar'; }");
    Node parentNode = createNode("var MyPrototype = { get MyFunction() { new MyFunction(); } }");

    boolean result = checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node thisNode = createNode("this");
    Node parentNode = createNode("var x = this;");

    boolean shouldReport = checkGlobalThis.shouldReportThis(thisNode, parentNode);

    assertThat(shouldReport).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    Node thisNode = createNode("this");
    Node parentNode = createNode("{ prototype: { get x() { return this; } } }");
    Node propertyNode = parentNode.getLastChild();

    boolean shouldReport = checkGlobalThis.shouldReportThis(thisNode, propertyNode);

    assertThat(shouldReport).isTrue();
  }

  private static Node createNode(String code) {
    return new RhinoCompiler().compileStringToNode(code).getFirstChild();
  }

  private static JSDocInfo createFunctionJsDocInfo(String name, String annotation) {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setName(name);
    jsDocInfo.setKinds(new String[]{"constructor", "function"});
    jsDocInfo.putAnnotation("@" + annotation);
    return jsDocInfo;
  }
}