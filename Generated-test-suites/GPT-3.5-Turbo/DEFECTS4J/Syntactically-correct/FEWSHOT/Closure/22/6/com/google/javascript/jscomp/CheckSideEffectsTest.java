import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    }

    @Test
    public void testProcess() {
        Node externs = IR.empty();
        Node root = IR.empty();
        checkSideEffects.process(externs, root);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testHotSwapScript() {
        Node scriptRoot = IR.empty();
        Node originalRoot = IR.empty();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisit() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        checkSideEffects.visit(t, n, parent);
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testProtectSideEffects() {
        List<Node> problemNodes = mock(List.class);
        when(problemNodes.isEmpty()).thenReturn(false);
        checkSideEffects.protectSideEffects();
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testAddExtern() {
        Node name = IR.name(CheckSideEffects.PROTECTOR_FN);
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        JSDocInfoBuilder builder = new JSDocInfoBuilder(false);
        builder.recordNoAlias();
        name.setJSDocInfo(builder.build(name));
        CompilerInput input = mock(CompilerInput.class);
        when(compiler.getSynthesizedExternsInput()).thenReturn(input);
        when(input.getAstRoot(compiler)).thenReturn(IR.empty());
        checkSideEffects.addExtern();
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testStripProtection() {
        Node externs = IR.empty();
        Node root = IR.empty();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.process(externs, root);
        verify(compiler, times(1)).reportCodeChange();
    }
}