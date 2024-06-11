import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThanPositive() {
 Expression arg1 = new Literal(5.0);
 Expression arg2 = new Literal(3.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
 EvalContext context = new EvalContext();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 NodeInfo nodeInfo = new NodeInfo("root", new Path("/"), null, null);
 infoSet.setRootNode(nodeInfo);
 op.computeValue(context);
 assertTrue(op.getValue().booleanValue());
 }

 @Test
 public void testGreaterThanZero() {
 Expression arg1 = new Literal(0.0);
 Expression arg2 = new Literal(5.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
 EvalContext context = new EvalContext();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 NodeInfo nodeInfo = new NodeInfo("root", new Path("/"), null, null);
 infoSet.setRootNode(nodeInfo);
 op.computeValue(context);
 assertFalse(op.getValue().booleanValue());
 }

 @Test
 public void testGreaterThanEqual() {
 Expression arg1 = new Literal(5.0);
 Expression arg2 = new Literal(5.0);
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
 EvalContext context = new EvalContext();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 NodeInfo nodeInfo = new NodeInfo("root", new Path("/"), null, null);
 infoSet.setRootNode(nodeInfo);
 op.computeValue(context);
 assertTrue(op.getValue().booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanNull() {
 Expression arg1 = new Literal(5.0);
 Expression arg2 = null;
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanNonNumeric() {
 Expression arg1 = new StringLiteral("5");
 Expression arg2 = new StringLiteral("3");
 CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
 }
}