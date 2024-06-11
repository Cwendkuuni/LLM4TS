import org.apache.commons.jxpath.ri.CompilerTest; import org.apache.commons.jxpath.ri.InfoSet; import org.apache.commons.jxpath.ri.JXPathContext; import org.apache.commons.jxpath.ri.NodeInfo; import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest extends CompilerTest {

 @Test public void testGreaterThanOrEqual() {
 // Test with double values, both greater than or equal to each other
 NodeInfo arg1 = createDoubleNodeInfo(5.0);
 NodeInfo arg2 = createDoubleNodeInfo(7.0);
 JXPathContext ctx = new JXPathContext(createDocument());
 InfoSet is = createInfoSet(arg1, arg2);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(args[0], args[1]);
 Object result = expression.computeValue(ctx, is);
 assertTrue(result.equals(Boolean.TRUE));

 // Test with double values, arg1 less than arg2
 arg1 = createDoubleNodeInfo(3.0);
 arg2 = createDoubleNodeInfo(5.0);
 result = expression.computeValue(ctx, createInfoSet(arg1, arg2));
 assertFalse(result.equals(Boolean.TRUE));
 assertFalse(result.equals(Boolean.FALSE));
 assertNull(result);

 // Test with double values, arg1 equal to arg2
 arg1 = createDoubleNodeInfo(5.0);
 arg2 = createDoubleNodeInfo(5.0);
 result = expression.computeValue(ctx, createInfoSet(arg1, arg2));
 assertTrue(result.equals(Boolean.TRUE));

 // Test with null arguments
 arg1 = null;
 arg2 = null;
 result = expression.computeValue(ctx, createInfoSet(arg1, arg2));
 assertNull(result);

 // Test with integer values, both greater than or equal to each other
 arg1 = createIntNodeInfo(5);
 arg2 = createIntNodeInfo(7);
 result = expression.computeValue(ctx, createInfoSet(arg1, arg2));
 assertTrue(result.equals(Boolean.TRUE));

 // Test with integer values, arg1 less than arg2
 arg1 = createIntNodeInfo(3);
 arg2 = createIntNodeInfo(5);
 result = expression.computeValue(ctx, createInfoSet(arg1, arg2));
 assertFalse(result.equals(Boolean.TRUE));
 assertFalse(result.equals(Boolean.FALSE));
 assertNull(result);

 // Test with integer values, arg1 equal to arg2
 arg1 = createIntNodeInfo(5);
 arg2 = createIntNodeInfo(5);
 result = expression.computeValue(ctx, createInfoSet(arg1, arg2));
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test(expected = IllegalArgumentException.class) public void testInvalidArguments() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(args[0], null);
 }
}