import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> position,
          String aliasName, String aliasValue) {
        System.out.println(String.format("Alias %s %s -> %s", aliasName, sourceFileName, aliasValue));
      }
    };
  }

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.addTransformation(new AliasTransformation());
    options.setAliasTransformationHandler(transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    Node root = parser.parse(compiler, sourceCode, "Test.java", null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    // Check aliases were logged correctly
    assertEquals(1, transformationHandler.getLog().size());
    AliasTransformation aliasTransformation = transformationHandler.getLog().get(0);
    assertEquals("Test.java", aliasTransformation.getSourceFileName());
    assertEquals("div", aliasTransformation.getAliasName());
    assertEquals("goog.dom.TagName.DIV", aliasTransformation.getAliasValue());

    // Check root node was transformed correctly
    Node transformedRoot = compiler.getRootNode();
    Node transformedDivNode = transformedRoot.lookupVar("dom").getNode().lookupVar("DIV");
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        transformedRoot.toString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        transformedDivNode.toString());
  }
}