import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class VarCheckTest {

    private static final String VAR_NAME = "testVar";
    private Jscomp jscomp;

    private static final DiagnosticType UNDEFINED_VAR_ERROR = DiagnosticType.error(
            "JSC_UNDEFINED_VARIABLE",
            "variable {0} is undefined");

    @Before
    public void setUp() {
        jscomp = new Jscomp();
    }

    @Test
    public void testProcess_VarNameUndefined_ErrorThrown() {
        VarCheck varCheck = new VarCheck(jscomp);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.EXPR_RESULT);
        Compiler compiler = new Compiler();
        CompilerInput input = new CompilerInput.JsInput(
                "test.js", false, root, CompilerOptions.NULL_OPTIONS);
        input.setCompiler(compiler);
        input.setModule(new JSModule("test.js"));
        compiler.init(externs, new JSModule("test.js", true));
        compiler.addInput(input);

        try {
            varCheck.process(externs, root);
        } catch (IllegalStateException e) {
            Assert.assertEquals("Unexpected variable " + VAR_NAME, e.getMessage());
            return;
        }
        Assert.fail("Expected IllegalStateException to be thrown.");
    }

    @Test
    public void testVisit_VarNameDeclaration_NoExceptionThrown() {
        VarCheck varCheck = new VarCheck(jscomp, true);
        Node varNode = new Node(Token.NAME, VAR_NAME);
        Node varStatement = new Node(Token.VAR, varNode);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.EXPR_RESULT, varStatement);
        Compiler compiler = new Compiler();
        CompilerInput input = new CompilerInput.JsInput(
                "test.js", false, root, CompilerOptions.NULL_OPTIONS);
        input.setCompiler(compiler);
        input.setModule(new JSModule("test.js"));
        compiler.init(externs, new JSModule("test.js", true));
        compiler.addInput(input);

        try {
            varCheck.process(externs, root);
        } catch (IllegalStateException e) {
            Assert.fail("Unexpected IllegalStateException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisit_VarNameAlreadyDeclared_NoExceptionThrown() {
        VarCheck varCheck = new VarCheck(jscomp, true);
        varCheck.getSynthesizedExternsRoot().addChildToBack(
                new Node(Token.VAR, new Node(Token.NAME, VAR_NAME)));
        Node varNode = new Node(Token.NAME, VAR_NAME);
        Node varStatement = new Node(Token.VAR, varNode);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.EXPR_RESULT, varStatement);
        Compiler compiler = new Compiler();
        CompilerInput input = new CompilerInput.JsInput(
                "test.js", false, root, CompilerOptions.NULL_OPTIONS);
        input.setCompiler(compiler);
        input.setModule(new JSModule("test.js"));
        compiler.init(externs, new JSModule("test.js", true));
        compiler.addInput(input);

        try {
            varCheck.process(externs, root);
        } catch (IllegalStateException e) {
            Assert.fail("Unexpected IllegalStateException was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisit_NameReferenceInExterns_ErrorMessageDisplayed() {
        VarCheck varCheck = new VarCheck(jscomp);
        varCheck.getSynthesizedExternsRoot().addChildToBack(
                new Node(Token.VAR, new Node(Token.NAME, VAR_NAME)));
        Node varNode = new Node(Token.NAME, VAR_NAME);
        Node varStatement = new Node(Token.EXPR_RESULT, varNode);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK, varStatement));
        Node root = new Node(Token.EXPR_RESULT, varStatement);
        Compiler compiler = new Compiler();
        CompilerInput input = new CompilerInput.JsInput(
                "test.js", false, root, CompilerOptions.NULL_OPTIONS);
        input.setCompiler(compiler);
        input.setModule(new JSModule("test.js"));
        compiler.init(externs, new JSModule("test.js", true));
        compiler.addInput(input);

        try {
            varCheck.process(externs, root);
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Unexpected variable"));
        }
    }

    @Test
    public void testVisit_NameReferenceInExterns_AddToVarsToDeclareInExterns() {
        VarCheck varCheck = new VarCheck(jscomp);
        Node varNode = new Node(Token.NAME, VAR_NAME);
        Node varStatement = new Node(Token.VAR, varNode);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK, varStatement));
        Node root = new Node(Token.EXPR_RESULT, varStatement);
        Compiler compiler = new Compiler();
        CompilerInput input = new CompilerInput.JsInput(
                "test.js", false, root, CompilerOptions.NULL_OPTIONS);
        input.setCompiler(compiler);
        input.setModule(new JSModule("test.js"));
        compiler.init(externs, new JSModule("test.js", true));
        compiler.addInput(input);

        try {
            varCheck.process(externs, root);
        } catch (IllegalStateException e) {
            Assert.assertTrue(varCheck.varsToDeclareInExterns.contains(VAR_NAME));
        }
    }
}