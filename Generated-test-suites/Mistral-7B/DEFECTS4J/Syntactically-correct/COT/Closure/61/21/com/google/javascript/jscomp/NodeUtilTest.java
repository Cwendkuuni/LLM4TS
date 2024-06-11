package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, 0, new int[] {3}, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(SCRIPT, 0, new int[] {}, new int[] {});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(SCRIPT, 0, new int[] {}, new int[] {});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(ADD, 0, new int[] {1, 2}, new int[] {0, 1});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, 0, new int[] {1}, new int[] {0});
    nodeFunctionDeclaration.setProperty("id", new Node("id", 0, new int[] {}, new int[] {}));
    nodeFunctionDeclaration.setProperty("params", new Node(LIST, 0, new int[] {1}, new int[] {}));
    nodeFunctionDeclaration.setProperty("body", new Node(LIST, 0, new int[] {}, new int[] {}));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(VAR, 0, new int[] {1}, new int[] {0});
    nodeNotFunctionDeclaration.setProperty("id", new Node("id", 0, new int[] {}, new int[] {}));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, 0, new int[] {1}, new int[] {0});
    nodeFunctionDeclaration.setProperty("id", new Node("id", 0, new int[] {}, new int[] {}));
    nodeFunctionDeclaration.setProperty("params", new Node(LIST, 0, new int[] {1}, new int[] {}));
    nodeFunctionDeclaration.setProperty("body", new Node(LIST, 0, new int[] {}, new int[] {}));

    Node parentScript = new Node(SCRIPT, 0, new int[] {}, new int[] {});
    parentScript.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node parentFunction = new Node(FUNCTION, 0, new int[] {1}, new int[] {0});
    parentFunction.setProperty("id", new Node("id", 0, new int[] {}, new int[] {}));
    parentFunction.addChild(nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testParentNull() {
    Node nodeStatement = new Node(SCRIPT, 0, new int[] {}, new int[] {});
    NodeUtil.isStatement(nodeStatement);

    Node parentNull = null;
    Node node = new Node(SCRIPT, 0, new int[] {}, new int[] {});
    NodeUtil.isStatement(node);
    node.setParent(parentNull);
  }
}