import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.parser.JsdocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions();
    options.checkGlobalThis = true;

    AbstractCompiler compiler = new AbstractCompiler(options);
    CheckerFactory checkerFactory = new CheckerFactory();
    NodeTraversal traversal = new NodeTraversal(compiler, checkerFactory, null);

    Parser parser = new Parser(null);
    Node rootNode = parser.parse("function evil() { this.useful = undefined; }", null, null);
    Node entryNode = parser.parse("(function() { evil(); })();", null, rootNode).getFirstChild();

    traversal.traverse(entryNode, null);

    assertNull(rootNode.getFirstChild().getJSDocInfo()); // Function should not have JSDoc
    assertFalse(rootNode.getFirstChild().getJSDocInfo().isConstructor());
    assertFalse(rootNode.getFirstChild().getJSDocInfo().hasThisType());
    assertFalse(rootNode.getFirstChild().getJSDocInfo().isOverride());

    Node functionNode = rootNode.getFirstChild().getFirstChild();
    assertFalse(functionNode.getJSDocInfo().isConstructor());
    assertFalse(functionNode.getJSDocInfo().hasThisType());
    assertFalse(functionNode.getJSDocInfo().isOverride());

    Node assignmentNode = functionNode.getLastChild();
    Node thisNode = assignmentNode.getFirstChild();
    assertNotNull(thisNode);
    assertFalse(thisNode.getType() == Token.ASSIGN); // This is not an assignment
    assertFalse(thisNode.getLastChild().getString().equals("prototype")); // This is not a prototype property

    traversal.traverse(thisNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test constructor function
    rootNode = parser.parse("function EvilConstructor() { this.useful = undefined; }", null, null);
    functionNode = rootNode.getFirstChild();
    JsdocInfo jsDoc = functionNode.getJSDocInfo();
    assertTrue(jsDoc.isConstructor());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method inside constructor function
    rootNode = parser.parse("function EvilConstructor() { this.constructorFunction = function() { this.useful = undefined; } }", null, rootNode);
    functionNode = rootNode.getFirstChild().getLastChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method with @this annotation
    rootNode = parser.parse("// @this (types: Object) function myFunction() { this.useful = undefined; }", null, null);
    functionNode = rootNode.getFirstChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertTrue(jsDoc.hasThisType());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method with no logical place to put a @this annotation
    rootNode = parser.parse("// @this (types: Object) function myFunction() { var a = this; this.useful = undefined; a.anotherProperty = undefined; }", null, null);
    functionNode = rootNode.getFirstChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method that uses a prototype property
    rootNode = parser.parse("function EvilPrototype() { } EvilPrototype.prototype.myFunction = function() { this.useful = undefined; };", null, null);
    functionNode = rootNode.getFirstChild().getLastChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method that uses a getter property
    rootNode = parser.parse("function EvilPrototype() { get _myFunction() { return function() { this.useful = undefined; } } }", null, null);
    Node getterNode = rootNode.getFirstChild().getLastChild();
    functionNode = getterNode.getLastChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method that is inside a prototype method
    rootNode = parser.parse("function EvilPrototype() { this.prototypeMethod = function() { this.myFunction = function() { this.useful = undefined; } }; }", null, null);
    Node prototypeMethodNode = rootNode.getFirstChild().getLastChild();
    functionNode = prototypeMethodNode.getLastChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method that is inside a constructor annotated function
    rootNode = parser.parse("// @constructor function EvilConstructor() { this.constructorFunction = function() { this.myFunction = function() { this.useful = undefined; } }; }", null, null);
    prototypeMethodNode = rootNode.getFirstChild().getLastChild();
    functionNode = prototypeMethodNode.getLastChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method that is inside a function annotated with @this
    rootNode = parser.parse("// @this (types: Object) function outerFunction() { function innerFunction() { this.useful = undefined; } }", null, null);
    functionNode = rootNode.getFirstChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertTrue(jsDoc.hasThisType());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported

    // Test method that is inside a function where there's no logical place to put a @this annotation
    rootNode = parser.parse("// @this (types: Object) function outerFunction() { var a = this; function innerFunction() { this.useful = undefined; a.anotherProperty = undefined; } }", null, null);
    functionNode = rootNode.getFirstChild().getLastChild();
    jsDoc = functionNode.getJSDocInfo();
    assertFalse(jsDoc.isConstructor());
    assertFalse(jsDoc.hasThisType());
    assertFalse(jsDoc.isOverride());

    traversal = new NodeTraversal(compiler, checkerFactory, null);
    traversal.traverse(functionNode, null);
    assertNull(compiler.getDiagnostics().getDiagnostics()); // No diagnostics should be reported
  }

  private void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError("Expected true but got false");
    }
  }
}