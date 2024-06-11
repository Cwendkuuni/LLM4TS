import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.testing.TopLevelTestingCompiler;
import com.google.javascript.rhino.Nodeops;

public class RemoveConstantExpressionsTest {

 private Compiler compiler;

 @Before
 public void setUp() {
 compiler = new TopLevelTestingCompiler();
 }

 @Test
 public void testRemoveConstantExpressions() {
 CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.SQUASH);
 RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler.getAbstractCompiler());
 ParallelCompilerPass.runPasses(pass, Lists.newArrayList(options), compiler.getExterns(), compiler.getRoot());

 Node root = compiler.getRoot();
 Node node1 = new Node(EXPR_STATEMENT, new Node(BINOP, OPERATOR_PLUS, new Node(UNARY_OP, OPERATOR_PLUS, new Node(IDENTIFIER, "1")), new Node(CALL_EXPRESSION, new Node(IDENTIFIER, "foo"), Lists.newArrayList(new Node(IDENTIFIER, "bar")))));
 Node node2 = new Node(EXPR_STATEMENT, new Node(BINOP, OPERATOR_PLUS, new Node(IDENTIFIER, "1"), node1));
 Node node3 = new Node(EXPR_STATEMENT, node1);
 Node node4 = new Node(EXPR_STATEMENT, node2);
 Node node5 = new Node(EXPR_STATEMENT, node3);
 Node node6 = new Node(EXPR_STATEMENT, node4);
 Node node7 = new Node(EXPR_STATEMENT, node5);

 Node expectedRoot = new Node(EXPR_STATEMENT, new Node(CALL_EXPRESSION, new Node(IDENTIFIER, "foo"), Lists.newArrayList()), new Node(CALL_EXPRESSION, new Node(IDENTIFIER, "bar"), Lists.newArrayList()));

 assertEquals("Incorrect simplification of removeConstantExpressions", expectedRoot, root);
 }
}

class NodeUtil {
 static boolean nodeTypeMayHaveSideEffects(Node node) {
 return node.getType() == CALL_EXPRESSION || node.getType() == ASSIGN || node.getType() == STATEMENT || node.getType() == UNARY_OP && node.getOperator() == OPERATOR_DELETE;
 }
}

class ReportCodeHasChangedListenerTest {
 @Test
 public void testReportCodeHasChangedListener() {
 Result result = new Result();
 ReportCodeHasChangedListener listener = new ReportCodeHasChangedListener(result);
 AstChangeProxy changeProxy = new AstChangeProxy();
 changeProxy.registerListener(listener);

 Node node = new Node(EXPR_STATEMENT, new Node(BINOP, OPERATOR_PLUS, new Node(IDENTIFIER, "1"), new Node(IDENTIFIER, "2")));
 changeProxy.replaceWith(null, node, new Node(EXPR_STATEMENT, new Node(IDENTIFIER, "3")));

 assertTrue("Code has not been reported as changed", result.changed);
 }
}

class GatherSideEffectSubexpressionsCallback {
 private Node parent;
 private GatherSideEffectSubexpressionsCallback next;
 private List<Node> subexpressions;

 public GatherSideEffectSubexpressionsCallback(Node parent, List<Node> subexpressions) {
 this.parent = parent;
 this.subexpressions = subexpressions;
 }

 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
 subexpressions.add(node);
 }
 if (next != null) {
 next.visit(traversal, node, parent);
 }
 }

 public static void gatherSideEffectSubexpressions(Node node, List<Node> subexpressions) {
 NodeTraversal.traverse(null, node, new GatherSideEffectSubexpressionsCallback(null, subexpressions));
 }
}

class CopySideEffectSubexpressions {
 private Node parent;
 private List<Node> subexpressions;

 public CopySideEffectSubexpressions(Node parent, List<Node> subexpressions) {
 this.parent = parent;
 this.subexpressions = subexpressions;
 }

 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (this.parent != null && this.parent.equals(parent)) {
 Node replacement = new Node(EXPR_STATEMENT, new Node(TOKEN, Token.EXPR_RESULT), Lists.newArrayList());
 Node newParent = new Node(EXPR_STATEMENT, replacement, Lists.newArrayList());
 Nodeops.setParent(node, newParent);
 NodeUtil.trySimplify(parent, newParent);
 NodeUtil.trySimplify(parent, replacement);
 this.subexpressions.add(replacement);
 }
 if (next != null) {
 next.visit(traversal, node, parent);
 }
 }
}