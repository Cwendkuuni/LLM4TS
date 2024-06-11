import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Sample code for testing
    Node root = Node.newObjectLiteral(
        Node.newProperty(Node.newName("a"), Node.newNumber(1)),
        Node.newProperty(Node.newName("b"), Node.newObjectLiteral(
            Node.newProperty(Node.newName("c"), Node.newNumber(2)),
            Node.newProperty(Node.newName("d"), Node.newNumber(3))
        ))
    );

    Scope scope = new Scope(Node.newRootNode(root), null);
    Var variable = new Var(Node.newName("obj"), Node.newObjectLiteral(
        Node.newProperty(Node.newName("a"), Node.newNumber(1)),
        Node.newProperty(Node.newName("b"), Node.newObjectLiteral(
            Node.newProperty(Node.newName("c"), Node.newNumber(2)),
            Node.newProperty(Node.newName("d"), Node.newNumber(3))
        ))
    ));
    scope.addVar(variable);

    inlineObjectLiterals.process(null, root);

    Node expectedRoot = Node.newObjectLiteral(
        Node.newProperty(Node.newName("a"), Node.newNumber(1)),
        Node.newProperty(Node.newName("b"), Node.newObjectLiteral(
            Node.newProperty(Node.newName("c"), Node.newName("c")),
            Node.newProperty(Node.newName("d"), Node.newName("d"))
        ))
    );

    ComparatorUtil.compareTrees(expectedRoot, root);
  }
}

class ComparatorUtil {
  public static void compareTrees(Node expected, Node actual) {
    assertEquals(expected.getClass(), actual.getClass());

    if (expected instanceof Node) {
      Node expectedNode = (Node) expected;
      Node actualNode = (Node) actual;

      if (expectedNode.getType() != actualNode.getType()) {
        fail("Expected type " + expectedNode.getType() +
             ", but got " + actualNode.getType());
      }

      if (expectedNode.getFirstChild() != null) {
        compareTrees(expectedNode.getFirstChild(), actualNode.getFirstChild());
      }

      if (expectedNode.getLastChild() != null) {
        compareTrees(expectedNode.getLastChild(), actualNode.getLastChild());
      }

    } else if (expected instanceof List) {
      List<?> expectedList = (List<?>) expected;
      List<?> actualList = (List<?>) actual;

      assertEquals(expectedList.size(), actualList.size());

      for (int i = 0; i < expectedList.size(); i++) {
        Node expectedNode = (Node) expectedList.get(i);
        Node actualNode = (Node) actualList.get(i);
        compareTrees(expectedNode, actualNode);
      }

    } else if (expected instanceof Map) {
      Map<?, ?> expectedMap = (Map<?, ?>) expected;
      Map<?, ?> actualMap = (Map<?, ?>) actual;

      assertEquals(expectedMap.size(), actualMap.size());

      for (Map.Entry<?, ?> entry : expectedMap.entrySet()) {
        Object key = entry.getKey();
        Object value = entry.getValue();

        Object actualKey = actualMap.get(key);
        Node actualValue = null;

        if (actualKey != null) {
          actualValue = (Node) actualKey;
        }

        if (value instanceof Node) {
          compareTrees((Node) value, actualValue);
        } else if (value instanceof List) {
          List<?> valueList = (List<?>) value;
          List<?> actualValueList = Lists.newArrayList();

          for (Object obj : valueList) {
            if (obj instanceof Node) {
              actualValueList.add(actualMap.get(obj));
            }
          }

          compareTrees(Node.newObjectLiteral(valueList.toArray(new Node[0])),
                      Node.newObjectLiteral(actualValueList.toArray(new Node[0])));
        }
      }

    }
  }
}