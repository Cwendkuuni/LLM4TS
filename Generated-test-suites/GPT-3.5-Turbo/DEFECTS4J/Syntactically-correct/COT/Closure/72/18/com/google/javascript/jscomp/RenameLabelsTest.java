import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.RenameLabels;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RenameLabelsTest {
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        compiler = new Compiler();
    }

    @Test
    public void testRenameLabels() {
        String sourceCode = "function bar() {" +
                "a: {" +
                "b: {" +
                "foo();" +
                "}" +
                "}" +
                "" +
                "a: {" +
                "b: break a;" +
                "}" +
                "}";
        compiler.compile(sourceCode);
        RenameLabels renameLabels = new RenameLabels(compiler);
        renameLabels.process(compiler.getRoot(), compiler.getRoot());

        // Assert statements for the renamed labels
        assertEquals("a", compiler.getRoot().getFirstChild().getFirstChild().getString());
        assertEquals("b", compiler.getRoot().getFirstChild().getFirstChild().getFirstChild().getString());
        assertEquals("a", compiler.getRoot().getLastChild().getFirstChild().getString());
        assertEquals("b", compiler.getRoot().getLastChild().getFirstChild().getFirstChild().getString());

        // Assert statements for the removed labels
        assertEquals(4, compiler.getRoot().getLastChild().getChildCount());
        assertEquals("b", compiler.getRoot().getLastChild().getFirstChild().getFirstChild().getString());
    }
}