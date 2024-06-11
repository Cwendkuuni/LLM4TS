import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrepareAstTest {

    @Test
    public void testNormalizeBlocks() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node n = IR.block(IR.exprResult(IR.number(1)));
        PrepareAst prepareAst = new PrepareAst(compiler);
        prepareAst.normalizeBlocks(n);
        
        assertEquals(n, n.getFirstChild());
        assertTrue(n.isBlock());
        assertFalse(n.isEmpty());
    }

    @Test
    public void testNormalizeNodeTypes() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node n = IR.block(IR.exprResult(IR.number(1)));
        PrepareAst prepareAst = new PrepareAst(compiler);
        prepareAst.normalizeNodeTypes(n);
        
        assertEquals(n, n.getFirstChild());
        assertTrue(n.isBlock());
        assertFalse(n.isEmpty());
    }

    @Test
    public void testPrepareAnnotations() {
        AbstractCompiler compiler = new AbstractCompiler();
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        NodeTraversal.Callback callback = prepareAnnotations;

        Node objlit = IR.objectlit();
        Node key = IR.string("a");
        Node value = IR.string("b");
        key.addChildToBack(value);
        objlit.addChildToBack(key);
        callback.shouldTraverse(new NodeTraversal(compiler), objlit, null);
        assertNull(key.getJSDocInfo());
        assertNull(value.getJSDocInfo());

        Node call = IR.call(IR.name("f"));
        callback.visit(new NodeTraversal(compiler), call, null);
        assertFalse(call.getBooleanProp(Node.FREE_CALL));

        Node eval = IR.name("eval");
        eval.addChildToFront(IR.number(0));
        Node evalCall = IR.call(eval);
        callback.visit(new NodeTraversal(compiler), evalCall, null);
        assertTrue(eval.getBooleanProp(Node.DIRECT_EVAL));

        Node func = IR.function(IR.name("f"));
        Node assign = IR.assign(IR.name("x"), func);
        Node assignInParent = IR.exprResult(assign);
        Node parent = IR.block(assignInParent);
        NodeTraversal traversal = new NodeTraversal(compiler);
        traversal.traverse(parent);
        callback.visit(traversal, func, assignInParent);
        assertTrue(func.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        PrepareAst prepareAst = new PrepareAst(compiler);
        Node externs = null;
        Node root = IR.block();
        prepareAst.process(externs, root);
        assertNull(compiler.getErrorManager().getLastError());
    }
}