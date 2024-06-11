import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
    private NodeTraversal nodeTraversal;
    private Scope scope;

    @Before
    public void setUp() {
        nodeTraversal = Mockito.mock(NodeTraversal.class);
        scope = Mockito.mock(Scope.class);
        makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        makeDeclaredNamesUnique.setScope(scope);
        makeDeclaredNamesUnique.setNodeTraversal(nodeTraversal);
    }

    @Test
    public void testEnterScope_GlobalScope() {
        makeDeclaredNamesUnique.enterScope(nodeTraversal);
        assertEquals(makeDeclaredNamesUnique.rootRenamer, makeDeclaredNamesUnique.getNameStack().peek());
    }

    @Test
    public void testEnterScope_FunctionScope() {
        Node functionNode = Mockito.mock(Node.class);
        Node functionDeclarationNode = Mockito.mock(Node.class);
        Node functionBodyNode = Mockito.mock(Node.class);

        when(functionNode.getType()).thenReturn(Token.FUNCTION);
        when(functionNode.getFirstChild()).thenReturn(functionDeclarationNode);
        when(functionDeclarationNode.getLastChild()).thenReturn(functionBodyNode);

        makeDeclaredNamesUnique.enterScope(nodeTraversal);
        assertEquals(makeDeclaredNamesUnique.rootRenamer, makeDeclaredNamesUnique.getNameStack().peek());

        makeDeclaredNamesUnique.findDeclaredNames(functionBodyNode, functionNode, makeDeclaredNamesUnique.getNameStack().peek());

        Var functionVar = Mockito.mock(Var.class);
        when(functionVar.getName()).thenReturn("functionName");
        when(scope.getVar("functionName")).thenReturn(functionVar);

        makeDeclaredNamesUnique.addDeclaredName("functionName");

        assertEquals("functionName$0", makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("functionName"));
    }

    @Test
    public void testVisit_NameNode_GlobalScope() {
        Node nameNode = Mockito.mock(Node.class);
        when(nameNode.getType()).thenReturn(Token.NAME);
        when(nameNode.getString()).thenReturn("globalName");

        makeDeclaredNamesUnique.visit(nodeTraversal, nameNode, null);
        assertNull(makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("globalName"));
    }

    @Test
    public void testVisit_NameNode_FunctionScope() {
        Node nameNode = Mockito.mock(Node.class);
        when(nameNode.getType()).thenReturn(Token.NAME);
        when(nameNode.getString()).thenReturn("functionName");

        Var functionVar = Mockito.mock(Var.class);
        when(functionVar.getName()).thenReturn("functionName");
        when(scope.getVar("functionName")).thenReturn(functionVar);

        makeDeclaredNamesUnique.getNameStack().push(makeDeclaredNamesUnique.getNameStack().peek());

        makeDeclaredNamesUnique.visit(nodeTraversal, nameNode, null);

        assertEquals("functionName$0", makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("functionName"));
    }

    @Test
    public void testVisit_FunctionNode() {
        Node functionNode = Mockito.mock(Node.class);
        when(functionNode.getType()).thenReturn(Token.FUNCTION);

        makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);
        assertEquals(makeDeclaredNamesUnique.rootRenamer, makeDeclaredNamesUnique.getNameStack().peek());
    }

    @Test
    public void testVisit_CatchNode() {
        Node catchNode = Mockito.mock(Node.class);
        when(catchNode.getType()).thenReturn(Token.CATCH);

        makeDeclaredNamesUnique.visit(nodeTraversal, catchNode, null);
        assertEquals(makeDeclaredNamesUnique.rootRenamer, makeDeclaredNamesUnique.getNameStack().peek());
    }

    @Test(expected = AssertionError.class)
    public void testVisit_DeclarationNode() {
        Node declarationNode = Mockito.mock(Node.class);
        when(declarationNode.getType()).thenReturn(Token.VAR);

        makeDeclaredNamesUnique.visit(nodeTraversal, declarationNode, null);
    }

    @Test
    public void testGetReplacementName_UnknownName() {
        assertEquals(null, makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("unknownName"));
    }

    @Test
    public void testGetReplacementName_KnownName() {
        String oldName = "functionName";
        String newName = "functionName$0";

        makeDeclaredNamesUnique.getNameStack().push(makeDeclaredNamesUnique.getNameStack().peek());
        makeDeclaredNamesUnique.addDeclaredName(oldName);

        assertEquals(newName, makeDeclaredNamesUnique.getNameStack().peek().getReplacementName(oldName));
    }

    @Test
    public void testStripConstIfReplaced() {
        Node nameNode = Mockito.mock(Node.class);
        when(nameNode.getType()).thenReturn(Token.NAME);
        when(nameNode.getProp(Node.IS_CONSTANT_NAME)).thenReturn(true);

        makeDeclaredNamesUnique.setNameStack(new ArrayDeque<>());
        makeDeclaredNamesUnique.setGetReplacementName((name) -> "newName");

        makeDeclaredNamesUnique.visit(nodeTraversal, nameNode, null);

        assertFalse(nameNode.hasProp(Node.IS_CONSTANT_NAME));
    }

    @Test
    public void testFindDeclaredNames_NoDeclarations() {
        Node functionBodyNode = Mockito.mock(Node.class);
        makeDeclaredNamesUnique.findDeclaredNames(functionBodyNode, null, makeDeclaredNamesUnique.getNameStack().peek());
        assertTrue(makeDeclaredNamesUnique.getNameStack().isEmpty());
    }

    @Test
    public void testFindDeclaredNames_OneDeclaration() {
        Node functionBodyNode = Mockito.mock(Node.class);
        Node declarationNode = Mockito.mock(Node.class);
        when(declarationNode.getType()).thenReturn(Token.NAME);
        when(declarationNode.getString()).thenReturn("declaredName");

        makeDeclaredNamesUnique.findDeclaredNames(functionBodyNode, null, makeDeclaredNamesUnique.getNameStack().peek());

        Var declarationVar = Mockito.mock(Var.class);
        when(declarationVar.getName()).thenReturn("declaredName");
        when(scope.getVar("declaredName")).thenReturn(declarationVar);

        makeDeclaredNamesUnique.addDeclaredName("declaredName");

        assertEquals("declaredName$0", makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("declaredName"));
    }

    @Test
    public void testFindDeclaredNames_MultipleDeclarations() {
        Node functionBodyNode = Mockito.mock(Node.class);
        Node declarationNode1 = Mockito.mock(Node.class);
        Node declarationNode2 = Mockito.mock(Node.class);
        when(declarationNode1.getType()).thenReturn(Token.NAME);
        when(declarationNode1.getString()).thenReturn("declaredName1");
        when(declarationNode2.getType()).thenReturn(Token.NAME);
        when(declarationNode2.getString()).thenReturn("declaredName2");

        makeDeclaredNamesUnique.findDeclaredNames(functionBodyNode, null, makeDeclaredNamesUnique.getNameStack().peek());

        Var declarationVar1 = Mockito.mock(Var.class);
        when(declarationVar1.getName()).thenReturn("declaredName1");
        when(scope.getVar("declaredName1")).thenReturn(declarationVar1);

        Var declarationVar2 = Mockito.mock(Var.class);
        when(declarationVar2.getName()).thenReturn("declaredName2");
        when(scope.getVar("declaredName2")).thenReturn(declarationVar2);

        makeDeclaredNamesUnique.addDeclaredName("declaredName1");
        makeDeclaredNamesUnique.addDeclaredName("declaredName2");

        assertEquals("declaredName1$1", makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("declaredName1"));
        assertEquals("declaredName2$2", makeDeclaredNamesUnique.getNameStack().peek().getReplacementName("declaredName2"));
    }
}