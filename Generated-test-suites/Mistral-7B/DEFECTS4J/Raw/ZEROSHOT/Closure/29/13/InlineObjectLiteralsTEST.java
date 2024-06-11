import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String source = "function f() {\n" +
        "  var o = {\n" +
        "    x: 1,\n" +
        "    y: 2,\n" +
        "    z: function() {}\n" +
        "  };\n" +
        "  var a = o.x;\n" +
        "  var b = o.y;\n" +
        "  var c = o.z;\n" +
        "  o.z();\n" +
        "  return a + b;\n" +
        "}";

    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(source, null);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> null);
    inlineObjectLiterals.process(null, root);

    NodeTraversal traverser = new NodeTraversal(root);
    // Check that the object literal has been inlined
    assertEquals(12, traverser.getRootNode().getNumChildren());

    int xIndex = 0;
    int yIndex = 1;
    int zIndex = 2;
    // Check that 'x' and 'y' have been inlined as separate variables
    Node x = traverser.getRootNode().getChildAt(xIndex);
    Node y = traverser.getRootNode().getChildAt(yIndex);
    assertEquals(VAR_PREFIX + "x" + "_", x.getName());
    assertEquals(VAR_PREFIX + "y" + "_", y.getName());

    // Check that 'z' has been inlined as a function
    Node z = traverser.getRootNode().getChildAt(zIndex);
    assertEquals(VAR_PREFIX + "z" + "_", z.getName());
    assertEquals(Node.FUNCTION_DECL, z.getNodeType());

    // Check that the original object literal is gone
    assertNull(traverser.getRootNode().getChildAt(3));

    // Check that the function call to 'z' is still present
    Node zCall = traverser.getRootNode().getChildAt(8);
    assertNotNull(zCall);
    assertEquals(Node.CALL, zCall.getNodeType());
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() {
    String source = "function f() {\n" +
        "  var o = {\n" +
        "    x: 1,\n" +
        "    y: 2,\n" +
        "    z: function() {}\n" +
        "  };\n" +
        "  o.x = 3;\n" +
        "  o.y = 4;\n" +
        "  return o.x + o.y;\n" +
        "}";

    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(source, null);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> null);
    inlineObjectLiterals.process(null, root);

    NodeTraversal traverser = new NodeTraversal(root);
    // Check that the object literal has been inlined
    assertEquals(18, traverser.getRootNode().getNumChildren());

    int xIndex = 0;
    int yIndex = 1;
    int zIndex = 2;

    // Check that 'x' and 'y' have been inlined as separate variables
    Node x = traverser.getRootNode().getChildAt(xIndex);
    Node y = traverser.getRootNode().getChildAt(yIndex);
    assertEquals(VAR_PREFIX + "x" + "_", x.getName());
    assertEquals(3, x.getIntProperty("value"));

    assertEquals(VAR_PREFIX + "y" + "_", y.getName());
    assertEquals(4, y.getIntProperty("value"));

    // Check that 'z' has been inlined as a function
    Node z = traverser.getRootNode().getChildAt(zIndex);
    assertEquals(VAR_PREFIX + "z" + "_", z.getName());
    assertEquals(Node.FUNCTION_DECL, z.getNodeType());

    // Check that the original object literal is gone
    assertNull(traverser.getRootNode().getChildAt(3));

    // Check that the assignments to 'x' and 'y' are still present
    Node xAssignment = traverser.getRootNode().getChildAt(4);
    Node yAssignment = traverser.getRootNode().getChildAt(5);
    // Check that the 'x' and 'y' assignments have been updated
    assertNotNull(xAssignment.getAssignedValue());
    assertEquals(3, xAssignment.getAssignedValue().getIntProperty("value"));

    assertNotNull(yAssignment.getAssignedValue());
    assertEquals(4, yAssignment.getAssignedValue().getIntProperty("value"));

    // Check that the function call to 'z' is still present
    Node zCall = traverser.getRootNode().getChildAt(10);
    assertNotNull(zCall);
    assertEquals(Node.CALL, zCall.getNodeType());
  }

  @Test
  public void testInlineObjectLiteralsWithInitAndDeclaration() {
    String source = "function f() {\n" +
        "  var o = {\n" +
        "    x: 1,\n" +
        "    y: 2,\n" +
        "    z: function() {}\n" +
        "  };\n" +
        "  var a = o.x;\n" +
        "  var b = o.y;\n" +
        "  var o = {\n" +
        "    x: 3,\n" +
        "    y: 4,\n" +
        "    z: function() {}\n" +
        "  };\n" +
        "  return a + b;\n" +
        "}";

    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(source, null);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> null);
    inlineObjectLiterals.process(null, root);

    NodeTraversal traverser = new NodeTraversal(root);
    // Check that the object literal has been inlined
    assertEquals(21, traverser.getRootNode().getNumChildren());

    int xIndex = 0;
    int yIndex = 1;
    int zIndex = 2;

    // Check that 'x' and 'y' have been inlined as separate variables
    Node x = traverser.getRootNode().getChildAt(xIndex);
    Node y = traverser.getRootNode().getChildAt(yIndex);
    assertEquals(VAR_PREFIX + "x" + "_", x.getName());
    assertEquals(1, x.getIntProperty("value"));

    assertEquals(VAR_PREFIX + "y" + "_", y.getName());
    assertEquals(2, y.getIntProperty("value"));

    // Check that 'z' has been inlined as a function
    Node z = traverser.getRootNode().getChildAt(zIndex);
    assertEquals(VAR_PREFIX + "z" + "_", z.getName());
    assertEquals(Node.FUNCTION_DECL, z.getNodeType());

    // Check that the original object literal is gone
    assertNull(traverser.getRootNode().getChildAt(3));

    // Check that the first assignment to 'o' is still present
    Node firstO = traverser.getRootNode().getChildAt(4);
    assertNotNull(firstO);
    assertEquals(Node.VAR, firstO.getNodeType());

    // Check that the second assignment to 'o' has been inlined
    Node secondO = traverser.getRootNode().getChildAt(9);
    assertNotNull(secondO);
    assertEquals(Node.ASSIGN, secondO.getNodeType());
    assertEquals(10, secondO.getNumChildren());

    // Check that the assignments to 'x' and 'y' in the second object literal
    // are still present
    Node xAssignment = secondO.getChildAt(1);
    Node yAssignment = secondO.getChildAt(2);
    assertNotNull(xAssignment.getAssignedValue());
    assertEquals(3, xAssignment.getAssignedValue().getIntProperty("value"));

    assertNotNull(yAssignment.getAssignedValue());
    assertEquals(4, yAssignment.getAssignedValue().getIntProperty("value"));

    // Check that the function call to 'z' is still present
    Node zCall = traverser.getRootNode().getChildAt(13);
    assertNotNull(zCall);
    assertEquals(Node.CALL, zCall.getNodeType());
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariables() {
    String source = "function f() {\n" +
        "  exports.x = {\n" +
        "    y: 1,\n" +
        "    z: function() {}\n" +
        "  };\n" +
        "  return exports.x.y;\n" +
        "}";

    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(source, null);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> null);
    inlineObjectLiterals.process(null, root);

    NodeTraversal traverser = new NodeTraversal(root);
    // Check that the object literal has been inlined
    assertEquals(3, traverser.getRootNode().getNumChildren());

    // Check that 'x' and 'y' have been inlined as separate variables
    Node x = traverser.getRootNode().getChildAt(0);
    Node y = traverser.getRootNode().getChildAt(1);
    assertEquals(VAR_PREFIX + "x" + "_", x.getName());
    assertEquals(VAR_PREFIX + "y" + "_", y.getName());

    // Check that the original object literal is gone
    assertNull(traverser.getRootNode().getChildAt(2));

    // Check that the exported variable is still present
    Node exports = traverser.getRootNode().getChildAt(0);
    assertNotNull(exports);
    assertEquals(Node.OBJECT_LIT, exports.getNodeType());
    assertEquals(1, exports.getNumChildren());
    Node exportX = exports.getChildAt(0);
    assertEquals(Node.VAR, exportX.getNodeType());
    assertEquals("x", exportX.getString());

    // Check that 'y' is still a property of the exported variable
    Node yProperty = exportX.getChildAt(0);
    assertEquals(Node.PROP, yProperty.getNodeType());
    assertEquals("y", yProperty.getString());
    assertEquals(1, yProperty.getNumChildren());
    assertEquals(Node.INT_LIT, yProperty.getChildAt(0).getNodeType());
    assertEquals(1, yProperty.getChildAt(0).getIntProperty("value"));
  }
}