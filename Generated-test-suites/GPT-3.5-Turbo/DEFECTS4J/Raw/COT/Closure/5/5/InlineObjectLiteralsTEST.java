import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
import com.google.javascript.jscomp.*;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.javascript.rhino.Node;


public class InlineObjectLiteralsTest {

    private AbstractCompiler compiler;
    private CompilerPass pass;

    public InlineObjectLiteralsTest() {
        compiler = new Compiler();
        compiler.initOptions(new CompilerOptions());
        
        Supplier<String> safeNameIdSupplier = Suppliers.ofInstance("");
        pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
        
        compiler.addCustomPass(NodeTraversal.ScopedCallback.class, pass);
      
    }

    @Test
    public void testProcess() {
        Node externs = parse("var externs;");
        Node root = parse("function root(){}");

        pass.process(externs, root);

        Iterator<Node> externsIt = externs.children().iterator();
        Iterator<Node> rootIt = root.children().iterator();

        assertTrue(externsIt.next().getString().equals("externs"));
        assertTrue(rootIt.next().getString().equals("root"));
    }

    private Node parse(String js) {
        return compiler.parseTestCode(js);
    }
}

