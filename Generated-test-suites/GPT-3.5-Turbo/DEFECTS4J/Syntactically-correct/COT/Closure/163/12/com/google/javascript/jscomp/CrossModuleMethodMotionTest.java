import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;
    private Compiler compiler;

    @Before
    public void setUp() {
        compiler = new Compiler();
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    }

    @Test
    public void testConstructor() {
        assertNotNull(crossModuleMethodMotion);
    }

    @Test
    public void testProcess() {
        Node externRoot = IR.root();
        Node root = IR.root(
            IR.exprResult(
                IR.call(
                    IR.name("foo"),
                    IR.nullNode(),
                    IR.number(1))
            )
        );
        crossModuleMethodMotion.process(externRoot, root);
        // Your assert statement here
    }
}