import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  private Compiler compiler;
  private AliasTransformationHandler transformationHandler;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String sourceFile, SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Compiler compiler, Node externs) {
            compiler.report("Alias transformation applied.");
          }
        };
      }
    };
  }

  @Test
  public void testScopedAliases() {
    // Create a simple test case with a single scoped alias.
    root = new Node(Token.SCRIPT, null);
    Node googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    Node anonymousFn = new Node(Token.FUNCTION, null);
    Node body = new Node(Token.BLOCK, null);
    Node aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    Node aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    anonymousFn.addChild(body);
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that the alias was correctly identified and transformed.
    assertEquals("goog.dom.Div", aliasUsage.getQualifiedName());

    // Create a test case with multiple scoped aliases.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    anonymousFn = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "g"),
        new Node(Token.NAME, null, "goog"));
    aliasUsage = new Node(Token.NAME, null, "g");
    body.addChild(aliasDefinition);
    anonymousFn.addChild(body);
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that both aliases were correctly identified and transformed.
    assertEquals("goog.Dom.Div", aliasUsage.getQualifiedName(0));
    assertEquals("goog", aliasUsage.getQualifiedName(1));

    // Test that an improperly used goog.scope call results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    Node badCall = new Node(Token.CALL, null, googScopeCall);
    root.addChild(badCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the improperly used goog.scope call.
    assertTrue(scopedAliases.hasErrors());

    // Test that a goog.scope call with bad parameters results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    Node badParameters = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    anonymousFn = new Node(Token.FUNCTION, null);
    body.addChild(anonymousFn);
    badParameters.addChild(body);
    badParameters.addChild(googScopeCall);
    root.addChild(badParameters);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the goog.scope call with bad parameters.
    assertTrue(scopedAliases.hasErrors());

    // Test that a goog.scope call that references 'this' results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    anonymousFn = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    anonymousFn.addChild(body);
    body.addChild(new Node(Token.THIS, null, null));
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the goog.scope call that references 'this'.
    assertTrue(scopedAliases.hasErrors());

    // Test that a goog.scope call that uses 'return' results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    anonymousFn = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    anonymousFn.addChild(body);
    body.addChild(new Node(Token.RETURN, null, null));
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the goog.scope call that uses 'return'.
    assertTrue(scopedAliases.hasErrors());

    // Test that a goog.scope call that uses 'throw' results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    anonymousFn = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    anonymousFn.addChild(body);
    body.addChild(new Node(Token.THROW, null, null));
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the goog.scope call that uses 'throw'.
    assertTrue(scopedAliases.hasErrors());

    // Test that a goog.scope call with multiple alias definitions results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    anonymousFn = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "g"),
        new Node(Token.NAME, null, "goog"));
    aliasUsage = new Node(Token.NAME, null, "g");
    body.addChild(aliasDefinition);
    anonymousFn.addChild(body);
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the goog.scope call with multiple alias definitions.
    assertTrue(scopedAliases.hasErrors());

    // Test that a local variable in a goog.scope that is not an alias results in an error.
    root = new Node(Token.SCRIPT, null);
    googScopeCall = new Node(Token.CALL, null, SCOPING_METHOD_NAME);
    anonymousFn = new Node(Token.FUNCTION, null);
    body = new Node(Token.BLOCK, null);
    aliasDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "dom"),
        new Node(Token.NAME, null, "Div"));
    aliasUsage = new Node(Token.NAME, null, "dom");
    body.addChild(aliasDefinition);
    Node localVarDefinition = new Node(Token.ASSIGN, null, null, null,
        new Node(Token.NAME, null, "local"),
        new Node(Token.NUMBER, null, 42));
    body.addChild(localVarDefinition);
    anonymousFn.addChild(body);
    googScopeCall.addChild(anonymousFn);
    root.addChild(googScopeCall);

    // Process the test case using the ScopedAliases pass.
    scopedAliases.process(null, root);

    // Verify that an error was reported for the local variable in the goog.scope.
    assertTrue(scopedAliases.hasErrors());
  }
}