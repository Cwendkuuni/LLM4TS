package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = new DummyAbstractCompiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(null, "test.js", null);
    Node root = NodeFactory.createScriptlet(null, null);

    // Test empty file
    processor.process(input, root);
    assertNotNull(processor.getModule());
    assertEquals(0, processor.getModule().getRequireCount());

    // Test require call
    Node requireNode = NodeFactory.createCall(NodeFactory.createName("require"),
        NodeFactory.createString("module"));
    root.addChildToFront(requireNode);

    input.setSourceFileName("test.js");
    processor.process(input, root);
    assertNotNull(processor.getModule());
    assertEquals(1, processor.getModule().getRequireCount());
    assertEquals("module", processor.getModule().getRequire(0).getName());

    // Test multiple require calls
    Node requireNode2 = NodeFactory.createCall(NodeFactory.createName("require"),
        NodeFactory.createString("module2"));
    root.addChildToFront(requireNode2);

    input.setSourceFileName("test.js");
    processor.process(input, root);
    assertNotNull(processor.getModule());
    assertEquals(2, processor.getModule().getRequireCount());
    assertEquals("module", processor.getModule().getRequire(0).getName());
    assertEquals("module2", processor.getModule().getRequire(1).getName());

    // Test require with relative path
    Node requireNodeRel = NodeFactory.createCall(NodeFactory.createName("require"),
        NodeFactory.createString("./module"));
    root.addChildToFront(requireNodeRel);

    String sourceName = "test.js";
    input.setSourceFileName(sourceName);
    processor.process(input, root);
    assertNotNull(processor.getModule());
    assertEquals(3, processor.getModule().getRequireCount());
    assertEquals("module", processor.getModule().getRequire(0).getName());
    assertEquals("./module", processor.getModule().getRequire(1).getName());
    assertEquals("module2", processor.getModule().getRequire(2).getName());

    // Test require with full path
    Node requireNodeAbs = NodeFactory.createCall(NodeFactory.createName("require"),
        NodeFactory.createString("/path/to/module"));
    root.addChildToFront(requireNodeAbs);

    String requiredSourceName = "/path/to/module.js";
    input.setSourceFileName(sourceName);
    processor.process(input, root);
    assertNotNull(processor.getModule());
    assertEquals(4, processor.getModule().getRequireCount());
    assertEquals("module", processor.getModule().getRequire(0).getName());
    assertEquals("./module", processor.getModule().getRequire(1).getName());
    assertEquals("module2", processor.getModule().getRequire(2).getName());
    assertEquals("/path/to/module", processor.getModule().getRequire(3).getName());

    // Test require with invalid path
    Node requireNodeInvalid = NodeFactory.createCall(NodeFactory.createName("require"),
        NodeFactory.createString("./invalid"));
    root.addChildToFront(requireNodeInvalid);

    input.setSourceFileName(sourceName);
    try {
      processor.process(input, root);
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // Expected
    }
    assertNull(processor.getModule());
  }
}

class DummyAbstractCompiler implements AbstractCompiler {
  @Override
  public NodeTraversal createTraverser(Node node, Node parent) {
    return null;
  }

  @Override
  public void reportCodeChange() {
    // No-op
  }
}