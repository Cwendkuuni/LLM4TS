import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private Compiler compiler = new Compiler();

  @SuppressWarnings("unchecked")
  private RenameLabels renameLabels = new RenameLabels(compiler);

  @SuppressWarnings("unchecked")
  private Node rootNode;

  @SuppressWarnings("unchecked")
  private Node traversalRoot;

  @SuppressWarnings("unchecked")
  private NodeTraversal nodeTraversal;

  @SuppressWarnings("unchecked")
  private Map<String, LabelInfo> labelMap;

  @SuppressWarnings("unchecked")
  private void setup() {
    rootNode = Node.externalCall("null", "main", new ArrayList<Node>());
    traversalRoot = Node.newBlock("labelTest", null);
    labelMap = Maps.newHashMap();
  }

  @SuppressWarnings("unchecked")
  private void teardown() {
    compiler = null;
    renameLabels = null;
    rootNode = null;
    traversalRoot = null;
    nodeTraversal = null;
    labelMap.clear();
  }

  /**
   * Test renaming a label that is not referenced and is at the top level.
   */
  @Test
  public void testRenameUnreferencedTopLevelLabel() {
    setup();

    String labelName = "a";
    Node labelNode = Node.newLabel(labelName, Node.newBlock(labelName, null));
    rootNode.addChild(labelNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName, getNameForId(0));

    teardown();
  }

  /**
   * Test renaming a label that is not referenced and is nested.
   */
  @Test
  public void testRenameUnreferencedNestedLabel() {
    setup();

    String labelName1 = "a";
    Node labelNode1 = Node.newLabel(labelName1, Node.newBlock(labelName1, null));
    Node functionNode = Node.newFunction("test", Node.newBlock("test", null));
    functionNode.addChild(labelNode1);
    rootNode.addChild(functionNode);

    String labelName2 = "b";
    Node labelNode2 = Node.newLabel(labelName2, Node.newBlock(labelName2, null));
    Node nestedFunctionNode = Node.newFunction("nestedTest", Node.newBlock("nestedTest", null));
    nestedFunctionNode.addChild(labelNode2);
    labelNode1.addChild(Node.newCall(Node.newIdentifier("nestedTest"), null));
    functionNode.addChild(nestedFunctionNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName1, getNameForId(1));
    assertEquals(labelName2, getNameForId(2));

    teardown();
  }

  /**
   * Test renaming a label that is referenced and is at the top level.
   */
  @Test
  public void testRenameReferencedTopLevelLabel() {
    setup();

    String labelName = "a";
    Node labelNode = Node.newLabel(labelName, Node.newBlock(labelName, null));
    Node breakNode = Node.newBreak();
    Node breakTarget = Node.newLabel(labelName);
    labelNode.addChild(breakTarget);
    breakNode.addChild(Node.newIdentifier(labelName));
    rootNode.addChild(labelNode);
    rootNode.addChild(breakNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName, getNameForId(0));

    teardown();
  }

  /**
   * Test renaming a label that is referenced and is nested.
   */
  @Test
  public void testRenameReferencedNestedLabel() {
    setup();

    String labelName1 = "a";
    Node labelNode1 = Node.newLabel(labelName1, Node.newBlock(labelName1, null));
    Node functionNode = Node.newFunction("test", Node.newBlock("test", null));
    functionNode.addChild(labelNode1);
    rootNode.addChild(functionNode);

    String labelName2 = "b";
    Node labelNode2 = Node.newLabel(labelName2, Node.newBlock(labelName2, null));
    Node nestedFunctionNode = Node.newFunction("nestedTest", Node.newBlock("nestedTest", null));
    nestedFunctionNode.addChild(labelNode2);
    labelNode1.addChild(Node.newCall(Node.newIdentifier("nestedTest"), null));
    Node breakNode = Node.newBreak();
    Node breakTarget = Node.newLabel(labelName2);
    labelNode2.addChild(breakTarget);
    breakNode.addChild(Node.newIdentifier(labelName2));
    nestedFunctionNode.addChild(breakNode);
    functionNode.addChild(nestedFunctionNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName1, getNameForId(1));
    assertEquals(labelName2, getNameForId(2));

    teardown();
  }

  /**
   * Test renaming a label that is used as a break target and is at the top level.
   */
  @Test
  public void testRenameLabelAsBreakTargetTopLevel() {
    setup();

    String labelName = "a";
    Node labelNode = Node.newLabel(labelName, Node.newBlock(labelName, null));
    Node loopNode = Node.newWhile(Node.newBinaryOp(Token.EQUALS, Node.newIdentifier("i"), Node.newNumber(10)), Node.newBlock("loop", null));
    Node breakNode = Node.newBreak();
    Node breakTarget = Node.newLabel(labelName);
    labelNode.addChild(breakTarget);
    breakNode.addChild(Node.newIdentifier(labelName));
    loopNode.addChild(labelNode);
    loopNode.addChild(breakNode);
    rootNode.addChild(loopNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName, getNameForId(0));

    teardown();
  }

  /**
   * Test renaming a label that is used as a break target and is nested.
   */
  @Test
  public void testRenameLabelAsBreakTargetNested() {
    setup();

    String labelName1 = "a";
    Node labelNode1 = Node.newLabel(labelName1, Node.newBlock(labelName1, null));
    Node functionNode = Node.newFunction("test", Node.newBlock("test", null));
    functionNode.addChild(labelNode1);
    rootNode.addChild(functionNode);

    String labelName2 = "b";
    Node labelNode2 = Node.newLabel(labelName2, Node.newBlock(labelName2, null));
    Node nestedFunctionNode = Node.newFunction("nestedTest", Node.newBlock("nestedTest", null));
    nestedFunctionNode.addChild(labelNode2);
    Node loopNode = Node.newWhile(Node.newBinaryOp(Token.EQUALS, Node.newIdentifier("i"), Node.newNumber(10)), Node.newBlock("loop", null));
    labelNode1.addChild(Node.newCall(Node.newIdentifier("nestedTest"), null));
    Node breakNode = Node.newBreak();
    Node breakTarget = Node.newLabel(labelName2);
    labelNode2.addChild(breakTarget);
    breakNode.addChild(Node.newIdentifier(labelName2));
    nestedFunctionNode.addChild(loopNode);
    nestedFunctionNode.addChild(breakNode);
    functionNode.addChild(nestedFunctionNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName1, getNameForId(1));
    assertEquals(labelName2, getNameForId(2));

    teardown();
  }

  /**
   * Test renaming a label that is used as a continue target and is at the top level.
   */
  @Test
  public void testRenameLabelAsContinueTargetTopLevel() {
    setup();

    String labelName = "a";
    Node labelNode = Node.newLabel(labelName, Node.newBlock(labelName, null));
    Node loopNode = Node.newWhile(Node.newBinaryOp(Token.LESS, Node.newIdentifier("i"), Node.newNumber(5)), Node.newBlock("loop", null));
    Node continueNode = Node.newContinue();
    Node continueTarget = Node.newLabel(labelName);
    labelNode.addChild(continueTarget);
    continueNode.addChild(Node.newIdentifier(labelName));
    loopNode.addChild(labelNode);
    loopNode.addChild(continueNode);
    rootNode.addChild(loopNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName, getNameForId(0));

    teardown();
  }

  /**
   * Test renaming a label that is used as a continue target and is nested.
   */
  @Test
  public void testRenameLabelAsContinueTargetNested() {
    setup();

    String labelName1 = "a";
    Node labelNode1 = Node.newLabel(labelName1, Node.newBlock(labelName1, null));
    Node functionNode = Node.newFunction("test", Node.newBlock("test", null));
    functionNode.addChild(labelNode1);
    rootNode.addChild(functionNode);

    String labelName2 = "b";
    Node labelNode2 = Node.newLabel(labelName2, Node.newBlock(labelName2, null));
    Node nestedFunctionNode = Node.newFunction("nestedTest", Node.newBlock("nestedTest", null));
    nestedFunctionNode.addChild(labelNode2);
    Node loopNode = Node.newWhile(Node.newBinaryOp(Token.LESS, Node.newIdentifier("i"), Node.newNumber(5)), Node.newBlock("loop", null));
    labelNode1.addChild(Node.newCall(Node.newIdentifier("nestedTest"), null));
    Node continueNode = Node.newContinue();
    Node continueTarget = Node.newLabel(labelName2);
    labelNode2.addChild(continueTarget);
    continueNode.addChild(Node.newIdentifier(labelName2));
    nestedFunctionNode.addChild(loopNode);
    nestedFunctionNode.addChild(continueNode);
    functionNode.addChild(nestedFunctionNode);

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName1, getNameForId(1));
    assertEquals(labelName2, getNameForId(2));

    teardown();
  }

  /**
   * Test renaming a label that is used as a break target and a continue target.
   */
  @Test
  public void testRenameLabelUsedAsBreakAndContinue() {
    setup();

    String labelName = "a";
    Node labelNode = Node.newLabel(labelName, Node.newBlock(labelName, null));
    Node loopNode = Node.newWhile(Node.newBinaryOp(Token.LESS, Node.newIdentifier("i"), Node.newNumber(5)), Node.newBlock("loop", null));
    Node breakNode = Node.newBreak();
    Node breakTarget = Node.newLabel(labelName);
    labelNode.addChild(breakTarget);
    breakNode.addChild(Node.newIdentifier(labelName));
    loopNode.addChild(labelNode);
    loopNode.addChild(breakNode);

    Node functionNode = Node.newFunction("function", Node.newBlock("function", null));
    Node continueNode = Node.newContinue();
    Node continueTarget = Node.newLabel(labelName);
    labelNode.addChild(continueTarget);
    continueNode.addChild(Node.newIdentifier(labelName));
    functionNode.addChild(Node.newCall(Node.newIdentifier("function"), null));
    functionNode.addChild(Node.newBlock(Node.newIdentifier("loop"), null));
    functionNode.addChild(loopNode);
    functionNode.addChild(continueNode);
    rootNode.addChild(Node.newCall(Node.newIdentifier("function"), null));

    renameLabels.process(compiler.getExterns(), rootNode);

    assertEquals(labelName, getNameForId(0));

    teardown();
  }

  private String getNameForId(int id) {
    Node labelNode = traversalRoot;
    for (int i = 0; i < id; i++) {
      labelNode = labelNode.getFirstChild();
    }
    return labelNode.getFirstChild().getString();
  }

  @Before
  public void setUp() {
    setup();
    nodeTraversal = new NodeTraversal(traversalRoot, compiler);
  }

  @After
  public void tearDown() {
    teardown();
    NodeUtil.dispose(traversalRoot);
  }
}