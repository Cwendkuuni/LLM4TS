import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RenameLabelsTest {
    @Mock
    AbstractCompiler compiler;

    @Mock
    Supplier<String> nameSupplier;

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        nameSupplier = mock(Supplier.class);
        renameLabels = new RenameLabels(compiler, nameSupplier, true);
    }

    @Test
    public void testProcess() {
        // set up
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        // execute
        renameLabels.process(externs, root);

        // assert
        verify(compiler, times(1)).addToDebugLog(anyString());
        verify(compiler, times(1)).reportCodeChange();
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testProcess_nullRoot() {
        // set up
        Node externs = mock(Node.class);
        Node root = null;

        // execute
        renameLabels.process(externs, root);

        // assert
        verify(compiler, times(0)).addToDebugLog(anyString());
        verify(compiler, times(0)).reportCodeChange();
        verify(compiler, times(0)).reportCodeChange();
    }

    @Test
    public void testProcess_nullExterns() {
        // set up
        Node externs = null;
        Node root = mock(Node.class);

        // execute
        renameLabels.process(externs, root);

        // assert
        verify(compiler, times(0)).addToDebugLog(anyString());
        verify(compiler, times(0)).reportCodeChange();
        verify(compiler, times(0)).reportCodeChange();
    }

    @Test
    public void testProcess_nullExternsAndRoot() {
        // set up
        Node externs = null;
        Node root = null;

        // execute
        renameLabels.process(externs, root);

        // assert
        verify(compiler, times(0)).addToDebugLog(anyString());
        verify(compiler, times(0)).reportCodeChange();
        verify(compiler, times(0)).reportCodeChange();
    }

    @Test
    public void testProcessLabels() {
        // set up
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

        // execute
        processLabels.enterScope(null);
        processLabels.exitScope(null);

        // assert
        LabelNamespace expectedNamespace = processLabels.namespaceStack.peek();
        assertNotNull(expectedNamespace);
        assertEquals(0, expectedNamespace.renameMap.size());

        ArrayList<String> expectedNames = processLabels.names;
        assertNotNull(expectedNames);
        assertEquals(0, expectedNames.size());
    }

    @Test
    public void testShouldTraverse() {
        // set up
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        Node child = mock(Node.class);

        // execute
        when(node.getType()).thenReturn(Token.LABEL);
        when(node.getFirstChild()).thenReturn(child);
        when(child.getString()).thenReturn("test");
        renameLabels.process(nodeTraversal, node, parent);

        // assert
        verify(compiler, times(1)).addToDebugLog(anyString());
    }

    @Test
    public void testShouldTraverse_noFirstChild() {
        // set up
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);

        // execute
        when(node.getType()).thenReturn(Token.LABEL);
        when(node.getFirstChild()).thenReturn(null);
        renameLabels.process(nodeTraversal, node, parent);

        // assert
        verify(compiler, times(0)).addToDebugLog(anyString());
    }

    @Test
    public void testVisit() {
        // set up
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);

        // execute
        when(node.getType()).thenReturn(Token.LABEL);
        renameLabels.process(nodeTraversal, node, parent);

        // assert
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisit_noFirstChild() {
        // set up
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);

        // execute
        when(node.getType()).thenReturn(Token.BREAK);
        renameLabels.process(nodeTraversal, node, parent);

        // assert
        verify(compiler, times(0)).reportCodeChange();
    }

    @Test
    public void testVisitLabel() {
        // set up
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        Node nameNode = mock(Node.class);
        Node newChild = mock(Node.class);

        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("test1");
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());

        // execute
        when(node.getFirstChild()).thenReturn(nameNode);
        when(nameNode.getString()).thenReturn("test");
        when(processLabels.namespaceStack.peek().renameMap.containsKey("test")).thenReturn(true);
        when(processLabels.namespaceStack.peek().renameMap.get("test")).thenReturn(processLabels.new LabelInfo(1));
        when(compiler.reportCodeChange()).thenReturn(true);
        when(node.getLastChild()).thenReturn(newChild);
        when(newChild.getType()).thenReturn(Token.BLOCK);
        renameLabels.processLabels.visitLabel(node, parent);

        // assert
        verify(nameNode, times(1)).setString("test1");
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitLabel_unreferenced() {
        // set up
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        Node nameNode = mock(Node.class);
        Node newChild = mock(Node.class);

        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("test1");
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());

        // execute
        when(node.getFirstChild()).thenReturn(nameNode);
        when(nameNode.getString()).thenReturn("test");
        when(processLabels.namespaceStack.peek().renameMap.containsKey("test")).thenReturn(true);
        when(processLabels.namespaceStack.peek().renameMap.get("test")).thenReturn(processLabels.new LabelInfo(1));
        when(compiler.reportCodeChange()).thenReturn(true);
        when(node.getLastChild()).thenReturn(newChild);
        when(newChild.getType()).thenReturn(Token.BLOCK);
        when(node.removeChild(newChild)).thenReturn(newChild);
        renameLabels.processLabels.visitLabel(node, parent);

        // assert
        verify(node, times(1)).replaceChild(node, newChild);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitBreakOrContinue() {
        // set up
        Node node = mock(Node.class);
        Node nameNode = mock(Node.class);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("test1");
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());

        // execute
        when(node.getFirstChild()).thenReturn(nameNode);
        when(nameNode.getString()).thenReturn("test");
        when(processLabels.namespaceStack.peek().renameMap.containsKey("test")).thenReturn(true);
        when(processLabels.namespaceStack.peek().renameMap.get("test")).thenReturn(processLabels.new LabelInfo(1));
        when(compiler.reportCodeChange()).thenReturn(true);
        renameLabels.processLabels.visitBreakOrContinue(node);

        // assert
        verify(nameNode, times(1)).setString("test1");
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testGetNameForId() {
        // set up
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("test1");

        // execute
        String result = processLabels.getNameForId(1);

        // assert
        assertEquals("test1", result);
    }

    @Test
    public void testGetLabelInfo() {
        // set up
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("test1");
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());
        processLabels.namespaceStack.peek().renameMap.put("test", processLabels.new LabelInfo(1));

        // execute
        RenameLabels.LabelInfo result = processLabels.getLabelInfo("test");

        // assert
        assertEquals(1, result.id);
    }
}