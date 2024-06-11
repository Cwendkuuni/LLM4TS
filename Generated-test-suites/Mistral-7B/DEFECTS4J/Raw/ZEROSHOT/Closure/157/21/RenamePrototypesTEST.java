import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  private Compiler compiler;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    compiler = new Compiler(null, null, null, null, null, null, null);
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
  }

  @Test
  public void testSimpleRename() {
    String code = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    Node root = input.ast;

    renamePrototypes.process(input.externs, root);
    assertEquals("function Foo() {} Foo.prototype.foo_bar = function() {};",
                 input.getSource());
  }

  @Test
  public void testAggressiveRename() {
    String code = "function Foo() {} Foo.prototype.fooBar = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.foo_fooBar = function() {};", input.getSource());
  }

  @Test
  public void testNoRename() {
    String code = "function Foo() {} Foo.prototype.toString = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.toString = function() {};", input.getSource());
  }

  @Test
  public void testPrivateRename() {
    String code = "function Foo() {} Foo.prototype._bar = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype._bar = function() {};", input.getSource());
  }

  @Test
  public void testReservedRename() {
    String code = "function Foo() {} Foo.prototype.indexOf = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.indexOf = function() {};", input.getSource());
  }

  @Test
  public void testNoRenameWithObjLit() {
    String code = "function Foo() {} Foo.prototype = { getBar : function() {} };";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype = { getBar : function() {} };", input.getSource());
  }

  @Test
  public void testRenameWithObjLit() {
    String code = "function Foo() {} Foo.prototype = { fooBar : function() {} };";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype = { foo_fooBar : function() {} };", input.getSource());
  }

  @Test
  public void testRenameWithObjLitAndPrototype() {
    String code = "function Foo() {} Foo.prototype = { fooBar : function() {} }; Foo.prototype.bar = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype = { foo_fooBar : function() {} }; Foo.prototype.bar = function() {};", input.getSource());
  }

  @Test
  public void testRenameWithStringNodes() {
    String code = "function Foo() {} Foo.prototype.bar = function() { return 'test'; };";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    Node root = input.ast;

    renamePrototypes.process(input.externs, root);
    assertEquals("function Foo() {} Foo.prototype.foo_bar = function() { return 'test'; };", input.getSource());

    Node bar = findNode(root, "Foo.prototype.foo_bar");
    assertEquals("function() { return 'test'; }", bar.getSource());
  }

  @Test
  public void testRenameWithPrototypeAccess() {
    String code = "function Foo() {} Foo.prototype.getBar = function() { return this.bar; }; Foo.prototype.bar = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.getBar = function() { return this.foo_bar; }; Foo.prototype.foo_bar = function() {};", input.getSource());
  }

  @Test
  public void testRenameWithPrototypeAccessAndStringNodes() {
    String code = "function Foo() {} Foo.prototype.getBar = function() { return this.bar; }; Foo.prototype.bar = function() { return 'test'; };";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.getBar = function() { return this.foo_bar; }; Foo.prototype.foo_bar = function() { return 'test'; };", input.getSource());

    Node getBar = findNode(input.ast, "Foo.prototype.getBar");
    assertEquals("function() { return this.foo_bar; }", getBar.getSource());

    Node fooBar = findNode(input.ast, "Foo.prototype.foo_bar");
    assertEquals("function() { return 'test'; }", fooBar.getSource());
  }

  @Test
  public void testRenameWithPrototypeAndPrototypeAccess() {
    String code = "function Foo() {} Foo.prototype.getBar = function() { return this.bar; }; Foo.prototype.bar = function() { return this.fooBar; }; Foo.prototype.fooBar = function() {};";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.getBar = function() { return this.foo_fooBar; }; Foo.prototype.foo_fooBar = function() { return this.foo_fooBar; };", input.getSource());
  }

  @Test
  public void testRenameWithPrototypeAndPrototypeAccessAndStringNodes() {
    String code = "function Foo() {} Foo.prototype.getBar = function() { return this.bar; }; Foo.prototype.bar = function() { return this.fooBar; }; Foo.prototype.fooBar = function() { return 'test'; };";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("function Foo() {} Foo.prototype.getBar = function() { return this.foo_fooBar; }; Foo.prototype.foo_fooBar = function() { return 'test'; };", input.getSource());

    Node getBar = findNode(input.ast, "Foo.prototype.getBar");
    assertEquals("function() { return this.foo_fooBar; }", getBar.getSource());

    Node fooBar = findNode(input.ast, "Foo.prototype.foo_fooBar");
    assertEquals("function() { return 'test'; }", fooBar.getSource());
  }

  @Test
  public void testRenameWithPrototypeAndPrototypeAccessAndStringNodesAndExterns() {
    String code = "String.prototype.foo = function() {}; Foo.prototype.getBar = function() { return this.bar; }; Foo.prototype.bar = function() { return this.fooBar; }; Foo.prototype.fooBar = function() { return 'test'; };";
    CompilerInput input = new CompilerInput(new TokenStream(new NodeFactory(), new TokenStreamReader(code)), code);

    renamePrototypes.process(input.externs, input.ast);
    assertEquals("String.prototype.foo = function() {}; Foo.prototype.getBar = function() { return this.foo_fooBar; }; Foo.prototype.foo_fooBar = function() { return 'test'; };", input.getSource());

    Node getBar = findNode(input.ast, "Foo.prototype.getBar");
    assertEquals("function() { return this.foo_fooBar; }", getBar.getSource());

    Node fooBar = findNode(input.ast, "Foo.prototype.foo_fooBar");
    assertEquals("function() { return 'test'; }", fooBar.getSource());

    Node foo = findNode(input.ast, "String.prototype.foo");
    assertEquals("function() {}", foo.getSource());
  }

  private Node findNode(Node node, String name) {
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    Node result = null;
    traversal.traverse(node, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getSource().equals(name)) {
          result = n;
        }
      }
    });
    return result;
  }
}