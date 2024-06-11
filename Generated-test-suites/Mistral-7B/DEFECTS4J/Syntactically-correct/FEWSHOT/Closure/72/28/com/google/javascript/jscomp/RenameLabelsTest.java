package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenameLabelsTest {
  @Test
  public void testProcessLabelsEnterAndExitScope() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test entering a new namespace.
    processLabels.enterScope(new NodeTraversal() {});
    assertTrue(processLabels.namespaceStack.size() == 1);

    // Test exiting a namespace.
    processLabels.exitScope(new NodeTraversal() {});
    assertTrue(processLabels.namespaceStack.size() == 0);

    // Test entering and exiting multiple namespaces.
    processLabels.enterScope(new NodeTraversal() {});
    processLabels.enterScope(new NodeTraversal() {});
    assertTrue(processLabels.namespaceStack.size() == 2);
    processLabels.exitScope(new NodeTraversal() {});
    processLabels.exitScope(new NodeTraversal() {});
    assertTrue(processLabels.namespaceStack.size() == 0);
  }

  @Test
  public void testProcessLabelsShouldTraverseLabelNode() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test creating a new label name.
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label1"); }});
      }
    }, new Node() {{ /* empty */}});
    assertTrue(processLabels.names.size() == 1);
    assertEquals("a", processLabels.names.get(0));

    // Test creating multiple label names.
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label2"); }});
      }
    }, new Node() {{ /* empty */}});
    assertTrue(processLabels.names.size() == 2);
    assertEquals("a", processLabels.names.get(0));
    assertEquals("b", processLabels.names.get(1));
  }

  @Test
  public void testProcessLabelsGetNameForId() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test getting the name for a label with an id of 1.
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label1"); }});
      }
    }, new Node() {{ /* empty */}});
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label2"); }});
      }
    }, new Node() {{ /* empty */}});
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label3"); }});
      }
    }, new Node() {{ /* empty */}});
    assertEquals("a", processLabels.getNameForId(1));
    assertEquals("b", processLabels.getNameForId(2));
    assertEquals("c", processLabels.getNameForId(3));
  }

  @Test
  public void testProcessLabelsGetLabelInfo() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test getting label info for a label with name "label1".
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label1"); }});
      }
    }, new Node() {{ /* empty */}});
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label2"); }});
      }
    }, new Node() {{ /* empty */}});
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label3"); }});
      }
    }, new Node() {{ /* empty */}});
    LabelInfo labelInfo1 = processLabels.getLabelInfo("label1");
    LabelInfo labelInfo2 = processLabels.getLabelInfo("label2");
    LabelInfo labelInfo3 = processLabels.getLabelInfo("label3");

    // Test that label info exists for all labels.
    assertNotNull(labelInfo1);
    assertNotNull(labelInfo2);
    assertNotNull(labelInfo3);

    // Test that label info has the correct id.
    assertEquals(1, labelInfo1.id);
    assertEquals(2, labelInfo2.id);
    assertEquals(3, labelInfo3.id);

    // Test that label info is not referenced initially.
    assertFalse(labelInfo1.referenced);
    assertFalse(labelInfo2.referenced);
    assertFalse(labelInfo3.referenced);
  }

  @Test
  public void testProcessLabelsVisitLabel() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test renaming a label that is referenced.
    Node labelNode = new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label1"); }});
      }
    };
    Node labelParent = new Node() {{ /* empty */}};
    processLabels.visit(new NodeTraversal() {/* unused */}, labelNode, labelParent);
    assertEquals("a", labelNode.getFirstChild().getString());

    // Test renaming a label that is not referenced.
    Node unreferencedLabelNode = new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label4"); }});
      }
    };
    Node unreferencedLabelParent = new Node() {{ /* empty */}};
    processLabels.visit(new NodeTraversal() {/* unused */}, unreferencedLabelNode,
        unreferencedLabelParent);
    assertNull(unreferencedLabelNode.getFirstChild());
  }

  @Test
  public void testProcessLabelsVisitBreakOrContinue() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test renaming a named break.
    Node namedBreakNode = new Node() {
      {
        setType(Token.BREAK);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label1"); }});
      }
    };
    Node namedBreakParent = new Node() {{ /* empty */}};
    processLabels.visitBreakOrContinue(namedBreakNode);
    assertEquals("a", namedBreakNode.getFirstChild().getString());

    // Test renaming an unnamed break.
    Node unnamedBreakNode = new Node() {
      {
        setType(Token.BREAK);
      }
    };
    Node unnamedBreakParent = new Node() {{ /* empty */}};
    processLabels.visitBreakOrContinue(unnamedBreakNode);
    assertNull(unnamedBreakNode.getFirstChild());

    // Test renaming a named continue.
    Node namedContinueNode = new Node() {
      {
        setType(Token.CONTINUE);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label2"); }});
      }
    };
    Node namedContinueParent = new Node() {{ /* empty */}};
    processLabels.visitBreakOrContinue(namedContinueNode);
    assertEquals("b", namedContinueNode.getFirstChild().getString());
  }

  @Test
  public void testProcessLabelsRemoveUnusedLabel() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler() {});
    ProcessLabels processLabels = new ProcessLabels();

    // Test removing a label that is not referenced.
    Node unreferencedLabelNode = new Node() {
      {
        setType(Token.LABEL);
        setFirstChild(new Node() {{ setType(Token.IDENT); setString("label5"); }});
      }
    };
    Node unreferencedLabelParent = new Node() {{ /* empty */}};
    processLabels.shouldTraverse(new NodeTraversal() {/* unused */}, unreferencedLabelNode,
        unreferencedLabelParent);
    processLabels.visitLabel(unreferencedLabelNode, unreferencedLabelParent);
    assertNull(unreferencedLabelNode.getParent());
  }
}