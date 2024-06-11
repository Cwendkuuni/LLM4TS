import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrepareAstTest {

    @Test
    public void testNormalizeNodeTypes() {
        PrepareAst prepareAst = new PrepareAst(null, true);
        Node rootNode = IR.root();
        Node childNode1 = IR.exprResult(IR.name("x"));
        Node childNode2 = IR.empty();
        rootNode.addChildToBack(childNode1);
        rootNode.addChildToBack(childNode2);
    
        prepareAst.normalizeNodeTypes(rootNode);

        assertEquals(IR.exprResult(IR.name("x")), childNode1);
        assertEquals(IR.empty(), childNode2);
    }

    @Test
    public void testNormalizeBlocks() {
        PrepareAst prepareAst = new PrepareAst(null, true);
        Node rootNode = IR.root();
        Node ifNode = IR.ifNode(IR.trueNode(), IR.exprResult(IR.number(1)), IR.exprResult(IR.number(0)));
        rootNode.addChildToBack(ifNode);

        prepareAst.normalizeBlocks(rootNode);

        assertFalse(ifNode.isBlock());
        assertTrue(ifNode.getFirstChild().isBlock());
    }

    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node objlit = IR.objectlit(
                IR.stringKey("a", IR.string("b"))
        );

        prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

        assertEquals(IR.string("b"), objlit.getFirstChild().getFirstChild());
    }

    @Test
    public void testAnnotateCalls_FreeCall() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node callNode = IR.call(IR.name("foo"), IR.number(1));

        prepareAnnotations.annotateCalls(callNode);

        assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testAnnotateCalls_DirectEval() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node callNode = IR.call(IR.name("eval"));

        prepareAnnotations.annotateCalls(callNode);

        assertTrue(callNode.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));
    }

    @Test
    public void testAnnotateDispatchers() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node functionNode = IR.function(IR.name("foo"), IR.block());
        Node assignmentNode = IR.assign(IR.name("bar"), functionNode);

        prepareAnnotations.annotateDispatchers(functionNode, assignmentNode);

        assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testCompleteProcess() {
        PrepareAst prepareAst = new PrepareAst(null);
        Node rootNode = IR.root();
        Node externsNode = IR.root();
        Node ifNode1 = IR.ifNode(IR.trueNode(), IR.exprResult(IR.number(1)), IR.exprResult(IR.number(0)));
        Node ifNode2 = IR.ifNode(IR.falseNode(), IR.exprResult(IR.number(1)), IR.exprResult(IR.number(0)));
        rootNode.addChildToBack(ifNode1);
        rootNode.addChildToBack(ifNode2);

        prepareAst.process(externsNode, rootNode);

        assertFalse(ifNode1.isBlock());
        assertTrue(ifNode1.getFirstChild().isBlock());
        assertFalse(ifNode2.isBlock());
        assertTrue(ifNode2.getFirstChild().isBlock());
    }
}