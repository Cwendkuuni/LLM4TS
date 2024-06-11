import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.NodeUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private static class TestCompiler extends AbstractCompiler {

    @Override
    public CompilerPass getPass(String passName) {
      if (InlineObjectLiterals.class.getName().equals(passName)) {
        return new InlineObjectLiterals(this, mock(Supplier.class)) {
          @Override
          public void process(Node externs, Node root) {
            // No-op
          }
        };
      }
      return super.getPass(passName);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {
    TestCompiler compiler = new TestCompiler();
    Node root = NodeUtil.parseJsSnippet(
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() {" +
        "      return 2;" +
        "    }," +
        "    c: 3," +
        "    d: function() {" +
        "      return 4;" +
        "    }" +
        "  };" +
        "  obj.a = 5;" +
        "  obj.b();" +
        "  console.log(obj.a, obj.b(), obj.c, obj.d());" +
        "}"
    );

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass(
        InlineObjectLiterals.class.getName());

    // Run the pass
    compiler.process(NodeUtil.emptyNodeList(), root);

    // Check the result
    Node expected = NodeUtil.parseJsSnippet(
        "function foo() {" +
        "  var a = 5;" +
        "  var b = function() {" +
        "    return 2;" +
        "  };" +
        "  var c = 3;" +
        "  var d = function() {" +
        "    return 4;" +
        "  };" +
        "  console.log(a, b(), c, d());" +
        "}"
    );

    assertEquals(expected.getText(), root.getText());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfReference() {
    TestCompiler compiler = new TestCompiler();
    Node root = NodeUtil.parseJsSnippet(
        "function foo() {" +
        "  var obj = {" +
        "    a: function() {" +
        "      return this.b;" +
        "    }," +
        "    b: 1" +
        "  };" +
        "  obj.a();" +
        "}"
    );

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass(
        InlineObjectLiterals.class.getName());

    // Run the pass
    compiler.process(NodeUtil.emptyNodeList(), root);

    // Check the result
    Node expected = NodeUtil.parseJsSnippet(
        "function foo() {" +
        "  var a = function() {" +
        "    return this.b;" +
        "  };" +
        "  var b = 1;" +
        "  a = function() {" +
        "    return b;" +
        "  };" +
        "  console.log(a());" +
        "}"
    );

    assertEquals(expected.getText(), root.getText());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithNonInlineableObjects() {
    TestCompiler compiler = new TestCompiler();
    Node root = NodeUtil.parseJsSnippet(
        "function foo() {" +
        "  var obj = {" +
        "    a: function() {" +
        "      return 1;" +
        "    }," +
        "    b: {" +
        "      c: 1," +
        "      d: function() {" +
        "        return 2;" +
        "      }" +
        "    }" +
        "  };" +
        "  obj.a();" +
        "  obj.b.d();" +
        "}"
    );

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass(
        InlineObjectLiterals.class.getName());

    // Run the pass
    compiler.process(NodeUtil.emptyNodeList(), root);

    // Check the result
    Node expected = NodeUtil.parseJsSnippet(
        "function foo() {" +
        "  function a() {" +
        "    return 1;" +
        "  }" +
        "  var b = {" +
        "    c: 1," +
        "    d: function() {" +
        "      return 2;" +
        "    }" +
        "  };" +
        "  a = function() {" +
        "    return b.d();" +
        "  };" +
        "  a();" +
        "}"
    );

    assertEquals(expected.getText(), root.getText());
  }
}