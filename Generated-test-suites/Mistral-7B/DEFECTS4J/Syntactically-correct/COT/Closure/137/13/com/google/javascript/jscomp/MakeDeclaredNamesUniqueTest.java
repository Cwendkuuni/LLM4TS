import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueBasic() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node nameNode1 = NodeFactory.createName("name1");
    Node nameNode2 = NodeFactory.createName("name1");
    Node funcNode = NodeFactory.createFunction(null, "func", Arrays.asList(nameNode1), null);
    rootNode.addChild(nameNode1);
    rootNode.addChild(funcNode);
    rootNode.addChild(nameNode2);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    assertEquals("name1_0", nameNode1.getString());
    assertEquals("name1_1", nameNode2.getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueSameNameInSameScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node nameNode1 = NodeFactory.createName("name1");
    Node nameNode2 = NodeFactory.createName("name1");
    rootNode.addChild(nameNode1);
    rootNode.addChild(nameNode2);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    assertEquals("name1_0", nameNode1.getString());
    assertEquals("name1_1", nameNode2.getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueDifferentNameInSameScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node nameNode1 = NodeFactory.createName("name1");
    Node nameNode2 = NodeFactory.createName("name2");
    rootNode.addChild(nameNode1);
    rootNode.addChild(nameNode2);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    assertEquals("name1", nameNode1.getString());
    assertEquals("name2", nameNode2.getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueFunctionParameters() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node funcNode = NodeFactory.createFunction(null, "func", null, null);
    Node paramsNode = NodeFactory.createList(Arrays.asList(
        NodeFactory.createName("param1"),
        NodeFactory.createName("param1")
    ));
    funcNode.setFirstChild(paramsNode);
    rootNode.addChild(funcNode);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    NodeList params = funcNode.getFirstChild().asList();
    assertEquals("param1_0", params.get(0).getString());
    assertEquals("param1_1", params.get(1).getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueFunctionBody() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node funcNode = NodeFactory.createFunction(null, "func", null, null);
    Node bodyNode = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createName("name1"),
        NodeFactory.createName("name1")
    ));
    funcNode.setLastChild(bodyNode);
    rootNode.addChild(funcNode);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    NodeList body = funcNode.getLastChild().asList();
    assertEquals("name1_0", body.get(0).getString());
    assertEquals("name1_1", body.get(1).getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueFunctionRecursion() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node funcNode = NodeFactory.createFunction(null, "func", null, null);
    Node recursiveNameNode = NodeFactory.createName("nameRecursive");
    Node recursiveCallNode = NodeFactory.createCall(funcNode, Arrays.asList(recursiveNameNode));
    Node bodyNode = NodeFactory.createBlock(Arrays.asList(recursiveNameNode));
    funcNode.setLastChild(bodyNode);
    rootNode.addChild(funcNode);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    NodeList body = funcNode.getLastChild().asList();
    assertEquals("nameRecursive_0", body.get(0).getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueCatchBlock() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node tryBlockNode = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createName("name1")
    ));
    Node catchBlockNode = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createName("nameCatch"),
        NodeFactory.createName("name1")
    ));
    Node tryCatchNode = NodeFactory.createTryCatch(tryBlockNode, catchBlockNode);
    rootNode.addChild(tryCatchNode);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    NodeList tryBlock = tryCatchNode.getFirstChild().asList();
    assertEquals("name1_0", tryBlock.get(0).getString());
    NodeList catchBlock = tryCatchNode.getLastChild().asList();
    assertEquals("name1_1", catchBlock.get(0).getString());
    assertEquals("nameCatch", catchBlock.get(1).getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUniqueGlobalScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node nameNode1 = NodeFactory.createName("name1");
    Node nameNode2 = NodeFactory.createName("name1");
    rootNode.addChild(nameNode1);
    rootNode.addChild(nameNode2);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);

    assertEquals("name1_0", nameNode1.getString());
    assertEquals("name1_1", nameNode2.getString());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueInvalidNode() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node invalidNode = NodeFactory.createBlock(null);
    rootNode.addChild(invalidNode);

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUniqueNullNode() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = null;

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUniqueNullParent() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Compiler compiler = mock(Compiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    Node rootNode = NodeFactory.createProgram();
    Node nameNode = NodeFactory.createName("name");

    makeDeclaredNamesUnique.traverse(compiler, rootNode, null, nameNode);
  }
}