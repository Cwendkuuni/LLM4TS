import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;

import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {
  private DevirtualizePrototypeMethods method;

  @Before
  public void setUp() {
    method = new DevirtualizePrototypeMethods();
  }

  @Test
  public void testRewriteCallSites() {
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    Definition definition = new Definition();
    String newMethodName = "newMethodName";
    method.rewriteCallSites(defFinder, definition, newMethodName);

    // verify that the call sites are rewritten correctly
  }

  @Test
  public void testRewriteDefinition() {
    Node node = new Node();
    String newMethodName = "newMethodName";
    method.rewriteDefinition(node, newMethodName);

    // verify that the method definition is rewritten correctly
  }

  @Test
  public void testFixFunctionType() {
    Node functionNode = new Node();
    method.fixFunctionType(functionNode);

    // verify that the function type is fixed correctly
  }

  @Test
  public void testReplaceReferencesToThis() {
    Node node = new Node();
    String name = "name";
    method.replaceReferencesToThis(node, name);

    // verify that references to "this" are replaced correctly
  }
}