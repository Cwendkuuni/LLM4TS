import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "test_" + System.currentTimeMillis());
  }

  @Test
  public void testInlineSimpleObjectLiteral() {
    Node root = nodeFactory.createScriptlet("{}");
    inlineObjectLiterals.process(null, root);
    Node result = compiler.getOutput();
    assertEquals("var JSCompiler_object_inline_key1_test_1656855529628=\"value1\";\n" +
                 "var JSCompiler_object_inline_key2_test_1656855529628=\"value2\";\n" +
                 "true;", result.toString());
  }

  @Test
  public void testInlineComplexObjectLiteral() {
    Node root = nodeFactory.createScriptlet("{key1: {key2: 'value2'}}");
    inlineObjectLiterals.process(null, root);
    Node result = compiler.getOutput();
    assertEquals("var JSCompiler_object_inline_key1_test_1656855529628={};\n" +
                 "JSCompiler_object_inline_key1_test_1656855529628.JSCompiler_object_inline_key2_test_1656855529628=\"value2\";\n" +
                 "true;", result.toString());
  }

  @Test
  public void testInlineObjectLiteralWithFunction() {
    Node root = nodeFactory.createScriptlet("{fn: function() {}}");
    inlineObjectLiterals.process(null, root);
    Node result = compiler.getOutput();
    assertEquals("var JSCompiler_object_inline_fn_test_1656855529628=function() {};\n" +
                 "true;", result.toString());
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    Node root = nodeFactory.createScriptlet("export var obj = {};");
    inlineObjectLiterals.process(null, root);
    Node result = compiler.getOutput();
    assertEquals("// Unchanged export statement\n" +
                 "true;", result.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testInlineObjectLiteralWithSelfAssignment() {
    Node root = nodeFactory.createScriptlet("var obj = {a: obj.b};");
    inlineObjectLiterals.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testInlineObjectLiteralWithCircularReference() {
    Node root = nodeFactory.createScriptlet("var obj1 = {a: obj2};\n" +
                                            "var obj2 = {a: obj1};");
    inlineObjectLiterals.process(null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInlineObjectLiteralWithInvalidInput() {
    Node root = nodeFactory.createScriptlet("1 + 1;");
    inlineObjectLiterals.process(null, root);
  }
}