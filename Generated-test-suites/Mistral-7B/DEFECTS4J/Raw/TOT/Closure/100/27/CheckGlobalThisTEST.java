import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.Checker.$;
import static com.google.javascript.jscomp.CheckType.CHECK_TYPE_ERROR;
import static com.google.javascript.rhino.NodeTree.makeFunction;
import static com.google.javascript.rhino.NodeTree.makeIdentifier;
import static com.google.javascript.rhino.NodeTree.makePropertyAccess;
import static com.google.javascript.rhino.NodeTree.makeString;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    String jsCode = "function Foo() { this.a = 1; }";
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    assertThat(compiler.getDiagnostics(), empty());
  }

  @Test
  public void testUnsafeAssignmentToThis() {
    String jsCode = "var a = function() { this = 1; }";
    CheckLevel level = CheckLevel.ERROR;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    CheckTypeError error = $(CHECK_TYPE_ERROR, 0);
    assertThat(compiler.getDiagnostics(), contains(error));
  }

  @Test
  public void testUnsafePropertyAccessOnThis() {
    String jsCode = "function Foo() { this.prototype.a = 1; }";
    CheckLevel level = CheckLevel.ERROR;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    CheckTypeError error = $(CHECK_TYPE_ERROR, 0);
    assertThat(compiler.getDiagnostics(), contains(error));
  }

  @Test
  public void testUnsafePrototypePropertyAccessOnThis() {
    String jsCode = "function Foo() {} Foo.prototype.a = function() { this.prototype.b = 1; }";
    CheckLevel level = CheckLevel.ERROR;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    CheckTypeError error = $(CHECK_TYPE_ERROR, 0);
    assertThat(compiler.getDiagnostics(), contains(error));
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String jsCode = "function @this Foo() { this.a = 1; }";
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    assertThat(compiler.getDiagnostics(), empty());
  }

  @Test
  public void testConstructorFunction() {
    String jsCode = "function Foo() { this.a = 1; } Foo.prototype.constructor = Foo;";
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    assertThat(compiler.getDiagnostics(), empty());
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    String jsCode = "function Foo() { this.a = 1; }";
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    compiler.process(makeString(jsCode), null, null);
    checker.process(compiler.getRootNode(), null);

    CheckTypeError error = $(CHECK_TYPE_ERROR, 0);
    assertThat(compiler.getDiagnostics(), contains(error));
  }

  @Test
  public void testNestedAssignments() {
    String jsCode = "(a = this).b = 1;";
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node root = makeFunction("test", null, null, null, null, null);
    Node a = root.getLastChild();
    Node assign = makeAssign(a, makeIdentifier("this"));
    Node b = makePropertyAccess(assign, "b");
    Node value = makeNumber(1);
    Node assign2 = makeAssign(b, value);

    root.setLastChild(assign2);

    compiler.process(makeString(jsCode), root, null);
    checker.process(compiler.getRootNode(), null);

    assertThat(compiler.getDiagnostics(), empty());
  }

  private Node makeFunction(String name, Node... args) {
    return makeFunction(name, null, null, args, null, null);
  }

  private Node makeFunction(String name, Node body, Node prototype,
      Node[] args, Node sourceName, Node sourceStart) {
    Node function = makeFunction(name, args.length, null, null, null, null);
    function.setName(name);
    if (body != null) {
      function.setBody(body);
    }
    if (prototype != null) {
      function.setPrototype(prototype);
    }
    if (args != null) {
      Node param = null;
      for (Node arg : args) {
        if (param == null) {
          param = arg;
        } else {
          Node p = makeIdentifier(getName(arg));
          Node prop = makePropertyAccess(function, "prototype");
          Node pProp = makePropertyAccess(prop, "arguments");
          Node pParam = makePropertyAccess(pProp, getIndexOf(param));
          Node assign = makeAssign(pParam, arg);
          function.addChild(assign);
        }
      }
    }
    return function;
  }

  private Node makeIdentifier(String name) {
    return makeIdentifier(name, null);
  }

  private Node makeIdentifier(String name, Node prefix) {
    return makeIdentifier(name, null, null, null, null, null);
  }

  private Node makeIdentifier(String name, Node prefix, Node id, Node attrs,
      Node flags, Node type) {
    return makeIdentifier(name, null, id, attrs, flags, type, null);
  }

  private Node makeIdentifier(String name, Node[] id, Node attrs, Node flags,
      Node type, Node init) {
    return makeIdentifier(name, null, id, attrs, flags, type, init, null);
  }

  private Node makeIdentifier(String name, Node[] id, Node attrs, Node flags,
      Node type, Node init, Node scope) {
    return makeIdentifier(name, id, attrs, flags, type, init, scope, null);
  }

  private Node makeIdentifier(String name, Node[] id, Node attrs, Node flags,
      Node type, Node init, Node scope, Node parent) {
    Node identifier = makeToken(Token.NAME, name);
    if (id != null) {
      for (Node i : id) {
        identifier = makeQualifiedName(identifier, i);
      }
    }
    if (attrs != null) {
      identifier = makeJSDocInfo(identifier, attrs);
    }
    if (flags != null) {
      identifier = makeFlags(identifier, flags);
    }
    if (type != null) {
      identifier = makeTypeAnnotation(identifier, type);
    }
    if (init != null) {
      identifier = makeInitializer(identifier, init);
    }
    if (scope != null) {
      identifier = makeChildNode(scope, identifier);
    }
    if (parent != null) {
      parent.addChild(identifier);
    }
    return identifier;
  }

  private Node makePropertyAccess(Node object, String name) {
    return makePropertyAccess(object, makeIdentifier(name));
  }

  private Node makePropertyAccess(Node object, Node name) {
    Node prop = makeGetProp(object, name);
    Node index = makeString("");
    Node getProp = makeGetProp(prop, index);
    return getProp;
  }

  private Node makeGetProp(Node object, Node name) {
    Node getProp = makeToken(Token.GETPROP, object, name);
    return getProp;
  }

  private Node makeAssign(Node lhs, Node rhs) {
    Node assign = makeToken(Token.ASSIGN, lhs, rhs);
    return assign;
  }

  private Node makeNumber(double value) {
    return makeToken(Token.NUMBER, value);
  }

  private Node makeString(String value) {
    return makeToken(Token.STRING, value);
  }

  private Node makeToken(int type, Node... children) {
    Node token = makeToken(type);
    for (Node child : children) {
      token.addChild(child);
    }
    return token;
  }

  private Node makeToken(int type) {
    Node token = makeNode(type);
    return token;
  }

  private Node makeNode(int type) {
    Node node = makeToken(type);
    node.setType(type);
    return node;
  }

  private Node makeJSDocInfo(Node node, Node jsDoc) {
    node.setJSDocInfo(jsDoc);
    return node;
  }

  private Node makeFlags(Node node, Node flags) {
    node.setFlags(flags);
    return node;
  }

  private Node makeTypeAnnotation(Node node, Node type) {
    node.setTypeAnnotation(type);
    return node;
  }

  private Node makeInitializer(Node node, Node initializer) {
    node.setInitializer(initializer);
    return node;
  }

  private Node makeChildNode(Node parent, Node node) {
    parent.addChild(node);
    return node;
  }

  private Node makeFunction(int type, int arity, Node body, Node prototype,
      Node sourceName, Node sourceStart) {
    Node function = makeNode(type);
    function.setArity(arity);
    function.setBody(body);
    function.setPrototype(prototype);
    function.setSourceName(sourceName);
    function.setSourceStart(sourceStart);
    return function;
  }

  private int getIndexOf(Node node) {
    return node.getIndex();
  }

  private String getName(Node node) {
    return node.getName();
  }
}