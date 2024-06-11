import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  // Test case for the process() method
  @Test
  public void processTest() {
    // Test with null inputs
    compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, null);
    
    // Assert that the compiler is not null
    assertNotNull(compiler);

    // Test with non-null inputs
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);
    
    // Assert that the compiler is not null
    assertNotNull(compiler);
  }

  // Test case for the normalizeNodeTypes() method
  @Test
  public void normalizeNodeTypesTest() {
    // Test with empty node
    Node emptyNode = new Node(Token.EMPTY);
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst.normalizeNodeTypes(emptyNode);
    
    // Assert that the node is still empty
    assertTrue(emptyNode.isEmpty());

    // Test with a node containing multiple children
    Node node = new Node(Token.ROOT);
    node.addChildToBack(new Node(Token.IF));
    node.addChildToBack(new Node(Token.WHILE));
    node.addChildToBack(new Node(Token.DO));
    node.addChildToBack(new Node(Token.FOR));
    prepareAst.normalizeNodeTypes(node);
    
    // Assert that each child node is now a block node
    for (Node child : node.children()) {
      assertTrue(child.isBlock());
    }
  }

  // Test case for the normalizeBlocks() method
  @Test
  public void normalizeBlocksTest() {
    // Test with a control structure node that is not a block or switch
    Node ifNode = new Node(Token.IF);
    Node statement = new Node(Token.GETPROP);
    ifNode.addChildToFront(statement);
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst.normalizeBlocks(ifNode);
    
    // Assert that the statement is now wrapped in a block
    assertTrue(ifNode.getFirstChild().isBlock());
    assertEquals(ifNode.getFirstChild().getFirstChild(), statement);

    // Test with a control structure node that is a label
    Node labelNode = new Node(Token.LABEL);
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst.normalizeBlocks(labelNode);
    
    // Assert that the label node is unchanged
    assertTrue(labelNode.isLabel());

    // Test with a control structure node that is a switch
    Node switchNode = new Node(Token.SWITCH);
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst.normalizeBlocks(switchNode);
    
    // Assert that the switch node is unchanged
    assertTrue(switchNode.isSwitch());
  }

  // Test case for the PrepareAnnotations class
  @Test
  public void prepareAnnotationsTest() {
    // Test with an object literal node
    Node objectLit = new Node(Token.OBJECTLIT);
    objectLit.addChildToFront(new Node(Token.STRING_KEY, new Node(Token.STRING)));
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(objectLit);
    
    // Assert that each child key node has its value's JSDocInfo set to its own JSDocInfo
    Node firstChildKey = objectLit.getFirstChild();
    assertNotNull(firstChildKey.getJSDocInfo());
    assertNotNull(firstChildKey.getFirstChild().getJSDocInfo());

    // Test with a call node that is a free call
    Node callNode = new Node(Token.CALL);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(callNode);
    
    // Assert that the call node's FREE_CALL property is set to true
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));

    // Test with a call node that is a direct call to eval
    Node evalNode = new Node(Token.NAME, new Node(Token.STRING));
    evalNode.setString("eval");
    Node callNode = new Node(Token.CALL, evalNode);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(callNode);
    
    // Assert that the name node's DIRECT_EVAL property is set to true
    assertTrue(evalNode.getBooleanProp(Node.DIRECT_EVAL));

    // Test with a function node that is a dispatcher
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN, new Node(Token.NAME), functionNode);
    parent.getJSDocInfo().setJavaDispatch(true);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateDispatchers(functionNode, parent);
    
    // Assert that the function node's IS_DISPATCHER property is set to true
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }
}