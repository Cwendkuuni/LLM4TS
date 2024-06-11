import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private static <T> T createCompiler() {
    return (T) new AbstractCompiler() {
      @Override
      public CompilerPass getPass(Class<? extends CompilerPass> pass) {
        return new RenameLabels(this);
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static <T> NodeTraversal createNodeTraversal(T compiler) {
    return new NodeTraversal(compiler);
  }

  private static void testRenameLabels(Node root) {
    AbstractCompiler compiler = createCompiler();
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);

    compiler.process(null, root);
    NodeTraversal.traverse(compiler, root, new ProcessLabels());

    assertEquals("labels renamed correctly", 7, nodeTraversal.getRootNode().getChildCount());

    Node expected = Node.newBlock(null, 0, new ArrayList<Node>() {
      {
        add(Node.newLabel("a"));
        add(Node.newLabel("b"));
        add(Node.newLabel("c"));
        add(Node.newLabel("d"));
        add(Node.newLabel("e"));
        add(Node.newLabel("f"));
        add(Node.newLabel("g"));
      }
    });

    Node actual = NodeUtil.getChild(NodeUtil.getChild(root, 0), 0);
    NodeUtil.assertEqualNodeOrdering(expected, actual);
  }

  @Test
  public void testSimpleLabels() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("f", null, new ArrayList<Node>() {{
        add(Node.newLabel("a"));
        add(Node.newLabel("b"));
        add(Node.newLabel("c"));
      }}));
      add(Node.newFunction("g", null, new ArrayList<Node>() {{
        add(Node.newLabel("d"));
        add(Node.newLabel("e"));
        add(Node.newLabel("f"));
      }}));
    }});
    testRenameLabels(root);
  }

  @Test
  public void testNestedLabels() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("f", null, new ArrayList<Node>() {{
        add(Node.newLabel("a"));
        add(Node.newBlock(null, 0, new ArrayList<Node>() {{
          add(Node.newLabel("b"));
          add(Node.newLabel("c"));
        }}));
      }}));
      add(Node.newFunction("g", null, new ArrayList<Node>() {{
        add(Node.newLabel("d"));
        add(Node.newBlock(null, 0, new ArrayList<Node>() {{
          add(Node.newLabel("e"));
          add(Node.newLabel("f"));
          add(Node.newBlock(null, 0, new ArrayList<Node>() {{
            add(Node.newLabel("g"));
          }}));
        }}));
      }}));
    }});
    testRenameLabels(root);
  }

  @Test
  public void testRemoveUnusedLabels() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("f", null, new ArrayList<Node>() {{
        add(Node.newLabel("a"));
        add(Node.newLabel("b"));
        add(Node.newLabel("c"));
      }}));
      add(Node.newFunction("g", null, new ArrayList<Node>() {{
        add(Node.newLabel("d"));
        add(Node.newLabel("e"));
        add(Node.newLabel("f"));
        add(Node.newExpression("", Node.TO_STRING, Node.newIdentifier("x")));
      }}));
    }});
    RenameLabels renameLabels = new RenameLabels(createCompiler());
    renameLabels.removeUnused = true;
    NodeTraversal nodeTraversal = createNodeTraversal(renameLabels.compiler);

    renameLabels.process(null, root);
    NodeTraversal.traverse(renameLabels.compiler, root, new ProcessLabels());

    assertEquals("labels renamed correctly", 5, nodeTraversal.getRootNode().getChildCount());

    Node expected = Node.newBlock(null, 0, new ArrayList<Node>() {{
      add(Node.newLabel("a"));
      add(Node.newLabel("b"));
      add(Node.newLabel("d"));
      add(Node.newLabel("e"));
      add(Node.newLabel("x"));
    }});

    Node actual = NodeUtil.getChild(NodeUtil.getChild(root, 0), 0);
    NodeUtil.assertEqualNodeOrdering(expected, actual);
  }

  @Test
  public void testNestedLabelsWithUnused() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("f", null, new ArrayList<Node>() {{
        add(Node.newLabel("a"));
        add(Node.newBlock(null, 0, new ArrayList<Node>() {{
          add(Node.newLabel("b"));
          add(Node.newLabel("c"));
        }}));
      }}));
      add(Node.newFunction("g", null, new ArrayList<Node>() {{
        add(Node.newLabel("d"));
        add(Node.newBlock(null, 0, new ArrayList<Node>() {{
          add(Node.newLabel("e"));
          add(Node.newLabel("f"));
          add(Node.newBlock(null, 0, new ArrayList<Node>() {{
            add(Node.newLabel("g"));
            add(Node.newExpression("", Node.TO_STRING, Node.newIdentifier("x")));
          }}));
        }}));
      }}));
    }});
    RenameLabels renameLabels = new RenameLabels(createCompiler());
    renameLabels.removeUnused = true;
    NodeTraversal nodeTraversal = createNodeTraversal(renameLabels.compiler);

    renameLabels.process(null, root);
    NodeTraversal.traverse(renameLabels.compiler, root, new ProcessLabels());

    assertEquals("labels renamed correctly", 5, nodeTraversal.getRootNode().getChildCount());

    Node expected = Node.newBlock(null, 0, new ArrayList<Node>() {{
      add(Node.newLabel("a"));
      add(Node.newLabel("b"));
      add(Node.newLabel("d"));
      add(Node.newLabel("e"));
      add(Node.newLabel("x"));
    }});

    Node actual = NodeUtil.getChild(NodeUtil.getChild(root, 0), 0);
    NodeUtil.assertEqualNodeOrdering(expected, actual);
  }

  @Test
  public void testLabelsWithKeyword() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("f", null, new ArrayList<Node>() {{
        add(Node.newLabel("break"));
        add(Node.newLabel("case"));
        add(Node.newLabel("continue"));
      }}));
    }});
    testRenameLabels(root);
  }

  @Test(expected = RuntimeException.class)
  public void testLabelsWithDuplicateName() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("f", null, new ArrayList<Node>() {{
        add(Node.newLabel("a"));
        add(Node.newLabel("a"));
      }}));
    }});
    testRenameLabels(root);
  }

  @Test
  public void testLabelsWithDuplicateNameInSubScope() {
    Node root = Node.newProgram(null, new ArrayList<Node>() {{
      add(Node.newFunction("outer", null, new ArrayList<Node>() {{
        add(Node.newLabel("a"));
        add(Node.newBlock(null, 0, new ArrayList<Node>() {{
          add(Node.newLabel("inner"));
          add(Node.newLabel("a"));
        }}));
      }}));
    }});
    testRenameLabels(root);
  }
}