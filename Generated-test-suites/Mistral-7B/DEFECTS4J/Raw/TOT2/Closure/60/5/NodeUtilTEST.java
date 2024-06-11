import static org.junit.Assert.assertTrue;
   import com.google.javascript.rhino.Node;

   public class NodeUtilTest {

       @org.junit.Test
       public void testIsForIn() {
           Node forInNode = new Node(Token.FOR);
           Node forInNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.ASSIGN), new Node(Token.IDENTIFIER)});
           assertTrue(NodeUtil.isForIn(forInNodeWithThreeChildren));
           assertTrue(!NodeUtil.isForIn(new Node(Token.IF)));
       }

       @org.junit.Test
       public void testIsStatement() {
           Node statementNode = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER), new Node(Token.NUMERIC_LITERAL));
           assertTrue(NodeUtil.isStatement(statementNode));
           assertTrue(!NodeUtil.isStatement(new Node(Token.IF)));
       }

       @org.junit.Test
       public void testIsFunctionDeclaration() {
           Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.BLOCK)});
           assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
           assertTrue(!NodeUtil.isFunctionDeclaration(new Node(Token.FOR)));
       }

       @org.junit.Test
       public void testIsHoistedFunctionDeclaration() {
           Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.BLOCK)}, new Node(Token.SCRIPT));
           assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
           assertTrue(!NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.BLOCK)}, new Node(Token.BLOCK))));
       }
   }