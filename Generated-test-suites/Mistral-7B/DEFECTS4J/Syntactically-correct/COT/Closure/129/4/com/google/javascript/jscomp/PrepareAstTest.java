import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 // Initialize the compiler for testing
 compiler = new AbstractCompiler();
 }

 /**
 * Test method for the constructor PrepareAst(AbstractCompiler)
 */
 @Test
 public void testPrepareAstConstructor() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 assertNotNull(prepareAst);
 }

 /**
 * Test method for the constructor PrepareAst(AbstractCompiler, boolean)
 */
 @Test
 public void testPrepareAstConstructorWithCheckOnlyFlag() {
 PrepareAst prepareAst = new PrepareAst(compiler, true);
 assertNotNull(prepareAst);
 }

 /**
 * Test method for the process method with null externs and root
 */
 @Test
 public void testProcessWithNullExternsAndRoot() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = null;
 Node externs = null;
 prepareAst.process(externs, root);
 // No exception is thrown, so the test passes
 }

 /**
 * Test method for the normalizeNodeTypes method
 */
 @Test
 public void testNormalizeNodeTypes() {
 Node root = new Node(Token.BLOCK);
 PrepareAst prepareAst = new PrepareAst(compiler);
 prepareAst.process(null, root);

 Node n = new Node(Token.IF);
 n.setFirstChild(new Node(Token.TRUE));
 n.setNext(new Node(Token.BRACE));
 n.getLastChild().setNext(new Node(Token.RETURN, new Node(Token.INT_LITERAL, 1)));
 root.addChild(n);

 // Test normalization with checkOnly flag set to false
 prepareAst = new PrepareAst(compiler, false);
 prepareAst.process(null, root);
 assertEquals(1, root.getChildCount());
 Node block = root.getFirstChild();
 assertEquals(3, block.getChildCount());
 assertEquals(NodeType.BLOCK, block.getType());
 assertEquals(NodeType.IF, n.getType());

 // Test normalization with checkOnly flag set to true
 prepareAst = new PrepareAst(compiler, true);
 prepareAst.process(null, root);
 // An assertion error is thrown as checkOnly flag prevents any changes
 }

 /**
 * Test method for the normalizeBlocks method
 */
 @Test
 public void testNormalizeBlocks() {
 Node root = new Node(Token.BLOCK);
 PrepareAst prepareAst = new PrepareAst(compiler);
 prepareAst.process(null, root);

 Node ifStatement = new Node(Token.IF);
 ifStatement.setFirstChild(new Node(Token.TRUE));
 ifStatement.setNext(new Node(Token.BRACE));
 ifStatement.getLastChild().setNext(new Node(Token.RETURN, new Node(Token.INT_LITERAL, 1)));
 root.addChild(ifStatement);

 Node elseStatement = new Node(Token.ELSE);
 elseStatement.setNext(new Node(Token.BRACE));
 ifStatement.addSibling(elseStatement);

 prepareAst = new PrepareAst(compiler, false);
 prepareAst.process(null, root);
 assertEquals(2, root.getChildCount());
 Node ifBlock = ifStatement.getLastChild();
 assertEquals(3, ifBlock.getChildCount());
 Node elseBlock = elseStatement.getNext().getFirstChild();
 assertEquals(1, elseBlock.getChildCount());
 }

 /**
 * Test method for the PrepareAnnotations class
 */
 @Test
 public void testPrepareAnnotations() {
 Node root = new Node(Token.BLOCK);
 PrepareAst prepareAst = new PrepareAst(compiler);
 prepareAst.process(null, root);

 Node objLit = new Node(Token.OBJECT_LITERAL);
 root.addChild(objLit);

 Node keyA = new Node(Token.IDENTIFIER, "a");
 Node valueA = new Node(Token.STRING_LITERAL, "b");
 objLit.addChild(keyA);
 objLit.addChild(valueA);

 Node keyB = new Node(Token.IDENTIFIER, "c");
 Node valueB = new Node(Token.FUNCTION, new Node(Token.BLOCK));
 valueB.setNext(new Node(Token.RETURN, new Node(Token.STRING_LITERAL, "d")));
 objLit.addChild(keyB);
 objLit.addChild(valueB);

 // Test JsDoc annotations are correctly normalized
 prepareAst = new PrepareAst(compiler, false);
 prepareAst.process(null, root);
 assertEquals(1, root.getChildCount());
 Node normalizedObjLit = root.getFirstChild();
 assertEquals(3, normalizedObjLit.getChildCount());

 Node keyANode = normalizedObjLit.getChild(0);
 assertEquals(NodeType.IDENTIFIER, keyANode.getType());
 assertEquals("a", keyANode.getString());
 Node valueANode = normalizedObjLit.getChild(1);
 assertEquals(NodeType.STRING_LITERAL, valueANode.getType());
 assertEquals("b", valueANode.getString());

 Node keyBNode = normalizedObjLit.getChild(2);
 assertEquals(NodeType.IDENTIFIER, keyBNode.getType());
 assertEquals("c", keyBNode.getString());
 Node valueBNode = normalizedObjLit.getChild(3);
 assertEquals(NodeType.FUNCTION, valueBNode.getType());
 assertEquals(1, valueBNode.getChildCount());
 Node valueBBlock = valueBNode.getFirstChild();
 assertEquals(NodeType.BLOCK, valueBBlock.getType());
 assertEquals(1, valueBBlock.getChildCount());
 Node valueBReturn = valueBBlock.getLastChild();
 assertEquals(NodeType.RETURN, valueBReturn.getType());
 assertEquals("d", valueBReturn.getString());
 }
}