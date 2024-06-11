import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class FunctionRewriterTest {
  private FunctionRewriter functionRewriter;
  private Node rootNode;

  @Before
  public void setUp() {
    FunctionRewriter compiler = new FunctionRewriter(new Compiler());
    functionRewriter = compiler;

    rootNode = createTestNode();
  }

  @Test
  public void testEmptyFunctionReduction() {
    functionRewriter.process(null, rootNode);

    Node emptyFunction = rootNode.getFirstChild().getNext();
    Node emptyFunctionHelper = emptyFunction.getFirstChild();

    // Verify helper function was added
    assertThat(emptyFunctionHelper.getNumChildren()).isEqualTo(0);
    assertThat(emptyFunctionHelper.getString()).isEqualTo("emptyFn");

    // Verify empty function was replaced
    Node oldEmptyFunction = emptyFunction.getLastChild();
    Node newEmptyFunction = emptyFunction.getFirstChild();
    assertThat(oldEmptyFunction.getNumChildren()).isGreaterThan(0);
    assertThat(newEmptyFunction.getNumChildren()).isEqualTo(0);
  }

  @Test
  public void testIdentityFunctionReduction() {
    Node identityFunction = new Node(Token.FUNCTION, 1, 0);
    identityFunction.addChildToBack(new Node(Token.PARAM, 0, 0, "a"));
    identityFunction.addChildToBack(new Node(Token.RETURN, 1, 0, identityFunction));
    identityFunction.addChildToBack(new Node(Token.NAME, 1, 0, "a"));

    rootNode.addChildToBack(identityFunction);

    functionRewriter.process(null, rootNode);

    Node oldIdentityFunction = identityFunction;
    identityFunction = identityFunction.getLastChild();

    // Verify helper function was added
    Node identityFunctionHelper = identityFunction.getFirstChild();
    assertThat(identityFunctionHelper.getNumChildren()).isEqualTo(1);
    assertThat(identityFunctionHelper.getString()).isEqualTo("identityFn");

    // Verify identity function was replaced
    Node newIdentityFunction = identityFunction.getFirstChild();
    Node newIdentityFunctionHelper = newIdentityFunction.getFirstChild();
    assertThat(oldIdentityFunction.getNumChildren()).isGreaterThan(0);
    assertThat(newIdentityFunction.getNumChildren()).isEqualTo(1);
    assertThat(newIdentityFunctionHelper.getString()).isEqualTo("identityFn_value");
  }

  @Test
  public void testGetterReduction() {
    Node getterFunction = new Node(Token.FUNCTION, 1, 0);
    getterFunction.addChildToBack(new Node(Token.PARAM, 0, 0, "a"));
    getterFunction.addChildToBack(new Node(Token.RETURN, 1, 0, new Node(Token.GETPROP, 1, 0, new Node(Token.THIS, 1, 0), new Node(Token.STRING, 1, 0, "a"))));

    rootNode.addChildToBack(getterFunction);

    functionRewriter.process(null, rootNode);

    Node oldGetterFunction = getterFunction;
    getterFunction = getterFunction.getLastChild();

    // Verify helper function was added
    Node getterFunctionHelper = getterFunction.getFirstChild();
    assertThat(getterFunctionHelper.getNumChildren()).isEqualTo(1);
    assertThat(getterFunctionHelper.getString()).isEqualTo("getter");

    // Verify getter function was replaced
    Node newGetterFunction = getterFunction.getFirstChild();
    Node newGetterFunctionHelper = newGetterFunction.getFirstChild();
    Node getterFunctionName = newGetterFunction.getLastChild();
    assertThat(oldGetterFunction.getNumChildren()).isGreaterThan(0);
    assertThat(newGetterFunction.getNumChildren()).isEqualTo(1);
    assertThat(newGetterFunctionHelper.getString()).isEqualTo("getter_name");
    assertThat(getterFunctionName.getString()).isEqualTo("a");
  }

  @Test
  public void testSetterReduction() {
    Node setterFunction = new Node(Token.FUNCTION, 1, 0);
    setterFunction.addChildToBack(new Node(Token.PARAM, 0, 0, "value"));
    setterFunction.addChildToBack(new Node(Token.VAR, 1, 0, "a", new Node(Token.THIS, 1, 0), new Node(Token.NAME, 1, 0, "a")));
    setterFunction.addChildToBack(new Node(Token.ASSIGN, 1, 0, new Node(Token.GETPROP, 1, 0, new Node(Token.THIS, 1, 0), new Node(Token.STRING, 1, 0, "a")), new Node(Token.NAME, 1, 0, "value")));

    rootNode.addChildToBack(setterFunction);

    functionRewriter.process(null, rootNode);

    Node oldSetterFunction = setterFunction;
    setterFunction = setterFunction.getLastChild();

    // Verify helper function was added
    Node setterFunctionHelper = setterFunction.getFirstChild();
    assertThat(setterFunctionHelper.getNumChildren()).isEqualTo(1);
    assertThat(setterFunctionHelper.getString()).isEqualTo("setter");

    // Verify setter function was replaced
    Node newSetterFunction = setterFunction.getFirstChild();
    Node newSetterFunctionHelper = newSetterFunction.getFirstChild();
    Node setterFunctionName = newSetterFunction.getLastChild();
    Node setterFunctionParam = newSetterFunction.getFirstChild().getNext();
    Node setterFunctionValue = newSetterFunction.getLastChild();

    assertThat(oldSetterFunction.getNumChildren()).isGreaterThan(0);
    assertThat(newSetterFunction.getNumChildren()).isEqualTo(2);
    assertThat(newSetterFunctionHelper.getString()).isEqualTo("setter_name");
    assertThat(setterFunctionName.getString()).isEqualTo("a");
    assertThat(setterFunctionParam.getType()).isEqualTo(Token.NAME);
    assertThat(setterFunctionParam.getString()).isEqualTo("value");
    assertThat(newSetterFunctionValue.getString()).isEqualTo("value");
  }

  private Node createTestNode() {
    Node root = new Node(Token.PROGRAM, 1, 0);

    Node emptyFunction = new Node(Token.FUNCTION, 0, 0);
    root.addChildToBack(emptyFunction);

    Node identityFunction = new Node(Token.FUNCTION, 1, 0);
    root.addChildToBack(identityFunction);

    Node getterFunction = new Node(Token.FUNCTION, 1, 0);
    root.addChildToBack(getterFunction);

    Node setterFunction = new Node(Token.FUNCTION, 1, 0);
    root.addChildToBack(setterFunction);

    return root;
  }
}