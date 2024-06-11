import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;

public class PrepareAstTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 }

 @Test
 public void testProcessExternsAndRootNull() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 prepareAst.process(null, null);
 }

 @Test
 public void testProcessExternsAndRootNotNull() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node externs = IR.objectLiteral();
 Node root = IR.objectLiteral();
 prepareAst.process(externs, root);
 }

 @Test
 public void testNormalizeNodeTypes_EmptyNode() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node n = IR.empty();
 prepareAst.normalizeNodeTypes(n);
 assertEquals(NodeType.UNDEFINED, n.getType());
 }

 @Test
 public void testNormalizeNodeTypes_VoidExpr() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node n = IR.exprVoid();
 prepareAst.normalizeNodeTypes(n);
 assertEquals(NodeType.VOID, n.getType());
 }

 @Test
 public void testNormalizeNodeTypes_Function() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node n = IR.function("func", null, IR.block());
 prepareAst.normalizeNodeTypes(n);
 assertEquals(NodeType.FUNCTION, n.getType());
 }

 @Test
 public void testNormalizeNodeTypes_FunctionWithBody() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node n = IR.function("func", null, IR.literal(1));
 prepareAst.normalizeNodeTypes(n);
 assertEquals(NodeType.FUNCTION, n.getType());
 }

 @Test(expected = AssertionError.class)
 public void testReportChange_CheckOnly() {
 PrepareAst prepareAst = new PrepareAst(compiler, true);
 Node n = IR.literal(1);
 prepareAst.reportChange();
 }

 @Test
 public void testNormalizeBlocks_EmptyNode() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node n = IR.empty();
 prepareAst.normalizeBlocks(n);
 assertEquals(0, n.getChildCount());
 }

 @Test
 public void testNormalizeBlocks_ControlStructure() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node ifNode = IR.ifExpr(null, IR.literal(true), IR.literal(1));
 prepareAst.normalizeBlocks(ifNode);
 assertEquals(3, ifNode.getChildCount());
 }

 @Test
 public void testPrepareAnnotations_ObjectLit() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node objlit = IR.objectLiteral();
 Node key = IR.identifier("a");
 Node value = IR.literal("b");
 objlit.addChildToEnd(key);
 objlit.addChildToEnd(value);
 prepareAst.new PrepareAnnotations().normalizeObjectLiteralAnnotations(objlit);
 assertEquals("b", value.getString());
 }

 @Test
 public void testAnnotateCalls_FreeCall() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node call = IR.call(IR.identifier("func"), IR.literal(1));
 prepareAst.new PrepareAnnotations().annotateCalls(call);
 assertTrue(call.getBooleanProp(Node.FREE_CALL));
 }

 @Test
 public void testAnnotateCalls_DirectEval() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node call = IR.call(IR.identifier("eval"), null);
 prepareAst.new PrepareAnnotations().annotateCalls(call);
 assertTrue(call.getBooleanProp(Node.DIRECT_EVAL));
 }

 @Test
 public void testAnnotateDispatchers() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node obj = IR.objectLiteral();
 Node func = IR.function("func", null, IR.block());
 obj.addChildToEnd(IR.property(IR.identifier("x"), func));
 Node assign = IR.assign(IR.identifier("x"), IR.identifier("x"));
 prepareAst.new PrepareAnnotations().annotateDispatchers(assign, obj);
 assertTrue(func.getBooleanProp(Node.IS_DISPATCHER));
 }
}