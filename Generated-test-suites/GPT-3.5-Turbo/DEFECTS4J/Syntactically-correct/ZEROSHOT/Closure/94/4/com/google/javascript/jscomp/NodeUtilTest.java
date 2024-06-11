import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR, 
                              new Node(Token.NAME),
                              new Node(Token.EMPTY),
                              new Node(Token.EMPTY));
    assertTrue(nodeUtil.isForIn(forInNode));
    
    Node nonForInNode = new Node(Token.NAME);
    assertFalse(nodeUtil.isForIn(nonForInNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    assertTrue(nodeUtil.isStatement(scriptNode));
    
    Node blockNode = new Node(Token.BLOCK, new Node(Token.EMPTY));
    assertTrue(nodeUtil.isStatement(blockNode));
    
    Node labelNode = new Node(Token.LABEL, new Node(Token.EMPTY));
    assertTrue(nodeUtil.isStatement(labelNode));
    
    Node nonStatementNode = new Node(Token.NAME);
    assertFalse(nodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION,
                                            new Node(Token.NAME),
                                            new Node(Token.EMPTY));
    assertTrue(nodeUtil.isFunctionDeclaration(functionDeclarationNode));
    
    Node nonFunctionDeclarationNode = new Node(Token.NAME);
    assertFalse(nodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION,
                                                   new Node(Token.NAME),
                                                   new Node(Token.EMPTY));
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT,
                                                      hoistedFunctionDeclarationNode,
                                                      new Node(Token.EMPTY)));
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    
    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION,
                                                      new Node(Token.NAME),
                                                      new Node(Token.EMPTY));
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}