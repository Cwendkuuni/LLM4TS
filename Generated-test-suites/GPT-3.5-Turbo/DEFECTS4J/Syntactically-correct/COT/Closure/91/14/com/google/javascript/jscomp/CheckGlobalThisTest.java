import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        // Test shouldTraverse() method with different inputs
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.OFF);

        // Test when node type is FUNCTION and it has a valid JSDocInfo
        NodeTraversal nodeTraversal1 = new NodeTraversal(null, null);
        Node n1 = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        Node parent1 = null;
        assertEquals(true, checkGlobalThis.shouldTraverse(nodeTraversal1, n1, parent1));

        // Test when node type is FUNCTION and it has an invalid JSDocInfo that includes @constructor
        NodeTraversal nodeTraversal2 = new NodeTraversal(null, null);
        Node n2 = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        n2.setJSDocInfo(new JSDocInfo(true));
        Node parent2 = null;
        assertEquals(false, checkGlobalThis.shouldTraverse(nodeTraversal2, n2, parent2));

        // Test when node type is FUNCTION and it has an invalid JSDocInfo that includes @this
        NodeTraversal nodeTraversal3 = new NodeTraversal(null, null);
        Node n3 = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        JSDocInfo jsDocInfo3 = new JSDocInfo();
        jsDocInfo3.recordThisType(new Node(Token.STRING));
        n3.setJSDocInfo(jsDocInfo3);
        Node parent3 = null;
        assertEquals(false, checkGlobalThis.shouldTraverse(nodeTraversal3, n3, parent3));

        // Test when node type is FUNCTION and it has an invalid JSDocInfo that includes @override
        NodeTraversal nodeTraversal4 = new NodeTraversal(null, null);
        Node n4 = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        n4.setJSDocInfo(new JSDocInfo().setOverride(true));
        Node parent4 = null;
        assertEquals(false, checkGlobalThis.shouldTraverse(nodeTraversal4, n4, parent4));

        // Test when node type is FUNCTION and it is not in a valid place
        NodeTraversal nodeTraversal5 = new NodeTraversal(null, null);
        Node n5 = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        Node parent5 = new Node(Token.NAME);
        assertEquals(false, checkGlobalThis.shouldTraverse(nodeTraversal5, n5, parent5));

        // Test when parent type is ASSIGN and node is part of the left side of the assignment
        NodeTraversal nodeTraversal6 = new NodeTraversal(null, null);
        Node n6 = new Node(Token.THIS);
        Node parent6 = new Node(Token.ASSIGN, n6, new Node());
        checkGlobalThis.visit(nodeTraversal6, n6, parent6);
        Node assignLhsChild6 = null;
        assertEquals(true, checkGlobalThis.shouldTraverse(nodeTraversal6, n6, parent6));

        // Test when parent type is ASSIGN and node is part of the right side of the assignment
        NodeTraversal nodeTraversal7 = new NodeTraversal(null, null);
        Node n7 = new Node(Token.THIS);
        Node parent7 = new Node(Token.ASSIGN, new Node(), n7);
        checkGlobalThis.visit(nodeTraversal7, n7, parent7);
        Node assignLhsChild7 = n7;
        assertEquals(false, checkGlobalThis.shouldTraverse(nodeTraversal7, n7, parent7));
    }

    @Test
    public void testVisit() {
        // Test visit() method with different inputs
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        // Test when shouldReportThis() returns true
        checkGlobalThis.visit(nodeTraversal, n, parent);
        assertEquals(1, compiler.getWarnings().size());

        // Test when shouldReportThis() returns false
        parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(nodeTraversal, n, parent);
        assertEquals(1, compiler.getWarnings().size());
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // Test getFunctionJsDocInfo() method with different inputs
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.OFF);
        Node n = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        Node parent = null;

        // Test when the given node has no JSDocInfo and its parent has a valid JSDocInfo
        Node nameNode1 = new Node(Token.NAME);
        Node assignNode1 = new Node(Token.ASSIGN, nameNode1, n);
        Node varNode1 = new Node(Token.VAR, assignNode1);
        JSDocInfo jsDocInfo1 = new JSDocInfo();
        varNode1.setJSDocInfo(jsDocInfo1);
        assertEquals(jsDocInfo1, checkGlobalThis.getFunctionJsDocInfo(nameNode1));

        // Test when the given node has no JSDocInfo and its parent has no JSDocInfo
        Node nameNode2 = new Node(Token.NAME);
        assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(nameNode2));

        // Test when the given node has a valid JSDocInfo
        Node nameNode3 = new Node(Token.NAME);
        JSDocInfo jsDocInfo3 = new JSDocInfo();
        n.setJSDocInfo(jsDocInfo3);
        assertEquals(jsDocInfo3, checkGlobalThis.getFunctionJsDocInfo(n));

        // Test when the given node has an invalid JSDocInfo that includes @constructor
        Node nameNode4 = new Node(Token.NAME);
        JSDocInfo jsDocInfo4 = new JSDocInfo(true);
        n.setJSDocInfo(jsDocInfo4);
        assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(n));

        // Test when the given node has an invalid JSDocInfo that includes @this
        Node nameNode5 = new Node(Token.NAME);
        JSDocInfo jsDocInfo5 = new JSDocInfo();
        jsDocInfo5.recordThisType(new Node(Token.STRING));
        n.setJSDocInfo(jsDocInfo5);
        assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(n));

        // Test when the given node has an invalid JSDocInfo that includes @override
        Node nameNode6 = new Node(Token.NAME);
        JSDocInfo jsDocInfo6 = new JSDocInfo().setOverride(true);
        n.setJSDocInfo(jsDocInfo6);
        assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(n));
    }
}