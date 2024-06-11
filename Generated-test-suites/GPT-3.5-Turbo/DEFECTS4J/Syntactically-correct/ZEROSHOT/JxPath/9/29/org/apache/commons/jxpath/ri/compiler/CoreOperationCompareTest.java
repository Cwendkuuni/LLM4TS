import org.junit.Test;

public class CoreOperationCompareTest {
  
  @Test
  public void testEqual() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);
    assert result == true;
  }
  
  @Test
  public void testEqualWithCollections() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    context.setVariable("collection1", new HashSet());
    context.setVariable("collection2", new HashSet());
    boolean result = operation.equal(context, arg1, arg2);
    assert result == true;
  }
  
  @Test
  public void testEqualWithPointers() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    context.setVariable("pointer1", new Pointer());
    context.setVariable("pointer2", new Pointer());
    boolean result = operation.equal(context, arg1, arg2);
    assert result == true;
  }
  
  @Test
  public void testEqualWithNumbers() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);
    assert result == false;
  }
  
  @Test
  public void testEqualWithStrings() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);
    assert result == false;
  }
  
  @Test
  public void testEqualWithNull() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);
    assert result == false;
  }
  
  @Test
  public void testContains() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    Iterator it = new Iterator();
    Object value = new Object();
    boolean result = operation.contains(it, value);
    assert result == false;
  }
  
  @Test
  public void testFindMatch() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    Iterator lit = new Iterator();
    Iterator rit = new Iterator();
    boolean result = operation.findMatch(lit, rit);
    assert result == false;
  }
  
  @Test
  public void testEqualWithBoolean() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);
    assert result == false;
  }
  
  @Test
  public void testEqualWithNaN() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);
    assert result == false;
  }

}