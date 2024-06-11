import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {

    @Test
    public void testProcess() {
        String code = "function testFunc(x) { if (x == 1) { return 1; } else { return 2; } }";
        CompilerOptions options = new CompilerOptions();
        options.setLanguage(CompilerOptions.LanguageMode.ECMASCRIPT6);
        Compiler compiler = new Compiler();
        compiler.disableThreads();
        compiler.initOptions(options);

        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        pass.process(externs, root);

        assertEquals("function testFunc(x) { if (x == 1) { return 1; } else { return 2; } }",
                compiler.toSource(root));
    }

    @Test
    public void testProcessWithDeadCode() {
        String code = "function testFunc(x) { if (x == 1) { return 1; alert(1); } else { return 2; alert(2); } }";
        CompilerOptions options = new CompilerOptions();
        options.setLanguage(CompilerOptions.LanguageMode.ECMASCRIPT6);
        Compiler compiler = new Compiler();
        compiler.disableThreads();
        compiler.initOptions(options);

        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        pass.process(externs, root);

        assertEquals("function testFunc(x) { if (x == 1) { return 1; null; } else { return 2; alert(2); } }",
                compiler.toSource(root));
    }

    @Test
    public void testProcessWithNoOpStatements() {
        String code = "function testFunc() { var x; var y = 10; }";
        CompilerOptions options = new CompilerOptions();
        options.setLanguage(CompilerOptions.LanguageMode.ECMASCRIPT6);
        Compiler compiler = new Compiler();
        compiler.disableThreads();
        compiler.initOptions(options);

        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        pass.process(externs, root);

        assertEquals("function testFunc() { var x; null; }",
                compiler.toSource(root));
    }

    @Test
    public void testProcessWithTryCatchBlock() {
        String code = "function testFunc() { try { console.log('try block'); } catch (e) { console.error('error: ' + e); } }";
        CompilerOptions options = new CompilerOptions();
        options.setLanguage(CompilerOptions.LanguageMode.ECMASCRIPT6);
        Compiler compiler = new Compiler();
        compiler.disableThreads();
        compiler.initOptions(options);

        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(code);
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        pass.process(externs, root);

        assertEquals("function testFunc() { try { console.log('try block'); } catch (e) { console.error('error: ' + e); } }",
                compiler.toSource(root));
    }

}