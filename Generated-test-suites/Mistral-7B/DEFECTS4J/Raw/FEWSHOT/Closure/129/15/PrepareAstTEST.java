package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAst.NodeTraversal.Callback;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node root = NodeFactory.newProgram();
    NodeTraversal.traverse(prepareAst, root, null);

    assertTrue(root.isProgram());
    assertTrue(root.getFirstChild().isStatements());
  }

  @Test
  public void testNormalizeNodeTypes() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node root = NodeFactory.newBlock(
        NodeFactory.newExpressionStatement(
            NodeFactory.newCall(
                NodeFactory.newName("eval"),
                NodeFactory.newArray(),
                NodeFactory.newString("1 + 2")
            )
        )
    );

    NodeTraversal.traverse(prepareAst, root, null);

    assertTrue(root.getFirstChild().isCall());
    assertTrue(root.getFirstChild().getFirstChild().isName());
    assertTrue(root.getFirstChild().getFirstChild().isIdentifier("eval"));
    assertTrue(root.getFirstChild().getFirstChild().getNext().isString());
    assertTrue(root.getFirstChild().getFirstChild().getNext().getString().equals("1 + 2"));
  }

  @Test
  public void testNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node root = NodeFactory.newBlock(
        NodeFactory.newIfStatement(
            NodeFactory.newBinaryOperator(
                NodeFactory.newName("x"),
                Token.OP_EQ,
                NodeFactory.newNumber(1),
                null
            ),
            NodeFactory.newBlock(
                NodeFactory.newStatement(
                    NodeFactory.newExpressionStatement(
                        NodeFactory.newCall(
                            NodeFactory.newName("print"),
                            NodeFactory.newArray(),
                            NodeFactory.newString("x is 1")
                        )
                    )
                )
            ),
            NodeFactory.newBlock(
                NodeFactory.newStatement(
                    NodeFactory.newExpressionStatement(
                        NodeFactory.newCall(
                            NodeFactory.newName("print"),
                            NodeFactory.newArray(),
                            NodeFactory.newString("x is not 1")
                        )
                    )
                )
            )
        )
    );

    NodeTraversal.traverse(prepareAst, root, null);

    Node ifNode = root.getFirstChild();
    Node ifBlock = ifNode.getFirstChild();
    Node elseBlock = ifNode.getLastChild();

    assertTrue(ifNode.isIfStatement());
    assertTrue(ifBlock.isBlock());
    assertTrue(ifBlock.getFirstChild().isStatement());
    assertTrue(ifBlock.getFirstChild().getNext().isStatement());
    assertTrue(elseBlock.isBlock());
    assertTrue(elseBlock.getFirstChild().isStatement());
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node root = NodeFactory.newProgram();
    root.setJSDocInfo(new JsDocInfo("test"));

    Node objLit = NodeFactory.newObjectLiteral();
    objLit.setJSDocInfo(new JsDocInfo("foo"));

    Node key1 = NodeFactory.newName("a");
    Node key2 = NodeFactory.newName("b");

    Node value1 = NodeFactory.newNumber(1);
    value1.setJSDocInfo(new JsDocInfo("bar"));

    Node value2 = NodeFactory.newFunction(
        NodeFactory.newArray(),
        NodeFactory.newBlock(
            NodeFactory.newReturnStatement(
                NodeFactory.newCall(
                    NodeFactory.newName("print"),
                    NodeFactory.newArray(),
                    NodeFactory.newString("hello")
                )
            )
        )
    );
    value2.setJSDocInfo(new JsDocInfo("qux"));

    objLit.addChildrenToFront(
        NodeFactory.newName("a"),
        NodeFactory.newObjectProperty(
            key1,
            value1
        ),
        NodeFactory.newName("b"),
        NodeFactory.newObjectProperty(
            key2,
            value2
        )
    );

    root.addChildrenToBack(objLit);

    NodeTraversal.traverse(prepareAst, root, null);

    JsDocInfo objLitJSDocInfo = objLit.getJSDocInfo();
    JsDocInfo key1JSDocInfo = key1.getJSDocInfo();
    JsDocInfo value1JSDocInfo = value1.getJSDocInfo();
    JsDocInfo key2JSDocInfo = key2.getJSDocInfo();
    JsDocInfo value2JSDocInfo = value2.getJSDocInfo();

    assertTrue(objLitJSDocInfo.isThis());
    assertTrue(objLitJSDocInfo.isConstructor());
    assertTrue(objLitJSDocInfo.getName().equals("foo"));

    assertTrue(key1JSDocInfo.isName());
    assertTrue(key1JSDocInfo.getTagName().equals("param"));
    assertTrue(key1JSDocInfo.getName().equals("a"));

    assertTrue(value1JSDocInfo.isValue());
    assertTrue(value1JSDocInfo.getTagName().equals("value"));
    assertTrue(value1JSDocInfo.getName().equals("bar"));

    assertTrue(key2JSDocInfo.isName());
    assertTrue(key2JSDocInfo.getTagName().equals("param"));
    assertTrue(key2JSDocInfo.getName().equals("b"));

    assertTrue(value2JSDocInfo.isFunction());
    assertTrue(value2JSDocInfo.getTagName().equals("method"));
    assertTrue(value2JSDocInfo.getName().equals("qux"));
  }

  @Test
  public void testPrepareAnnotations_KeepExistingJSDoc() {
    PrepareAst prepareAst = new PrepareAst(null);

    Node root = NodeFactory.newProgram();
    root.setJSDocInfo(new JsDocInfo("test"));

    Node objLit = NodeFactory.newObjectLiteral();
    objLit.setJSDocInfo(new JsDocInfo("foo"));

    Node key = NodeFactory.newName("a");
    Node value = NodeFactory.newFunction(
        NodeFactory.newArray(),
        NodeFactory.newBlock(
            NodeFactory.newReturnStatement(
                NodeFactory.newCall(
                    NodeFactory.newName("print"),
                    NodeFactory.newArray(),
                    NodeFactory.newString("hello")
                )
            )
        )
    );
    value.setJSDocInfo(new JsDocInfo("qux", new String[]{"bar"}));

    objLit.addChildrenToFront(
        NodeFactory.newName("a"),
        NodeFactory.newObjectProperty(
            key,
            value
        )
    );

    root.addChildrenToBack(objLit);

    NodeTraversal.traverse(prepareAst, root, null);

    JsDocInfo valueJSDocInfo = value.getJSDocInfo();

    assertTrue(valueJSDocInfo.getTags().length > 0);
    assertTrue(valueJSDocInfo.getTagNames()[0].equals("bar"));
  }

  @Test(expected = AssertionError.class)
  public void testCheckOnlyConstraint() {
    PrepareAst prepareAst = new PrepareAst(null, true);

    Node root = NodeFactory.newProgram();
    NodeTraversal.traverse(prepareAst, root, null);

    // Should not reach here
  }
}

class JsDocInfo {
  private String[] tags;
  private String tagNames[];
  private String name;

  public JsDocInfo(String info) {
    this(info, new String[0]);
  }

  public JsDocInfo(String info, String[] tags) {
    this.tags = tags;
    this.tagNames = tags;
    this.name = info;
  }

  public boolean isThis() {
    return this.name.equals("this");
  }

  public boolean isConstructor() {
    return this.name.equals("constructor");
  }

  public boolean isMethod() {
    return this.name.startsWith("method ");
  }

  public boolean isParam() {
    return this.name.startsWith("param ");
  }

  public boolean isValue() {
    return this.name.startsWith("value ");
  }

  public String[] getTags() {
    return this.tags;
  }

  public String[] getTagNames() {
    return this.tagNames;
  }

  public String getName() {
    return this.name;
  }
}