
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

  @Test
  public void testFlowThroughVariableStatement_addToUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    Node varStatement = new Node(Token.VAR);
    Node varName = new Node(Token.NAME, "x");
    Node varValue = new Node(Token.NUMBER, "10");
    varStatement.addChildToBack(varName);
    varStatement.addChildToBack(varValue);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(varStatement, input);
    
    assertEquals(1, output.mayUseMap.size());
    assertEquals(varValue, output.mayUseMap.get(varName).iterator().next());
  }

  @Test
  public void testFlowThroughVariableStatement_removeFromUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    Node varStatement = new Node(Token.VAR);
    Node varName = new Node(Token.NAME, "x");
    Node varValue = new Node(Token.NUMBER, "10");
    varStatement.addChildToBack(varName);
    varStatement.addChildToBack(varValue);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(varStatement, input);

    Node newVarValue = new Node(Token.NUMBER, "20");
    varStatement.replaceChild(varValue, newVarValue);
    output = maybeReachingVariableUse.flowThrough(varStatement, output);

    assertEquals(1, output.mayUseMap.size());
    assertEquals(1, output.mayUseMap.get(varName).size());
    assertEquals(newVarValue, output.mayUseMap.get(varName).iterator().next());
  }

  @Test
  public void testFlowThroughAssignmentExpression_addToUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    Node assignmentExpression = new Node(Token.ASSIGN);
    Node varName = new Node(Token.NAME, "x");
    Node varValue = new Node(Token.NUMBER, "10");
    assignmentExpression.addChildToBack(varName);
    assignmentExpression.addChildToBack(varValue);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(assignmentExpression, input);

    assertEquals(1, output.mayUseMap.size());
    assertEquals(varValue, output.mayUseMap.get(varName).iterator().next());
  }

  @Test
  public void testFlowThroughAssignmentExpression_removeFromUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    Node assignmentExpression = new Node(Token.ASSIGN);
    Node varName = new Node(Token.NAME, "x");
    Node varValue = new Node(Token.NUMBER, "10");
    assignmentExpression.addChildToBack(varName);
    assignmentExpression.addChildToBack(varValue);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(assignmentExpression, input);

    Node newVarValue = new Node(Token.NUMBER, "20");
    assignmentExpression.replaceChild(varValue, newVarValue);
    output = maybeReachingVariableUse.flowThrough(assignmentExpression, output);

    assertEquals(1, output.mayUseMap.size());
    assertEquals(1, output.mayUseMap.get(varName).size());
    assertEquals(newVarValue, output.mayUseMap.get(varName).iterator().next());
  }

  @Test
  public void testFlowThroughFunction_addToUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    Node funcNode = new Node(Token.FUNCTION);
    Node funcName = new Node(Token.NAME, "myFunction");
    Node param = new Node(Token.NAME, "param");
    Node paramValue = new Node(Token.NUMBER, "10");
    funcNode.addChildToBack(funcName);
    funcNode.addChildToBack(param);
    funcNode.addChildToBack(paramValue);
    
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(funcNode, input);
    
    assertEquals(1, output.mayUseMap.size());
    assertEquals(paramValue, output.mayUseMap.get(param).iterator().next());
  }

  @Test
  public void testFlowThroughIfStatement() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    Node ifStatement = new Node(Token.IF);
    Node condition = new Node(Token.TRUE);
    Node dupeVarNode = new Node(Token.VAR);
    Node left = new Node(Token.NAME, "x");
    Node right = new Node(Token.NUMBER, "10");
    dupeVarNode.addChildToBack(left);
    dupeVarNode.addChildToBack(right);
    ifStatement.addChildToBack(condition);
    ifStatement.addChildToBack(dupeVarNode);

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(ifStatement, input);

    assertEquals(1, output.mayUseMap.size());
    assertEquals(right, output.mayUseMap.get(left).iterator().next());
  }
}
