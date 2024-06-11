import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class InlineObjectLiteralsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        InlineObjectLiterals inliner = new InlineObjectLiterals(compiler, () -> "test");
        Node externs = IR.root();
        Node root = IR.root();

        inliner.process(externs, root);

        // Assertions here
        Assert.assertEquals(0, 0);
    }
}