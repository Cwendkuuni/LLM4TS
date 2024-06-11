import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CheckLevel;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  @SuppressWarnings("unchecked")
  private static <T> T newNode(NodeType type, Object... args) {
    return (T) NodeFactory.createNodeFromPrintable(type, null, args);
  }

  private AbstractCompiler compiler;
  private CompilerInput input;
  private CompilerInput externsInput;
  private Node externsRoot;
  private VarCheck varCheck;

  @Before
  public void setup() {
    compiler = new CompilerOptions().newCompiler();
    input = new CompilerInput("TestFile.js", null);
    externsInput = new CompilerInput("TestFileExterns.js", null);
    externsRoot = new Node(TOKEN_PROGRAM, null);

    varCheck = new VarCheck(compiler);
  }

  @After
  public void tearDown() {
    compiler = null;
    input = null;
    externsInput = null;
    externsRoot = null;
    compiler.dispose();
  }

  @Test
  public void testUndefinedVar() {
    Node root = newNode(TOKEN_PROGRAM,
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externsRoot, input.getAstRoot(compiler));
    Node undeclaredVar = traversal.getRoot().getFirstChild();
    assertEquals(UNDEFINED_VAR_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_UNDEFINED_VARIABLE", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }

  @Test
  public void testUndefinedVarInFunction() {
    Node root = newNode(TOKEN_PROGRAM,
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(LP, null),
        newNode(TOKEN_FUNCTION, null, newNode(VAR, newNode(TOKEN_NAME, "bar"))),
        newNode(TOKEN_LBRACE, null),
        newNode(TOKEN_RETURN, null, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_RBRACE, null),
        newNode(RP, null),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externsRoot, input.getAstRoot(compiler));
    Node undeclaredVar = traversal.getRoot().getChildAt(1);
    assertEquals(UNDEFINED_VAR_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_UNDEFINED_VARIABLE", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }

  @Test
  public void testUndefinedVarInExterns() {
    Node externs = newNode(TOKEN_PROGRAM,
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    Node root = newNode(TOKEN_PROGRAM,
        newNode(TOKEN_NAME, "bar"),
        newNode(TOKEN_DOT, null),
        newNode(TOKEN_NAME, "foo"),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externs, input.getAstRoot(compiler));
    Node accessToUndefinedVar = traversal.getRoot().getChildAt(2);
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }

  @Test
  public void testModuleDependency() {
    Node externs = newNode(TOKEN_PROGRAM,
        newNode(IMPORT_DECLARATION, null,
            newNode(STRING_LITERAL, "TestDepModule.js"),
            newNode(STRUCT_CTOR, "TestDepModule", null)
        ),
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    Node root = newNode(TOKEN_PROGRAM,
        newNode(IMPORT_DECLARATION, null,
            newNode(STRING_LITERAL, "TestDepModule.js"),
            newNode(STRUCT_CTOR, "TestModule", null)
        ),
        newNode(LP, null),
        newNode(TOKEN_FUNCTION, null, newNode(VAR, newNode(TOKEN_NAME, "x"))),
        newNode(TOKEN_LBRACE, null),
        newNode(TOKEN_RETURN, null, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_RBRACE, null),
        newNode(RP, null),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externs, input.getAstRoot(compiler));
    assertEquals(VIOLATED_MODULE_DEP_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("TestModule", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
    assertEquals("TestDepModule", varCheck.getLastDiagnostic().getArg1().getMessage(input.getSourceName()));
    assertEquals("foo", varCheck.getLastDiagnostic().getArg2().getMessage(input.getSourceName()));
  }

  @Test
  public void testMissingModuleDependency() {
    Node externs = newNode(TOKEN_PROGRAM,
        newNode(IMPORT_DECLARATION, null,
            newNode(STRING_LITERAL, "TestMissingDepModule.js"),
            newNode(STRUCT_CTOR, "TestModule", null)
        ),
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    Node root = newNode(TOKEN_PROGRAM,
        newNode(IMPORT_DECLARATION, null,
            newNode(STRING_LITERAL, "TestModule.js"),
            newNode(STRUCT_CTOR, "TestModule", null)
        ),
        newNode(LP, null),
        newNode(TOKEN_FUNCTION, null, newNode(VAR, newNode(TOKEN_NAME, "x"))),
        newNode(TOKEN_LBRACE, null),
        newNode(TOKEN_RETURN, null, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_RBRACE, null),
        newNode(RP, null),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externs, input.getAstRoot(compiler));
    assertEquals(MISSING_MODULE_DEP_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("TestModule", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
    assertEquals("TestMissingDepModule", varCheck.getLastDiagnostic().getArg1().getMessage(input.getSourceName()));
    assertEquals("foo", varCheck.getLastDiagnostic().getArg2().getMessage(input.getSourceName()));
  }

  @Test
  public void testStrictModuleDependency() {
    Node externs = newNode(TOKEN_PROGRAM,
        newNode(IMPORT_DECLARATION, null,
            newNode(STRING_LITERAL, "TestDepModule.js"),
            newNode(STRUCT_CTOR, "TestDepModule", null)
        ),
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    Node root = newNode(TOKEN_PROGRAM,
        newNode(IMPORT_DECLARATION, null,
            newNode(STRING_LITERAL, "TestModule.js"),
            newNode(STRUCT_CTOR, "TestModule", null)
        ),
        newNode(LP, null),
        newNode(TOKEN_FUNCTION, null, newNode(VAR, newNode(TOKEN_NAME, "x"))),
        newNode(TOKEN_LBRACE, null),
        newNode(TOKEN_RETURN, null, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_RBRACE, null),
        newNode(RP, null),
        newNode(TOKEN_SEMICOLON)
    );

    CompilerOptions options = new CompilerOptions();
    options.setErrorLevel(JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR), CheckLevel.WARNING);
    compiler = new CompilerOptions(options).newCompiler();

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externs, input.getAstRoot(compiler));
    assertEquals(STRICT_MODULE_DEP_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("TestModule", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
    assertEquals("TestDepModule", varCheck.getLastDiagnostic().getArg1().getMessage(input.getSourceName()));
    assertEquals("foo", varCheck.getLastDiagnostic().getArg2().getMessage(input.getSourceName()));
  }

  @Test
  public void testDuplicateVarDeclaration() {
    Node root = newNode(TOKEN_PROGRAM,
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON),
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externsRoot, input.getAstRoot(compiler));
    Node duplicateVar = traversal.getRoot().getChildAt(1);
    assertEquals(UNDEFINED_VAR_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_UNDEFINED_VARIABLE", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }

  @Test
  public void testDuplicateVarDeclarationInFunction() {
    Node root = newNode(TOKEN_PROGRAM,
        newNode(VAR, new Node(TOKEN_NAME, "MyModule"),
            newNode(TOKEN_LP, null),
            newNode(TOKEN_FUNCTION, null, new Node(TOKEN_NAME, "foo")),
            newNode(TOKEN_LBRACE, null),
            new Node(TOKEN_VAR, null, new Node(TOKEN_NAME, "foo")),
            new Node(TOKEN_SEMICOLON),
            new Node(TOKEN_RETURN, null, new Node(TOKEN_NAME, "x")),
            newNode(TOKEN_RBRACE, null),
            newNode(TOKEN_RP, null),
            newNode(TOKEN_SEMICOLON)
        ),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externsRoot, input.getAstRoot(compiler));
    Node duplicateVar = traversal.getRoot().getChildAt(0).getChildAt(1);
    assertEquals(UNDEFINED_VAR_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_UNDEFINED_VARIABLE", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }

  @Test
  public void testDuplicateVarDeclarationInExterns() {
    Node externs = newNode(TOKEN_PROGRAM,
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON),
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    Node root = new Node(TOKEN_PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externs, input.getAstRoot(compiler));
    Node duplicateVar = traversal.getRoot().getChildAt(1);
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    Node root = newNode(TOKEN_PROGRAM,
        newNode(TOKEN_FUNCTION, null)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externsRoot, input.getAstRoot(compiler));
    assertEquals(INVALID_FUNCTION_DECL, varCheck.getLastDiagnosticType());
    assertEquals("JSC_INVALID_FUNCTION_DECL", varCheck.getLastDiagnostic().getType().getName());
  }

  @Test
  public void testNameReferenceInExterns() {
    Node externs = newNode(TOKEN_PROGRAM,
        newNode(VAR, newNode(TOKEN_NAME, "foo")),
        newNode(TOKEN_SEMICOLON)
    );

    Node root = newNode(TOKEN_PROGRAM,
        newNode(TOKEN_NAME, "bar"),
        newNode(TOKEN_DOT, null),
        newNode(TOKEN_NAME, "foo"),
        newNode(TOKEN_SEMICOLON)
    );

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    varCheck.process(externs, input.getAstRoot(compiler));
    Node nameReference = traversal.getRoot().getChildAt(2);
    assertEquals(NAME_REFERENCE_IN_EXTERNS_ERROR, varCheck.getLastDiagnosticType());
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", varCheck.getLastDiagnostic().getType().getName());
    assertEquals("foo", varCheck.getLastDiagnostic().getMessage(input.getSourceName()));
  }
}