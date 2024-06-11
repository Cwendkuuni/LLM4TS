import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeThisUsages() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.traverse(checker, NodeTraversal.emptyPredicate(), NodeTraversal.emptyNode())).thenReturn(traversal);

    Node function = createFunctionNode("function foo() { this.bar = 'safe'; }");
    Node object = createObjectNode("var obj = { foo: function() { this.bar = 'safe'; } }");

    checker.visit(traversal, null, null);
    checker.visit(traversal, function, null);
    checker.visit(traversal, object, null);

    traversal.traverse(function);
    traversal.traverse(object);

    assertTrue(checker.level.hasWarnings());
  }

  @Test
  public void testUnsafeThisUsages() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.traverse(checker, NodeTraversal.emptyPredicate(), NodeTraversal.emptyNode())).thenReturn(traversal);

    Node constructor = createFunctionNode("function Foo() { this.x = 1; }");
    Node method = createFunctionNode("function Foo.prototype.bar = function() { this.x = 2; }");
    Node prototypeProperty = createFunctionNode("function Foo.prototype.getX = function() { return this.x; }");
    Node global = createFunctionNode("this.x = 1;");

    checker.visit(traversal, null, null);
    checker.visit(traversal, constructor, null);
    checker.visit(traversal, method, constructor);
    checker.visit(traversal, prototypeProperty, constructor);
    checker.visit(traversal, global, null);

    traversal.traverse(constructor);
    traversal.traverse(method);
    traversal.traverse(prototypeProperty);
    traversal.traverse(global);

    assertTrue(checker.level.hasErrors());
  }

  private Node createFunctionNode(String jsCode) {
    Node node = new Node(Token.FUNCTION);
    node.setSource(jsCode);
    node.setJSDocInfo(createFunctionJsDocInfo(jsCode));
    return node;
  }

  private Node createObjectNode(String jsCode) {
    Node node = new Node(Token.SCRIPT);
    node.setSource(jsCode);
    Node object = new Node(Token.NAME);
    object.setString("obj");
    Node assign = new Node(Token.ASSIGN);
    Node value = new Node(Token.OBJECT);
    Node property = new Node(Token.GETPROP);
    property.setString("foo");
    Node function = new Node(Token.FUNCTION);
    function.setSource("function() { this.bar = 'safe'; }");
    property.setParent(value);
    assign.setParent(object);
    assign.setFirstChild(value);
    node.setFirstChild(object);
    return node;
  }

  private JSDocInfo createFunctionJsDocInfo(String jsCode) {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setSource(jsCode);
    jsDoc.setKind(JSDocInfo.Function);
    return jsDoc;
  }
}