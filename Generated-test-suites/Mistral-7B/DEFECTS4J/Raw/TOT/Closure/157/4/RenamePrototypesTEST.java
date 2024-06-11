import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private Compiler compiler;
  private CompilerResult result;

  @Before
  public void setup() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions()
        .setCompilerFlags(ImmutableMap.of(
            "aggressiveRenaming", "true",
            "charactersToExcludeFromRenamedNames", "[]"))
        .setInput("test.js", new SourceFile("test.js"))
        .setOutputFile("output.js")
        .setCompilerLevel(CompilationLevel.SIMPLE_OPTIMIZATIONS));
  }

  @Test
  public void testSimplePropertyRenaming() {
    String input = "function Foo() {} Foo.prototype.x = 1;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node xNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode.getFirstChild().getString());
  }

  @Test
  public void testSimplePropertyRenamingWithExistingName() {
    String input = "function Foo() {} Foo.prototype.x = 1; Foo.prototype.x = 2;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node xNode1 = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode1.getFirstChild().getString());

    Node xNode2 = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    Node xNode2Name = xNode2.getFirstChild();
    assertEquals("x should keep its name if already used", "x", xNode2Name.getString());
  }

  @Test
  public void testSimpleObjLitPropertyRenaming() {
    String input = "function Foo() {} var foo = { x : 1 }; Foo.prototype = foo;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node xNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode.getFirstChild().getString());
  }

  @Test
  public void testAggressivePropertyRenaming() {
    String input = "function Foo() {} Foo.prototype.MyFunction = function() {};";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node myFunctionNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("MyFunction should be renamed", "myFunction_", myFunctionNode.getFirstChild().getString());
  }

  @Test
  public void testNoRenamingForPrivateProperties() {
    String input = "function Foo() {} function Foo.prototype.privateX = 1;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node privateXNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("private properties should not be renamed", "privateX", privateXNode.getFirstChild().getString());
  }

  @Test
  public void testNoRenamingForExternedProperties() {
    String input = "function Foo() {}; String.prototype.foo = function() {};";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);   
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node fooNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("externed properties should not be renamed", "foo", fooNode.getFirstChild().getString());
  }

  @Test
  public void testNoRenamingForObjLitPropertiesWithQuotes() {
    String input = "function Foo() {} var foo = { 'prop1' : 1 }; Foo.prototype = foo;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node prop1Node = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("properties with quotes should not be renamed", "prop1", prop1Node.getFirstChild().getString());
  }

  @Test
  public void testNoRenamingForObjLitPropertiesWithoutQuotes() {
    String input = "function Foo() {} var foo = { prop1 : 1 }; Foo.prototype = foo;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node prop1Node = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("properties without quotes should not be renamed", "prop1", prop1Node.getFirstChild().getString());
  }

  @Test
  public void testRenamingMultiplePropertiesWithSameName() {
    String input = "function Foo() {} Foo.prototype.x = 1; Foo.prototype.x = 2;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node xNode1 = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode1.getFirstChild().getString());

    Node xNode2 = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should keep its name if already used", "x_", xNode2.getFirstChild().getString());
  }

  @Test
  public void testRenamingPrototypeProperties() {
    String input = "function Foo() {}; function Foo.prototype.getX = function() { return this.x; }; Foo.prototype.x = 1;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node getXNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("getX should be renamed", "getX_", getXNode.getFirstChild().getString());

    Node xNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode.getFirstChild().getString());
  }

  @Test
  public void testRenamingPrototypePropertiesWithAggressiveRenaming() {
    String input = "function Foo() {}; function Foo.prototype.getMyFunction = function() { return this.myFunction; }; Foo.prototype.myFunction = 1;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node getMyFunctionNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("getMyFunction should be renamed", "getMyFunction_", getMyFunctionNode.getFirstChild().getString());

    Node myFunctionNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("myFunction should be renamed", "myFunction_", myFunctionNode.getFirstChild().getString());
  }

  @Test
  public void testRenamingObjLitProperties() {
    String input = "function Foo() {} var foo = { x : 1 }; Foo.prototype = foo;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node xNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode.getFirstChild().getString());
  }

  @Test
  public void testRenamingObjLitPropertiesWithAggressiveRenaming() {
    String input = "function Foo() {} var foo = { myFunction : function() {} }; Foo.prototype = foo;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node myFunctionNode = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("myFunction should be renamed", "myFunction_", myFunctionNode.getFirstChild().getString());
  }

  @Test
  public void testRenamingExistingPrototypeProperties() {
    String input = "function Foo() {} Foo.prototype.x = 1; Foo.prototype.x = 2;";
    Node rootNode = compiler.parseWithErrors(input).getTree();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(compiler.getExterns(), rootNode);

    Node xNode1 = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should be renamed", "x_", xNode1.getFirstChild().getString());

    Node xNode2 = NodeTraversal.findDeepestChildOfType(rootNode, GETPROP);
    assertEquals("x should keep its name if already used", "x_", xNode2.getFirstChild().getString());
  }
}