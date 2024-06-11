
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

    @Test
    public void testUndefinedVarError() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }

    @Test
    public void testViolatedModuleDepError() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }

    @Test
    public void testMissingModuleDepWarning() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }

    @Test
    public void testStrictModuleDepError() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }

    @Test
    public void testNameReferenceInExternsWarning() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }

    @Test
    public void testUndefinedExternVarWarning() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }

    @Test
    public void testInvalidFunctionDeclError() {
        VarCheck varCheck = new VarCheck();
        varCheck.process(externs, root);
        // TODO: Add assertion statement(s)
    }
}

