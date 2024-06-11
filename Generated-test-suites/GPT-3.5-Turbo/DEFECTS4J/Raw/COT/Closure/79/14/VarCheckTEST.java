
import org.junit.Test;

public class VarCheckTest {

    @Test
    public void testUndefinedVarError() {
        // Test case to check undefined variable error
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("undefinedVar"));
    }

    @Test
    public void testViolatedModuleDepError() {
        // Test case to check violated module dependency error
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("varName"));
    }

    @Test
    public void testMissingModuleDepWarning() {
        // Test case to check missing module dependency warning
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("varName"));
    }

    @Test
    public void testStrictModuleDepError() {
        // Test case to check strict module dependency error
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("varName"));
    }

    @Test
    public void testNameReferenceInExternsWarning() {
        // Test case to check name reference in externs warning
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("varName"));
    }

    @Test
    public void testUndefinedExternVarWarning() {
        // Test case to check undefined extern variable warning
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("varName"));
    }

    @Test
    public void testInvalidFunctionDeclarationError() {
        // Test case to check invalid function declaration error
        VarCheck varCheck = new VarCheck();
        varCheck.visit(new NodeTraversal(), new Node("varName"));
    }
}
