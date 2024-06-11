/*

JUnit 4 Test Cases for ScopedAliases class

*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ErrorManager;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScopedAliasesTest {
    private CompilerOptions options;
    private Compiler compiler;

    @Before
    public void setup() {
        options = new CompilerOptions();
        options.setLanguageIn(LanguageMode.ECMASCRIPT6_STRICT);
        options.setLanguageOut(LanguageMode.ECMASCRIPT5);
        options.setWarningLevel(ErrorManager.WARNING_LEVEL_QUIET);

        compiler = new Compiler();
        compiler.initOptions(options);
    }

    @Test
    public void testProcess() {
        ScopedAliases aliased = new ScopedAliases(compiler, null, null);
        aliased.process(null, null);

        // Assertions here
    }

    @Test
    public void testHotSwapScript() {
        ScopedAliases aliased = new ScopedAliases(compiler, null, null);
        aliased.hotSwapScript(null, null);

        // Assertions here
    }

    @Test
    public void testEnterScope() {
        ScopedAliases aliased = new ScopedAliases(compiler, null, null);
        NodeTraversal t = new NodeTraversal(compiler, null);
        aliased.enterScope(t);

        // Assertions here
    }

    @Test
    public void testExitScope() {
        ScopedAliases aliased = new ScopedAliases(compiler, null, null);
        NodeTraversal t = new NodeTraversal(compiler, null);
        aliased.exitScope(t);

        // Assertions here
    }

    @Test
    public void testShouldTraverse() {
        ScopedAliases aliased = new ScopedAliases(compiler, null, null);
        NodeTraversal t = new NodeTraversal(compiler, null);
        boolean result = aliased.shouldTraverse(t, null, null);

        // Assertion here
    }

    @Test
    public void testVisit() {
        ScopedAliases aliased = new ScopedAliases(compiler, null, null);
        NodeTraversal t = new NodeTraversal(compiler, null);
        aliased.visit(t, null, null);

        // Assertions here
    }
}