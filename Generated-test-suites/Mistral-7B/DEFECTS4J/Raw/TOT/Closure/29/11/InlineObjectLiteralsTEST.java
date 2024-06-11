import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

 private Compiler compiler;
 private Node rootNode;

 @Before
 public void setUp() {
 compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setPrintWarnings(false);
 options.setPrintStats(false);
 rootNode = NodeUtil.parseJsSource("{\n" +
         "  x: {\n" +
         "    a: 1,\n" +
         "    b: 2,\n" +
         "    c: {\n" +
         "      d: 3,\n" +
         "      e: 4\n" +
         "    }\n" +
         "  },\n" +
         "  y: function() {\n" +
         "    return x.a;\n" +
         "  }\n" +
         "}", null);
 }

 @Test
 public void testInlineObjectLiterals() {
 compiler.process(rootNode, null, options);
 Node x = rootNode.getFirstChild();
 Node y = NodeUtil.getFunctionChild(x, 1);
 Node callNode = NodeUtil.getFunctionChild(y, 0);

 // Check that the object literal has been inlined and the variables assigned
 Node inlineX = x.getFirstChild();
 Node a = inlineX.getFirstChild();
 Node inlineC = inlineX.getLastChild();
 Node inlineD = inlineC.getFirstChild();
 Node e = inlineC.getLastChild();

 // Check that the variables have the correct values
 assertEquals(1, a.getNumber().intValueExact());
 assertEquals(2, NodeUtil.getChildValue(inlineX, 1).getNumber().intValueExact());
 assertEquals(3, inlineD.getNumber().intValueExact());
 assertEquals(4, e.getNumber().intValueExact());

 // Check that the function still works correctly
 Node result = NodeUtil.runFunction(callNode, null, null);
 assertEquals(1, result.getNumber().intValueExact());
 }

 @Test
 public void testInlineObjectLiteralsWithComplexValues() {
 rootNode = NodeUtil.parseJsSource("{\n" +
         "  x: {\n" +
         "    a: 1,\n" +
         "    b: { g: function() { return 5; } },\n" +
         "    c: [1, 2, 3],\n" +
         "    d: '\u0061'\n" +
         "  }\n" +
         "}", null);

 compiler.process(rootNode, null, options);
 Node x = rootNode.getFirstChild();
 Node g = NodeUtil.getFunctionChild(NodeUtil.getChildAtIndex(
         NodeUtil.getChildAtIndex(x, 1), 0), 0);
 Node a = NodeUtil.getChildValue(x, 0);
 Node cFirst = NodeUtil.getChildAtIndex(NodeUtil.getChildAtIndex(x, 1), 1);
 Node cSecond = NodeUtil.getChildAtIndex(cFirst, 1);
 Node cThird = NodeUtil.getChildAtIndex(cFirst, 2);
 Node d = NodeUtil.getChildValue(x, 3);

 // Check that the object literal has been inlined and the variables assigned
 Node inlineX = x.getFirstChild();
 Node aInline = inlineX.getFirstChild();
 Node inlineBG = inlineX.getFirstChild().getLastChild();
 Node gInline = inlineBG.getFirstChild();
 Node cFirstInline = inlineX.getLastChild().getFirstChild();
 Node cSecondInline = NodeUtil.getChildAtIndex(cFirstInline, 1);
 Node cThirdInline = NodeUtil.getChildAtIndex(cSecondInline, 1);
 Node dInline = inlineX.getLastChild().getLastChild();

 // Check that the variables have the correct values
 assertEquals(1, aInline.getNumber().intValueExact());
 assertEquals(5, gInline.getNumber().intValueExact());
 assertEquals(1, cFirstInline.getNumber().intValueExact());
 assertEquals(2, cSecondInline.getNumber().intValueExact());
 assertEquals(3, cThirdInline.getNumber().intValueExact());
 assertEquals('a', dInline.getString().charAt(0));

 // Check that the function still works correctly
 Node result = NodeUtil.runFunction(gInline, null, null);
 assertEquals(5, result.getNumber().intValueExact());
 }

 @Test(expected = AssertionError.class)
 public void testInlineObjectLiteralsWithInvalidObject() {
 rootNode = NodeUtil.parseJsSource("{\n" +
         "  x: function() {\n" +
         "  },\n" +
         "}", null);
 compiler.process(rootNode, null, options);

 // This test should fail as the object literal is not valid
 Node x = rootNode.getFirstChild();
 assertNotNull(x);
 }
}