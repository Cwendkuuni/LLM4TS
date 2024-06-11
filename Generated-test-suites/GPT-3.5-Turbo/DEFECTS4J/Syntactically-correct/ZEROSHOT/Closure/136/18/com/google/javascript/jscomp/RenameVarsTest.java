import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class RenameVarsTest {
    private RenameVars renameVars;
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        compiler = new Compiler();
        renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    }

    @Test
    public void testProcess() {
        renameVars.process(null, null);
    }

    @Test
    public void testProcess_withExterns() {
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        renameVars.process(externs, root);
    }
}