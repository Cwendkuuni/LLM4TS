package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticMessageCategory;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    level = CheckLevel.WARNING;
    checker = new CheckGlobalThis(compiler, level);
    traversal = NodeTraversal.create(checker, null);
  }

  /**
   * Test case for checking the function annotated with {@code @constructor}
   * and having a JSDocInfo associated with it.
   */
  @Test
  public void testConstructorFunction() {
    Node node = createNode(Token.FUNCTION, "constructor", new Node[] {});
    JSDocInfo jsDoc = createJsDocInfoNode("@constructor");
    node.setJSDocInfo(jsDoc);
    Node parent = createNode(Token.SCRIPT, null, new Node[] { node });

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the function annotated with {@code @this} and
   * having a JSDocInfo associated with it.
   */
  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = createNode(Token.FUNCTION, "myFunction", new Node[] {});
    JSDocInfo jsDoc = createJsDocInfoNode("@this");
    node.setJSDocInfo(jsDoc);
    Node parent = createNode(Token.SCRIPT, null, new Node[] { node });

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the function that is a prototype method.
   */
  @Test
  public void testPrototypeMethod() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "prototype"),
            createNode(Token.DOT, null, new Node[] { createNode(Token.NAME, "MyObject"), createNode(Token.NAME, "prototype") })
        }),
        createNode(Token.FUNCTION, "prototypeMethod", new Node[] {
            createNode(Token.THIS, null),
            createNode(Token.IDENT, "doSomething")
        })
    });

    Node node = createNode(Token.FUNCTION, "nonPrototypeMethod", new Node[] {});
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the function where there's no logical place to put
   * a {@code @this} annotation.
   */
  @Test
  public void testFunctionWithNoThisAnnotation() {
    Node node = createNode(Token.FUNCTION, "myFunction", new Node[] {});
    Node parent = createNode(Token.SCRIPT, null, new Node[] { node });

    assertTrue(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the function assignment.
   */
  @Test
  public void testFunctionAssignment() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "myFunction"),
            createNode(Token.FUNCTION, null, new Node[] {
                createNode(Token.THIS, null),
                createNode(Token.IDENT, "doSomething")
            })
        })
    });

    Node node = createNode(Token.FUNCTION, "otherFunction", new Node[] {});
    assertTrue(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the left side of an assignment.
   */
  @Test
  public void testAssignmentLeftSide() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "myFunction"),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.IDENT, "myFunction");
    assertTrue(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the right side of an assignment.
   */
  @Test
  public void testAssignmentRightSide() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "myFunction"),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.THIS, null);
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the property access on the left side of an
   * assignment.
   */
  @Test
  public void testPropertyAccessLeftSide() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "myFunction.prototype.myProperty"),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.THIS, null);
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the property access on the right side of an
   * assignment.
   */
  @Test
  public void testPropertyAccessRightSide() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "myFunction"),
            createNode(Token.THIS, null, new Node[] {
                createNode(Token.GETPROP, null, new Node[] { createNode(Token.IDENT, "prototype"), createNode(Token.IDENT, "myProperty") })
            })
        })
    });

    Node node = createNode(Token.THIS, null);
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the parent node that is not a script, block, name,
   * or assignment.
   */
  @Test
  public void testUnexpectedParentNode() {
    Node node = createNode(Token.THIS, null);
    Node parent = createNode(Token.COMMENT, null, new Node[] { node });

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the function call.
   */
  @Test
  public void testFunctionCall() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "myFunction"),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.CALL, null, new Node[] { createNode(Token.NAME, "myFunction"), createNode(Token.THIS) });
    assertTrue(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the assignment to a prototype property.
   */
  @Test
  public void testAssignmentToPrototypeProperty() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "prototype"),
            createNode(Token.GETPROP, null, new Node[] { createNode(Token.NAME, "MyObject"), createNode(Token.IDENT, "prototype") }),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.THIS, null);
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the assignment to a sub-property of a prototype property.
   */
  @Test
  public void testAssignmentToSubPropertyOfPrototypeProperty() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "prototype"),
            createNode(Token.GETPROP, null, new Node[] { createNode(Token.NAME, "MyObject"), createNode(Token.IDENT, "prototype") }),
            createNode(Token.GETPROP, null, new Node[] { createNode(Token.IDENT, "prototypeProperty") }),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.THIS, null);
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  /**
   * Test case for checking the two-level prototype property access.
   */
  @Test
  public void testTwoLevelPrototypePropertyAccess() {
    Node parent = createNode(Token.BLOCK, null, new Node[] {
        createNode(Token.ASSIGN, null, new Node[] {
            createNode(Token.NAME, "MyObject.prototype.MyNestedPrototypeProperty"),
            createNode(Token.THIS)
        })
    });

    Node node = createNode(Token.THIS, null);
    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, parent), node, parent));
  }

  private Node createNode(int type, String name, Node[] children) {
    Node node = new Node();
    node.setType(type);
    node.setFirstChild(createNodeArray(children));
    if (name != null) {
      node.setIdentifier(name);
    }
    return node;
  }

  private Node createJsDocInfoNode(String jsDocInfo) {
    Node jsDocNode = new Node();
    jsDocNode.setType(Token.COMMENT);
    jsDocNode.setFirstChild(createNode(Token.COMMENT, null, new Node[] {
        createNode(Token.COMMENT, null, new Node[] { new NodeText("/**") })
    }));

    Node jsDocInfoNode = createNode(Token.COMMENT, null, new Node[] {
        createNode(Token.COMMENT, null, new Node[] { new NodeText(jsDocInfo) })
    });

    jsDocNode.setLastChild(jsDocInfoNode);
    return jsDocNode;
  }

  private Node[] createNodeArray(Node[] nodes) {
    if (nodes == null) {
      return new Node[0];
    }

    Node[] nodeArray = new Node[nodes.length];
    for (int i = 0; i < nodes.length; ++i) {
      nodeArray[i] = nodes[i];
    }
    return nodeArray;
  }

  private boolean isGet(Node node) {
    if (node.getType() != Token.GETPROP) {
      return false;
    }

    Node getPropNode = node;
    Node identifier = null;
    while (getPropNode.getLastChild() != null) {
      Node child = getPropNode.getLastChild();
      if (child.getType() == Token.IDENT) {
        identifier = child;
        break;
      }
      getPropNode = child;
    }

    return identifier != null;
  }
}