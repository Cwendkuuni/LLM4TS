import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScopedAliasesTest {
    @Test
    public void testProcess() {
        // create an instance of ScopedAliases
        ScopedAliases scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new CompilerOptions.AliasTransformationHandler() {
            @Override
            public CompilerOptions.AliasTransformation logAliasTransformation(String s, SourcePosition<ScopedAliases.AliasTransformation> sourcePosition) {
                return null;
            }
        });

        // create and traverse the root node
        Node root = new Node(null);
        Node externs = new Node(null);
        scopedAliases.traverse(externs, root);

        // check if there are any errors
        boolean hasErrors = scopedAliases.hasErrors();
        assertEquals(false, hasErrors);
    }
}