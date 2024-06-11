import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

 private BaseUnivariateRealOptimizer<FUNC> optimizerMock;
 private RandomGenerator randomGeneratorMock;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 optimizerMock = mock(BaseUnivariateRealOptimizer.class);
 randomGeneratorMock = mock(RandomGenerator.class);
 }

 @Test
 public void testConstructor() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);

 Assert.assertNotNull(optimizer.optimizer);
 Assert.assertEquals(3, optimizer.starts);
 Assert.assertNotNull(optimizer.generator);
 }

 @Test
 public void testSetConvergenceChecker() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerMock = mock(ConvergenceChecker.class);

 optimizer.setConvergenceChecker(convergenceCheckerMock);
 verify(optimizer.optimizer).setConvergenceChecker(eq(convergenceCheckerMock));
 }

 @Test
 public void testGetConvergenceChecker() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerMock = mock(ConvergenceChecker.class);

 optimizer.optimizer.setConvergenceChecker(convergenceCheckerMock);
 Assert.assertEquals(convergenceCheckerMock, optimizer.getConvergenceChecker());
 }

 @Test
 public void testGetMaxEvaluations() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);

 Assert.assertNotNull(optimizer.optimizer);
 Assert.assertEquals(0, optimizer.getMaxEvaluations());
 optimizer.setMaxEvaluations(100);
 Assert.assertEquals(100, optimizer.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);

 Assert.assertEquals(0, optimizer.getEvaluations());
 }

 @Test
 public void testSetMaxEvaluations() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);

 optimizer.setMaxEvaluations(100);
 optimizer.optimizer.setMaxEvaluations(100);
 Assert.assertEquals(100, optimizer.getMaxEvaluations());
 }

 @Test
 public void testGetOptima() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);

 UnivariateRealPointValuePair[] optima = optimizer.getOptima();
 Assert.assertNull(optima);

 optimizer.optimize(mock(DummyFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
 optima = optimizer.getOptima();
 Assert.assertNotNull(optima);
 Assert.assertEquals(3, optima.length);
 }

 @Test(expected = MathIllegalStateException.class)
 public void testGetOptimaBeforeOptimize() {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);

 optimizer.getOptima();
 }

 @Test
 public void testOptimizeMinimum() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenReturn(Math.sin(anyDouble()));
 when(f.getMinimum()).thenReturn(0.0);
 when(f.getMinimumNumber()).thenReturn(1);
 when(optimizer.optimizer.optimize(f, GoalType.MINIMIZE, anyDouble(), anyDouble()))
         .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));

 UnivariateRealPointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);

 Assert.assertEquals(0.0, result.getX(), 1e-8);
 Assert.assertEquals(0.0, result.getValue(), 1e-8);

 verify(optimizer.optimizer, times(3)).optimize(eq(f), eq(GoalType.MINIMIZE), anyDouble(), anyDouble());
 }

 @Test
 public void testOptimizeMaximum() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenReturn(Math.sin(anyDouble()) * -1.0);
 when(f.getMaximum()).thenReturn(0.0);
 when(f.getMaximumNumber()).thenReturn(1);
 when(optimizer.optimizer.optimize(f, GoalType.MAXIMIZE, anyDouble(), anyDouble()))
         .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));

 UnivariateRealPointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0);

 Assert.assertEquals(0.0, result.getX(), 1e-8);
 Assert.assertEquals(-0.0, result.getValue(), 1e-8);

 verify(optimizer.optimizer, times(3)).optimize(eq(f), eq(GoalType.MAXIMIZE), anyDouble(), anyDouble());
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testOptimizeConvergenceException() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenThrow(new ConvergenceException());

 try {
 optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
 fail();
 } catch (ConvergenceException e) {
 // expected
 }

 verify(optimizer.optimizer, times(3)).optimize(eq(f), eq(GoalType.MINIMIZE), anyDouble(), anyDouble());
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenThrow(new FunctionEvaluationException("Test error message"));

 try {
 optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
 fail();
 } catch (FunctionEvaluationException e) {
 // expected
 Assert.assertEquals("Test error message", e.getMessage());
 }

 verify(optimizer.optimizer, times(3)).optimize(eq(f), eq(GoalType.MINIMIZE), anyDouble(), anyDouble());
 }

 @Test(expected = MathIllegalStateException.class)
 public void testOptimizeWithInvalidGoalType() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenReturn(Math.sin(anyDouble()));
 when(optimizer.optimizer.optimize(f, anyInt(), anyDouble(), anyDouble()))
         .thenThrow(new ConvergenceException());

 try {
 optimizer.optimize(f, GoalType.OPTIMIZE_EITHER, 0.0, 1.0);
 fail();
 } catch (MathIllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testOptimizeWithInvalidGoalType1() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenReturn(Math.sin(anyDouble()));
 when(optimizer.optimizer.optimize(f, anyInt(), anyDouble(), anyDouble()))
         .thenThrow(new ConvergenceException());

 try {
 optimizer.optimize(f, GoalType.OPTIMIZE_EITHER, 0.0, 1.0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = MathIllegalStateException.class)
 public void testOptimizeWithInvalidMinMaxValues() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenReturn(Math.sin(anyDouble()));
 when(optimizer.optimizer.optimize(f, eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
         .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));
 when(optimizer.optimizer.optimize(f, eq(GoalType.MAXIMIZE), anyDouble(), anyDouble()))
         .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));

 try {
 optimizer.optimize(f, GoalType.MINIMIZE, Double.NaN, Double.NaN);
 fail();
 } catch (MathIllegalStateException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptimizeWithInvalidMinMaxValues1() throws FunctionEvaluationException {
 MultiStartUnivariateRealOptimizer<DummyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
 optimizerMock, 3, randomGeneratorMock);
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(f.value(anyDouble())).thenReturn(Math.sin(anyDouble()));
 when(optimizer.optimizer.optimize(f, eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
         .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));
 when(optimizer.optimizer.optimize(f, eq(GoalType.MAXIMIZE), anyDouble(), anyDouble()))
         .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));

 try {
 optimizer.optimize(f, GoalType.MINIMIZE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}