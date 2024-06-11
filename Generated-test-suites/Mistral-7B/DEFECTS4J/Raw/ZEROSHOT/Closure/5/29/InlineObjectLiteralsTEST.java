import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.newVar;
import static com.google.javascript.rhino.NodeFactory.newCallExpr;
import static com.google.javascript.rhino.NodeFactory.newGetProp;
import static com.google.javascript.rhino.NodeFactory.newVarDecl;
import static com.google.javascript.rhino.NodeFactory.newAssign;
import static com.google.javascript.rhino.NodeFactory.newName;
import static com.google.javascript.rhino.NodeFactory.newUndefined;
import static com.google.javascript.rhino.NodeFactory.newComma;
import static com.google.javascript.rhino.NodeFactory.newIdentifier;
import static com.google.javascript.rhino.NodeFactory.newStringLiteral;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope(null);
    VariableVariable var1 = new VariableVariable("obj1", null, null, false, scope);
    VariableVariable var2 = new VariableVariable("obj2", null, null, false, scope);

    // Test with no references to object literals
    Node root = newVarDecl(newName("externs"), null, false);
    root = newAssign(root, newName("externs"), newObjectLiteral( Sets.newHashSet() ));
    root = newVarDecl(newName("root"), null, root, false, scope);

    inlineObjectLiterals.process(null, root);
    assertEquals(2, scope.getVars().size());

    // Test with an object literal reference
    scope = new Scope(null);
    root = newVarDecl(newName("externs"), null, false);
    root = newAssign(root, newName("externs"), newObjectLiteral( Sets.newHashSet() ));
    root = newVarDecl(newName("root"), null, root, false, scope);

    Node objLiteral = newObjectLiteral(Sets.newHashSet(
        newPair("key1", newStringLiteral("value1")),
        newPair("key2", newStringLiteral("value2"))));

    Var objVar = newVar("obj", null, null, false, scope);
    root = newAssign(objVar, newName("obj"), objLiteral);

    ReferenceCollection refs = new ReferenceCollection();
    refs.add(new Reference(newName("obj"), objVar, null, null, null, null, null));
    refs.add(new Reference(newGetProp(newName("obj"), newIdentifier("key1")), null, null, null, null, null, null, objVar));
    refs.add(new Reference(newGetProp(newName("obj"), newIdentifier("key2")), null, null, null, null, null, null, objVar));

    inlineObjectLiterals.process(null, root);

    assertEquals(5, scope.getVars().size());
    Map<String, String> expectedMap = Maps.newLinkedHashMap();
    expectedMap.put("key1", "obj_key1_test");
    expectedMap.put("key2", "obj_key2_test");
    expectedMap.put("value1", "obj_value1_test");
    expectedMap.put("value2", "obj_value2_test");
    Map<String, Node> actualMap = Maps.newHashMap();

    for (Var v : scope.getVars()) {
      String name = v.getName();
      if (name.startsWith("obj_")) {
        String index = name.substring(3);
        String key = "";
        int indexOfUnderscore = index.lastIndexOf("_");
        if (indexOfUnderscore > -1) {
          key = index.substring(0, indexOfUnderscore);
          index = index.substring(indexOfUnderscore + 1);
        }
        actualMap.put(key, v.getValue());
      }
    }

    assertEquals(expectedMap, actualMap);

    // Test with a call to a method on an object literal
    scope = new Scope(null);
    root = newVarDecl(newName("externs"), null, false);
    root = newAssign(root, newName("externs"), newObjectLiteral( Sets.newHashSet() ));
    root = newVarDecl(newName("root"), null, root, false, scope);

    objLiteral = newObjectLiteral(Sets.newHashSet(
        newPair("key1", newStringLiteral("value1")),
        newPair("key2", newFunctionLiteral(
            newName("fn"),
            newList(newName("arg"))))));

    objVar = newVar("obj", null, null, false, scope);
    root = newAssign(objVar, newName("obj"), objLiteral);

    ReferenceCollection refs2 = new ReferenceCollection();
    refs2.add(new Reference(newName("obj"), objVar, null, null, null, null, null));
    refs2.add(new Reference(newCallExpr(
        newIdentifier("obj"),
        newList(newIdentifier("fn"))),
        null, null, null, null, null, null, objVar));

    inlineObjectLiterals.process(null, root);

    assertEquals(7, scope.getVars().size());
    Map<String, String> expectedMap2 = Maps.newLinkedHashMap();
    expectedMap2.put("fn", "obj_fn_test");
    expectedMap2.put("arg", "obj_arg_test");
    expectedMap2.put("key1", "obj_key1_test");
    expectedMap2.put("key2", "obj_key2_test");
    expectedMap2.put("value1", "obj_value1_test");
    expectedMap2.put("arg_value", "obj_arg_value_test");

    Map<String, Node> actualMap2 = Maps.newHashMap();

    for (Var v : scope.getVars()) {
      String name = v.getName();
      if (name.startsWith("obj_")) {
        String index = name.substring(3);
        String key = "";
        int indexOfUnderscore = index.lastIndexOf("_");
        if (indexOfUnderscore > -1) {
          key = index.substring(0, indexOfUnderscore);
          index = index.substring(indexOfUnderscore + 1);
        }
        actualMap2.put(key, v.getValue());
      } else if (name.equals("fn")) {
        actualMap2.put(name, newFunctionLiteral(
            newName("fn"),
            newList(newIdentifier("arg"))));
      } else if (name.equals("arg")) {
        actualMap2.put(name, newIdentifier("arg"));
      }
    }

    assertEquals(expectedMap2, actualMap2);
  }

  private static Node newPair(String key, Node value) {
    return new ObjectLiteral(
        Lists.newArrayList(
            newProperty(newName(key), value)
        )
    );
  }

  private static Node newFunctionLiteral(String name, List<Node> arguments) {
    return newCallExpr(
        newName("Function"),
        Lists.newArrayList(
            newArrayLiteral(
                Lists.newArrayList(
                    newName(name),
                    newIdentifier("this"),
                    newArrayLiteral(arguments)
                )
            )
        )
    );
  }

  private static Node newArrayLiteral(List<Node> elements) {
    return newArrayLiteral(newList(elements.toArray()));
  }

  private static Node newArrayLiteral(List<Node> elements, boolean isFunction) {
    List<Node> list = Lists.newArrayList();
    list.add(newIdentifier("function"));
    list.addAll(elements);
    return newArrayLiteral(newList(list.toArray()));
  }

  private static Node newObjectLiteral(List<Node> properties) {
    return newObjectLiteral(newList(properties.toArray()));
  }

  private static Node newObjectLiteral(List<Node> properties, boolean isFunction) {
    List<Node> list = Lists.newArrayList();
    list.add(newIdentifier("function"));
    list.addAll(properties);
    return newObjectLiteral(newList(list.toArray()));
  }

  private static Node newProperty(String name, Node value) {
    return newObjectLiteral(
        Lists.newArrayList(
            newPropertyName(name),
            newName("value"),
            value
        )
    );
  }

  private static Node newPropertyName(String name) {
    return newName(name);
  }

  private static Node newIdentifier(String name) {
    return newName(name);
  }

  private static Node newList(Node... nodes) {
    List<Node> list = Lists.newArrayList();
    list.addAll(Lists.newArrayList(nodes));
    return newArrayLiteral(newList(list.toArray()));
  }
}