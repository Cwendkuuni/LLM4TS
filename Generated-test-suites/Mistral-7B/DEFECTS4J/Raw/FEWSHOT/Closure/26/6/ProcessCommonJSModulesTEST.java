package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private CompilerInput input;
  private NodeFactory factory;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    input = mock(CompilerInput.class);
    factory = mock(NodeFactory.class);
    processor = new ProcessCommonJSModules(mock(AbstractCompiler.class), "");
    when(input.getSourceFileName()).thenReturn("test.js");
    when(input.getFileName()).thenReturn("test.js");
    when(input.getNodeFactory()).thenReturn(factory);
  }

  @Test
  public void testProcess() {
    Node root = createNode("function main() {}");
    processor.process(input, root);
    verify(input).addProvide("test$main");
    verify(input).addRequire("test$main");
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("test$main", processor.guessCJSModuleName("test.js"));
    assertEquals("test$subdir$subfile$main", processor.guessCJSModuleName("test/subdir/subfile.js"));
  }

  @Test
  public void testProcessCommonJsModulesCallback_RequireCall() {
    Node requireCall = createNode("require('./subfile')");
    Node root = createNode("function main() { require('./subfile') }");
    Node parent = createNode("function test() { main() }");

    NodeTraversal traversal = new NodeTraversal(processor, parent, null);
    traversal.traverse(root);

    assertEquals("test$require$subfile", root.getChild(0).getString());
    assertFalse(requireCall.getParent().getChildren().contains(requireCall));
    assertTrue(root.getChild(0).getChildren().contains(requireCall));
  }

  @Test
  public void testProcessCommonJsModulesCallback_Script() {
    String scriptSource = "var x = 1; exports.x = x;";
    Node script = createNode(scriptSource);
    Node root = createNode("function main() { }");

    NodeTraversal traversal = new NodeTraversal(processor, root, null);
    traversal.traverse(root);

    assertEquals("test$main$x", script.getChild(0).getString());
    assertEquals("test$main$exports", script.getChild(1).getString());
    assertEquals("test$main", script.getString());
    assertEquals(2, script.getChildCount());
  }

  @Test
  public void testProcessCommonJsModulesCallback_ModuleExports() {
    String scriptSource = "exports.x = 1;";
    Node script = createNode(scriptSource);
    Node root = createNode("function main() { }");

    NodeTraversal traversal = new NodeTraversal(processor, root, null);
    traversal.traverse(root);

    assertEquals("test$main$exports$x", script.getString());
    assertEquals("test$main$exports", script.getChild(0).getString());
    assertEquals(2, script.getChildCount());
  }

  @Test
  public void testProcessCommonJsModulesCallback_RenameVars() {
    String scriptSource = "var x = 1, y = 2;";
    Node script = createNode(scriptSource);
    Node root = createNode("function main() { }");

    NodeTraversal traversal = new NodeTraversal(processor, root, null);
    traversal.traverse(root);

    Node xNode = script.getChild(0);
    Node yNode = script.getChild(1);

    assertEquals("test$main$$x", xNode.getString());
    assertEquals("test$main$$y", yNode.getString());
  }

  @Test
  public void testProcessCommonJsModulesCallback_ModuleExportsOverride() {
    String scriptSource = "exports.x = 1; module.exports = exports;";
    Node script = createNode(scriptSource);
    Node root = createNode("function main() { }");

    NodeTraversal traversal = new NodeTraversal(processor, root, null);
    traversal.traverse(root);

    Node moduleExports = script.getChildAtIndex(1);
    assertEquals("test$main$exports$x", moduleExports.getChild(0).getString());
    assertEquals("test$main$exports", moduleExports.getString());
  }

  private Node createNode(String source) {
    Node node = new Node(Node.SCRIPT);
    node.setSource(source);
    node.setSourceFileName("test.js");
    node.setLine(1);
    node.setColumn(1);
    node.setEndLine(2);
    node.setEndColumn(1);
    return node;
  }
}