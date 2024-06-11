package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeTraversal;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    // Given
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(inlineObjectLiterals);

    String sourceCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 2,\n" +
        "      d: function() {}\n" +
        "    },\n" +
        "    e: [1, 2, 3],\n" +
        "    f: function() {}\n" +
        "  };\n" +
        "  return obj.a;\n" +
        "}";

    Node externs = Node.externalRoot();
    Node root = Node.parse(sourceCode);

    // When
    passManager.runPasses(externs, root, options);

    // Then
    Scope scope = root.getScope();
    Node fooFunc = scope.getNamedFunction("foo");
    Node fooFuncBody = fooFunc.getBody();
    Node fooFuncReturn = fooFuncBody.getFirstChild();

    // Check that 'obj' is inlined and expanded into individual variables
    Node objVar = scope.getVar("obj");
    Node objInlineVars = objVar.getInitializer();
    Map<String, Node> objInlineVarMap = ImmutableMap.of(
        "a", objInlineVars.getChildAtIndex(0),
        "b", objInlineVars.getChildAtIndex(1),
        "b.c", objInlineVars.getChildAtIndex(2),
        "b.d", objInlineVars.getChildAtIndex(3),
        "e", objInlineVars.getChildAtIndex(4),
        "e[0]", objInlineVars.getChildAtIndex(5),
        "e[1]", objInlineVars.getChildAtIndex(6),
        "e[2]", objInlineVars.getChildAtIndex(7),
        "f", objInlineVars.getChildAtIndex(8)
    );

    // Check that 'obj.a' is replaced with 'obj.a._'
    assertEquals(
        Node.name("obj.a._"),
        fooFuncReturn
    );

    // Check that 'obj.b.c' is replaced with 'obj.b.c._'
    Node objBc = objInlineVarMap.get("b.c");
    assertEquals(
        Node.name("obj.b.c._"),
        fooFuncBody.getChildAtIndex(1)
    );

    // Check that 'obj.e[0]' is replaced with 'obj.e[0]._'
    Node objEZero = objInlineVarMap.get("e[0]");
    assertEquals(
        Node.name("obj.e[0]._"),
        fooFuncBody.getChildAtIndex(3)
    );

    // Check that 'obj.f' is replaced with 'obj.f._'
    Node objFFunction = objInlineVarMap.get("f");
    assertEquals(
        Node.name("obj.f._"),
        fooFuncBody.getChildAtIndex(4)
    );

    // Check that 'obj.b.d' function is inlined and its body is replaced with
    // a new function with the same name
    Node objBdFunction = objInlineVarMap.get("b.d");
    Node objBdFunctionBody = objBdFunction.getBody();
    Node newObjBdFunction = fooFuncBody.getChildAtIndex(5);
    assertEquals(
        newObjBdFunction,
        objBdFunctionBody
    );

    // Check that functions are inlined correctly
    Node functionDeclaration = root.getFirstChild();
    Node functionDeclarationBody = functionDeclaration.getBody();

    Preconditions.checkState(functionDeclarationBody.getNumChildren() > 0);
    Node functionBody = functionDeclarationBody.getChildAtIndex(0);
    Node functionReturn = functionBody.getLastChild();

    assertEquals(
        Node.name("foo._"),
        functionDeclaration.getChildAtIndex(0)
    );

    assertEquals(
        Node.name("obj._"),
        functionBody.getChildAtIndex(0)
    );

    assertEquals(
        Node.name("a._"),
        functionBody.getChildAtIndex(1)
    );

    assertEquals(
        Node.name("b._"),
        functionBody.getChildAtIndex(2)
    );

    assertEquals(
        Node.name("b.c._"),
        functionBody.getChildAtIndex(3)
    );

    assertEquals(
        Node.name("b.d._"),
        functionBody.getChildAtIndex(4)
    );

    assertEquals(
        Node.name("e._[0]"),
        functionBody.getChildAtIndex(5)
    );

    assertEquals(
        Node.name("e._[1]"),
        functionBody.getChildAtIndex(6)
    );

    assertEquals(
        Node.name("e._[2]"),
        functionBody.getChildAtIndex(7)
    );

    assertEquals(
        Node.name("f._"),
        functionBody.getChildAtIndex(8)
    );

    assertEquals(
        Node.name("return"),
        functionBody.getChildAtIndex(9)
    );

    assertEquals(
        Node.name("a._"),
        functionReturn
    );
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() throws Exception {
    // Given
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(inlineObjectLiterals);

    String sourceCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: obj\n" +
        "    }\n" +
        "  };\n" +
        "}";

    Node externs = Node.externalRoot();
    Node root = Node.parse(sourceCode);

    // When
    passManager.runPasses(externs, root, options);

    // Then
    // This test case should fail because of the self-referential
    // assignment in the object literal.
  }
}