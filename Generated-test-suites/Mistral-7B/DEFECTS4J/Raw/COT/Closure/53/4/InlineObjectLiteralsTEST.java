import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.jscomp.Scope.VarType.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_";
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // When
    Node externs = newNode(Token.PROGRAM, null);
    Node root = newNode(Token.BLOCK, null);

    inlineObjectLiterals.process(externs, root);

    // Then
    Scope globalScope = root.getFirstChild().getScope();

    // Test that all inlineable object literals are replaced
    List<Node> objectsToTest = ImmutableList.of(
        newNode(Token.OBJECTLIT, null,
          newNode(Token.PROPERTIES, null,
            newNode(Token.PROPERTIES, null,
              newNode(Token.ASSIGN, null,
                newNode(Token.NAME, "a"),
                newNode(Token.NUMBER, 1)
              ),
              newNode(Token.ASSIGN, null,
                newNode(Token.NAME, "b"),
                newNode(Token.NUMBER, 2)
              )
            )
          ),
          newNode(Token.ASSIGN, null,
            newNode(Token.NAME, "c"),
            newNode(Token.NUMBER, 3)
          )
        ),
        newNode(Token.OBJECTLIT, null,
          newNode(Token.PROPERTIES, null,
            newNode(Token.ASSIGN, null,
              newNode(Token.NAME, "x"),
              newNode(Token.OBJECTLIT, null,
                newNode(Token.PROPERTIES, null,
                  newNode(Token.ASSIGN, null,
                    newNode(Token.NAME, "y"),
                    newNode(Token.NUMBER, 4)
                  ),
                  newNode(Token.ASSIGN, null,
                    newNode(Token.NAME, "z"),
                    newNode(Token.NUMBER, 5)
                  )
                )
              )
            )
          )
        )
    );

    for (Node object : objectsToTest) {
      Node objectNode = root.getFirstChild().findChildByName("test_");
      Node expectedObject = object;
      // Check that the object literals are replaced with individual variables
      Node actualObject = NodeUtil.findDeep(objectNode, object);
      assertEquals(expectedObject, actualObject);

      // Check that the variables are declared and assigned correctly
      Map<String, Var> variables = NodeUtil.getDeclaredVariables(objectNode);
      for (Map.Entry<String, Var> variableEntry : variables.entrySet()) {
        Var variable = variableEntry.getValue();
        String variableName = variable.name;
        Node variableAssignment = NodeUtil.getAssignmentForVariable(objectNode, variable);
        assertNotNull(variableAssignment);
        Node variableValue = variableAssignment.getLastChild();
        // Check that the variable value matches the expected value
        if (variableValue.getType() == Token.NUMBER) {
          int expectedValue = NodeUtil.getIntValue(expectedObject, variableName);
          int actualValue = NodeUtil.getIntValue(variableValue);
          assertEquals(expectedValue, actualValue);
        } else {
          // Handle object literals recursively, as they may contain more variables
          Node variableValueObject = NodeUtil.getObjectLiteral(variableValue);
          assertEquals(NodeUtil.getObjectLiteral(expectedObject, variableName), variableValueObject);
        }
      }
    }

    // Test that objects with self-referential assignments are not inlined
    Node selfReferentialObject = newNode(Token.OBJECTLIT, null,
      newNode(Token.PROPERTIES, null,
        newNode(Token.ASSIGN, null,
          newNode(Token.NAME, "x"),
          newNode(Token.OBJECTLIT, null,
            newNode(Token.PROPERTIES, null,
              newNode(Token.ASSIGN, null,
                newNode(Token.NAME, "y"),
                NodeUtil.newVarNode(
                  "x",
                  NodeUtil.newString(Token.NAME, "x")
                )
              )
            )
          )
        )
      )
    );

    Node objectNode = root.getFirstChild().findChildByName("test_");
    assertNull(NodeUtil.findDeep(objectNode, selfReferentialObject));

    // Test that variables that are part of an exported object are not inlined
    Node exportedObject = newNode(Token.OBJECTLIT, null,
      newNode(Token.PROPERTIES, null,
        newNode(Token.ASSIGN, null,
          newNode(Token.NAME, "x"),
          newNode(Token.NUMBER, 1)
        ),
        newNode(Token.EXPORT, null,
          newNode(Token.NAME, "exported"),
          NodeUtil.newVarNode("x", "x")
        )
      )
    );

    Node objectNodeExported = root.getFirstChild().findChildByName("test_");
    assertNull(NodeUtil.findDeep(objectNodeExported, exportedObject));

    // Test that variables with circular references are not inlined
    Node circularObject = newNode(Token.OBJECTLIT, null,
      newNode(Token.PROPERTIES, null,
        newNode(Token.ASSIGN, null,
          newNode(Token.NAME, "x"),
          NodeUtil.newVarNode("y", "y")
        ),
        newNode(Token.ASSIGN, null,
          newNode(Token.NAME, "y"),
          NodeUtil.newVarNode("x", "x")
        )
      )
    );

    Node objectNodeCircular = root.getFirstChild().findChildByName("test_");
    assertNull(NodeUtil.findDeep(objectNodeCircular, circularObject));

    // Test that variables that are part of a const object are not inlined
    Node constObject = newNode(Token.OBJECTLIT, null,
      newNode(Token.PROPERTIES, null,
        newNode(Token.CONST, null,
          newNode(Token.NAME, "constX"),
          newNode(Token.NUMBER, 1)
        ),
        newNode(Token.ASSIGN, null,
          newNode(Token.NAME, "x"),
          NodeUtil.newVarNode("constX", "constX")
        )
      )
    );

    Node objectNodeConst = root.getFirstChild().findChildByName("test_");
    assertNull(NodeUtil.findDeep(objectNodeConst, constObject));
  }

  @Test(expected = AssertionError.class)
  public void testCompilerError() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_";
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // When
    Node externs = newNode(Token.PROGRAM, null);
    Node root = newNode(Token.BLOCK, null);

    inlineObjectLiterals.process(externs, root);

    // Then
    // This test fails intentionally as a way to test that the compiler
    // throws an error when it encounters a problem.
    Node object = newNode(Token.OBJECTLIT, null,
      newNode(Token.PROPERTIES, null,
        newNode(Token.ASSIGN, null,
          newNode(Token.NAME, "a"),
          NodeUtil.newVarNode("invalid", "invalid")
        )
      )
    );

    Node objectNode = root.getFirstChild().findChildByName("test_");
    Node actualObject = NodeUtil.findDeep(objectNode, object);
    Node expectedObject = object;
    assertEquals(expectedObject, actualObject);
  }
}