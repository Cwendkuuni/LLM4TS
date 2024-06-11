import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    }

    @Test
    public void process_ShouldTraverseRootNodeAndCallVisitMethod() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        NodeTraversal.traverse(compiler, root, checkSideEffects);

        // Verify that the traverse method was called with the correct parameters
        verify(compiler).traverse(externs, root, checkSideEffects);
    }

    @Test
    public void hotSwapScript_ShouldTraverseScriptRootAndOriginalRootAndCallVisitMethod() {
        Node scriptRoot = mock(Node.class);
        Node originalRoot = mock(Node.class);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Verify that the traverse method was called with the correct parameters
        verify(compiler).traverse(scriptRoot, originalRoot, checkSideEffects);
    }

    @Test
    public void visit_ShouldNotReportErrorWhenNodeHasSideEffects() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);

        when(node.isEmpty()).thenReturn(false);
        when(node.isComma()).thenReturn(false);
        when(NodeUtil.isExpressionResultUsed(node)).thenReturn(true);
        when(NodeUtil.isSimpleOperatorType(node.getType())).thenReturn(true);
        when(NodeUtil.mayHaveSideEffects(node, compiler)).thenReturn(true);

        checkSideEffects.visit(traversal, node, parent);

        // Verify that the report method was not called
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void visit_ShouldReportErrorWhenNodeLacksSideEffects() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);

        when(node.isEmpty()).thenReturn(false);
        when(node.isComma()).thenReturn(false);
        when(NodeUtil.isExpressionResultUsed(node)).thenReturn(false);
        when(NodeUtil.isSimpleOperatorType(node.getType())).thenReturn(true);
        when(NodeUtil.mayHaveSideEffects(node, compiler)).thenReturn(false);

        checkSideEffects.visit(traversal, node, parent);

        // Verify that the report method was called with the correct parameters
        verify(compiler).report(traversal.makeError(node, CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "The result of the 'operator' operator is not being used."));
    }

    @Test
    public void visit_ShouldNotReportErrorWhenNodeIsQualifiedNameWithJSDocInfo() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);

        when(node.isEmpty()).thenReturn(false);
        when(node.isQualifiedName()).thenReturn(true);
        when(node.getJSDocInfo()).thenReturn(jsDocInfo);

        checkSideEffects.visit(traversal, node, parent);

        // Verify that the report method was not called
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void visit_ShouldNotReportErrorWhenNodeIsExprResult() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);

        when(node.isEmpty()).thenReturn(false);
        when(node.isExprResult()).thenReturn(true);

        checkSideEffects.visit(traversal, node, parent);

        // Verify that the report method was not called
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void protectSideEffects_ShouldAddExternAndProtectSideEffectFreeNodes() {
        Node node1 = new Node(Token.NAME, "var");
        Node node2 = new Node(Token.NAME, "x");
        Node node3 = new Node(Token.ASSIGN, node2);
        node1.addChildToBack(node3);

        problemNodes.add(node1);

        checkSideEffects.protectSideEffects();

        // Verify that the problem node was replaced with a call to the external function
        Node expectedNode1 = new Node(Token.CALL, new Node(Token.NAME, PROTECTOR_FN));
        expectedNode1.addChildToBack(node1);
        assertEquals(expectedNode1, node1.getParent().getChildAtIndex(0));

        // Verify that the extern function was added
        Node externs = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
        Node expectedExterns = new Node(Token.VAR, new Node(Token.NAME, PROTECTOR_FN));
        assertEquals(expectedExterns, externs.getFirstChild());
    }
}

