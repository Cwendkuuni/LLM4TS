import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.CompilerPassTestRunner;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSyntax;
import com.google.javascript.jscomp.traverse.AbstractPostOrderCallback;

import java.util.Set;

public class VarCheckTest extends CompilerPassTestCase {

  public void testVarCheck() throws Exception {
    String sourceCode =
        "var x = 1;\n" +
        "function foo() {\n" +
        "  var y = 2;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var x = 3;\n" +
        "}\n" +
        "var globalX = 4;\n";

    Node root = parse(sourceCode, TestSyntax.ECMASCRIPT5_STRICT);

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertNoErrors();
  }

  public void testUndefinedVarError() throws Exception {
    String sourceCode = "var x = 1;\n" +
        "function foo() {\n" +
        "  var y = x;\n" + // undefined
        "}\n";

    Node root = parse(sourceCode, TestSyntax.ECMASCRIPT5_STRICT);

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertErrorMessage("JSC_UNDEFINED_VARIABLE", 1, 8, "x");
  }

  public void testViolatedModuleDepError() throws Exception {
    String moduleA = "var x = 1;";
    String moduleB = "import a from './a';\n" +
        "export function foo() {\n" +
        "  x++;\n" +
        "}" ;

    String sourceCode = "import 'a.js';\n" +
        "function foo() {\n" +
        "  var x = 2;\n" + // should reference 'a.js'
        "}" ;

    Node rootModuleA = parse(moduleA, TestSyntax.ECMASCRIPT5_STRICT);
    Node rootModuleB = parse(moduleB, TestSyntax.ECMASCRIPT5_STRICT);
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.IMPORT, new Node(Token.STRING_LITERAL, "a.js")),
        new Node(Token.IMPORT, Lists.newArrayList(
            new Node(Token.IDENTIFIER, "a"),
            new Node(Token.FROM, new Node(Token.STRING_LITERAL, "./a.js"))
        )),
        rootModuleB.getFirstChild()
    ));

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertErrorMessage("JSC_VIOLATED_MODULE_DEPENDENCY", 2, 12, "a.js", "a", "foo");
  }

  public void testMissingModuleDepError() throws Exception {
    String moduleA = "var x = 1;";
    String sourceCode = "import a from './a';\n" +
        "function foo() {\n" +
        "  var x = 2;\n" + // should reference 'a.js'
        "}" ;

    Node rootModuleA = parse(moduleA, TestSyntax.ECMASCRIPT5_STRICT);

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(rootModuleA);

    Node root = parse(sourceCode, TestSyntax.ECMASCRIPT5_STRICT);

    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertErrorMessage("JSC_MISSING_MODULE_DEPENDENCY", 1, 12, "a.js");
  }

  public void testStrictModuleDepError() throws Exception {
    String moduleA = "var x = 1;";
    String moduleB = "export function foo() {\n" +
        "  var x = 2;\n" + // should be error but not warning
        "}" ;

    String sourceCode = "import a from './a';\n" +
        "function foo() {\n" +
        "  var x = 3;\n" +
        "}" ;

    Node rootModuleA = parse(moduleA, TestSyntax.ECMASCRIPT5_STRICT);
    Node rootModuleB = parse(moduleB, TestSyntax.ECMASCRIPT5_STRICT);
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(
        new Node(Token.IMPORT, new Node(Token.IDENTIFIER, "a")),
        new Node(Token.IMPORT, Lists.newArrayList(
            new Node(Token.IDENTIFIER, "a"),
            new Node(Token.FROM, new Node(Token.STRING_LITERAL, "./a.js"))
        )),
        rootModuleB.getFirstChild()
    ));

    CompilerPass compilerPass = new VarCheck(getCompiler(), true);
    compile(rootModuleA);
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertErrorMessage("JSC_STRICT_MODULE_DEPENDENCY", 2, 12, "a", "a");
  }

  public void testNameReferenceInExternsError() throws Exception {
    String sourceCode = "var x = 1;\n" +
        "import 'a.js';\n" + // should be a warning
        "function foo() {\n" +
        "  x++;\n" +
        "}" ;

    Node root = parse(sourceCode, TestSyntax.ECMASCRIPT5_STRICT);

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertWarningMessage("JSC_NAME_REFERENCE_IN_EXTERNS", 3, 6, "x");
  }

  public void testUndefinedExtrenVarError() throws Exception {
    String sourceCode = "import 'a.js';\n" + // should be a warning
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  x\n" + // should be undefined
        "}" ;

    Node root = parse(sourceCode, TestSyntax.ECMASCRIPT5_STRICT);

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertWarningMessage("JSC_UNDEFINED_EXTERN_VAR_ERROR", 3, 7, "x");
  }

  public void testInvalidFunctionDeclError() throws Exception {
    String sourceCode = "function foo() {}";

    Node root = parse(sourceCode, TestSyntax.ECMASCRIPT5_STRICT);

    CompilerPass compilerPass = new VarCheck(getCompiler());
    compile(root);

    NodeTraversal nodeTraversal = new NodeTraversal(compilerPass, root);
    nodeTraversal.traverse();

    assertErrorMessage("JSC_INVALID_FUNCTION_DECL", 1, 1, "");
  }
}