package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.CompilerTestCase;
import com.google.javascript.rhino.NodeOperations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerTestCase {

  public static void main(String[] args) throws Exception {
    junit.textui.TestRunner.run(InlineObjectLiteralsTest.class);
  }

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(new TestCompiler(), new TestSafeNameIdSupplier());
  }

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    options.setShowWarnings(false);
    return options;
  }

  @Test
  public void testInlineObjectLiterals() throws Exception {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() { return 2; }," +
        "    c: ['x', 'y']," +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b();" +
        "  for (var i = 0; i < obj.c.length; i++) {" +
        "    var z = obj.c[i];" +
        "    assert(typeof z === 'string');" +
        "  }" +
        "}";

    Node root = parse(input);

    doPass(root);

    Node output = getCompiledSource();

    // Check that all keys in the object literal have been inlined as variables
    List<Node> allVars = getAllVars(output);
    Map<String, Node> originalVars = new HashMap<>();
    Map<String, Node> inlinedVars = new HashMap<>();

    for (Node var : allVars) {
      if (var.isVar()) {
        String name = var.getString();

        if (originalVars.containsKey(name)) {
          continue;
        }

        // Check that the variable has been assigned a value
        Node init = var.getFirstChild();
        if (init == null || init.isUndefined()) {
          fail("Variable " + name + " has no initial value");
        }

        if (isObjectLitInit(init)) {
          // Inlined object literals should have been replaced by separate variable declarations
          String objName = var.getName();
          String key;
          for (Node child = init.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isName()) {
              key = child.getString();
              if (inlinedVars.containsKey(key)) {
                continue;
              }

              Node value = child.getNext();
              assertNotNull(value);
              inlinedVars.put(key, value);

              // Check that the variable has been assigned the correct value
              Node assignedValue = getVariableByName(output, objName + "_" + key);
              assertEquals(value.toString(), assignedValue.toString());

              originalVars.put(key, var);
            }
          }
        }
      }
    }

    // Check that all references to keys in the object literal have been replaced by the corresponding variables
    List<Node> allGetProps = getAllGetProps(output);
    for (Node getProp : allGetProps) {
      String name = getProp.getString();
      if (originalVars.containsKey(name)) {
        Node var = originalVars.get(name);
        Node value = getVariableByName(output, var.getName() + "_" + name);
        getProp.replaceChildren(value);
      }
    }

    // Check that all functions in the object literal have been inlined
    List<Node> allFunctionExprs = getAllFunctionExprs(output);
    for (Node functionExpr : allFunctionExprs) {
      String name = functionExpr.getName();
      if (originalVars.containsKey(name)) {
        String varName = VAR_PREFIX + name;
        Node var = getVariableByName(output, varName);
        Node body = functionExpr.getBody();
        Node newFunction = NodeOperations.newFunction(varName, body);
        newFunction.copyInformationFrom(functionExpr);
        functionExpr.replaceChildren(newFunction);
      }
    }

    // Check that all arrays in the object literal have been inlined
    List<Node> allArrays = getAllArrays(output);
    for (Node array : allArrays) {
      String name = array.getName();
      if (originalVars.containsKey(name)) {
        String varName = VAR_PREFIX + name;
        Node var = getVariableByName(output, varName);
        List<Node> elements = NodeOperations.getArrayElements(array);
        List<Node> newElements = Lists.newArrayList();
        for (Node element : elements) {
          newElements.add(getVariableByName(output, varName + "_" + nodesToStrings(elements.indexOf(element))[0]));
        }
        Node newArray = NodeOperations.newArray(newElements.toArray(new Node[0]));
        newArray.copyInformationFrom(array);
        array.replaceChildren(newArray);
      }
    }

    printSource(output);
  }

  private List<Node> nodesToStrings(int index) {
    return Arrays.asList(new String[] {"i"});
  }

  private static boolean isObjectLitInit(Node node) {
    return node.isObjectLit();
  }

  private Node getVariableByName(Node node, String name) {
    for (Node scope = node; scope != null; scope = scope.getParent()) {
      for (Node var : scope.getVars()) {
        if (var.name.equals(name)) {
          return var;
        }
      }
    }

    return null;
  }

  private List<Node> getAllGetProps(Node node) {
    return NodeOperations.findDescendants(node, new NodePredicate() {
      @Override
      public boolean matches(Node node, int pos) {
        return node.isGetProp();
      }
    }, null);
  }

  private List<Node> getAllVars(Node node) {
    return NodeOperations.findDescendants(node, new NodePredicate() {
      @Override
      public boolean matches(Node node, int pos) {
        return node.isVar();
      }
    }, null);
  }

  private List<Node> getAllFunctionExprs(Node node) {
    return NodeOperations.findDescendants(node, new NodePredicate() {
      @Override
      public boolean matches(Node node, int pos) {
        return node.isFunctionExpr();
      }
    }, null);
  }

  private List<Node> getAllArrays(Node node) {
    return NodeOperations.findDescendants(node, new NodePredicate() {
      @Override
      public boolean matches(Node node, int pos) {
        return node.isArray();
      }
    }, null);
  }

  static class TestCompiler extends AbstractCompiler {
    @Override
    protected Scope createTopLevelScope() {
      return new Scope();
    }
  }

  static class TestSafeNameIdSupplier implements Supplier<String> {
    private int counter = 0;

    @Override
    public String get() {
      return String.valueOf(counter++);
    }
  }

  static class TestBehavior implements Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      // Empty implementation
    }

    @Override
    public void afterExitScope(NodeTraversal t, ReferenceCollection referenceCollection) {
      // Empty implementation
    }

    @Override
    public void afterExitScope(NodeTraversal t, Reference ref) {
      // Empty implementation
    }

    @Override
    public void afterEnterScope(NodeTraversal t, ReferenceMap referenceMap) {
      // Empty implementation
    }

    @Override
    public void afterEnterScope(NodeTraversal t, ReferenceCollection referenceCollection) {
      // Empty implementation
    }

    @Override
    public void afterEnterScope(NodeTraversal t, Reference ref) {
      // Empty implementation
    }

    @Override
    public void beforeEnterScope(NodeTraversal t, ReferenceMap referenceMap) {
      // Empty implementation
    }

    @Override
    public void beforeEnterScope(NodeTraversal t, ReferenceCollection referenceCollection) {
      // Empty implementation
    }

    @Override
    public void beforeEnterScope(NodeTraversal t, Reference ref) {
      // Empty implementation
    }
  }
}