package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Supplier;

import java.util.List;
import java.util.Map;

/**
 * Tests for the InlineObjectLiterals class.
 */
public class InlineObjectLiteralsTest {

  @SuppressWarnings("unchecked")
  private static <T> T newNode(int type, Node... children) {
    Node node = new Node(type);
    for (Node child : children) {
      node.addChild(child);
    }
    return (T) node;
  }

  @SuppressWarnings("unchecked")
  private static <T> T newVar(String name, T initialValue) {
    Var var = new Var(name, null, null, 0, null, null, null, null);
    var.setInitialValue(initialValue);
    return (T) var;
  }

  @SuppressWarnings("unchecked")
  private static <T> T newReference(Var var, T assignedValue) {
    Reference reference = new Reference();
    reference.setVar(var);
    reference.setAssignedValue(assignedValue);
    return (T) reference;
  }

  @SuppressWarnings("unchecked")
  private static <T> T newReferenceCollection(List<Reference> references) {
    ReferenceCollection referenceCollection = new ReferenceCollection();
    referenceCollection.setReferences((List<Reference>) references);
    return (T) referenceCollection;
  }

  @SuppressWarnings("unchecked")
  private static <T> T newReferenceMap(Map<Var, ReferenceCollection> referenceMap) {
    ReferenceMap referenceMap1 = new ReferenceMap();
    referenceMap1.setReferenceMap((Map<Var, ReferenceCollection>) referenceMap);
    return (T) referenceMap1;
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Node> newList(T... nodes) {
    return Lists.newArrayList((Node[]) nodes);
  }

  @SuppressWarnings("unchecked")
  private static <T> Map<String, T> newMap(Map<String, T> map, String key, T value) {
    map.put(key, value);
    return map;
  }

  /**
   * Test method for {@link InlineObjectLiterals#process(Node, Node)}.
   */
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node root = newNode(Token.PROGRAM,
        newNode(Token.BLOCK,
            newNode(Token.VAR, "obj", newNode(Token.OBJECT_LITERAL,
                newList(
                    newNode(Token.PROPERTIES,
                        newList(
                            newNode(Token.KEY, "prop1"),
                            newNode(Token.VALUE, newNode(Token.NUMBER, 1)),
                            newNode(Token.KEY, "prop2"),
                            newNode(Token.VALUE, newNode(Token.STRING, "hello")))
                    ),
                newList())),
                newNode(Token.EXPR_STATEMENT,
                    newNode(Token.ASSIGN,
                        newNode(Token.NAME, "obj"),
                        newNode(Token.OBJECT_LITERAL,
                            newList(
                                newNode(Token.PROPERTIES,
                                    newList(
                                        newNode(Token.KEY, "prop1"),
                                        newNode(Token.VALUE, newNode(Token.NUMBER, 2)),
                                        newNode(Token.KEY, "prop3"),
                                        newNode(Token.VALUE, newNode(Token.STRING, "world")))
                                    ),
                                newList())
                            )
                        )
                    )
                )
            ),
            newVar("obj", null)
        );

    inlineObjectLiterals.process(null, root);

    // The object literal should be split into individual variables.
    Map<String, Var> expectedVars = Maps.newLinkedHashMap();
    Var obj = newVar("obj", null);
    expectedVars.put("prop1", newVar("prop1", null));
    expectedVars.put("prop2", newVar("prop2", new Node(Token.NUMBER, 1)));
    expectedVars.put("prop3", newVar("prop3", new Node(Token.STRING, "world")));
    Map<Var, ReferenceCollection> referenceMap = Map.of(obj, newReferenceCollection(
        Lists.newArrayList(
            newReference(expectedVars.get("prop1"), null),
            newReference(expectedVars.get("prop2"), new Node(Token.NUMBER, 1)),
            newReference(expectedVars.get("prop3"), new Node(Token.STRING, "world")))
    ));

    // Check that the variables have been declared and initialized correctly.
    for (Map.Entry<String, Var> entry : expectedVars.entrySet()) {
      Var actualVar = compiler.getRootScope().getVar(entry.getKey());
      assertEquals("Variable " + entry.getKey() + " has not been declared.",
          entry.getValue(), actualVar);
      assertEquals("Variable " + entry.getKey() + " has not been initialized.",
          newNode(Token.ASSIGN, new Node(Token.NAME, entry.getKey()), null),
          actualVar.getInitialValue());
    }

    // Check that the object literal has been replaced by the assigned variables.
    Node actualObj = root.getLastChild().getFirstChild();
    List<Node> actualChildren = actualObj.getFirstChild().getChildren();
    assertEquals("Unexpected number of children for the object literal.", 3, actualChildren.size());
    for (int i = 0; i < 3; i++) {
      Node actualChild = actualChildren.get(i);
      String key = "";
      if (i == 0) {
        key = "prop1";
      } else if (i == 1) {
        key = "prop2";
      } else if (i == 2) {
        key = "prop3";
      }
      Node expectedChild = newNode(Token.NAME, expectedVars.get(key).getName());
      assertEquals("Unexpected key for child " + i + ".", key, expectedChild.getString());
      assertEquals("Unexpected value for child " + i + ". (Expected: " + expectedChild + ")", expectedChild, actualChild);
    }
  }

  /**
   * Test method for {@link InlineObjectLiterals#process(Node, Node)} with
   * an object literal that is never referenced directly.
   */
  @Test
  public void testProcessWithUnusedObject() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node root = newNode(Token.PROGRAM,
        newNode(Token.BLOCK,
            newNode(Token.VAR, "obj", newNode(Token.OBJECT_LITERAL,
                newList(
                    newNode(Token.PROPERTIES,
                        newList(
                            newNode(Token.KEY, "prop1"),
                            newNode(Token.VALUE, newNode(Token.NUMBER, 1)),
                            newNode(Token.KEY, "prop2"),
                            newNode(Token.VALUE, newNode(Token.STRING, "hello")))
                    ),
                newList())),
                newNode(Token.EXPR_STATEMENT,
                    newNode(Token.ASSIGN,
                        newNode(Token.NAME, "obj"),
                        newNode(Token.OBJECT_LITERAL,
                            newList(
                                newNode(Token.PROPERTIES,
                                    newList(
                                        newNode(Token.KEY, "prop1"),
                                        newNode(Token.VALUE, newNode(Token.NUMBER, 2)),
                                        newNode(Token.KEY, "prop3"),
                                        newNode(Token.VALUE, newNode(Token.STRING, "world")))
                                    ),
                                newList())
                            )
                        )
                    )
                )
            ),
            newVar("unused", null)
        );

    inlineObjectLiterals.process(null, root);

    // The object literal should be split into individual variables.
    Map<String, Var> expectedVars = Maps.newLinkedHashMap();
    Var obj = newVar("obj", null);
    expectedVars.put("prop1", newVar("prop1", null));
    expectedVars.put("prop2", newVar("prop2", new Node(Token.NUMBER, 1)));
    expectedVars.put("prop3", newVar("prop3", new Node(Token.STRING, "world")));
    Map<Var, ReferenceCollection> referenceMap = Map.of(obj, newReferenceCollection(
        Lists.newArrayList(
            newReference(expectedVars.get("prop1"), null),
            newReference(expectedVars.get("prop2"), new Node(Token.NUMBER, 1)),
            newReference(expectedVars.get("prop3"), new Node(Token.STRING, "world")))
    ));

    // Check that the variables have been declared and initialized correctly.
    for (Map.Entry<String, Var> entry : expectedVars.entrySet()) {
      Var actualVar = compiler.getRootScope().getVar(entry.getKey());
      assertEquals("Variable " + entry.getKey() + " has not been declared.",
          entry.getValue(), actualVar);
      assertEquals("Variable " + entry.getKey() + " has not been initialized.",
          newNode(Token.ASSIGN, new Node(Token.NAME, entry.getKey()), null),
          actualVar.getInitialValue());
    }

    // Check that the object literal has been replaced by the assigned variables.
    Node actualObj = root.getLastChild().getFirstChild();
    List<Node> actualChildren = actualObj.getFirstChild().getChildren();
    assertEquals("Unexpected number of children for the object literal.", 3, actualChildren.size());
    for (int i = 0; i < 3; i++) {
      Node actualChild = actualChildren.get(i);
      String key = "";
      if (i == 0) {
        key = "prop1";
      } else if (i == 1) {
        key = "prop2";
      } else if (i == 2) {
        key = "prop3";
      }
      Node expectedChild = newNode(Token.NAME, expectedVars.get(key).getName());
      assertEquals("Unexpected key for child " + i + ".", key, expectedChild.getString());
      assertEquals("Unexpected value for child " + i + ". (Expected: " + expectedChild + ")", expectedChild, actualChild);
    }

    // Check that the object literal is not referenced directly.
    Node actualUnused = root.getLastChild().getFirstChild().getFirstChild().getFirstChild();
    assertEquals("Variable 'unused' has been referenced directly.", null, actualUnused);
  }
}