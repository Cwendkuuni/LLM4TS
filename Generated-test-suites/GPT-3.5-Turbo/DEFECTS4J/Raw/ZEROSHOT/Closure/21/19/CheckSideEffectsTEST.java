import com.google.javascript.jscomp.*;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;
    private AbstractCompiler compiler;
    private Node scriptRoot;
    private Node originalRoot;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        scriptRoot = mock(Node.class);
        originalRoot = mock(Node.class);
    }

    // Test process() method
    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        checkSideEffects.process(externs, scriptRoot);
        verify(compiler, times(1)).reportCodeChange();
    }

    // Test hotSwapScript() method
    @Test
    public void testHotSwapScript() {
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        verify(compiler, times(1)).reportCodeChange();
    }

    // Test visit() method with empty node
    @Test
    public void testVisitEmptyNode() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isEmpty()).thenReturn(true);
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with comma node and result used
    @Test
    public void testVisitCommaNodeWithResultUsed() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node ancestor = mock(Node.class);
        when(n.isComma()).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(true);
        when(n.getParent()).thenReturn(parent);
        when(parent.getAncestors()).thenReturn(Lists.newArrayList(ancestor));
        when(ancestor.getType()).thenReturn(Token.EXPR_RESULT);
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with comma node, not result used, and not last child
    @Test
    public void testVisitCommaNodeNotResultUsed() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isComma()).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(false);
        when(n.getParent()).thenReturn(parent);
        when(parent.getLastChild()).thenReturn(mock(Node.class));
        when(mock(Node.class).getAncestors()).thenReturn(Lists.newArrayList(mock(Node.class)));
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with comma node, not result used, and last child
    @Test
    public void testVisitCommaNodeNotResultUsedLastChild() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isComma()).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(false);
        when(n.getParent()).thenReturn(parent);
        when(parent.getLastChild()).thenReturn(n);
        when(n.getAncestors()).thenReturn(Lists.newArrayList(mock(Node.class)));
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with parent node and result used
    @Test
    public void testVisitParentNodeWithResultUsed() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isExprResult()).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(true);
        when(n.getJSDocInfo()).thenReturn(mock(com.google.javascript.rhino.JSDocInfo.class));
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with parent node and empty JSDocInfo
    @Test
    public void testVisitParentNodeWithEmptyJSDocInfo() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isExprResult()).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(true);
        when(n.getJSDocInfo()).thenReturn(null);
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with parent node and qualified name
    @Test
    public void testVisitParentNodeQualifiedName() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        com.google.javascript.rhino.JSDocInfo info = mock(com.google.javascript.rhino.JSDocInfo.class);
        when(n.isExprResult()).thenReturn(true);
        when(n.isQualifiedName()).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(true);
        when(n.getJSDocInfo()).thenReturn(info);
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with simple operator node and result used
    @Test
    public void testVisitSimpleOperatorNodeWithResultUsed() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isExprResult()).thenReturn(false);
        when(n.getType()).thenReturn(Token.ADD);
        when(NodeUtil.isSimpleOperatorType(n.getType())).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(true);
        checkSideEffects.visit(t, n, parent);
        verify(t, never()).makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class), anyString());
    }

    // Test visit() method with simple operator node and not result used
    @Test
    public void testVisitSimpleOperatorNodeNotResultUsed() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.isExprResult()).thenReturn(false);
        when(n.getType()).thenReturn(Token.ADD);
        when(NodeUtil.isSimpleOperatorType(n.getType())).thenReturn(true);
        when(NodeUtil.isExpressionResultUsed(n)).thenReturn(false);
        checkSideEffects.visit(t, n, parent);
        verify(t, times(1)).makeError(eq(n), eq(CheckLevel.WARNING), eq(CheckSideEffects.USELESS_CODE_ERROR), contains("The result of the 'add' operator is not being used."));
    }

    // Test visit() method with side-effect free statement
    @Test
    public void testVisitSideEffectFreeStatement() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandParent = mock(Node.class);
        when(n.isExprResult()).thenReturn(true);
        when(NodeUtil.isStatement(n)).thenReturn(true);
        when(NodeUtil.isSimpleOperatorType(n.getType())).thenReturn(false);
        when(NodeUtil.mayHaveSideEffects(n, compiler)).thenReturn(false);
        when(n.getAncestor(anyInt())).thenReturn(grandParent);
        when(grandParent.getType()).thenReturn(Token.BLOCK);
        checkSideEffects.visit(t, n, parent);
        verify(t, times(1)).makeError(eq(n), eq(CheckLevel.WARNING), eq(CheckSideEffects.USELESS_CODE_ERROR), contains("This code lacks side-effects. Is there a bug?"));
        verify(problemNodes, times(1)).add(eq(n));
    }

    // Test protectSideEffects() method
    @Test
    public void testProtectSideEffects() {
        Node node = mock(Node.class);
        Node name = mock(Node.class);
        Node replacement = mock(Node.class);
        checkSideEffects.problemNodes.add(node);
        when(IR.name(CheckSideEffects.PROTECTOR_FN)).thenReturn(name);
        when(IR.call(name)).thenReturn(replacement);
        when(node.getParent()).thenReturn(mock(Node.class));
        checkSideEffects.protectSideEffects();
        verify(compiler, times(1)).reportCodeChange();
    }

    // Test addExtern() method
    @Test
    public void testAddExtern() {
        Node name = mock(Node.class);
        Node var = mock(Node.class);
        JSDocInfoBuilder builder = mock(JSDocInfoBuilder.class);
        CompilerInput input = mock(CompilerInput.class);
        when(IR.name(CheckSideEffects.PROTECTOR_FN)).thenReturn(name);
        when(IR.var(name)).thenReturn(var);
        when(compiler.getSynthesizedExternsInput()).thenReturn(input);
        when(input.getAstRoot(compiler)).thenReturn(mock(Node.class));

        checkSideEffects.addExtern();
        verify(name, times(1)).putBooleanProp(Node.IS_CONSTANT_NAME, true);
        verify(builder, times(1)).recordNoAlias();
        verify(var, times(1)).setJSDocInfo(any());
        verify(mock(Node.class), times(1)).addChildrenToBack(var);
        verify(compiler, times(1)).reportCodeChange();
    }

    // Test visit() method of StripProtection CompilerPass
    @Test
    public void testStripProtectionVisit() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node target = mock(Node.class);
        Node expr = mock(Node.class);
        CompilerPass stripProtection = new CheckSideEffects.StripProtection(compiler);
        when(n.isCall()).thenReturn(true);
        when(n.getFirstChild()).thenReturn(target);
        when(target.isName()).thenReturn(true);
        when(target.getString()).thenReturn(CheckSideEffects.PROTECTOR_FN);
        when(n.getLastChild()).thenReturn(expr);
        when(n.detachChildren()).thenReturn(expr);
        checkSideEffects.visit(t, n, parent);
        assertEquals(expr, parent);
    }
}