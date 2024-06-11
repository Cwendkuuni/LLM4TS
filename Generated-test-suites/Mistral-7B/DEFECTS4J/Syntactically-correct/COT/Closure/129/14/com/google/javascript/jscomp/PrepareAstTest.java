import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import org.junit.Test;

public class PrepareAstTest {

 @Test
 public void testProcessExternsAndRootNull() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 prepareAst.process(null, null);
 }

 @Test
 public void testNormalizeNodeTypesNullChild() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node node = IR.literal(1);
 prepareAst.normalizeNodeTypes(node);
 assertNotNull(node.getFirstChild());
 }

 @Test
 public void testNormalizeNodeTypesEmptyChild() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node node = IR.block();
 node.setWasEmptyNode(true);
 prepareAst.normalizeNodeTypes(node);
 assertNotNull(node.getFirstChild());
 }

 @Test
 public void testNormalizeBlocksNullChild() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node node = IR.literal(1);
 prepareAst.normalizeBlocks(node);
 assertFalse(node.isBlock());
 }

 @Test
 public void testNormalizeBlocksEmptyChild() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node node = IR.block();
 prepareAst.normalizeBlocks(node);
 assertTrue(node.isBlock());
 }

 @Test
 public void testProcessExternsAndRootNotNull() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node externs = IR.literal(1);
 Node root = IR.literal(2);
 prepareAst.process(externs, root);
 }

 @Test
 public void testNormalizeNodeTypes() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node node = IR.function("foo", new Node[0], IR.block());
 prepareAst.normalizeNodeTypes(node);
 assertTrue(node.isFunction());
 }

 @Test(expected = AssertionError.class)
 public void testReportChange() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node node = IR.literal(1);
 prepareAst.process(null, node);
 }

 @Test
 public void testAnnotateCalls() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node parent = IR.assign(IR.name("x"), IR.call(IR.name("foo"), new Node[0]));
 Node call = parent.getLastChild();
 prepareAst.process(null, parent);
 assertTrue(call.isCall());
 assertFalse(call.getBooleanProp(Node.DIRECT_EVAL));
 assertFalse(call.getBooleanProp(Node.FREE_CALL));

 parent.replaceChild(parent.getFirstChild(), IR.name("this"));
 parent.getLastChild().setParent(parent);
 prepareAst.process(null, parent);
 assertTrue(call.getBooleanProp(Node.DIRECT_EVAL));
 assertFalse(call.getBooleanProp(Node.FREE_CALL));
 }

 @Test
 public void testAnnotateDispatchers() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node parent = IR.assign(IR.name("x"), IR.function("foo", new Node[0], IR.block()));
 Node function = parent.getLastChild();
 prepareAst.process(null, parent);
 assertFalse(function.getBooleanProp(Node.IS_DISPATCHER));

 Node dispatcherParent = IR.assign(IR.name("x"), IR.block());
 Node dispatcherFunction = IR.function("foo", new Node[0], IR.block());
 dispatcherFunction.putBooleanProp(Node.IS_DISPATCHER, true);
 dispatcherParent.addChildToEnd(dispatcherFunction);
 prepareAst.process(null, dispatcherParent);
 assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
 }

 @Test
 public void testNormalizeObjectLiteralKeyAnnotations() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node objlit = IR.objectLit();
 Node key = IR.name("a");
 Node value = IR.function("foo", new Node[0], IR.block());
 key.setJSDocInfo(new JSDocInfo());
 objlit.addChildToEnd(key);
 objlit.addChildToEnd(value);
 prepareAst.process(null, objlit);
 assertNull(key.getJSDocInfo());
 assertNotNull(value.getJSDocInfo());
 }

 @Test
 public void testTraverse() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 Node n = IR.block();
 NodeTraversal.traverse(prepareAst, n, new PrepareAnnotations());
 }

 @Test
 public void testShouldTraverseObjectLit() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
 Node node = IR.objectLit();
 assertTrue(prepareAnnotations.shouldTraverse(null, node, null));
 }

 @Test
 public void testVisitObjectLit() {
 PrepareAst prepareAst = new PrepareAst(new AbstractCompiler() {});
 PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
 Node node = IR.objectLit();
 prepareAnnotations.visit(null, node, null);
 }
}