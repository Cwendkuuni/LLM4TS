import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private Compiler compiler;
  private SimpleDefinitionFinder defFinder;
  private Node root;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    compiler.addPass(new DevirtualizePrototypeMethods(compiler));
    defFinder = new SimpleDefinitionFinder(compiler);
    root = new Node(Token.PROGRAM, null);
  }

  @org.junit.Test
  public void testSimplePrototypeMethodDevirtualization() {
    String jsCode =
        "function Foo() {}" +
            "Foo.prototype.bar = function(x) {" +
            " return x + 1;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    Node expected =
        new Node(
            Token.BLOCK,
            new Node(
                Token.VAR,
                new Node(Token.NAME, "JSCompiler_StaticMethods_bar"),
                new Node(
                    Token.FUNCTION,
                    new Node(Token.NAME, "JSCompiler_StaticMethods_bar"),
                    new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.ADD, new Node(Token.NAME, "this"), new Node(Token.NUM, 1))))),
                new Node(Token.BLOCK, null)));

    Node actual = (Node) defSite.node.getFirstChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testPrototypeMethodCallSiteDevirtualization() {
    String jsCode =
        "function Foo() {}" +
            "Foo.prototype.bar = function(x) {" +
            " return x + 1;" +
            "}" +
            "function FooUser() {" +
            " return new Foo().bar(1);" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    Node expectedCallSite =
        new Node(
            Token.CALL,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_bar"),
            new Node(new Node(Token.THIS, null)));

    Node expected =
        new Node(
            Token.FUNCTION,
            new Node(Token.NAME, "FooUser"),
            new Node(Token.BLOCK, new Node(Token.RETURN, expectedCallSite)));

    Node actual = root;
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testPrototypeMethodDevirtualizationWithArguments() {
    String jsCode =
        "function Foo() {}" +
            "Foo.prototype.bar = function(x, y) {" +
            " return x + y;" +
            "}" +
            "function FooUser() {" +
            " return new Foo().bar(1, 2);" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    Node expectedCallSite =
        new Node(
            Token.CALL,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_bar"),
            new Node(new Node(Token.THIS, null)),
            new Node(Token.NUM, 1),
            new Node(Token.NUM, 2));

    Node expected =
        new Node(
            Token.FUNCTION,
            new Node(Token.NAME, "FooUser"),
            new Node(Token.BLOCK, new Node(Token.RETURN, expectedCallSite)));

    Node actual = root;
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testPrototypeMethodDevirtualizationWithVarargs() {
    String jsCode =
        "function Foo() {}" +
            "Foo.prototype.bar = function() {" +
            " var args = arguments;" +
            " return args.length;" +
            "}" +
            "function FooUser() {" +
            " return new Foo().bar();" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    Node expectedCallSite =
        new Node(
            Token.CALL,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_bar"),
            new Node(new Node(Token.THIS, null)),
            new Node(Token.ARRAY, Lists.newArrayList()));

    Node expected =
        new Node(
            Token.FUNCTION,
            new Node(Token.NAME, "FooUser"),
            new Node(Token.BLOCK, new Node(Token.RETURN, expectedCallSite)));

    Node actual = root;
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testPrototypeMethodDevirtualizationWithObjectAccess() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return this.x;" +
            "}" +
            "function FooUser() {" +
            " return new Foo().bar();" +
            "}" +
            "FooUser.prototype.baz = function() {" +
            " return this.x;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    Node expectedCallSite =
        new Node(
            Token.GETPROP,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_bar"),
            new Node(new Node(Token.THIS, null)));

    Node expected =
        new Node(
            Token.FUNCTION,
            new Node(Token.NAME, "FooUser"),
            new Node(Token.BLOCK, new Node(Token.RETURN, expectedCallSite)));

    Node actual = root;
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testPrototypeMethodDevirtualizationWithPropertyRenaming() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return this.x;" +
            "}" +
            "function FooUser() {" +
            " Foo.prototype.bar.name = 'baz';" +
            " return new Foo().baz();" +
            "}" +
            "FooUser.prototype.qux = function() {" +
            " return this.x;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    Node expectedCallSite =
        new Node(
            Token.GETPROP,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_baz"),
            new Node(new Node(Token.THIS, null)));

    Node expected =
        new Node(
            Token.FUNCTION,
            new Node(Token.NAME, "FooUser"),
            new Node(Token.BLOCK, new Node(Token.RETURN, expectedCallSite)));

    Node actual = root;
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testPrototypeMethodDevirtualizationWithCircularDependency() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return this.x;" +
            "}" +
            "function Bar() {}" +
            "Bar.prototype.qux = function() {" +
            " return this.x;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite1 = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite1, defFinder);

    DefinitionSite defSite2 = defFinder.getDefinitionSite("Bar.prototype.qux");
    rewriteDefinitionIfEligible(defSite2, defFinder);

    Node expectedCallSite1 =
        new Node(
            Token.GETPROP,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_bar"),
            new Node(new Node(Token.THIS, null)));

    Node expectedCallSite2 =
        new Node(
            Token.GETPROP,
            new Node(
                Token.NAME,
                "JSCompiler_StaticMethods_qux"),
            new Node(new Node(Token.THIS, null)));

    Node expected =
        new Node(
            Token.BLOCK,
            new Node(Token.RETURN, expectedCallSite1),
            new Node(
                Token.FUNCTION,
                new Node(Token.NAME, "Bar"),
                new Node(Token.BLOCK, new Node(Token.RETURN, expectedCallSite2))));

    Node actual = root;
    assertEquals(expected, actual);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPrototypeMethodDevirtualizationWithControlFlow() {
    String jsCode =
        "function Foo() {" +
            " if (true) {" +
            " this.x = 1;" +
            " }" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return this.x;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    fail("Expected an AssertionError.");
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPrototypeMethodDevirtualizationWithExportedFunction() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            " " +
            " export function bar() {" +
            " return this.x;" +
            "}" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return this.x;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    fail("Expected an AssertionError.");
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPrototypeMethodDevirtualizationWithUnknownFunction() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " function qux() {" +
            " return this.x;" +
            "}" +
            " return qux();" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    fail("Expected an AssertionError.");
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPrototypeMethodDevirtualizationWithMultipleDefinitions() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return this.x;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " return 2;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    fail("Expected an AssertionError.");
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testPrototypeMethodDevirtualizationWithVariableDeclaration() {
    String jsCode =
        "function Foo() {" +
            " this.x = 1;" +
            "}" +
            "Foo.prototype.bar = function() {" +
            " var x = 2;" +
            " return this.x;" +
            "}";
    compiler.compile(jsCode, root, null, null);

    DefinitionSite defSite = defFinder.getDefinitionSite("Foo.prototype.bar");
    rewriteDefinitionIfEligible(defSite, defFinder);

    fail("Expected an AssertionError.");
  }

  private void rewriteDefinitionIfEligible(DefinitionSite defSite,
                                          SimpleDefinitionFinder defFinder) {
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizer.rewriteDefinitionIfEligible(defSite, defFinder);
  }
}