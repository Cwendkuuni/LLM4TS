import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private static final String CLASS_NAME = "com.google.javascript.jscomp.RenameLabels";
  private static final String TEST_CLASS_NAME = "RenameLabelsTest";

  @org.junit.Before
  public void setUp() {
    CompilerPassManager manager = new CompilerPassManager();
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);
    manager.addPass(renameLabels);
  }

  @org.junit.Test
  public void testSimpleRename() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node labelNode = NodeFactory.createLabelNode("label");
    NodeFactory nodeFactory = NodeFactory.getFactory();
    Node root = nodeFactory.createBlock(labelNode);

    renameLabels.process(NodeUtil.emptyMap(), root);

    LabelInfo labelInfo = getLabelInfo("label");
    assertEquals(1, labelInfo.id);
    assertEquals("a", labelInfo.name);

    Node visitedLabelNode = root.getFirstChild();
    assertEquals("a", visitedLabelNode.getString());
  }

  @org.junit.Test
  public void testDeepRename() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node labelNode1 = NodeFactory.createLabelNode("label1");
    Node labelNode2 = NodeFactory.createLabelNode("label2");
    Node labelNode3 = NodeFactory.createLabelNode("label3");

    Node root = NodeFactory.createBlock(
        NodeFactory.createBlock(
            NodeFactory.createBlock(
                NodeFactory.createLabelStatement(labelNode1),
                NodeFactory.createStatement(NodeFactory.createAssignment(
                    NodeFactory.createVariable("x"),
                    NodeFactory.createStringLiteral("hello")
                )),
                NodeFactory.createLabelStatement(labelNode2),
                NodeFactory.createStatement(NodeFactory.createAssignment(
                    NodeFactory.createVariable("y"),
                    NodeFactory.createStringLiteral("world")
                )),
                NodeFactory.createLabelStatement(labelNode3)
            )
        )
    );

    renameLabels.process(NodeUtil.emptyMap(), root);

    LabelInfo labelInfo1 = getLabelInfo("label1");
    assertEquals(1, labelInfo1.id);
    assertEquals("a", labelInfo1.name);

    LabelInfo labelInfo2 = getLabelInfo("label2");
    assertEquals(2, labelInfo2.id);
    assertEquals("b", labelInfo2.name);

    LabelInfo labelInfo3 = getLabelInfo("label3");
    assertEquals(3, labelInfo3.id);
    assertEquals("c", labelInfo3.name);

    Node visitedLabelNode1 = root.getFirstChild().getLastChild();
    assertEquals("a", visitedLabelNode1.getString());

    Node visitedLabelNode2 = root.getFirstChild().getLastChild().getLastChild();
    assertEquals("b", visitedLabelNode2.getString());

    Node visitedLabelNode3 = root.getFirstChild().getLastChild().getLastChild().getLastChild();
    assertEquals("c", visitedLabelNode3.getString());
  }

  @org.junit.Test
  public void testRemoveUnusedLabel() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler, new RemoveUnusedLabelSupplier(), true);

    Node labelNode1 = NodeFactory.createLabelNode("label1");
    Node labelNode2 = NodeFactory.createLabelNode("label2");
    Node root = NodeFactory.createBlock(
        NodeFactory.createLabelStatement(labelNode1),
        NodeFactory.createStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("x"),
            NodeFactory.createStringLiteral("hello")
        ))
    );

    renameLabels.process(NodeUtil.emptyMap(), root);

    LabelInfo labelInfo1 = getLabelInfo("label1");
    assertEquals(1, labelInfo1.id);
    assertEquals("a", labelInfo1.name);

    Node visitedLabelNode1 = root.getFirstChild();
    assertEquals("a", visitedLabelNode1.getString());

    LabelInfo labelInfo2 = getLabelInfo("label2");
    assertNull(labelInfo2);

    assertEquals(1, root.getChildCount());
  }

  @org.junit.Test
  public void testDontRemoveUnusedLabel() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler, new RemoveUnusedLabelSupplier(), false);

    Node labelNode1 = NodeFactory.createLabelNode("label1");
    Node labelNode2 = NodeFactory.createLabelNode("label2");
    Node root = NodeFactory.createBlock(
        NodeFactory.createLabelStatement(labelNode1),
        NodeFactory.createStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("x"),
            NodeFactory.createStringLiteral("hello")
        )),
        NodeFactory.createLabelStatement(labelNode2)
    );

    renameLabels.process(NodeUtil.emptyMap(), root);

    LabelInfo labelInfo1 = getLabelInfo("label1");
    assertEquals(1, labelInfo1.id);
    assertEquals("a", labelInfo1.name);

    LabelInfo labelInfo2 = getLabelInfo("label2");
    assertEquals(2, labelInfo2.id);
    assertEquals("b", labelInfo2.name);

    Node visitedLabelNode1 = root.getFirstChild();
    assertEquals("a", visitedLabelNode1.getString());

    Node visitedLabelNode2 = root.getFirstChild().getLastChild();
    assertEquals("b", visitedLabelNode2.getString());

    assertEquals(3, root.getChildCount());
  }

  private LabelInfo getLabelInfo(String name) {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node root = NodeFactory.createBlock(NodeFactory.createLabelStatement(
        NodeFactory.createLabelNode(name)
    ));
    when(compiler.createNodeTraversal(TEST_CLASS_NAME, root)).thenReturn(nodeTraversal);
    when(nodeTraversal.getRoot()).thenReturn(root);
    renameLabels.process(NodeUtil.emptyMap(), root);

    Map<String, LabelInfo> renameMap = ((LabelNamespace) namespaceStack.peek()).renameMap;
    return renameMap.get(name);
  }

  private static class RemoveUnusedLabelSupplier implements Supplier<String> {
    @Override
    public String get() {
      return new DefaultNameSupplier().get();
    }
  }
}