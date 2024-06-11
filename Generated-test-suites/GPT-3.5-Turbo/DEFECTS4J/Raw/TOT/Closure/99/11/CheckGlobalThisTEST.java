import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compilerMock;
    private NodeTraversal.Callback callbackMock;

    @Before
    public void setUp() {
        compilerMock = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
        callbackMock = new NodeTraversal.Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
            }
        };
    }

    @Test
    public void shouldNotTraverseFunctionWithConstructorAnnotation() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        jsDocInfoMock.recordConstructor();
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(jsDocInfoMock);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null));
    }

    @Test
    public void shouldNotTraverseFunctionWithThisTypeAnnotation() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        jsDocInfoMock.recordThisType();
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(jsDocInfoMock);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null));
    }

    @Test
    public void shouldNotTraverseFunctionWithOverrideAnnotation() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        jsDocInfoMock.recordOverride();
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(jsDocInfoMock);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null));
    }

    @Test
    public void shouldNotTraverseFunctionInBlock() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node blockNode = new Node(Token.BLOCK);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME, functionNode);
        blockNode.addChildToFront(parent);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent));
    }

    @Test
    public void shouldNotTraverseFunctionInScript() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME, functionNode);
        scriptNode.addChildToFront(parent);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent));
    }

    @Test
    public void shouldNotTraverseFunctionWithName() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node nameNode = new Node(Token.NAME);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME, functionNode);
        nameNode.addChildToFront(parent);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent));
    }

    @Test
    public void shouldNotTraverseFunctionInAssignment() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node assignNode = new Node(Token.ASSIGN);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME, functionNode);
        assignNode.addChildToFront(parent);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent));
    }

    @Test
    public void shouldTraverseFunctionWithNoSpecialConditions() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node functionNode = new Node(Token.FUNCTION);

        assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null));
    }

    @Test
    public void shouldReportGlobalThisOnLeftSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel checkLevel = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, checkLevel);
        NodeTraversal.Callback callback = new NodeTraversal.Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                // Do nothing
            }
        };

        NodeTraversal nodeTraversal = new NodeTraversal(compiler, callback);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.THIS);
        Node rhsNode = new Node(Token.NAME);
        assignNode.addChildToFront(lhsNode);
        assignNode.addChildToBack(rhsNode);

        lhsNode.setJSDocInfo(jsDocInfoMock);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, lhsNode, assignNode));
        checkGlobalThis.visit(nodeTraversal, lhsNode, assignNode);

        String expectedErrorMessage = "dangerous use of the global 'this' object";
        assertTrue(compiler.getErrors().get(0).getDescription().equals(expectedErrorMessage));
        assertEquals(checkLevel, compiler.getErrors().get(0).getLevel());
    }

    @Test
    public void shouldTraverseFunctionWithoutGlobalThisOnLeftSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel checkLevel = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, checkLevel);
        NodeTraversal.Callback callback = new NodeTraversal.Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                // Do nothing
            }
        };

        NodeTraversal nodeTraversal = new NodeTraversal(compiler, callback);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.THIS);
        Node rhsNode = new Node(Token.NAME);
        assignNode.addChildToFront(rhsNode);
        assignNode.addChildToBack(lhsNode);

        lhsNode.setJSDocInfo(jsDocInfoMock);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, lhsNode, assignNode));
        checkGlobalThis.visit(nodeTraversal, lhsNode, assignNode);

        assertEquals(0, compiler.getErrors().size());
    }

    @Test
    public void shouldTraverseFunctionWithGlobalThisOnRightSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel checkLevel = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, checkLevel);
        NodeTraversal.Callback callback = new NodeTraversal.Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                // Do nothing
            }
        };

        NodeTraversal nodeTraversal = new NodeTraversal(compiler, callback);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.NAME);
        Node rhsNode = new Node(Token.THIS);
        assignNode.addChildToBack(lhsNode);
        assignNode.addChildToFront(rhsNode);

        lhsNode.setJSDocInfo(jsDocInfoMock);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, lhsNode, assignNode));
        checkGlobalThis.visit(nodeTraversal, lhsNode, assignNode);

        assertEquals(0, compiler.getErrors().size());
    }

    @Test
    public void shouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.GETPROP);
        Node rhsNode = new Node(Token.NAME);
        lhsNode.addChildToBack(rhsNode);
        lhsNode.addChildToBack(new Node(Token.STRING, "prototype"));
        assignNode.addChildToFront(lhsNode);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, rhsNode, assignNode));
    }

    @Test
    public void shouldNotTraverseRightSideOfAssignmentToSubproperty() {
        NodeTraversal nodeTraversal = new NodeTraversal(compilerMock, callbackMock);
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.NAME);
        Node rhsNode = new Node(Token.GETPROP);
        rhsNode.addChildToBack(lhsNode);
        rhsNode.addChildToBack(new Node(Token.STRING, "prototype"));
        assignNode.addChildToFront(rhsNode);

        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, lhsNode, assignNode));
    }

    @Test
    public void shouldReportGlobalThisWithPropertyAccess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel checkLevel = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, checkLevel);
        NodeTraversal.Callback callback = new NodeTraversal.Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                // Do nothing
            }
        };

        NodeTraversal nodeTraversal = new NodeTraversal(compiler, callback);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        Node parent = new Node(Token.GETPROP);
        Node thisNode = new Node(Token.THIS);
        parent.addChildToBack(thisNode);

        assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, thisNode, parent));
        checkGlobalThis.visit(nodeTraversal, thisNode, parent);

        String expectedErrorMessage = "dangerous use of the global 'this' object";
        assertTrue(compiler.getErrors().get(0).getDescription().equals(expectedErrorMessage));
        assertEquals(checkLevel, compiler.getErrors().get(0).getLevel());
    }

    @Test
    public void shouldNotReportThisWithoutPropertyAccess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel checkLevel = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, checkLevel);
        NodeTraversal.Callback callback = new NodeTraversal.Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                // Do nothing
            }
        };

        NodeTraversal nodeTraversal = new NodeTraversal(compiler, callback);
        JSDocInfo jsDocInfoMock = new JSDocInfo();
        Node parent = new Node(Token.NAME);
        Node thisNode = new Node(Token.THIS);
        parent.addChildToBack(thisNode);

        assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, thisNode, parent));
        checkGlobalThis.visit(nodeTraversal, thisNode, parent);

        assertEquals(0, compiler.getErrors().size());
    }
}