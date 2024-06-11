import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import junit.framework.TestCase;
import org.junit.Test;

public class CheckSideEffectsTest extends TestCase {

@Test
public void testVisit() {
CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
Node n = IR.exprResult(IR.assign(IR.name("x"), IR.number(5)));
NodeTraversal t = new NodeTraversal(null, checkSideEffects);
checkSideEffects.visit(t, n, null);
// assert statements here
}

@Test
public void testHotSwapScript() {
CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
Node scriptRoot = IR.script();
Node originalRoot = IR.script();
NodeTraversal t = new NodeTraversal(null, checkSideEffects);
checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
// assert statements here
}

@Test
public void testProcess() {
CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
Node externs = IR.script();
Node root = IR.script();
checkSideEffects.process(externs, root);
// assert statements here
}

}