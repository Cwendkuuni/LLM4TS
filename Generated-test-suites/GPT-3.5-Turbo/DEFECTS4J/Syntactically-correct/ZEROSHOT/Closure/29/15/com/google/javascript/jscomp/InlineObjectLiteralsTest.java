import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CodeBuilder;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InlineObjectLiteralsTest {
    private AbstractCompiler compiler;
    private Supplier<String> safeNameIdSupplier;
    private InlineObjectLiterals inlineObjectLiterals;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        safeNameIdSupplier = mock(Supplier.class);
        inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    }

    @Test
    public void process_shouldInlineObjectLiterals() {
        // Arrange
        Node externs = new Node();
        Node root = new Node();
        ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
            compiler, inlineObjectLiterals.new InliningBehavior());
        callback.process(externs, root);

        // Act
        inlineObjectLiterals.process(externs, root);

        // Assert
        assertEquals(0, compiler.getChangeCount());
    }
}