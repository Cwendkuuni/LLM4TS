import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Prepare test case
    Node root = Node.objectLiteral();
    Node obj = root.addChild(Node.PROPERTY("obj", Node.objectLiteral()));

    Node objPropertyA = obj.getFirstChild();
    objPropertyA.setName("a");
    objPropertyA.addChild(Node.PROPERTY("a1", Node.numberLiteral(1)));

    Node objPropertyB = objPropertyA.addChild(Node.PROPERTY("b", Node.objectLiteral()));
    objPropertyB.setName("b");
    objPropertyB.addChild(Node.PROPERTY("b1", Node.numberLiteral(2)));
    objPropertyB.addChild(Node.PROPERTY("b2", Node.numberLiteral(3)));

    Node objPropertyC = obj.addProperty(Node.PROPERTY("c", Node.numberLiteral(4)));

    // Run the pass
    pass.process(null, root);

    // Verify the output
    Node expected = Node.objectLiteral();
    Node actual = root;
    assertEquals("InlineObjectLiterals: Inlined object literal {obj: {a: a1, b: {b1: b1, b2: b2}}}",
        expected.toString(), actual.toString());

    // Check that all object literals have been inlined
    List<Node> objectLiterals = Lists.newArrayList(objPropertyA, objPropertyB);
    for (Node ol : objectLiterals) {
      Node check = ol;
      while (check.isObjectLit()) {
        check = check.getFirstChild();
      }
      assertTrue("Object literal not inlined: " + check.toString(), check.isUndefined());
    }

    // Check that all variables have been created and assigned
    Map<String, Node> variables = new HashMap<>();
    Node current = root;
    while (!current.isUndefined()) {
      if (current.isVar()) {
        String name = current.getString();
        variables.put(name, current);
      }
      current = current.getParent();
    }
    Map<String, Node> expectedVariables = new HashMap<>();
    expectedVariables.put("a1", Node.numberLiteral(1));
    expectedVariables.put("b1", Node.numberLiteral(2));
    expectedVariables.put("b2", Node.numberLiteral(3));
    assertEquals("InlineObjectLiterals: Failed to create variables",
        expectedVariables, variables);

    // Check that references to the inlined properties have been updated
    Node checkRef = root;
    while (!checkRef.isGetProp()) {
      checkRef = checkRef.getParent();
    }
    String checkRefPropName = checkRef.getLastChild().getString();
    assertEquals("InlineObjectLiterals: Failed to update reference",
        "a", checkRefPropName);

    checkRef = checkRef.getParent();
    checkRefPropName = checkRef.getLastChild().getString();
    assertEquals("InlineObjectLiterals: Failed to update reference",
        "b", checkRefPropName);

    checkRef = checkRef.getParent();
    checkRefPropName = checkRef.getLastChild().getString();
    assertEquals("InlineObjectLiterals: Failed to update reference",
        "b1", checkRefPropName);

    checkRef = checkRef.getParent();
    checkRefPropName = checkRef.getLastChild().getString();
    assertEquals("InlineObjectLiterals: Failed to update reference",
        "b2", checkRefPropName);

  }
}