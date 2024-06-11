import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NameRefInExternsCheck;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VarCheck;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class VarCheckTest {
    private VarCheck varCheck;
    private Compiler compiler;
    private Node externs;
    private Node root;
    private NodeTraversal traversal;
    private List<Node> traversalRoots;

    @Before
    public void setUp() {
        compiler = mock(Compiler.class);
        externs = mock(Node.class);
        root = mock(Node.class);
        traversal = mock(NodeTraversal.class);
        traversalRoots = new ArrayList<>();

        varCheck = new VarCheck(compiler);
    }

    @Test
    public void process_WithSanityCheckMode_DoesNotRunExternsChecking() {
        varCheck = new VarCheck(compiler, true);

        varCheck.process(externs, root);

        verifyNoInteractions(compiler);
        verifyZeroInteractions(externs, root);
    }

    @Test
    public void process_CallsTraverseRootsOnTraversalWithExternsAndRootNodes() {
        traversalRoots.add(externs);
        traversalRoots.add(root);
        when(compiler.getErrorLevel(ArgumentMatchers.any(JSError.class))).thenReturn(null);

        varCheck.process(externs, root);

        verify(compiler).getErrorLevel(JSError.make("", 0, 0, VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
        verify(compiler).getModuleGraph();
        verify(traversal).traverseRoots(compiler, traversalRoots, varCheck);
        verifyZeroInteractions(externs, root);
    }

    @Test
    public void visit_WithNameNode_CallsTraverseGlobalIfTheVarIsDefinedInExterns() {
        Node nameNode = mock(Node.class);
        Node parent = mock(Node.class);
        NodeTraversal t = mock(NodeTraversal.class);
        Node varNode = mock(Node.class);
        Scope.Var var = mock(Scope.Var.class);

        String varName = "testVar";
        when(nameNode.getType()).thenReturn(Token.NAME);
        when(nameNode.getString()).thenReturn(varName);
        when(varNode.getType()).thenReturn(Token.VAR);
        when(varNode.getFirstChild()).thenReturn(nameNode);
        when(parent.getType()).thenReturn(Token.VAR);
        when(parent.getFirstChild()).thenReturn(varNode);
        when(t.getScope()).thenReturn(mock(Scope.class));
        when(t.getScope().getVar(varName)).thenReturn(var);
        when(var.input).thenReturn(mock(CompilerInput.class));
        when(t.getInput()).thenReturn(mock(CompilerInput.class));

        varCheck.visit(t, nameNode, parent);

        verify(t).getInput();
        verify(var).input;
        verify(t.getScope()).getVar(varName);
        verifyZeroInteractions(compiler, externs, root, traversal);
    }

    @Test
    public void visit_WithNameNode_CallsTraverseGlobalIfTheVarIsNotDefinedInCurrentScopeAndInStrictExternCheckMode() {
        Node nameNode = mock(Node.class);
        Node parent = mock(Node.class);
        NodeTraversal t = mock(NodeTraversal.class);
        Node varNode = mock(Node.class);
        Scope.Var var = mock(Scope.Var.class);

        String varName = "testVar";
        when(nameNode.getType()).thenReturn(Token.NAME);
        when(nameNode.getString()).thenReturn(varName);
        when(varNode.getType()).thenReturn(Token.VAR);
        when(varNode.getFirstChild()).thenReturn(nameNode);
        when(parent.getType()).thenReturn(Token.VAR);
        when(parent.getFirstChild()).thenReturn(varNode);
        when(t.getScope()).thenReturn(mock(Scope.class));
        when(t.getScope().getVar(varName)).thenReturn(null);
        when(var.input).thenReturn(mock(CompilerInput.class));

        when(compiler.getErrorLevel(VarCheck.UNDEFINED_EXTERN_VAR_ERROR)).thenReturn(CheckLevel.ERROR);
        varCheck = new VarCheck(compiler, false);

        varCheck.visit(t, nameNode, parent);

        verify(compiler).getErrorLevel(VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
        verify(t, times(2)).getScope();
        verify(t.getScope()).getVar(varName);
        verifyZeroInteractions(externs, root, traversal);
    }

    // TODO: Add more test cases as necessary

}