import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
  }

  @Test
  public void testInlineObjectLiterals_Basic() {
    Node root = Node.objectLiteral(
        "foo", Node.newNumber(1),
        "bar", Node.newNumber(2)
    );

    Scope scope = new Scope();
    scope.declareVar("x", Node.SCOPE_VAR);

    inlineObjectLiterals.process(null, root);

    List<Node> children = scope.getVars().get(0).getChildren();
    assertEquals(4, children.size());
    for (Node node : children) {
      if (node.isVar()) {
        String name = node.getString();
        if (name.startsWith("JSCompiler_object_inline_")) {
          Node value = node.getFirstChild();
          switch (name) {
            case "JSCompiler_object_inline_foo":
              assertEquals("1", value.getString());
              break;
            case "JSCompiler_object_inline_bar":
              assertEquals("2", value.getString());
              break;
            default:
              fail("Unexpected variable: " + name);
          }
        } else if (node.isAssign()) {
          Scope childScope = node.getScope();
          Var childVar = childScope.getVar(node.getFirstChild().getString());
          assertEquals(name, childVar.name);
          assertEquals("JSCompiler_object_inline_" + childVar.name,
              childVar.nameId);
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiterals_ComplexObject() {
    Node root = Node.objectLiteral(
        "foo", Node.objectLiteral(
            "a", Node.newNumber(1),
            "b", Node.objectLiteral(
                "c", Node.newNumber(2),
                "d", Node.newNumber(3)
            )
        ),
        "bar", Node.newNumber(2)
    );

    Scope scope = new Scope();
    inlineObjectLiterals.process(null, root);

    List<Node> children = scope.getVars().get(0).getChildren();
    assertEquals(8, children.size());
    for (Node node : children) {
      if (node.isVar()) {
        String name = node.getString();
        Node value = node.getFirstChild();
        switch (name) {
          case "JSCompiler_object_inline_foo_a":
            assertEquals("1", value.getString());
            break;
          case "JSCompiler_object_inline_foo_b_c":
            assertEquals("2", value.getString());
            break;
          case "JSCompiler_object_inline_foo_b_d":
            assertEquals("3", value.getString());
            break;
          case "JSCompiler_object_inline_foo":
            assertTrue(value.isObjectLit());
            List<Node> grandchildren = value.getFirstChild().getChildren();
            assertEquals(3, grandchildren.size());
            for (Node child : grandchildren) {
              if (child.isVar()) {
                String childName = child.getString();
                Node childValue = child.getFirstChild();
                switch (childName) {
                  case "JSCompiler_object_inline_foo_b_c":
                    assertEquals("2", childValue.getString());
                    break;
                  case "JSCompiler_object_inline_foo_b_d":
                    assertEquals("3", childValue.getString());
                    break;
                  default:
                    fail("Unexpected variable: " + childName);
                }
              }
            }
            break;
          case "JSCompiler_object_inline_bar":
            assertEquals("2", value.getString());
            break;
          default:
            fail("Unexpected variable: " + name);
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiterals_InlineObjectInAssignment() {
    Node root = Node.assign(
        Node.name("x"),
        Node.objectLiteral(
            "foo", Node.newNumber(1),
            "bar", Node.newNumber(2)
        )
    );

    Scope scope = new Scope();
    inlineObjectLiterals.process(null, root);

    List<Node> children = scope.getVars().get(0).getChildren();
    assertEquals(4, children.size());
    for (Node node : children) {
      if (node.isVar()) {
        String name = node.getString();
        Node value = node.getFirstChild();
        switch (name) {
          case "JSCompiler_object_inline_foo":
            assertEquals("1", value.getString());
            break;
          case "JSCompiler_object_inline_bar":
            assertEquals("2", value.getString());
            break;
          case "x":
            assertTrue(value.isObjectLit());
            List<Node> grandchildren = value.getFirstChild().getChildren();
            assertEquals(2, grandchildren.size());
            for (Node child : grandchildren) {
              if (child.isVar()) {
                String childName = child.getString();
                Node childValue = child.getFirstChild();
                switch (childName) {
                  case "JSCompiler_object_inline_foo":
                    assertEquals("1", childValue.getString());
                    break;
                  case "JSCompiler_object_inline_bar":
                    assertEquals("2", childValue.getString());
                    break;
                  default:
                    fail("Unexpected variable: " + childName);
                }
              }
            }
            break;
          default:
            fail("Unexpected variable: " + name);
        }
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_CircularReference() {
    Node root = Node.objectLiteral(
        "foo", Node.objectLiteral(
            "bar", Node.name("foo")
        )
    );

    inlineObjectLiterals.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_SelfAssignment() {
    Node root = Node.assign(
        Node.name("x"),
        Node.objectLiteral(
            "foo", Node.assign(Node.name("foo"), Node.name("x"))
        )
    );

    inlineObjectLiterals.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_ObjectWithUnsupportedType() {
    Node root = Node.objectLiteral(
        "foo", Node.booleanLiteral(true)
    );

    inlineObjectLiterals.process(null, root);
  }

  @Test
  public void testInlineObjectLiterals_PassThrough() {
    Node root = Node.objectLiteral(
        "foo", Node.newNumber(1)
    );

    CompilerPassManager manager = new CompilerPassManager();
    manager.addPass(inlineObjectLiterals);
    manager.runPasses(compiler, root);

    List<Node> children = scope.getVars().get(0).getChildren();
    assertEquals(2, children.size());
    for (Node node : children) {
      if (node.isVar()) {
        String name = node.getString();
        Node value = node.getFirstChild();
        switch (name) {
          case "JSCompiler_object_inline_foo":
            assertEquals("1", value.getString());
            break;
          case "foo":
            assertTrue(value.isObjectLit());
            break;
          default:
            fail("Unexpected variable: " + name);
        }
      }
    }
  }
}