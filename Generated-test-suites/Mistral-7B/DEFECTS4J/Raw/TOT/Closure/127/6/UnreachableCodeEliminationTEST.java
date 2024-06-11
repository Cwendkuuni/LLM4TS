import static com.google.javascript.jscomp.UnreachableCodeElimination.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private Compiler compiler;
  private Parser parser;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOption(CompilerOptions.WARNING_LEVEL, CompilerOptions.WarningLevel.ERROR);
    parser = new Parser(compiler);
  }

  @Test
  public void testSimpleReturn() {
    String source = "function test() {" +
                   "  return; " +
                   "  alert('unreachable'); " +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testNoOpStatement() {
    String source = "function test() {" +
                   "  true;" +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertFalse(root.isFunction());
  }

  @Test
  public void testNoOpStatementInFunction() {
    String source = "function test() {" +
                   "  function inner() {" +
                   "    true;" +
                   "  } " +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertFalse(root.isFunction());
  }

  @Test
  public void testRemoveFunction() {
    String source = "function test() {" +
                   "  function inner() {" +
                   "    return;" +
                   "  } " +
                   "  inner(); " +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testRemoveStatementAfterReturn() {
    String source = "function test() {" +
                   "  return; " +
                   "  alert('unreachable'); " +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testRemoveDeadCodeInConditionalStatement() {
    String source = "function test() {" +
                   "  var x = 0;" +
                   "  if (x > 0) {" +
                   "    // Dead code" +
                   "  } " +
                   "  return;" +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  var x = 0;" +
                 "  if (x > 0) {" +
                 "  } " +
                 "  return; " +
                 "}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInLoop() {
    String source = "function test() {" +
                   "  for (var i = 0; i < 10; i++) {" +
                   "    // Dead code" +
                   "  } " +
                   "  return;" +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  for (var i = 0; i < 10; i++) {}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInSwitchStatement() {
    String source = "function test() {" +
                   "  var x = 0;" +
                   "  switch (x) {" +
                   "    case 1:" +
                   "      return;" +
                   "    case 0:" +
                   "      // Dead code" +
                   "  } " +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  var x = 0;" +
                 "  switch (x) {", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInSwitchStatementWithDefault() {
    String source = "function test() {" +
                   "  var x = 0;" +
                   "  switch (x) {" +
                   "    case 1:" +
                   "      return;" +
                   "    default:" +
                   "      // Dead code" +
                   "  } " +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  var x = 0;" +
                 "  switch (x) {", root.toString());
  }

  @Test
  public void testKeepPrototypeProperty() {
    String source = "function test() {" +
                   "  var obj = {" +
                   "    x: 1" +
                   "  }; " +
                   "  // Dead code" +
                   "  return; " +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, true).process(null, root);
    assertSame(root, parser.parse(input, null, null).getRootNode());
  }

  @Test
  public void testRemoveNoOpStatementInFunction() {
    String source = "function test() {" +
                   "  function inner() {" +
                   "    true;" +
                   "  } " +
                   "  inner(); " +
                   "  return;" +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, true).process(null, root);
    assertEquals("function test() {" +
                 "  function inner() {", root.toString());
  }

  @Test
  public void testRemoveNoOpStatementInBlock() {
    String source = "function test() {" +
                   "  {true;}" +
                   "  return;" +
                   "} ";
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, true).process(null, root);
    assertEquals("function test() {}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpression() {
    String source = "function test() {" +
                   "  return (x > 0) ? 1 : null;" +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  return x > 0 ? 1 : null; " +
                 "}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpressionWithEmptyStatement() {
    String source = "function test() {" +
                   "  return (x > 0) ? 1 : {} ;" +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  return x > 0 ? 1 : null; " +
                 "}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpressionWithExpressionStatement() {
    String source = "function test() {" +
                   "  return (x > 0) ? 1 : alert('unreachable');" +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  return x > 0 ? 1 : null; " +
                 "}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpressionWithEmptyStatementAndReturn() {
    String source = "function test() {" +
                   "  return (x > 0) ? 1 : {return: 2};" +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  return x > 0 ? 1 : 2; " +
                 "}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpressionWithReturn() {
    String source = "function test() {" +
                   "  return (x > 0) ? 1 : return 2; " +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  return x > 0 ? 1 : 2; " +
                 "}", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpressionWithBreak() {
    String source = "function test() {" +
                   "  for (var i = 0; i < 10; i++) {" +
                   "    if (x > 0) {" +
                   "      break;" +
                   "    } " +
                   "    return;" +
                   "  } " +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  for (var i = 0; i < 10; i++) {" +
                 "    if (x > 0) {" +
                 "      break; " +
                 "    } " +
                 "  } ", root.toString());
  }

  @Test
  public void testRemoveDeadCodeInConditionalExpressionWithContinue() {
    String source = "function test() {" +
                   "  for (var i = 0; i < 10; i++) {" +
                   "    if (x > 0) {" +
                   "      continue;" +
                   "    } " +
                   "    return;" +
                   "  } " +
                   "}" ;
    SourceFile input = new SourceFile("test.js", source);
    Node root = parser.parse(input, null, null).getRootNode();
    new UnreachableCodeElimination(compiler, false).process(null, root);
    assertEquals("function test() {" +
                 "  for (var i = 0; i < 10; i++) {" +
                 "    if (x > 0) {" +
                 "      continue; " +
                 "    } " +
                 "  } ", root.toString());
  }
}