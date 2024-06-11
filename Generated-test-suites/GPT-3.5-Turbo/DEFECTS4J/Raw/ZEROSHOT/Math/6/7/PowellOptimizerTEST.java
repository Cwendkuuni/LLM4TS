import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

  @Test
  public void testConstructor1() {
    double rel = 0.1;
    double abs = 0.01;
    ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
      public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
        return iterations > 100;
      }
    };

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);

    assertThat(optimizer.getRelativeThreshold(), is(rel));
    assertThat(optimizer.getAbsoluteThreshold(), is(abs));
    assertThat(optimizer.getConvergenceChecker(), is(checker));
  }

  @Test
  public void testConstructor2() {
    double rel = 0.1;
    double abs = 0.01;
    double lineRel = 0.001;
    double lineAbs = 0.0001;
    ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
      public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
        return iterations > 100;
      }
    };

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs, checker);

    assertThat(optimizer.getRelativeThreshold(), is(rel));
    assertThat(optimizer.getAbsoluteThreshold(), is(abs));
    assertThat(optimizer.getLine().getRelativeThreshold(), is(lineRel));
    assertThat(optimizer.getLine().getAbsoluteThreshold(), is(lineAbs));
    assertThat(optimizer.getConvergenceChecker(), is(checker));
  }

  @Test
  public void testConstructor3() {
    double rel = 0.1;
    double abs = 0.01;

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

    assertThat(optimizer.getRelativeThreshold(), is(rel));
    assertThat(optimizer.getAbsoluteThreshold(), is(abs));
    assertThat(optimizer.getConvergenceChecker(), is(null));
  }

  @Test
  public void testConstructor4() {
    double rel = 0.1;
    double abs = 0.01;
    double lineRel = 0.001;
    double lineAbs = 0.0001;

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs);

    assertThat(optimizer.getRelativeThreshold(), is(rel));
    assertThat(optimizer.getAbsoluteThreshold(), is(abs));
    assertThat(optimizer.getLine().getRelativeThreshold(), is(lineRel));
    assertThat(optimizer.getLine().getAbsoluteThreshold(), is(lineAbs));
    assertThat(optimizer.getConvergenceChecker(), is(null));
  }

  @Test
  public void testDoOptimize() {
    PowellOptimizer optimizer = new PowellOptimizer(0.1, 0.01);

    PointValuePair solution = optimizer.doOptimize();

    assertNotNull(solution);
  }

}