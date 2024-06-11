
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {
    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @Test
    public void testProcess_emptyRoot() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        List<String> expectedCollapsedVariables = Lists.newArrayList();

        collapseVariableDeclarations.process(externs, root);
        List<String> actualCollapsedVariables = collapseVariableDeclarations.getCollapsedVariables();

        assertEquals(expectedCollapsedVariables, actualCollapsedVariables);
    }
    
    @Test
    public void testProcess_noVariableDeclarations() {
        Node externs = new Node(Token.SCRIPT);
        
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "a"));
        Node root = new Node(Token.SCRIPT, varNode);
        
        List<String> expectedCollapsedVariables = Lists.newArrayList();

        collapseVariableDeclarations.process(externs, root);
        List<String> actualCollapsedVariables = collapseVariableDeclarations.getCollapsedVariables();

        assertEquals(expectedCollapsedVariables, actualCollapsedVariables);
    }
    
    @Test
    public void testProcess_collapseVariableDeclarations() {
        Node externs = new Node(Token.SCRIPT);

        Node varNode1 = new Node(Token.VAR, new Node(Token.NAME, "a"));
        Node varNode2 = new Node(Token.VAR, new Node(Token.NAME, "b"), new Node(Token.ASSIGN, new Node(Token.FALSE)));
        Node varNode3 = new Node(Token.VAR, new Node(Token.NAME, "c"), new Node(Token.ASSIGN, new Node(Token.TRUE)));
        
        Node root = new Node(Token.SCRIPT, varNode1, varNode2, varNode3);
        
        List<String> expectedCollapsedVariables = Lists.newArrayList("a", "b = false", "c = true");

        collapseVariableDeclarations.process(externs, root);
        List<String> actualCollapsedVariables = collapseVariableDeclarations.getCollapsedVariables();

        assertEquals(expectedCollapsedVariables, actualCollapsedVariables);
    }
    
    @Test
    public void testProcess_collapseVariableAssignments() {
        Node externs = new Node(Token.SCRIPT);

        Node assignNode1 = new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.FALSE));
        Node assignNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.FALSE));
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "c"), assignNode1, assignNode2);
        
        Node root = new Node(Token.SCRIPT, varNode);
        
        List<String> expectedCollapsedVariables = Lists.newArrayList("c = b = a = false");

        collapseVariableDeclarations.process(externs, root);
        List<String> actualCollapsedVariables = collapseVariableDeclarations.getCollapsedVariables();

        assertEquals(expectedCollapsedVariables, actualCollapsedVariables);
    }
}
