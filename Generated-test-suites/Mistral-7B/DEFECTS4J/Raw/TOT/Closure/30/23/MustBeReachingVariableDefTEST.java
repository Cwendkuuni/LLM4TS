import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE = "function test() {" +
      "  var x = 1;" +
      "  var y = 2;" +
      "  var z = 3;" +
      "  if (1 > 2) {" +
      "    var x = 5;" +
      "  } else {" +
      "    var y = 5;" +
      "  }" +
      "  alert(x);" +
      "  alert(y);" +
      "}";

  private Scope jsScope;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setUp() {
    jsScope = new Scope();
    analyzer = new MustBeReachingVariableDef(
        ControlFlowGraph.create(Node.parse(TEST_CODE), jsScope),
        jsScope, new NoOpCompiler());
  }

  @Test
  public void testReachingDefinitionForVariableWithOneDefinition() {
    Node useNodeX = jsScope.getRootNode().getLastChild();
    Node defNodeX = analyzer.getDef("x", useNodeX);
    assertThat(defNodeX).isNotNull();
    assertThat(defNodeX.getLineNumber()).isEqualTo(1);
  }

  @Test
  public void testReachingDefinitionForVariableWithMultipleDefinitions() {
    Node useNodeY = jsScope.getRootNode().getLastChild().getPrevious();
    Node defNodeY1 = analyzer.getDef("y", useNodeY);
    Node defNodeY2 = analyzer.getRootNode().getFirstChild().getLastChild();
    assertThat(defNodeY1).isNotNull();
    assertThat(defNodeY1.getLineNumber()).isEqualTo(2);
    assertThat(defNodeY2).isNotNull();
    assertThat(defNodeY2.getLineNumber()).isEqualTo(5);

    // Check that the definition with the last use wins.
    Node useNodeY2 = jsScope.getRootNode().getLastChild();
    Node defNodeY3 = analyzer.getDef("y", useNodeY2);
    assertThat(defNodeY3).isSameInstance(defNodeY2);
  }

  @Test
  public void testNoReachingDefinitionForVariableWithNoDefinition() {
    Node useNodeZ = jsScope.getRootNode().getFirstChild();
    Node defNodeZ = analyzer.getDef("z", useNodeZ);
    assertThat(defNodeZ).isNull();
  }

  @Test
  public void testReachingDefinitionForVariableThatIsEscaped() {
    String escapedVariableName = "arguments";
    Node useNodeEscaped = jsScope.getRootNode().getFirstChild();
    Node defNodeEscaped = analyzer.getDef(escapedVariableName, useNodeEscaped);
    assertThat(defNodeEscaped).isNull();
  }

  @Test
  public void testReachingDefinitionForVariableThatIsOutOfScope() {
    String outOfScopeVariableName = "outerX";
    Node useNodeOutOfScope = Node.newIntLiteral(1);
    Node defNodeOutOfScope = analyzer.getDef(outOfScopeVariableName, useNodeOutOfScope);
    assertThat(defNodeOutOfScope).isNull();
  }

  @Test
  public void testReachingDefinitionForVariableThatDependsOnOutOfScopeVariable() {
    String outOfScopeVariableName = "outerX";
    String dependentVariableName = "z";
    Node useNodeDependent = jsScope.getRootNode().getLastChild();
    Node defNodeDependent = analyzer.getDef(dependentVariableName, useNodeDependent);
    assertThat(defNodeDependent).isNull();

    // Check that the use of "z" depends on "outerX"
    Node defNodeOutOfScope = analyzer.getDef(outOfScopeVariableName, null);
    assertThat(defNodeOutOfScope).isNull();
    boolean dependsOnOuterScope = analyzer.dependsOnOuterScopeVars(dependentVariableName, useNodeDependent);
    assertThat(dependsOnOuterScope).isTrue();
  }
}