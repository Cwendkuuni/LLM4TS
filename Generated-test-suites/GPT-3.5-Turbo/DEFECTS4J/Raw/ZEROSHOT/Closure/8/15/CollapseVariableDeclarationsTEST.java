import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        Node externs = new Node();
        Node root = new Node();

        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

        collapseVariableDeclarations.process(externs, root);

        // Test if the collapses list is empty after processing
        assertEquals(0, collapseVariableDeclarations.collapses.size());
    }
}